package com.ad.android.ridesystems.passengercounter.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.ad.android.ridesystems.passengercounter.common.Utils;

/**
 * 
 * Vehicle entity.  
 * Original entity comes from WebService and has next structure:
 * 
 * <?xml version="1.0"?>
 *	<Route>
      <UniqueID>f719e38c-4606-482d-9259-ba882581bac3</UniqueID>
      <Comments>
        <Active/>
        <Deleted/>
      </Comments>
      <Description>Red</Description>
      <IsCheckedOnMap>false</IsCheckedOnMap>
      <IsVisibleOnMap>true</IsVisibleOnMap>
      <Landmarks>
      	<Active/>
        <Deleted/>
      </Landmarks>
      <MapLatitude>0</MapLatitude>
      <MapLineColor>#FF0000</MapLineColor>
      <MapLongitude>0</MapLongitude>
      <MapZoom>0</MapZoom>
      <Order>1</Order>
      <RouteID>15</RouteID>
      <Stops>
      	<Active/>
        <Deleted/>
      </Stops>
      <TextingKey/>
	</Route>
 *
 * Android app uses only part of this   
 * 
 *
 */
public class Route extends AEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8453050945861287507L;

	public static final String SQL_CREATE_TABLE = "CREATE TABLE route (" +
														"id INTEGER PRIMARY KEY, " +
														"uniqueId TEXT, " +
														"routeId INTEGER, " +
														"description TEXT," +
														"mapLineColor TEXT" +
												  ");";
	
	private String uniqueId = "";
	
	private int routeId = 0;
	
	private String description = "";
	
	private String mapLineColor = "";
	
	private List<RouteStop> stops = new ArrayList<RouteStop>();
	
	private List<RouteInstance> routeInstances = new ArrayList<RouteInstance>();

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}	

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMapLineColor() {
		return mapLineColor;
	}

	public void setMapLineColor(String mapLineColor) {
		this.mapLineColor = mapLineColor;
	}
		
	public List<RouteStop> getStops() {
		return stops;
	}

	public void setStops(List<RouteStop> stops) {
		this.stops = stops;
	}
		

	public List<RouteInstance> getRouteInstances() {
		return routeInstances;
	}

	public void setRouteInstances(List<RouteInstance> routeInstances) {
		this.routeInstances = routeInstances;
	}

	public static Route fromDOM(Element element) {
		Route inst = new Route();
		inst.setDescription(Utils.getElementStringValue(element, "Description"));				
		inst.setUniqueId(Utils.getElementStringValue(element, "UniqueID"));
		inst.setMapLineColor(Utils.getElementStringValue(element, "MapLineColor"));
		inst.setRouteId(Utils.getElementIntValue(element, "RouteID"));
		return inst; 
	}
	
	@Override
	public String toString() {
		return this.description + " " + this.mapLineColor;
	}
	
}
