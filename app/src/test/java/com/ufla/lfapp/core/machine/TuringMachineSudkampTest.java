package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.machine.pda.PDASimulator;
import com.ufla.lfapp.core.machine.pda.PDATransitionFunction;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.core.machine.tm.TMSimulator;
import com.ufla.lfapp.core.machine.tm.TMTransitionFunction;
import com.ufla.lfapp.core.machine.tm.TuringMachine;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTapeSimulator;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTapeTransitionFunction;
import com.ufla.lfapp.core.machine.tm.var.TuringMachineMultiTape;
import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

/**
 * Created by carlos on 02/08/17.
 */

public class TuringMachineSudkampTest {

    static {
        ResourcesContext.isTest = true;
        TMMove.test = true;
    }

    public TMMove[] getMoves(String[] movesStr) {
        TMMove[] moves = new TMMove[movesStr.length];
        for (int i = 0; i < movesStr.length; i++) {
            moves[i] = TMMove.getInstance(movesStr[i]);
        }
        return moves;
    }

    private TuringMachine getTMExample_8_1_1() {
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
                { "q0", "B", "q1", "B", "R" },
                { "q1", "a", "q1", "b", "R" },
                { "q1", "b", "q1", "a", "R" },
                { "q1", "B", "q2", "B", "L" }

        };
        SortedSet<TMTransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new TMTransitionFunction(statesMap.get(tfS[0]),
                    tfS[1], statesMap.get(tfS[2]), tfS[3],
                    TMMove.getInstance(tfS[4])));
        }
        return new TuringMachine(states, statesMap.get("q0"), finalStates, tf);
    }

    // pag 257 FEATURE - Turing Machines - TM Simulator
    @Test
    public void example_8_1_1Test() throws Exception {
        TuringMachine tm = getTMExample_8_1_1();
        String string = "abab";
        TMSimulator tmSimulator = new TMSimulator(tm,string);
        assertTrue(tmSimulator.processWithoutSpan());
    }

    private TuringMachine getTMExample_8_2_1() {
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
                { "q0", "B", "q1", "B", "R" },
                { "q1", "b", "q1", "b", "R" },
                { "q1", "a", "q2", "a", "R" },
                { "q2", "b", "q1", "b", "R" },
                { "q2", "a", "q3", "a", "R" }

        };
        SortedSet<TMTransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new TMTransitionFunction(statesMap.get(tfS[0]),
                    tfS[1], statesMap.get(tfS[2]), tfS[3],
                    TMMove.getInstance(tfS[4])));
        }
        return new TuringMachine(states, statesMap.get("q0"), finalStates, tf);
    }

    // pag 260 FEATURE - Turing Machines - TM Simulator
    @Test
    public void example_8_2_1Test() throws Exception {
        TuringMachine tm = getTMExample_8_2_1();
        String string = "aabb";
        TMSimulator tmSimulator = new TMSimulator(tm,string);
        assertTrue(tmSimulator.processWithoutSpan());
    }


    private TuringMachineMultiTape getTMExample_8_6_1() {
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
        String[][][] tfStr = {
                { { "q0" }, { "B", "B" }, { "q1" }, { "B", "B" }, { "R", "R" } },
                { { "q1" }, { "a", "B" }, { "q1" }, { "a", "a" }, { "R", "R" } },
                { { "q1" }, { "b", "B" }, { "q2" }, { "b", "B" }, { "R", "L" } },
                { { "q2" }, { "a", "a" }, { "q2" }, { "a", "a" }, { "R", "L" } },
                { { "q2" }, { "B", "B" }, { "q3" }, { "B", "B" }, { "R", "R" } }
        };
        Set<TMMultiTapeTransitionFunction> tf = new HashSet<>();
        for (String[][] tfS : tfStr) {
            tf.add(new TMMultiTapeTransitionFunction(statesMap.get(tfS[0][0]),
                    statesMap.get(tfS[2][0]), tfS[1], tfS[3],
                    getMoves(tfS[4])));
        }
        return new TuringMachineMultiTape(states, statesMap.get("q0"), finalStates, tf, 2);
    }

    // pag 269 FEATURE - Turing Machines - TM Multi-tape Simulator
    @Test
    public void example_8_6_1Test() throws Exception {
        TuringMachineMultiTape tm = getTMExample_8_6_1();
        String string1 = "aaabaaa";
        String string2 = "aabaaa";
        String string3 = "b";
        String string4 = "abba";
        TMMultiTapeSimulator tmSimulator = new TMMultiTapeSimulator(tm, string1);
        assertTrue(tmSimulator.process());

        tmSimulator = new TMMultiTapeSimulator(tm, string2);
        assertFalse(tmSimulator.process());

        tmSimulator = new TMMultiTapeSimulator(tm, string3);
        assertTrue(tmSimulator.process());

        tmSimulator = new TMMultiTapeSimulator(tm, string4);
        assertFalse(tmSimulator.process());

    }

    private TuringMachine getTMExample_8_7_1() {
        String[] statesStr = { "q0", "q1", "q2", "q3", "q4", "q5", "q6", "q7" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("q4"));
        finalStates.add(statesMap.get("q7"));
        String[][] tfStr = {
                { "q0", "B", "q1", "B", "R" },
                { "q1", "a", "q1", "a", "R" },
                { "q1", "b", "q1", "b", "R" },
                { "q1", "c", "q1", "c", "R" },
                { "q1", "c", "q2", "c", "R" },
                { "q1", "c", "q5", "c", "L" },
                { "q2", "a", "q3", "a", "R" },
                { "q3", "b", "q4", "b", "R" },
                { "q5", "b", "q6", "b", "L" },
                { "q6", "a", "q7", "a", "L" }

        };
        SortedSet<TMTransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new TMTransitionFunction(statesMap.get(tfS[0]),
                    tfS[1], statesMap.get(tfS[2]), tfS[3],
                    TMMove.getInstance(tfS[4])));
        }
        return new TuringMachine(states, statesMap.get("q0"), finalStates, tf);
    }

    // pag 275 FEATURE - Turing Machines - TM Nondeterministic Simulator
    @Test
    public void example_8_7_1Test() throws Exception {
        TuringMachine tm = getTMExample_8_7_1();
        String string1 = "cab";
        String string2 = "abc";
        String string3 = "cbaabac";
        String string4 = "cbcacbb";
        String string5 = "abacaba";
        String string6 = "cacbacbabcbba";

        TMSimulator tmSimulator = new TMSimulator(tm, string1);
        assertTrue(tmSimulator.processWithoutSpan());

        tmSimulator = new TMSimulator(tm, string2);
        assertTrue(tmSimulator.processWithoutSpan());

        tmSimulator = new TMSimulator(tm, string3);
        assertFalse(tmSimulator.processWithoutSpan());

        tmSimulator = new TMSimulator(tm, string4);
        assertFalse(tmSimulator.processWithoutSpan());

        tmSimulator = new TMSimulator(tm, string5);
        assertTrue(tmSimulator.processWithoutSpan());

        tmSimulator = new TMSimulator(tm, string6);
        assertTrue(tmSimulator.processWithoutSpan());

    }

    private TuringMachineMultiTape getTMExample_8_8_1() {
        String[] statesStr = { "q0", "q1", "q2", "q3", "q4",
                "q5", "q6" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        String[][][] tfStr = {
                { { "q0" }, { "B", "B" }, { "q1" }, { "B", "B" }, { "R", "R" } },
                { { "q1" }, { "B", "B" }, { "q2" }, { "#", "a" }, { "R", "S" } },
                { { "q2" }, { "B", "a" }, { "q3" }, { "#", "a" }, { "R", "S" } },
                { { "q3" }, { "B", "a" }, { "q3" }, { "a", "a" }, { "R", "R" } },
                { { "q3" }, { "B", "B" }, { "q4" }, { "B", "B" }, { "S", "L" } },
                { { "q4" }, { "B", "a" }, { "q4" }, { "b", "a" }, { "R", "L" } },
                { { "q4" }, { "B", "B" }, { "q5" }, { "B", "B" }, { "S", "R" } },
                { { "q5" }, { "B", "a" }, { "q5" }, { "c", "a" }, { "R", "R" } },
                { { "q5" }, { "B", "B" }, { "q6" }, { "B", "a" }, { "S", "L" } },
                { { "q6" }, { "B", "a" }, { "q6" }, { "B", "a" }, { "S", "L" } },
                { { "q6" }, { "B", "B" }, { "q3" }, { "#", "B" }, { "R", "R" } },
        };
        Set<TMMultiTapeTransitionFunction> tf = new HashSet<>();
        for (String[][] tfS : tfStr) {
            tf.add(new TMMultiTapeTransitionFunction(statesMap.get(tfS[0][0]),
                    statesMap.get(tfS[2][0]), tfS[1], tfS[3],
                    getMoves(tfS[4])));
        }
        return new TuringMachineMultiTape(states, statesMap.get("q0"), finalStates, tf, 2);
    }

    // pag 283 FEATURE - Turing Machines - TM as Language Enumerator Simulator
    @Test
    public void example_8_8_1Test() throws Exception {
        TuringMachineMultiTape tm = getTMExample_8_8_1();
        TMMultiTapeSimulator enumSim = new TMMultiTapeSimulator(tm);
        int skip = 5;
        while (skip > 0) {
            enumSim.nextWord();
            skip--;
        }
        String[] tapes = enumSim.nextWord().getTapes();
        String[] words = tapes[0].replaceAll("B", "").split("#");
        String[] wordsExp = new String[] { "", "", "abc", "aabbcc", "aaabbbccc", "aaaabbbbcccc" };
        assertArrayEquals(wordsExp, words);
    }

}
