package com.ad.android.ridesystems.passengercounter.model.dao;

import com.ad.android.ridesystems.passengercounter.model.entities.Employee;

/**
 * Interface for additional methods of dao
 *
 */
public interface IEmployeeDAO extends IDAO<Employee, Integer> {

	/**
	 * Employee 
	 * @param login login
	 * @param pass password
	 * @return employee or null
	 */
	Employee get(String login, String pass); 
}
