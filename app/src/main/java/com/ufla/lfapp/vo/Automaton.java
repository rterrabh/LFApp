package com.ufla.lfapp.vo;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by carlos on 11/23/16.
 */

public class Automaton extends Machine {

    protected static final String STATE_ERROR = "err";
    private Set<TransitionFunction> transitions;

    public Automaton getCompleteAutomaton() {
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
                    automaton.states.add(STATE_ERROR);
                    automaton.transitions.add(new TransitionFunction(states[i], alphabet[j],
                            STATE_ERROR));
                }
            }
        }
        return automaton;
    }

    public TransitionFunction getTransition(String fromState, String symbol) {
        for (TransitionFunction t : transitions) {
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
}
