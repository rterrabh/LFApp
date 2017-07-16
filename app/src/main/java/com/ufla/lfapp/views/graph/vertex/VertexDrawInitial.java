package com.ufla.lfapp.views.graph.vertex;

import android.graphics.Path;
import android.graphics.Point;

import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.views.graph.BorderVertex;
import com.ufla.lfapp.utils.PointUtils;

/**
 * Created by carlos on 11/22/16.
 */

public class VertexDrawInitial extends VertexDrawDefault {

    public VertexDrawInitial(EditGraphLayout editGraphLayout, BorderVertex borderVertex) {
        super(editGraphLayout, borderVertex);
        vertexCenterPointX += vertexSquareDimension;
    }

    public VertexDrawInitial(int vertexRadius, int vertexCenterPointX,
                             int vertexCenterPointY, float vertexInitialStateSize) {
        super(vertexRadius, vertexCenterPointX, vertexCenterPointY, vertexInitialStateSize);
    }

    @Override
    public Path getInternVertexPath() {
        Path internVertexPath = new Path();
        internVertexPath.addCircle(vertexCenterPointX, vertexCenterPointY,
                vertexRadius, Path.Direction.CW);
        return internVertexPath;
    }

    @Override
    public Path getExternVertexPath() {
        Path externVertexPath = new Path();
        externVertexPath.addCircle(vertexCenterPointX, vertexCenterPointY,
                vertexRadius, Path.Direction.CW);
        Point referencePoint  = new Point(vertexCenterPointX - vertexSquareDimension, vertexCenterPointY);
        Point onStatePoint = new Point(vertexCenterPointX - vertexRadius, vertexCenterPointY);
        float angle = PointUtils.angleFromP1ToP2(onStatePoint, referencePoint);
        externVertexPath.moveTo(onStatePoint.x, onStatePoint.y);
        externVertexPath.lineTo(onStatePoint.x + vertexInitialStateSize *
                        (float) Math.cos(angle + Math.toRadians(30)),
                onStatePoint.y + vertexInitialStateSize *
                        (float) Math.sin(angle + Math.toRadians(30)));
        externVertexPath.moveTo(onStatePoint.x, onStatePoint.y);
        externVertexPath.lineTo(onStatePoint.x + vertexInitialStateSize *
                        (float) Math.cos(angle - Math.toRadians(30)),
                onStatePoint.y + vertexInitialStateSize *
                        (float) Math.sin(angle - Math.toRadians(30)));
        return externVertexPath;
    }

    @Override
    public Path getBorderVertexPath() {
        Path borderVertexPath = new Path();
        borderVertexPath.addRect(borderVertex.isLeft() ? borderSpace : 0,
                borderVertex.isTop() ? borderSpace : 0,
                borderVertex.isRight() ? vertexSquareDimension * 2 - borderSpace :
                        vertexSquareDimension * 2,
                borderVertex.isBottom() ? vertexSquareDimension - borderSpace :
                        vertexSquareDimension,
                Path.Direction.CW);
        borderVertexPath.moveTo(vertexSquareDimension, borderVertex.isTop() ? borderSpace : 0);
        borderVertexPath.lineTo(vertexSquareDimension,
                borderVertex.isBottom() ? vertexSquareDimension - borderSpace :
                        vertexSquareDimension);
        return borderVertexPath;
    }

//    @Override
//    public Path getLabelPath() {
//        Path labelPath = new Path();
//        labelPath.moveTo(vertexSquareDimension + (vertexSpace * 2), vertexCenterPointY);
//        labelPath.lineTo((vertexSquareDimension * 2) - (vertexSpace * 2), vertexCenterPointY);
//        return labelPath;
//    }

    @Override
    public VertexDrawType getVertexDrawType() {
        return VertexDrawType.INITIAL;
    }
}
