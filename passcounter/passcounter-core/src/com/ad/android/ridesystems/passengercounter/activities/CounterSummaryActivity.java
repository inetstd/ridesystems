package com.ad.android.ridesystems.passengercounter.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.common.Config;
import com.ad.android.ridesystems.passengercounter.common.InAppDebug;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstanceDetail;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteTrackingCriteria;
import com.ad.android.ridesystems.passengercounter.model.entities.TrackingLevel;
import com.ad.android.ridesystems.passengercounter.model.vo.IPCDataVO;
import com.ad.android.ridesystems.passengercounter.model.vo.SummaryViewVO;
import com.ad.android.ridesystems.passengercounter.views.SummaryListViewAdapter;


/**
 * Summary screen
 *
 */
public class CounterSummaryActivity extends ABaseActivity {
	
	/**
	 * 
	 */
	public CounterSummaryActivity() {
		super(R.layout.layout_counter_summary);
	}

	/**
	 * 
	 */
	@Override
	public void setUpViews() {
		
		dataVO = (IPCDataVO) getIntent().getExtras().get(IPCDataVO.KEY);
		
		
		HashMap<Integer, SummaryViewVO> criteriaSummaryViewMap = new HashMap<Integer, SummaryViewVO>();
		List<SummaryViewVO> viewArray = new ArrayList<SummaryViewVO>();
		for (TrackingLevel level : dataVO.getTrackingLevels()) {
			for (RouteTrackingCriteria criteria : level.getCriterias()) {
				SummaryViewVO summary = new SummaryViewVO();
				summary.setTrackingLevelName(level.getDescription());
				summary.setCriteriaName(criteria.getDescription());				
				criteriaSummaryViewMap.put(criteria.getRouteTrackingCriteriaId(), summary);
				viewArray.add(summary);
			}
		}
		for (RouteInstanceDetail detail : dataVO.getRouteInstance().getDetails()) {
			SummaryViewVO summaryViewVO = criteriaSummaryViewMap.get(detail.getRouteTrackingCriteriaId());
			if (summaryViewVO != null) summaryViewVO.addQuantity(detail.getQuantity());
		};
		
		
		ListView listView = (ListView) findViewById(R.id.summary_list);
		listView.setAdapter(new SummaryListViewAdapter(this, viewArray));
		
		listView.getLayoutParams().height = viewArray.size() * 100;
		listView.setLayoutParams(listView.getLayoutParams());	
	}
	
	/**
	 * 
	 */
	@Override
	public void setUpMenu() {
		super.setUpMenu();
		
		Button back = (Button) findViewById(R.id.menu_btn_back);
		Button next = (Button) findViewById(R.id.menu_btn_next);
				
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CounterSummaryActivity.this.finish();	
			}
		});
		
		next.setText("Finish");		
		next.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(CounterSummaryActivity.this);		
				AlertDialog alert = builder.setTitle(R.string.counter_summary_finish_title)
				.setCancelable(true)
				.setItems(R.array.counter_summary_finish_array, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dataVO.getRouteInstance().setFinished(true);
						getManagerHolder().getRouteInstanceManager().update(dataVO.getRouteInstance());
						// see resource R.array.counter_summary_finish_array
						switch (which) {
						case 0: // Start this route again		
							
							// vehicleRoute will be send to WS, means that vehicle is out of route  			
							// saving into local storage. Background service will send it to WS
							InAppDebug.log(getApplicationContext(), "start route again, logout: " + dataVO.getEmployee().getPersonId() + " " + Config.getInstance().getDeviceName());
							getManagerHolder().getVehicleRouteManager().createVehicleRouteOnFinishRoute(dataVO.getEmployee().getPersonId());
							
							// create route instance for new counting
							Intent intent2 = new Intent(CounterSummaryActivity.this, LandingActivity.class);    							
							dataVO.setRouteInstance(null);							
							dataVO.setCurrentRouteStopIndex(0);																		

							intent2.putExtra(IPCDataVO.KEY, dataVO);
							intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // recreate activity  
						    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // kill all activities in ActivityStack after LandingActivity
							startActivity(intent2);
							return;
						case 1: // Select another route

							// vehicleRoute will be send to WS, means that vehicle is out of route  			
							// saving into local storage. Background service will send it to WS
							InAppDebug.log(getApplicationContext(), "select another route, logout: " + dataVO.getEmployee().getPersonId() + " " + Config.getInstance().getDeviceName());
							getManagerHolder().getVehicleRouteManager().
								createVehicleRouteOnFinishRoute(dataVO.getEmployee().getPersonId());							
							
							Intent intent  = new Intent(CounterSummaryActivity.this, LandingActivity.class);
							dataVO.setRoute(null);
							dataVO.setVehicle(null);
							dataVO.setRoute(null);
							dataVO.setRouteInstance(null);
							dataVO.setCurrentRouteStopIndex(0);
							intent.putExtra(IPCDataVO.KEY, dataVO);
							intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // recreate activity  
						    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // kill all activities in ActivityStack after LandingActivity        
						    startActivity(intent);
							return;						
						case 2: // Log out
							logout();
							return;
						}
							
					}
				}).create();
				alert.show();				
			}
		});
		
		
	}
	

}
