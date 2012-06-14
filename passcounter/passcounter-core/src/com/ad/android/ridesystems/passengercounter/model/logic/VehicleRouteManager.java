package com.ad.android.ridesystems.passengercounter.model.logic;

import android.util.Log;

import com.ad.android.ridesystems.passengercounter.model.dao.IVehicleRouteDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.VehicleRoute;

/**
 * Working with model VehicleRoute.
 *
 */
public class VehicleRouteManager extends ABaseEntityManager<VehicleRoute, Integer> {

	/***
	 * 
	 * @param dao
	 */
	public VehicleRouteManager(IVehicleRouteDAO dao) {
		super(dao);				
	}
	
	/**
	 * vehicleRoute will be send to WS, means that vehicle is on a route  
	 * @param vehicleId
	 * @param routeId
	 * @param personId
	 */
	public void createVehicleRouteOnStartRoute(int vehicleId, int routeId, int personId) {
		
		VehicleRoute vehicleRoute = new VehicleRoute();
		vehicleRoute.setPersonId(personId);
		vehicleRoute.setRouteId(routeId);
		vehicleRoute.setVehicleId(vehicleId);
		Log.i("VehicleRouteManager", " vechicle route strart vrp " +  vehicleId + " " + routeId + " " + personId);		
		insert(vehicleRoute);
	}
	
	/**
	 * vehicleRoute will be send to WS, means that vehicle is out from route  
	 * @param vehicleId
	 * @param routeId
	 * @param personId
	 */
	public void createVehicleRouteOnFinishRoute(int personId) {
		VehicleRoute vehicleRoute = new VehicleRoute();
		vehicleRoute.setPersonId(personId);
		vehicleRoute.setRouteId(0);
		vehicleRoute.setVehicleId(0);
		Log.i("VehicleRouteManager", " vechicle route finish vp " + " " + personId);
		insert(vehicleRoute);
	}

}
