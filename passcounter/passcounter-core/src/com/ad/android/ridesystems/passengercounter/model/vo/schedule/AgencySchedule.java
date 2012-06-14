package com.ad.android.ridesystems.passengercounter.model.vo.schedule;


import java.io.Serializable;
import java.util.HashMap;

public class AgencySchedule implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6033084649089579009L;
	
	HashMap<Integer, RouteSchedule> schedules = new HashMap<Integer, RouteSchedule>();

	/**
	 * @return the schedules
	 */
	public RouteSchedule getRouteSchedule(int routeId) {
		return schedules.get(routeId);
	}

	/**
	 * @param schedules the schedules to set
	 */
	public void addRouteSchedule(RouteSchedule schedule) {
		this.schedules.put(schedule.getRouteId(), schedule);
	}

	/**
	 * @return the schedules
	 */
	public HashMap<Integer, RouteSchedule> getSchedules() {
		return schedules;
	}	
	
}
