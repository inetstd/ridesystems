package com.ad.android.ridesystems.passengercounter.views;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.model.entities.Vehicle;


/**
 * 
 *
 */
public class VehicleSpinnerAdapter extends ArrayAdapter<Vehicle> {
	/** spinner context */
	private Context context;
	/** routes array */
	private List<Vehicle> values;
 
	/**
	 * Adapter for routes vehicles
	 * @param context
	 * @param objects
	 */
	public VehicleSpinnerAdapter(Context context, List<Vehicle> objects) {
		super(context, R.layout.layout_spinner_list_item, objects);
		this.context = context;
		this.values = objects;				
	}
	
	/**
	 * view of selected item
	 */
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		Vehicle entry = getItem(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.layout_spinner_list_view, null);
		}
		if (entry != null) ((TextView) convertView).setText(entry.getName());
		else ((TextView) convertView).setText(R.string.select_route_select_vehicle_default);
		return convertView;
	}

	public int getCount() {    	
		return values.size();
	}

	public Vehicle getItem(int position) {
		Vehicle entry = null;
		try {
			entry = values.get(position);
		} catch (Exception e) {
			entry = null;
		}
		return entry;
	}

	public long getItemId(int position) {		
		return position;

	}
	
}