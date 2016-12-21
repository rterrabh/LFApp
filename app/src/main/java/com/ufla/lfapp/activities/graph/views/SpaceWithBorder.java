package com.ufla.lfapp.activities.graph.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by carlos on 10/10/16.
 */
public class SpaceWithBorder extends View {

    private Paint mBorderPaint;
    private int squareDimension;

    public SpaceWithBorder(Context context) {
        super(context);
    }

    public void setmBorderPaint(Paint mBorderPaint) {
        this.mBorderPaint = mBorderPaint;
    }

    public void setSquareDimension(int squareDimension) {
        this.squareDimension = squareDimension;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mBorderPaint != null) {
            canvas.drawRect(0, 0, squareDimension, squareDimension, mBorderPaint);
        }
    }

    /**
     * Define a largura e a altura do estado a ser desenhado.
     *
     * @param widthMeasureSpec  largura.
     * @param heightMeasureSpec altura.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(squareDimension, squareDimension);
    }

    public static SpaceWithBorder getSpaceWithBorder(Context context, Paint mBorderPaint,
                                                     int squareDimension) {
        SpaceWithBorder spaceWithBorder = new SpaceWithBorder(context);
        spaceWithBorder.mBorderPaint = mBorderPaint;
        spaceWithBorder.squareDimension = squareDimension;
        spaceWithBorder.setMinimumHeight(squareDimension);
        spaceWithBorder.setMinimumWidth(squareDimension);
        return spaceWithBorder;
    }
}
