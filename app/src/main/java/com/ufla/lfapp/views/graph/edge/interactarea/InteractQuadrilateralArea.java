package com.ufla.lfapp.views.graph.edge.interactarea;

import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.PointUtils;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;

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
            throw new RuntimeException(ResourcesContext.getString(R.string.exception_inconsistent_interact_quad));
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
            // NOT TO DO
        }
        return red >= blue;
    }

    @Override
    public boolean isOnInteractArea(PointF point) {
        boolean inside = false;
        for (int i = 0, j = NUMBER_OF_VERTICES - 1; i < NUMBER_OF_VERTICES; j = i++) {
            if (intersects(points[i], points[j], point)) {
                inside = !inside;
            }
        }
        return inside;
    }

    @Override
    public boolean isOnInteractLabelArea(PointF point) {
        return false;
    }

    @Override
    public float distanceToObject(PointF point) {
        return (float) ( Math.abs((points[2].y - points[0].y) * point.x - (points[2].x - points[0].x)
                        * point.y + points[2].x * points[0].y - points[2].y * points[0].x) /
                        Math.sqrt(Math.pow(points[2].y - points[0].y, 2.0) +
                                Math.pow(points[2].x - points[0].x, 2.0) ) );
    }

    @Override
    public Path getInteractArea() {
        Path interactArea = new Path();
        // Px(t) = P0 + (P1 - P0)t
        PointF point = new PointF();
        for (float t = 0; t <= 1.1; t += 0.1) {
            point.x = points[2].x - (points[2].x - points[0].x) * t;
            point.y = points[2].y - (points[2].y - points[0].y) * t;
            interactArea.addCircle(point.x, point.y, EditGraphLayout.MAX_DISTANCE_FROM_EDGE,
                    Path.Direction.CW);
        }
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
            throw new RuntimeException(ResourcesContext.getString(R.string.exception_clone));
        }
    }

}
