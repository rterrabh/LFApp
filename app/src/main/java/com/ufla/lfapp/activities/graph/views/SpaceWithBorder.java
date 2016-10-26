package com.ufla.lfapp.activities.graph.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by carlos on 10/10/16.
 */
public class SpaceWithBorder extends View {

    public static Paint mBorderPaint;

    static {
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(0.5f);
        mBorderPaint.setColor(Color.parseColor("#60000000"));
    }

    public SpaceWithBorder(Context context) {
        super(context);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(0, 0, VertexView.squareDimension(), VertexView.squareDimension(),
                SpaceWithBorder.mBorderPaint);
    }

    /**
     * Define a largura e a altura do estado a ser desenhado.
     *
     * @param widthMeasureSpec  largura.
     * @param heightMeasureSpec altura.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(VertexView.squareDimension(), VertexView.squareDimension());
    }
}
