package com.ad.android.ridesystems.passengercounter.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ad.android.ridesystems.passengercounter.common.Config;
import com.ad.android.ridesystems.passengercounter.common.InAppDebug;
import com.ad.android.ridesystems.passengercounter.common.Utils;
import com.ad.android.ridesystems.passengercounter.model.entities.Employee;
import com.ad.android.ridesystems.passengercounter.model.entities.Route;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstance;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstanceDetail;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteStop;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteTrackingCriteria;
import com.ad.android.ridesystems.passengercounter.model.entities.TrackingLevel;
import com.ad.android.ridesystems.passengercounter.model.entities.Vehicle;
import com.ad.android.ridesystems.passengercounter.model.entities.VehicleRoute;
import com.ad.android.ridesystems.passengercounter.model.logic.ScheduleManager;
import com.ad.android.ridesystems.passengercounter.model.vo.SyncVO;
import com.ad.android.ridesystems.passengercounter.model.vo.schedule.AgencySchedule;
import com.ad.android.ridesystems.passengercounter.services.IRideWCFServiceClient;

/**
 * Native implementation of WS, because of bad  soap   
 *
 */
public class RideWCFServiceNativeImpl implements IRideWCFServiceClient {
	/** method to get employees and vehicles */
	public static final String GET_HANDHELD_EMPLOYEES_AND_VEHICLES = "GetHandheldEmployeesAndVehicles";
	/** method to get routes */
	public static final String GET_HANDHELD_ROTUES = "GetHandheldRoutes";	
	/** method to save routes */
	public static final String SAVE_ROUTE_INSTANCES = "SaveRouteInstances";
	/** method to save vehicle */
	public static final String SAVE_VEHICLE_ROUTE = "SaveVehicleRoute";
	/** method to get schedules */
	public static final String GET_ROUTE_SCHEDULES = "GetRouteSchedules";
	/** list of clients url */
	public static final String GET_CLIENT_URL = "http://www.ridesystems.net/RideAdmin/Services/MobileClientService.svc/JSON/GetClients";
	
