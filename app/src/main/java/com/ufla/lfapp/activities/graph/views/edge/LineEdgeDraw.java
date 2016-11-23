package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.graph.views.EdgeView;
import com.ufla.lfapp.activities.graph.views.PointUtils;
import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 17/10/16.
 */
public class LineEdgeDraw extends AbstractEdgeDraw {

    private static final float ERROR_RECT_F_LABEL = VertexView.stateRadius * 0.60f;

    public LineEdgeDraw(Pair<Point, Point> gridPoint) {
        super(gridPoint);
    }

    @Override
    protected void setCircPointsOnCircumference() {
        //Setando o primeiro ponto na circunferência
        float angle = (float) Math.atan2((circPoints.second.y - circPoints.first.y),
                (circPoints.second.x - circPoints.first.x));
        circPoints.first.x += VertexView.stateRadius * Math.cos(angle);
        circPoints.first.y += VertexView.stateRadius * Math.sin(angle);

        //Setando o segundo ponto na circunferência
        angle = (float) Math.atan2((circPoints.first.y - circPoints.second.y),
                (circPoints.first.x - circPoints.second.x));
        circPoints.second.x += VertexView.stateRadius * Math.cos(angle);
        circPoints.second.y += VertexView.stateRadius * Math.sin(angle);
    }

    /**
     * Não possui ponto de controle. Na verdade é ponto médio entre os pontos nas circuferências
     * dos vértices.
     */
    @Override
    protected void setPointControl() {
        return;
    }

    @Override
    protected Pair<PointF, PointF> getPointsControlInteractArea() {
        PointF middlePoint = PointUtils.getMiddlePoint(circPoints);
        float angle = (float) Math.atan2((circPoints.second.y - middlePoint.y),
                (circPoints.second.x - middlePoint.x));
        PointF pointControl1 = new PointF();
        PointF pointControl2 = new PointF();
        pointControl1.x = (float) (middlePoint.x + ERROR_RECT_F_LABEL *
                Math.cos(angle - ANGLE_90));
        pointControl1.y = (float) (middlePoint.y + ERROR_RECT_F_LABEL *
                Math.sin(angle - ANGLE_90));
        pointControl2.x = (float) (middlePoint.x + ERROR_RECT_F_LABEL *
                Math.cos(angle + ANGLE_90));
        pointControl2.y = (float) (middlePoint.y + ERROR_RECT_F_LABEL *
                Math.sin(angle + ANGLE_90));
        return Pair.create(pointControl1, pointControl2);
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