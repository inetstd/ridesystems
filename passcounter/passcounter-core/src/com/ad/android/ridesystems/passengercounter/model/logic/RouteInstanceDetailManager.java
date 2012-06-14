package com.ad.android.ridesystems.passengercounter.model.logic;


import java.util.List;

import com.ad.android.ridesystems.passengercounter.model.dao.IRouteInstanceDetailDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstanceDetail;

/**
 * Manager encapsulate logic of RouteStop entity.
 * 
 *
 */
public class RouteInstanceDetailManager extends ABaseEntityManager<RouteInstanceDetail, Integer> {

	/**
	 * 
	 * @param dao DAO instance
	 */
	public RouteInstanceDetailManager(IRouteInstanceDetailDAO dao) {
		super(dao);
	}
	
	public List<RouteInstanceDetail> getAllByRouteInstanceId(int routeId) {
		return ((IRouteInstanceDetailDAO) dao).getAllByRouteInstanceId(routeId);
	}
	
}
