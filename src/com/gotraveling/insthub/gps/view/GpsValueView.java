package com.gotraveling.insthub.gps.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.insthub.ecmobile.R;

/**
 * Created by ChaoMing on 2015/5/15.
 */
public class GpsValueView extends View {
    private int visStyle = 0;
    private Paint textTitlePaint = new Paint(1);
    private Paint textValPaint = new Paint(1);
    private Paint framePaint = new Paint(1);
    private Rect rcWork2 = new Rect();
    private Rect rcWork1 = new Rect();
    private RectF rcWork = new RectF();
    private int clrTextTitle;
    private int clrTextValue;
    private int clrFrame;
    private float textSizeTitle = 12.0F;
    private float textSizeVal = 24.0F;;
    private float onePix = 1.0F;
    private int padding = 5;
    protected String strTitle = "";
    protected String strValue = "";
    protected  String strUnit ="";
    private Typeface mFace;
    protected int defaultWidth =0;
    private boolean fixWidth =false;
    public GpsValueView(Context context, AttributeSet attrs){
        super(context,attrs);
        Resources resources = getResources();
        onePix = resources.getDimension(R.dimen.one_pixel);
        padding = (int)(padding * onePix);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GpsValueView);
        textSizeVal = array.getDimension(R.styleable.GpsValueView_textSize,resources.getDimensionPixelSize(R.dimen.sat_info_value));
        textSizeTitle =  array.getDimension(R.styleable.GpsValueView_nameSize, resources.getDimensionPixelSize(R.dimen.sat_info_title));
        visStyle = array.getInt(R.styleable.GpsValueView_body, 0);
        strTitle = array.getString(R.styleable.GpsValueView_name);
        strValue  = array.getString(R.styleable.GpsValueView_text);
        clrTextTitle = array.getColor(R.styleable.GpsValueView_nameColor, resources.getColor(R.color.gps_info_text));
        clrTextValue = array.getColor(R.styleable.GpsValueView_textColor, resources.getColor(R.color.gps_info_text));
        clrFrame = resources.getColor(R.color.gps_ring_bottom);
        textTitlePaint.setColor(clrTextTitle);
        textTitlePaint.setTextSize(textSizeTitle);
        textTitlePaint.setTextAlign(Paint.Align.CENTER);

        mFace = Typeface.create("sans", 3);
        textValPaint.setTypeface(mFace);
        textValPaint.setColor(clrTextValue);
        textValPaint.setTextScaleX(0.9F);
        textValPaint.setTextSize(textSizeVal);
        textValPaint.setTextAlign(Paint.Align.CENTER);
        textValPaint.setSubpixelText(true);

        framePaint.setStrokeWidth(onePix);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setColor(clrFrame);
        int k = getPaddingTop();
        if( k>0 ) padding =k;
        defaultWidth =  calcColumnWidth(strTitle, strValue);

    }
    public void setUnit(String v ){ strUnit = v;}
    public void setValue(String v){
        strValue = v;
        invalidate();
    }
    public String getValue(){ return strValue; }
    private void drawValueFrame(Canvas canvas, int i, int j, String s, Paint paint, int width) {
        if (visStyle == 1) {
            canvas.getClipBounds();
            paint.getTextBounds(s, 0, s.length(), rcWork1);
            rcWork1.inset(-4, 0);
            rcWork1.offset(2 + (i - rcWork1.width() / 2), j + (int)textSizeTitle);
            rcWork.set(rcWork1);
            canvas.save(2);
            canvas.clipRect(rcWork, android.graphics.Region.Op.DIFFERENCE);
            rcWork.set(i - width/ 2, (int)(j + 2.0F * (textSizeTitle / 3F)), i + width / 2,
                    (int)(4F + (j + textSizeTitle +padding+ textSizeVal+getPaddingBottom())));
            canvas.drawRoundRect(rcWork, 3F * onePix, 3F * onePix, framePaint);
            canvas.restore();
        }
    }
    private int calcColumnWidth(String title, String value)
    {
        textTitlePaint.getTextBounds(title, 0, title.length(), rcWork1);
        textValPaint.getTextBounds(value, 0, value.length(), rcWork2);
        int  k = Math.max(rcWork1.width(), rcWork2.width());
        k += 10+10/2+getPaddingLeft()*2+2*onePix;

        if( k < getSuggestedMinimumWidth()) k= getSuggestedMinimumWidth();
        return k;
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
        if (widthMode == View.MeasureSpec.EXACTLY) mode += 1;
        if (heightMode == View.MeasureSpec.EXACTLY) mode += 2;
        switch (mode) {
            case 0:
                height =  (int)(textSizeVal + textSizeTitle + 2 * padding+ onePix*4+getPaddingBottom() );

                String tmp = strTitle;
                if(  strUnit.length()>0 ) tmp +="("+strUnit+")";
                width =  calcColumnWidth(tmp, strValue);

                if( defaultWidth>width ) width=defaultWidth;
                if( width> widthSize ) width = widthSize;
                if( height> heightSize ) height = heightSize;
                break;
            case 1:
                width = widthSize;
                fixWidth = true;
                height =  (int)(textSizeVal + textSizeTitle + 2 * padding+ onePix*4+ getPaddingBottom() );
                //height = Math.min(height, heightSize);
                break;
            case 2:
                height = heightSize;
                tmp = strTitle;
                if(  strUnit.length()>0 ) tmp +="("+strUnit+")";
                width =  calcColumnWidth(tmp, strValue);
                // width = Math.min(widthSize, wSat);
                break;
            case 3:
                fixWidth = true;
                width = widthSize;
                height = heightSize;
                break;
        }
        setMeasuredDimension(width, height);
        //Log.i("GpsValueView.onMeasure(): w=" + width + "(" + widthSize + ")  h = " + height + "( " + heightSize + ")");
    }

    private void drawValue(Canvas canvas, int x, int j, String title, String value, int width) {
        drawValueFrame(canvas, x, j, title, textTitlePaint, width);
        canvas.drawText(title, x, j + textSizeTitle, textTitlePaint);
        canvas.drawText(value, x, j + textSizeTitle + textSizeVal+padding, textValPaint);
    }
    @Override
	protected void onDraw(Canvas canvas) {
        int i = getMeasuredWidth();
        int j = getMeasuredHeight();

        framePaint.setStyle(android.graphics.Paint.Style.STROKE);
        int snap = padding;
        if (visStyle == 1)          snap = 0;
        int wSat =  calcColumnWidth(strTitle, strValue) ;
        if( fixWidth ) wSat = i;
        String tmp = strTitle;
        if(  strUnit.length()>0 ) tmp +="("+strUnit+")";
        drawValue(canvas, wSat/2, snap, tmp, strValue, wSat);
    }
}
