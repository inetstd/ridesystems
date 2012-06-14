package com.ad.android.ridesystems.passengercounter.model.vo.schedule;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

public class RouteSchedule implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4308273855789395306L;
	
	
	private int routeId;	
	int loopsPerHour = 0;
	String startTime = "";
	String endTime = "";
	
	List<RouteStopSchedule> rss = new ArrayList<RouteStopSchedule>();

	/**
	 * @return the rss
	 */
	public List<RouteStopSchedule> getRss() {
		return rss;
	}

	/**
	 * @param rss the rss to set
	 */
	public void setRss(List<RouteStopSchedule> rss) {
		this.rss = rss;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the loopsPerHour
	 */
	public int getLoopsPerHour() {
		return loopsPerHour;
	}

	/**
	 * @param loopsPerHour the loopsPerHour to set
	 */
	public void setLoopsPerHour(int loopsPerHour) {
		this.loopsPerHour = loopsPerHour;
	}

	/**
	 * @return the routeId
	 */
	public int getRouteId() {
		return routeId;
	}

	/**
	 * @param routeId the routeId to set
	 */
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

 

	/**
	 * stopId 
	 */
	HashMap<Integer, List<Date>> schedule = new HashMap<Integer, List<Date>>();
	
	public HashMap<Integer, List<Date>> getSchedule() {
		return schedule;
	}

	public void process() {
		
		try {			
			Date start = new SimpleDateFormat("HH:mm:ss").parse(this.startTime.substring(11));			
			Date end = new SimpleDateFormat("HH:mm:ss").parse(this.endTime.substring(11));					
			int offset = 60 / loopsPerHour;	// in mimutes	
			while (start.getTime() < end.getTime()) {
				long hourTime = start.getTime();							
				for (int i = 0; i < loopsPerHour; i++) {					
					long startDrivingRoute = hourTime + i * offset * 60 * 1000;
					for (RouteStopSchedule rs : this.rss) {
						List<Date> dates = schedule.get(rs.getRouteStopID());
						if (dates == null) {
							dates = new ArrayList<Date>();
							schedule.put(rs.getRouteStopID(), dates);
						}						
						dates.add(new Date(startDrivingRoute + rs.getMinutesAfterStart() * 60 * 1000));						
					}
				}
				start.setTime(start.getTime() + 1000 * 60 * 60); // plus one hour				
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}		
	}
	
	private Date getNearestTimeForRouteStop(Integer routeStopId) {	
		if (routeStopId == null) return null;
		Date closest = null;
		
		List<Date> dates = this.schedule.get(routeStopId);		
		//long nowMills = now.getHours() * 60 * 60 * 1000 + now.getMinutes() * 60 * 1000 + now.getSeconds() * 1000;
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");		
		
		long nowMills;
		try {
			nowMills = df.parse(df.format(new Date())).getTime();
		} catch (ParseException e) {
			Date now = new Date();
			nowMills = now.getHours() * 60 * 60 * 1000 + now.getMinutes() * 60 * 1000;
		}

		if (dates != null) {
			long min = Long.MAX_VALUE;			
			for (Date date : dates) {
				long diff = Math.abs(date.getTime() - nowMills);
				if (diff < min) {
					min = diff;
					closest = date;					
				}
			}
		}			
		return closest;
	}

	public String getNearestSchedule(Integer thisRouteStopId, Integer nextRouteStopId, Integer nextNextRouteStopId) {
		Date d1 = getNearestTimeForRouteStop(thisRouteStopId);
		Date d2 = getNearestTimeForRouteStop(nextRouteStopId);
		Date d3 = getNearestTimeForRouteStop(nextNextRouteStopId);
		String res = "";
		DateFormat df = new SimpleDateFormat("HH:mm");
		if (d1 != null) res += df.format(d1) + ", ";
		if (d2 != null) res += "##" + df.format(d2) + "##, ";
		if (d3 != null) res += df.format(d3) + ", ";
		res = res.substring(0, res.length() - 2);
		return res;
	}

}
