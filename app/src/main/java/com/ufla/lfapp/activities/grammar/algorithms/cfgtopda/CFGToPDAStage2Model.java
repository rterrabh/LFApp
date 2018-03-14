package com.ufla.lfapp.activities.grammar.algorithms.cfgtopda;

import com.ufla.lfapp.core.machine.pda.PushdownAutomatonExtend;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.pda.PDAExtTransitionFunction;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 3/8/17.
 */

public class CFGToPDAStage2Model {

    protected PushdownAutomatonExtend pushdownAutomatonExtend;

    public CFGToPDAStage2Model() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        SortedSet<State> states = new TreeSet<>();
        SortedSet<State> finalStates = new TreeSet<>();
        states.add(q0);
        states.add(q1);
        finalStates.add(q1);
        pushdownAutomatonExtend = new PushdownAutomatonExtend(states, q0, finalStates,
                new HashSet<PDAExtTransitionFunction>());
    }

    public PushdownAutomatonExtend getPushdownAutomatonExtended() {
        return pushdownAutomatonExtend;
    }

}
