package com.ad.android.ridesystems.passengercounter.model.entities;

import java.io.Serializable;

import org.dom4j.Element;

import com.ad.android.ridesystems.passengercounter.common.Utils;

/**
 * 
 * Vehicle entity.  
 * Original entity comes from WebService and has next structure:
 * 
 * <?xml version="1.0"?>
 *	<Vehicle>
 *	  <UniqueID>c5e8b005-4548-449d-a31b-8d810ecca367</UniqueID>
 *	  <Comments>
 *	    <Active/>
 *	    <Deleted/>
 *	  </Comments>
 *	  <DVIRS>
 *	    <Active/>
 *	    <Deleted/>
 *	  </DVIRS>
 *	  <MaintenanceRecords>
 *	    <Active/>
 *	    <Deleted/>
 *	  </MaintenanceRecords>
 *	  <Make/>
 *	  <Model/>
 *	  <Name>4350</Name>
 *	  <VIN/>
 *	  <VehicleID>35</VehicleID>
 *	  <VehicleType>
 *	    <UniqueID>98f4349c-d607-40d4-9fea-bff02f728a7d</UniqueID>
 *	    <Description>Bus</Description>
 *	    <VehicleTypeID>1</VehicleTypeID>
 *	  </VehicleType>
 *	  <Year>2011</Year>
 *	</Vehicle>
 *
 * Android app uses only part of this   
 * 
 *
 */
public class Vehicle extends AEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4282444956444491299L;

	public static final String SQL_CREATE_TABLE = "CREATE TABLE vehicle (" +
														"id INTEGER PRIMARY KEY, " +
														"uniqueId TEXT, " +
														"vehicleId INTEGER, " +
														"name TEXT" +
												  ");";
	
	private String uniqueId = "";
	
	private int vehicleId = 0;
	
	private String name = "";

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}		
	
	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public static Vehicle fromDOM(Element element) {
		Vehicle inst = new Vehicle();
		inst.setName(Utils.getElementStringValue(element, "Name"));				
		inst.setUniqueId(Utils.getElementStringValue(element, "UniqueID"));		
		inst.setVehicleId(Utils.getElementIntValue(element, "VehicleID"));
		return inst; 
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
