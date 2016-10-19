package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Pair;

import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 18/10/16.
 */
public class ReflexiveEdgeDrawUp extends AbstractReflexiveEdgeDrawType {

    private static final int BEGIN_ANGLE = 180;

    public ReflexiveEdgeDrawUp(Pair<PointF, PointF> circPoints) {
        super(circPoints);
    }

    private RectF getRectF() {
        float cathetus = VertexView.stateRadius / (float) Math.sqrt(2.0f);
        float sqDim = VertexView.squareDimension() * 0.8f;
        return new RectF(circPoints.first.x - cathetus,
                circPoints.first.y - cathetus - sqDim,
                circPoints.second.x + cathetus,
                circPoints.second.y - cathetus + sqDim);
    }

    @Override
    public Path getEdge() {
        Path edge = new Path();
        edge.addArc(getRectF(), ReflexiveEdgeDrawUp.BEGIN_ANGLE,
                ANGLE_LENGHT);
        return edge;
    }

    @Override
    public Path getInvertedEdge() {
        Path invertedEdge = new Path();
        invertedEdge.addArc(getRectF(), ReflexiveEdgeDrawUp.BEGIN_ANGLE +
                ANGLE_LENGHT, -ANGLE_LENGHT);
        return invertedEdge;
    }

    @Override
    public Path getArrowHead() {
        return null;
    }

}
