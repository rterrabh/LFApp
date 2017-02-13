package com.ufla.lfapp.activities.automata.graph.views.vertex;

import com.ufla.lfapp.activities.automata.graph.layout.EditGraphLayout;
import com.ufla.lfapp.activities.automata.graph.views.BorderVertex;

/**
 * Created by carlos on 11/16/16.
 */

public class VertexDrawnFactory {

    public VertexDraw createVertexDraw(VertexDrawType vertexDrawType,
                                         EditGraphLayout editGraphLayout,
                                         BorderVertex borderVertex) {
        VertexDraw vertexDraw = null;
        if (vertexDrawType == VertexDrawType.DEFAULT) {
            vertexDraw = new VertexDrawDefault(editGraphLayout, borderVertex);
        } else if (vertexDrawType == VertexDrawType.FINAL) {
            vertexDraw = new VertexDrawFinal(editGraphLayout, borderVertex);
        } else if (vertexDrawType == VertexDrawType.INITIAL) {
            vertexDraw = new VertexDrawInitial(editGraphLayout, borderVertex);
        } else if (vertexDrawType == VertexDrawType.INITIAL_FINAL) {
            vertexDraw = new VertexDrawInitialAndFinal(editGraphLayout, borderVertex);
        } else {
            throw new RuntimeException("Vertex draw type don't exist!");
        }
        return new VertexDrawViewBuilder()
                .withInternVertexPath(vertexDraw.getInternVertexPath())
                .withExternVertexPath(vertexDraw.getExternVertexPath())
                .withBorderVertexPath(vertexDraw.getBorderVertexPath())
                .withLabelPath(vertexDraw.getLabelPath())
                .withVertexCenterPoint(editGraphLayout.getVertexCenterPoint())
                .withVertexRadius(editGraphLayout.getVertexRadius())
                .withVertexDrawType(vertexDraw.getVertexDrawType())
                .createVertexDrawView();
    }

}
