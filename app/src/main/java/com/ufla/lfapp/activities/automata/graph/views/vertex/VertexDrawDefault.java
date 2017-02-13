package com.ufla.lfapp.activities.automata.graph.views.vertex;

import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.automata.graph.layout.EditGraphLayout;
import com.ufla.lfapp.activities.automata.graph.views.BorderVertex;
import com.ufla.lfapp.activities.automata.graph.views.PointUtils;

/**
 * Created by carlos on 11/22/16.
 */

public class VertexDrawDefault implements VertexDraw {

    protected int vertexSquareDimension;
    protected int vertexCenterPoint;
    protected int vertexRadius;
    protected int vertexSpace;
    protected float vertexInitialStateSize;
    protected BorderVertex borderVertex;
    protected int borderSpace = 5;

    public VertexDrawDefault(EditGraphLayout editGraphLayout, BorderVertex borderVertex) {
        vertexSquareDimension = editGraphLayout.getVertexSquareDimension();
        vertexCenterPoint = editGraphLayout.getVertexCenterPoint();
        vertexRadius = editGraphLayout.getVertexRadius();
        vertexSpace = editGraphLayout.getVertexSpace();
        vertexInitialStateSize = editGraphLayout.getVertexInitialStateSize();
        this.borderVertex = borderVertex;
    }

    @Override
    public Path getInternVertexPath() {
        Path internVertexPath = new Path();
        internVertexPath.addCircle(vertexCenterPoint, vertexCenterPoint, vertexRadius,
                Path.Direction.CW);
        return internVertexPath;
    }

    @Override
    public Path getExternVertexPath() {
        Path externVertexPath = new Path();
        externVertexPath.addCircle(vertexCenterPoint, vertexCenterPoint, vertexRadius,
                Path.Direction.CW);
        return externVertexPath;
    }

    @Override
    public Path getBorderVertexPath() {
        Path borderVertexPath = new Path();
        borderVertexPath.addRect(borderVertex.isLeft() ? borderSpace : 0,
                borderVertex.isTop() ? borderSpace : 0,
                borderVertex.isRight() ? vertexSquareDimension - borderSpace :
                        vertexSquareDimension,
                borderVertex.isBottom() ? vertexSquareDimension - borderSpace :
                        vertexSquareDimension,
                Path.Direction.CW);
        return borderVertexPath;
    }

    @Override
    public Path getLabelPath() {
        Path labelPath = new Path();
        labelPath.moveTo(vertexSpace * 2, vertexCenterPoint);
        labelPath.lineTo(vertexSquareDimension - (vertexSpace * 2), vertexCenterPoint);
        return labelPath;
    }

    @Override
    public boolean isOnInteractArea(PointF pointF) {
        return PointUtils.dist(Pair.create(pointF,
                new PointF(vertexCenterPoint, vertexCenterPoint))) <= vertexRadius;
    }

    @Override
    public VertexDrawType getVertexDrawType() {
        return VertexDrawType.DEFAULT;
    }
}
