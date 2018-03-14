package com.ufla.lfapp.views.graph.vertex;

import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.views.graph.BorderVertex;
import com.ufla.lfapp.utils.PointUtils;

/**
 * Created by carlos on 11/22/16.
 */

public class VertexDrawDefault implements VertexDraw {

    protected int vertexSquareDimension;
    protected int vertexCenterPointX;
    protected int vertexCenterPointY;
    protected int vertexRadius;
    protected int vertexSpace;
    protected float vertexInitialStateSize;
    protected BorderVertex borderVertex;
    protected int borderSpace = 5;

    public VertexDrawDefault(EditGraphLayout editGraphLayout, BorderVertex borderVertex) {
        vertexSquareDimension = editGraphLayout.getVertexSquareDimension();
        vertexCenterPointX = editGraphLayout.getVertexCenterPoint();
        vertexCenterPointY = vertexCenterPointX;
        vertexRadius = editGraphLayout.getVertexRadius();
        vertexSpace = editGraphLayout.getVertexSpace();
        vertexInitialStateSize = editGraphLayout.getVertexInitialStateSize();
        this.borderVertex = borderVertex;
    }

    public VertexDrawDefault(int vertexRadius, int vertexCenterPointX,
                             int vertexCenterPointY, float vertexInitialStateSize) {
        this.vertexCenterPointX = vertexCenterPointX;
        this.vertexCenterPointY = vertexCenterPointY;
        this.vertexRadius = vertexRadius;
        vertexSpace = (int) (vertexRadius * 0.15f);
        vertexSquareDimension = (vertexRadius + vertexSpace) * 2;
        this.vertexInitialStateSize = vertexInitialStateSize;
        this.borderVertex = new BorderVertex(false, false, false, false);
    }

    @Override
    public Path getInternVertexPath() {
        Path internVertexPath = new Path();
        internVertexPath.addCircle(vertexCenterPointX, vertexCenterPointY, vertexRadius,
                Path.Direction.CW);
        return internVertexPath;
    }

    @Override
    public Path getExternVertexPath() {
        Path externVertexPath = new Path();
        externVertexPath.addCircle(vertexCenterPointX, vertexCenterPointY, vertexRadius,
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
        labelPath.moveTo(vertexCenterPointX - (vertexRadius - vertexSpace), vertexCenterPointY);
        labelPath.lineTo(vertexCenterPointX + (vertexRadius - vertexSpace), vertexCenterPointY);
        return labelPath;
    }

    @Override
    public boolean isOnInteractArea(PointF pointF) {
        return PointUtils.dist(Pair.create(pointF,
                new PointF(vertexCenterPointX, vertexCenterPointY))) <= vertexRadius;
    }

    @Override
    public VertexDrawType getVertexDrawType() {
        return VertexDrawType.DEFAULT;
    }
}
