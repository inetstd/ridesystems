package com.ad.android.ridesystems.passengercounter.model.entities;

import java.io.Serializable;

/**
 * 
 * VehicleRoute entity.
 * Instance is sending to WS on start route on finish or on logout.
 * On start passed: VehicleID, RouteID, PersonID
 * On finish      : VehicleID, 0, PersonID
 * Original entity comes from WebService and has next structure:
 *
 */
public class VehicleRoute extends AEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4282434234234L;

	public static final String SQL_CREATE_TABLE = "CREATE TABLE vehicleroute (" +
														"id INTEGER PRIMARY KEY, " +														
														"vehicleId INTEGER, " +
														"routeId INTEGER, " +
														"personId INTEGER " +
												  ");";
	
	private int id;
	
	private int vehicleId = 0;
	
	private int routeId = 0;

	private int personId = 0;

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}
	
	

}
