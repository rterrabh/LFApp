package com.ufla.lfapp.activities.automata.graph.views.edge.interactarea;

import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.automata.graph.views.PointUtils;

/**
 * Created by carlos on 11/21/16.
 */

public class InteractQuadrilateralArea implements InteractArea, Cloneable {

    private static final int NUMBER_OF_VERTICES = 4;
    private PointF points[];

    protected InteractQuadrilateralArea() {
        points = new PointF[NUMBER_OF_VERTICES];
    }

    public InteractQuadrilateralArea(Pair<PointF, PointF> circumferencePoints,
                                     Pair<PointF, PointF> controlPoints) {
        setCircumferencePoints(circumferencePoints);
        setControlPoints(controlPoints);
        if (isInconsistent()) {
            throw new RuntimeException("Instance of InteractQuadrilateralArea is inconsistent!");
        }
    }

    private boolean intersects(PointF a, PointF b, PointF p) {
        if (a.y > b.y) {
            return intersects(b, a, p);
        }
        if (p.y == a.y || p.y == b.y) {
            p.y += 0.0001;
        }
        if (p.y > b.y || p.y < a.y || p.x > Math.max(a.x, b.x)) {
            return false;
        }
        if (p.x < Math.min(a.x, b.x)) {
            return true;
        }
        double red = Double.MAX_VALUE;
        double blue = Double.MAX_VALUE;
        try {
            red = (p.y - a.y) / (double) (p.x - a.x);
            blue = (b.y - a.y) / (double) (b.x - a.x);
        } catch (ArithmeticException e) {
        }
        return red >= blue;
    }

    @Override
    public boolean isOnInteractArea(PointF point) {
        boolean inside = false;

        for(int i = 0, j = NUMBER_OF_VERTICES - 1; i < NUMBER_OF_VERTICES; j = i++) {
            if( intersects(points[i], points[j], point)) {
                inside = !inside;
            }
        }

        return inside;
    }

    @Override
    public Path getInteractArea() {
        Path interactArea = new Path();
        interactArea.moveTo(points[0].x, points[0].y);
        for (int i = 1; i < NUMBER_OF_VERTICES; i++) {
            interactArea.lineTo(points[i].x, points[i].y);
        }
        interactArea.lineTo(points[0].x, points[0].y);
        return interactArea;
    }

    @Override
    public float distanceFromCircumferences() {
        return PointUtils.dist(points[0], points[2]);
    }

    @Override
    public float distanceToCircumferenceOfSourceVertex(PointF point) {
        return PointUtils.dist(points[0], point);
    }

    protected boolean isInconsistent() {
        for (int i = 0; i < NUMBER_OF_VERTICES; i++) {
            if (points[i] == null) {
                return true;
            }
        }
        return false;
    }

    public PointF getNearestPoint() {
        return null;
    }


    protected void setCircumferencePoints(Pair<PointF, PointF> circumferencePoints) {
        points[0] = PointUtils.clonePointF(circumferencePoints.first);
        points[2] = PointUtils.clonePointF(circumferencePoints.second);
    }

    protected void setControlPoints(Pair<PointF, PointF> controlPoints) {
        points[1] = PointUtils.clonePointF(controlPoints.first);
        points[3] = PointUtils.clonePointF(controlPoints.second);
    }

    @Override
    public InteractArea clone() {
        try {
            InteractQuadrilateralArea interactAreaCloned = (InteractQuadrilateralArea)
                    super.clone();
            for (int i = 0; i < NUMBER_OF_VERTICES; i++) {
                interactAreaCloned.points[i] = PointUtils.clonePointF(points[i]);
            }
            return interactAreaCloned;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("Not possible clone this instance!");
        }
    }

}
