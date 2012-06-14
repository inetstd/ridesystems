package com.ad.android.ridesystems.passengercounter.activities;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.common.Config;
import com.ad.android.ridesystems.passengercounter.common.Utils;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstanceDetail;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteStop;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteTrackingCriteria;
import com.ad.android.ridesystems.passengercounter.model.entities.TrackingLevel;
import com.ad.android.ridesystems.passengercounter.model.logic.ScheduleManager;
import com.ad.android.ridesystems.passengercounter.model.vo.IPCDataVO;
import com.ad.android.ridesystems.passengercounter.model.vo.schedule.AgencySchedule;
import com.ad.android.ridesystems.passengercounter.model.vo.schedule.RouteSchedule;
import com.ad.android.ridesystems.passengercounter.views.CounterElement;
import com.ad.android.ridesystems.passengercounter.views.NumericalKeyboard;

/** 
 * 
 * Application main activity.
 * Checks is data fetched ready.
 *
 */
public class CounterActivity extends ABaseActivity {

	/** Label for vehicle name */
	TextView busLabel = null;
	/** Label for route name */
	TextView routeLabel = null;
	/** Label for stop name */
	TextView stopLabel = null;	
 
	/** Label for stop name */
	TextView timeLabel = null;
	TextView departureLabel = null;

