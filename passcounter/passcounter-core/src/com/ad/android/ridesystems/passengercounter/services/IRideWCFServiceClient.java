package com.ad.android.ridesystems.passengercounter.services;


import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstance;
import com.ad.android.ridesystems.passengercounter.model.entities.VehicleRoute;
import com.ad.android.ridesystems.passengercounter.model.vo.SyncVO;
import com.ad.android.ridesystems.passengercounter.model.vo.schedule.AgencySchedule;

/**
 * Interface of backend. 
 *
 */
public interface IRideWCFServiceClient {
	
	/**
	 * fetch data about employees and vehicles  
	 * @return
	 */
	public SyncVO getAllEmployeesAndVehicles();
	
	/**
	 * fetch data about resources 
	 * @return
	 */
	public SyncVO getAllRoutesAndTrackingLevels();
	
	
	/**
	 * sync route instance data about resources 
	 * @param ri route instance
	 * @return success
	 */
	public boolean saveRouteInstance(RouteInstance ri);
	
	
	/**
	 * Tells WS is vehicle on route or out of 
	 * @param ri route instance
	 * @return success
	 */
	public boolean saveVehicleRoute(VehicleRoute vr);

	/**
	 * 
	 * @return
	 */
	public AgencySchedule getAllRouteSchedules();
	
	/**
	 * 
	 * @return
	 */
	public String getAgencyUrl();
	
}
