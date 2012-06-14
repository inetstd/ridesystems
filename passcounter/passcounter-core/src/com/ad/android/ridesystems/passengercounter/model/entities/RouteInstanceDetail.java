package com.ad.android.ridesystems.passengercounter.model.entities;

import java.io.Serializable;


public class RouteInstanceDetail extends AEntity implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8981667701902498924L;

	public static final String SQL_CREATE_TABLE = 
		"CREATE TABLE routeinstancedetail (" +
			"id INTEGER PRIMARY KEY, " +
			"routeDate TEXT, " +			
			"routeTrackingCriteriaId INTEGER, " +
			"routeStopId INTEGER, " +
			"routeInstanceId INTEGER, " +
			"quantity INTEGER," +
			"FOREIGN KEY(routeInstanceId) REFERENCES routeinstance(id) ON DELETE CASCADE" +
		");";	
		
	private int routeStopId;
	private int routeTrackingCriteriaId;	
	private int routeInstanceId;
	private String routeDate = "";
	private int quantity = 0;
	public int getRouteStopId() {
		return routeStopId;
	}
	public void setRouteStopId(int routeStopId) {
		this.routeStopId = routeStopId;
	}
	public int getRouteTrackingCriteriaId() {
		return routeTrackingCriteriaId;
	}
	public void setRouteTrackingCriteriaId(int routeTrackingCriteriaId) {
		this.routeTrackingCriteriaId = routeTrackingCriteriaId;
	}
	public int getRouteInstanceId() {
		return routeInstanceId;
	}
	public void setRouteInstanceId(int routeInstanceId) {
		this.routeInstanceId = routeInstanceId;
	}
	public String getRouteDate() {
		return routeDate;
	}
	public void setRouteDate(String routeDate) {
		this.routeDate = routeDate;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "{id: " + getId() + ", criteriaId: " + this.routeTrackingCriteriaId + ", stopId: " + routeStopId + ", instanceId: " + routeInstanceId +  ", value: " + quantity + "}";
	}
	
}
