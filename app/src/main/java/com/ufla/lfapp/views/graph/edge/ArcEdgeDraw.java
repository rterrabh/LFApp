package com.ufla.lfapp.views.graph.edge;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.utils.PointUtils;
import com.ufla.lfapp.views.graph.edge.interactarea.BezierQuadraticCurve;

/**
 * Created by carlos on 17/10/16.
 */
public class ArcEdgeDraw extends AbstractArcEdgeDraw {

    private static final double ANGLE = Math.toRadians(20.0f);
    private float LENGHT;
    private float ERROR_RECT_F_LABEL_OUT;
    private float ERROR_RECT_F_LABEL_INTERN;

    public ArcEdgeDraw(Pair<Point, Point> gridPoints, EditGraphLayout editGraphLayout) {
        super(gridPoints, editGraphLayout);
    }

    @Override
    protected void setCircPointsOnCircumference() {
        float angle1To2 = PointUtils.angleFromP1ToP2(circPoints.first, circPoints.second);
        float angle2To1 = PointUtils.angleFromP1ToP2(circPoints.second, circPoints.first);
        circPoints.first.x += vertexRadius * Math.cos(angle1To2 - ANGLE);
        circPoints.first.y += vertexRadius * Math.sin(angle1To2 - ANGLE);
        circPoints.second.x += vertexRadius * Math.cos(angle2To1 + ANGLE);
        circPoints.second.y += vertexRadius * Math.sin(angle2To1 + ANGLE);
    }

    @Override
    protected void defineInteractArea() {
        interactArea = new BezierQuadraticCurve(circPoints.first, pointControl, circPoints.second,
                getPointsControlInteractArea(), vertexRadius);
    }

    @Override
    protected void setPointControl() {
        LENGHT = vertexRadius / 2.0f;
        ERROR_RECT_F_LABEL_OUT = LENGHT * 1.50f;
        ERROR_RECT_F_LABEL_INTERN = LENGHT * 0.65f;
        PointF middlePoint = PointUtils.getMiddlePoint(circPoints);
        pointControl = new PointF();
        float angle = PointUtils.angleFromP1ToP2(middlePoint, circPoints.second);
        pointControl.x = (float) (middlePoint.x + LENGHT * Math.cos(angle - ANGLE_90));
        pointControl.y = (float) (middlePoint.y + LENGHT * Math.sin(angle - ANGLE_90));
    }

    @Override
    public Pair<PointF, PointF> getPointsControlInteractArea() {
        LENGHT = vertexRadius / 2.0f;
        ERROR_RECT_F_LABEL_OUT = LENGHT * 1.50f;
        ERROR_RECT_F_LABEL_INTERN = LENGHT * 0.65f;
        PointF middlePoint = PointUtils.getMiddlePoint(circPoints);
        float angle = PointUtils.angleFromP1ToP2(middlePoint, circPoints.second);
        PointF pointControl1 = new PointF();
        PointF pointControl2 = new PointF();
        pointControl1.x = (float) (middlePoint.x + (LENGHT + ERROR_RECT_F_LABEL_OUT) *
                    Math.cos(angle - ANGLE_90));
        pointControl1.y = (float) (middlePoint.y + (LENGHT + ERROR_RECT_F_LABEL_OUT) *
                Math.sin(angle - ANGLE_90));
        pointControl2.x = (float) (middlePoint.x + ERROR_RECT_F_LABEL_INTERN *
                Math.cos(angle + ANGLE_90));
        pointControl2.y = (float) (middlePoint.y + ERROR_RECT_F_LABEL_INTERN *
                Math.sin(angle + ANGLE_90));
        return Pair.create(pointControl1, pointControl2);
    }