	/**
	 * 
	 */
	BroadcastReceiver timeReciever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateTimerWithNow();
		}
	};

	/**
	 * 
	 */
	HashMap<Integer, Button> buttonGroup = new HashMap<Integer, Button>();


	/**
	 * 
	 */
	public CounterActivity() {
		super(null);
		this.mainContentView = R.layout.counter;	
	}


	/**
	 * Base on create method
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		dataVO = (IPCDataVO) getIntent().getExtras().get(IPCDataVO.KEY);

		List<RouteStop> stops = getManagerHolder().getRouteStopManager().getAllByRouteId(dataVO.getRoute().getRouteId());
		dataVO.getRoute().setStops(stops);

		List<TrackingLevel> levels = getManagerHolder().getTrackingLevelManager().getAllFull();		
		dataVO.setTrackingLevels(levels);		

		super.onCreate(savedInstanceState);

		registerReceiver(timeReciever, new IntentFilter(Intent.ACTION_TIME_TICK));
	}


	@Override
	protected void onDestroy() {
		unregisterReceiver(timeReciever);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();		
		updateTimerWithNow();
	}

	private void updateTimerWithNow() {
		if (this.timeLabel != null) timeLabel.setText(DateFormat.format("h:mm a", new Date()) + "");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUpViews() {	    	

		busLabel = (TextView) findViewById(R.id.status_bar_message_left);
		routeLabel = (TextView) findViewById(R.id.status_bar_message_right);
		stopLabel = (TextView) findViewById(R.id.stop_label);
		
		departureLabel = (TextView) findViewById(R.id.departure_label);
		timeLabel = (TextView) findViewById(R.id.time_label);


		if (dataVO.getRoute() != null && routeLabel != null) {
			routeLabel.setText(dataVO.getRoute().getDescription());
		}

		if (dataVO.getVehicle() != null && busLabel != null) {
			busLabel.setText(dataVO.getVehicle().getName());
		}	

		setupMenu();

		setUpTrackingLevels();

		initForCurrentRouteStop();				

		// hide if only one level 
		if (dataVO.getTrackingLevels().size() == 1) {
			findViewById(R.id.tracking_level_container).setVisibility(View.GONE);
		}
	}

	/**
	 * Set stop name, restores counters values from DB (memory)
	 * 
	 */
	private void initForCurrentRouteStop() {
		Log.i("schdule", "initForCurrentRouteStop for " + dataVO.getCurrentRouteStop().getRouteId());
					
		String departures = "";
		
		RouteStop prevStop = dataVO.getPrevRouteStop();
		RouteStop nextStop = dataVO.getNextRouteStop();
		String prevDep = null, 
			   thisDep = null, 
			   nextDep = null;
		
		if (prevStop != null) prevDep = prevStop.getNearesDeparture();
		thisDep = dataVO.getCurrentRouteStop().getNearesDeparture();
		if (nextStop != null) nextDep = nextStop.getNearesDeparture();
		
		
		if (prevDep != null) departures += prevDep + ", ";
		if (thisDep != null) departures += "##" + thisDep + "##, ";
		if (nextDep != null) departures += nextDep + ", ";
		
//		departures = dataVO.getCurrentRouteStop().getNearesDepartureSeries();

		if (departures.length() > 0) {
			departures = departures.substring(0, departures.length() - 2);
			departureLabel.setText(Utils.setSpanBetweenTokens("Departure - " + departures, "##", new StyleSpan(Typeface.BOLD)));
			departureLabel.setVisibility(View.VISIBLE);	
		} else {
			departureLabel.setVisibility(View.GONE);
		}
		
		
		
		stopLabel.setText("Stop: " + dataVO.getCurrentRouteStop().getDescription());
		setUpCurrentTrackingLevelCriterias(dataVO.getTrackingLevels().get(0));
		updateRouteQuantityLabel();
		updateStopQuantityLabel();

	}	

	/**
	 * 
	 * 
	 */
	private void updateStopQuantityLabel() {
		int value = 0;		
		for (RouteInstanceDetail detail : dataVO.getRouteInstance().getDetails()) {
			RouteTrackingCriteria routeTrackingCriteria = dataVO.getCriteriaById(detail.getRouteTrackingCriteriaId()); // get criteria for route instance detail
			boolean isCreteriaInMainCount = (routeTrackingCriteria != null && routeTrackingCriteria.isIncludeInMainCount()); // do we need to add value in maim counter??
			if (isCreteriaInMainCount && detail.getRouteStopId() == dataVO.getCurrentRouteStop().getRouteStopId()) {				 
				value += detail.getQuantity();
			}
		}
		((TextView)findViewById(R.id.quantity_on_stop)).setText(value + "");
	}

	/**
	 * 
	 */
	private void updateRouteQuantityLabel() {
		int value = 0;
		for (RouteInstanceDetail detail : dataVO.getRouteInstance().getDetails()) {
			RouteTrackingCriteria routeTrackingCriteria = dataVO.getCriteriaById(detail.getRouteTrackingCriteriaId()); // get criteria for route instance detail
			boolean isCreteriaInMainCount = (routeTrackingCriteria != null && routeTrackingCriteria.isIncludeInMainCount()); // do we need to add value in maim counter??			
			if (isCreteriaInMainCount) value += detail.getQuantity();
		}
		((TextView)findViewById(R.id.quantity_on_route)).setText(value + "");
	}


	/**
	 * Recreate criterias for tracking levels
	 * @param tl
	 */
	private void setUpCurrentTrackingLevelCriterias(TrackingLevel tl) {
		// clear counters view
		//	trackingLevelLabel.setText(tl.getDescription());
		LinearLayout countersContainer = (LinearLayout) findViewById(R.id.sub_content_view);
		countersContainer.removeAllViews();

		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// if one criteria - init keyboard
		if (tl.getCriterias().size() == 1) {			
			NumericalKeyboard kb = (NumericalKeyboard) layoutInflater.inflate(R.layout.layout_numerical_keyboard, null);			
			final RouteInstanceDetail rtr = getRouteInstanceDetailForCriteria(tl.getCriterias().get(0));

			// init keypad			
			kb.setOnChangeListener(new NumericalKeyboard.OnChangeListener() {			
				@Override
				public void onChange(NumericalKeyboard keypad) {				
					rtr.setQuantity(keypad.getCurrentValue());
					// UPDATE DB VALUE!!!!!!!
					getManagerHolder().getRouteInstanceDetailManager().update(rtr);
					// UPDATE LABELS!!!!!!!
					updateStopQuantityLabel();
					updateRouteQuantityLabel();
				}
			});
			kb.setCurrentValue(rtr.getQuantity());

			countersContainer.addView(kb);
			// if more than one criteria - init counter elements
		} else {
			// for each criteria create a counter element 
			for (RouteTrackingCriteria criteria : tl.getCriterias()) {								

				// looking for previous value
				final RouteInstanceDetail rtr = getRouteInstanceDetailForCriteria(criteria);				

				CounterElement ce = (CounterElement) layoutInflater.inflate(R.layout.layout_counter_element, null);		
				ce.initCounter(criteria, rtr);	
				ce.setChangeListener(new CounterElement.OnChangeListener() {

					@Override
					public void onChange(CounterElement ce) {					
						getManagerHolder().getRouteInstanceDetailManager().update(rtr);					
						updateRouteQuantityLabel();
						updateStopQuantityLabel();
					}
				});

				countersContainer.addView(ce);		
			}			
		}	
		manageButtonGroup(tl.getTrackingLevelId());
	}


	/**
	 * Get route instance detail for criteria. If there is no existing (first time showing) creates and inserts in local storage.
	 * Otherwise quantity of criteria will be set in correct (previous) value  
	 * @param criteria
	 * @return
	 */
	private RouteInstanceDetail getRouteInstanceDetailForCriteria(RouteTrackingCriteria criteria) {
		RouteInstanceDetail rtr = dataVO.getRouteInstanceDetailForCriteria(criteria);
		// if creteria is new insert in local storage
		if (rtr.getId() == 0) getManagerHolder().getRouteInstanceDetailManager().insert(rtr);
		return rtr;
	}


	/**
	 * Set correct button group
	 * @param trackinglevelid
	 */
	private void manageButtonGroup(int trackinglevelid) {
		// clear state
		for (Integer i : buttonGroup.keySet()) buttonGroup.get(i).setSelected(false);
		// set correct
		Button btn = buttonGroup.get(trackinglevelid);
		if (btn != null) btn.setSelected(true);
	}

	/**
	 * Create 
	 */
	public void setUpTrackingLevels() {
		LinearLayout trackingLevelContainer = (LinearLayout) findViewById(R.id.tracking_level_container);
		trackingLevelContainer.removeAllViews();

		for (final TrackingLevel level: dataVO.getTrackingLevels()) {
			Button btn = new Button(this);
			btn.setText(level.getDescription());			
			btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_blue_res_toggle));
			btn.setTextColor(Color.WHITE);
			btn.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					setUpCurrentTrackingLevelCriterias(level);						
				}
			});
			trackingLevelContainer.addView(btn);						
			buttonGroup.put(level.getTrackingLevelId(), btn);
		}		
	}

	/**
	 * Set listeners to bottom menu buttons
	 */
	public void setupMenu() {
		super.setUpMenu();
		Button back = (Button) findViewById(R.id.menu_btn_back);
		Button next = (Button) findViewById(R.id.menu_btn_next);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!dataVO.decStop()) { 
					Intent resultIntent = new Intent();					
					resultIntent.putExtra(IPCDataVO.KEY, dataVO);
					dataVO.setRoute(null);
					dataVO.setRouteInstance(null);
					CounterActivity.this.setResult(RESULT_OK, resultIntent);
					setResult(RESULT_OK, resultIntent);
					CounterActivity.this.finish();	
				} else {
					initForCurrentRouteStop();
				}

			}
		});

		next.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if (dataVO.incStop()) {
					initForCurrentRouteStop();
				} else {
					Intent intent = new Intent(CounterActivity.this, CounterSummaryActivity.class);
					intent.putExtra(IPCDataVO.KEY, dataVO);
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * Add constant to stop id. Because of menuItem should have unique intem.getItemId().
	 * We want to use routestop id (from db) as unique key, but it can be equal to another 
	 * menuitemid (is actual for id = 1,2,3,5 ... 10)  
	 * 
	 * @param id
	 * @return unique id for context id
	 */
	private int prepareShiftedSropIdForMenu(int id) {
		return id + 1000;
	}

	/**
	 * 
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {	  	  
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.clear();
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
		for (RouteStop stop : dataVO.getRoute().getStops()) {
			menu.findItem(R.id.go_to_stop).getSubMenu().add(Menu.NONE, prepareShiftedSropIdForMenu(stop.getId()), stop.getId(), stop.getDescription());
		}	  
	}

	/**
	 * 
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		for (RouteStop stop : dataVO.getRoute().getStops()) {		  		 
			if (item != null && (item.getItemId() == prepareShiftedSropIdForMenu(stop.getId()))) {

				dataVO.setCurrentRouteStop(stop);
				initForCurrentRouteStop();
				return true;
			}			
		}	  	
		switch (item.getItemId()) {
		case R.id.go_to_start:
			dataVO.setCurrentRouteStopIndex(0);
			initForCurrentRouteStop();
			return true;
		case R.id.go_to_end:
			dataVO.setCurrentRouteStopIndex(dataVO.getRoute().getStops().size() - 1);
			initForCurrentRouteStop();
			return true;	
		case R.id.end_the_route:
			Intent intent = new Intent(CounterActivity.this, CounterSummaryActivity.class);
			intent.putExtra(IPCDataVO.KEY, dataVO);
			startActivity(intent);
			return true;
		case R.id.show_map:
			if (Config.getInstance().getWebAddress().length() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(CounterActivity.this);
				builder.setMessage("Can't resolve client web address")
				.setCancelable(false)
				.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();						
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			} else {
				final ProgressDialog progressDialog;
				progressDialog = new ProgressDialog(this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setMessage("Loading...");
				progressDialog.setCancelable(false);
				progressDialog.show();


				// get view
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.layout_web_view, null);

				// setup webview
				WebView webView = (WebView) view.findViewById(R.id.webview);		
				webView.getSettings().setJavaScriptEnabled(true);	


				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				builder.setTitle("Route: " + dataVO.getRoute().getDescription())
				.setCancelable(true)			
				.setNegativeButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				}).setView(view);
				final AlertDialog dialog = builder.create();			

				//			final Dialog dialog = new Dialog(CounterActivity.this);			
				//			dialog.setTitle("Route: " + dataVO.getRoute().getDescription());
				//			dialog.setContentView(view);            
				//			dialog.setCancelable(true);                 

				WebChromeClient client = new WebChromeClient(){			   			    
					@Override
					public void onProgressChanged(WebView view, int progress)   
					{	             			     
						progressDialog.setProgress(progress); //Make the bar disappear after URL is loaded
						// Return the app name after finish loading
						if(progress == 100) {
							progressDialog.dismiss();
							// show dialog						    
							dialog.show();
						}
					}

				};
				webView.setWebChromeClient(client);			
				webView.loadUrl(Config.getInstance().getMapUrl(dataVO.getRoute().getRouteId()));
				return true;
			}			
		default:
			return super.onContextItemSelected(item);
		}
	}


}