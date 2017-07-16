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
                "",
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

    public boolean addTransition(TMMultiTrackTransitionFunction transition) {
        if (transition.hasNumTapes(getNumTracks())) {
            getTransitionFunctions().add(transition);
            return true;
        }
        return false;
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

    public SortedSet<TMMultiTrackTransitionFunction> createTransitions(String currentState, String futureState,
                                                             String label) {
        SortedSet<TMMultiTrackTransitionFunction> transitions = new TreeSet<>();
        State a = getState(currentState);
        State b = getState(futureState);
        String[] tra = label.split("\\n");
        for (int i = 0; i < tra.length; i++) {
            String[] args = tra[i].split("[ /]");
            int n = args.length / 2;
            String readSymbols[] = new String[n];
            String writeSymbols[] = new String[n];
            for (int j = 0; j < n; j++) {
                readSymbols[j] = args[j * 2];
                writeSymbols[j] = args[j * 2 + 1];
            }
            transitions.add(new TMMultiTrackTransitionFunction(a, b, readSymbols,
                    writeSymbols,
                    TMMove.getInstance(args[args.length-1])));
        }
        return transitions;
    }

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
