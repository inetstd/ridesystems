package com.ad.android.ridesystems.passengercounter.model.dao;

import java.util.List;

import com.ad.android.ridesystems.passengercounter.model.entities.RouteStop;


/**
 * Interface for additional methods of dao
 *
 */
public interface IRouteStopDAO extends IDAO<RouteStop, Integer> {

	/**
	 * getAllRoutes by route id 
	 * @param routeId
	 * @return
	 */
	public List<RouteStop> getAllByRouteId(int routeId);
	
}
