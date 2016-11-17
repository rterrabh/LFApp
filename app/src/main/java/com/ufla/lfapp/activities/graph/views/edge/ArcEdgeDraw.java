package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.Point;
import android.util.Pair;

import com.ufla.lfapp.activities.graph.views.edge.arc.ArcEdgeDrawType;

/**
 * Created by carlos on 17/10/16.
 */
public class ArcEdgeDraw extends AbstractEdgeDraw {

    private ArcEdgeDrawType arcEdgeDrawType;

    public ArcEdgeDraw(Pair<Point, Point> gridPoints) {
        super(gridPoints);
        arcEdgeDrawType = new ArcEdgeDrawImplement(circPoints);
        /*
        if (sameColumn() && circPoints.first.y < circPoints.second.y) {
            arcEdgeDrawType = new ArcEdgeDrawColumnUp(circPoints);
        } else if (sameColumn() && circPoints.first.y > circPoints.second.y) {
            arcEdgeDrawType = new ArcEdgeDrawColumnBottom(circPoints);
        } else if (sameLine() && circPoints.first.x < circPoints.second.x) {
            arcEdgeDrawType = new ArcEdgeDrawLineUp(circPoints);
        } else if (sameLine() && circPoints.first.x > circPoints.second.x) {
            arcEdgeDrawType = new ArcEdgeDrawLineBottom(circPoints);
        } else if (isQuadrantI()) {
            arcEdgeDrawType = new ArcEdgeDrawQuadrantI(circPoints);
        } else if (isQuadrantII()) {
            arcEdgeDrawType = new ArcEdgeDrawQuadrantII(circPoints);
        } else if (isQuadrantIII()) {
            arcEdgeDrawType = new ArcEdgeDrawQuadrantIII(circPoints);
        } else if (isQuadrantIV()) {
            arcEdgeDrawType = new ArcEdgeDrawQuadrantIV(circPoints);
        }*/
    }

    private boolean sameColumn() {
        return Math.abs(circPoints.first.x - circPoints.second.x) < 0.1f;
    }

    private boolean sameLine() {
        return Math.abs(circPoints.first.y - circPoints.second.y) < 0.1f;
    }

    private boolean isQuadrantI() {
        return circPoints.first.x < circPoints.second.x &&
                circPoints.first.y < circPoints.second.y;
    }

    private boolean isQuadrantII() {
        return circPoints.first.x < circPoints.second.x &&
                circPoints.first.y > circPoints.second.y;
    }

    private boolean isQuadrantIII() {
        return circPoints.first.x > circPoints.second.x &&
                circPoints.first.y > circPoints.second.y;
    }

    private boolean isQuadrantIV() {
        return circPoints.first.x > circPoints.second.x &&
                circPoints.first.y < circPoints.second.y;
    }


    @Override
    public Path getEdge() {
        Path edge = arcEdgeDrawType.getEdge();
        return edge;
    }

    @Override
    public Path getInvertedEdge() {
        return arcEdgeDrawType.getInvertedEdge();
    }

    @Override
    public Path getArrowHead() {
        return arcEdgeDrawType.getArrowHead();
    }
}
