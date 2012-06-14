package com.ad.android.ridesystems.passengercounter.model.entities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Element;

import android.util.Log;

import com.ad.android.ridesystems.passengercounter.common.Utils;

/**
 * 
 * Vehicle entity.  
 * Original entity comes from WebService and has next structure:
 * 
 * <?xml version="1.0"?>
 * <RouteStop>
 * 		<UniqueID>f930e5c7-b2de-43ca-bcbe-5ca39d0b5760</UniqueID>
 * 		<AddressID>138</AddressID>
 * 		<City>SLC</City>
 * 		<Latitude>40.768507</Latitude>
 * 		<Line1>Med Towers (S)</Line1>
 * 		<Line2/>
 * 		<Longitude>-111.832164</Longitude>
 * 		<State>UT</State>
 * 		<Zip>84112</Zip>
 * 		<Comments>
 * 			<Active/>
 * 			<Deleted/>
 * 		</Comments>
 * 		<Description>Med Towers (S)</Description>
 * 		<Heading>0</Heading>
 * 		<MapPoints>
 * 			<Active/>
 * 			<Deleted/>
 * 		</MapPoints>
 * 		<Order>16</Order>
 * 		<RouteID>20</RouteID>
 * 		<RouteStopID>460</RouteStopID>
 * 		<SecondsAtStop>30</SecondsAtStop>
 * 		<SecondsToNextStop>30</SecondsToNextStop>
 * 		<ShowEstimatesOnMap>true</ShowEstimatesOnMap>
 * 		<StopEvents>
 * 			<Active/>
 * 			<Deleted/>
 * 		</StopEvents>
 * 		<TextingKey/>
 * </RouteStop>	
 *
 * Android app uses only part of this   
 * 
 * 
 *
 */
public class RouteStop extends AEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3003297696617052290L;

	public static final String SQL_CREATE_TABLE = "CREATE TABLE routestop (" +
	"id INTEGER PRIMARY KEY, " +
	"uniqueId TEXT, " +
	"routeId INTEGER, " +
	"routeStopId INTEGER, " +
	"iorder INTEGER, " +
	"description TEXT," +
	"schedules TEXT" +
	");";
	

	private String uniqueId = "";

	private int routeId = 0;

	private int routeStopId = 0;

	private int order = 0;

	private String description = "";
	
	private String schedules = "";

	List<Date> depatures = null;


	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}	

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public int getRouteStopId() {
		return routeStopId;
	}

	public void setRouteStopId(int routeStopId) {
		this.routeStopId = routeStopId;
	}


	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getSchedules() {
		return schedules;
	}
	
	public List<Date> parseDepartures() {
		depatures = new ArrayList<Date>();
		SimpleDateFormat df = new SimpleDateFormat("hh:mm");
		String[] times = this.schedules.split(", ");
		
		for (int i = 0; i < times.length; i++) {
			if (times[i].length() > 0) {
				try {
					Date date = df.parse(times[i]);
					if (date != null) {
						depatures.add(date);
					}
				} catch (Exception e) {
					Log.i("parse", e.getMessage());
				}
			}
		}
		return depatures;
	}
	
	public String getNearesDepartureSeries() {
		String result = "";
		String nearest = getNearesDeparture();		
		if (nearest != null) {			
			String[] times = this.schedules.split(", ");
			int selected = -1; 
			for (int i = 0; i < times.length; i++) {
				if (times[i].equals(nearest)) {
					selected = i;
					break;
				}
			}
			if (selected > 0) result += times[selected - 1] + ", ";
			result += "##" + times[selected] +  "##, ";
			if (selected < times.length - 1) result += times[selected + 1] + ", ";;
			
			result = result.substring(0, result.length() - 2);			
		}
		
		
		return result;
	}
	
	public String getNearesDeparture() {	
		String closestStr = null;
		Date closest = null;
		if (depatures == null) {
			this.parseDepartures();
		}
				
		//long nowMills = now.getHours() * 60 * 60 * 1000 + now.getMinutes() * 60 * 1000 + now.getSeconds() * 1000;
				
		SimpleDateFormat df = new SimpleDateFormat("hh:mm");
		long nowMills;
		try {
			nowMills = df.parse(df.format(new Date())).getTime();
		} catch (ParseException e) {
			Date now = new Date();
			nowMills = now.getHours() * 60 * 60 * 1000 + now.getMinutes() * 60 * 1000;
		}
		
		long min = Long.MAX_VALUE;			
		for (Date date : depatures) {
			long diff = nowMills - date.getTime() - 1000 * 60 * 10;
			if (diff < 0) {
				min = diff;
				closest = date;	
				closestStr = df.format(closest);
				break;
			}
		}
					
		return closest != null ? closestStr : null;		
	}
	
	public void setSchedules(String schedules) {
		this.schedules = schedules;
	}

	public static RouteStop fromDOM(Element element) {
		RouteStop inst = new RouteStop();
		inst.setDescription(Utils.getElementStringValue(element, "Description"));				
		inst.setUniqueId(Utils.getElementStringValue(element, "UniqueID"));
		inst.setRouteStopId(Utils.getElementIntValue(element, "RouteStopID"));
		inst.setRouteId(Utils.getElementIntValue(element, "RouteID"));
		inst.setOrder(Utils.getElementIntValue(element, "Order"));
		return inst; 
	}

	@Override
	public String toString() {
		return "RouteStop {id: " + this.getId() + ", description:'" + this.description + "', routeId:" + routeId + ", routeStopId: " + routeStopId + " }";	
	}

}
