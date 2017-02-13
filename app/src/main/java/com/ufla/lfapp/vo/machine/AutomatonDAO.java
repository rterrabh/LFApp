package com.ufla.lfapp.vo.machine;

import android.graphics.Point;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by carlos on 12/7/16.
 */

public class AutomatonDAO extends AutomatonGUI implements Serializable {

    private Map<Point, Long> gridPositionsId;
    private Map<State, Long>  statesId;
    private Map<String, Long>  symbolsId;
    private Map<TransitionFunction, Long>  transitionsId;

    public AutomatonDAO(AutomatonGUI automatonGUI) {
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

    public void putTransitionId(TransitionFunction transitionFunction, Long transitionId) {
        transitionsId.put(transitionFunction, transitionId);
    }

    public Long getTransitionId(TransitionFunction transitionFunction) {
        return transitionsId.get(transitionFunction);
    }




}
