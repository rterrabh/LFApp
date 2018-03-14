package com.ufla.lfapp.views.graph.edge;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.views.graph.layout.EditGraphLayout;

/**
 * Created by carlos on 18/10/16.
 */
public class ReflexiveUpEdgeDraw extends AbstractReflexiveEdgeDraw {

    private PointF up;

    public ReflexiveUpEdgeDraw(Pair<Point, Point> gridPoints, EditGraphLayout editGraphLayout) {
        super(gridPoints, editGraphLayout);
    }

//    @Override
//    protected void setCircPointsOnCircumference() {
//        circPoints.first.y += vertexSquareDimension;
//        circPoints.second.y += vertexSquareDimension;
//        super.setCircPointsOnCircumference();
//    }

    @Override
    protected void setPointControl() {
        LENGHT = vertexRadius * 2.2f;
        ERROR_RECT_F_LABEL = LENGHT * 0.40f;
        pointControl = new PointF();
        pointControl.x = up.x;
        pointControl.y = up.y - LENGHT;
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

    protected void setExtremePoint() {
        up = new PointF();
        up.x = circPoints.first.x;
        up.y = circPoints.first.y - vertexRadius;
    }

    protected PointF getExtremePoint() {
        return up;
    }

    @Override
    public Path getLabelPath() {
        Path labelPath = new Path();
        //int labelLenght = getLabelLenght();
        int columnLenght = numColumns * vertexSquareDimension;
        int labelLenght = (int) Math.min(pointControl.x, columnLenght - pointControl.x);
        labelPath.moveTo(pointControl.x - labelLenght, pointControl.y + (LENGHT / 2.0f));
        labelPath.lineTo(pointControl.x + labelLenght, pointControl.y + (LENGHT / 2.0f));
        return labelPath;
    }

    @Override
    public Pair<PointF, PointF> getLabelLine() {
        //int labelLenght = getLabelLenght();
        int columnLenght = numColumns * vertexSquareDimension;
        int labelLenght = (int) Math.min(pointControl.x, columnLenght - pointControl.x);
        PointF pointF0 = new PointF(pointControl.x - labelLenght, pointControl.y + (LENGHT / 2.0f));
        PointF pointFX = new PointF(pointControl.x + labelLenght, pointControl.y + (LENGHT / 2.0f));
        return Pair.create(pointF0, pointFX);
    }

    @Override
    public EdgeDrawType getEdgeDrawType() {
        return EdgeDrawType.REFLEXIVE_UP_EDGE_DRAW;
    }

}
