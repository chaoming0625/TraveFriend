package com.gotraveling.insthub.gps.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.View;

import com.gotraveling.external.activeandroid.util.Log;
import com.gotraveling.insthub.gps.utils.SatelliteInfo;
import com.insthub.ecmobile.R;

/**
 * Created by ChaoMing on 2015/5/5.
 */
public class SatelliteCompassView extends View {

	private static final int satColors[] = { 0xff999999, 0xffcc824d,
			0xfff28130, -26368, 0xffbfbf00 };
	private static final int satFixColor = 0xff00cc00;
	private static boolean m3D = false;
	private int visStyle = 0;
	float textSizeTiny = 8.0F;
	float onePix = 1.0F;
	float twoPix;
	float reticleLineStroke = 5F;
	protected int strokeWidth = 2;
	int padding = 5;
	int satRadius = 3;
	int satRadiusBig = 5;
	float north = 0.0F;
	int clrReticle;
	int clrRingBottom;
	int clrRingEdge;
	int clrRingTop;
	int clrInnerBottom;
	int clrInnerTop;
	int clrSatText;
	int clrMarks;
	private Typeface mFace;
	protected float fontReticleSize = 10F;
	protected float fontTitleSize = 12F;
	protected float gaugePointsBig[] = new float[16];
	protected float gaugePointsT[] = new float[16];
	protected float arrowPointsBig[] = new float[4];
	protected float arrowPointsT[] = new float[4];
	protected float markPoints[] = new float[8];
	protected float markPointsT[] = new float[8];
	protected Matrix mtxGauge = new Matrix();
	protected Matrix mtxRing = new Matrix();
	private Paint reticlePaint = new Paint(1);
	private Paint arrowPaint = new Paint(1);
	private Paint ringEdgePaint = new Paint(1);
	private Paint ringPaint = new Paint(1);
	private Paint ringInternalPaint = new Paint(1);
	private Paint satTextPaint = new Paint(1);
	private Paint textPaint = new Paint(1);
	private Paint satPaint = new Paint(1);
	private Paint satFixPaint = new Paint(1);
	Path pathReticle30 = new Path();
	Path pathReticle30T = new Path();
	Path pathReticle60 = new Path();
	Path pathReticle60T = new Path();
	// private int widthBig;
	// private int heightBig;
	protected int radiusExternalBig = 1;
	protected int radiusInternalBig = 1;
	protected RectF boundingBox = new RectF();
	protected RectF innerBoundingBox = new RectF();
	protected Bitmap mBigImage = null;

	public SatelliteCompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Resources resources = getResources();
		textSizeTiny = resources.getDimensionPixelSize(R.dimen.sat_info_small);
		onePix = resources.getDimension(R.dimen.one_pixel);
		twoPix = 2.0F * onePix;
		padding = (int) (padding * onePix);
		reticleLineStroke = 5F * onePix;
		strokeWidth = (int) (2.0F * onePix);

		satRadiusBig = (int) (satRadiusBig * onePix);
		satRadius = (int) (satRadius * onePix);
		fontReticleSize = resources
				.getDimensionPixelSize(R.dimen.sat_reticle_size);
		fontTitleSize = resources.getDimensionPixelSize(R.dimen.sat_title_size);

		clrRingEdge = resources.getColor(R.color.gps_ring_bottom);
		clrRingTop = resources.getColor(R.color.gps_ring_top);
		clrRingBottom = resources.getColor(R.color.gps_ring_bottom);
		clrInnerTop = resources.getColor(R.color.gps_inner_top);
		clrInnerBottom = resources.getColor(R.color.gps_inner_bottom);

		clrReticle = resources.getColor(R.color.gps_reticle);
		clrSatText = resources.getColor(R.color.gps_sat_text);
		clrMarks = resources.getColor(R.color.gps_marks);
		int clrText = resources.getColor(R.color.waypoint_text);

