package com.ufla.lfapp.activities.automata.graph.adapters;

/**
 * Created by carlos on 11/10/16.
 */
public interface VertexFactory<V extends IVertex> {

    V createVertex();
}
