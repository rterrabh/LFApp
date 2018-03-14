package com.ufla.lfapp.views.graph.edge;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.views.graph.edge.interactarea.InteractArea;

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

    public EdgeDrawViewBuilder withLabelLine(Pair<PointF, PointF> labelLine) {
        edgeDrawView.setLabelLine(labelLine);
        return this;
    }

    public EdgeDrawView createEdgeDrawView() {
        if (edgeDrawView.isInconsistent()) {
            throw new RuntimeException(ResourcesContext.getString(R.string.exception_edge_draw_inconsistent));
        }
        return edgeDrawView;
    }
}