		if (visStyle == 1)
			clrText = clrRingEdge;

		mFace = Typeface.create("sans", 3);

		ringEdgePaint.setColor(clrRingEdge);
		ringEdgePaint.setStrokeWidth(strokeWidth);
		ringEdgePaint.setStyle(android.graphics.Paint.Style.STROKE);

		textPaint.setTypeface(mFace);
		textPaint.setColor(clrText);
		textPaint.setTextSize(fontTitleSize);
		textPaint.setSubpixelText(true);
		textPaint.setTextAlign(android.graphics.Paint.Align.CENTER);

		satTextPaint.setTypeface(mFace);
		satTextPaint.setTextSize(fontTitleSize);
		satTextPaint.setSubpixelText(true);
		satTextPaint.setTextSkewX(-0.1F);
		satTextPaint.setTextAlign(android.graphics.Paint.Align.LEFT);
		satTextPaint.setShadowLayer(twoPix, onePix, onePix,
				Color.argb(220, 0, 0, 0));

		satPaint.setColor(resources.getColor(R.color.text_color));
		satPaint.setStyle(android.graphics.Paint.Style.FILL);
		satPaint.setShadowLayer(3F * onePix, onePix, onePix,
				Color.argb(128, 0, 0, 0));

		satFixPaint.setColor(satFixColor);
		satFixPaint.setStyle(android.graphics.Paint.Style.STROKE);
		satFixPaint.setStrokeWidth(3F * onePix);

		reticlePaint.setColor(clrReticle);
		reticlePaint.setStrokeWidth(onePix);
		reticlePaint.setStyle(android.graphics.Paint.Style.STROKE);
		reticlePaint.setShadowLayer(twoPix, onePix, onePix,
				Color.argb(128, 0, 0, 0));
		reticlePaint.setTextSize(fontReticleSize);

		arrowPaint.setColor(Color.argb(255, 192, 0, 0));
		arrowPaint.setStrokeWidth(3F * onePix);
		arrowPaint.setStyle(android.graphics.Paint.Style.STROKE);

		// int bigViewWidth =
		// resources.getDimensionPixelSize(R.dimen.compass_big_width);
		// int bigViewHeight =
		// resources.getDimensionPixelSize(R.dimen.compass_big_height);
		// generateBigImage(bigViewWidth,bigViewHeight);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
		int width = 0;
		int height = 0;
		int mode = 0;
		if (widthMode == View.MeasureSpec.EXACTLY)
			mode += 1;
		if (heightMode == View.MeasureSpec.EXACTLY)
			mode += 2;
		switch (mode) {
		case 0:
			width = height = 500;
			break;
		case 1:
			width = widthSize;
			height = Math.min(width, heightSize);
			break;
		case 2:
			height = heightSize;
			width = Math.min(widthSize, height);
			break;
		case 3:
			width = widthSize;
			height = heightSize;
			break;
		}
		Log.i("SatelliteCompassView.onMeasure(): w=" + width + "(" + widthSize
				+ ")  h = " + height + "( " + heightSize + ")");
		setMeasuredDimension(width, height);
		int i1 = Math.min(width, height);
		mtxGauge.reset();
		mtxGauge.setTranslate(width / 2, i1 / 2);
		pathReticle60.transform(mtxGauge, pathReticle60T);
		pathReticle30.transform(mtxGauge, pathReticle30T);
		generateBigImage(width * 4 / 5, height * 4 / 5);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBigGPS(canvas, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
	}

