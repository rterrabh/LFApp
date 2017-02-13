package com.ufla.lfapp.activities.automata.graph.views.edge;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.automata.graph.layout.EditGraphLayout;

/**
 * Created by carlos on 18/10/16.
 */
public class ReflexiveBottomEdgeDraw extends AbstractReflexiveEdgeDraw {

    private PointF bottom;

    public ReflexiveBottomEdgeDraw(Pair<Point, Point> gridPoints, EditGraphLayout editGraphLayout) {
        super(gridPoints, editGraphLayout);
    }

    @Override
    protected void setPointControl() {
        LENGHT = vertexRadius * 2.2f;
        ERROR_RECT_F_LABEL = LENGHT * 0.40f;
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
        bottom.y = circPoints.first.y + vertexRadius;
    }

    protected PointF getExtremePoint() {
        return bottom;
    }

    @Override
    public Path getLabelPath() {
        Path labelPath = new Path();
        int labelLenght = getLabelLenght();
        labelPath.moveTo(pointControl.x - (labelLenght / 2), pointControl.y - (LENGHT / 3.0f));
        labelPath.lineTo(pointControl.x + (labelLenght / 2), pointControl.y - (LENGHT / 3.0f));
        return labelPath;
    }

    @Override
    public EdgeDrawType getEdgeDrawType() {
        return EdgeDrawType.REFLEXIVE_BOTTOM_EDGE_DRAW;
    }

}
