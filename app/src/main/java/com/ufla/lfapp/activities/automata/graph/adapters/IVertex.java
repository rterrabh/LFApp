package com.ufla.lfapp.activities.automata.graph.adapters;

import android.graphics.Point;

/**
 * Created by carlos on 11/10/16.
 */
public interface IVertex {

    String getLabel();

    void setLabel(String label);

    Point getPosition();

    void setPosition(Point point);


}
