package com.ad.android.ridesystems.passengercounter.model.vo.schedule;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteStopSchedule implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3599160475535305780L;
	
	
	int routeStopID;
	int minutesAfterStart;	
	List<Date> times = new ArrayList<Date>();
	
	/**
	 * @return the routeStopID
	 */
	public int getRouteStopID() {
		return routeStopID;
	}

	/**
	 * @param routeStopID the routeStopID to set
	 */
	public void setRouteStopID(int routeStopID) {
		this.routeStopID = routeStopID;
	}

	/**
	 * @return the minutesAfterStart
	 */
	public int getMinutesAfterStart() {
		return minutesAfterStart;
	}

	/**
	 * @param minutesAfterStart the minutesAfterStart to set
	 */
	public void setMinutesAfterStart(int minutesAfterStart) {
		this.minutesAfterStart = minutesAfterStart;
	}

	/**
	 * @return the times
	 */
	public List<Date> getTimes() {
		return times;
	}

	/**
	 * @param times the times to set
	 */
	public void setTimes(List<Date> times) {
		this.times = times;
	}
	
	public void addTime(Date date) {
		times.add(date);
	}
	
	public Date getClosest() {		
		return times.get(0);
	}
}
