package com.ad.android.ridesystems.passengercounter.views;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.model.vo.SummaryViewVO;

public class SummaryListViewAdapter extends ArrayAdapter<SummaryViewVO> implements Filterable {

	private Context context;
	private List<SummaryViewVO> values;
	
	public SummaryListViewAdapter(Context context, List<SummaryViewVO> objects) {
		super(context, R.layout.layout_summary_list_item, objects);
		values = objects;
		this.context = context;
	}

	public int getCount() {    	
		return values.size();
	}




	public View getView(int position, View convertView, ViewGroup viewGroup) {
		SummaryViewVO entry = values.get(position);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.layout_summary_list_item, null);
		}
		

		((TextView)convertView.findViewById(R.id.summary_list_field)).setText(entry.getName());
		((TextView)convertView.findViewById(R.id.summary_list_value)).setText(entry.getQuantity() + "");
		return convertView;
	}
	

	public SummaryViewVO getItem(int position) {
		return values.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
