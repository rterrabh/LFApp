package com.ufla.lfapp.views.graph.edge;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.views.graph.edge.interactarea.InteractArea;

/**
 * Created by carlos on 17/10/16.
 */
public interface EdgeDraw {

    Path getEdge();

    Path getLabelPath();

    Pair<PointF, PointF> getLabelLine();

    Path getArrowHead();

    InteractArea getInteractArea();

    boolean isOnInteractArea(PointF pointF);

    float distanceToObject(PointF point);

    boolean isOnInteractLabelArea(PointF point);

    Path getPathInteractArea();

    float distanceToCircumferenceOfSourceVertex(PointF point);

    float distanceFromCircumferences();

    Paint.Align getPaintAlign();

    EdgeDrawType getEdgeDrawType();

}
