package com.ufla.lfapp.views.graph.edge;

import android.graphics.Point;
import android.support.v4.util.Pair;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;

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
            throw new RuntimeException(ResourcesContext.getString(R.string.exception_edge_draw_type_not_found));
        }
        return new EdgeDrawViewBuilder()
                .withEdge(edgeDraw.getEdge())
                .withLabelPath(edgeDraw.getLabelPath())
                .withArrowHead(edgeDraw.getArrowHead())
                .withInteractArea(edgeDraw.getInteractArea())
                .withPaintAlign(edgeDraw.getPaintAlign())
                .withEdgeDrawType(edgeDraw.getEdgeDrawType())
                .withLabelLine(edgeDraw.getLabelLine())
                .createEdgeDrawView();
    }
}
