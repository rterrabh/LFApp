package com.ufla.lfapp.core.machine.tm;

import android.support.v4.util.Pair;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.Machine;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.TransitionFunction;
import com.ufla.lfapp.utils.ResourcesContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 4/2/17.
 */

public class TuringMachine
        extends Machine
        implements Serializable {

    private Set<TMTransitionFunction> transitionFunction;

    @Override
    public Set<? extends TransitionFunction> getTransitionFunctionsGen() {
        return transitionFunction;
    }

    @Override
    public MachineType getMachineType() {
        return MachineType.TM;
    }

    // CONSTRUCTORS
    public TuringMachine() {
        this(new TreeSet<String>(), "", new TreeSet<String>(), new TreeSet<TMTransitionFunction>());
    }

    public TuringMachine(SortedSet<String> states, String initialState,
                         SortedSet<String> finalStates,
                         Set<TMTransitionFunction> transitionFunction) {
        super(states, initialState, finalStates);
        this.transitionFunction = transitionFunction;
    }

    public TuringMachine(SortedSet<State> states, State initialState,
                         SortedSet<State> finalStates,
                         Set<TMTransitionFunction> transitionFunction) {
        super(states, initialState, finalStates);
        this.transitionFunction = transitionFunction;
    }

    public Map<Pair<State, State>, SortedSet<String>> getTransitionsTM() {
        Map<Pair<State, State>, SortedSet<String>> transitionsPDA = new HashMap<>();
        for (TMTransitionFunction t : transitionFunction) {
            Pair<State, State> states = Pair.create(t.getCurrentState(), t.getFutureState());
            if (!transitionsPDA.containsKey(states)) {
                transitionsPDA.put(states, new TreeSet<String>());
            }
            transitionsPDA
                    .get(states)
                    .add(
                            new StringBuilder(t.getSymbol())
                                    .append('/')
                                    .append(t.getWriteSymbol())
                                    .append(" ")
                                    .append(t.getMove())
                                    .toString()
                    );
        }
        return transitionsPDA;
    }

    public Set<TMTransitionFunction> getTransitions(State state, String read) {
        Set<TMTransitionFunction> transitions = new HashSet<>();
        for (TMTransitionFunction t : transitionFunction) {
            if (t.getCurrentState().equals(state) &&
                    t.getSymbol().equals(read)) {
                transitions.add(t);
            }
        }
        return transitions;
    }


    @Override
    public SortedSet<String> getAlphabet() {
        return null;
    }

    // MÉTODOS ACESSORES

    public Set<TMTransitionFunction> getTransitionFunctions() {
        return transitionFunction;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString())
                .append('\n')
                .append(ResourcesContext.getString(R.string.transitions))
                .append(": \n");
        Set<TMTransitionFunction> transitionFunctions = getTransitionFunctions();
        for (TMTransitionFunction transitionFunction : transitionFunctions) {
            sb.append(transitionFunction)
                    .append('\n');
        }
        return sb.toString();
    }

}
