package com.ad.android.ridesystems.passengercounter.views;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.model.entities.Route;

/**
 * 
 *
 */
public class RouteSpinnerAdapter extends ArrayAdapter<Route> {
	/** spinner context */
	private Context context;
	/** routes array */
	private List<Route> values;

	/**
	 * Adapter for routes array
	 * @param context
	 * @param objects
	 */
	public RouteSpinnerAdapter(Context context, List<Route> objects) {
		super(context, R.layout.layout_spinner_list_item, objects);
		this.context = context;
		this.values = objects;			
	}
	
	/**
	 * view of selected item
	 */
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		Route entry = getItem(position);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.layout_spinner_list_view, null);
		}
		if (entry != null) ((TextView) convertView).setText(entry.getDescription());
		else ((TextView) convertView).setText(R.string.select_route_select_route_default);		
		return convertView;
	}

	/**
	 * view of drop down
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {				
		Route entry = values.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.layout_spinner_list_item_with_color, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.spinner_list_item_value);
		TextView color = (TextView) convertView.findViewById(R.id.spinner_list_item_color);

		item.setText(entry.getDescription());				
		if (!entry.getMapLineColor().trim().equals("")) {
			color.setBackgroundColor(Color.parseColor(entry.getMapLineColor().replace("#", "#FF")));
		}
				

		return convertView;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getCount() {    	
		return values.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public Route getItem(int position) {
		Route entry = null;
		try {
			entry = values.get(position);
		} catch (Exception e) {
			entry = null;
		}
		return entry;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getItemId(int position) {		
		return position;

	}

}