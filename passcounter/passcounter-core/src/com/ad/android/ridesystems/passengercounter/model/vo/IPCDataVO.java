package com.ad.android.ridesystems.passengercounter.model.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ad.android.ridesystems.passengercounter.common.Utils;
import com.ad.android.ridesystems.passengercounter.model.entities.Employee;
import com.ad.android.ridesystems.passengercounter.model.entities.Route;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstance;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstanceDetail;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteStop;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteTrackingCriteria;
import com.ad.android.ridesystems.passengercounter.model.entities.TrackingLevel;
import com.ad.android.ridesystems.passengercounter.model.entities.Vehicle;

/**
 * Interprocess data value object.
 * Save state of current progress 
 *
 */
public class IPCDataVO implements Serializable {

	public static String KEY = "IPCDataVO";

	/**
	 * 
	 */
	private static final long serialVersionUID = 2671212236956623962L;

	private Employee employee = null;
	
	private Route route = null;
	
	private Vehicle vehicle = null;
	
	private RouteStop currentRouteStop = null;
	
	int currentRouteStopIndex = 0;
	
	private List<TrackingLevel> trackingLevels = null;
	
	private RouteInstance routeInstance = null;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public RouteStop getCurrentRouteStop() {
		if (route != null  && route.getStops() != null && route.getStops().size() > currentRouteStopIndex) { 
			currentRouteStop = route.getStops().get(currentRouteStopIndex);
		}
		return currentRouteStop;
	}

	public void setCurrentRouteStop(RouteStop currentRouteStop) {
		this.currentRouteStop = currentRouteStop;
		
		currentRouteStopIndex = route.getStops().indexOf(currentRouteStop);
		
	}

	public int getCurrentRouteStopIndex() {
		return currentRouteStopIndex;
	}

	public void setCurrentRouteStopIndex(int currentRouteStopIndex) {
		this.currentRouteStopIndex = currentRouteStopIndex;
		if (route != null) currentRouteStop = route.getStops().get(currentRouteStopIndex);
	}

	public List<TrackingLevel> getTrackingLevels() {
		return trackingLevels;
	}

	public void setTrackingLevels(List<TrackingLevel> trackingLevels) {
		this.trackingLevels = trackingLevels;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}	

	public RouteInstance getRouteInstance() {
		return routeInstance;
	}

	public void setRouteInstance(RouteInstance routeInstance) {
		this.routeInstance = routeInstance;
	}

	/**
	 * Set current stop to next if there is next stop. 
	 * 
	 * @return if there is next stop
	 */
	public boolean incStop() {
		if (this.getCurrentRouteStopIndex() < route.getStops().size() - 1) {
			currentRouteStopIndex++;
			return true;
		}
		return false;
	}
	
	/**
	 * Set current stop to next if there is next stop. 
	 * 
	 * @return if there is next stop
	 */
	public RouteStop getNextRouteStop() {
		return getNextRouteStopTo(currentRouteStopIndex);
	}
	
	/**
	 * Set current stop to next if there is next stop. 
	 * 
	 * @return if there is next stop
	 */
	public RouteStop getPrevRouteStop() {
		return getPrevRouteStopTo(currentRouteStopIndex);
	}
	
	/**
	 * Set current stop to next if there is next stop. 
	 * 
	 * @return if there is next stop
	 */
	public RouteStop getPrevRouteStopTo(int index) {
		if (index > 0) {			
			return route.getStops().get(index - 1);
		}
		return null;
	}
	
	/**
	 * Set current stop to next if there is next stop. 
	 * 
	 * @return if there is next stop
	 */
	public RouteStop getNextRouteStopTo(int index) {
		if (index < route.getStops().size() - 1) {			
			return route.getStops().get(index + 1);
		}
		return null;
	}

	public boolean decStop() {
		if (currentRouteStopIndex > 0) {
			currentRouteStopIndex--;
			return true;
		} 
		return false;
	}
	
	
	/**
	 * 
	 * @param criteriaId
	 * @return
	 */
	private RouteInstanceDetail getRouteInstanceDetailByStopAndCreteria(int criteriaId, int stopId) {
		for (RouteInstanceDetail detail : this.getRouteInstance().getDetails()) {
			if (detail.getRouteTrackingCriteriaId() == criteriaId && detail.getRouteStopId() == stopId) {
				return detail;
			}
		}
		return null;
	}
	

	/**
	 * Look is there result for counter in DB by <routeStop, criteria> 
	 * @param criteria
	 * @return
	 */
	public RouteInstanceDetail getRouteInstanceDetailForCriteria(RouteTrackingCriteria criteria) {

		RouteInstanceDetail detail = getRouteInstanceDetailByStopAndCreteria(criteria.getRouteTrackingCriteriaId(), this.getCurrentRouteStop().getRouteStopId());

		if (detail == null) {
			detail = new RouteInstanceDetail();
			detail.setRouteDate(Utils.getDotnetDate(new Date()));		
			detail.setRouteInstanceId(this.getRouteInstance().getId());
			detail.setRouteStopId(this.getCurrentRouteStop().getRouteStopId());
			detail.setRouteTrackingCriteriaId(criteria.getRouteTrackingCriteriaId());			
			this.getRouteInstance().getDetails().add(detail);
		}

		return detail;
	}

	public RouteTrackingCriteria getCriteriaById(int routeTrackingCriteriaId) {
		for (TrackingLevel level : trackingLevels) {
			for (RouteTrackingCriteria rtr : level.getCriterias()) {
				if (rtr.getRouteTrackingCriteriaId() == routeTrackingCriteriaId) return rtr;
			}			
		}
		return null;
	}
	
	
}
