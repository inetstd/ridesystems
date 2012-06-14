package com.ad.android.ridesystems.passengercounter.model.entities;

import java.io.Serializable;

import org.dom4j.Element;

import com.ad.android.ridesystems.passengercounter.common.Utils;

/**
 * 
 * <RouteTrackingCriteria>
	      <UniqueID>93f15f12-735b-4945-96f2-ca7d048bb852</UniqueID>
	      <Description>On</Description>
	      <IncludeInMainCount>true</IncludeInMainCount>
	      <Order>0</Order>
	      <RouteTrackingCriteriaID>1</RouteTrackingCriteriaID>
	      <TrackingLevelID>1</TrackingLevelID>
	</RouteTrackingCriteria>
	
 *

 *
 */
public class RouteTrackingCriteria extends AEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8880870222696405025L;

	public static final String SQL_CREATE_TABLE = 
		"CREATE TABLE routetrackingcriteria (" +
			"id INTEGER PRIMARY KEY, " +
			"uniqueId TEXT, " +			
			"routeTrackingCriteriaId INTEGER, " +
			"trackingLevelId INTEGER, " +
			"description TEXT," +
			"includeInMainCount INTEGER," + 
			"iorder INTEGER" +
		");";
	
	private String uniqueId = "";
	
	private String description = "";
	
	private boolean includeInMainCount = true;
	
	private int order = 0;
	
	private int routeTrackingCriteriaId = 0;
	
	private int trackingLevelId = 0;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isIncludeInMainCount() {
		return includeInMainCount;
	}

	public void setIncludeInMainCount(boolean includeInMainCount) {
		this.includeInMainCount = includeInMainCount;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getRouteTrackingCriteriaId() {
		return routeTrackingCriteriaId;
	}

	public void setRouteTrackingCriteriaId(int routeTrackingCriteriaId) {
		this.routeTrackingCriteriaId = routeTrackingCriteriaId;
	}

	public int getTrackingLevelId() {
		return trackingLevelId;
	}

	public void setTrackingLevelId(int trackingLevelId) {
		this.trackingLevelId = trackingLevelId;
	}
	
	public static RouteTrackingCriteria fromDOM(Element element) {
		RouteTrackingCriteria inst = new RouteTrackingCriteria();
		inst.setDescription(Utils.getElementStringValue(element, "Description"));				
		inst.setUniqueId(Utils.getElementStringValue(element, "UniqueID"));
		inst.setRouteTrackingCriteriaId(Utils.getElementIntValue(element, "RouteTrackingCriteriaID"));
		inst.setTrackingLevelId(Utils.getElementIntValue(element, "TrackingLevelID"));		
		inst.setIncludeInMainCount(Utils.getElementBoolValue(element, "IncludeInMainCount"));
		inst.setOrder(Utils.getElementIntValue(element, "Order"));		
		return inst; 
	}
}