    @Override
    public Path getLabelPath() {
        Path labelPath = new Path();
        float displaceForEdgeText = editGraphLayout.getEdgeTextSize() / 2.0f;
        int columnLenght = numColumns * vertexSquareDimension;
        int labelLenght = (int) Math.min(pointControl.x, columnLenght - pointControl.x);
        if (isSameRow() && isOriginColumnLeft()) {
            labelPath.moveTo(pointControl.x - labelLenght, pointControl.y
                    + displaceForEdgeText / 3);
            labelPath.lineTo(pointControl.x + labelLenght, pointControl.y
                    + displaceForEdgeText / 3);
        } else if (isSameRow() && !isOriginColumnLeft()) {
            labelPath.moveTo(pointControl.x - labelLenght, pointControl.y
                    + displaceForEdgeText);
            labelPath.lineTo(pointControl.x + labelLenght, pointControl.y
                    + displaceForEdgeText);
        } else if (isSameColumn() && isOriginRowUp()) {
            labelPath.moveTo(pointControl.x, pointControl.y);
            labelPath.lineTo(columnLenght, pointControl.y);
        } else if (isSameColumn() && !isOriginRowUp()){
            labelPath.moveTo(0, pointControl.y);
            labelPath.lineTo(pointControl.x, pointControl.y);
        } else if ((isOriginColumnLeft() && isOriginRowUp())
                || (!isOriginColumnLeft() && isOriginRowUp())) {
            labelPath.moveTo(pointControl.x, pointControl.y + displaceForEdgeText);
            labelPath.lineTo(columnLenght, pointControl.y + displaceForEdgeText);
        } else {
            labelPath.moveTo(0, pointControl.y + displaceForEdgeText);
            labelPath.lineTo(pointControl.x , pointControl.y + displaceForEdgeText);
        }
        return labelPath;
    }

    @Override
    public Pair<PointF, PointF> getLabelLine() {
        float displaceForEdgeText = editGraphLayout.getEdgeTextSize() / 2.0f;
        //int labelLenght = getLabelLenght();
        int columnLenght = numColumns * vertexSquareDimension;
        int labelLenght = (int) Math.min(pointControl.x, columnLenght - pointControl.x);
        PointF pointF0;
        PointF pointFX;
        if (isSameRow() && isOriginColumnLeft()) {
            pointF0 = new PointF(pointControl.x - labelLenght, pointControl.y
                    + displaceForEdgeText / 3);
            pointFX = new PointF(pointControl.x + labelLenght, pointControl.y
                    + displaceForEdgeText / 3);
        } else if (isSameRow() && !isOriginColumnLeft()) {
            pointF0 = new PointF(pointControl.x - labelLenght, pointControl.y
                    + displaceForEdgeText);
            pointFX = new PointF(pointControl.x + labelLenght, pointControl.y
                    + displaceForEdgeText);
        } else if (isSameColumn() && isOriginRowUp()) {
            pointF0 = new PointF(pointControl.x, pointControl.y);
            pointFX = new PointF(columnLenght, pointControl.y);
        } else if (isSameColumn() && !isOriginRowUp()){
            pointF0 = new PointF(0, pointControl.y);
            pointFX = new PointF(pointControl.x, pointControl.y);
        } else if ((isOriginColumnLeft() && isOriginRowUp())
                || (!isOriginColumnLeft() && isOriginRowUp())) {
            pointF0 = new PointF(pointControl.x, pointControl.y + displaceForEdgeText);
            pointFX = new PointF(columnLenght, pointControl.y + displaceForEdgeText);
        } else {
            pointF0 = new PointF(0, pointControl.y + displaceForEdgeText);
            pointFX = new PointF(pointControl.x , pointControl.y + displaceForEdgeText);
        }
        return Pair.create(pointF0, pointFX);
    }


    @Override
    public Paint.Align getPaintAlign() {
        if (isSameRow()) {
            return Paint.Align.CENTER;
        }
        if ( (isSameColumn() && isOriginRowUp())
                || (isOriginColumnLeft() && isOriginRowUp())
                || (!isOriginColumnLeft() && isOriginRowUp()) ) {
                return Paint.Align.LEFT;
        }
        return Paint.Align.RIGHT;
    }

    @Override
    public EdgeDrawType getEdgeDrawType() {
        return EdgeDrawType.ARC_EDGE_DRAW;
    }

}
