package com.ufla.lfapp.views.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created by carlos on 2/7/17.
 */

public class TitleTableTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint paintLine;

    public TitleTableTextView(Context context) {
        super(context);
        init();
    }

    public TitleTableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleTableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        definePaintLine();
    }

    private void definePaintLine() {
        paintLine = new Paint();
        paintLine.setColor(Color.BLACK);
        paintLine.setAntiAlias(true);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        paintLine.setStrokeWidth(metrics.densityDpi * 0.01f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawLine(0, 1, width, 1, paintLine);
        canvas.drawLine(0, height - 1, width, height - 1, paintLine);
    }
}