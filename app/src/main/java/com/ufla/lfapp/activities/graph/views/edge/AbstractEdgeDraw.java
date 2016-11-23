package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.graph.views.PointUtils;
import com.ufla.lfapp.activities.graph.views.VertexView;
import com.ufla.lfapp.activities.graph.views.edge.interactarea.InteractArea;
import com.ufla.lfapp.activities.graph.views.edge.interactarea.InteractQuadrilateralAreaBuilder;

/**
 * Created by carlos on 17/10/16.
 */
public abstract class AbstractEdgeDraw implements EdgeDraw {

    protected static final double ANGLE_90 = Math.toRadians(90.0f);
    protected Pair<Point, Point> gridPoints;
    protected Pair<PointF, PointF> circPoints;
    protected InteractArea interactArea;

    public AbstractEdgeDraw(Pair<Point, Point> gridPoints) {
        this.gridPoints = gridPoints;
        setCircPointsOnCenter();
        setCircPointsOnCircumference();
        setPointControl();
        defineInteractArea();
    }

    protected abstract Pair<PointF, PointF> getPointsControlInteractArea();

    protected abstract void setCircPointsOnCircumference();

    protected abstract void setPointControl();

    private void defineInteractArea() {
        interactArea = new InteractQuadrilateralAreaBuilder()
                .withCircumferencePoints(circPoints)
                .withControlPoints(getPointsControlInteractArea())
                .create();
    }

    protected abstract Path getInvertedEdge();

    @Override
    public float distanceFromCircumferences() {
        return interactArea.distanceFromCircumferences();
    }

    @Override
    public float distanceToCircumferenceOfSourceVertex(PointF point) {
        return interactArea.distanceToCircumferenceOfSourceVertex(point);
    }

    public Path getPathInteractArea() {
        return interactArea.getInteractArea();
    }

    public InteractArea getInteractArea() {
        return interactArea.clone();
    }

    public boolean isOnInteractArea(PointF point) {
        return interactArea.isOnInteractArea(point);
    }

    /**
     * Calcula os pontos mais próximos nas circunferências entre dois círculos. Recebe um par de
     * pontos que identificam a localização dos círculos no grid, então calcula o ponto na
     * circunferência de cada círculo que está mais próximo do outro círculo.
     *
     * @return
     */
    protected void setCircPointsOnCenter() {
        Pair<Point, Point> gridPointsOnOrigin = PointUtils.getPointsOnOrigin(gridPoints);
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

        circPoints = Pair.create(first, second);
    }

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
