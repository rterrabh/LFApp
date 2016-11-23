package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 18/10/16.
 */
public class ReflexiveBottomEdgeDraw extends AbstractReflexiveEdgeDraw {

    private PointF bottom;

    public ReflexiveBottomEdgeDraw(Pair<Point, Point> gridPoints) {
        super(gridPoints);
        setCircPointsOnCircumference();
    }

    @Override
    protected void setCircPointsOnCircumference() {
        super.setCircPointsOnCircumference();
        float angle = (float) Math.atan2((bottom.y - circPoints.first
                .y), (bottom.x - circPoints.first.x));
        circPoints.first.x += VertexView.stateRadius * Math.cos(angle - ANGLE);
        circPoints.first.y += VertexView.stateRadius * Math.sin(angle - ANGLE);
        circPoints.second.x += VertexView.stateRadius * Math.cos(angle + ANGLE);
        circPoints.second.y += VertexView.stateRadius * Math.sin(angle + ANGLE);
    }

    @Override
    protected void setPointControl() {
        setBottom();
        pointControl = new PointF();
        pointControl.x = bottom.x;
        pointControl.y = bottom.y + LENGHT;
    }

    private void setBottom() {
        bottom = new PointF();
        bottom.x = circPoints.first.x;
        bottom.y = circPoints.first.y + VertexView.stateRadius;
    }

    @Override
    protected Pair<PointF, PointF> getPointsControlInteractArea() {
        PointF pointControl1 = new PointF();
        PointF pointControl2 = new PointF();
        pointControl1.x = bottom.x;
        pointControl1.y = bottom.y;
        pointControl2.x = pointControl.x;
        pointControl2.y = pointControl.y + ERROR_RECT_F_LABEL;
        return Pair.create(pointControl1, pointControl2);
    }

}
