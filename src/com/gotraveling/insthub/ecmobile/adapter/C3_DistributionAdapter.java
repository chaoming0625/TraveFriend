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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gotraveling.insthub.ecmobile.activity.F2_EditAddressActivity;
import com.gotraveling.insthub.ecmobile.adapter.F0_AddressListAdapter.ViewHolder;
import com.gotraveling.insthub.ecmobile.protocol.ADDRESS;
import com.gotraveling.insthub.ecmobile.protocol.PAYMENT;
import com.gotraveling.insthub.ecmobile.protocol.SHIPPING;
import com.insthub.ecmobile.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class C3_DistributionAdapter extends BaseAdapter {
	
	private Context context;
	private List<SHIPPING> list;
	private LayoutInflater inflater;

	public C3_DistributionAdapter(Context context, List<SHIPPING> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {	
		final ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.c2_payment_cell, null);
			holder.name = (TextView) convertView.findViewById(R.id.payment_item_name);
			holder.fee = (TextView) convertView.findViewById(R.id.payment_item_fee);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.name.setText(list.get(position).shipping_name);
		holder.fee.setText(list.get(position).format_shipping_fee);
		
		return convertView;
	}
	
	class ViewHolder {
		private TextView name;
		private TextView fee;
	}

}
