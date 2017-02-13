package com.ufla.lfapp.activities.automata.graph.views.edge.interactarea;

import android.graphics.PointF;
import android.support.v4.util.Pair;

/**
 * Created by carlos on 11/21/16.
 */

public class InteractQuadrilateralAreaBuilder {

    private InteractQuadrilateralArea interactQuadrilateralArea;

    public InteractQuadrilateralAreaBuilder() {
        interactQuadrilateralArea = new InteractQuadrilateralArea();
    }

    public InteractQuadrilateralAreaBuilder withCircumferencePoints(
            Pair<PointF, PointF> circumferencePoints) {
        interactQuadrilateralArea.setCircumferencePoints(circumferencePoints);
        return this;
    }

    public InteractQuadrilateralAreaBuilder withControlPoints(
            Pair<PointF, PointF> controlPoints) {
        interactQuadrilateralArea.setControlPoints(controlPoints);
        return this;
    }

    public InteractQuadrilateralArea create() {
        if (interactQuadrilateralArea.isInconsistent()) {
            throw new RuntimeException("Instance of InteractQuadrilateralArea is inconsistent!");
        }
        return interactQuadrilateralArea;
    }
}
