package com.ufla.lfapp.core.machine.fsa;

import android.graphics.Point;

import com.ufla.lfapp.core.machine.State;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by carlos on 12/7/16.
 */

public class FiniteStateAutomatonDAO
        extends FiniteStateAutomatonGUI
        implements Serializable {

    private Map<Point, Long> gridPositionsId;
    private Map<State, Long>  statesId;
    private Map<String, Long>  symbolsId;
    private Map<FSATransitionFunction, Long>  transitionsId;

    public FiniteStateAutomatonDAO(FiniteStateAutomatonGUI automatonGUI) {
        super(automatonGUI);
        gridPositionsId = new HashMap<>();
        statesId = new HashMap<>();
        symbolsId = new HashMap<>();
        transitionsId = new HashMap<>();
    }

    public Collection<Long> getTransitionsId() {
        return new HashSet<>(transitionsId.values());
    }

    public void putGridPositionId(Point point, Long pointId) {
        gridPositionsId.put(point, pointId);
    }

    public Long getGridPositionId(Point point) {
        return gridPositionsId.get(point);
    }

    public void putStateId(State state, Long stateId) {
        statesId.put(state, stateId);
    }

    public Long getStateId(State state) {
        return statesId.get(state);
    }

    public void putSymbolId(String symbol, Long symbolId) {
        symbolsId.put(symbol, symbolId);
    }

    public Long getSymbolId(String symbol) {
        return symbolsId.get(symbol);
    }

    public void putTransitionId(FSATransitionFunction FSATransitionFunction, Long transitionId) {
        transitionsId.put(FSATransitionFunction, transitionId);
    }

    public Long getTransitionId(FSATransitionFunction FSATransitionFunction) {
        return transitionsId.get(FSATransitionFunction);
    }




}
