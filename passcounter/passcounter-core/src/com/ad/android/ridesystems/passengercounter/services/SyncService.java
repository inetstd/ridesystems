package com.ad.android.ridesystems.passengercounter.services;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.ad.android.ridesystems.passengercounter.common.Config;
import com.ad.android.ridesystems.passengercounter.common.InAppDebug;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstance;
import com.ad.android.ridesystems.passengercounter.model.entities.VehicleRoute;
import com.ad.android.ridesystems.passengercounter.model.helpers.ManagerHolder;
import com.ad.android.ridesystems.passengercounter.model.helpers.SqliteDBHelper;
import com.ad.android.ridesystems.passengercounter.services.impl.RideWCFServiceNativeImpl;

/**
 * Sync service.
 * Working in background. 
 * Runs thread and  
 * 
 *
 */
public class SyncService extends Service {

	private ManagerHolder managerHolder;
	
	private IRideWCFServiceClient wcfServiceClient;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

		managerHolder = new ManagerHolder(new SqliteDBHelper(getApplicationContext()));
				
		wcfServiceClient = new RideWCFServiceNativeImpl();
		
		new Thread(new Runnable() {

			@Override
			public void run() {	
				
				while (true) {
					Log.d("Service", "working " + new SimpleDateFormat("hh:mm:ss").format(new Date()));
					InAppDebug.log(getApplicationContext(), "Service working");
					try {
						for (RouteInstance ri : managerHolder.getRouteInstanceManager().getAllFinished()) {
							Log.d("Service", "r " + ri.getRouteId() + " " + ri.getId() + " " + ri.isFinished());
							InAppDebug.log(getApplicationContext(), "send ri {rid,vid,fin}: " + ri.getRouteId() + " " + ri.getVehicleId() + " " + ri.isFinished());
							boolean res = wcfServiceClient.saveRouteInstance(ri);
//							boolean res = true;
							InAppDebug.log(getApplicationContext(), "got resp: " + res);	
							if (res) {								
								managerHolder.getRouteInstanceManager().delete(ri);
								InAppDebug.log(getApplicationContext(), "del from db  {ri,vid,fin}: " + ri.getRouteId() + " " + ri.getVehicleId() + " " + ri.isFinished());
							} else {
								InAppDebug.log(getApplicationContext(), "keep in db  {ri,vid,fin}: " + ri.getRouteId() + " " + ri.getVehicleId() + " " + ri.isFinished());
							}
						}						 
						for (VehicleRoute vr : managerHolder.getVehicleRouteManager().getAll()) {												
							InAppDebug.log(getApplicationContext(), "send VR {v,r,p}: " + vr.getVehicleId() + " " + vr.getRouteId() + " " + vr.getPersonId());							
							boolean res = wcfServiceClient.saveVehicleRoute(vr);
//							boolean res = true;
							InAppDebug.log(getApplicationContext(), "got resp: " + res);							
							if (res) {
								InAppDebug.log(getApplicationContext(), "del from db  {v,r,p}: " + vr.getVehicleId() + " " + vr.getRouteId() + " " + vr.getPersonId());
								managerHolder.getVehicleRouteManager().delete(vr);																																				
							} else {
								InAppDebug.log(getApplicationContext(), "keep in db  {v,r,p}: " + vr.getVehicleId() + " " + vr.getRouteId() + " " + vr.getPersonId());
							}
						}
												
						
						
						// save last sync date
						SharedPreferences settings = getSharedPreferences(Config.C_PREFS_NAME, 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putString(Config.C_LAST_LOCAL_DATA_SYNC, new SimpleDateFormat(Config.C_DEFAULT_DATE_FORMAT).format(new Date()).toString());
						editor.commit();
						
						Thread.sleep(Config.C_SYNC_SERVICE_PERIOD);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}

			}
		}).start();
	}
	
}
