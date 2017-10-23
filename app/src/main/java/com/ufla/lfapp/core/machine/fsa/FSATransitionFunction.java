package com.ufla.lfapp.core.machine.fsa;

import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.TransitionFunction;
import com.ufla.lfapp.utils.Symbols;

import java.io.Serializable;

public class FSATransitionFunction
        extends TransitionFunction
        implements Serializable {

    protected String symbol;

    public FSATransitionFunction(State currentState, String symbol, State futureState) {
        super(currentState, futureState);
        this.symbol = symbol;
    }

    public String getLabel() {
        return symbol;
    }

    public FSATransitionFunction(FSATransitionFunction FSATransitionFunction) {
        this(new State(FSATransitionFunction.getCurrentState().getName()),
                FSATransitionFunction.getSymbol(),
                new State(FSATransitionFunction.getFutureState().getName()));
    }

    // REFACTOR
    public static String stateWithSpan(String state) {
        return new StringBuilder()
                .append("q<sub><small>")
                .append(state.substring(1))
                .append("</small></sub>")
                .toString();
    }

    public FSATransitionFunction copy() {
        return new FSATransitionFunction(new State(getCurrentState().getName()), getSymbol(),
                new State(getFutureState().getName()));
    }

    // MÉTODOS EQUALS
    public boolean equalsCurrentState(State currentState) {
        return getCurrentState().equals(currentState);
    }

    public boolean equalsSymbol(String symbol) {
        return getSymbol().equals(symbol);
    }

    // MÉTODOS ACESSORES
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public int compareTo(TransitionFunction another) {
        int result = super.compareTo(another);
        if (result != 0) return result;
        FSATransitionFunction t = (FSATransitionFunction) another;
        return symbol.compareTo(t.symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FSATransitionFunction that = (FSATransitionFunction) o;

        return symbol != null ? symbol.equals(that.symbol) : that.symbol == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder("(")
                .append(getCurrentState())
                .append(", ")
                .append(getSymbol())
                .append(") -> (")
                .append(getFutureState())
                .append(")")
                .toString();
    }

}
