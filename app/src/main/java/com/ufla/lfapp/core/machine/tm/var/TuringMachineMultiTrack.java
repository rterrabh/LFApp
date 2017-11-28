package com.ufla.lfapp.core.machine.tm.var;

import android.support.v4.util.Pair;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.Machine;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.TransitionFunction;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.core.machine.tm.TMTransitionFunction;
import com.ufla.lfapp.utils.ResourcesContext;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 4/7/17.
 */

public class TuringMachineMultiTrack
        extends Machine
        implements Serializable {

    private int numTracks;
    private Set<TMMultiTrackTransitionFunction> transitionFunction;

    // CONSTRUCTORS

    public TuringMachineMultiTrack(int numTracks) {
        this(new TreeSet<String>(),
                null,
                new TreeSet<String>(),
                new TreeSet<TMMultiTrackTransitionFunction>(),
                numTracks);
    }

    public TuringMachineMultiTrack(SortedSet<String> states,
                                   String initialState,
                                   SortedSet<String> finalStates,
                                   int numTracks) {
        this(states, initialState, finalStates,
                new TreeSet<TMMultiTrackTransitionFunction>(),
                numTracks);
    }

    public TuringMachineMultiTrack(SortedSet<String> states,
                                   String initialState,
                                   SortedSet<String> finalStates,
                                   Set<TMMultiTrackTransitionFunction> transitionFunction,
                                   int numTracks) {
        super(states, initialState, finalStates);
        setNumTracks(numTracks);
        setTransitionFunction(transitionFunction);
    }

    public TuringMachineMultiTrack(SortedSet<State> states,
                                   State initialState,
                                   SortedSet<State> finalStates,
                                   int numTracks) {
        this(states, initialState, finalStates,
                new TreeSet<TMMultiTrackTransitionFunction>(), numTracks);
    }

    public TuringMachineMultiTrack(SortedSet<State> states,
                                   State initialState,
                                   SortedSet<State> finalStates,
                                   Set<TMMultiTrackTransitionFunction> transitionFunction,
                                   int numTracks) {
        super(states, initialState, finalStates);
        setNumTracks(numTracks);
        setTransitionFunction(transitionFunction);
    }

    public TuringMachineMultiTrack() {
        this(2);
    }

    public Map<Pair<State, State>, SortedSet<String>> getTransitionsTMMultiTrack() {
        Set<TMMultiTrackTransitionFunction> transitionFunction = getTransitionFunctions();
        Map<Pair<State, State>, SortedSet<String>> transitionsTMMultiTape = new HashMap<>();
        for (TMMultiTrackTransitionFunction t : transitionFunction) {
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

    public Set<TMMultiTrackTransitionFunction> getTransitions(State state, String[] read) {
        Set<TMMultiTrackTransitionFunction> transitionFunctions = getTransitionFunctions();
        Set<TMMultiTrackTransitionFunction> transitions = new HashSet<>();
        for (TMMultiTrackTransitionFunction t : transitionFunctions) {
            if (t.equalsCurrentState(state)
                    && t.equalsReadSymbols(read)) {
                transitions.add(t);
            }
        }
        return transitions;
    }

    // MÉTODOS ACESSORES

    public int getNumTracks() {
        return numTracks;
    }

    private void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
    }

    @Override
    public Set<? extends TransitionFunction> getTransitionFunctionsGen() {
        return transitionFunction;
    }

    @Override
    public MachineType getMachineType() {
        return MachineType.TM_MULTI_TRACK;
    }

    public Set<TMMultiTrackTransitionFunction> getTransitionFunctions() {
        return transitionFunction;
    }

    public void setTransitionFunction(Set<TMMultiTrackTransitionFunction> transitionFunction) {
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
        Set<TMMultiTrackTransitionFunction> transitionFunctions = getTransitionFunctions();
        for (TMMultiTrackTransitionFunction transitionFunction : transitionFunctions) {
            sb.append(transitionFunction)
                    .append('\n');
        }
        return sb.toString();
    }

}
