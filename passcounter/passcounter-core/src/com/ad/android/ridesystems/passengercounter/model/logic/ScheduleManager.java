package com.ad.android.ridesystems.passengercounter.model.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StringReader;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import android.content.Context;
import android.util.Log;

import com.ad.android.ridesystems.passengercounter.common.Config;
import com.ad.android.ridesystems.passengercounter.common.Utils;
import com.ad.android.ridesystems.passengercounter.model.vo.schedule.AgencySchedule;
import com.ad.android.ridesystems.passengercounter.model.vo.schedule.RouteSchedule;
import com.ad.android.ridesystems.passengercounter.model.vo.schedule.RouteStopSchedule;

public class ScheduleManager {

	private static ScheduleManager instance = null;

	private AgencySchedule agencySchedule;

	public static ScheduleManager getInstance() {
		if (instance == null) instance = new ScheduleManager();
		return instance;
	}

	private ScheduleManager() {} 


	public AgencySchedule parse(String xml) throws DocumentException {
		SAXReader reader = new SAXReader();					    // dom4j SAXReader		
		Document document = reader.read(new StringReader(xml)); // dom4j Document
		agencySchedule = new AgencySchedule();
		List schedules = document.selectNodes("//RouteSchedule");						
		for (Object object : schedules) {
			if (object instanceof Element) {
				Element domSchedule = (Element)object;
				String startTime = Utils.getElementStringValue(domSchedule, "StartTime");
				String endTime = Utils.getElementStringValue(domSchedule, "EndTime");
				int routeId = Utils.getElementIntValue(domSchedule, "RouteID");
				int loopsPerHour = Utils.getElementIntValue(domSchedule, "LoopsPerHour");

				RouteSchedule routeSchedule = new RouteSchedule();
				routeSchedule.setRouteId(routeId);
				routeSchedule.setEndTime(endTime);
				routeSchedule.setStartTime(startTime);
				routeSchedule.setLoopsPerHour(loopsPerHour);


				List stopSchdulesNodes = domSchedule.selectNodes("//RouteStopSchedule");

				for (Object oStopSchedule : stopSchdulesNodes) {
					Element domStopSchedule = (Element) oStopSchedule;
					int routeStopID = Utils.getElementIntValue(domStopSchedule, "RouteStopID");
					int minutesAfterStart = Utils.getElementIntValue(domStopSchedule, "MinutesAfterStart");

					RouteStopSchedule rs = new RouteStopSchedule();
					rs.setMinutesAfterStart(minutesAfterStart);
					rs.setRouteStopID(routeStopID);
					routeSchedule.getRss().add(rs);
				}					
				routeSchedule.process();
				agencySchedule.addRouteSchedule(routeSchedule);
			}				
		}
		return agencySchedule;
	}

	public void saveAgencySchedule(Context ctx, AgencySchedule aSchedule) throws IOException {
		FileOutputStream fos = ctx.openFileOutput(Config.C_SCHEDULE_FILE_NAME, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(aSchedule);
		os.close();
	}


	public AgencySchedule readAgencySchedule(Context ctx) {

		try {
			FileInputStream fis = ctx.openFileInput(Config.C_SCHEDULE_FILE_NAME);
			ObjectInputStream is = new ObjectInputStream(fis);
			AgencySchedule aSchedule = (AgencySchedule) is.readObject();
			is.close();
			return aSchedule;
		} catch (FileNotFoundException e) {
			Log.e("sc", e.getMessage());
			e.printStackTrace();
			return null;
		} catch (OptionalDataException e) {
			Log.e("sc", e.getMessage());
			return null;
		} catch (ClassNotFoundException e) {
			Log.e("sc", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("sc", e.getMessage());
			return null;
		}

	}

	/**
	 * @return the agencySchedule
	 */
	public AgencySchedule getAgencySchedule() {
		return agencySchedule;
	}

	/**
	 * @param agencySchedule the agencySchedule to set
	 */
	public void setAgencySchedule(AgencySchedule agencySchedule) {
		this.agencySchedule = agencySchedule;
	}

	/**
	 * @param instance the instance to set
	 */
	public static void setInstance(ScheduleManager instance) {
		ScheduleManager.instance = instance;
	}


}
