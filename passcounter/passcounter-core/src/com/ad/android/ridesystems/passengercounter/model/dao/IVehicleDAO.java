package com.ad.android.ridesystems.passengercounter.model.dao;

import com.ad.android.ridesystems.passengercounter.model.entities.Vehicle;

/**
 * Interface for additional methods of dao
 *
 */
public interface IVehicleDAO extends IDAO<Vehicle, Integer> {

	/**
	 * Get route by routeId (id in WS db)
	 * @param vehicleId
	 * @return vehicle
	 */
	Vehicle getByVehicleId(int vehicleId);
}
