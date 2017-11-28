package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by terra on 10/07/17.
 */

public class DFACompleteTest {

    private FiniteStateAutomaton dfa;
    private FiniteStateAutomaton completeDfa;

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    @Before
    public void setUp() {
        String[] statesStr = { "q0", "q1", "q2", "q3", "q4", "q5", "q6" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("q4"));
        finalStates.add(statesMap.get("q5"));
        finalStates.add(statesMap.get("q6"));
        String[][] tfStr = {
                { "q0", "b", "q1" },
                { "q1", "b", "q2" },
                { "q1", "a", "q5" },
                { "q2", "a", "q6" },
                { "q3", "b", "q3" },
                { "q3", "a", "q3" },
                { "q4", "a", "q4" },
                { "q5", "b", "q5" },
                { "q6", "b", "q6" },
                { "q6", "a", "q6" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        this.dfa = new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);


        // MIN DFA
        String[] statesStrMin = { "q0", "q1", "q2", "q3", "q4", "q5", "q6", "err" };
        SortedSet<State> statesMin = new TreeSet<>();
        Map<String, State> statesMapMin = new HashMap<>();
        for (String st : statesStrMin) {
            State state = new State(st);
            statesMin.add(state);
            statesMapMin.put(st, state);
        }
        SortedSet<State> finalStatesMin = new TreeSet<>();
        finalStatesMin.add(statesMap.get("q4"));
        finalStatesMin.add(statesMap.get("q5"));
        finalStatesMin.add(statesMap.get("q6"));
        String[][] tfStrMin = {
                { "q0", "b", "q1" },
                { "q0", "a", "err" },
                { "q1", "b", "q2" },
                { "q1", "a", "q5" },
                { "q2", "b", "err" },
                { "q2", "a", "q6" },
                { "q3", "b", "q3" },
                { "q3", "a", "q3" },
                { "q4", "b", "err" },
                { "q4", "a", "q4" },
                { "q5", "b", "q5" },
                { "q5", "a", "err" },
                { "q6", "b", "q6" },
                { "q6", "a", "q6" },
                { "err", "b", "err" },
                { "err", "a", "err" },

        };
        SortedSet<FSATransitionFunction> tfMin = new TreeSet<>();
        for (String[] tfS : tfStrMin) {
            tfMin.add(new FSATransitionFunction(statesMapMin.get(tfS[0]), tfS[1], statesMapMin.get(tfS[2])));
        }
        this.completeDfa = new FiniteStateAutomaton(statesMin, statesMapMin.get("q0"),
                finalStatesMin, tfMin);
    }

    @Test
    public void testCompleteDFA() throws Exception {
        assertEquals(completeDfa, dfa.getCompleteAutomaton());
//        System.out.println(tmSimulator.processALL());
//        System.out.println(Arrays.toString(tmSimulator.getTapes()));
//        System.out.println(Arrays.deepToString(tmSimulator.getConfigurations()));
    }

}
