package com.ad.android.ridesystems.passengercounter.model.dao;

import java.util.List;

import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstance;


/**
 * Interface for additional methods of dao
 *
 */
public interface IRouteInstanceDAO extends IDAO<RouteInstance, Integer> {

	/**
	 * Get active route tracking
	 * @param employeeId
	 * @return
	 */
	public RouteInstance getNotFinishedByEmployee(int employeeId);
	
	/**
	 * Get active route tracking
	 * @param employeeId
	 * @return
	 */
	public RouteInstance getNotFinished();

	/**
	 * Get all instances to sync
	 */
	public List<RouteInstance> getAllFinished();
		
}
