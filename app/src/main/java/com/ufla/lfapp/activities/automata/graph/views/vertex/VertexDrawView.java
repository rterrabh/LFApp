package com.ufla.lfapp.activities.automata.graph.views.vertex;

import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.activities.automata.graph.views.PointUtils;

/**
 * Created by carlos on 1/17/17.
 */

public class VertexDrawView implements VertexDraw {

    private Path internVertexPath;
    private Path externVertexPath;
    private Path borderVertexPath;
    private Path labelPath;
    private int vertexCenterPoint;
    private int vertexRadius;
    private VertexDrawType vertexDrawType;

    public VertexDrawView(Path internVertexPath, Path externVertexPath, Path borderVertexPath,
                          Path labelPath, int vertexCenterPoint, int vertexRadius,
                          VertexDrawType vertexDrawType) {
        this.internVertexPath = internVertexPath;
        this.externVertexPath = externVertexPath;
        this.borderVertexPath = borderVertexPath;
        this.labelPath = labelPath;
        this.vertexCenterPoint = vertexCenterPoint;
        this.vertexRadius = vertexRadius;
        this.vertexDrawType = vertexDrawType;
    }

    protected VertexDrawView()  {

    }

    public Path getInternVertexPath() {
        return internVertexPath;
    }

    public Path getExternVertexPath() {
        return externVertexPath;
    }

    public Path getBorderVertexPath() {
        return borderVertexPath;
    }

    public Path getLabelPath() {
        return labelPath;
    }

    public VertexDrawType getVertexDrawType() {
        return vertexDrawType;
    }

    public boolean isOnInteractArea(PointF pointF) {
        return PointUtils.dist(Pair.create(pointF,
                new PointF(vertexCenterPoint, vertexCenterPoint))) <= vertexRadius;
    }

    protected boolean isInconsistent() {
        return internVertexPath == null || externVertexPath == null || borderVertexPath == null
                || labelPath == null || vertexCenterPoint <= 0 || vertexRadius <= 0
                || vertexDrawType == null;
    }

    protected void setInternVertexPath(Path internVertexPath) {
        this.internVertexPath = internVertexPath;
    }

    protected void setExternVertexPath(Path externVertexPath) {
        this.externVertexPath = externVertexPath;
    }

    protected void setBorderVertexPath(Path borderVertexPath) {
        this.borderVertexPath = borderVertexPath;
    }

    protected void setLabelPath(Path labelPath) {
        this.labelPath = labelPath;
    }

    protected void setVertexCenterPoint(int vertexCenterPoint) {
        this.vertexCenterPoint = vertexCenterPoint;
    }

    protected void setVertexDrawType(VertexDrawType vertexDrawType) {
        this.vertexDrawType = vertexDrawType;
    }

    protected void setVertexRadius(int vertexRadius) {
        this.vertexRadius = vertexRadius;
    }
}
