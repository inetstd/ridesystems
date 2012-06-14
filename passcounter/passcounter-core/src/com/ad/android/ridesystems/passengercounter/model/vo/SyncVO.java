package com.ad.android.ridesystems.passengercounter.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.ad.android.ridesystems.passengercounter.model.entities.Employee;
import com.ad.android.ridesystems.passengercounter.model.entities.Route;
import com.ad.android.ridesystems.passengercounter.model.entities.TrackingLevel;
import com.ad.android.ridesystems.passengercounter.model.entities.Vehicle;

/**
 * Value object for better WS client communication. 
 * Incapsulates some common variables
 *
 */
public class SyncVO {

	List<Employee> employees = new ArrayList<Employee>();
	
	List<Vehicle> vehicles = new ArrayList<Vehicle>();
	
	List<TrackingLevel> trackingLevels = new ArrayList<TrackingLevel>();
	
	List<Route> routes = new ArrayList<Route>();

	public List<Employee> getEmployees() {
		return employees;
	}
	
	
	public List<TrackingLevel> getTrackingLevels() {
		return trackingLevels;
	}



	public void setTrackingLevels(List<TrackingLevel> trackingLevels) {
		this.trackingLevels = trackingLevels;
	}



	public List<Route> getRoutes() {
		return routes;
	}



	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}



	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}
	
	
}
