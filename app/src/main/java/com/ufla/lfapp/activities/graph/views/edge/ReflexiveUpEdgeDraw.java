package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 18/10/16.
 */
public class ReflexiveUpEdgeDraw extends AbstractReflexiveEdgeDraw {

    private PointF up;

    public ReflexiveUpEdgeDraw(Pair<Point, Point> gridPoints) {
        super(gridPoints);
    }

    @Override
    protected void setCircPointsOnCircumference() {
        super.setCircPointsOnCircumference();
        float angle = (float) Math.atan2((up.y - circPoints.first
                .y), (up.x - circPoints.first.x));
        circPoints.first.x += VertexView.stateRadius * Math.cos(angle - ANGLE);
        circPoints.first.y += VertexView.stateRadius * Math.sin(angle - ANGLE);
        circPoints.second.x += VertexView.stateRadius * Math.cos(angle + ANGLE);
        circPoints.second.y += VertexView.stateRadius * Math.sin(angle + ANGLE);
    }

    @Override
    protected void setPointControl() {
        setUp();
        pointControl = new PointF();
        pointControl.x = up.x;
        pointControl.y = up.y - LENGHT;
    }

    private void setUp() {
        up = new PointF();
        up.x = circPoints.first.x;
        up.y = circPoints.first.y - VertexView.stateRadius;
    }

    @Override
    protected Pair<PointF, PointF> getPointsControlInteractArea() {
        PointF pointControl1 = new PointF();
        PointF pointControl2 = new PointF();
        pointControl1.x = up.x;
        pointControl1.y = up.y;
        pointControl2.x = pointControl.x;
        pointControl2.y = pointControl.y - ERROR_RECT_F_LABEL;
        return Pair.create(pointControl1, pointControl2);
    }

}
