package com.gotraveling.insthub.gps.view;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.gotraveling.external.activeandroid.util.Log;
import com.gotraveling.insthub.BeeFramework.activity.MainActivity;
import com.gotraveling.insthub.gps.utils.FloatPrefs;
import com.gotraveling.insthub.gps.utils.NaviPrefs;

public class FloatWindowService extends Service implements
		View.OnLongClickListener {

	private static final String TAG = "FloatWindowService";

	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mLayoutParams;
	private LayoutInflater mLayoutInflater;

	private SpeedFloatView mFloatView;
	private int mCurrentX;
	private int mCurrentY;
	private static int mFloatViewWidth = 50;
	private static int mFloatViewHeight = 80;
	public static final String Cmd_Start = "start";
	public static final String Cmd_Stop = "stop";
	public static final String Cmd_Restart = "restart";
	private MsgReceiver mReceiver;

	public static void Notify(Context context, String cmd) {
		Intent intent = new Intent(context, FloatWindowService.class);
		intent.setAction(cmd);
		context.startService(intent);
	}

	public static void FNotify(Context context) {
		NaviPrefs.mFloatShow = false;
		Intent intent = new Intent(context, FloatWindowService.class);
		intent.setAction(Cmd_Stop);
		context.stopService(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("FloatWindowService.onCreate()");
		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = mWindowManager.getDefaultDisplay(); 
		FloatPrefs.Load(this);
		mReceiver = new MsgReceiver();
	}

	// 需要执行操作在onStartCommand里面来进行操作就可以了。
	// @Override
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String tmp = intent.getAction();
			Log.i("FloatWindowService.onStartCommand(): " + tmp);
			if (tmp != null) {
				if (tmp.equals(Cmd_Start)){
					createView();
				}else if(tmp.equals(Cmd_Restart)){
					if (mFloatView != null)
						mFloatView.resetView();
				}else if (tmp.equals(Cmd_Stop)){
					stopSelf();
					return START_NOT_STICKY;
				}
			}
		}
		// 这里的返回有三种类型，可以自己手动返回。return XXXXX
		return super.onStartCommand(intent, flags, startId);
	}

	/* 由于直接startService(),因此该方法没用 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		Log.i("FloatWindowService.onDestroy()");
		if (mFloatView != null) {
			unregisterReceiver(mReceiver);
			mFloatView.removeView();
		}
		mFloatView = null;
		super.onDestroy();
	}

	private void createView() {
		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = mWindowManager.getDefaultDisplay(); 
		FloatPrefs.Load(this, display.getHeight());
		if (mFloatView == null) {
			mFloatView = new SpeedFloatView(this);
			mFloatView.createView();
			mFloatView.updata();
			mFloatView.setLongClickable(true);
			mFloatView.setOnLongClickListener(this);
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(TrackService.MSG_Position);
			registerReceiver(mReceiver, intentFilter);
		}
	}

	@Override
	public boolean onLongClick(View view) {
//		Intent intent = new Intent(this, MainActivity.class);
//		startActivity(intent);
		return true;
	}

	/**
	 * 
	 * @author ChaoMing
	 *
	 */
	public class MsgReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("FloatWindowService.updata");
			if (mFloatView != null)
				mFloatView.updata();
		}
	}

	private void updateFloatView() {
		mLayoutParams.x = mCurrentX;
		mLayoutParams.y = mCurrentY;
		mWindowManager.updateViewLayout(mFloatView, mLayoutParams);
	}

	private class OnFloatViewTouchListener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			/*
			 * getRawX(),getRawY()这两个方法很重要。通常情况下，我们使用的是getX(),getY()来获得事件的触发点坐标，
			 * 但getX(),getY()获得的是事件触发点相对与视图左上角的坐标；而getRawX(),getRawY()获得的是事件触发点
			 * 相对与屏幕左上角的坐标。由于LayoutParams中的x,y是相对与屏幕的，所以需要使用getRawX(),getRawY()。
			 */
			mCurrentX = (int) event.getRawX() - mFloatViewWidth;
			mCurrentY = (int) event.getRawY() - mFloatViewHeight;
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				updateFloatView();
				break;
			case MotionEvent.ACTION_UP:
				FloatPrefs.mFloatX = mCurrentX;
				FloatPrefs.mFloatY = mCurrentY;
				FloatPrefs.SaveData(FloatWindowService.this);
				break;
			}
			return true;
		}
	}

}
