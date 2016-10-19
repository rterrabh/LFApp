package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Pair;

import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 18/10/16.
 */
public class ArcEdgeDrawQuadrantIV extends AbstractArcEdgeDrawType {

    private static final int BEGIN_ANGLE = 0;

    public ArcEdgeDrawQuadrantIV(Pair<PointF, PointF> circPoints) {
        super(circPoints);
        setCircPoints();
    }

    private void setCircPoints() {
        circPoints.first.y -= VertexView.stateRadius;
        circPoints.second.x += VertexView.stateRadius;
    }

    private RectF getRectF() {
        float distX = Math.abs(circPoints.first.x - circPoints.second.x);
        float distY = Math.abs(circPoints.first.y - circPoints.second.y);
        return new RectF(Math.min(circPoints.first.x, circPoints.second.x) - distX,
                Math.min(circPoints.first.y, circPoints.second.y) - distY,
                Math.max(circPoints.first.x, circPoints.second.x),
                Math.max(circPoints.first.y, circPoints.second.y));
    }

    @Override
    public Path getEdge() {
        Path edge = new Path();
        edge.addArc(getRectF(), ArcEdgeDrawQuadrantIV.BEGIN_ANGLE,
                ANGLE_LENGHT);
        return edge;
    }

    @Override
    public Path getInvertedEdge() {
        Path invertedEdge = new Path();
        invertedEdge.addArc(getRectF(), ArcEdgeDrawQuadrantIV.BEGIN_ANGLE +
                ANGLE_LENGHT, -ANGLE_LENGHT);
        return invertedEdge;
    }

    @Override
    public Path getArrowHead() {
        return null;
    }

}
