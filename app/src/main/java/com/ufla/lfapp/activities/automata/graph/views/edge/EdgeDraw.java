package com.ufla.lfapp.activities.automata.graph.views.edge;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.ufla.lfapp.activities.automata.graph.views.edge.interactarea.InteractArea;

/**
 * Created by carlos on 17/10/16.
 */
public interface EdgeDraw {

    Path getEdge();

    Path getLabelPath();

    Path getArrowHead();

    InteractArea getInteractArea();

    boolean isOnInteractArea(PointF pointF);

    Path getPathInteractArea();

    float distanceToCircumferenceOfSourceVertex(PointF point);

    float distanceFromCircumferences();

    Paint.Align getPaintAlign();

    EdgeDrawType getEdgeDrawType();

}
