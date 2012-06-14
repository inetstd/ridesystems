package com.ad.android.ridesystems.passengercounter.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 */
public class RouteInstance extends AEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 333001123121956024L;

	public static final String SQL_CREATE_TABLE = 
		"CREATE TABLE routeinstance (" +
			"id INTEGER PRIMARY KEY, " +
			"routeDate TEXT, " +			
			"routeId INTEGER, " +
			"employeeId INTEGER, " +			
			"vehicleId INTEGER," +
			"finished INTEGER" +
		");";
	
	private String routeDate = "";
	private int routeId;		
	private int employeeId;		
	private int vehicleId;
	private List<RouteInstanceDetail> details = new ArrayList<RouteInstanceDetail>();
	private boolean finished = false;
	
	public String getRouteDate() {
		return routeDate;
	}
	public void setRouteDate(String routeDate) {
		this.routeDate = routeDate;
	}
	public int getRouteId() {
		return routeId;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public List<RouteInstanceDetail> getDetails() {
		return details;
	}
	public void setDetails(List<RouteInstanceDetail> details) {
		this.details = details;
	}
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	@Override
	public String toString() {	
		return super.toString();
	}
}
