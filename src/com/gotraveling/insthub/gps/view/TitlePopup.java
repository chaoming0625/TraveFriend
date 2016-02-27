package com.gotraveling.insthub.gps.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.gotraveling.insthub.gps.utils.ListAdapter;
import com.gotraveling.insthub.gps.utils.Util;
import com.insthub.ecmobile.R;

public class TitlePopup extends PopupWindow {
	private Context mContext;

	protected final int LIST_PADDING = 10;

	private Rect mRect = new Rect();

	private final int[] mLocation = new int[2];

	@SuppressWarnings("unused")
	private int mScreenWidth, mScreenHeight;

	private boolean mIsDirty;

	private int popupGravity = Gravity.NO_GRAVITY;

	private OnItemOnClickListener mItemOnClickListener;

	private ListView mListView;

	private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();

	public TitlePopup(Context context) {

		this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	@SuppressWarnings("deprecation")
	public TitlePopup(Context context, int width, int height) {
		this.mContext = context;

		setFocusable(true);

		setTouchable(true);

		setOutsideTouchable(true);

		mScreenWidth = Util.getScreenWidth(mContext);
		mScreenHeight = Util.getScreenHeight(mContext);

		setWidth(width);
		setHeight(height);
		setBackgroundDrawable(new BitmapDrawable());

		setContentView(LayoutInflater.from(mContext).inflate(
				R.layout.title_popup, null));

		initUI();
	}

	private void initUI() {
		mListView = (ListView) getContentView().findViewById(R.id.title_list);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {

				dismiss();

				if (mItemOnClickListener != null)
					mItemOnClickListener.onItemClick(mActionItems.get(index),
							index);
			}
		});
	
	}

	public void show(View view) {

		view.getLocationOnScreen(mLocation);

		mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),
				mLocation[1] + view.getHeight());

		if (mIsDirty) {
			populateActions();
		}

		showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING
				- (getWidth() / 2), mRect.bottom);
	}

	private void populateActions() {
		mIsDirty = false;

		ListAdapter listadapter = new ListAdapter(mContext, mActionItems);
		mListView.setAdapter(listadapter);

	}

	public void addAction(ActionItem action) {
		if (action != null) {
			mActionItems.add(action);
			mIsDirty = true;
		}
	}

	public void cleanAction() {
		if (mActionItems.isEmpty()) {
			mActionItems.clear();
			mIsDirty = true;
		}
	}

	public ActionItem getAction(int position) {
		if (position < 0 || position > mActionItems.size())
			return null;
		return mActionItems.get(position);
	}

	public void setItemOnClickListener(
			OnItemOnClickListener onItemOnClickListener) {
		this.mItemOnClickListener = onItemOnClickListener;
	}

	public static interface OnItemOnClickListener {
		public void onItemClick(ActionItem item, int position);
	}
	

}