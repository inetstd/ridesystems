package com.ad.android.ridesystems.passengercounter.model.logic;


import com.ad.android.ridesystems.passengercounter.model.dao.IEmployeeDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.Employee;

/**
 * Manager encapsulate logic of Employee entity.
 * 
 *
 */
public class EmployeeManager extends ABaseEntityManager<Employee, Integer> {
		
	/**
	 * 
	 * @param dao DAO instance
	 */
	public EmployeeManager(IEmployeeDAO dao) {
		super(dao);	
	}
	
	
	/**
	 * Get employee.
	 * @param login login
	 * @param password password
	 * @return employee instance or null
	 */
	public Employee get(String login, String password) {		
		return ((IEmployeeDAO) dao).get(login, password);		
	}
	
	
}
