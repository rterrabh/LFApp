package com.ufla.lfapp.views.graph.edge.interactarea;

import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.ResourcesContext;

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
            throw new RuntimeException(ResourcesContext.getString(R.string.exception_inconsistent_interact_quad));
        }
        return interactQuadrilateralArea;
    }
}