	/**
	 * Doing request for employees and vehicles
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public SyncVO getAllEmployeesAndVehicles() {
		SyncVO syncVO = new SyncVO();
		String xml = doSOAPRequest(this.getSoapUrlByMethod(GET_HANDHELD_EMPLOYEES_AND_VEHICLES), this.getAllEmployeesAndRoutesRequest(0, 0));
		Log.i("getAllEmployeesAndVehicles", xml + "");
		if (xml != null) {  
			try {			
				xml = Utils.cleanNamespaces(xml);
				SAXReader reader = new SAXReader();					    // dom4j SAXReader		
				Document document = reader.read(new StringReader(xml)); // dom4j Document

				List employees = document.selectNodes("//Employee");
				List vehicles = document.selectNodes("//Vehicle");			

				for (Object object : employees) {
					if (object instanceof Element) {
						Employee emp = Employee.fromDOM((Element)object);
						syncVO.getEmployees().add(emp);
					}				
				}
				for (Object object : vehicles) {
					if (object instanceof Element) {
						Vehicle vechicle = Vehicle.fromDOM((Element)object);
						syncVO.getVehicles().add(vechicle);
					}				
				}
			} catch (DocumentException e) {
				Log.e("RideWCF", "WS result xml error");
			}
		}

		return syncVO;
	}

	/**
	 *  Doing request for Routes and TrackingLevels
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public SyncVO getAllRoutesAndTrackingLevels() {

		SyncVO syncVO = new SyncVO();
		String xml = doSOAPRequest(this.getSoapUrlByMethod(GET_HANDHELD_ROTUES), this.getAllRoutesRequest(0, 0));
		if (xml != null) {
		try {						
			xml = Utils.cleanNamespaces(xml);
			Log.i("getAllRoutesAndTrackingLevels", xml);
			SAXReader reader = new SAXReader(); // dom4j SAXReader		
			Document document = reader.read(new StringReader(xml)); // dom4j Document
			
			List routesNodes = document.selectNodes("//Route");							
			for (Object object : routesNodes) {
				if (object instanceof Element) {
					Element el = (Element)object;
					Route route = Route.fromDOM(el);
					syncVO.getRoutes().add(route);
					// set attribute id for Route tag in order to increase xPath fetching
					el.addAttribute("id", route.getRouteId() + "");					
					List stopNodes = el.selectNodes("//Route[@id='" + route.getRouteId() + "']//RouteStop");										
					for (Object stopObj : stopNodes) {						
						if (object instanceof Element) {
							Element stopEl = (Element) stopObj;							
							RouteStop stop = RouteStop.fromDOM(stopEl);
							route.getStops().add(stop);
						}
					}
				}			
			}	

			List levelsNodes = document.selectNodes("//TrackingLevel");
			for (Object object : levelsNodes) {
				if (object instanceof Element) {
					Element el = (Element)object;
					TrackingLevel level = TrackingLevel.fromDOM(el);
					syncVO.getTrackingLevels().add(level);
					// set attribute id for TrackingLevel tag in order to increase xPath fetching
					String id = "tr_" + level.getTrackingLevelId();
					el.addAttribute("id", id);					
					List criteriaNodes = el.selectNodes("//TrackingLevel[@id='" + id + "']//RouteTrackingCriteria");										
					for (Object criteriaObj : criteriaNodes) {						
						if (object instanceof Element) {
							RouteTrackingCriteria criteria = RouteTrackingCriteria.fromDOM((Element) criteriaObj);
							level.getCriterias().add(criteria);
						}
					}
				}			
			}	

		} catch (DocumentException e) {
			Log.e("RideWCF", "WS result xml error");
		}
		}
		return syncVO;
	}
	
	
	/**
	 * Doing request for employees and vehicles
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public AgencySchedule getAllRouteSchedules() {
		Log.i("got", "getAllRouteSchedules");
		AgencySchedule schedule = null;
		String xml = doSOAPRequest(this.getSoapUrlByMethod(GET_ROUTE_SCHEDULES), this.getRouteSchedulesRequest(0, 0));		
		if (xml != null) {  
			try {			
				xml = Utils.cleanNamespaces(xml);
				
				schedule = ScheduleManager.getInstance().parse(xml);
				
				
			} catch (DocumentException e) {
				Log.e("RideWCF", "WS result xml error " + e.getMessage());
			}
		}

		return schedule;
	}
	
	//<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"><s:Body><GetRouteSchedules xmlns="http://ServiceContracts.RideSystems.net/2009/06/"><credentials xmlns:a="http://DataContracts.SmartSync.RideSystems.net/2009/06/" xmlns:i="http://www.w3.org/2001/XMLSchema-instance"><a:CompanyID>5</a:CompanyID><a:EmployeeID>1</a:EmployeeID><a:Token>USU1</a:Token></credentials><request xmlns:a="http://DataContracts.RideSystems.net/2009/06/" xmlns:i="http://www.w3.org/2001/XMLSchema-instance"><a:Date>0001-01-01T00:00:00</a:Date><a:RequestType>FullSync</a:RequestType></request></GetRouteSchedules></s:Body></s:Envelope>
	
	

	

	
	/**
	 * Sync collected data with server 
	 */
	@Override
	public boolean saveRouteInstance(RouteInstance ri) {		
		Log.d("Service", "send " + this.getAllRouteInstanceRequest(0, 0, ri));
		String xml = doSOAPRequest(this.getSoapUrlByMethod(SAVE_ROUTE_INSTANCES), this.getAllRouteInstanceRequest(0, 0, ri));
		if (xml == null) return false;				
		Log.d("WS response", xml);		
		return true;
	}
	
	/**
	 * Set vehicle status
	 */
	@Override
	public boolean saveVehicleRoute(VehicleRoute vr) {
		Log.d("Service", "send " + this.getAllVehicleRouteRequest(0, 0, vr));
		String xml = doSOAPRequest(this.getSoapUrlByMethod(SAVE_VEHICLE_ROUTE), this.getAllVehicleRouteRequest(0, 0, vr));
		if (xml == null) return false;				
		Log.d("WS response", xml);		
		return true;
	}	


