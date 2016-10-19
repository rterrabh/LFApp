package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.Point;
import android.util.Pair;

/**
 * Created by carlos on 17/10/16.
 */
public interface EdgeDraw {

    Pair<Point, Point> getGridPoints();

    Path getEdge();

    Path getLabelPath();

    Path getArrowHead();

}
