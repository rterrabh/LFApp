package com.ufla.lfapp.views.graph.edge.interactarea;

import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.ResourcesContext;

/**
 * Created by carlos on 11/21/16.
 * <p>
 * Um builder para facilitar a criação de uma área de interação do tipo InteractQuadrilateralArea.
 */

public class InteractQuadrilateralAreaBuilder {

    private InteractQuadrilateralArea interactQuadrilateralArea;

    public InteractQuadrilateralAreaBuilder() {
        interactQuadrilateralArea = new InteractQuadrilateralArea();
    }

    /**
     * Define os pontos nas circunferências dos vértices que determinam a aresta.
     *
     * @param circumferencePoints pontos nas circunferências dos vértices que determinam a aresta
     * @return próprio builder
     */
    public InteractQuadrilateralAreaBuilder withCircumferencePoints(
            Pair<PointF, PointF> circumferencePoints) {
        interactQuadrilateralArea.setCircumferencePoints(circumferencePoints);
        return this;
    }

    /**
     * Define os dois pontos de controle do quadrilátero, além dos pontos que estão nas
     * circunferências dos vértices.
     *
     * @param controlPoints pontos de controle do quadrilátero, além dos pontos que estão nas
     *                      circunferências dos vértices.
     * @return próprio builder
     */
    public InteractQuadrilateralAreaBuilder withControlPoints(
            Pair<PointF, PointF> controlPoints) {
        interactQuadrilateralArea.setControlPoints(controlPoints);
        return this;
    }

    /**
     * Cria uma instância de InteractQuadrilateralArea com os pontos fornecidos.
     *
     * @return instância de InteractQuadrilateralArea com os pontos fornecidos
     */
    public InteractQuadrilateralArea create() {
        if (interactQuadrilateralArea.isInconsistent()) {
            throw new RuntimeException(ResourcesContext.getString(R.string.exception_inconsistent_interact_quad));
        }
        return interactQuadrilateralArea;
    }
}
