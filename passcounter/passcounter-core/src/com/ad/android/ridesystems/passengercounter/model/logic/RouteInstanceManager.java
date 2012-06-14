package com.ad.android.ridesystems.passengercounter.model.logic;


import java.util.List;

import com.ad.android.ridesystems.passengercounter.model.dao.IRouteInstanceDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstance;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstanceDetail;

/**
 * Manager encapsulate logic of RouteStop entity.
 * 
 *
 */
public class RouteInstanceManager extends ABaseEntityManager<RouteInstance, Integer> {

	/**
	 * 
	 * @param dao DAO instance
	 */
	public RouteInstanceManager(IRouteInstanceDAO dao) {
		super(dao);
		
	}

	/**
	 * Get not finished routeinstance for employee.
	 * @param employeeId 
	 * @return routeinstance with details or null 
	 */
	public RouteInstance getNotFinishedByEmployee(int employeeId) {
		RouteInstance ri = ((IRouteInstanceDAO) dao).getNotFinishedByEmployee(employeeId);
		if (ri != null) {
			List<RouteInstanceDetail> list = getManagerHolder().getRouteInstanceDetailManager().getAllByRouteInstanceId(ri.getId());		
			ri.getDetails().addAll(list);
		}
		return ri;
	}
	
	public RouteInstance getNotFinished() {
		RouteInstance ri = ((IRouteInstanceDAO) dao).getNotFinished();
		if (ri != null) {
			List<RouteInstanceDetail> list = getManagerHolder().getRouteInstanceDetailManager().getAllByRouteInstanceId(ri.getId());		
			ri.getDetails().addAll(list);
		}
		return ri;
	}
	
	
	/**
	 * {@inheritDoc}
	 */	
	public RouteInstance getFull(Integer id) {
		RouteInstance ri = dao.get(id);
		if (ri != null) {
			List<RouteInstanceDetail> list = getManagerHolder().getRouteInstanceDetailManager().getAllByRouteInstanceId(id);		
			ri.getDetails().addAll(list);
		}
		return ri;
	}

	/**
	 * 
	 * @return
	 */
	public List<RouteInstance> getAllFinished() {
		List<RouteInstance> list = ((IRouteInstanceDAO) dao).getAllFinished();
		for (RouteInstance routeInstance : list) {
			List<RouteInstanceDetail> details = getManagerHolder().getRouteInstanceDetailManager().getAllByRouteInstanceId(routeInstance.getId());		
			routeInstance.getDetails().addAll(details);
		}
		
		return list;		
	}
	
}