	private void generateBigImage(int widthBig, int heightBig) {
		int j = Math.min(widthBig, heightBig);
		float scaleBig;
		int outerRectSizeBig = 215;
		int innerRectSizeBig = 190;
		scaleBig = (float) (j - 6) / (float) outerRectSizeBig;
		int k = j / 2;
		int l = j / 2;
		int i1 = Math.round((outerRectSizeBig * scaleBig) / 2.0F);
		int j1 = Math.round((innerRectSizeBig * scaleBig) / 2.0F);
		boundingBox.set(k - i1, l - i1, k + i1, l + i1);
		innerBoundingBox.set(k - j1, l - j1, k + j1, l + j1);
		Canvas canvas;
		// if (mBigImage == null) {
		mBigImage = Bitmap.createBitmap((int) boundingBox.width() + 2
				* strokeWidth, (int) boundingBox.height() + 2 * strokeWidth,
				android.graphics.Bitmap.Config.ARGB_8888);
		// } else {
		// mBigImage.eraseColor(Color.argb(0, 0, 0, 0));
		// }

		canvas = new Canvas(mBigImage);
		radiusExternalBig = i1;
		radiusInternalBig = j1 + 2;
		generateBigGpsImage(k, l, i1, j1, canvas);
	}

	private void drawMarkPoints(Canvas canvas, float f, int i, int j, int k,
			boolean flag, Paint paint) {
		float f1 = 2.0F;
		if (visStyle == 1)
			f1 = 3F;

		if (flag) {
			markPoints[0] = i;
			markPoints[1] = j - f;
			markPoints[2] = i;
			markPoints[3] = (j - f) + 2.0F * f1 * onePix;
			markPoints[4] = i;
			markPoints[5] = (j - f) + 3F * f1 * onePix;
			markPoints[6] = i;
			markPoints[7] = (j - f) + 4F * f1 * onePix;
		} else {
			markPoints[0] = i;
			markPoints[1] = (j - f) + 4F * f1 * onePix;
			markPoints[2] = i;
			markPoints[3] = (j - f) + 2.0F * f1 * onePix;
			markPoints[4] = i;
			markPoints[5] = (j - f) + f1 * onePix;
			markPoints[6] = i;
			markPoints[7] = j - f;
		}
		Rect rect = new Rect();
		for (int l = 0; l < 360; l += k) {
			mtxRing.reset();
			mtxRing.setRotate(l, i, j);
			mtxRing.mapPoints(markPointsT, markPoints);

			if ((!flag || (l + k) % 90 != 0 && (l - k) % 90 != 0)
					&& (visStyle != 1 || l == 0 || !flag || (l + k * 2) % 180 != 0
							&& (l - k * 2) % 180 != 0)) {
				if (l % 90 == 0) {
					if (flag) {
						String s = String.valueOf(l);
						paint.setStyle(android.graphics.Paint.Style.FILL);
						float f2 = 4F * onePix;
						if (l == 180)
							f2 = 0.0F;
						if (l == 0)
							f2 *= 2.0F;

						float f3 = 0.0F;
						if (l == 270) {
							f3 = 2.0F * onePix;
							if (visStyle == 1)
								f3 *= 3F;

						}
						if (!m3D) {
							paint.getTextBounds(s, 0, s.length(), rect);
							rect.inset(-2, -2);
							rect.offset(
									(int) (f3 + markPointsT[4]) - rect.width()
											/ 2, (int) (markPointsT[5] + (paint
											.getTextSize() - f2) / 2.0F));
							int i1 = paint.getColor();
							paint.setColor(0xff000000);
							canvas.drawRect(rect, paint);
							paint.setColor(i1);
						}
						canvas.drawText(s, f3 + markPointsT[4], markPointsT[5]
								+ (paint.getTextSize() - f2) / 2.0F, paint);
						paint.setStyle(android.graphics.Paint.Style.STROKE);
					} else {
						canvas.drawLine(markPointsT[0], markPointsT[1],
								markPointsT[6], markPointsT[7], paint);
					}
				} else if (l % 30 == 0) {
					canvas.drawLine(markPointsT[0], markPointsT[1],
							markPointsT[4], markPointsT[5], paint);
				} else {
					canvas.drawLine(markPointsT[0], markPointsT[1],
							markPointsT[2], markPointsT[3], paint);
				}
			}

		}
	}

