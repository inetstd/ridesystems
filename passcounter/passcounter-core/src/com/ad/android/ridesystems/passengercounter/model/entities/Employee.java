package com.ad.android.ridesystems.passengercounter.model.entities;

import java.io.Serializable;

import org.dom4j.Element;

import com.ad.android.ridesystems.passengercounter.common.Utils;

/**
 * Employee entity.  
 * Original entity comes from WebService and has next structure:
 * 
 * <pre>
 *	<?xml version="1.0"?>
 *	<Employee>
 *	  <UniqueID>d1a48efd-219e-46eb-8014-ddaaf82eae1d</UniqueID>
 *	  <Addresses>
 *	    <Active/>
 *	    <Deleted/>
 *	  </Addresses>
 *	  <Birthdate>0001-01-01T00:00:00</Birthdate>
 *	  <Comments>
 *	    <Active/>
 *	    <Deleted/>
 *	  </Comments>
 *	  <EmergencyPhone/>
 *	  <FirstName>Maria</FirstName>
 *	  <HomePhone/>
 *	  <LastName/>
 *	  <MobilePhone/>
 *	  <PersonID>2</PersonID>
 *	  <EmployeeType>
 *	    <UniqueID>29ab7d10-b92b-49b7-942d-a76be9d711cd</UniqueID>
 *	    <Description/>
 *	    <EmployeeTypeID>2</EmployeeTypeID>
 *	  </EmployeeType>
 *	  <EmployeeTypeID>2</EmployeeTypeID>
 *	  <HireDate>0001-01-01T00:00:00</HireDate>
 *	  <Password>0000</Password>
 *	  <Rights/>
 *	  <UserName>Maria</UserName>
 *	</Employee> 
 * </pre>
 * 
 * Android app uses only part of this   
 * 
 *
 */

public class Employee extends AEntity implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 8314388483967258635L;


	public static final String SQL_CREATE_TABLE = "CREATE TABLE employee (" +
														"id INTEGER PRIMARY KEY, " +
														"firstName TEXT, " +
														"uniqueId TEXT, " +
														"personId INTEGER, " +
														"password TEXT " +														
													");";
		
		
	private String uniqueId = "";
	
	private String firstName = "";
	
	private String password = "";
	
	private Integer personId = 0;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
		
	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
		
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static Employee fromDOM(Element element) {
		Employee inst = new Employee();
		inst.setFirstName(Utils.getElementStringValue(element, "UserName"));				
		inst.setUniqueId(Utils.getElementStringValue(element, "UniqueID"));		
		inst.setPersonId(Utils.getElementIntValue(element, "PersonID"));
		inst.setPassword(Utils.getElementStringValue(element, "Password"));
		return inst; 
	}
	
	public String toString() {
		return this.firstName; 
	}
	
}
