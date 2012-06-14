package com.ad.android.ridesystems.passengercounter.activities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.common.Config;
import com.ad.android.ridesystems.passengercounter.common.InAppDebug;
import com.ad.android.ridesystems.passengercounter.common.Utils;
import com.ad.android.ridesystems.passengercounter.model.entities.Route;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstance;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteStop;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteTrackingCriteria;
import com.ad.android.ridesystems.passengercounter.model.entities.TrackingLevel;
import com.ad.android.ridesystems.passengercounter.model.entities.Vehicle;
import com.ad.android.ridesystems.passengercounter.model.logic.ScheduleManager;
import com.ad.android.ridesystems.passengercounter.model.vo.IPCDataVO;
import com.ad.android.ridesystems.passengercounter.model.vo.LoginScreenViewType;
import com.ad.android.ridesystems.passengercounter.model.vo.SyncVO;
import com.ad.android.ridesystems.passengercounter.model.vo.schedule.AgencySchedule;
import com.ad.android.ridesystems.passengercounter.services.IRideWCFServiceClient;
import com.ad.android.ridesystems.passengercounter.services.impl.RideWCFServiceNativeImpl;

/**
 * 
 * Application main activity.
 * Checks is data fetched ready.
 *
 */
public class LandingActivity extends ABaseActivity {

	static final int LOGIN_REQUEST = 0;

	static final int SELECT_ROUTE_REQUEST = 1;

	static final int COUNTER_REQUEST = 2;	


