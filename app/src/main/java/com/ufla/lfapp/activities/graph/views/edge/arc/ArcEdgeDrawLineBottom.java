package com.ufla.lfapp.activities.graph.views.edge.arc;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Pair;

import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 18/10/16.
 */
public class ArcEdgeDrawLineBottom extends AbstractArcEdgeDrawType {

    private static final int BEGIN_ANGLE = 0;

    public ArcEdgeDrawLineBottom(Pair<PointF, PointF> circPoints) {
        super(circPoints);
    }

    private RectF getRectF() {
        return new RectF(Math.min(circPoints.first.x, circPoints.second.x),
                circPoints.first.y - (VertexView.stateRadius + VertexView.SPACE),
                Math.max(circPoints.first.x, circPoints.second.x),
                circPoints.first.y + (VertexView.stateRadius + VertexView.SPACE));
    }

    @Override
    public Path getEdge() {
        Path edge = new Path();
        edge.addArc(getRectF(), ArcEdgeDrawLineBottom.BEGIN_ANGLE,
                ANGLE_LENGHT + ANGLE_LENGHT);
        return edge;
    }

    @Override
    public Path getInvertedEdge() {
        Path invertedEdge = new Path();
        invertedEdge.addArc(getRectF(), ArcEdgeDrawLineBottom.BEGIN_ANGLE
                        + ANGLE_LENGHT + ANGLE_LENGHT,
                -(ANGLE_LENGHT + ANGLE_LENGHT));
        return invertedEdge;
    }

    @Override
    public Path getArrowHead() {
        return null;
    }

}
