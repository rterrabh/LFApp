package com.ufla.lfapp.activities.graph.views.edge.arc;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Pair;

import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 18/10/16.
 */
public class ArcEdgeDrawQuadrantI extends AbstractArcEdgeDrawType {

    private static final int BEGIN_ANGLE = 270;

    public ArcEdgeDrawQuadrantI(Pair<PointF, PointF> circPoints) {
        super(circPoints);
    }

    private RectF getRectF() {
        float distX = Math.abs(circPoints.first.x - circPoints.second.x);
        float distY = Math.abs(circPoints.first.y - circPoints.second.y);
        return new RectF(Math.min(circPoints.first.x, circPoints.second.x) - distX,
                Math.min(circPoints.first.y, circPoints.second.y),
                Math.max(circPoints.first.x, circPoints.second.x),
                Math.max(circPoints.first.y, circPoints.second.y) + distY);
    }

    @Override
    public Path getEdge() {
        Path edge = new Path();
        edge.addArc(getRectF(), ArcEdgeDrawQuadrantI.BEGIN_ANGLE,
                ANGLE_LENGHT);
        return edge;
    }

    @Override
    public Path getInvertedEdge() {
        Path invertedEdge = new Path();
        invertedEdge.addArc(getRectF(), ArcEdgeDrawQuadrantI.BEGIN_ANGLE
                + ANGLE_LENGHT, -ANGLE_LENGHT);
        return invertedEdge;
    }

    @Override
    public Path getArrowHead() {
        return null;
    }

}
