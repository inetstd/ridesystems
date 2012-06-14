package com.ad.android.ridesystems.passengercounter.model.logic;

import com.ad.android.ridesystems.passengercounter.model.dao.IVehicleDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.Vehicle;

/**
 * Working with model Vehicle.
 *
 */
public class VehicleManager extends ABaseEntityManager<Vehicle, Integer> {

	/***
	 * 
	 * @param dao
	 */
	public VehicleManager(IVehicleDAO dao) {
		super(dao);		
		
	}

	/**
	 * get route vehicle 
	 * @param vehicleId
	 * @return
	 */
	public Vehicle getByVehicleId(int vehicleId) {
		return ((IVehicleDAO) dao).getByVehicleId(vehicleId);
	}

}
