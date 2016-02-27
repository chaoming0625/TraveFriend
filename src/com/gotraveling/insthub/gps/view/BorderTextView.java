package com.gotraveling.insthub.gps.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.insthub.ecmobile.R;

/**
 * Created by ChaoMing on 2015/5/27.
 */
public class BorderTextView  extends TextView {
    private int clrBody=0;
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Paint paint = new Paint();
        //  ���߿���Ϊ��ɫ
        paint.setColor(clrBody);
        //  ��TextView��4����
        //canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
        //canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
        //canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1, paint);
        canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, paint);
    }
    public BorderTextView (Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BorderTextView);
        clrBody = array.getColor(R.styleable.BorderTextView_bodyColor,0x81ce47 );
    }

}