	private void generateBigGpsImage(int i, int j, int k, int l, Canvas canvas) {
		Path pathClipRing = new Path();
		pathClipRing.reset();
		pathClipRing.addCircle(i, j, radiusExternalBig,
				android.graphics.Path.Direction.CW);
		pathClipRing.close();
		Path pathFullRing = new Path();
		if (visStyle == 1) {
			ringEdgePaint.setColor(0xff000000);
			ringEdgePaint.setStyle(android.graphics.Paint.Style.FILL);
			canvas.drawCircle(i, j, radiusExternalBig, ringEdgePaint);
			if (m3D) {
				LinearGradient lineargradient = new LinearGradient(0.0F,
						boundingBox.top, j, boundingBox.bottom, Color.rgb(10,
								10, 10), Color.rgb(64, 64, 64),
						android.graphics.Shader.TileMode.CLAMP);
				ringPaint.setShader(lineargradient);
				lineargradient = new LinearGradient(0.0F, innerBoundingBox.top,
						j, innerBoundingBox.bottom, Color.rgb(80, 80, 80),
						Color.rgb(10, 10, 10),
						android.graphics.Shader.TileMode.CLAMP);
				ringInternalPaint.setShader(lineargradient);
				pathFullRing.reset();
				pathFullRing.addCircle(i, j, k,
						android.graphics.Path.Direction.CW);
				pathFullRing.addCircle(i, j, l,
						android.graphics.Path.Direction.CW);
				pathFullRing
						.setFillType(android.graphics.Path.FillType.EVEN_ODD);
				canvas.drawCircle(i, j, radiusExternalBig, ringPaint);
				canvas.drawCircle(i, j, radiusInternalBig, ringInternalPaint);
			}
			ringEdgePaint.setStyle(android.graphics.Paint.Style.STROKE);
			ringEdgePaint.setColor(clrRingEdge);
			ringEdgePaint.setStrokeWidth(1.5F * strokeWidth);
			canvas.drawCircle(i, j, radiusExternalBig, ringEdgePaint);
			ringEdgePaint.setStrokeWidth(strokeWidth);
			if (!m3D) {
				canvas.drawCircle(i, j, radiusInternalBig, ringEdgePaint);
			}
			textPaint.setStyle(android.graphics.Paint.Style.STROKE);
			textPaint.setStrokeWidth(2.0F * onePix);
			textPaint.setColor(clrRingEdge);
			textPaint.setTextScaleX(1.0F);
			textPaint.setFakeBoldText(true);
			textPaint.setTextSize(1.5F * fontTitleSize);
			textPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
			drawMarkPoints(canvas, k - 5F * onePix, i, j, 3, true,
					textPaint);
			textPaint.setTextScaleX(1.0F);
			textPaint.setTextSize(fontTitleSize);
			textPaint.setStyle(android.graphics.Paint.Style.FILL);
		} else {
			LinearGradient lineargradient = new LinearGradient(0.0F,
					boundingBox.top, j, boundingBox.bottom, clrRingTop,
					clrRingBottom, android.graphics.Shader.TileMode.CLAMP);
			ringPaint.setShader(lineargradient);
			lineargradient = new LinearGradient(0.0F, innerBoundingBox.top, j,
					innerBoundingBox.bottom, clrInnerTop, clrInnerBottom,
					android.graphics.Shader.TileMode.CLAMP);
			ringInternalPaint.setShader(lineargradient);
			pathFullRing.reset();
			pathFullRing.addCircle(i, j, k, android.graphics.Path.Direction.CW);
			pathFullRing.addCircle(i, j, l, android.graphics.Path.Direction.CW);
			pathFullRing.setFillType(android.graphics.Path.FillType.EVEN_ODD);
			canvas.drawCircle(i, j, radiusExternalBig, ringPaint);
			canvas.drawCircle(i, j, radiusInternalBig, ringInternalPaint);
			ringEdgePaint.setColor(clrRingEdge);
			canvas.drawCircle(i, j, radiusExternalBig, ringEdgePaint);
			textPaint.setTextSize(38F * onePix);
			textPaint.setColor(clrMarks);
			textPaint.setTextAlign(android.graphics.Paint.Align.LEFT);
			textPaint.setTextSkewX(-0.15F);
			canvas.drawText("Sat", i + 20, j + 10, textPaint);
			textPaint.setTextSkewX(0.0F);
		}
		gaugePointsBig[0] = -radiusInternalBig - 1;
		gaugePointsBig[1] = 0.0F;
		gaugePointsBig[2] = 0.0F;
		gaugePointsBig[3] = 0.0F;
		gaugePointsBig[4] = 0.0F;
		gaugePointsBig[5] = 0.0F;
		gaugePointsBig[6] = 1 + radiusInternalBig;
		gaugePointsBig[7] = 0.0F;
		gaugePointsBig[8] = 0.0F;
		gaugePointsBig[9] = -radiusInternalBig - 1;
		gaugePointsBig[10] = 0.0F;
		gaugePointsBig[11] = 0.0F;
		gaugePointsBig[12] = 0.0F;
		gaugePointsBig[13] = 0.0F;
		gaugePointsBig[14] = 0.0F;
		gaugePointsBig[15] = 1 + radiusInternalBig;
		arrowPointsBig[0] = 0.0F;
		arrowPointsBig[1] = -radiusExternalBig - 1;
		arrowPointsBig[2] = 0.0F;
		arrowPointsBig[3] = -radiusInternalBig;
		pathReticle60.reset();
		pathReticle60.addCircle(0.0F, 0.0F, radiusInternalBig / 3,
				android.graphics.Path.Direction.CCW);
		pathReticle60.close();
		pathReticle30.reset();
		pathReticle30.addCircle(0.0F, 0.0F, 2 * (radiusInternalBig / 3),
				android.graphics.Path.Direction.CCW);
		pathReticle30.close();
	}

