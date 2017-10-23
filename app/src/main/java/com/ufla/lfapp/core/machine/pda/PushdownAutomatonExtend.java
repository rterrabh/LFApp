package com.ufla.lfapp.core.machine.pda;

import android.support.v4.util.Pair;

import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.TransitionFunction;
import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.core.machine.Machine;
import com.ufla.lfapp.core.machine.State;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 3/2/17.
 */

public class PushdownAutomatonExtend extends Machine implements Serializable {

    private Set<PDAExtTransitionFunction> transitionFunction;

    @Override
    public Set<? extends TransitionFunction> getTransitionFunctionsGen() {
        return transitionFunction;
    }

    @Override
    public MachineType getMachineType() {
        return MachineType.PDA_EXT;
    }

    public PushdownAutomatonExtend() {
        this(new TreeSet<State>(), null, new TreeSet<State>(),
                new TreeSet<PDAExtTransitionFunction>());
    }

    public PushdownAutomatonExtend(PushdownAutomaton pda) {
        this(new TreeSet<State>(), null, new TreeSet<State>(),
                new TreeSet<PDAExtTransitionFunction>());
        defineAutomaton(pda);
    }

    private void defineStates(PushdownAutomaton pda) {
        SortedSet<State> statesPDA = pda.getStates();
        for (State state : statesPDA) {
            states.add(new State(state.getName()));
        }
        SortedSet<State> finalStatesPDA = pda.getFinalStates();
        for (State state : finalStatesPDA) {
            finalStates.add(getState(state.getName()));
        }
        initialState = getState(pda.getInitialState().getName());
    }

    private void defineTransitions(PushdownAutomaton pda) {
        Set<PDATransitionFunction> PDATransitionFunctions = pda.getTransitionFunctions();
        for (PDATransitionFunction tFPDA : PDATransitionFunctions) {
            transitionFunction.add(new PDAExtTransitionFunction(tFPDA, this));
        }
    }


    private void defineAutomaton(PushdownAutomaton pda) {
        defineStates(pda);
        defineTransitions(pda);
    }


    public PushdownAutomatonExtend(SortedSet<State> states, State initialState,
                                   SortedSet<State> finalStates,
                                   Set<PDAExtTransitionFunction> transitionFunction) {
        super(states, initialState, finalStates);
        this.transitionFunction = transitionFunction;
    }

    public Set<PDAExtTransitionFunction> getTransitionFunctions() {
        return transitionFunction;
    }

    @Override
    public SortedSet<String> getAlphabet() {
        SortedSet<String> alphabet = new TreeSet<>();
        for (PDAExtTransitionFunction tFPAExt : transitionFunction) {
            alphabet.add(tFPAExt.getSymbol());
        }
        return alphabet;
    }

    public Map<Pair<State, State>, SortedSet<String>> getTransitionsPDA() {
        Map<Pair<State, State>, SortedSet<String>> transitionsPDA = new HashMap<>();
        for (PDAExtTransitionFunction t : transitionFunction) {
            Pair<State, State> states = Pair.create(t.getCurrentState(), t.getFutureState());
            if (!transitionsPDA.containsKey(states)) {
                transitionsPDA.put(states, new TreeSet<String>());
            }
            transitionsPDA.get(states).add(t.getSymbol() + " " + t.getPops() + "/" +
                    t.stackingToString());
        }
        return transitionsPDA;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TransitionFunctions = ").append(Arrays.toString(transitionFunction.toArray()));
        return sb.toString();
    }
}
