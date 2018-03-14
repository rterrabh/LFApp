package com.ufla.lfapp.views.graph.vertex;

import android.graphics.Path;
import android.graphics.PointF;

/**
 * Created by carlos on 11/16/16.
 *
 * Representa os elementos geométricos do desenho de um vértice no grafo.
 */
public interface VertexDraw {

    /**
     *
     * @return
     */
    Path getInternVertexPath();

    /**
     *
     * @return
     */
    Path getExternVertexPath();

    /**
     *
     * @return
     */
    Path getBorderVertexPath();

    /**
     *
     * @return
     */
    Path getLabelPath();

    /**
     *
     * @return
     */
    VertexDrawType getVertexDrawType();

    /**
     *
     * @return
     */
    boolean isOnInteractArea(PointF pointF);

}
