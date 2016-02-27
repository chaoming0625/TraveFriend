package com.gotraveling.insthub.gps.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gotraveling.external.activeandroid.util.Log;
import com.gotraveling.insthub.BeeFramework.fragment.BaseFragment;
import com.insthub.ecmobile.R;

/**
 * Created by ChaoMing on 2016/1/21.
 */
public class SatelliteFragment extends BaseFragment implements DataFlush {
	protected SatelliteSnrView mSateView = null;
	protected SatelliteCompassView mCompView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_satellite, container,
				false);
		mSateView = (SatelliteSnrView) layout.findViewById(R.id.sate_snr);
		mCompView = (SatelliteCompassView) layout.findViewById(R.id.sate_comp);
		return layout;
	}

	@Override
	public void onDataChange(String act) {
		if (TrackService.MSG_Satellite.equals(act)) {
			if (mSateView != null)
				mSateView.invalidate();
			if (mCompView != null)
				mCompView.invalidate();
		}
	}

	@Override
	public void onSelected() {
		if (mSateView != null)
			mSateView.invalidate();
		if (mCompView != null)
			mCompView.invalidate();
	}

	@Override
	public void onDestroy() {
		Log.i("SatelliteFragment.onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("SatelliteFragment.onResume()");
		if (mSateView != null)
			mSateView.invalidate();
		if (mCompView != null)
			mCompView.invalidate();
	}
}
