package com.gotraveling.insthub.gps.view;

import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.gotraveling.external.activeandroid.util.Log;
import com.gotraveling.insthub.gps.utils.Course;
import com.gotraveling.insthub.gps.utils.FavorWriter;
import com.gotraveling.insthub.gps.utils.GpsInfo;
import com.gotraveling.insthub.gps.utils.NaviPrefs;
import com.gotraveling.insthub.gps.utils.PosRecord;
import com.gotraveling.insthub.gps.utils.PosWriter;
import com.gotraveling.insthub.gps.utils.SatelliteInfo;
import com.gotraveling.insthub.gps.utils.ToastUtils;
import com.insthub.ecmobile.R;

/**
 * Created by ChaoMing on 2015/4/17.
 */
public class TrackService extends Service {
	protected final String TAG = "TrackService.";
	protected LocationManager mLocationManager;
	protected MyLocationListener mListener;
	// protected Context mContext;
	// protected String mProvider;
	public static GpsInfo mInfo = new GpsInfo();
	public static Course mCourse = new Course();
	protected PosWriter mWrite = new PosWriter();
	public static boolean mOpened = false;
	public static final String Cmd_Start = "start";
	public static final String Cmd_Stop = "stop";
	public static final String Cmd_Save = "favor";
	public static final String Cmd_Pause = "pause";
	public static final String Cmd_New = "new";
	public static final String MSG_Position = "com.gotraveling.gps.view.POS_RECEIVER";
	public static final String MSG_Satellite = "com.gotraveling.gps.view.SATE_RECEIVER";
	protected Intent mIntentPos = new Intent(MSG_Position);
	protected Intent mIntentSat = new Intent(MSG_Satellite);
	private Timer timer = new Timer();

	public static void Notify(Context context, String cmd) {
		Intent intent = new Intent(context, TrackService.class);
		intent.setAction(cmd);
		context.startService(intent);
	}

	public static void FNotify(Context context) {
		Intent intent = new Intent(context, TrackService.class);
		intent.setAction(Cmd_Stop);
		context.stopService(intent);
	}

	@Override
	public void onCreate() {
		Log.i(TAG + "onCreate()");
		super.onCreate();
		NaviPrefs.Load(this);
		NaviPrefs.LoadData(this);
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String tmp = intent.getAction();
			Log.i(TAG + "onStartCommand(): " + tmp);
			if (tmp != null) {
				if (tmp.equals(Cmd_Start)) {
					try {
						Open();
					} catch (Exception e) {
						Log.i(TAG + "onStartCommand(): open error.", e);
						Close();
					}
				}
				else if (tmp.equals(Cmd_Stop)) {
					Close();
					stopSelf();
					return START_NOT_STICKY;
				} else if (tmp.equals(Cmd_Save)) {
					SaveFavor();
				} else if (tmp.equals(Cmd_Pause)) {
					Close();
				} else if (tmp.equals(Cmd_New)) {
					if (mOpened && (mWrite != null))
						mWrite.NewTrack();
				}
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG + "onDestroy()");
		Close();
		sendBroadcast(mIntentPos);
		sendBroadcast(mIntentSat);
		super.onDestroy();
	}

	protected void SaveFavor() {
		if (mInfo == null)
			return;
		if (!mInfo.good())
			return;
		FavorWriter writer = new FavorWriter();
		try {
			writer.Open();
			writer.Record(mInfo.getRecord());
		} catch (Exception e) {
			Log.i(TAG + "SaveFavor(): save error", e);
		} finally {
			writer.Close();
		}
	}

	private boolean checkgps() {
		boolean providerEnabled = mLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// ��������򷵻���ֵ
		if (providerEnabled == true) {
			// ToastUtils.show(this, R.string.GPSNORMAL);
			Log.i(TAG + "checkgps(): gps is turn on");
			return true;
		} else {
			ToastUtils.show(this, R.string.GPSOPEN);
			// ��ת��GPS������ҳ��
			// Intent intent = new
			// Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			// startActivityForResult(intent, 0); // ��Ϊ������ɺ󷵻ص���ȡ����
			return false;
		}

	}

