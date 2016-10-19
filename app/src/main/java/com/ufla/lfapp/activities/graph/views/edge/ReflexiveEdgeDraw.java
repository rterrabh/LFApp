package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.Point;
import android.util.Pair;

import com.ufla.lfapp.activities.graph.views.edge.reflexive.ReflexiveEdgeDrawBottom;
import com.ufla.lfapp.activities.graph.views.edge.reflexive.ReflexiveEdgeDrawType;
import com.ufla.lfapp.activities.graph.views.edge.reflexive.ReflexiveEdgeDrawUp;

/**
 * Created by carlos on 18/10/16.
 */
public class ReflexiveEdgeDraw extends AbstractEdgeDraw {

    private ReflexiveEdgeDrawType reflexiveEdgeDraw;
    private static int cont = 0;

    public ReflexiveEdgeDraw(Pair<Point, Point> gridPoints) {
        super(gridPoints);
        if (cont % 2 == 0) {
            reflexiveEdgeDraw = new ReflexiveEdgeDrawBottom(circPoints);
        } else {
            reflexiveEdgeDraw = new ReflexiveEdgeDrawUp(circPoints);
        }
        cont++;
    }

    @Override
    public Path getEdge() {
        return reflexiveEdgeDraw.getEdge();
    }

    @Override
    public Path getInvertedEdge() {
        return reflexiveEdgeDraw.getInvertedEdge();
    }

    @Override
    public Path getLabelPath() {
        if (reflexiveEdgeDraw instanceof ReflexiveEdgeDrawUp) {
            return reflexiveEdgeDraw.getEdge();
        } else if (reflexiveEdgeDraw instanceof ReflexiveEdgeDrawBottom) {
            return reflexiveEdgeDraw.getInvertedEdge();
        } else {
            throw new RuntimeException("Instância inválida!");
        }
    }

    @Override
    public Path getArrowHead() {
        return reflexiveEdgeDraw.getArrowHead();
    }

}
