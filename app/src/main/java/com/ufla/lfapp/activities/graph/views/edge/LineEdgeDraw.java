package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.Point;
import android.util.Pair;

import com.ufla.lfapp.activities.graph.views.EdgeView;

/**
 * Created by carlos on 17/10/16.
 */
public class LineEdgeDraw extends AbstractEdgeDraw {


    public LineEdgeDraw(Pair<Point, Point> gridPoint) {
        super(gridPoint);
    }


    @Override
    public Path getEdge() {
        Path path = new Path();
        path.moveTo(circPoints.first.x, circPoints.first.y);
        path.lineTo(circPoints.second.x, circPoints.second.y);
        return path;
    }

    @Override
    protected Path getInvertedEdge() {
        Path path = new Path();
        path.moveTo(circPoints.second.x, circPoints.second.y);
        path.lineTo(circPoints.first.x, circPoints.first.y);
        return path;
    }

    @Override
    public Path getArrowHead() {
        Path path = new Path();
        float angle = (float) Math.atan2((circPoints.first
                .y - circPoints.second.y), (circPoints.first.x -
                circPoints.second.x));
        float arrowHeady1 = (float) (circPoints.second.y + EdgeView.getArrowHeadLenght() *
                Math.sin(angle - EdgeView.getArrowAngle()));
        float arrowHeadx1 = (float) (circPoints.second.x + EdgeView.getArrowHeadLenght() *
                Math.cos(angle - EdgeView.getArrowAngle()));
        float arrowHeady2 = (float) (circPoints.second.y + EdgeView.getArrowHeadLenght() *
                Math.sin(angle + EdgeView.getArrowAngle()));
        float arrowHeadx2 = (float) (circPoints.second.x + EdgeView.getArrowHeadLenght() *
                Math.cos(angle + EdgeView.getArrowAngle()));
        path.moveTo(circPoints.second.x, circPoints.second.y);
        path.lineTo(arrowHeadx1, arrowHeady1);
        path.moveTo(circPoints.second.x, circPoints.second.y);
        path.lineTo(arrowHeadx2, arrowHeady2);
        return path;
    }
}
