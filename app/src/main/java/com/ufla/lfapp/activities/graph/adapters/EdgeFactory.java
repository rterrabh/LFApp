package com.ufla.lfapp.activities.graph.adapters;

import android.util.Pair;

/**
 * Created by carlos on 11/10/16.
 */
public interface EdgeFactory<E extends IEdge> {

    E createEdge(Pair<IVertex, IVertex> vertices);

}
