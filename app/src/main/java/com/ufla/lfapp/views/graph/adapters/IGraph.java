package com.ufla.lfapp.views.graph.adapters;

/**
 * Created by carlos on 11/10/16.
 */
public interface IGraph<V extends IVertex, E extends IEdge> {

    void addVertex(V vertex);

    boolean removeVertex(V vertex);

    void addEdge(E edge);

    boolean removeEdge(E edge);
}