	/**
	 * Get soap actions
	 * @param str method name
	 * @return url
	 */
	private String getSoapUrlByMethod(String str) {
		if (GET_HANDHELD_EMPLOYEES_AND_VEHICLES.equals(str)) 					
			return "http://ServiceContracts.RideSystems.net/2009/06/ISmartSyncServiceContract/GetHandheldEmployeesAndVehicles";
		if (GET_HANDHELD_ROTUES.equals(str)) 
			return "http://ServiceContracts.RideSystems.net/2009/06/ISmartSyncServiceContract/GetHandheldRoutes";
		if (SAVE_ROUTE_INSTANCES.equals(str)) 
			return "http://ServiceContracts.RideSystems.net/2009/06/ISmartSyncServiceContract/SaveRouteInstances";
		if (SAVE_VEHICLE_ROUTE.equals(str)) 
			return "http://ServiceContracts.RideSystems.net/2009/06/ISmartSyncServiceContract/SaveVehicleRoute";
		if (GET_ROUTE_SCHEDULES.equals(str)) 
			return "http://ServiceContracts.RideSystems.net/2009/06/ISmartSyncServiceContract/GetRouteSchedules";
		return null;		
	}

	/**
	 * GET_HANDHELD_EMPLOYEES_AND_VEHICLES request xml body
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	private String getAllEmployeesAndRoutesRequest(int companyId, int employeeId) {
		String command = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
				"<s:Body><GetHandheldEmployeesAndVehicles xmlns=\"http://ServiceContracts.RideSystems.net/2009/06/\">" +
				"<request xmlns:a=\"http://DataContracts.RideSystems.net/2009/06/\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<a:Request>" +
				"<a:Date>0001-01-01T00:00:00</a:Date>" +
				"<a:RequestType>FullSync</a:RequestType>" +
				"</a:Request>" +
				"<a:RideCredential xmlns:b=\"http://DataContracts.SmartSync.RideSystems.net/2009/06/\">" +
				"<b:CompanyID>0</b:CompanyID>" +
				"<b:EmployeeID>0</b:EmployeeID>" +
				"<b:Token>" + Config.getInstance().gebWebServiceToken() + "</b:Token>" +
				"</a:RideCredential>" +
				"<a:HistoricalDays>0</a:HistoricalDays>" +
				"</request>" +
				"</GetHandheldEmployeesAndVehicles>" +
				"</s:Body>" +
				"</s:Envelope>";
	
		return command;
	}

	/**
	 * GET_HANDHELD_ROTUES request xml body
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	private String getAllRoutesRequest(int companyId, int employeeId) {
		String command = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
		"<s:Body>" +
		"<GetHandheldRoutes xmlns=\"http://ServiceContracts.RideSystems.net/2009/06/\">" +
		"<request xmlns:a=\"http://DataContracts.RideSystems.net/2009/06/\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
		"<a:Request>" +
		"<a:Date>0001-01-01T00:00:00</a:Date>" +
		"<a:RequestType>FullSync</a:RequestType>" +
		"</a:Request>" +
		"<a:RideCredential xmlns:b=\"http://DataContracts.SmartSync.RideSystems.net/2009/06/\">" +
		"<b:CompanyID>" + companyId + "</b:CompanyID>" +
		"<b:EmployeeID>" + employeeId + "</b:EmployeeID>" +
		"<b:Token>" + Config.getInstance().gebWebServiceToken() + "</b:Token>" +
		"</a:RideCredential>" +
		"<a:SyncComments>false</a:SyncComments>" +
		"<a:SyncLandmarks>false</a:SyncLandmarks>" +
		"<a:SyncMapPoints>false</a:SyncMapPoints>" +
		"</request></GetHandheldRoutes>" +
		"</s:Body>" +
		"</s:Envelope>";
		return command;
	}

	/**
	 * get SAVE_ROUTE_INSTANCES update soap request body 
	 * @param companyId
	 * @param employeeId
	 * @param ri
	 * @return
	 */
	private String getAllRouteInstanceRequest(int companyId, int employeeId, RouteInstance ri) {
		String command = "";
		command = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
		"<s:Body><SaveRouteInstances xmlns=\"http://ServiceContracts.RideSystems.net/2009/06/\">" +
		"<request xmlns:a=\"http://DataContracts.RideSystems.net/2009/06/\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
		"<a:RideCredentials xmlns:b=\"http://DataContracts.SmartSync.RideSystems.net/2009/06/\">" +
		"<b:CompanyID>" + companyId + "</b:CompanyID>" +
		"<b:EmployeeID>" + employeeId + "</b:EmployeeID>" +
		"<b:Token>" + Config.getInstance().gebWebServiceToken() + "</b:Token>" +
		"</a:RideCredentials>" +
		"<a:GPSGateUser i:nil=\"true\"/>" +
		"<a:IMEINumber>" + Config.getInstance().getImei() + "</a:IMEINumber>" +
		"<a:RouteInstances>" +
		getRouteInstanceSOAPBody(ri) +
		"</a:RouteInstances>" +
		"</request>" +
		"</SaveRouteInstances>" +
		"</s:Body>" +
		"</s:Envelope>";

		return command;
	}

