package com.ufla.lfapp.activities.automata.graph.views.edge;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.automata.graph.layout.EditGraphLayout;
import com.ufla.lfapp.activities.automata.graph.views.EdgeView;
import com.ufla.lfapp.activities.automata.graph.views.PointUtils;

/**
 * Created by carlos on 11/17/16.
 */

public abstract class AbstractArcEdgeDraw extends AbstractEdgeDraw {

    protected PointF pointControl;

    public AbstractArcEdgeDraw(Pair<Point, Point> gridPoints, EditGraphLayout editGraphLayout) {
        super(gridPoints, editGraphLayout);
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
        float angle = PointUtils.angleFromP1ToP2(circPoints.second, pointControl);
        PointF pointArrowHead = new PointF();
        pointArrowHead.x = (float) (circPoints.second.x + arrowHeadLenght *
                Math.cos(angle + EdgeView.getArrowAngle()));
        pointArrowHead.y = (float) (circPoints.second.y + arrowHeadLenght *
                Math.sin(angle + EdgeView.getArrowAngle()));
        arrowHead.moveTo(circPoints.second.x, circPoints.second.y);
        arrowHead.lineTo(pointArrowHead.x, pointArrowHead.y);
        pointArrowHead.x = (float) (circPoints.second.x + arrowHeadLenght *
                Math.cos(angle - EdgeView.getArrowAngle()));
        pointArrowHead.y = (float) (circPoints.second.y + arrowHeadLenght *
                Math.sin(angle - EdgeView.getArrowAngle()));
        arrowHead.moveTo(circPoints.second.x, circPoints.second.y);
        arrowHead.lineTo(pointArrowHead.x, pointArrowHead.y);
        return arrowHead;
    }

}
