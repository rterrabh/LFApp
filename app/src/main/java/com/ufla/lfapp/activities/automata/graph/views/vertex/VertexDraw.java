package com.ufla.lfapp.activities.automata.graph.views.vertex;

import android.graphics.Path;
import android.graphics.PointF;

/**
 * Created by carlos on 11/16/16.
 */

public interface VertexDraw {

    Path getInternVertexPath();

    Path getExternVertexPath();

    Path getBorderVertexPath();

    Path getLabelPath();

    VertexDrawType getVertexDrawType();

    boolean isOnInteractArea(PointF pointF);

}
