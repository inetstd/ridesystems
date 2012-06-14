package com.ad.android.ridesystems.passengercounter.model.logic;


import java.util.List;

import com.ad.android.ridesystems.passengercounter.model.dao.IRouteDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.Route;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteStop;

/**
 * Manager encapsulate logic of Route entity.
 * 
 *
 */
public class RouteManager extends ABaseEntityManager<Route, Integer> {


	/**
	 * 
	 * @param dao DAO instance
	 */
	public RouteManager(IRouteDAO dao) {
		super(dao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Route getFull(Integer id) {
		Route ri = dao.get(id);
		if (ri != null) {
			List<RouteStop> list = getManagerHolder().getRouteStopManager().getAllByRouteId(ri.getRouteId());		
			ri.getStops().addAll(list);
		}
		return ri;
	}
	
	/**
	 * Delete all entities and insert new.
	 * @param routes Array of routes
	 */
	public void syncAll(List<Route> routes) {
		this.deleteAll();
		this.insertAll(routes);
	}

	public Route getByRouteId(int routeId) {
		return ((IRouteDAO)dao).getByRouteId(routeId);
	}
	
}
