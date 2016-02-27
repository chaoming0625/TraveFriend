package com.gotraveling.insthub.gps.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;

import com.gotraveling.external.activeandroid.util.Log;
import com.gotraveling.insthub.gps.utils.FloatPrefs;
import com.insthub.ecmobile.R;

/**
 * Created by ChaoMing on 2015/4/21.
 */
public class SpeedFloatView extends TextView {
	private float mTouchStartX;
	private float mTouchStartY;
	private WindowManager wm = (WindowManager) getContext()
			.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	private WindowManager.LayoutParams mLayoutParams;
	private float mCurrentX;
	private float mCurrentY;
	private float mBigTxtSize = 18F;
	private float mTxtSize = 14F;

	public SpeedFloatView(Context context) {
		super(context);
		Resources resources = getResources();
		mTxtSize = resources.getDimensionPixelSize(R.dimen.float_txtsize);
		mBigTxtSize = resources
				.getDimensionPixelSize(R.dimen.float_txtsize_big);
	}

	private void updateViewPosition() {
		mLayoutParams.x = (int) (mCurrentX - mTouchStartX);
		mLayoutParams.y = (int) (mCurrentY - mTouchStartY);
		wm.updateViewLayout(this, mLayoutParams);
	}

	public void updata() {
		String tmp = "  ";
		switch (FloatPrefs.mFloatMode) {
		case 0:
			tmp += TrackService.mCourse.CurSpeed();
			break;
		case 1:
			tmp += TrackService.mInfo.Altitude();
			break;
		case 2:
			tmp += TrackService.mCourse.CurSpeed() + "  |  "
					+ TrackService.mInfo.Altitude();
			break;
		}
		tmp += "  ";
		this.setText(tmp);
		this.invalidate();
	}

	public void setViewBackground() {
		switch (FloatPrefs.FloatBackground) {
		case 0:
			setBackgroundColor(Color.argb(150, 255, 255, 255));
			break;
		case 1:
			setBackgroundColor(Color.argb(150, 0, 0, 0));
			break;
		case 2:
			setBackgroundColor(Color.argb(150, 200, 0, 0));
			break;
		case 3:
			setBackgroundColor(Color.argb(150, 200, 200, 0));
			break;
		case 4:
			setBackgroundColor(Color.argb(150, 0, 0, 200));
			break;
		case 5:
			setBackgroundColor(Color.argb(150, 0, 200, 0));
			break;
		case 6:
			setBackgroundResource(R.drawable.katong14);
			break;
		case 7:
			setBackgroundResource(R.drawable.k7);
			break;
		case 8:
			setBackgroundResource(R.drawable.k8);
			break;
		case 9:
			setBackgroundResource(R.drawable.katong9);
			break;
		case 10:
			setBackgroundResource(R.drawable.katong10);
			break;
		case 11:
			setBackgroundResource(R.drawable.katong11);
			break;
		case 12:
			setBackgroundResource(R.drawable.katong12);
			break;
		case 13:
			setBackgroundResource(R.drawable.katong13);
			break;
		case 14:
			setBackgroundColor(Color.argb(0, 0, 255, 255));
			break;
		}
	}

	public void resetView() {
		this.setTextColor(FloatPrefs.FloatTextColor);
		this.setTextSize(mTxtSize);
		switch (FloatPrefs.FloatStyle) {
		case 0:
		case 1:
			setTextSize(mBigTxtSize);
			break;
		case 2:
		case 3:
			setTextSize(mTxtSize);
			break;
		case 4:
			setTextColor(0);
			setBackgroundColor(0);
			break;
		}
		setViewBackground();
		this.invalidate();
	}

	public void createView() {
		this.setFocusable(false);
		this.setTextColor(FloatPrefs.FloatTextColor);
		this.setTextSize(mTxtSize);
		switch (FloatPrefs.FloatStyle) {
		case 0:
		case 1:
			setTextSize(mBigTxtSize);
			break;
		case 2:
		case 3:
			setTextSize(mTxtSize);
			break;
		case 4:
			setTextColor(0);
			setBackgroundColor(0);
			break;
		}
		setViewBackground();
		/* ΪView���ò��� */
		mLayoutParams = new WindowManager.LayoutParams();
		// ����ViewĬ�ϵİڷ�λ��
		mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
		// ����window type
		mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		// ���ñ���Ϊ͸��
		mLayoutParams.format = PixelFormat.RGBA_8888;
		// ע������Ե����ú���Ҫ��FLAG_NOT_FOCUSABLEʹ�������ڲ���ȡ����,�������ø����ԣ���Ļ������λ�õ����Ч��ӦΪ�����޷���ȡ����
		mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		// ������ͼ����ʾλ�ã�ͨ��WindowManager������ͼ��λ����ʵ���Ǹı�(x,y)��ֵ
		mCurrentX = mLayoutParams.x = FloatPrefs.mFloatX;
		mCurrentY = mLayoutParams.y = FloatPrefs.mFloatY;
		// ������ͼ�Ŀ���
		mLayoutParams.width = LayoutParams.WRAP_CONTENT;
		mLayoutParams.height = LayoutParams.WRAP_CONTENT;
		// ����ͼ��ӵ�Window��
		wm.addView(this, mLayoutParams);
	}

	public void removeView() {
		try {
			wm.removeView(this);
			wm = null;
			mLayoutParams = null;
		} catch (Exception exception) {
			Log.e("SpeedFloatView.removeView(): " + exception.getMessage());
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event == null)
			return true;
		// getRawX()��ȡ�����Ļ�����꣬������Ļ���Ͻ�Ϊԭ��
		mCurrentX = event.getRawX();
		mCurrentY = event.getRawY() - 25; // 25��ϵͳ״̬���ĸ߶�
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// getX()��ȡ���View�����꣬���Դ�View���Ͻ�Ϊԭ��
			mTouchStartX = event.getX();
			mTouchStartY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			updateViewPosition();
			break;

		case MotionEvent.ACTION_UP:
			FloatPrefs.mFloatX = (int) (mCurrentX - mTouchStartX);
			FloatPrefs.mFloatY = (int) (mCurrentY - mTouchStartY);
			mTouchStartY = 0.0F;
			mTouchStartX = 0.0F;
			FloatPrefs.SaveData(getContext());
			break;
		}
		return true;
	}
}