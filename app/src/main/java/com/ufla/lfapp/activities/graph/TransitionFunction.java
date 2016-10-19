package com.ufla.lfapp.activities.graph;

import android.util.Pair;

import com.ufla.lfapp.activities.graph.adapters.IEdge;
import com.ufla.lfapp.activities.graph.adapters.IVertex;

/**
 * Created by carlos on 10/3/16.
 */
public class TransitionFunction implements IEdge {

    protected State currentState;
    protected String symbol;
    protected State futureState;


    public TransitionFunction(State currentState, String symbol, State futureState) {
        this.currentState = currentState;
        this.symbol = symbol;
        this.futureState = futureState;
    }

//    public Point getGridPointCurrentState() {
//        return currentState.get
//    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public State getFutureState() {
        return futureState;
    }

    public void setFutureState(State futureState) {
        this.futureState = futureState;
    }

    @Override
    public String getLabel() {
        return symbol;
    }

    @Override
    public void setLabel(String label) {
        this.symbol = label;
    }

    @Override
    public Pair<IVertex, IVertex> getVertices() {
        return new Pair<>((IVertex) currentState, (IVertex) futureState);
    }

}
