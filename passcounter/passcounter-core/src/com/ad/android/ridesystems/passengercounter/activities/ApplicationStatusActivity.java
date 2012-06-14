package com.ad.android.ridesystems.passengercounter.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.common.Config;

/**
 * App satatus screen.
 *
 */
public class ApplicationStatusActivity extends ABaseActivity {

	/**
	 * Application status field
	 */
	TextView internetStatus = null;
	
	/**
	 * Server status field
	 */
	TextView serverStatus = null;
	
	/**
	 * Last full sync
	 */
	TextView lastAppDataSync = null;
	
	/**
	 * Last partial sync
	 */
	TextView lastTrackingSync = null;
	
	/**
	 * Constructor - sets status view into layout 
	 */
	public ApplicationStatusActivity() {
		super(R.layout.layout_application_status);
	}

	
	@Override
	public void setUpViews() {
		internetStatus = (TextView) findViewById(R.id.internet_status);
		serverStatus = (TextView) findViewById(R.id.server_status);
		lastAppDataSync = (TextView) findViewById(R.id.last_application_data_sync);
		lastTrackingSync = (TextView) findViewById(R.id.last_tracking_sync);
		
		if (isOnline()) {
			internetStatus.setText(R.string.application_status_internet_status_online);
			serverStatus.setText(R.string.application_status_server_status_connected);
		} else {
			internetStatus.setText(R.string.application_status_internet_status_offline);
			serverStatus.setText(R.string.application_status_server_status_disconnected);
		}
		
		
		SharedPreferences settings = getSharedPreferences(Config.C_PREFS_NAME, 0);
		
		String appDataSyncDate = settings.getString(Config.C_LAST_APP_DATA_SYNC, "");
		String localDataSyncDate = settings.getString(Config.C_LAST_LOCAL_DATA_SYNC, "");
				
		SimpleDateFormat storedFormat = new SimpleDateFormat(Config.C_DEFAULT_DATE_FORMAT);		
		SimpleDateFormat outputFormat = new SimpleDateFormat(getResources().getString(R.string.application_status_date_format));
		
		// prepare date of application data last sync
		if (appDataSyncDate.equals("")) {
			lastAppDataSync.setText(R.string.application_status_never_sync);	
		} else {				
	        try {	    
				Date date = storedFormat.parse(appDataSyncDate);			
				lastAppDataSync.setText(outputFormat.format(date));								
			} catch (ParseException e) {				
				lastAppDataSync.setText(R.string.application_status_never_sync);
			}	        
		}

		// prepare date of local data last sync
		if (localDataSyncDate.equals("")) {
			lastTrackingSync.setText(R.string.application_status_never_sync);	
		} else {				
	        try {	    
				Date date = storedFormat.parse(localDataSyncDate);			
				lastTrackingSync.setText(outputFormat.format(date));								
			} catch (ParseException e) {
				lastTrackingSync.setText(R.string.application_status_never_sync);
			}	        
		}
		
		
		
		
		
		
		
	}
	
	/**
	 * Override bottom menu.
	 */
	@Override
	public void setUpMenu() {
		super.setUpMenu();
		
		Button back = (Button) findViewById(R.id.menu_btn_back);
		Button next = (Button) findViewById(R.id.menu_btn_next);
		
		if (next != null) next.setVisibility(View.GONE); // we don't need this button
		if (back != null) {			
			back.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					finish();
					
				}
			});
		}	
	}

}
