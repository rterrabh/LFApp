package com.ufla.lfapp.activities.automata.graph.views.edge;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.automata.graph.layout.EditGraphLayout;
import com.ufla.lfapp.activities.automata.graph.views.PointUtils;

/**
 * Created by carlos on 18/10/16.
 */
public abstract class AbstractReflexiveEdgeDraw extends AbstractArcEdgeDraw {

    protected static final double ANGLE = Math.toRadians(30.0f);
    protected float LENGHT;
    protected float ERROR_RECT_F_LABEL;

    public AbstractReflexiveEdgeDraw(Pair<Point, Point> gridPoints, EditGraphLayout editGraphLayout) {
        super(gridPoints, editGraphLayout);
    }

    protected abstract PointF getExtremePoint();

    protected abstract void setExtremePoint();

    @Override
    protected void setCircPointsOnCircumference() {
        setExtremePoint();
        PointF extremePoint = getExtremePoint();
        float angle = PointUtils.angleFromP1ToP2(circPoints.first, extremePoint);
        circPoints.first.x += vertexRadius * Math.cos(angle - ANGLE);
        circPoints.first.y += vertexRadius * Math.sin(angle - ANGLE);
        circPoints.second.x += vertexRadius * Math.cos(angle + ANGLE);
        circPoints.second.y += vertexRadius * Math.sin(angle + ANGLE);
    }

}
