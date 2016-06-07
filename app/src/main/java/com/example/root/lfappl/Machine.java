package com.example.root.lfappl;

/**
 * Created by root on 17/05/16.
 */
import java.util.Set;
import java.util.HashSet;

public abstract class Machine {

    protected Set<String> states;
    protected Set<String> alphabet;
    protected String initialState;
    protected Set<String> finalStates;


    public Machine() {
        super();
        this.states = new HashSet<String>();
        this.alphabet = new HashSet<String>();
        this.initialState = new String();
        this.finalStates = new HashSet<String>();
    }

    public Machine(Set<String> states, Set<String> alphabet, String initialState, Set<String> finalStates) {
        super();
        this.states = states;
        this.alphabet = alphabet;
        this.initialState = initialState;
        this.finalStates = finalStates;
    }

    public Set<String> getStates() {
        return states;
    }

    public void setStates(Set<String> states) {
        this.states = states;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Set<String> alphabet) {
        this.alphabet = alphabet;
    }

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(Set<String> finalStates) {
        this.finalStates = finalStates;
    }
}