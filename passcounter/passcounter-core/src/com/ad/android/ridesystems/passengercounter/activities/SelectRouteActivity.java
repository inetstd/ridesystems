package com.ad.android.ridesystems.passengercounter.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.model.entities.Route;
import com.ad.android.ridesystems.passengercounter.model.entities.Vehicle;
import com.ad.android.ridesystems.passengercounter.model.vo.IPCDataVO;
import com.ad.android.ridesystems.passengercounter.views.RouteSpinnerAdapter;
import com.ad.android.ridesystems.passengercounter.views.VehicleSpinnerAdapter;

/**
 * 
 * Application main activity.
 * Checks is data fetched ready.
 *
 */
public class SelectRouteActivity extends ABaseActivity {

	private TextView driver = null;
	
	private Spinner routes = null;
	
	private Spinner vehicles = null;	
	
	Button submitBtn = null;
	
	AlertDialog alert = null;
	
	/**
	 * Constructor. Pass view template. 
	 */
	public SelectRouteActivity() {
		super(R.layout.layout_select_route);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUpViews() {	  		
		dataVO = (IPCDataVO) getIntent().getSerializableExtra(IPCDataVO.KEY);
		
		driver = (TextView) findViewById(R.id.driver);
		routes = (Spinner) findViewById(R.id.routes_list);
		vehicles = (Spinner) findViewById(R.id.vehicles_list);
		
		
		driver.setText(dataVO.getEmployee().getFirstName());
				
		ArrayAdapter<Route> adapter = new RouteSpinnerAdapter(this, getManagerHolder().getRouteManager().getAll());
		routes.setAdapter(adapter);		
		routes.setSelection(Integer.MAX_VALUE); // set default message as first item
		routes.setOnTouchListener(new View.OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				routes.setSelection(0);
				return false;
			}
		});
		
		ArrayAdapter<Vehicle> vehiclesAdapter = new VehicleSpinnerAdapter(this, getManagerHolder().getVehicleManager().getAll());		
		vehicles.setAdapter(vehiclesAdapter);
		vehicles.setSelection(Integer.MAX_VALUE); // set default message as first item
		vehicles.setOnTouchListener(new View.OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				vehicles.setSelection(0);
				return false;
			}
		});
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(SelectRouteActivity.this);
		builder.setMessage(R.string.select_route_select_route_message)
		.setCancelable(false)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				tryValidateAndSubmit();
				
			}
		})
		.setNegativeButton("Log out", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});	    	
		alert = builder.create();		
		
	}
	
	/**
	 * Override bottom menu.
	 */
	@Override
	public void setUpMenu() {
		super.setUpMenu();
		
		Button back = (Button) findViewById(R.id.menu_btn_back);
		Button next = (Button) findViewById(R.id.menu_btn_next);
		
		if (back != null) {
			
			back.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent resultIntent = new Intent();					
					resultIntent.putExtra(IPCDataVO.KEY, dataVO);
					dataVO.setEmployee(null);
					SelectRouteActivity.this.setResult(RESULT_OK, resultIntent);
					setResult(RESULT_OK, resultIntent);
					SelectRouteActivity.this.finish();					
				}
			});
		}
		if (next != null) {
			next.setText("Start");
			next.setOnClickListener(submitBtnOnclick);
		}
		
		
	}

	/**
	 * 
	 * submit handler
	 */
	private View.OnClickListener submitBtnOnclick = new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
			tryValidateAndSubmit();			
		}		
	};
		

	/**
	 * Validation and submit method
	 */
	public void tryValidateAndSubmit() {
		Route route = (Route) routes.getSelectedItem();
		Vehicle vehicle = (Vehicle) vehicles.getSelectedItem();
		if (route == null) {
			alert.setMessage(getResources().getString(R.string.select_route_select_route_message));
			alert.show();
		} else if (vehicle == null) {
			alert.setMessage(getResources().getString(R.string.select_route_select_vehicle_message));
			alert.show();
		} else {
			Intent resultIntent = new Intent();
			dataVO.setVehicle(vehicle);
			dataVO.setRoute(route);
			resultIntent.putExtra(IPCDataVO.KEY, dataVO);			
			SelectRouteActivity.this.setResult(RESULT_OK, resultIntent);
			setResult(RESULT_OK, resultIntent);
			SelectRouteActivity.this.finish();
		}
		
		
	}
	
}