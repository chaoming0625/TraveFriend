package com.gotraveling.insthub.gps.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.gotraveling.insthub.BeeFramework.activity.BaseActivity;
import com.gotraveling.insthub.gps.view.TitlePopup.OnItemOnClickListener;
import com.insthub.ecmobile.R;

public class LocationActivity extends BaseActivity implements OnClickListener,
		OnItemOnClickListener {

	private LocationClient mLocClient;
	private MyLocationListenner myListener;
	private LocationMode mCurrentMode;
	private ImageButton imageButton;
	private TitlePopup titlePopup;
	private BitmapDescriptor mCurrentMarker;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Button requestLocButton;
	private boolean isFirstLoc = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_location);
		imageButton = (ImageButton) findViewById(R.id.imagebutton);
		requestLocButton = (Button) findViewById(R.id.button1);
		mCurrentMode = LocationMode.NORMAL;
		requestLocButton.setText(R.string.mode_putong);
		OnClickListener btnClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					requestLocButton.setText(R.string.mode_gensui);
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					requestLocButton.setText(R.string.mode_putong);
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					requestLocButton.setText(R.string.mode_luopan);
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.f);
		mBaiduMap.setMapStatus(msu);

		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(this);
		myListener = new MyLocationListenner();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000); // ���÷���λ����ļ��ʱ��Ϊ1000ms
		mLocClient.setLocOption(option);
		mLocClient.start();

		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titlePopup.addAction(new ActionItem(this, R.string.map_zhengchang));
		titlePopup.addAction(new ActionItem(this, R.string.map_weixing));
		titlePopup.addAction(new ActionItem(this, R.string.map_traffic));
		titlePopup.addAction(new ActionItem(this, R.string.map_heat));

		imageButton.setOnClickListener(this);
		titlePopup.setItemOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}
	}

	@Override
	protected void onPause() {
		// MapView������������Activityͬ������activity����ʱ�����MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView������������Activityͬ������activity�ָ�ʱ�����MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.imagebutton:
			titlePopup.show(view);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(ActionItem item, int position) {

		switch (position) {
		case 0:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			mBaiduMap.setBaiduHeatMapEnabled(false);
			mBaiduMap.setTrafficEnabled(false);
			break;
		case 1:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			mBaiduMap.setBaiduHeatMapEnabled(false);
			mBaiduMap.setTrafficEnabled(false);
			break;
		case 2:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			mBaiduMap.setBaiduHeatMapEnabled(false);
			mBaiduMap.setTrafficEnabled(true);
			break;
		case 3:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			mBaiduMap.setTrafficEnabled(false);
			mBaiduMap.setBaiduHeatMapEnabled(true);
			break;
		}
	}
}
