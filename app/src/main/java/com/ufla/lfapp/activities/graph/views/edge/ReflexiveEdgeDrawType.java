package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;

/**
 * Created by carlos on 18/10/16.
 */
public interface ReflexiveEdgeDrawType {

    Path getEdge();

    Path getInvertedEdge();

    Path getArrowHead();

}
