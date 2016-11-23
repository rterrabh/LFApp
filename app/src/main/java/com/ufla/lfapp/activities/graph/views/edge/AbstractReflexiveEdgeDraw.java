package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Point;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 18/10/16.
 */
public abstract class AbstractReflexiveEdgeDraw extends AbstractArcEdgeDraw {

    protected static final double ANGLE = Math.toRadians(30.0f);
    protected static final float LENGHT = VertexView.stateRadius * 2.2f;
    protected static final float ERROR_RECT_F_LABEL = LENGHT * 0.40f;

    public AbstractReflexiveEdgeDraw(Pair<Point, Point> gridPoints) {
        super(gridPoints);
    }

    @Override
    protected void setCircPointsOnCircumference() {
        circPoints.first.y += VertexView.squareDimension();
        circPoints.second.y += VertexView.squareDimension();
    }

}
