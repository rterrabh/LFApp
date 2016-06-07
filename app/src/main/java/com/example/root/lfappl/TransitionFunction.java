package com.example.root.lfappl;

/**
 * Created by root on 17/05/16.
 */
public class TransitionFunction {

    protected String currentState;
    protected String symbol;
    protected String futureState;


    public TransitionFunction() {
        super();
        this.currentState = new String();
        this.symbol = new String();
        this.futureState = new String();
    }

    public TransitionFunction(String currentState, String symbol, String futureState) {
        super();
        this.currentState = currentState;
        this.symbol = symbol;
        this.futureState = futureState;
    }

    public TransitionFunction(TransitionFunction transitionFunction) {
        super();
        this.currentState = transitionFunction.getCurrentState();
        this.symbol = transitionFunction.getSymbol();
        this.futureState = transitionFunction.getFutureState();
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getFutureState() {
        return futureState;
    }

    public void setFutureState(String futureState) {
        this.futureState = futureState;
    }

    @Override
    public String toString() {
        return "(" + this.getCurrentState() + ", " + this.getSymbol() + ") -> (" + this.getFutureState() + ")";
    }
}