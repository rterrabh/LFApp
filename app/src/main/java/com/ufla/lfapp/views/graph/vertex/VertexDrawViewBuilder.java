package com.ufla.lfapp.views.graph.vertex;

import android.graphics.Path;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.ResourcesContext;

/**
 * Created by carlos on 1/17/17.
 */

public class VertexDrawViewBuilder {

    private VertexDrawView vertexDrawView;

    public VertexDrawViewBuilder() {
        vertexDrawView = new VertexDrawView();
    }

    public VertexDrawViewBuilder withInternVertexPath(Path internVertexPath) {
        vertexDrawView.setInternVertexPath(internVertexPath);
        return this;
    }

    public VertexDrawViewBuilder withExternVertexPath(Path externVertexPath) {
        vertexDrawView.setExternVertexPath(externVertexPath);
        return this;
    }

    public VertexDrawViewBuilder withBorderVertexPath(Path borderVertexPath) {
        vertexDrawView.setBorderVertexPath(borderVertexPath);
        return this;
    }

    public VertexDrawViewBuilder withLabelPath(Path labelPath) {
        vertexDrawView.setLabelPath(labelPath);
        return this;
    }

    public VertexDrawViewBuilder withVertexCenterPoint(int vertexCenterPoint) {
        vertexDrawView.setVertexCenterPoint(vertexCenterPoint);
        return this;
    }

    public VertexDrawViewBuilder withVertexCenterPointX(int vertexCenterPointX) {
        vertexDrawView.setVertexCenterPointX(vertexCenterPointX);
        return this;
    }

    public VertexDrawViewBuilder withVertexCenterPointY(int vertexCenterPointY) {
        vertexDrawView.setVertexCenterPointY(vertexCenterPointY);
        return this;
    }

    public VertexDrawViewBuilder withVertexRadius(int vertexRadius) {
        vertexDrawView.setVertexRadius(vertexRadius);
        return this;
    }

    public VertexDrawViewBuilder withVertexDrawType(VertexDrawType vertexDrawType) {
        vertexDrawView.setVertexDrawType(vertexDrawType);
        return this;
    }

    public VertexDrawView createVertexDrawView() {
        if (vertexDrawView.isInconsistent()) {
            throw new RuntimeException(ResourcesContext.getString(R.string.exception_vertex_draw_inconsistent));
        }
        return vertexDrawView;
    }
}
