package com.ufla.lfapp.vo.machine;

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
    protected Map<String, MyPoint> stateGridPositions;


    private static Map<String, MyPoint> convertToMyPoint(Map<String, Point> stateGridPositions) {
        Map<String, MyPoint> stateGridPositionsMyPoint = new HashMap<>();
        for (Map.Entry<String, Point> entry : stateGridPositions.entrySet()) {
            stateGridPositionsMyPoint.put(entry.getKey(), MyPoint.convertPoint(entry.getValue()));
        }
        return stateGridPositionsMyPoint;
    }

    private  static Map<String, Point> convertToPoint(Map<String, MyPoint> stateGridPositions) {
        Map<String, Point> stateGridPositionsPoint = new HashMap<>();
        for (Map.Entry<String, MyPoint> entry : stateGridPositions.entrySet()) {
            stateGridPositionsPoint.put(entry.getKey(), entry.getValue().toPoint());
        }
        return stateGridPositionsPoint;
    }

    public AutomatonGUI(Automaton automaton, Map<String, Point> stateGridPositions) {
        super(automaton);
        this.stateGridPositions = (stateGridPositions == null) ? new HashMap<String, MyPoint>()
                : AutomatonGUI.convertToMyPoint(stateGridPositions);
        this.creationDate = new Date();
        id = -1l;
    }

    public AutomatonGUI(Automaton automaton, Map<String, Point> stateGridPositions, long id,
                        String label, int contUses) {
        super(automaton);
        this.stateGridPositions = (stateGridPositions == null) ? new HashMap<String, MyPoint>()
                : AutomatonGUI.convertToMyPoint(stateGridPositions);;
        this.id = id;
        this.label = label;
        this.contUses = contUses;
        this.creationDate = new Date();
    }

    public AutomatonGUI(Automaton automaton, Map<String, Point> stateGridPositions, long id,
                        String label, int contUses, Date creationDate) {
        super(automaton);
        this.stateGridPositions = (stateGridPositions == null) ? new HashMap<String, MyPoint>()
                : AutomatonGUI.convertToMyPoint(stateGridPositions);;
        this.id = id;
        this.label = label;
        this.contUses = contUses;
        this.creationDate = (creationDate == null) ? new Date() : creationDate;
    }

    public AutomatonGUI(AutomatonGUI automatonGUI) {
        super(automatonGUI);
        stateGridPositions = new HashMap<>(automatonGUI.stateGridPositions);
        id = automatonGUI.id;
        label = automatonGUI.label;
        contUses = automatonGUI.contUses;
        creationDate = new Date();
    }

    public Point getGridPosition(String state) {
        return stateGridPositions.get(state).toPoint();
    }

    public Map<String, Point> getStateGridPositions() {
        return AutomatonGUI.convertToPoint(stateGridPositions);
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
