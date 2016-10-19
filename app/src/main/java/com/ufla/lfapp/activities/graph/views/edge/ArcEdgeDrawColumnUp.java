package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Pair;

import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 18/10/16.
 */
public class ArcEdgeDrawColumnUp extends AbstractArcEdgeDrawType {

    private static final int BEGIN_ANGLE = 270;

    public ArcEdgeDrawColumnUp(Pair<PointF, PointF> circPoints) {
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
        edge.addArc(getRectF(), ArcEdgeDrawColumnUp.BEGIN_ANGLE,
                +ANGLE_LENGHT + ANGLE_LENGHT);
        return edge;
    }

    @Override
    public Path getInvertedEdge() {
        Path invertedEdge = new Path();
        invertedEdge.addArc(getRectF(), ArcEdgeDrawColumnUp.BEGIN_ANGLE
                        + ANGLE_LENGHT + ANGLE_LENGHT,
                -(ANGLE_LENGHT + ANGLE_LENGHT));
        return invertedEdge;
    }

    @Override
    public Path getArrowHead() {
        return null;
    }

}
