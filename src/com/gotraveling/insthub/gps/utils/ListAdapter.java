package com.gotraveling.insthub.gps.utils;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gotraveling.insthub.gps.view.ActionItem;
import com.insthub.ecmobile.R;

public class ListAdapter extends BaseAdapter {
	Context context;
	private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();

	public ListAdapter(Context context, ArrayList<ActionItem> mActionItems) {
		this.context = context;
		this.mActionItems = mActionItems;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.listviewitem,
					null);
		}

		TextView textView = (TextView) view
				.findViewById(R.id.list_view_item_tv);
		ActionItem item = mActionItems.get(position);
		textView.setTextSize(14);
		// �����ı�����
		textView.setGravity(Gravity.CENTER);
		// �����ı���ķ�Χ
		textView.setPadding(0, 10, 0, 10);
		// �����ı���һ������ʾ�������У�
		textView.setSingleLine(true);
		// �����ı�����
		textView.setText(item.mTitle);
		// ����������ͼ��ļ��
		textView.setCompoundDrawablePadding(10);
		return view;
	}

	@Override
	public int getCount() {
		return mActionItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mActionItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}