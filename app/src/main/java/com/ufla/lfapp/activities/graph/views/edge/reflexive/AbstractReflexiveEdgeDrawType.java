package com.ufla.lfapp.activities.graph.views.edge.reflexive;

import android.graphics.PointF;
import android.util.Pair;

/**
 * Created by carlos on 18/10/16.
 */
public abstract class AbstractReflexiveEdgeDrawType implements ReflexiveEdgeDrawType {

    protected Pair<PointF, PointF> circPoints;
    protected static final int ANGLE_LENGHT = 180;

    public AbstractReflexiveEdgeDrawType(Pair<PointF, PointF> circPoints) {
        this.circPoints = circPoints;
    }

}