	private void drawReticle(Canvas canvas, int i, int j, int k, float af[],
			float af1[]) {
		if (visStyle == 1) {
			reticlePaint.setColor(clrRingEdge);
		} else {
			reticlePaint.setColor(clrReticle);
		}
		mtxGauge.reset();
		mtxGauge.setRotate(-north);
		mtxGauge.postTranslate(i, j);
		mtxGauge.mapPoints(gaugePointsT, af);
		reticlePaint.setStrokeWidth(1.5F * onePix);
		canvas.drawLine(gaugePointsT[0], gaugePointsT[1], gaugePointsT[2],
				gaugePointsT[3], reticlePaint);
		canvas.drawLine(gaugePointsT[4], gaugePointsT[5], gaugePointsT[6],
				gaugePointsT[7], reticlePaint);
		canvas.drawLine(gaugePointsT[8], gaugePointsT[9], gaugePointsT[10],
				gaugePointsT[11], reticlePaint);
		canvas.drawLine(gaugePointsT[12], gaugePointsT[13], gaugePointsT[14],
				gaugePointsT[15], reticlePaint);
		reticlePaint.setStrokeWidth(reticleLineStroke);
		mtxGauge.mapPoints(arrowPointsT, af1);
		canvas.drawLine(arrowPointsT[0], arrowPointsT[1], arrowPointsT[2],
				arrowPointsT[3], reticlePaint);
		canvas.drawLine(arrowPointsT[0], arrowPointsT[1], arrowPointsT[2],
				arrowPointsT[3], arrowPaint);
		reticlePaint.setStrokeWidth(1.5F * onePix);
		canvas.drawCircle(i, j, k * 2, reticlePaint);
		canvas.drawCircle(i, j, k, reticlePaint);
		if (k > 20) {
			reticlePaint.setStyle(android.graphics.Paint.Style.FILL);
			pathReticle30.transform(mtxGauge, pathReticle30T);
			canvas.drawTextOnPath("30\260", pathReticle30T, twoPix,
					fontReticleSize + onePix, reticlePaint);
			canvas.drawTextOnPath("30\260", pathReticle30T,
					(float) ((3.1415926535897931D * (k * 4)) / 2D)
							+ twoPix, fontReticleSize + onePix, reticlePaint);
			pathReticle60.transform(mtxGauge, pathReticle60T);
			canvas.drawTextOnPath("60\260", pathReticle60T, twoPix,
					fontReticleSize + onePix, reticlePaint);
			canvas.drawTextOnPath("60\260", pathReticle60T,
					(float) ((3.1415926535897931D * (k * 2)) / 2D)
							+ twoPix, fontReticleSize + onePix, reticlePaint);
			reticlePaint.setStyle(android.graphics.Paint.Style.STROKE);
		}
	}

