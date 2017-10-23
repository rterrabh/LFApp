package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.utils.Symbols;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by terra on 10/07/17.
 */

public class NDFAToDFATest {

    private FiniteStateAutomaton ndfal;
    private FiniteStateAutomaton ndfa;
    private FiniteStateAutomaton dfa;

    static {
        ResourcesContext.isTest = true;
    }

    @Before
    public void setUp() {
        String[] statesStr = { "q0", "q1", "q2" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("q1"));
        String[][] tfStr = {
                { "q0", "a", "q0" },
                { "q0", "a", "q1" },
                { "q0", "a", "q2" },
                { "q2", "c", "q2" },
                { "q2", Symbols.LAMBDA, "q1" },
                { "q1", "b", "q1" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        this.ndfal = new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);


        // NDFA
        String[] statesStrNDFA = { "q0", "q1", "q2" };
        SortedSet<State> statesNDFA = new TreeSet<>();
        Map<String, State> statesMapNDFA = new HashMap<>();
        for (String st : statesStrNDFA) {
            State state = new State(st);
            statesNDFA.add(state);
            statesMapNDFA.put(st, state);
        }
        SortedSet<State> finalStatesNDFA = new TreeSet<>();
        finalStatesNDFA.add(statesMapNDFA.get("q1"));
        finalStatesNDFA.add(statesMapNDFA.get("q2"));
        String[][] tfStrNDFA = {
                { "q0", "a", "q0" },
                { "q0", "a", "q1" },
                { "q0", "a", "q2" },
                { "q2", "c", "q2" },
                { "q2", "c", "q1" },
                { "q2", "b", "q1" },
                { "q1", "b", "q1" }
        };
        SortedSet<FSATransitionFunction> tfNDFA = new TreeSet<>();
        for (String[] tfS : tfStrNDFA) {
            tfNDFA.add(new FSATransitionFunction(statesMapNDFA.get(tfS[0]), tfS[1], statesMapNDFA.get(tfS[2])));
        }
        this.ndfa = new FiniteStateAutomaton(statesNDFA, statesMapNDFA.get("q0"),
                finalStatesNDFA, tfNDFA);

        // DFA
        String[] statesStrDFA = { "<q0>", "<q0,q1,q2>", "<q1,q2>", "<q1>" };
        SortedSet<State> statesDFA = new TreeSet<>();
        Map<String, State> statesMapDFA = new HashMap<>();
        for (String st : statesStrDFA) {
            State state = new State(st);
            statesDFA.add(state);
            statesMapDFA.put(st, state);
        }
        SortedSet<State> finalStatesDFA = new TreeSet<>();
        finalStatesDFA.add(statesMapDFA.get("<q1>"));
        finalStatesDFA.add(statesMapDFA.get("<q1,q2>"));
        finalStatesDFA.add(statesMapDFA.get("<q0,q1,q2>"));
        String[][] tfStrDFA = {
                { "<q0>", "a", "<q0,q1,q2>" },
                { "<q0,q1,q2>", "a", "<q0,q1,q2>" },
                { "<q0,q1,q2>", "b", "<q1>" },
                { "<q0,q1,q2>", "c", "<q1,q2>" },
                { "<q1>", "b", "<q1>" },
                { "<q1,q2>", "c", "<q1,q2>" },
                { "<q1,q2>", "b", "<q1>" }
        };
        SortedSet<FSATransitionFunction> tfDFA = new TreeSet<>();
        for (String[] tfS : tfStrDFA) {
            tfDFA.add(new FSATransitionFunction(statesMapDFA.get(tfS[0]),
                    tfS[1], statesMapDFA.get(tfS[2])));
        }
        this.dfa = new FiniteStateAutomaton(statesDFA, statesMapDFA.get("<q0>"),
                finalStatesDFA, tfDFA);
    }

    @Test
    public void testNDFALambdaToNDFA() throws Exception {
        assertEquals(true, ndfal.AFNDLambdaToAFND().equals(ndfa));
//        System.out.println(tmSimulator.processALL());
//        System.out.println(Arrays.toString(tmSimulator.getTapes()));
//        System.out.println(Arrays.deepToString(tmSimulator.getConfigurations()));
    }

    @Test
    public void testNDFALambdaToDFA() throws Exception {
//        System.out.println(ndfal.AFNDLambdaToAFD().toStringTest());
//        System.out.println("\n\n"+dfa.toStringTest());
        assertEquals(true, ndfal.AFNDLambdaToAFD().equals(dfa));
    }

    @Test
    public void testNDFAToDFA() throws Exception {
        assertEquals(true, ndfa.AFNDLambdaToAFD().equals(dfa));
    }

}
