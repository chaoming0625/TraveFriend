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
		
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.f);
		mBaiduMap.setMapStatus(msu);
		
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		myListener = new MyLocationListenner();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		// 打开gps
		option.setOpenGps(true);
		// 设置坐标类型
		option.setCoorType("bd09ll"); 
		//设置发起定位请求的间隔时间为1000ms 
		option.setScanSpan(1000);
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
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
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
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
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
