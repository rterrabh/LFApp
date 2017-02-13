package com.ufla.lfapp.activities.automata.graph.adapters;

import android.util.Pair;

/**
 * Created by carlos on 11/10/16.
 */
public interface IEdge {

    String getLabel();

    void setLabel(String label);

    Pair<IVertex, IVertex> getVertices();

//    boolean isDirected();
//
//    void setDirected(boolean directed);


}
