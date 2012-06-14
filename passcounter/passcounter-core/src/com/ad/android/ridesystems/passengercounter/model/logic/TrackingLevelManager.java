package com.ad.android.ridesystems.passengercounter.model.logic;




import java.util.List;

import com.ad.android.ridesystems.passengercounter.model.dao.ITrackingLevelDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteTrackingCriteria;
import com.ad.android.ridesystems.passengercounter.model.entities.TrackingLevel;

/**
 * Manager encapsulate logic of RouteStop entity.
 * 
 *
 */
public class TrackingLevelManager extends ABaseEntityManager<TrackingLevel, Integer> {	
	
	/**
	 * 
	 * @param dao DAO instance
	 */
	public TrackingLevelManager(ITrackingLevelDAO dao) {
		super(dao);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TrackingLevel> getAllFull() {
		List<TrackingLevel> list = (dao).getAll();
		for (TrackingLevel level : list) {			
			List<RouteTrackingCriteria> criterias = getManagerHolder().getCriteriaManager().getAllByTrackingLevelId(level.getTrackingLevelId());		
			level.setCriterias(criterias);
		}
		
		return list;		
	}
	
}
