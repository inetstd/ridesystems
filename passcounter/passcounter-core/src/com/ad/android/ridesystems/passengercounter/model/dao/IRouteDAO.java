package com.ad.android.ridesystems.passengercounter.model.dao;

import com.ad.android.ridesystems.passengercounter.model.entities.Route;

/**
 * Interface for additional methods of dao
 *
 */
public interface IRouteDAO extends IDAO<Route, Integer> {

	/**
	 * Get route by routeId (id in WS db)
	 * @param routeId
	 * @return route
	 */
	Route getByRouteId(int routeId);

}
