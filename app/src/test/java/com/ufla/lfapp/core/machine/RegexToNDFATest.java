package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.utils.Symbols;

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

public class RegexToNDFATest {

    private FiniteStateAutomaton A;
    private FiniteStateAutomaton B;
    private FiniteStateAutomaton AB;
    private FiniteStateAutomaton A_K;
    private FiniteStateAutomaton AorB;
    private FiniteStateAutomaton AorB_K;

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    public FiniteStateAutomaton getA() {
        String[] statesStr = { "q0", "q1" };
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
                { "q0", "a", "q1" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    public FiniteStateAutomaton getAB() {
        String[] statesStr = { "q0", "q1", "q2" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("q2"));
        String[][] tfStr = {
                { "q0", "a", "q1" },
                { "q1", "b", "q2" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    public FiniteStateAutomaton getB() {
        String[] statesStr = { "q0", "q1" };
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
                { "q0", "b", "q1" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    public FiniteStateAutomaton getA_K() {
        String[] statesStr = { "q0", "q1", "q2", "q3" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("q3"));
        String[][] tfStr = {
                { "q0", Symbols.LAMBDA, "q1" },
                { "q0", Symbols.LAMBDA, "q3" },
                { "q1", "a", "q2" },
                { "q2", Symbols.LAMBDA, "q3" },
                { "q3", Symbols.LAMBDA, "q1" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    public FiniteStateAutomaton getAorB() {
        String[] statesStr = { "q0", "q1", "q2", "q3", "q4", "q5" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("q5"));
        String[][] tfStr = {
                { "q0", Symbols.LAMBDA, "q1" },
                { "q0", Symbols.LAMBDA, "q3" },
                { "q1", "a", "q2" },
                { "q3", "b", "q4" },
                { "q2", Symbols.LAMBDA, "q5" },
                { "q4", Symbols.LAMBDA, "q5" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    public FiniteStateAutomaton getAorB_K() {
        String[] statesStr = { "q0", "q1", "q2", "q3", "q4", "q5", "q6", "q7" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("q7"));
        String[][] tfStr = {
                { "q0", Symbols.LAMBDA, "q1" },
                { "q0", Symbols.LAMBDA, "q7" },
                { "q1", Symbols.LAMBDA, "q2" },
                { "q1", Symbols.LAMBDA, "q4" },
                { "q2", "a", "q3" },
                { "q4", "b", "q5" },
                { "q3", Symbols.LAMBDA, "q6" },
                { "q5", Symbols.LAMBDA, "q6" },
                { "q6", Symbols.LAMBDA, "q7" },
                { "q7", Symbols.LAMBDA, "q1" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    @Before
    public void setUp() {
        A = getA();
        B = getB();
        AB = getAB();
        A_K = getA_K();
        AorB = getAorB();
        AorB_K = getAorB_K();
    }

    @Test
    public void testA() throws Exception {
        FiniteStateAutomatonGUI expected = new FiniteStateAutomatonGUI(A, A.getStatesPointsFake());
        FiniteStateAutomatonGUI res = FiniteStateAutomatonGUI.getAutomatonByRegex("a");
//        System.out.println(expected.toStringTest());
//        System.out.println("\n\n"+ res.toStringTest());
        assertEquals(expected, res);
    }

    @Test
    public void testB() throws Exception {
        FiniteStateAutomatonGUI expected = new FiniteStateAutomatonGUI(B, B.getStatesPointsFake());
        FiniteStateAutomatonGUI res = FiniteStateAutomatonGUI.getAutomatonByRegex("b");
//        System.out.println(expected.toStringTest());
//        System.out.println("\n\n"+ res.toStringTest());
        assertEquals(expected, res);
    }
    @Test
    public void testAB() throws Exception {
        FiniteStateAutomatonGUI expected = new FiniteStateAutomatonGUI(AB, AB.getStatesPointsFake());
        FiniteStateAutomatonGUI res = FiniteStateAutomatonGUI.getAutomatonByRegex("ab");
//        System.out.println(expected.toStringTest());
//        System.out.println("\n\n"+ res.toStringTest());
        assertEquals(expected, res);
    }

    @Test
    public void testAorB() throws Exception {
        FiniteStateAutomatonGUI expected = new FiniteStateAutomatonGUI(AorB, AorB.getStatesPointsFake());
        FiniteStateAutomatonGUI res = FiniteStateAutomatonGUI.getAutomatonByRegex("a|b");
//        System.out.println(expected.toStringTest());
//        System.out.println("\n\n"+ res.toStringTest());
        assertEquals(expected, res);
    }

    @Test
    public void testA_K() throws Exception {
        FiniteStateAutomatonGUI expected = new FiniteStateAutomatonGUI(A_K, A_K.getStatesPointsFake());
        FiniteStateAutomatonGUI res = FiniteStateAutomatonGUI.getAutomatonByRegex("a*");
        assertEquals(expected, res);
    }

    @Test
    public void testAorB_K() throws Exception {
        FiniteStateAutomatonGUI expected = new FiniteStateAutomatonGUI(AorB_K, AorB_K.getStatesPointsFake());
        FiniteStateAutomatonGUI res = FiniteStateAutomatonGUI.getAutomatonByRegex("(a|b)*");
//        System.out.println(expected.toStringTest());
//        System.out.println("\n\n"+ res.toStringTest());
        assertEquals(expected, res);
    }


}
