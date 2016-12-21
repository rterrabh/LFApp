package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.graph.layout.EditGraphLayout;
import com.ufla.lfapp.activities.graph.views.PointUtils;
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
    protected int vertexRadius;
    protected int vertexSpace;
    protected int vertexSquareDimension;
    protected float arrowHeadLenght;

    public AbstractEdgeDraw(Pair<Point, Point> gridPoints, EditGraphLayout editGraphLayout) {
        this.gridPoints = gridPoints;
        vertexRadius = editGraphLayout.getVertexRadius();
        vertexSpace = editGraphLayout.getVertexSpace();
        vertexSquareDimension = editGraphLayout.getVertexSquareDimension();
        arrowHeadLenght = editGraphLayout.getEdgeArrowHeadLenght();
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

    @Override
    public Path getPathInteractArea() {
        return interactArea.getInteractArea();
    }

    @Override
    public InteractArea getInteractArea() {
        return interactArea.clone();
    }

    @Override
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
        first.y = gridPointsOnOrigin.first.y * vertexSquareDimension +
                vertexRadius + vertexSpace;
        first.x = gridPointsOnOrigin.first.x * vertexSquareDimension +
                vertexRadius + vertexSpace;
        second.y = gridPointsOnOrigin.second.y * vertexSquareDimension +
                vertexRadius + vertexSpace;
        second.x = gridPointsOnOrigin.second.x * vertexSquareDimension +
                vertexRadius + vertexSpace;

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
