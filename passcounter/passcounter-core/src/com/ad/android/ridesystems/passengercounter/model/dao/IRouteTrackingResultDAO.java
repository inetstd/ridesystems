package com.ad.android.ridesystems.passengercounter.model.dao;


import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstanceDetail;


/**
 * Interface for additional methods of dao
 *
 */
public interface IRouteTrackingResultDAO extends IDAO<RouteInstanceDetail, Integer> {

	/**
	 * get all criterias for level 
	 * @param id tracking level id
	 * @return array of criterias
	 */
	RouteInstanceDetail get(int criteriaId, int routeId, int vehicleId, int employeeId, String sessionId);

}