	/**
	 * get RouteInstanceDetail xml for SAVE_ROUTE_INSTANCES update soap request body
	 * @param ri
	 * @return
	 */
	private String getRouteInstanceDetailSOAPBody(RouteInstanceDetail ri) {
		String command = "";
		command = "<a:RouteInstanceDetail>" +
		"<a:UniqueID>00000000-0000-0000-0000-000000000000</a:UniqueID>" +
		"<a:Quantity>" + ri.getQuantity() + "</a:Quantity>" +
		"<a:RouteDate>" + ri.getRouteDate() + "</a:RouteDate>" +
		"<a:RouteInstanceID>0</a:RouteInstanceID>" +
		"<a:RouteStopID>" + ri.getRouteStopId() + "</a:RouteStopID>" +
		"<a:RouteTrackingCriteriaID>" + ri.getRouteTrackingCriteriaId() + "</a:RouteTrackingCriteriaID>" +
		"</a:RouteInstanceDetail>";
		return command;
	}

	/**
	  * get RouteInstance xml for SAVE_ROUTE_INSTANCES update soap request body
	 * @param ri
	 * @return
	 */
	private String getRouteInstanceSOAPBody(RouteInstance ri) {
		String details = "";
		for (RouteInstanceDetail rid : ri.getDetails()) {
			details += getRouteInstanceDetailSOAPBody(rid);
		}
		String command = "";
		command = "<a:RouteInstance>" +
		"<a:UniqueID>00000000-0000-0000-0000-000000000001</a:UniqueID>" +
		"<a:Details>" +
		details +
		"</a:Details>" +
		"<a:PersonID>" + ri.getEmployeeId() + "</a:PersonID>" +
		"<a:RouteDate>" + ri.getRouteDate() + "</a:RouteDate>" +
		"<a:RouteID>" + ri.getRouteId() + "</a:RouteID>" +
		"<a:RouteInstanceID>0</a:RouteInstanceID>" +
		"<a:VehicleID>" + ri.getVehicleId() + "</a:VehicleID>" +
		"</a:RouteInstance>";
		return command;
	}
	
