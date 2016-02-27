package com.gotraveling.insthub.ecmobile.adapter;
//
//                       __
//                      /\ \   _
//    ____    ____   ___\ \ \_/ \           _____    ___     ___
//   / _  \  / __ \ / __ \ \    <     __   /\__  \  / __ \  / __ \
//  /\ \_\ \/\  __//\  __/\ \ \\ \   /\_\  \/_/  / /\ \_\ \/\ \_\ \
//  \ \____ \ \____\ \____\\ \_\\_\  \/_/   /\____\\ \____/\ \____/
//   \/____\ \/____/\/____/ \/_//_/         \/____/ \/___/  \/___/
//     /\____/
//     \/___/
//
//  Powered by BeeFramework
//

import java.util.List;

import com.gotraveling.insthub.ecmobile.protocol.REGIONS;
import com.insthub.ecmobile.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class F3_RegionPickAdapter extends BaseAdapter {

	private Context context;
	private List<REGIONS> list;
	
	private LayoutInflater inflater;
	
	public F3_RegionPickAdapter(Context context, List<REGIONS> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		 
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		 
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		 
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 
		final ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.f3_region_pick_cell, null);
			holder.name = (TextView) convertView.findViewById(R.id.city_item_name);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		REGIONS regions = list.get(position);
		holder.name.setText(regions.name);
		
		return convertView;
	}
	
	class ViewHolder {
		private TextView name;
		
	}

}
