package com.ad.android.ridesystems.passengercounter.model.dao;

import java.util.List;

import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstanceDetail;


/**
 * Interface for additional methods of dao
 *
 */
public interface IRouteInstanceDetailDAO extends IDAO<RouteInstanceDetail, Integer> {

	
	/**
	 * get all details by route instance id 
	 * @param routeId
	 * @return
	 */
	public List<RouteInstanceDetail> getAllByRouteInstanceId(int routeId);
	
}
