package com.ufla.lfapp.activities.automata.graph.views.vertex;

import android.graphics.Path;
import android.graphics.Point;

import com.ufla.lfapp.activities.automata.graph.layout.EditGraphLayout;
import com.ufla.lfapp.activities.automata.graph.views.BorderVertex;
import com.ufla.lfapp.activities.automata.graph.views.PointUtils;

/**
 * Created by carlos on 11/22/16.
 */

public class VertexDrawInitial extends VertexDrawDefault {

    public VertexDrawInitial(EditGraphLayout editGraphLayout, BorderVertex borderVertex) {
        super(editGraphLayout, borderVertex);
    }

    @Override
    public Path getInternVertexPath() {
        Path internVertexPath = new Path();
        internVertexPath.addCircle(vertexSquareDimension + vertexCenterPoint, vertexCenterPoint,
                vertexRadius, Path.Direction.CW);
        return internVertexPath;
    }

    @Override
    public Path getExternVertexPath() {
        Path externVertexPath = new Path();
        externVertexPath.addCircle(vertexSquareDimension + vertexCenterPoint, vertexCenterPoint,
                vertexRadius, Path.Direction.CW);
        Point referencePoint  = new Point(vertexCenterPoint, vertexCenterPoint);
        Point onStatePoint = new Point(vertexSquareDimension + vertexSpace, vertexCenterPoint);
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

    @Override
    public Path getLabelPath() {
        Path labelPath = new Path();
        labelPath.moveTo(vertexSquareDimension + (vertexSpace * 2), vertexCenterPoint);
        labelPath.lineTo((vertexSquareDimension * 2) - (vertexSpace * 2), vertexCenterPoint);
        return labelPath;
    }

    @Override
    public VertexDrawType getVertexDrawType() {
        return VertexDrawType.INITIAL;
    }
}
