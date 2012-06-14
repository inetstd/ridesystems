package com.ad.android.ridesystems.passengercounter.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.common.Config;
import com.ad.android.ridesystems.passengercounter.common.InAppDebug;
import com.ad.android.ridesystems.passengercounter.model.helpers.ManagerHolder;
import com.ad.android.ridesystems.passengercounter.model.helpers.SqliteDBHelper;
import com.ad.android.ridesystems.passengercounter.model.vo.IPCDataVO;
import com.ad.android.ridesystems.passengercounter.services.SyncService;

/**
 * 
 * Base class Activity.
 * Defines basic interface methods
 *
 */
public abstract class ABaseActivity extends Activity {

	static final int SETTINGS_REQUEST = 99;
	
	/**
	 * Default content view resource.
	 */
	protected Integer mainContentView = R.layout.main;

	/**
	 * Resource layout pointer.
	 * Can be overrided by ancestor  
	 */
	protected Integer subContentView = R.layout.layout_landing;

	/**
	 * Managers holder
	 */
	private ManagerHolder managerHolder = null;
	

	/**
	 * DB instance
	 */
	private SQLiteOpenHelper db = null;

	/**
	 * 
	 */
	ConnectivityManager cm;
	
	/**
	 * datavo from intent. represents current route data AND state. Current route strored in datavo
	 */
	IPCDataVO dataVO = null;

	
	/**
	 * Base Activity. Preset main content view and sub-content view (tiles or 'template' basic implementation)   
	 * @param resource - layout resource 
	 */
	public ABaseActivity(Integer resource) {
		super();
		this.subContentView = resource;	
	}