	/**
	 * 
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	private String getRouteSchedulesRequest(int companyId, int employeeId) {
		return "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
				"<s:Body>" +
					"<GetRouteSchedules xmlns=\"http://ServiceContracts.RideSystems.net/2009/06/\">" +
						"<credentials xmlns:a=\"http://DataContracts.SmartSync.RideSystems.net/2009/06/\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
							"<a:CompanyID>" + companyId + "</a:CompanyID>" +
							"<a:EmployeeID>" + employeeId + "</a:EmployeeID>" +
							"<a:Token>" + Config.getInstance().gebWebServiceToken() +  "</a:Token>" +
						"</credentials>" +
						"<request xmlns:a=\"http://DataContracts.RideSystems.net/2009/06/\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
							"<a:Date>0001-01-01T00:00:00</a:Date>" +
							"<a:RequestType>FullSync" +
							"</a:RequestType>" +
						"</request>" +
					"</GetRouteSchedules>" +
				"</s:Body>" +
			"</s:Envelope>";		
	}



	private String getAllVehicleRouteRequest(int companyId, int employeeId, VehicleRoute vr) {
		return "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<s:Body>" +
				"<SaveVehicleRoute xmlns=\"http://ServiceContracts.RideSystems.net/2009/06/\">" +
					"<credentials xmlns:a=\"http://DataContracts.SmartSync.RideSystems.net/2009/06/\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
						"<a:CompanyID>" + companyId + "</a:CompanyID>" +
						"<a:EmployeeID>" + employeeId + "</a:EmployeeID>" +
						"<a:Token>" + Config.getInstance().gebWebServiceToken() +  "</a:Token>" +
					"</credentials>" +
					"<vehicleRoute xmlns:a=\"http://DataContracts.RideSystems.net/2009/06/\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
						"<a:UniqueID>00000000-0000-0000-0000-000000000000</a:UniqueID>" +
						"<a:GpsGateUserName>" + Config.getInstance().getDeviceName() + "</a:GpsGateUserName>" +
						"<a:PersonID>" + vr.getPersonId() + "</a:PersonID>" +
						"<a:RouteID>" + vr.getRouteId() + "</a:RouteID>" +
						"<a:VehicleID>" + vr.getVehicleId() + "</a:VehicleID>" +
						"<a:VehicleName i:nil=\"true\"/>" +
					"</vehicleRoute>" +
				"</SaveVehicleRoute>" +
			"</s:Body>" +
		"</s:Envelope>";		
	}

	@Override
	public String getAgencyUrl() {
		String json = doJSONRequest(GET_CLIENT_URL);		
		Log.i("WS", json);
		try {
			JSONArray js = new JSONArray(json);
			for (int i = 0; i < js.length(); i++) {
				JSONObject jo = js.getJSONObject(i);
				System.out.println(jo.getString("Token"));
				if (Config.getInstance().getWebServiceToken().toLowerCase().equals(jo.getString("Token").toLowerCase())) {
					return jo.getString("WebAddress");
				}				
			}			
		} catch (JSONException e) {
			Log.e("WS paree", e.getMessage());
			return null;
		}
		return null;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public String doJSONRequest(String url) {
		int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpClient client = new DefaultHttpClient(httpParams);

		HttpGet request = new HttpGet(GET_CLIENT_URL);
		
		try {
			HttpResponse response = client.execute(request);
			if ( response != null) {
                InputStream is = response.getEntity().getContent(); //Get the data in the entity
                final char[] buffer = new char[0x10000];
                StringBuilder out = new StringBuilder();
                Reader in = new InputStreamReader(is, "UTF-8");
                int read;
                do {
                  read = in.read(buffer, 0, buffer.length);
                  if (read>0) {
                    out.append(buffer, 0, read);
                  }
                } while (read>=0);
                return out.toString();
			}
		} catch (ClientProtocolException e) {
			Log.e("WS response error", "ClientProtocolException: " + e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("WS response error", "IOException: " + e.getMessage());
			return null;
		}
		return null;		
	}
	
	/**
	 * Make soap request and return xml;
	 * @param soapAction action name
	 * @param soapBody body
	 * @return xml
	 */
	public String doSOAPRequest(String soapAction, String soapBody) {
		InputStream is = null;
		Writer writer = null;
		Reader isReader = null; 
		try {
			URL url = new URL(Config.getInstance().getWebServiceUrl());
			URLConnection conn = url.openConnection();			
			conn.setRequestProperty("Content-type", "text/xml; charset=utf-8");
			Log.i("soapAction", soapAction);
			Log.i("soapBody", soapBody);
			conn.setRequestProperty("SOAPAction", soapAction.toString().trim());			
			conn.setDoOutput(true);

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

			wr.write(soapBody);
			wr.flush();

			is =  conn.getInputStream();
			writer = new StringWriter();			

			char[] buffer = new char[1024];
			isReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			int n;
			while ((n = isReader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}

			is.close();
			isReader.close();

			String xml = writer.toString();
			Log.i("WS response sync ", xml);
			return xml;
		} catch (MalformedURLException e) {
			Log.e("WS response error", "MalformedURLException: " + e.getMessage());			
			return null;
		} catch (IOException e) {
			Log.e("WS response error", "IOException: " + e.getMessage());
			e.printStackTrace();
			return null;			
		} finally {
			// if got exception on close
			try {
				if (is != null) is.close();
				if (isReader != null) isReader.close();
				if (writer != null) writer.close();
			} catch (IOException e) {
				Log.e("WS response error",  "IOException 2: " + e.getMessage());
				return null;
			}

		}				
	}

}