package com.gotraveling.insthub.gps.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.gotraveling.external.activeandroid.util.Log;
import com.gotraveling.insthub.gps.utils.SatelliteInfo;
import com.insthub.ecmobile.R;

/**
 * Created by ChaoMing on 2015/5/5.
 */
public class SatelliteSnrView extends View {
	private static final int satColors[] = { 0xffa1673d, 0xffcc824d,
			0xfff28130, 0xffff9900, 0xffbfbf00 };
	private static final int satFixColor = 0xff00cc00;
	private Typeface mFace;
	private int visStyle = 0;
	private Paint textTinyPaint = new Paint(1);
	private Paint textTitlePaint = new Paint(1);
	private Paint textValPaint = new Paint(1);
	private Paint satPaint = new Paint(1);
	private Paint framePaint = new Paint(1);
	int clrActiveSat;
	int clrFrame;
	int clrInactiveSat;
	int clrTextTitle;
	int clrTextValue;
	int colSatWidth = 3;
	private int deviceOrientation = 1;
	float textSizeTiny = 8.0F;
	float textSizeTitle = 12.0F;
	float textSizeVal = 24.0F;;
	float onePix = 1.0F;
	int padding = 5;
	protected String strSATs = "";
	protected String strUsed = "";
	protected String strErrorFt = "";
	protected String strErrorM = "";
	protected String strFixAge;
	private RectF rcfWork = new RectF();
	protected Rect rcWork1 = new Rect();
	protected Rect rcWork2 = new Rect();
	private RectF rcWork = new RectF();
	private float mValueHeight = 0.0F;
	private float mSatNoHeight = 0.0F;
	private float mSnrHeight = 0.0F;
	private float mSatFlgHeight = 0.0F;

