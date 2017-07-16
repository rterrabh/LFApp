package com.ufla.lfapp.views.graph.vertex;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.views.graph.BorderVertex;

/**
 * Created by carlos on 11/16/16.
 */

public class VertexDrawnFactory {


    public VertexDraw createVertexDraw(VertexDrawType vertexDrawType,
                                    int vertexRadius,
                                    int vertexCenterPointX,
                                       int vertexCenterPointY,
                                       float vertexInitialStateSize) {
        VertexDraw vertexDraw = null;
        if (vertexDrawType == VertexDrawType.DEFAULT) {
            vertexDraw = new VertexDrawDefault(vertexRadius, vertexCenterPointX,
                    vertexCenterPointY, vertexInitialStateSize);
        } else if (vertexDrawType == VertexDrawType.FINAL) {
            vertexDraw = new VertexDrawFinal(vertexRadius, vertexCenterPointX,
                    vertexCenterPointY, vertexInitialStateSize);
        } else if (vertexDrawType == VertexDrawType.INITIAL) {
            vertexDraw = new VertexDrawInitial(vertexRadius, vertexCenterPointX,
                    vertexCenterPointY, vertexInitialStateSize);
        } else if (vertexDrawType == VertexDrawType.INITIAL_FINAL) {
            vertexDraw = new VertexDrawInitialAndFinal(vertexRadius, vertexCenterPointX,
                    vertexCenterPointY, vertexInitialStateSize);
        } else {
            throw new RuntimeException(ResourcesContext.getString(R.string.exception_vertex_draw_type));
        }
        return new VertexDrawViewBuilder()
                .withInternVertexPath(vertexDraw.getInternVertexPath())
                .withExternVertexPath(vertexDraw.getExternVertexPath())
                .withBorderVertexPath(vertexDraw.getBorderVertexPath())
                .withLabelPath(vertexDraw.getLabelPath())
                .withVertexCenterPointX(vertexCenterPointX)
                .withVertexCenterPointY(vertexCenterPointY)
                .withVertexRadius(vertexRadius)
                .withVertexDrawType(vertexDraw.getVertexDrawType())
                .createVertexDrawView();
    }

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
            throw new RuntimeException(ResourcesContext.getString(R.string.exception_vertex_draw_type));
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
