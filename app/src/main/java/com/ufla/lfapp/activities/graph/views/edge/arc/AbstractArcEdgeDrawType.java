package com.ufla.lfapp.activities.graph.views.edge.arc;

import android.graphics.PointF;
import android.util.Pair;

import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 18/10/16.
 */
public abstract class AbstractArcEdgeDrawType implements ArcEdgeDrawType {

    protected Pair<PointF, PointF> circPoints;
    protected static final int ANGLE_LENGHT = 90;
    private static final double ANGLE = Math.toRadians(10.0f);

    public AbstractArcEdgeDrawType(Pair<PointF, PointF> circPoints) {
        this.circPoints = circPoints;
    }



}
