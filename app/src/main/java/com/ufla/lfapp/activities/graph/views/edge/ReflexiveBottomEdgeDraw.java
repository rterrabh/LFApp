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
    }

    @Override
    protected void setPointControl() {
        pointControl = new PointF();
        pointControl.x = bottom.x;
        pointControl.y = bottom.y + LENGHT;
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

    protected void setExtremePoint() {
        bottom = new PointF();
        bottom.x = circPoints.first.x;
        bottom.y = circPoints.first.y + VertexView.stateRadius;
    }

    protected PointF getExtremePoint() {
        return bottom;
    }

}
