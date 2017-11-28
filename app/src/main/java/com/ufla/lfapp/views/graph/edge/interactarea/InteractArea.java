package com.ufla.lfapp.views.graph.edge.interactarea;

import android.graphics.Path;
import android.graphics.PointF;

/**
 * Created by carlos on 11/21/16.
 * <p>
 * Representa uma área da tela onde o usuário pode interagir com um objeto.
 */

public interface InteractArea extends Cloneable {

    /**
     * Verifica se um determinado ponto está na área de interação do objeto.
     *
     * @param point ponto a ser verificado
     * @return true, se o ponto está dentro da área de interação do objeto, caso contrário, false
     */
    boolean isOnInteractArea(PointF point);

    /**
     * Verifica se um determinado ponto está na área de interação do label do objeto.
     *
     * @param point ponto a ser verificado
     * @return true, se o ponto está dentro da área de interação do label do objeto, caso
     * contrário, false
     */
    boolean isOnInteractLabelArea(PointF point);

    /**
     * Calcula a distância de um determinado ponto até o ponto mais próximo do objeto.
     *
     * @param point ponto referência para o cálculo da distância
     * @return distância de um determinado ponto até o ponto mais próximo do objeto
     */
    float distanceToObject(PointF point);

    /**
     * Recupera um caminho que contém a aŕea de interação do objeto.
     *
     * @return caminho que contém a aŕea de interação do objeto.
     */
    Path getInteractArea();

    /**
     * Cria um clone desta área de interação.
     *
     * @return clone desta área de interação.
     */
    InteractArea clone();

    /**
     * Calcula a distância de um determinado ponto até a circunferência do vértice inicial de uma
     * aresta.
     *
     * @param point ponto a ser calculado a distância
     * @return distância de um determinado ponto até a circunferência do vértice inicial de uma
     * aresta
     */
    float distanceToCircumferenceOfSourceVertex(PointF point);

    /**
     * Calcula a distância entre as circunferências dos vértices inicial e final da aresta.
     *
     * @return distância entre as circunferências dos vértices inicial e final da aresta
     */
    float distanceFromCircumferences();

    /**
     * Recupera o ponto do objeto mais próximo do último ponto que em que aconteceu uma interação
     * com o objeto.
     *
     * @return ponto do objeto mais próximo do último ponto que em que aconteceu uma interação
     * com o objeto.
     */
    PointF getNearestPoint();

}
