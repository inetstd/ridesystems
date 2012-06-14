package com.ad.android.ridesystems.passengercounter.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.ad.android.ridesystems.passengercounter.common.Utils;

/**
 * 
 * 
 * <TrackingLevel>
	    <UniqueID>8844d993-f61c-4951-a5e0-68287200bce0</UniqueID>
	    <Criteria>
	          <Active>
	                <RouteTrackingCriteria>
	                      <UniqueID>93f15f12-735b-4945-96f2-ca7d048bb852</UniqueID>
	                      <Description>On</Description>
	                      <IncludeInMainCount>true</IncludeInMainCount>
	                      <Order>0</Order>
	                      <RouteTrackingCriteriaID>1</RouteTrackingCriteriaID>
	                      <TrackingLevelID>1</TrackingLevelID>
	                </RouteTrackingCriteria>
	          </Active>
	          <Deleted/>
	    </Criteria>
	    <Description>Passengers</Description>
	    <Level>0</Level>
	    <TrackingLevelID>1</TrackingLevelID>
  </TrackingLevel>
 * 
 *
 */
public class TrackingLevel extends AEntity implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2442048068427326761L;

	public static final String SQL_CREATE_TABLE = 
		"CREATE TABLE trackinglevel (" +
			"id INTEGER PRIMARY KEY, " +
			"uniqueId TEXT, " +
			"trackingLevelId INTEGER, " +
			"description TEXT," +
			"level INTEGER" +
		");";
	
	private String uniqueId = "";
	
	private String description = "";
	
	private int level = 0;
	
	private int trackingLevelId = 0;
	
	private List<RouteTrackingCriteria> criterias = new ArrayList<RouteTrackingCriteria>(); 

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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getTrackingLevelId() {
		return trackingLevelId;
	}

	public void setTrackingLevelId(int trackingLevelId) {
		this.trackingLevelId = trackingLevelId;
	}
		
	
	public List<RouteTrackingCriteria> getCriterias() {
		return criterias;
	}

	public void setCriterias(List<RouteTrackingCriteria> criterias) {
		this.criterias = criterias;
	}

	public static TrackingLevel fromDOM(Element element) {
		TrackingLevel inst = new TrackingLevel();
		inst.setDescription(Utils.getElementStringValue(element, "Description"));				
		inst.setUniqueId(Utils.getElementStringValue(element, "UniqueID"));
		inst.setLevel(Utils.getElementIntValue(element, "Level"));	
		inst.setTrackingLevelId(Utils.getElementIntValue(element, "TrackingLevelID"));		
		return inst; 
	}

}
