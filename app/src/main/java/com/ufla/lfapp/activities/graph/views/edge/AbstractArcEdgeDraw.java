package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.graph.views.EdgeView;

/**
 * Created by carlos on 11/17/16.
 */

public abstract class AbstractArcEdgeDraw extends AbstractEdgeDraw {

    protected PointF pointControl;

    public AbstractArcEdgeDraw(Pair<Point, Point> gridPoints) {
        super(gridPoints);
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
