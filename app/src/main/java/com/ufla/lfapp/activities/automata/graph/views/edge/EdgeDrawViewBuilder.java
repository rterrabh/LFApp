package com.ufla.lfapp.activities.automata.graph.views.edge;

import android.graphics.Paint;
import android.graphics.Path;

import com.ufla.lfapp.activities.automata.graph.views.edge.interactarea.InteractArea;

/**
 * Created by carlos on 11/18/16.
 */

public class EdgeDrawViewBuilder {

    private EdgeDrawView edgeDrawView;

    public EdgeDrawViewBuilder() {
        edgeDrawView = new EdgeDrawView();
    }

    public EdgeDrawViewBuilder withEdge(Path edge) {
        edgeDrawView.setEdge(edge);
        return this;
    }

    public EdgeDrawViewBuilder withLabelPath(Path labelPath) {
        edgeDrawView.setLabelPath(labelPath);
        return this;
    }

    public EdgeDrawViewBuilder withArrowHead(Path arrowHead) {
        edgeDrawView.setArrowHead(arrowHead);
        return this;
    }

    public EdgeDrawViewBuilder withInteractArea(InteractArea interactArea) {
        edgeDrawView.setInteractArea(interactArea);
        return this;
    }

    public EdgeDrawViewBuilder withPaintAlign(Paint.Align paintAlign) {
        edgeDrawView.setPaintAlign(paintAlign);
        return this;
    }

    public EdgeDrawViewBuilder withEdgeDrawType(EdgeDrawType edgeDrawType) {
        edgeDrawView.setEdgeDrawType(edgeDrawType);
        return this;
    }

    public EdgeDrawView createEdgeDrawView() {
        if (edgeDrawView.isInconsistent()) {
            throw new RuntimeException("Instance of EdgeDrawView is inconsistent!");
        }
        return edgeDrawView;
    }
}
