package com.ufla.lfapp.core.machine;

import android.support.v4.util.Pair;

import java.io.Serializable;

import static com.ufla.lfapp.core.machine.fsa.FSATransitionFunction.stateWithSpan;

/**
 * Created by carlos on 4/7/17.
 */

public abstract class TransitionFunction
        implements Serializable, Comparable<TransitionFunction> {

    protected State currentState;
    protected State futureState;

    @Override
    public int compareTo(TransitionFunction another) {
        if (!(this.getClass().equals(another.getClass()))) {
            return -1;
        }
        int result = currentState.compareTo(another.currentState);
        if (result != 0) return result;
        return futureState.compareTo(another.futureState);
    }

    public TransitionFunction(State currentState, State futureState) {
        this.currentState = currentState;
        this.futureState = futureState;
    }

    public Pair<State, State> getPairState() {
        return Pair.create(currentState, futureState);
    }

    public abstract String getLabel();

    protected boolean isValidInstance() {
        return currentState != null && futureState != null;
    }


    // MÉTODOS ACESSORES
    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public String getCurrentStateLabelWithSpan() {
        return stateWithSpan(currentState.getName());
    }

    public State getFutureState() {
        return futureState;
    }

    public void setFutureState(State futureState) {
        this.futureState = futureState;
    }

    public String getFutureStateLabelWithSpan() {
        return stateWithSpan(futureState.getName());
    }

    // MÉTODOS EQUALS
    public boolean equalsCurrentState(State currentState) {
        return this.currentState.equals(currentState);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransitionFunction that = (TransitionFunction) o;

        if (currentState != null ? !currentState.equals(that.currentState) : that.currentState != null)
            return false;
        return futureState != null ? futureState.equals(that.futureState) : that.futureState == null;

    }

    @Override
    public int hashCode() {
        int result = currentState != null ? currentState.hashCode() : 0;
        result = 31 * result + (futureState != null ? futureState.hashCode() : 0);
        return result;
    }

}
