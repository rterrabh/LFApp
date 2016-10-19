package com.ufla.lfapp.activities.graph;

import android.util.Pair;

import com.ufla.lfapp.activities.graph.adapters.EdgeFactory;
import com.ufla.lfapp.activities.graph.adapters.IVertex;

/**
 * Created by carlos on 11/10/16.
 */
public class TransitionFactory implements EdgeFactory<TransitionFunction> {


    @Override
    public TransitionFunction createEdge(Pair<IVertex, IVertex> vertices) {
        return null;
    }
}