	private static int getSatColor(float snr) {
		int k;
		if (snr < 10F) {
			k = 0;
		} else if (snr < 20F) {
			k = 1;
		} else if (snr < 30F) {
			k = 2;
		} else if (snr < 40F) {
			k = 3;
		} else {
			k = 4;
		}
		return satColors[k];
	}

	private void drawBigGPS(Canvas canvas, int i, int j) {

		canvas.drawBitmap(mBigImage, i - radiusExternalBig - strokeWidth, j
				- radiusExternalBig - strokeWidth, null);
		if (visStyle == 1 && !m3D) {
			satTextPaint.setShadowLayer(0.0F, onePix, onePix,
					Color.argb(220, 0, 0, 0));
		} else {
			satTextPaint.setShadowLayer(twoPix, onePix, onePix,
					Color.argb(220, 0, 0, 0));
		}
		if (!TrackService.mOpened)
			return;
		drawReticle(canvas, i, j, radiusInternalBig / 3, gaugePointsBig,
				arrowPointsBig);
		satTextPaint.setColor(clrSatText);

		SatelliteInfo info = TrackService.mInfo.getSatinfo();
		for (int k = 0; k < SatelliteInfo.MAXSAT; k++) {
			if (info.sat[k].id < 1)
				continue;
			float f1 = (float) Math.toRadians(info.sat[k].azimuth - north);
			if (f1 > 3.1415926535897931D) {
				f1 -= 6.283185F;
			}
			float f2 = ((1.570796F - (float) Math.toRadians(info.sat[k].elv)) / 1.570796F)
					* (radiusInternalBig - satRadiusBig);
			float f3 = f2 * FloatMath.cos(f1);
			float f4 = f2 * FloatMath.sin(f1);

			float x = f4 + i;
			float y = j - f3;

			if (info.sat[k].in_use != 0) {
				satFixPaint.setAlpha(0);
				satPaint.setColor(satFixColor);
				canvas.drawCircle(x, y, satRadiusBig - 1 + 2.0F
						* satRadiusBig, satFixPaint);
			} else
				satPaint.setColor(getSatColor(info.sat[k].sig));
			canvas.drawCircle(x, y, satRadiusBig, satPaint);
			String name = String.valueOf(k + 1);
			if (info.sat[k].hasEph) {
				satPaint.setStyle(android.graphics.Paint.Style.STROKE);
				satPaint.setStrokeWidth(onePix);
				canvas.drawCircle(x, y, satRadiusBig + twoPix, satPaint);
				satPaint.setStyle(android.graphics.Paint.Style.FILL);
				canvas.drawText(name, x + satRadiusBig + 2.0F * twoPix,
						y + satTextPaint.getTextSize() / 2.0F, satTextPaint);
			} else {
				canvas.drawText(name, x + satRadiusBig + twoPix, y
						+ satTextPaint.getTextSize() / 2.0F, satTextPaint);
			}
		}
		return;
	}

}
