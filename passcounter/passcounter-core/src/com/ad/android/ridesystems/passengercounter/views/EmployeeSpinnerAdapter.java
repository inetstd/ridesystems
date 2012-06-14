package com.ad.android.ridesystems.passengercounter.views;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.model.entities.Employee;

/**
 * 
 *
 */
public class EmployeeSpinnerAdapter extends ArrayAdapter<Employee> {
	/** spinner context */
	private Context context;
	/** routes array */
	private List<Employee> values;	
	/**
	 * Adapter for routes array
	 * @param context
	 * @param objects
	 */
	public EmployeeSpinnerAdapter(Context context, List<Employee> objects) {		
		super(context, R.layout.layout_spinner_list_item, objects);		
 
		this.context = context;
		this.values = objects;			
	}
	
 
	
	/**
	 * view of selected item
	 */
	public View getView(int position, View convertView, ViewGroup viewGroup) {		
		Employee entry = getItem(position);		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.layout_spinner_list_view, null);
		}
		
		if (entry != null) ((TextView) convertView).setText(entry.getFirstName());
		else ((TextView)convertView).setText("Please select");
		
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
	public Employee getItem(int position) {
		Employee entry = null;
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