package com.ufla.lfapp.views.graph.edge;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.utils.PointUtils;
import com.ufla.lfapp.views.graph.edge.interactarea.InteractArea;
import com.ufla.lfapp.views.graph.edge.interactarea.InteractQuadrilateralAreaBuilder;

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
    protected EditGraphLayout editGraphLayout;
    protected int numColumns;

    public AbstractEdgeDraw(Pair<Point, Point> gridPoints, EditGraphLayout editGraphLayout) {
        this.gridPoints = gridPoints;
        vertexRadius = editGraphLayout.getVertexRadius();
        vertexSpace = editGraphLayout.getVertexSpace();
        vertexSquareDimension = editGraphLayout.getVertexSquareDimension();
        arrowHeadLenght = editGraphLayout.getEdgeArrowHeadLenght();
        numColumns = editGraphLayout.getColumnCount();
        this.editGraphLayout = editGraphLayout;
        setCircPointsOnCenter();
        setCircPointsOnCircumference();
        setPointControl();
        defineInteractArea();
    }

    protected abstract Pair<PointF, PointF> getPointsControlInteractArea();

    protected abstract void setCircPointsOnCircumference();

    protected abstract void setPointControl();

    protected void defineInteractArea() {
        interactArea = new InteractQuadrilateralAreaBuilder()
                .withCircumferencePoints(circPoints)
                .withControlPoints(getPointsControlInteractArea())
                .create();
    }

    protected boolean isSamePoints() {
        return gridPoints.first.equals(gridPoints.second);
    }

    protected boolean isSameColumn() {
        return gridPoints.first.x == gridPoints.second.x;
    }

    protected boolean isSameRow() {
        return gridPoints.first.y == gridPoints.second.y;
    }

    protected boolean isOriginColumnLeft() {
        return gridPoints.first.x < gridPoints.second.x;
    }

    protected boolean isOriginRowUp() {
        return gridPoints.first.y < gridPoints.second.y;
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

    @Override
    public float distanceToObject(PointF point) {
        return interactArea.distanceToObject(point);
    }

    @Override
    public boolean isOnInteractLabelArea(PointF point) {
        return interactArea.isOnInteractLabelArea(point);
    }

    /**
     * Calcula os pontos mais próximos nas circunferências entre dois círculos. Recebe um par de
     * pontos que identificam a localização dos círculos no grid, então calcula o ponto na
     * circunferência de cada círculo que está mais próximo do outro círculo.
     *
     * @return
     */
    protected void setCircPointsOnCenter() {
        //Pair<Point, Point> gridPointsOnOrigin = PointUtils.getPointsOnOrigin(gridPoints);
        PointF first = new PointF();
        PointF second = new PointF();
        //Setando os pontos no centro de cada círculo
        first.y = gridPoints.first.y * vertexSquareDimension +
                vertexRadius + vertexSpace;
        first.x = gridPoints.first.x * vertexSquareDimension +
                vertexRadius + vertexSpace;
        second.y = gridPoints.second.y * vertexSquareDimension +
                vertexRadius + vertexSpace;
        second.x = gridPoints.second.x * vertexSquareDimension +
                vertexRadius + vertexSpace;
//        if (!isSamePoints()) {
//            if (isSameRow()) {
//                first.y += vertexSquareDimension;
//                second.y += vertexSquareDimension;
//            } else if (isSameColumn()) {
//                first.x += vertexSquareDimension;
//                second.x += vertexSquareDimension;
//            }
//        }
        circPoints = Pair.create(first, second);
    }

    protected int getLabelLenght() {
        return vertexSquareDimension;
    }

    @Override
    public Path getLabelPath() {
        if (isOriginColumnLeft() || (isSameColumn() && isOriginRowUp())) {
            return getEdge();
        } else {
            return getInvertedEdge();
        }
    }

    @Override
    public Paint.Align getPaintAlign() {
        return Paint.Align.CENTER;
    }

}
