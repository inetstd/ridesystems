package com.ad.android.ridesystems.passengercounter.model.helpers;

import android.database.sqlite.SQLiteOpenHelper;

import com.ad.android.ridesystems.passengercounter.model.dao.IEmployeeDAO;
import com.ad.android.ridesystems.passengercounter.model.dao.IRouteDAO;
import com.ad.android.ridesystems.passengercounter.model.dao.IRouteInstanceDAO;
import com.ad.android.ridesystems.passengercounter.model.dao.IRouteInstanceDetailDAO;
import com.ad.android.ridesystems.passengercounter.model.dao.IRouteStopDAO;
import com.ad.android.ridesystems.passengercounter.model.dao.IRouteTrackingCriteriaDAO;
import com.ad.android.ridesystems.passengercounter.model.dao.ITrackingLevelDAO;
import com.ad.android.ridesystems.passengercounter.model.dao.IVehicleDAO;
import com.ad.android.ridesystems.passengercounter.model.dao.IVehicleRouteDAO;
import com.ad.android.ridesystems.passengercounter.model.dao.impl.EmployeeDAOSqlite;
import com.ad.android.ridesystems.passengercounter.model.dao.impl.RouteDAOSqlite;
import com.ad.android.ridesystems.passengercounter.model.dao.impl.RouteInstanceDAOSqlite;
import com.ad.android.ridesystems.passengercounter.model.dao.impl.RouteInstanceDetailDAOSqlite;
import com.ad.android.ridesystems.passengercounter.model.dao.impl.RouteStopDAOSqlite;
import com.ad.android.ridesystems.passengercounter.model.dao.impl.RouteTrackingCriteriaDAOSqlite;
import com.ad.android.ridesystems.passengercounter.model.dao.impl.TrackingLevelDAOSqlite;
import com.ad.android.ridesystems.passengercounter.model.dao.impl.VehicleDAOSqlite;
import com.ad.android.ridesystems.passengercounter.model.dao.impl.VehicleRouteDAOSqlite;
import com.ad.android.ridesystems.passengercounter.model.logic.EmployeeManager;
import com.ad.android.ridesystems.passengercounter.model.logic.RouteInstanceDetailManager;
import com.ad.android.ridesystems.passengercounter.model.logic.RouteInstanceManager;
import com.ad.android.ridesystems.passengercounter.model.logic.RouteManager;
import com.ad.android.ridesystems.passengercounter.model.logic.RouteStopManager;
import com.ad.android.ridesystems.passengercounter.model.logic.RouteTrackingCriteriaManager;
import com.ad.android.ridesystems.passengercounter.model.logic.TrackingLevelManager;
import com.ad.android.ridesystems.passengercounter.model.logic.VehicleManager;
import com.ad.android.ridesystems.passengercounter.model.logic.VehicleRouteManager;

/**
 * Holds instances of managers. Creates instances if getter method is called 
 *
 */
public class ManagerHolder {
	
	/**
	 * 
	 */
	SQLiteOpenHelper dbHelper = null;
	
	/**
	 * Employee Manager instance. Instantiates in getter. 
	 * 
	 */
	private EmployeeManager employeeManager = null;

	/**
	 * Vehicle Manager instance. Instantiates in getter. 
	 * 
	 */
	private VehicleManager vehicleManager = null;
	
	/**
	 * Route Manager instance. Instantiates in getter. 
	 * 
	 */
	private RouteManager routeManager = null;
	
	/**
	 * Vehicle Manager instance. Instantiates in getter. 
	 * 
	 */
	private RouteStopManager routeStopManager = null;
	
	/**
	 * Route Manager instance. Instantiates in getter. 
	 * 
	 */
	private TrackingLevelManager trackingLevelManager = null;
	
	/**
	 * Vehicle Manager instance. Instantiates in getter. 
	 * 
	 */
	private RouteTrackingCriteriaManager criteriaManager = null;
	
	/**
	 * RouteInstance Manager instance. Instantiates in getter. 
	 * 
	 */
	private RouteInstanceManager routeInstanceManager = null;
	
	/**
	 * RouteInstanceDetail Manager instance. Instantiates in getter. 
	 */
	private RouteInstanceDetailManager routeInstanceDetailManager = null;
	
	/**
	 * VehicleRoute Manager instance. Instantiates in getter. 
	 */
	private VehicleRouteManager vehicleRouteManager = null;