	/**
	 * Constructor. Pass view template. 
	 */
	public LandingActivity() {
		super(null);
		this.mainContentView = R.layout.main;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUpViews() {		
		manageOnlineStatus();
	}


	/**
	 * Set listeners to bottom menu buttons
	 */
	public void setUpMenu() {
		super.setUpMenu();
		Button back = (Button) findViewById(R.id.menu_btn_back);
		Button next = (Button) findViewById(R.id.menu_btn_next);
		Button menu = (Button) findViewById(R.id.menu_btn_menu);		

		back.setText("Exit");
		menu.setVisibility(View.GONE);

		next.setText("Settings");
		next.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LandingActivity.this, ApplicationSettingsActivity.class);
				startActivityForResult(intent, SETTINGS_REQUEST);
			}
		});


	}

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().hasExtra(IPCDataVO.KEY)) dataVO = (IPCDataVO) getIntent().getExtras().getSerializable(IPCDataVO.KEY);
		else dataVO = new IPCDataVO();
		dispatch();				
	}


	/**
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		if (data != null) this.dataVO = (IPCDataVO) data.getSerializableExtra(IPCDataVO.KEY);
		switch (requestCode) {
		case LOGIN_REQUEST: {
			if (resultCode == RESULT_OK) {
				dispatch();         
			} else {
				// exit called - means close application.
				// Landing activity is normally last activity 
				// end finishing means close app
				finish();
			}
			break;
		}
		case SELECT_ROUTE_REQUEST:						
			dispatch();											
			break;
		case COUNTER_REQUEST:
			dispatch();
			break;
		case SETTINGS_REQUEST:
			logi("SETTINGS_REQUEST in Landing Activity");
			dispatch();
			break;		
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * Checks application state (is sync, can be sync, logged)
	 */
	public void dispatch() {

		SharedPreferences settings = getSharedPreferences(Config.C_PREFS_NAME, 0);

		String lastAppDataSync = settings.getString(Config.C_LAST_APP_DATA_SYNC, "");
		//Config.C_LAST_SCHEDULE_DATA_SYNC += "a" + Math.random();
		String lastScheduleDataSync = settings.getString(Config.C_LAST_SCHEDULE_DATA_SYNC, "");

		boolean hasAppData = !lastAppDataSync.equals("");

		hasAppData = hasAppData && (getManagerHolder().getEmployeeManager().getAll().size() > 0);

		boolean needToScheduleUpdate = false;
		if (!lastScheduleDataSync.equals("")) {
			SimpleDateFormat df = new SimpleDateFormat(Config.C_DEFAULT_DATE_FORMAT);
			try {				
				Date last = df.parse(lastScheduleDataSync);
	 			
				if (new Date().getTime() - last.getTime() > Config.C_SYNC_SCHEDULE_PERIOD) {					
					needToScheduleUpdate = true;
				}
			} catch (Exception e) {
				needToScheduleUpdate = true;
			}
		} else {
			needToScheduleUpdate = true;
		}
		
		
		
		boolean logged = (dataVO.getEmployee() != null);
		boolean routeSelected = (dataVO.getRoute() != null && dataVO.getVehicle() != null);

		if (!hasAppData && !isOnline()) {
			manageOnlineStatus();
			forceCloseApplication();
		} else if (!hasAppData && isOnline()){
			forceAppDataSync();
		} else if (needToScheduleUpdate) {
			forceScheduleDataSync();
		} else if (logged && !routeSelected) {
			forceSelectRoute();
		} else if (logged && routeSelected) {
			forceCounter();
		} else { // not logged
			forceLogin();
		}
	}

	/**
	 * Intent to select route activity
	 */
	private void forceSelectRoute() {

		// if there is an route instance, that is not finished - show dialog.

		//final RouteInstance routeInstance = getManagerHolder().getRouteInstanceManager().getNotFinishedByEmployee(dataVO.getEmployee().getPersonId());
		final RouteInstance routeInstance = getManagerHolder().getRouteInstanceManager().getNotFinished();

		if (routeInstance == null) { // no not finished route. go to select route activity

			Intent intent = new Intent(LandingActivity.this, SelectRouteActivity.class);
			intent.putExtra(IPCDataVO.KEY, dataVO);
			startActivityForResult(intent, SELECT_ROUTE_REQUEST);			

		} else { // show dialog

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.landing_activity_has_not_finished_route_instance)
			.setCancelable(false)
			//Continue
			.setPositiveButton(R.string.landing_activity_has_not_finished_route_instance_yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();					

					Route route = getManagerHolder().getRouteManager().getByRouteId(routeInstance.getRouteId());
					Vehicle vehicle = getManagerHolder().getVehicleManager().getByVehicleId(routeInstance.getVehicleId());

					if (route != null) dataVO.setRoute(route);
					if (vehicle != null) dataVO.setVehicle(vehicle);
					dataVO.setRouteInstance(routeInstance);

					dispatch();							
				}
			})
			//Delete and start new
			.setNegativeButton(R.string.landing_activity_has_not_finished_route_instance_no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

					// getManagerHolder().getRouteInstanceManager().delete(routeInstance.getId());
					// new logic. send partial route instance 
					routeInstance.setFinished(true);
					getManagerHolder().getRouteInstanceManager().update(routeInstance);

					dialog.dismiss();
					Intent intent = new Intent(LandingActivity.this, SelectRouteActivity.class);
					dataVO.setRouteInstance(null);
					intent.putExtra(IPCDataVO.KEY, dataVO);
					startActivityForResult(intent, SELECT_ROUTE_REQUEST);
				}
			});	    	
			AlertDialog alert = builder.create();
			alert.show();

		}
	}

	/**
	 * Intent to login activity
	 */
	private void forceLogin() {
		Intent intent = new Intent(LandingActivity.this, LoginActivity.class);		
		intent.putExtra(IPCDataVO.KEY, dataVO);
		startActivityForResult(intent, LOGIN_REQUEST);
	}

	/**
	 * Start background service 
	 */
	private void forceAppDataSync() {	
		new SyncAppDataTask().execute();
		SharedPreferences settings = getSharedPreferences(Config.C_PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(Config.C_LAST_SCHEDULE_DATA_SYNC, "");
		
		editor.commit();
	}

	/**
	 * Start background service 
	 */
	private void forceScheduleDataSync() {	
		new SyncScheduleDataTask().execute();
		
	}

	/**
	 * Start counter service
	 */
	private void forceCounter() {	
		Intent intent = new Intent(LandingActivity.this, CounterActivity.class);    				
		// if we resuming previous started route - we have dataVO.getRouteInstance 
		if (dataVO.getRouteInstance() == null) { // if not - create
			RouteInstance routeInstance = new RouteInstance();
			routeInstance.setRouteId(dataVO.getRoute().getRouteId());
			routeInstance.setEmployeeId(dataVO.getEmployee().getPersonId());
			routeInstance.setVehicleId(dataVO.getVehicle().getVehicleId());
			routeInstance.setFinished(false);
			routeInstance.setRouteDate(Utils.getDotnetDate(new Date()));
			getManagerHolder().getRouteInstanceManager().insert(routeInstance);
			dataVO.setRouteInstance(routeInstance);					
		}


		// vehicleRoute will be send to WS, means that vehicle is on a route
		// Note. it happens on resume route too. What will happens if user change PersonID, and resume 
		// route on this vehicle?? Hope server is handling this logic correctly
		// saving into local storage. Background service will send it to WS
		InAppDebug.log(getApplicationContext(), "create VR {v,r,p}: " + dataVO.getVehicle().getVehicleId() + ", " + dataVO.getRoute().getRouteId() + ", " + dataVO.getEmployee().getPersonId());
		getManagerHolder().getVehicleRouteManager().
		createVehicleRouteOnStartRoute(
				dataVO.getVehicle().getVehicleId(),
				dataVO.getRoute().getRouteId(),
				dataVO.getEmployee().getPersonId());

		intent.putExtra(IPCDataVO.KEY, dataVO);
		//TODO check if there is stop in a route!!!
		startActivityForResult(intent, COUNTER_REQUEST);
	}

	/**
	 * Close application - internet connection is required
	 */
	private void forceCloseApplication() {
		logi("Has no intentet connection and no account data...");
		// cannot start app
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Application cannot complete your request at this time. Please try again later")
		.setCancelable(false)
		.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				new CheckInteretConnectionTask().execute();				
			}
		}).setNeutralButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent(LandingActivity.this, ApplicationSettingsActivity.class);
				startActivityForResult(intent, SETTINGS_REQUEST);
			}
		}).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				LandingActivity.this.finish();
			}
		});	    	
		AlertDialog alert = builder.create();
		alert.show();

	}

	/**
	 * Task to check Internet connection. Wait several time.
	 * TODO move to wireless setup screen  
	 * @author victor-note
	 *
	 */
	public class CheckInteretConnectionTask extends AsyncTask<String, Long, Void> {
		private final ProgressDialog dialog = new ProgressDialog(LandingActivity.this);
		protected void onPreExecute() {
			this.dialog.setMessage("Checkin internet connection...");
			this.dialog.show();
		}
		@Override
		protected Void doInBackground(String... params) {
			for (int i = 0; i < 4; i++ ) {
				try {
					Thread.sleep(400);					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		protected void onPostExecute(final Void unused) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();				
			}
			manageOnlineStatus();
			dispatch();
		}
	}



	public class SyncScheduleDataTask extends AsyncTask<String, String, Boolean> {
		private final ProgressDialog dialog = new ProgressDialog(LandingActivity.this);

		@Override
		protected void onPreExecute() {
			this.dialog.setTitle("Communicating with server. Please wait...");
			this.dialog.setCancelable(false);			
			this.dialog.show();	
		}

		@Override
		protected Boolean doInBackground(String... params) {
			InAppDebug.log(LandingActivity.this, "Sync schedules");
			publishProgress("Fetching schedule");
			IRideWCFServiceClient client = new RideWCFServiceNativeImpl();
			publishProgress("Creating schedule");
			AgencySchedule as = client.getAllRouteSchedules();

			publishProgress("Saving schedule");
			if (as != null) {
				//ScheduleManager.getInstance().saveAgencySchedule(LandingActivity.this, as);
				getManagerHolder().getRouteStopManager().updateSchedules(as);
				return true;
			}			
			return false;
		}

		/**
		 * Update message of progress dialog
		 */
		@Override
		protected void onProgressUpdate(String... msg) {
			if (msg.length > 0 && msg[0].length() > 0) {
				this.dialog.setMessage(msg[0]);
			}
		}

		/**
		 * check result and update last sync date 
		 */
		protected void onPostExecute(final Boolean result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();				
			}
			if (result) {
				SharedPreferences settings = getSharedPreferences(Config.C_PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString(Config.C_LAST_SCHEDULE_DATA_SYNC, new SimpleDateFormat(Config.C_DEFAULT_DATE_FORMAT).format(new Date()).toString());
				editor.commit();
				dispatch();
			} else {							
				forceCloseApplication();
			}

		}

	}

	/**
	 * 
	 * Asynchronous task that fetches application data from WS.
	 * 
	 * @author Victor Melnik (Itirra - www.itirra.com)
	 *
	 */
	public class SyncAppDataTask extends AsyncTask<String, String, Boolean> {
		private final ProgressDialog dialog = new ProgressDialog(LandingActivity.this);

		/**
		 * Show progress dialog
		 */
		protected void onPreExecute() {						
			this.dialog.setTitle("Communicating with server. Please wait...");
			this.dialog.setCancelable(false);			
			this.dialog.show();			
		}

		/**
		 * Update message of progress dialog
		 */
		@Override
		protected void onProgressUpdate(String... msg) {
			if (msg.length > 0 && msg[0].length() > 0) {
				this.dialog.setMessage(msg[0]);
			}
		}


		/**
		 * fetching in background
		 */
		@Override
		protected Boolean doInBackground(String... params) {		

			IRideWCFServiceClient client = new RideWCFServiceNativeImpl();

			publishProgress("Fetching employees and vehicles");

			// Get employees and vehicles
			SyncVO employeesAndVehicles = client.getAllEmployeesAndVehicles();				

			publishProgress("Fetching routes and tracking levels");
			// Get routes
			SyncVO routesAndLevels = client.getAllRoutesAndTrackingLevels();


			publishProgress("Saving employees");
			// Sync employees in local storage			
			getManagerHolder().getEmployeeManager().syncAll(employeesAndVehicles.getEmployees());			

			publishProgress("Saving vehicles");
			// Sync vehicles in local storage
			getManagerHolder().getVehicleManager().syncAll(employeesAndVehicles.getVehicles());

			// Get all stops from routes
			List<RouteStop> stops = new ArrayList<RouteStop>();			
			for (Route route : routesAndLevels.getRoutes()) {
				stops.addAll(route.getStops());
			}			

			// Get all stops from routes
			List<RouteTrackingCriteria> criterias = new ArrayList<RouteTrackingCriteria>();			
			for (TrackingLevel level : routesAndLevels.getTrackingLevels()) {
				criterias.addAll(level.getCriterias());
			}

			publishProgress("Saving routes");
			// Sync routes in local storage		
			getManagerHolder().getRouteManager().syncAll(routesAndLevels.getRoutes());								

			publishProgress("Saving routes stops");
			// Sync stops in local storage	
			getManagerHolder().getRouteStopManager().syncAll(stops);

			publishProgress("Saving tracking levels");
			// Sync stops in local storage
			getManagerHolder().getTrackingLevelManager().syncAll(routesAndLevels.getTrackingLevels());

			publishProgress("Saving route tracking creterias");
			// Sync stops in local storage
			getManagerHolder().getCriteriaManager().syncAll(criterias);


			publishProgress("Getting client map url");
			String webAddress = client.getAgencyUrl();
			SharedPreferences settings = getSharedPreferences(Config.C_PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(Config.C_WEB_ADDRESS, webAddress);			
			editor.commit();


			// has full data to work e.g. routes, employees, tracking levels etc.
			boolean hasDataToWork = (employeesAndVehicles.getEmployees().size() > 0 && employeesAndVehicles.getVehicles().size() > 0 
					&& routesAndLevels.getRoutes().size() > 0 && routesAndLevels.getTrackingLevels().size() > 0); 
			InAppDebug.log(LandingActivity.this, "sycned emplpyess " + employeesAndVehicles.getEmployees().size());
			InAppDebug.log(LandingActivity.this, "sycned vehicles  " + employeesAndVehicles.getVehicles().size());
			InAppDebug.log(LandingActivity.this, "sycned routes    " + routesAndLevels.getRoutes().size());
			InAppDebug.log(LandingActivity.this, "sycned levels    " + routesAndLevels.getTrackingLevels().size());
			return hasDataToWork;			
		}

		/**
		 * check result and update last sync date 
		 */
		protected void onPostExecute(final Boolean result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();				
			}
			if (result) {
				SharedPreferences settings = getSharedPreferences(Config.C_PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString(Config.C_LAST_APP_DATA_SYNC, new SimpleDateFormat(Config.C_DEFAULT_DATE_FORMAT).format(new Date()).toString());
				editor.commit();
				dispatch();
			} else {					
				forceCloseApplication();
			}

		}
	}

}