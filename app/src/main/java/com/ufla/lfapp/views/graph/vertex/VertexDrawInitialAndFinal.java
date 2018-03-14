package com.ufla.lfapp.views.graph.vertex;

import android.graphics.Path;

import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.views.graph.BorderVertex;

/**
 * Created by carlos on 1/17/17.
 */

public class VertexDrawInitialAndFinal extends VertexDrawInitial {

    public VertexDrawInitialAndFinal(EditGraphLayout editGraphLayout, BorderVertex borderVertex) {
        super(editGraphLayout, borderVertex);
    }

    public VertexDrawInitialAndFinal(int vertexRadius, int vertexCenterPointX,
                                     int vertexCenterPointY, float vertexInitialStateSize) {
        super(vertexRadius, vertexCenterPointX, vertexCenterPointY, vertexInitialStateSize);
    }

    @Override
    public Path getInternVertexPath() {
        Path internVertexPath = new Path();
        internVertexPath.addCircle(vertexCenterPointX, vertexCenterPointY,
                vertexRadius - vertexSpace, Path.Direction.CW);
        return internVertexPath;
    }

    @Override
    public Path getExternVertexPath() {
        Path externVertexPath = super.getExternVertexPath();
        externVertexPath.addCircle(vertexCenterPointX, vertexCenterPointY,
                vertexRadius - vertexSpace, Path.Direction.CW);
        return externVertexPath;
    }

    @Override
    public VertexDrawType getVertexDrawType() {
        return VertexDrawType.INITIAL_FINAL;
    }

}