	/**
	 * 
	 * @param dbHelper
	 */
	public ManagerHolder(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
	}
	
	
	/**
	 * Gets manager instance. 
	 * Creates if is null
	 * @return EmployeeManager
	 */
	public EmployeeManager getEmployeeManager() {
		if (employeeManager == null) {
			IEmployeeDAO emplDAO = new EmployeeDAOSqlite(dbHelper);
			employeeManager = new EmployeeManager(emplDAO);
			employeeManager.setManagerHolder(this);
		}
		return employeeManager;
	}

	/**
	 * Gets manager instance. 
	 * Creates if is null
	 * @return VehicleManager
	 */
	public VehicleManager getVehicleManager() {
		if (vehicleManager == null) {
			IVehicleDAO vehicleDAO = new VehicleDAOSqlite(dbHelper);
			vehicleManager = new VehicleManager(vehicleDAO);
			vehicleManager.setManagerHolder(this);
		}
		return vehicleManager;
	}
	
	/**
	 * Gets manager instance. 
	 * Creates if is null
	 * @return RouteManager
	 */	
	public RouteManager getRouteManager() {
		if (routeManager == null) {
			IRouteDAO routeDAO = new RouteDAOSqlite(dbHelper);
			routeManager = new RouteManager(routeDAO);
			routeManager.setManagerHolder(this);
		}
		return routeManager;
	}

	/**
	 * Gets manager instance. 
	 * Creates if is null
	 * @return RouteStopManager
	 */
	public RouteStopManager getRouteStopManager() {
		if (routeStopManager == null) {
			IRouteStopDAO routeStopDAO = new RouteStopDAOSqlite(dbHelper);
			routeStopManager = new RouteStopManager(routeStopDAO);
			routeStopManager.setManagerHolder(this);
		}
		return routeStopManager;
	}


	/**
	 * Gets manager instance. 
	 * Creates if is null
	 * @return TrackingLevelManager
	 */
	public TrackingLevelManager getTrackingLevelManager() {
		if (trackingLevelManager == null) {
			ITrackingLevelDAO trackingLevelDAO = new TrackingLevelDAOSqlite(dbHelper);
			trackingLevelManager = new TrackingLevelManager(trackingLevelDAO);
			trackingLevelManager.setManagerHolder(this);
		}
		return trackingLevelManager;
	}

	/**
	 * Gets manager instance. 
	 * Creates if is null
	 * @return RouteTrackingCriteriaManager
	 */
	public RouteTrackingCriteriaManager getCriteriaManager() {
		if (criteriaManager == null) {
			IRouteTrackingCriteriaDAO criteriaDAO = new RouteTrackingCriteriaDAOSqlite(dbHelper);
			criteriaManager = new RouteTrackingCriteriaManager(criteriaDAO);
			criteriaManager.setManagerHolder(this);
		}
		return criteriaManager;
	}

	/**
	 * Gets manager instance. 
	 * Creates if is null
	 * @return
	 */
	public RouteInstanceManager getRouteInstanceManager() {
		if (routeInstanceManager == null) {
			IRouteInstanceDAO routeInstanceDAO = new RouteInstanceDAOSqlite(dbHelper);
			routeInstanceManager = new RouteInstanceManager(routeInstanceDAO);
			routeInstanceManager.setManagerHolder(this);
		}
		return routeInstanceManager;
	}
	
	/**
	 * Gets manager instance. 
	 * Creates if is null
	 * @return
	 */
	public RouteInstanceDetailManager getRouteInstanceDetailManager() {
		if (routeInstanceDetailManager == null) {
			IRouteInstanceDetailDAO routeDAO = new RouteInstanceDetailDAOSqlite(dbHelper);
			routeInstanceDetailManager = new RouteInstanceDetailManager(routeDAO);
			routeInstanceDetailManager.setManagerHolder(this);
		}
		return routeInstanceDetailManager;
	}
	
	/**
	 * Gets manager instance. 
	 * Creates if is null 
	 * @return
	 */
	public VehicleRouteManager getVehicleRouteManager() {
		if (vehicleRouteManager == null) {
			IVehicleRouteDAO vehicleRouteDAO = new VehicleRouteDAOSqlite(dbHelper);
			vehicleRouteManager = new VehicleRouteManager(vehicleRouteDAO);
			vehicleRouteManager.setManagerHolder(this);
		}
		return vehicleRouteManager;
	}
	
	
	
	
}
