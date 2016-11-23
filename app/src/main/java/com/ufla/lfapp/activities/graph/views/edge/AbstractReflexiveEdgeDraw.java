package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 18/10/16.
 */
public abstract class AbstractReflexiveEdgeDraw extends AbstractArcEdgeDraw {

    protected static final double ANGLE = Math.toRadians(30.0f);
    protected static final float LENGHT = VertexView.stateRadius * 2.2f;
    protected static final float ERROR_RECT_F_LABEL = LENGHT * 0.40f;

    public AbstractReflexiveEdgeDraw(Pair<Point, Point> gridPoints) {
        super(gridPoints);
    }

    protected abstract PointF getExtremePoint();

    protected abstract void setExtremePoint();

    @Override
    protected void setCircPointsOnCircumference() {
        setExtremePoint();
        PointF extremePoint = getExtremePoint();
        float angle = (float) Math.atan2((extremePoint.y - circPoints.first
                .y), (extremePoint.x - circPoints.first.x));
        circPoints.first.x += VertexView.stateRadius * Math.cos(angle - ANGLE);
        circPoints.first.y += VertexView.stateRadius * Math.sin(angle - ANGLE);
        circPoints.second.x += VertexView.stateRadius * Math.cos(angle + ANGLE);
        circPoints.second.y += VertexView.stateRadius * Math.sin(angle + ANGLE);
    }

}
