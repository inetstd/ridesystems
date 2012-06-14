package com.ad.android.ridesystems.passengercounter.model.dao;

import java.util.List;

import com.ad.android.ridesystems.passengercounter.model.entities.RouteTrackingCriteria;


/**
 * Interface for additional methods of dao
 *
 */
public interface IRouteTrackingCriteriaDAO extends IDAO<RouteTrackingCriteria, Integer> {

	/**
	 * get all criterias for level 
	 * @param id tracking level id
	 * @return array of criterias
	 */
	List<RouteTrackingCriteria> getAllByTrackingLevelId(int id);
}
