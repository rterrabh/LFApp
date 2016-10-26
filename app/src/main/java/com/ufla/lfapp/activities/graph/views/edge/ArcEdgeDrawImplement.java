package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.PointF;
import android.util.Pair;

import com.ufla.lfapp.activities.graph.views.EdgeView;
import com.ufla.lfapp.activities.graph.views.VertexView;
import com.ufla.lfapp.activities.graph.views.edge.arc.AbstractArcEdgeDrawType;

/**
 * Created by lfapp on 25/10/16.
 */

public class ArcEdgeDrawImplement extends AbstractArcEdgeDrawType {

    private static final int BEGIN_ANGLE = 0;
    private static final double ANGLE = Math.toRadians(20.0f);
    private static final double ANGLE_90 = Math.toRadians(90.0f);
    private static final float LENGHT = VertexView.stateRadius/2.0f;
    private PointF pointControl;

    public ArcEdgeDrawImplement(Pair<PointF, PointF> circPoints) {
        super(circPoints);
        setCircPoints();
        setPointControl();
    }

    private void setCircPoints() {
        float angle1To2 = (float) Math.atan2((circPoints.first
                .y - circPoints.second.y), (circPoints.first.x -
                circPoints.second.x));
        float angle2To1 = (float) Math.atan2((circPoints.second.y - circPoints.first
                .y), (circPoints.second.x - circPoints.first.x));
        circPoints.first.x += VertexView.stateRadius * Math.cos(angle2To1 - ANGLE);
        circPoints.first.y += VertexView.stateRadius * Math.sin(angle2To1 - ANGLE);
        circPoints.second.x += VertexView.stateRadius * Math.cos(angle1To2 + ANGLE);
        circPoints.second.y += VertexView.stateRadius * Math.sin(angle1To2 + ANGLE);
    }


    public PointF getMiddlePoint() {
        PointF middlePoint = new PointF();
        middlePoint.x = (circPoints.first.x + circPoints.second.x) / 2.0f;
        middlePoint.y = (circPoints.first.y + circPoints.second.y) / 2.0f;
        return  middlePoint;
    }

    public void setPointControl() {
        PointF middlePoint = getMiddlePoint();
        pointControl = new PointF();
        float angle = (float) Math.atan2((circPoints.second
                .y - middlePoint.y), (circPoints.second.x -
                middlePoint.x));
        pointControl.x = (float) (middlePoint.x + LENGHT * Math.cos(angle - ANGLE_90));
        pointControl.y = (float) (middlePoint.y + LENGHT * Math.sin(angle - ANGLE_90));
    }

    @Override
    public Path getEdge() {
        Path edge = new Path();
        edge.moveTo(circPoints.first.x, circPoints.first.y);
        edge.quadTo(pointControl.x, pointControl.y, circPoints.second.x, circPoints.second.y);
        return edge;
    }

    @Override
    public Path getInvertedEdge() {
        Path invertedEdge = new Path();
        invertedEdge.moveTo(circPoints.second.x, circPoints.second.y);
        invertedEdge.quadTo(pointControl.x, pointControl.y, circPoints.first.x, circPoints.first.y);
        return invertedEdge;
    }

    @Override
    public Path getArrowHead() {
        Path arrowHead = new Path();
        float angle = (float) Math.atan2((pointControl.y - circPoints.second
                .y), (pointControl.x - circPoints.second.x));
        PointF pointArrowHead = new PointF();
        pointArrowHead.x = (float) (circPoints.second.x + EdgeView.getArrowHeadLenght() *
                Math.cos(angle + EdgeView.getArrowAngle()));
        pointArrowHead.y = (float) (circPoints.second.y + EdgeView.getArrowHeadLenght() *
                Math.sin(angle + EdgeView.getArrowAngle()));
        arrowHead.moveTo(circPoints.second.x, circPoints.second.y);
        arrowHead.lineTo(pointArrowHead.x, pointArrowHead.y);
        pointArrowHead.x = (float) (circPoints.second.x + EdgeView.getArrowHeadLenght() *
                Math.cos(angle - EdgeView.getArrowAngle()));
        pointArrowHead.y = (float) (circPoints.second.y + EdgeView.getArrowHeadLenght() *
                Math.sin(angle - EdgeView.getArrowAngle()));
        arrowHead.moveTo(circPoints.second.x, circPoints.second.y);
        arrowHead.lineTo(pointArrowHead.x, pointArrowHead.y);
        return arrowHead;
    }

}