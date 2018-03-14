package com.ufla.lfapp.core.machine.tm.var;

import android.support.v4.util.Pair;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.Machine;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.TransitionFunction;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.utils.ResourcesContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 4/7/17.
 */

public class TuringMachineMultiTape
        extends Machine
        implements Serializable {

    private int numTapes;
    private Set<TMMultiTapeTransitionFunction> transitionFunction;

    // CONSTRUCTORS

    public TuringMachineMultiTape(int numTapes) {
        this(new TreeSet<String>(),
                null,
                new TreeSet<String>(),
                new TreeSet<TMMultiTapeTransitionFunction>(),
                numTapes);
    }

    public TuringMachineMultiTape(SortedSet<String> states,
                                  String initialState,
                                  SortedSet<String> finalStates,
                                  int numTapes) {
        this(states, initialState, finalStates,
                new TreeSet<TMMultiTapeTransitionFunction>(),
                numTapes);
    }

    public TuringMachineMultiTape(SortedSet<String> states,
                                  String initialState,
                                  SortedSet<String> finalStates,
                                  Set<TMMultiTapeTransitionFunction> transitionFunction,
                                  int numTapes) {
        super(states, initialState, finalStates);
        setNumTapes(numTapes);
        setTransitionFunction(transitionFunction);
    }

    public TuringMachineMultiTape(SortedSet<State> states,
                                  State initialState,
                                  SortedSet<State> finalStates,
                                  int numTapes) {
        this(states, initialState, finalStates,
                new TreeSet<TMMultiTapeTransitionFunction>(),
                numTapes);
    }

    public TuringMachineMultiTape(SortedSet<State> states,
                                  State initialState,
                                  SortedSet<State> finalStates,
                                  Set<TMMultiTapeTransitionFunction> transitionFunction,
                                  int numTapes) {
        super(states, initialState, finalStates);
        setNumTapes(numTapes);
        setTransitionFunction(transitionFunction);
    }

    public TuringMachineMultiTape() {
        this(2);
    }

    public Map<Pair<State, State>, SortedSet<String>> getTransitionsTMMultiTape() {
        Set<TMMultiTapeTransitionFunction> transitionFunction = getTransitionFunctions();
        Map<Pair<State, State>, SortedSet<String>> transitionsTMMultiTape = new HashMap<>();
        for (TMMultiTapeTransitionFunction t : transitionFunction) {
            Pair<State, State> states = Pair.create(t.getCurrentState(), t.getFutureState());
            if (!transitionsTMMultiTape.containsKey(states)) {
                transitionsTMMultiTape.put(states, new TreeSet<String>());
            }
            transitionsTMMultiTape
                    .get(states)
                    .add(
                            t.getLabel()
                    );
        }
        return transitionsTMMultiTape;
    }

    public Set<TMMultiTapeTransitionFunction> getTransitions(State state, String[] read) {
        Set<TMMultiTapeTransitionFunction> transitionFunctions = getTransitionFunctions();
        Set<TMMultiTapeTransitionFunction> transitions = new HashSet<>();
        for (TMMultiTapeTransitionFunction t : transitionFunctions) {
            if (t.equalsCurrentState(state)
                    && t.equalsReadSymbols(read)) {
                transitions.add(t);
            }
        }
        return transitions;
    }

    private String[] clean(String[] str) {
        List<String> list = new ArrayList<>();
        for (String st : str) {
            if (!st.isEmpty()) {
                list.add(st);
            }
        }
        return list.toArray(new String[0]);
    }

    // MÉTODOS ACESSORES

    public int getNumTapes() {
        return numTapes;
    }

    private void setNumTapes(int numTapes) {
        this.numTapes = numTapes;
    }

    @Override
    public Set<? extends TransitionFunction> getTransitionFunctionsGen() {
        return transitionFunction;
    }

    @Override
    public MachineType getMachineType() {
        return MachineType.TM_MULTI_TAPE;
    }

    public Set<TMMultiTapeTransitionFunction> getTransitionFunctions() {
        return transitionFunction;
    }

    public void setTransitionFunction(Set<TMMultiTapeTransitionFunction> transitionFunction) {
        this.transitionFunction = transitionFunction;
    }

    @Override
    public SortedSet<String> getAlphabet() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString())
                .append('\n')
                .append(ResourcesContext.getString(R.string.transitions))
                .append(": \n");
        Set<TMMultiTapeTransitionFunction> transitionFunctions = getTransitionFunctions();
        for (TMMultiTapeTransitionFunction transitionFunction : transitionFunctions) {
            sb.append(transitionFunction)
                    .append('\n');
        }
        return sb.toString();
    }

}
