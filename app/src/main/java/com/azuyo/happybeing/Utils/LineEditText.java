package com.azuyo.happybeing.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Admin on 28-12-2016.
 */

public class LineEditText extends EditText {

    private static Paint linePaint;
    private static Context context;
    static {
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#D8D8D8"));
        linePaint.setStyle(Paint.Style.STROKE);
    }

    public LineEditText(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect bounds = new Rect();
        int firstLineY = getLineBounds(0, bounds);
        int lineHeight = getLineHeight();
        int totalLines = Math.max(getLineCount(), getHeight() / lineHeight);

        for (int i = 0; i < totalLines; i++) {
            int lineY = firstLineY + i * lineHeight;
            canvas.drawLine(bounds.left, lineY, bounds.right, lineY, linePaint);
        }

        super.onDraw(canvas);
    }


}