	/**
	 * Base on create method
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);				
		setContentView(this.mainContentView);
		this.readConfig();
		// set overrided  		
		this.setUpSubContentView();		
		// invoke method to set up views 
		this.setUpViews();
		this.setUpMenu();
		this.setUpHeader();	
		
		Intent serviceIntent = new Intent(this, SyncService.class);
		startService(serviceIntent);
	}
	
	/**
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		switch (requestCode) {	
		case SETTINGS_REQUEST:									
			break;		
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * Create an singleton instance of config
	 */
	protected void readConfig() {
		Config cfg = Config.getInstance();
		
		SharedPreferences settings = getSharedPreferences(Config.C_PREFS_NAME, 0);
		
		String wsToken = settings.getString(Config.C_WS_TOKEN, Config.DEBUG ? Config.getInstance().gebWebServiceToken() :"");
		String wsUrl = settings.getString(Config.C_WS_URL, "");		
		String deviceName = settings.getString(Config.C_DEVICE_NAME, "");
		String webAddress = settings.getString(Config.C_WEB_ADDRESS, "");
		
		cfg.setWebServiceToken(wsToken);
		if (deviceName != "") cfg.setDeviceName(deviceName);
		if (wsUrl != "") cfg.setWebServiceUrl(wsUrl); // use the default if not set 				
		cfg.setWebAddress(webAddress);
		
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null) {
			cfg.setImei(telephonyManager.getDeviceId());		
		}
	}
	
	
	
	/**
	 * Sets header by activity name
	 */
	private void setUpHeader() {
		String key = getClass().getSimpleName().toLowerCase(); // key from class
		int resId = getResources().getIdentifier(key, "string", "com.ad.android.ridesystems.passengercounter"); // resource id
		TextView header = (TextView) findViewById(R.id.header_title);
		if (resId > 0 && header != null) { // if there is header
			header.setText(getResources().getText(resId));
		}		
	}

	/**
	 * Sets content view layout 
	 */
	public void setUpSubContentView() {		
		if (this.subContentView != null) { // if overrided
			LinearLayout content = (LinearLayout) findViewById(R.id.sub_content_view);
			LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);                  
			content.addView(layoutInflater.inflate(this.subContentView, null));
		}

	}	

	/**
	 * Set listeners to bottom menu buttons
	 */
	public void setUpMenu() {			
		Button menu = (Button) findViewById(R.id.menu_btn_menu);	
		if (menu != null) {
			menu.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					registerForContextMenu(v);
					openContextMenu(v);
					
				}
			});
		}
	}
	
	/**
	 * Define resource for context menu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);	  
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.menu.context_menu_short, menu);	  	
	}
	
	
	/**
	 * Context menu select handler. 
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {	 
	  switch (item.getItemId()) {	
	  case R.id.application_status:
			Intent intent = new Intent(this, ApplicationStatusActivity.class);
			startActivity(intent);
		    return true;
	  case R.id.show_map:
			Toast.makeText(this, "open browser", Toast.LENGTH_LONG).show();
		    return true;
	  case R.id.application_settings:
		  	Intent intent3 = new Intent(ABaseActivity.this, ApplicationSettingsActivity.class);			
			startActivityForResult(intent3, SETTINGS_REQUEST);
			return true;
	  case R.id.sync:
			SharedPreferences settings = getSharedPreferences(Config.C_PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(Config.C_LAST_APP_DATA_SYNC, "");
			editor.commit();
			Intent intent2 = new Intent(ABaseActivity.this, LandingActivity.class);			
			startActivity(intent2);
			return true;
	  case R.id.logout:						
		  	logout();											
			return true;
	  default:
	    return super.onContextItemSelected(item);
	  }
	}
	
	/**
	 * Logout user. Kill all activities between LandingActivities 
	 */
	public void logout() {
		// if logout happens while vehicle on route - create finish request object for WS  
		if (dataVO != null && dataVO.getEmployee() != null) {
			InAppDebug.log(getApplicationContext(), "logout person, gps: " + dataVO.getEmployee().getPersonId() + " " + Config.getInstance().getDeviceName());
			getManagerHolder().getVehicleRouteManager().createVehicleRouteOnFinishRoute(dataVO.getEmployee().getPersonId());
		}
		
		Intent intent  = new Intent(ABaseActivity.this, LandingActivity.class);
		intent.setFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP); // recreate activity  
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // kill all activities in ActivityStack after LandingActivity
	    startActivity(intent);
	}

	/**
	 * Method to setup view listeners etc.  
	 */
	public abstract void setUpViews();


	/**
	 * Get db helper
	 * @return db
	 */
	public SQLiteOpenHelper getDB() {		
		if (db == null) {
			db = new SqliteDBHelper(this);
		}		
		return db;
	}
	
	/**
	 * get manager holder instance
	 * Creates only one instance;
	 * @return
	 */
	public ManagerHolder getManagerHolder() {
		if (managerHolder == null) {
			managerHolder = new ManagerHolder(getDB());
		}
		return managerHolder;
	}

	/**
	 * Check if is Internet connected
	 * @return
	 */
	public boolean isOnline() {
		if (cm == null) {
			cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		return (cm != null) && (cm.getActiveNetworkInfo() != null) && cm.getActiveNetworkInfo().isConnectedOrConnecting();

	}

	/**
	 * Sets correct value of status
	 */
	protected void manageOnlineStatus() {
		TextView onlineStatusTextView = (TextView) findViewById(R.id.online_status);
		if (onlineStatusTextView != null) {
			if (isOnline()) {
				onlineStatusTextView.setText(R.string.status_online);
				onlineStatusTextView.setTextColor(getResources().getColor(R.color.blue));
			} else {
				onlineStatusTextView.setText(R.string.status_offline);
				onlineStatusTextView.setTextColor(getResources().getColor(R.color.red));
			}			
			onlineStatusTextView.setVisibility(View.VISIBLE);
 		}
	}
	
	/**
	 * Hide online status field
	 */
	protected void hideOnlineStatus() {
		TextView onlineStatusTextView = (TextView) findViewById(R.id.online_status);
		if (onlineStatusTextView != null) {
			onlineStatusTextView.setVisibility(View.GONE);
 		}
	}
	
	@Override
	public void onBackPressed() { 
		return;
	}
	
	/**
	 * Shortcut for log info
	 * @param msg
	 */
	public void logi(String msg) {
		Log.i(getResources().getString(R.string.app_name), msg);
	}

	/**
	 * Shortcut for log error
	 * @param msg
	 */
	public void loge(String msg) {
		Log.e(getResources().getString(R.string.app_name), msg);
	}

	/**
	 * Shortcut for log info
	 * @param msg
	 */
	public void logw(String msg) {
		Log.w(getResources().getString(R.string.app_name), msg);
	}
	
}