	private void openGPS() {
		// �½�Criteria��
		Criteria locationcriteria = new Criteria();
		// ���þ�ȷ����
		locationcriteria.setAccuracy(Criteria.ACCURACY_FINE);
		// ���ṩ���θ߶���Ϣ
		locationcriteria.setAltitudeRequired(true);
		// ���ṩ������Ϣ
		locationcriteria.setBearingRequired(true);
		// ������Ӫ�̼Ʒ�
		locationcriteria.setCostAllowed(true);
		// ���õ������Ϊ�ͺķ�
		locationcriteria.setPowerRequirement(Criteria.POWER_LOW);
		// ʹ��getSystemService()�������λ�ù���������
		// locationManager.setTestProviderEnabled("gps", true);
		// ���gps���ܿ���
		if (checkgps()) {
			// mProvider =mLocationManager.getBestProvider(locationcriteria,
			// true);
			// Log.d("TrackService", mProvider);
			// ע��λ�ü�����
			mListener = new MyLocationListener();
			// mLocationManager.requestLocationUpdates(mProvider, 1000,
			// 0,mListener);
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1000, 0, mListener);
			mLocationManager.addGpsStatusListener(mListener);
			mOpened = true;
		}

	}

	private class MyLocationListener implements GpsStatus.Listener,
			LocationListener {

		@Override
		public void onGpsStatusChanged(int event) {
			GpsStatus gpsstatus = mLocationManager.getGpsStatus(null);
			Log.i(TAG + "onGpsStatusChanged(): " + event);
			switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				gpsstatus.getTimeToFirstFix();
				mInfo.fix = 2;
				mInfo.lastFixTime = System.currentTimeMillis();
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				// �õ������յ������ǵ���Ϣ������
				// ���ǵĸ߶Ƚǡ���λ�ǡ�����ȡ���α����ţ������Ǳ�ţ�
				if (mOpened) {
					Iterable<GpsSatellite> allSatellites;
					allSatellites = gpsstatus.getSatellites();
					Iterator it = allSatellites.iterator();
					SatelliteInfo tmp = new SatelliteInfo();
					while (it.hasNext()) {
						GpsSatellite oSat = (GpsSatellite) it.next();
						// Log.d(TAG+": found "+oSat.getPrn());
						tmp.set(oSat);
					}
					// Log.d(TAG + "onGpsStatusChanged(): found satellites " +
					// tmp.inview + " when fix=" + mInfo.fix);
					mInfo.setSatis(tmp);
					mIntentSat.putExtra("status", false);
					sendBroadcast(mIntentSat);
				}
				break;

			case GpsStatus.GPS_EVENT_STARTED:
				// Event sent when the GPS system has started.
				mInfo.fix = 1;
				mInfo.lastFixTime = 0;
				break;

			case GpsStatus.GPS_EVENT_STOPPED:
				// Event sent when the GPS system has stopped.
				mInfo.zero();
				mIntentSat.putExtra("status", true);
				sendBroadcast(mIntentSat);
				break;
			default:
				break;
			}
		}

		/**
		 * ��λ�÷����仯��onLocationChanged����������
		 */
		@Override
		public void onLocationChanged(Location location) {
			Log.i(TAG + ": onLocationChanged");
			if (location != null) {
				mInfo.LocChange(location);
				mCourse.CalcDistance(mInfo);
				PosRecord r = mInfo.getRecord();
				r.speed = mCourse.getSpeed();
				mWrite.Record(r);
				mIntentPos.putExtra("status", false);
				sendBroadcast(mIntentPos);
			}
			// locationManager.removeUpdates(this);
			// locationManager.setTestProviderEnabled("gps", true);
		}

		// �������ṩ�̣��÷���������

		@Override
		public void onProviderDisabled(String provider) {
			Log.i(TAG + "onProviderDisabled(): " + provider);
			mInfo.lastFixTime = 0;
		}

		// �������ṩ�̣��÷���������
		@Override
		public void onProviderEnabled(String provider) {
			Log.i(TAG + "onProviderEnabled(): " + provider);
		}

		// ��״̬�����仯���÷���������
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.i(TAG + "onStatusChanged(): " + status + "");
			mInfo.SATChange(status);
			mIntentPos.putExtra("status", true);
			sendBroadcast(mIntentPos);
		}

	}

	protected class myChecker extends TimerTask {
		public myChecker() {
		}

		@Override
		public void run() {
			if (!mOpened)
				return;
			if (mCourse.fixSpeed()) {
				mIntentPos.putExtra("status", false);
				sendBroadcast(mIntentPos);
			}
		}

	}

	public void Open() throws IOException {
		if (mOpened)
			return;
		if (!mWrite.isReady())
			mWrite.Open();
		mCourse.Open();
		openGPS();
		timer.schedule(new myChecker(), 1000, 1000);
	}

	public void Close() {
		mWrite.Close();
		mCourse.Close(this);
		timer.cancel();
		if (mLocationManager != null) {
			if (mListener != null) {
				mLocationManager.removeGpsStatusListener(mListener);
				mLocationManager.removeUpdates(mListener);
			}
			mListener = null;
		}
		mInfo.zero();
		mOpened = false;
	}
}
