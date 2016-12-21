package com.ufla.lfapp.vo.machine;

import android.graphics.Point;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsável por construir um autômato que deve ser exibido de forma gráfica.
 *
 * Created by carlos on 12/12/16.
 */

public class AutomatonGUIBuilder extends AutomatonBuilder {

    protected Long id;
    protected String label;
    protected int contUses = 1;
    protected Date creationDate;
    protected Map<String, Point> stateGridPositions;

    /**
     * Constrói um autômato vazio.
     */
    public AutomatonGUIBuilder() {
        super();
        stateGridPositions = new HashMap<>();
        id = -1l;
    }

    /**
     * Constrói um autômato com os atributos do autômato passada por parâmetro.
     *
     * @param automatonGUI autômato usada para construir o autômato desse construtor de autômato
     */
    public AutomatonGUIBuilder(AutomatonGUI automatonGUI) {
        super(automatonGUI);
        id = automatonGUI.getId();
        label = automatonGUI.getLabel();
        contUses = automatonGUI.getContUses();
        creationDate = automatonGUI.getCreationDate();
        stateGridPositions = automatonGUI.getStateGridPositions();
    }


    public AutomatonGUIBuilder addOrChangeStatePosition(String state, Point gridPosition) {
        super.addState(state);
        stateGridPositions.put(state, gridPosition);
        return this;
    }

    public AutomatonGUIBuilder removeState(String state) {
        super.removeState(state);
        stateGridPositions.remove(state);
        return this;
    }

    public AutomatonGUIBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public AutomatonGUIBuilder setContUses(int contUses) {
        this.contUses = contUses;
        return this;
    }

    public AutomatonGUIBuilder setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public AutomatonGUIBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * Cria o autômato em construção.
     *
     * @return autômato em construção
     */
    public AutomatonGUI createAutomatonGUI() {
        return new AutomatonGUI(createAutomaton(), stateGridPositions, id, label, contUses,
                creationDate);
    }


}
