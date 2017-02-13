package com.ufla.lfapp.activities.automata.graph.views.edge;

import android.graphics.Point;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.automata.graph.layout.EditGraphLayout;

/**
 * Created by carlos on 11/17/16.
 */

public class EdgeDrawFactory {

    public EdgeDraw createEdgeDraw(EdgeDrawType edgeDrawType, Pair<Point, Point> gridPoints,
                                   EditGraphLayout editGraphLayout) {
        EdgeDraw edgeDraw = null;
        if (edgeDrawType == EdgeDrawType.LINE_EDGE_DRAW) {
            edgeDraw = new LineEdgeDraw(gridPoints, editGraphLayout);
        } else if (edgeDrawType == EdgeDrawType.ARC_EDGE_DRAW) {
            edgeDraw = new ArcEdgeDraw(gridPoints, editGraphLayout);
        } else if (edgeDrawType == EdgeDrawType.REFLEXIVE_BOTTOM_EDGE_DRAW) {
            edgeDraw = new ReflexiveBottomEdgeDraw(gridPoints, editGraphLayout);
        } else if (edgeDrawType == EdgeDrawType.REFLEXIVE_UP_EDGE_DRAW) {
            edgeDraw = new ReflexiveUpEdgeDraw(gridPoints, editGraphLayout);
        } else {
            throw new RuntimeException("Edge draw type don't exist!");
        }
        return new EdgeDrawViewBuilder()
                .withEdge(edgeDraw.getEdge())
                .withLabelPath(edgeDraw.getLabelPath())
                .withArrowHead(edgeDraw.getArrowHead())
                .withInteractArea(edgeDraw.getInteractArea())
                .withPaintAlign(edgeDraw.getPaintAlign())
                .withEdgeDrawType(edgeDraw.getEdgeDrawType())
                .createEdgeDrawView();
    }
}
