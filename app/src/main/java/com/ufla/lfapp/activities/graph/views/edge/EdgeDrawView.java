package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Path;
import android.graphics.PointF;

import com.ufla.lfapp.activities.graph.views.edge.interactarea.InteractArea;

/**
 * Created by carlos on 11/18/16.
 */

public class EdgeDrawView implements EdgeDraw {

    private Path edge;
    private Path labelPath;
    private Path arrowHead;
    private InteractArea interactArea;

    public EdgeDrawView(Path edge, Path labelPath, Path arrowHead, InteractArea interactArea) {
        this.edge = edge;
        this.labelPath = labelPath;
        this.arrowHead = arrowHead;
        this.interactArea = interactArea;
        if (isInconsistent()) {
            throw new RuntimeException("Instance of EdgeDrawView is inconsistent!");
        }
    }

    protected EdgeDrawView() {

    }

    protected boolean isInconsistent() {
        return edge == null || labelPath == null || arrowHead == null || interactArea == null;
    }

    @Override
    public float distanceFromCircumferences() {
        return interactArea.distanceFromCircumferences();
    }

    @Override
    public float distanceToCircumferenceOfSourceVertex(PointF point) {
        return interactArea.distanceToCircumferenceOfSourceVertex(point);
    }

    @Override
    public Path getEdge() {
        return new Path(edge);
    }

    @Override
    public Path getLabelPath() {
        return new Path(labelPath);
    }

    @Override
    public Path getArrowHead() {
        return new Path(arrowHead);
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
    public boolean isOnInteractArea(PointF pointF) {
        return interactArea.isOnInteractArea(pointF);
    }

    protected void setEdge(Path edge) {
        this.edge = new Path(edge);
    }

    protected void setLabelPath(Path labelPath) {
        this.labelPath = new Path(labelPath);
    }

    protected void setArrowHead(Path arrowHead) {
        this.arrowHead = new Path(arrowHead);
    }

    protected void setInteractArea(InteractArea interactArea) {
        this.interactArea = interactArea.clone();
    }
}
