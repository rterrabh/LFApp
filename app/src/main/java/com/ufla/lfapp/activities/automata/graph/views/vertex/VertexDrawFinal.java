package com.ufla.lfapp.activities.automata.graph.views.vertex;

import android.graphics.Path;

import com.ufla.lfapp.activities.automata.graph.layout.EditGraphLayout;
import com.ufla.lfapp.activities.automata.graph.views.BorderVertex;

/**
 * Created by carlos on 11/22/16.
 */

public class VertexDrawFinal extends VertexDrawDefault {

    public VertexDrawFinal(EditGraphLayout editGraphLayout, BorderVertex borderVertex) {
        super(editGraphLayout, borderVertex);
    }

    @Override
    public Path getInternVertexPath() {
        Path internVertexPath = new Path();
        internVertexPath.addCircle(vertexCenterPoint, vertexCenterPoint, vertexRadius - vertexSpace,
                Path.Direction.CW);
        return internVertexPath;
    }

    @Override
    public Path getExternVertexPath() {
        Path externVertexPath = super.getExternVertexPath();
        externVertexPath.addCircle(vertexCenterPoint, vertexCenterPoint, vertexRadius - vertexSpace,
                Path.Direction.CW);
        return externVertexPath;
    }

    @Override
    public VertexDrawType getVertexDrawType() {
        return VertexDrawType.FINAL;
    }

}
