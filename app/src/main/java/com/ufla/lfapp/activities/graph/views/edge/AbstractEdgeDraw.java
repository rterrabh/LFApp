package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Pair;

import com.ufla.lfapp.activities.graph.views.VertexView;

/**
 * Created by carlos on 17/10/16.
 */
public abstract class AbstractEdgeDraw implements EdgeDraw {

    protected Pair<Point, Point> gridPoints;
    protected Pair<PointF, PointF> circPoints;

    public AbstractEdgeDraw(Pair<Point, Point> gridPoints) {
        this.gridPoints = gridPoints;
        setCircPointsOnCenter();
    }

    public Pair<Point, Point> getGridPoints() {
        return this.gridPoints;
    }

    /**
     * Calcula a distância entre dois pontos.
     *
     * @param points par de pontos em que será calculada a distância
     * @return distância entre os dois pontos
     */
    public float dist(Pair<PointF, PointF> points) {
        float distX = points.second.x - points.first.x;
        float distY = points.second.y - points.first.y;
        return (float) Math.sqrt(distX * distX + distY * distY);
    }


    /**
     * Calcula os pontos mais próximos nas circunferências entre dois círculos. Recebe um par de
     * pontos que identificam a localização dos círculos no grid, então calcula o ponto na
     * circunferência de cada círculo que está mais próximo do outro círculo.
     *
     * @return
     */
    private void setCircPointsOnCenter() {
        Pair<Point, Point> gridPointsOnOrigin = getGridPointsOnOrigin();
        PointF first = new PointF();
        PointF second = new PointF();
        //Setando os pontos no centro de cada círculo
        first.y = gridPointsOnOrigin.first.y * VertexView.squareDimension() +
                VertexView.stateRadius + VertexView.SPACE;
        first.x = gridPointsOnOrigin.first.x * VertexView.squareDimension() +
                VertexView.stateRadius + VertexView.SPACE;
        second.y = gridPointsOnOrigin.second.y * VertexView.squareDimension() +
                VertexView.stateRadius + VertexView.SPACE;
        second.x = gridPointsOnOrigin.second.x * VertexView.squareDimension() +
                VertexView.stateRadius + VertexView.SPACE;

        if (first.x == second.x && first.y == second.y) {
            float sqDim = VertexView.squareDimension();
            first.y += sqDim;
            second.y += sqDim;
        } else {
//            //Setando o primeiro ponto na circunferência
//            float angle = (float) Math.atan2((second.y - first.y), (second.x - first.x));
//            first.x += VertexView.stateRadius * Math.cos(angle);
//            first.y += VertexView.stateRadius * Math.sin(angle);
//
//            //Setando o segundo ponto na circunferência
//            angle = (float) Math.atan2((first.y - second.y), (first.x - second.x));
//            second.x += VertexView.stateRadius * Math.cos(angle);
//            second.y += VertexView.stateRadius * Math.sin(angle);
        }

        circPoints = Pair.create(first, second);
    }

    private Point clonePoint(Point point) {
        Point pointClone = new Point();
        pointClone.set(point.x, point.y);
        return pointClone;
    }

    private Pair<Point, Point> clonePairPoints(Pair<Point, Point> pairPoints) {
        Point pointFirstClone = clonePoint(pairPoints.first);
        Point pointSecondClone = clonePoint(pairPoints.second);
        return Pair.create(pointFirstClone, pointSecondClone);
    }

    private Pair<Point, Point> getGridPointsOnOrigin() {
        Pair<Point, Point> gridPointsOnOrigin = clonePairPoints(gridPoints);
        int minx = Math.min(gridPointsOnOrigin.first.x, gridPointsOnOrigin.second.x);
        int miny = Math.min(gridPointsOnOrigin.first.y, gridPointsOnOrigin.second.y);
        gridPointsOnOrigin.first.x -= minx;
        gridPointsOnOrigin.second.x -= minx;
        gridPointsOnOrigin.first.y -= miny;
        gridPointsOnOrigin.second.y -= miny;
        return gridPointsOnOrigin;
    }

    protected abstract Path getInvertedEdge();

    @Override
    public Path getLabelPath() {
        if (gridPoints.first.x < gridPoints.second.x || (gridPoints.first.x == gridPoints.second.x
                && gridPoints.first.y <= gridPoints.second.y)) {
            return getEdge();
        } else {
            return getInvertedEdge();
        }
    }

}
