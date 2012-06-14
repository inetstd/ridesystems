package com.ad.android.ridesystems.passengercounter.model.logic;

import java.util.List;

import com.ad.android.ridesystems.passengercounter.model.dao.IRouteTrackingCriteriaDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteTrackingCriteria;

/**
 * Manager encapsulate logic of RouteTrackingCriteria entity.
 * 
 *
 */
public class RouteTrackingCriteriaManager extends ABaseEntityManager<RouteTrackingCriteria, Integer> {
	
	
	/**
	 * 
	 * @param dao DAO instance
	 */
	public RouteTrackingCriteriaManager(IRouteTrackingCriteriaDAO dao) {
		super(dao);
		
	}
	
	public List<RouteTrackingCriteria> getAllByTrackingLevelId(int id) {
		return ((IRouteTrackingCriteriaDAO)dao).getAllByTrackingLevelId(id);
	}
	
	
}
