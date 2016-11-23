package com.ufla.lfapp.vo;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by carlos on 11/23/16.
 */

public class Automaton extends Machine {

    public static final String STATE_ERROR = "err";
    private Set<TransitionFunction> transitionFunctions;
    private String stateError;

    public Automaton(Set<String> states, Set<String> alphabet,
                   String initialState, Set<String> finalStates,
                     Set<TransitionFunction> transitionFunctions) {
        super(states, alphabet, initialState, finalStates);
        this.transitionFunctions = transitionFunctions;
    }

    protected Automaton() {

    }

    public String getStateError() {
        return stateError;
    }

    public Set<TransitionFunction> getTransitionFunctionsToCompleteAutomaton() {
        if (states.contains(STATE_ERROR)) {
            int cont = 0;
            while (states.contains(STATE_ERROR + cont)) {
                cont++;
            }
            stateError = STATE_ERROR + cont;
        } else {
            stateError = STATE_ERROR;
        }
        String states[] = this.states.toArray(new String[this.states.size()]);
        String alphabet[] = this.alphabet.toArray(new String[this.alphabet.size()]);
        Set<TransitionFunction> transitionFunctionsToComplAuto = new HashSet<>();
        for (int i = 0; i < states.length; i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (getTransition(states[i], alphabet[j]) == null) {
                    transitionFunctionsToComplAuto.add(new TransitionFunction(states[i], alphabet[j],
                            stateError));
                }
            }
        }
        if (!transitionFunctionsToComplAuto.isEmpty()) {
            for (int j = 0; j < alphabet.length; j++) {
                transitionFunctionsToComplAuto.add(new TransitionFunction(stateError, alphabet[j],
                        stateError));
            }
        }
        return transitionFunctionsToComplAuto;
    }

    public Automaton getCompleteAutomaton() {
        if (states.contains(STATE_ERROR)) {
            int cont = 0;
            while (states.contains(STATE_ERROR + cont)) {
                cont++;
            }
            stateError = STATE_ERROR + cont;
        } else {
            stateError = STATE_ERROR;
        }
        Automaton automaton = new Automaton();
        automaton.alphabet = new HashSet<>(alphabet);
        automaton.initialState = initialState;
        automaton.states = new HashSet<>(states);
        automaton.finalStates = new HashSet<>(finalStates);
        String states[] = this.states.toArray(new String[this.states.size()]);
        String alphabet[] = this.alphabet.toArray(new String[this.alphabet.size()]);
        for (int i = 0; i < states.length; i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (getTransition(states[i], alphabet[j]) == null) {
                    automaton.states.add(stateError);
                    automaton.transitionFunctions.add(new TransitionFunction(states[i], alphabet[j],
                            STATE_ERROR));
                }
            }
        }
        return automaton;
    }

    public TransitionFunction getTransition(String fromState, String symbol) {
        for (TransitionFunction t : transitionFunctions) {
            if (t.currentState.equals(fromState) && t.symbol.equals(symbol)) {
                return t;
            }
        }
        return null;
    }

    public TransitionFunction[][] getTransitionTable() {
        TransitionFunction[][] transitionTable =
                new TransitionFunction[states.size()][alphabet.size()];
        String states[] = this.states.toArray(new String[this.states.size()]);
        String alphabet[] = this.alphabet.toArray(new String[this.alphabet.size()]);
        for (int i = 0; i < states.length; i++) {
                for (int j = 0; j < alphabet.length; j++) {
                    transitionTable[i][j] = getTransition(states[i], alphabet[j]);
                }
        }
        return transitionTable;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\nTransições: \n");
        for (TransitionFunction transitionFunction : transitionFunctions) {
            sb.append(transitionFunctions.toString())
                    .append('\n');
        }
        return sb.toString();
    }
}
