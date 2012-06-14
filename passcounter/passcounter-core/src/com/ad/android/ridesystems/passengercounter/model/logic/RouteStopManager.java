package com.ad.android.ridesystems.passengercounter.model.logic;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.text.format.DateFormat;
import android.util.Log;

import com.ad.android.ridesystems.passengercounter.model.dao.IRouteStopDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteStop;
import com.ad.android.ridesystems.passengercounter.model.vo.schedule.AgencySchedule;
import com.ad.android.ridesystems.passengercounter.model.vo.schedule.RouteSchedule;
import com.ad.android.ridesystems.passengercounter.model.vo.schedule.RouteStopSchedule;

/**
 * Manager encapsulate logic of RouteStop entity.
 * 
 *
 */
public class RouteStopManager extends ABaseEntityManager<RouteStop, Integer> {

	/**
	 * 
	 * @param dao DAO instance
	 */
	public RouteStopManager(IRouteStopDAO dao) {
		super(dao);
	}
	
	/**
	 * 
	 * @param routeId
	 * @return
	 */
	public List<RouteStop> getAllByRouteId(int routeId) {
		return ((IRouteStopDAO) dao).getAllByRouteId(routeId);
	}
	
	public void updateSchedules(AgencySchedule agencySchedule) {
		List<RouteStop> stops = getAll();
		
		for (RouteStop routeStop : stops) {
			routeStop.getRouteId();
			RouteSchedule rs = agencySchedule.getRouteSchedule(routeStop.getRouteId());
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			if (rs != null) {
				String deparures = "";
				List<Date> dates = rs.getSchedule().get(routeStop.getRouteStopId());
				if (dates != null) {
					for (Date date : dates) {
						deparures += df.format(date) + ", ";
					}
				}
				routeStop.setSchedules(deparures);				
				this.update(routeStop);
			}
			
		}
		 	
		
	}
}