	public SatelliteSnrView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Resources resources = getResources();
		strSATs = resources.getString(R.string.gps_info_sats);
		strUsed = resources.getString(R.string.gps_info_used);
		strErrorFt = resources.getString(R.string.gps_info_error_ft);
		strErrorM = resources.getString(R.string.gps_info_error_m);
		strFixAge = resources.getString(R.string.gps_info_fix_age);
		textSizeVal = resources.getDimensionPixelSize(R.dimen.sat_info_value);
		textSizeTitle = resources.getDimensionPixelSize(R.dimen.sat_info_title);
		textSizeTiny = resources.getDimensionPixelSize(R.dimen.sat_info_small);
		onePix = resources.getDimension(R.dimen.one_pixel);
		colSatWidth = (int) (colSatWidth * onePix);
		padding = (int) (padding * onePix);
		initColors(resources);
		satPaint.setStyle(Paint.Style.FILL);
		framePaint.setStrokeWidth(onePix);
		framePaint.setStyle(Paint.Style.STROKE);
		framePaint.setColor(clrFrame);
		textTitlePaint.setColor(clrTextTitle);
		textTitlePaint.setTextSize(textSizeTitle);
		textTitlePaint.setTextAlign(Paint.Align.CENTER);
		textTinyPaint.setColor(-1);
		textTinyPaint.setTextSize(textSizeTiny);
		textTinyPaint.setTextAlign(Paint.Align.CENTER);
		mFace = Typeface.create("sans", 3);
		textValPaint.setTypeface(mFace);
		textValPaint.setColor(clrTextTitle);
		textValPaint.setTextScaleX(0.9F);
		textValPaint.setTextSize(textSizeVal);
		textValPaint.setTextAlign(Paint.Align.CENTER);
		textValPaint.setSubpixelText(true);
		mValueHeight = textSizeVal + textSizeTitle + 2 * padding;
		mSatNoHeight = textSizeTiny * 2 + 2.0F * onePix;
		mSnrHeight = onePix + textSizeTiny + padding;
		mSatFlgHeight = textSizeTiny + onePix;
	}

	private void initColors(Resources resources) {
		clrTextValue = resources.getColor(R.color.gps_info_text);
		clrTextTitle = resources.getColor(R.color.gps_info_text);
		clrActiveSat = resources.getColor(R.color.gps_active_sat);
		clrInactiveSat = resources.getColor(R.color.gps_inactive_sat);
		clrFrame = resources.getColor(R.color.gps_ring_bottom);
	}

	private int calcColumnWidth(String title, String value) {
		textTitlePaint.getTextBounds(title, 0, title.length(), rcWork1);
		textValPaint.getTextBounds(value, 0, value.length(), rcWork2);
		return Math.max(rcWork1.width(), rcWork2.width());
	}

	private int calcSatHeight(float satH) {
		int satBtm = (int) (mValueHeight + mSatNoHeight + mSatFlgHeight + 5F * satH);
		return (int) (satBtm + mSnrHeight);
	}

	@SuppressWarnings("unused")
	private String coordConvertor(double paramDouble) {
		double d = Math.abs(paramDouble);
		int i = (int) Math.floor(d);
		int j = (int) Math.floor(60.0D * (d - i));
		float f = (float) (60.0D * (60.0D * (d - i) - j));
		return String.format("%d��%d'%.2f\"", i, j, f);
	}

	private void drawFrame(Canvas canvas, RectF rectf) {
		framePaint.setStyle(android.graphics.Paint.Style.STROKE);
		framePaint.setStrokeWidth(2.0F * onePix);
		framePaint.setColor(clrFrame);
		canvas.drawRoundRect(rectf, 2 * padding, 2 * padding, framePaint);
		rectf.offset(2.0F * onePix, 2.0F * onePix);
		framePaint.setStyle(android.graphics.Paint.Style.FILL);
		framePaint.setColor(0xff000000);
		framePaint.setAlpha(10);
		canvas.drawRoundRect(rectf, 2 * padding, 2 * padding, framePaint);
		framePaint.setAlpha(255);
	}

	private void drawValue(Canvas canvas, int i, int j, String title,
			String value, int width) {
		drawValueFrame(canvas, i, j, title, textTitlePaint, width);
		canvas.drawText(title, i, j + textSizeTitle, textTitlePaint);
		canvas.drawText(value, i, j + textSizeTitle + textSizeVal,
				textValPaint);
	}

	@SuppressWarnings("unused")
	private void drawValue(Canvas canvas, int x, int y, String name,
			String value, String s2, Paint paint, Paint paint1, int k, int l) {
		drawValueFrame(canvas, x, y, name, paint, k);
		canvas.drawText(name, x, y + textSizeTitle, paint);
		canvas.drawText(value, x + l, y + textSizeTitle + textSizeVal,
				paint1);
		textValPaint.getTextBounds(value, 0, value.length(), rcWork2);
		textTitlePaint.getTextBounds(s2, 0, s2.length(), rcWork1);
		int i1 = 0;
		if (s2 == "\260") {
			i1 = (int) (0 - textSizeTitle / 2.0F);
		}
		canvas.drawText(s2,
				x + rcWork2.width() / 2 + rcWork1.width() / 2 + 2.0F
						* onePix + l, y + textSizeTitle
						+ textSizeVal + i1, paint);
	}

	private void drawValueFrame(Canvas canvas, int i, int j, String s,
			Paint paint, int k) {
		if (visStyle == 1) {
			canvas.getClipBounds();
			paint.getTextBounds(s, 0, s.length(), rcWork1);
			rcWork1.inset(-4, 0);
			rcWork1.offset(2 + (i - rcWork1.width() / 2), j
					+ (int) textSizeTitle);
			rcWork.set(rcWork1);
			canvas.save(2);
			canvas.clipRect(rcWork, android.graphics.Region.Op.DIFFERENCE);
			rcWork.set(i - k / 2,
					(int) (j + 2.0F * (textSizeTitle / 3F)), i + k / 2,
					(int) (4F + (j + textSizeTitle + textSizeVal)));
			canvas.drawRoundRect(rcWork, 3F * onePix, 3F * onePix, framePaint);
			canvas.restore();
		}
	}

	protected int getMinX() {
		int wSat = 10 + calcColumnWidth(strSATs, "00") + 10 / 2;
		int wUsed = 10 + calcColumnWidth(strUsed, "00") + 10 / 2;
		int wErr = 10 + calcColumnWidth(strErrorFt, "0000") + 10 / 2;
		int wFix = 10 + calcColumnWidth(strFixAge, "00:00");

		int width = wFix + wErr + wSat + wUsed + 2 * padding
				+ (int) (6 * onePix);
		int k = (int) ((textSizeTiny + onePix) * 32 + 2 * padding);
		if (width < k)
			width = k;
		k = (int) (onePix * 5 * 32 + 2 * padding);
		if (width < k)
			width = k;
		return width;
	}

	protected int getMinY() {
		return calcSatHeight(onePix * 3);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width = 0;
		int height = 0;
		int mode = 0;
		float f2;
		if (widthMode == View.MeasureSpec.EXACTLY)
			mode += 1;
		if (heightMode == View.MeasureSpec.EXACTLY)
			mode += 2;
		switch (mode) {
		case 0:
			width = getMinX();
			height = getMinY();
			break;
		case 1:
			width = widthSize;
			int k = (width - 2 * padding) / 32;
			int half_width = k / 2 - 1;
			f2 = half_width * 2 + onePix;
			height = calcSatHeight(f2);
			if (height > heightSize)
				height = heightSize;
			break;
		case 2:
			height = heightSize;
			int wSat = 10 + calcColumnWidth(strSATs, "00") + 10 / 2;
			int wUsed = 10 + calcColumnWidth(strUsed, "00") + 10 / 2;
			int wErr = 10 + calcColumnWidth(strErrorFt, "0000") + 10 / 2;
			int wFix = 10 + calcColumnWidth(strFixAge, "00:00");

			width = wFix + wErr + wSat + wUsed + 2 * padding
					+ (int) (6 * onePix);
			f2 = (mValueHeight + mSatNoHeight + mSatFlgHeight + mSnrHeight - heightSize) / 5;
			f2 = -f2 - onePix + 1;
			f2 = f2 * 32 + 2 * padding;
			if (width < f2)
				width = (int) f2;
			if (width > widthSize)
				width = widthSize;
			break;
		case 3:
			width = widthSize;
			height = heightSize;
			break;
		}

		Log.i("SatelliteSnrView.onMeasure(): w=" + width + "(" + widthSize
				+ ")  h = " + height + "( " + heightSize + ")");
		setMeasuredDimension(width, height);
	}

	private void beginUpdate() {

	}

	private void stopUpdate() {

	}

	@Override
	protected void onWindowVisibilityChanged(int paramInt) {
		super.onWindowVisibilityChanged(paramInt);
		if (paramInt == View.VISIBLE) {
			beginUpdate();
		} else
			stopUpdate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int i = getMeasuredWidth();
		int j = getMeasuredHeight();
		int width = (i - 2 * padding) / 32;
		int half_width = width / 2 - 1;
		float satHeight = half_width * 2;
		float f2 = satHeight + onePix;
		int satBtm = (int) (mValueHeight + mSatNoHeight + mSatFlgHeight + 5F * f2);
		float bottom = satBtm + mSnrHeight;
		if (bottom > j) {
			bottom = j - onePix;
			satBtm = (int) (bottom - mSnrHeight - padding / 2);
			f2 = (satBtm - mValueHeight - mSatNoHeight - mSatFlgHeight) / 5;
			satHeight = f2 - onePix;
		}
		int starty = (int) (j - bottom) / 2;
		rcfWork.set(0.0F, starty, i - 2.0F * onePix, j - 2.0F
				* onePix - starty);
		framePaint.setStyle(android.graphics.Paint.Style.STROKE);
		textValPaint.setColor(clrTextTitle);
		textTitlePaint.setColor(clrTextTitle);
		if (visStyle == 1) {
			framePaint.setStrokeWidth(2.0F * onePix);
			// rcfWork.set(onePix, onePix+starty, (float)i - 2.0F * onePix,
			// (float)j - 2.0F * onePix);
			rcfWork.set(onePix, onePix + starty, i, j - starty);
			framePaint.setStyle(android.graphics.Paint.Style.FILL);
			framePaint.setColor(0xff000000);
			textValPaint.setAlpha(196);
			framePaint.setStrokeWidth(onePix);
			framePaint.setStyle(android.graphics.Paint.Style.STROKE);
			framePaint.setColor(clrFrame);
		} else {
			rcfWork.set(1.0F, 1.0F + starty, i - 2.0F * onePix,
					j - 2.0F * onePix - starty);
			// rcfWork.bottom = (rcfWork.top + 3F * (rcfWork.height() / 4F)) -
			// 2.0F * onePix;
			drawFrame(canvas, rcfWork);
		}
		int snap = padding + starty;
		if (visStyle == 1)
			snap = starty;

		int wSat = 10 + calcColumnWidth(strSATs, "00") + 10 / 2;
		int wUsed = 10 + calcColumnWidth(strUsed, "00") + 10 / 2;
		int wErr = 10 + calcColumnWidth(strErrorFt, "0000") + 10 / 2;
		int wFix = 10 + calcColumnWidth(strFixAge, "00:00");
		int col_snap = (i - (wFix + wErr + wSat + wUsed) - 2 * padding) / 3;
		float left, right;
		drawValue(canvas, wSat / 2 + padding, snap, strSATs,
				String.valueOf(TrackService.mInfo.getSatinfo().inview), wSat);
		left = rcWork.left;
		drawValue(canvas, wSat + padding + col_snap + wUsed / 2, snap, strUsed,
				String.valueOf(TrackService.mInfo.getSatinfo().inuse), wUsed);

		drawValue(canvas, wSat + padding + col_snap * 2 + wUsed + wErr / 2,
				snap, strErrorM, TrackService.mInfo.DOP(), wErr);
		String s13;
		if (!TrackService.mInfo.good()) {
			s13 = "--";
		} else {
			long l11 = System.currentTimeMillis()
					- TrackService.mInfo.lastFixTime;
			Object aobj[] = new Object[2];
			aobj[0] = Long.valueOf(l11);
			aobj[1] = Long.valueOf(l11);
			s13 = String.format("%tM:%tS", aobj);
		}
		drawValue(canvas, i - wFix / 2 - padding, snap, strFixAge, s13, wFix);

		int startx = width / 2;
		right = rcWork.right;
		if (visStyle == 1) {
			rcWork.set(left, mValueHeight - 2.0F * onePix + starty, right,
					(j) - 2.0F * onePix - starty);
			canvas.drawRoundRect(rcWork, 3F * onePix, 3F * onePix, framePaint);
			startx = (int) (left + onePix);
		}
		startx = startx + (int) (3 * onePix);
		SatelliteInfo info = TrackService.mInfo.getSatinfo();
		satBtm += starty;
		starty += mValueHeight;
		for (i = 0; i < 32; i++, startx += width) {
			String satno = String.valueOf(i + 1);
			if (info.sat[i].id < 1) {
				textTinyPaint.setColor(clrInactiveSat);
				canvas.drawText(satno, startx, starty + textSizeTiny
						* (1 + i % 2), textTinyPaint);
				continue;
			}
			textTinyPaint.setColor(clrActiveSat);
			canvas.drawText(satno, startx, starty + textSizeTiny
					* (1 + i % 2), textTinyPaint);
			satno = String.valueOf((int) (info.sat[i].sig % 100));
			canvas.drawText(satno, startx, satBtm + onePix + textSizeTiny,
					textTinyPaint);
			float y1 = satBtm;
			for (int k14 = 0; k14 < 5; k14++, y1 -= f2) {
				if (info.sat[i].sig > (k14 * 10)) {
					if (info.sat[i].in_use == 1) {
						satPaint.setColor(satFixColor);
					} else {
						satPaint.setColor(satColors[k14]);
					}
					satPaint.setAlpha(255);
					canvas.drawRect(startx - half_width, y1 - satHeight, startx
							+ half_width, y1, satPaint);
				}
			}
			if (info.sat[i].in_use == 1) {
				if (info.sat[i].hasAlm)
					canvas.drawText("e", startx, y1, textTinyPaint);
				else
					canvas.drawText("a", startx, y1, textTinyPaint);
			}
		}

	}
}
