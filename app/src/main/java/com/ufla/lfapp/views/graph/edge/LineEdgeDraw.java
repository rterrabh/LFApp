package com.ufla.lfapp.views.graph.edge;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.views.graph.EdgeView;
import com.ufla.lfapp.utils.PointUtils;

/**
 * Created by carlos on 17/10/16.
 */
public class LineEdgeDraw extends AbstractEdgeDraw {

    private float ERROR_RECT_F_LABEL;

    public LineEdgeDraw(Pair<Point, Point> gridPoint, EditGraphLayout editGraphLayout) {
        super(gridPoint, editGraphLayout);
    }

    @Override
    protected void setCircPointsOnCircumference() {
        //Setando o primeiro ponto na circunferência
        float angle = PointUtils.angleFromP1ToP2(circPoints.first, circPoints.second);
        circPoints.first.x += vertexRadius * Math.cos(angle);
        circPoints.first.y += vertexRadius * Math.sin(angle);

        //Setando o segundo ponto na circunferência
        angle = PointUtils.angleFromP1ToP2(circPoints.second, circPoints.first);
        circPoints.second.x += vertexRadius * Math.cos(angle);
        circPoints.second.y += vertexRadius * Math.sin(angle);
    }

    /**
     * Não possui ponto de controle. Na verdade é ponto médio entre os pontos nas circuferências
     * dos vértices.
     */
    @Override
    protected void setPointControl() {
        ERROR_RECT_F_LABEL = vertexRadius * 0.60f;
    }

    @Override
    protected Pair<PointF, PointF> getPointsControlInteractArea() {
        PointF middlePoint = PointUtils.getMiddlePoint(circPoints);
        float angle = PointUtils.angleFromP1ToP2(middlePoint, circPoints.second);
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
        float angle = PointUtils.angleFromP1ToP2(circPoints.second, circPoints.first);
        float arrowHeady1 = (float) (circPoints.second.y + arrowHeadLenght *
                Math.sin(angle - EdgeView.getArrowAngle()));
        float arrowHeadx1 = (float) (circPoints.second.x + arrowHeadLenght *
                Math.cos(angle - EdgeView.getArrowAngle()));
        float arrowHeady2 = (float) (circPoints.second.y + arrowHeadLenght *
                Math.sin(angle + EdgeView.getArrowAngle()));
        float arrowHeadx2 = (float) (circPoints.second.x + arrowHeadLenght *
                Math.cos(angle + EdgeView.getArrowAngle()));
        path.moveTo(circPoints.second.x, circPoints.second.y);
        path.lineTo(arrowHeadx1, arrowHeady1);
        path.moveTo(circPoints.second.x, circPoints.second.y);
        path.lineTo(arrowHeadx2, arrowHeady2);
        return path;
    }

    @Override
    public Path getLabelPath() {
        PointF middlePoint = PointUtils.getMiddlePoint(circPoints.first, circPoints.second);
        Path labelPath = new Path();
        float displaceForEdgeText = editGraphLayout.getEdgeTextSize() / 2.0f;
        int columnLenght = numColumns * vertexSquareDimension;
        int labelLenght = (int) Math.min(middlePoint.x, columnLenght - middlePoint.x);
        //int labelLenght = getLabelLenght();
        if (isSameRow()) {
            if (isOriginColumnLeft()) {
                labelPath.moveTo(middlePoint.x - labelLenght, circPoints.first.y - displaceForEdgeText);
                labelPath.lineTo(middlePoint.x + labelLenght, circPoints.first.y - displaceForEdgeText);
            } else {
                labelPath.moveTo(middlePoint.x - labelLenght, circPoints.first.y - displaceForEdgeText);
                labelPath.lineTo(middlePoint.x + labelLenght, circPoints.first.y - displaceForEdgeText);
            }
        } else if ((!isOriginColumnLeft() && !isOriginRowUp())
                || (isOriginColumnLeft() && isOriginRowUp())) {
            labelPath.moveTo(middlePoint.x + displaceForEdgeText, middlePoint.y);
            labelPath.lineTo(columnLenght, middlePoint.y);
        } else {
            labelPath.moveTo(0, middlePoint.y);
            labelPath.lineTo(middlePoint.x - displaceForEdgeText, middlePoint.y);
        }
        return labelPath;
    }

    @Override
    public Pair<PointF, PointF> getLabelLine() {
        PointF middlePoint = PointUtils.getMiddlePoint(circPoints.first, circPoints.second);
        float displaceForEdgeText = editGraphLayout.getEdgeTextSize() / 2.0f;
        int columnLenght = numColumns * vertexSquareDimension;
        int labelLenght = (int) Math.min(middlePoint.x, columnLenght - middlePoint.x);
        //int labelLenght = getLabelLenght();
        PointF pointF0;
        PointF pointFX;
        if (isSameRow()) {
            if (isOriginColumnLeft()) {
                pointF0 = new PointF(middlePoint.x - labelLenght, circPoints.first.y - displaceForEdgeText);
                pointFX = new PointF(middlePoint.x + labelLenght, circPoints.first.y - displaceForEdgeText);
            } else {
                pointF0 = new PointF(middlePoint.x - labelLenght, circPoints.first.y - displaceForEdgeText);
                pointFX = new PointF(middlePoint.x + labelLenght, circPoints.first.y - displaceForEdgeText);
            }
        } else if ((!isOriginColumnLeft() && !isOriginRowUp())
                || (isOriginColumnLeft() && isOriginRowUp())) {
            pointF0 = new PointF(middlePoint.x + displaceForEdgeText, middlePoint.y);
            pointFX = new PointF(columnLenght, middlePoint.y);
        } else {
            pointF0 = new PointF(0, middlePoint.y);
            pointFX = new PointF(middlePoint.x - displaceForEdgeText, middlePoint.y);
        }
        return Pair.create(pointF0, pointFX);
    }

    @Override
    public Paint.Align getPaintAlign() {
        if (isSameRow()) {
            return Paint.Align.CENTER;
        }
        if ((!isOriginColumnLeft() && !isOriginRowUp())
                || (isOriginColumnLeft() && isOriginRowUp())) {
            return Paint.Align.LEFT;
        }
        return Paint.Align.RIGHT;
    }

    @Override
    public EdgeDrawType getEdgeDrawType() {
        return EdgeDrawType.LINE_EDGE_DRAW;
    }
}
