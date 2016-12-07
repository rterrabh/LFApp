package com.ufla.lfapp.vo;

import android.graphics.Point;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by carlos on 12/6/16.
 */

public class AutomatonGUI extends Automaton implements Serializable {

    protected Long id;
    protected String label;
    protected Date creationDate;
    protected int contUses = 1;
    protected Map<String, Point> stateGridPositions;

    public AutomatonGUI(Automaton automaton, Map<String, Point> stateGridPositions) {
        super(automaton);
        this.stateGridPositions = (stateGridPositions == null) ? new HashMap<String, Point>()
                : stateGridPositions;
        this.creationDate = new Date();
    }

    public AutomatonGUI(AutomatonGUI automatonGUI) {
        super(automatonGUI);
        if (automatonGUI == null || automatonGUI.stateGridPositions == null) {
            stateGridPositions = new HashMap<>();
        } else {
            stateGridPositions = new HashMap<>(automatonGUI.stateGridPositions);
        }
        this.creationDate = new Date();
    }

    public Map<String, Point> getStateGridPositions() {
        return new HashMap<>(stateGridPositions);
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public int getContUses() {
        return contUses;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContUses(int contUses) {
        this.contUses = contUses;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
