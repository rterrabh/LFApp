package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by carlos on 01/08/17.
 */

public class FiniteAutomataSudkampTest {

    static {
        ResourcesContext.isTest = true;
        TMMove.test = true;
    }

    private FiniteStateAutomaton getAutomatonFigure_5_2() {
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
                { "q0", "a", "q1" },
                { "q0", "b", "q0" },
                { "q1", "a", "q1" },
                { "q1", "b", "q0" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    // pag 149 FEATURE - Machine - Simulator FSA
    @Test
    public void table_5_2Test() throws Exception {
        FiniteStateAutomaton fsa = getAutomatonFigure_5_2();
        assertTrue(fsa.processEntry("aba"));
    }

    private FiniteStateAutomaton getAutomatonExample_5_2_1() {
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
                { "q0", "a", "q0" },
                { "q0", "b", "q1" },
                { "q1", "a", "q0" },
                { "q1", "b", "q2" },
                { "q2", "a", "q2" },
                { "q2", "b", "q2" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    // pag 149 FEATURE - Machine - Simulator FSA
    @Test
    public void example_5_2_1Test() throws Exception {
        FiniteStateAutomaton fsa = getAutomatonExample_5_2_1();
        assertTrue(fsa.processEntry("abba"));
        assertFalse(fsa.processEntry("abab"));
        assertTrue(fsa.processEntry("ababb"));
    }

    private FiniteStateAutomaton getAutomatonExample_5_2_2() {
        String[] statesStr = { "0", "5", "10", "15", "20", "25", "30" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("0"));
        String[][] tfStr = {
                { "0", "n", "0" },
                { "0", "d", "0" },
                { "0", "q", "0" },
                { "5", "n", "0" },
                { "5", "d", "0" },
                { "5", "q", "0" },
                { "10", "n", "5" },
                { "10", "d", "0" },
                { "10", "q", "0" },
                { "15", "n", "10" },
                { "15", "d", "5" },
                { "15", "q", "0" },
                { "20", "n", "15" },
                { "20", "d", "10" },
                { "20", "q", "0" },
                { "25", "n", "20" },
                { "25", "d", "15" },
                { "25", "q", "0" },
                { "30", "n", "25" },
                { "30", "d", "20" },
                { "30", "q", "5" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("30"), finalStates, tf);
    }

    // pag 150 FEATURE - Machine - Simulator FSA
    @Test
    public void example_5_2_2Test() throws Exception {
        FiniteStateAutomaton fsa = getAutomatonExample_5_2_2();
        assertTrue(fsa.processEntry("dndn"));
        assertFalse(fsa.processEntry("nndn"));
    }

    private FiniteStateAutomaton getAutomatonExample_5_3_9() {
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
                { "q0", "c", "q2" },
                { "q1", "b", "q0" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    private FiniteStateAutomaton getAutomatonExample_5_3_9Complete() {
        String[] statesStr = { "q0", "q1", "q2", "err" };
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
                { "q0", "b", "err" },
                { "q0", "c", "q2" },
                { "q1", "a", "err" },
                { "q1", "b", "q0" },
                { "q1", "c", "err" },
                { "q2", "a", "err" },
                { "q2", "b", "err" },
                { "q2", "c", "err" },
                { "err", "a", "err" },
                { "err", "b", "err" },
                { "err", "c", "err" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    // pag 158 FEATURE - Machine - Complete DFA
    @Test
    public void example_5_3_9Test() throws Exception {
        FiniteStateAutomaton dfa = getAutomatonExample_5_3_9();
        FiniteStateAutomaton expectedDFA = getAutomatonExample_5_3_9Complete();
        //System.out.println(expectedDFA);
        FiniteStateAutomaton resultDFA = dfa.getCompleteAutomaton();
        assertEquals(expectedDFA, resultDFA);
    }

    private FiniteStateAutomaton getAutomatonExample_5_4_1() {
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
                { "q0", "a", "q0" },
                { "q0", "b", "q0" },
                { "q0", "b", "q1" },
                { "q1", "b", "q2" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }


    // pag 164 FEATURE - Machine - NFA Simulator
    @Test
    public void example_5_4_1Test() throws Exception {
        FiniteStateAutomaton nfa = getAutomatonExample_5_4_1();
        assertTrue(nfa.processEntry("ababb"));
    }

    private FiniteStateAutomaton getAutomatonExample_5_5_1() {
        String[] statesStr = { "q0", "q1-0", "q1-1", "q1-2", "q2-0", "q2-1" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("q1-2"));
        finalStates.add(statesMap.get("q2-0"));
        finalStates.add(statesMap.get("q2-1"));
        String[][] tfStr = {
                { "q0", "λ", "q1-0" },
                { "q0", "λ", "q2-0" },
                { "q1-0", "a", "q1-0" },
                { "q1-0", "b", "q1-0" },
                { "q1-0", "b", "q1-1" },
                { "q1-1", "b", "q1-2" },
                { "q1-2", "a", "q1-2" },
                { "q1-2", "b", "q1-2" },
                { "q2-0", "a", "q2-1" },
                { "q2-0", "b", "q2-0" },
                { "q2-1", "b", "q2-0" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }


    // pag 166 FEATURE - Machine - NFA-λ Simulator
    @Test
    public void example_5_5_1Test() throws Exception {
        FiniteStateAutomaton nfa = getAutomatonExample_5_5_1();
        assertTrue(nfa.processEntry("ababb"));
        assertTrue(nfa.processEntry("ababa"));
    }

    private FiniteStateAutomaton getAutomatonExample_5_6_1() {
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
                { "q1", "b", "q1" },
                { "q2", "λ", "q1" },
                { "q2", "c", "q2" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    private FiniteStateAutomaton getAutomatonExample_5_6_1NFA() {
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
        finalStates.add(statesMap.get("q2"));
        String[][] tfStr = {
                { "q0", "a", "q0" },
                { "q0", "a", "q1" },
                { "q0", "a", "q2" },
                { "q1", "b", "q1" },
                { "q2", "b", "q1" },
                { "q2", "c", "q1" },
                { "q2", "c", "q2" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }


    // pag 171 FEATURE - Machine - Removing Nondeterminism
    @Test
    public void example_5_6_1Test() throws Exception {
        FiniteStateAutomaton nfa_l = getAutomatonExample_5_6_1();
        FiniteStateAutomaton nfa = getAutomatonExample_5_6_1NFA();
        //System.out.println(nfa.AFNDtoAFD());
        assertEquals(nfa, nfa_l.AFNDLambdaToAFND());
    }

    private FiniteStateAutomaton getAutomatonFigure_5_4DFA() {
        String[] statesStr = { "<q0>", "<q0,q1,q2>", "<q1,q2>", "<q1>" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("<q0,q1,q2>"));
        finalStates.add(statesMap.get("<q1,q2>"));
        finalStates.add(statesMap.get("<q1>"));
        String[][] tfStr = {
                { "<q0>", "a", "<q0,q1,q2>" },
                { "<q0,q1,q2>", "a", "<q0,q1,q2>" },
                { "<q0,q1,q2>", "b", "<q1>" },
                { "<q0,q1,q2>", "c", "<q1,q2>" },
                { "<q1,q2>", "b", "<q1>" },
                { "<q1,q2>", "c", "<q1,q2>" },
                { "<q1>", "b", "<q1>" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("<q0>"), finalStates, tf);
    }

    // pag 173 FEATURE - Machine - Removing Nondeterminism
    @Test
    public void figure_5_4Test() throws Exception {
        FiniteStateAutomaton nfa_l = getAutomatonExample_5_6_1();
        FiniteStateAutomaton nfa = getAutomatonExample_5_6_1NFA();
        FiniteStateAutomaton resultDFA1 = nfa_l.AFNDLambdaToAFD();
        FiniteStateAutomaton resultDFA2 = nfa.AFNDtoAFD();
        FiniteStateAutomaton expectedDFA = getAutomatonFigure_5_4DFA();
        assertEquals(resultDFA1, resultDFA2);
        assertEquals(expectedDFA, resultDFA1);
    }

    private FiniteStateAutomaton getAutomatonExample_5_6_2() {
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
                { "q0", "a", "q0" },
                { "q0", "a", "q1" },
                { "q1", "b", "q1" },
                { "q1", "b", "q2" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    private FiniteStateAutomaton getAutomatonExample_5_6_2DFA() {
        String[] statesStr = { "<q0>", "<q0,q1>", "<q1,q2>" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("<q1,q2>"));
        String[][] tfStr = {
                { "<q0>", "a", "<q0,q1>" },
                { "<q0,q1>", "a", "<q0,q1>" },
                { "<q0,q1>", "b", "<q1,q2>" },
                { "<q1,q2>", "b", "<q1,q2>" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("<q0>"), finalStates, tf);
    }

    // pag 174 FEATURE - Machine - Removing Nondeterminism
    @Test
    public void example_5_6_2Test() throws Exception {
        FiniteStateAutomaton nfa = getAutomatonExample_5_6_2();
        FiniteStateAutomaton resultDFA = nfa.AFNDtoAFD();
        FiniteStateAutomaton expectedDFA = getAutomatonExample_5_6_2DFA();
        assertEquals(expectedDFA, resultDFA);
    }

    private FiniteStateAutomaton getAutomatonExample_5_6_3() {
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
                { "q0", "b", "q2" },
                { "q1", "a", "q0" },
                { "q1", "a", "q1" },
                { "q1", "b", "q0" },
                { "q2", "b", "q1" },
                { "q2", "b", "q2" }

        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    private FiniteStateAutomaton getAutomatonExample_5_6_3DFA() {
        String[] statesStr = { "<q0>", "<q1>", "<q2>", "<q0,q1>", "<q1,q2>", "<q0,q2>", "<q0,q1,q2>" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("<q0,q2>"));
        finalStates.add(statesMap.get("<q1,q2>"));
        finalStates.add(statesMap.get("<q0,q1,q2>"));
        finalStates.add(statesMap.get("<q2>"));
        String[][] tfStr = {
                { "<q0>", "a", "<q1>" },
                { "<q0>", "b", "<q2>" },
                { "<q1>", "a", "<q0,q1>" },
                { "<q1>", "b", "<q0>" },
                { "<q2>", "b", "<q1,q2>" },
                { "<q0,q1>", "a", "<q0,q1>" },
                { "<q0,q1>", "b", "<q0,q2>" },
                { "<q1,q2>", "a", "<q0,q1>" },
                { "<q1,q2>", "b", "<q0,q1,q2>" },
                { "<q0,q2>", "a", "<q1>" },
                { "<q0,q2>", "b", "<q1,q2>" },
                { "<q0,q1,q2>", "a", "<q0,q1>" },
                { "<q0,q1,q2>", "b", "<q0,q1,q2>" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("<q0>"), finalStates, tf);
    }

    // pag 175 FEATURE - Machine - Removing Nondeterminism
    @Test
    public void example_5_6_3Test() throws Exception {
        FiniteStateAutomaton nfa = getAutomatonExample_5_6_3();
        FiniteStateAutomaton resultDFA = nfa.AFNDtoAFD();
        FiniteStateAutomaton expectedDFA = getAutomatonExample_5_6_3DFA();
        assertEquals(expectedDFA, resultDFA);
    }

    private FiniteStateAutomaton getAutomatonExample_5_6_4() {
        String[] statesStr = { "q0", "q1", "q2", "q3" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("q2"));
        finalStates.add(statesMap.get("q3"));
        String[][] tfStr = {
                { "q0", "λ", "q1" },
                { "q0", "λ", "q3" },
                { "q1", "a", "q2" },
                { "q2", "b", "q1" },
                { "q3", "a", "q3" }

        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    private FiniteStateAutomaton getAutomatonExample_5_6_4NFA() {
        String[] statesStr = { "q0", "q1", "q2", "q3" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("q0"));
        finalStates.add(statesMap.get("q2"));
        finalStates.add(statesMap.get("q3"));
        String[][] tfStr = {
                { "q0", "a", "q2" },
                { "q0", "a", "q3" },
                { "q1", "a", "q2" },
                { "q2", "b", "q1" },
                { "q3", "a", "q3" }

        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    private FiniteStateAutomaton getAutomatonExample_5_6_4DFA() {
        String[] statesStr = { "<q0,q1,q3>", "<q2,q3>", "<q3>", "<q1>", "<q2>" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("<q0,q1,q3>"));
        finalStates.add(statesMap.get("<q2,q3>"));
        finalStates.add(statesMap.get("<q3>"));
        finalStates.add(statesMap.get("<q2>"));
        String[][] tfStr = {
                { "<q0,q1,q3>", "a", "<q2,q3>" },
                { "<q2,q3>", "a", "<q3>" },
                { "<q2,q3>", "b", "<q1>" },
                { "<q3>", "a", "<q3>" },
                { "<q1>", "a", "<q2>" },
                { "<q2>", "b", "<q1>" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("<q0,q1,q3>"), finalStates, tf);
    }

    // pag 176 FEATURE - Machine - Removing Nondeterminism
    @Test
    public void example_5_6_4Test() throws Exception {
        FiniteStateAutomaton nfa_l = getAutomatonExample_5_6_4();
        FiniteStateAutomaton resultNFA = nfa_l.AFNDLambdaToAFND();
        FiniteStateAutomaton expectedNFA = getAutomatonExample_5_6_4NFA();
        assertEquals(expectedNFA, resultNFA);
        FiniteStateAutomaton resultDFA1 = resultNFA.AFNDtoAFD();
        FiniteStateAutomaton resultDFA2 = nfa_l.AFNDLambdaToAFD();
        FiniteStateAutomaton expectedDFA = getAutomatonExample_5_6_4DFA();
        assertEquals(resultDFA1, resultDFA2);
        assertEquals(expectedDFA, resultDFA1);
    }

    private FiniteStateAutomaton getAutomatonExample_5_7_1() {
        String[] statesStr = { "q0", "q1", "q2", "q3", "q4", "q5", "q6", "q7" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("q1"));
        finalStates.add(statesMap.get("q2"));
        finalStates.add(statesMap.get("q3"));
        finalStates.add(statesMap.get("q4"));
        finalStates.add(statesMap.get("q5"));
        finalStates.add(statesMap.get("q6"));
        String[][] tfStr = {
                { "q0", "a", "q1" },
                { "q0", "b", "q4" },
                { "q1", "a", "q2" },
                { "q1", "b", "q3" },
                { "q2", "a", "q7" },
                { "q2", "b", "q7" },
                { "q3", "a", "q7" },
                { "q3", "b", "q3" },
                { "q4", "a", "q5" },
                { "q4", "b", "q6" },
                { "q5", "a", "q7" },
                { "q5", "b", "q7" },
                { "q6", "a", "q7" },
                { "q6", "b", "q6" },
                { "q7", "a", "q7" },
                { "q7", "b", "q7" }

        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    private FiniteStateAutomaton getAutomatonExample_5_7_1DFAMin() {
        String q1q4Str = "qc0";
        String q2q5Str = "qc1";
        String q3q6Str = "qc2";
        String[] statesStr = { "q0", q1q4Str, q2q5Str, q3q6Str, "q7" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get(q1q4Str));
        finalStates.add(statesMap.get(q2q5Str));
        finalStates.add(statesMap.get(q3q6Str));
        String[][] tfStr = {
                { "q0", "a", q1q4Str },
                { "q0", "b", q1q4Str },
                { q1q4Str, "a", q2q5Str },
                { q1q4Str, "b", q3q6Str },
                { q2q5Str, "a", "q7" },
                { q2q5Str, "b", "q7" },
                { q3q6Str, "a", "q7" },
                { q3q6Str, "b", q3q6Str },
                { "q7", "a", "q7" },
                { "q7", "b", "q7" }

        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    // pag 181 FEATURE - Machine - DFA Minimization
    @Test
    public void example_5_7_1Test() throws Exception {
        FiniteStateAutomaton dfa = getAutomatonExample_5_7_1();
        FiniteStateAutomaton resultDFA = dfa.getMinimizeAutomaton();
        FiniteStateAutomaton expectDFA = getAutomatonExample_5_7_1DFAMin();
        assertEquals(expectDFA, resultDFA);
    }

    private FiniteStateAutomaton getAutomatonExample_5_7_2() {
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
                { "q0", "a", "q4" },
                { "q0", "b", "q1" },
                { "q1", "a", "q5" },
                { "q1", "b", "q2" },
                { "q2", "a", "q6" },
                { "q2", "b", "q3" },
                { "q3", "a", "q3" },
                { "q3", "b", "q3" },
                { "q4", "a", "q4" },
                { "q4", "b", "q4" },
                { "q5", "a", "q5" },
                { "q5", "b", "q5" },
                { "q6", "a", "q6" },
                { "q6", "b", "q6" }

        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    private FiniteStateAutomaton getAutomatonExample_5_7_2DFAMin() {
        String q4q5q6Str = "qc0";
        String[] statesStr = { "q0", "q1", "q2", "q3", q4q5q6Str };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get(q4q5q6Str));
        String[][] tfStr = {
                { "q0", "a", q4q5q6Str },
                { "q0", "b", "q1" },
                { "q1", "a", q4q5q6Str },
                { "q1", "b", "q2" },
                { "q2", "a", q4q5q6Str },
                { "q2", "b", "q3" },
                { "q3", "a", "q3" },
                { "q3", "b", "q3" },
                { q4q5q6Str, "a", q4q5q6Str },
                { q4q5q6Str, "b", q4q5q6Str }

        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    // pag 182 FEATURE - Machine - DFA Minimization
    @Test
    public void example_5_7_2Test() throws Exception {
        FiniteStateAutomaton dfa = getAutomatonExample_5_7_2();
        FiniteStateAutomaton resultDFA = dfa.getMinimizeAutomaton();
        FiniteStateAutomaton expectDFA = getAutomatonExample_5_7_2DFAMin();
        assertEquals(expectDFA, resultDFA);
    }







}
