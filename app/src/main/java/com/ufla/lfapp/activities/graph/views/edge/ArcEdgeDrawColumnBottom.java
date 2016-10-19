package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Pair;

import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 18/10/16.
 */
public class ArcEdgeDrawColumnBottom extends AbstractArcEdgeDrawType {

    private static final int BEGIN_ANGLE = 90;

    public ArcEdgeDrawColumnBottom(Pair<PointF, PointF> circPoints) {
        super(circPoints);
    }

    private RectF getRectF() {
        return new RectF(circPoints.first.x - (VertexView.stateRadius + VertexView.SPACE),
                Math.min(circPoints.first.y, circPoints.second.y),
                circPoints.first.x + (VertexView.stateRadius + VertexView.SPACE),
                Math.max(circPoints.first.y, circPoints.second.y));
    }

    @Override
    public Path getEdge() {
        Path edge = new Path();
        edge.addArc(getRectF(), ArcEdgeDrawColumnBottom.BEGIN_ANGLE,
                ANGLE_LENGHT + ANGLE_LENGHT);
        return edge;
    }

    @Override
    public Path getInvertedEdge() {
        Path invertedEdge = new Path();
        invertedEdge.addArc(getRectF(), ArcEdgeDrawColumnBottom.BEGIN_ANGLE
                        + ANGLE_LENGHT + ANGLE_LENGHT,
                -(ANGLE_LENGHT + ANGLE_LENGHT));
        return invertedEdge;
    }

    @Override
    public Path getArrowHead() {
        return null;
    }

}
