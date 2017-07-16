package com.ufla.lfapp.core.machine.fsa;

import android.graphics.Point;

import com.ufla.lfapp.core.machine.State;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Responsável por construir um autômato que deve ser exibido de forma gráfica.
 *
 * Created by carlos on 12/12/16.
 */

public class FiniteStateFiniteStateAutomatonGUIBuilder extends FiniteStateAutomatonBuilder {

    protected Long id;
    protected String label;
    protected int contUses = 1;
    protected Date creationDate;
    protected SortedMap<State, Point> stateGridPositions;

    /**
     * Constrói um autômato vazio.
     */
    public FiniteStateFiniteStateAutomatonGUIBuilder() {
        super();
        stateGridPositions = new TreeMap<>();
        id = -1l;
    }

    /**
     * Constrói um autômato com os atributos do autômato passada por parâmetro.
     *
     * @param automatonGUI autômato usada para construir o autômato desse construtor de autômato
     */
    public FiniteStateFiniteStateAutomatonGUIBuilder(FiniteStateAutomatonGUI automatonGUI) {
        super(automatonGUI);
        id = automatonGUI.getId();
        label = automatonGUI.getLabel();
        contUses = automatonGUI.getContUses();
        creationDate = automatonGUI.getCreationDate();
        stateGridPositions = automatonGUI.getStateGridPoint();
    }


    public FiniteStateFiniteStateAutomatonGUIBuilder addOrChangeStatePosition(State state, Point gridPosition) {
        super.addState(state);
        stateGridPositions.put(state, gridPosition);
        return this;
    }

    public FiniteStateFiniteStateAutomatonGUIBuilder removeState(State state) {
        super.removeState(state);
        stateGridPositions.remove(state);
        return this;
    }

    public FiniteStateFiniteStateAutomatonGUIBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public FiniteStateFiniteStateAutomatonGUIBuilder setContUses(int contUses) {
        this.contUses = contUses;
        return this;
    }

    public FiniteStateFiniteStateAutomatonGUIBuilder setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public FiniteStateFiniteStateAutomatonGUIBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * Cria o autômato em construção.
     *
     * @return autômato em construção
     */
    public FiniteStateAutomatonGUI createAutomatonGUI() {
        return new FiniteStateAutomatonGUI(createAutomaton(), stateGridPositions, id, label, contUses,
                creationDate);
    }


}
