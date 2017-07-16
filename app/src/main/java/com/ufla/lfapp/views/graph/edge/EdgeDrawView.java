package com.ufla.lfapp.views.graph.edge;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.views.graph.edge.interactarea.InteractArea;

/**
 * Created by carlos on 11/18/16.
 */

public class EdgeDrawView implements EdgeDraw {

    private Path edge;
    private Path labelPath;
    private Path arrowHead;
    private InteractArea interactArea;
    private Paint.Align paintAlign;
    private EdgeDrawType edgeDrawType;
    private Pair<PointF, PointF> labelLine;

    public EdgeDrawView(Path edge, Path labelPath, Path arrowHead, InteractArea interactArea,
                        Paint.Align paintAlign, EdgeDrawType edgeDrawType,
                        Pair<PointF, PointF> labelLine) {
        this.edge = edge;
        this.labelPath = labelPath;
        this.arrowHead = arrowHead;
        this.interactArea = interactArea;
        this.paintAlign = paintAlign;
        this.edgeDrawType = edgeDrawType;
        this.labelLine = labelLine;
        if (isInconsistent()) {
            throw new RuntimeException(ResourcesContext.getString(R.string.exception_edge_draw_inconsistent));
        }
    }

    protected EdgeDrawView() {

    }

    protected boolean isInconsistent() {
        return edge == null || labelPath == null || arrowHead == null || interactArea == null
                || paintAlign == null || edgeDrawType == null || labelLine == null;
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

    @Override
    public Paint.Align getPaintAlign() {
        return paintAlign;
    }

    @Override
    public EdgeDrawType getEdgeDrawType() {
        return edgeDrawType;
    }

    @Override
    public Pair<PointF, PointF> getLabelLine() {
        return labelLine;
    }

    protected void setEdgeDrawType(EdgeDrawType edgeDrawType) {
        this.edgeDrawType = edgeDrawType;
    }

    protected void setPaintAlign(Paint.Align paintAlign) {
        this.paintAlign = paintAlign;
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

    protected void setLabelLine(Pair<PointF, PointF> labelLine) {
        this.labelLine = Pair.create(labelLine.first, labelLine.second);
    }
}
