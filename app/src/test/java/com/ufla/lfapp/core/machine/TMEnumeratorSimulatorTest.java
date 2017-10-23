package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.core.machine.tm.TMSimulator;
import com.ufla.lfapp.core.machine.tm.TMTransitionFunction;
import com.ufla.lfapp.core.machine.tm.TuringMachine;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTapeSimulator;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTapeTransitionFunction;
import com.ufla.lfapp.core.machine.tm.var.TuringMachineMultiTape;
import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by terra on 10/07/17.
 */

public class TMEnumeratorSimulatorTest {

    private TuringMachineMultiTape tm;

    static {
        ResourcesContext.isTest = true;
    }

    public TMMove[] getMoves(String[] movesStr) {
        TMMove[] moves = new TMMove[movesStr.length];
        for (int i = 0; i < movesStr.length; i++) {
            moves[i] = TMMove.getInstance(movesStr[i]);
        }
        return moves;
    }
    @Before
    public void setUp() {
        String[] statesStr = { "q0", "q1", "q2", "q3", "q4"};
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
                { { "q1" }, { "B", "B" }, { "q2" }, { "#", "B" }, { "R", "S" } },
                { { "q2" }, { "B", "B" }, { "q3" }, { "#", "X" }, { "R", "S" } },
                { { "q3" }, { "B", "X" }, { "q3" }, { "a", "X" }, { "R", "L" } },
                { { "q3" }, { "B", "B" }, { "q4" }, { "B", "B" }, { "S", "R" } },
                { { "q4" }, { "B", "X" }, { "q4" }, { "b", "X" }, { "R", "R" } },
                { { "q4" }, { "B", "B" }, { "q3" }, { "#", "X" }, { "R", "S" } }
        };
        Set<TMMultiTapeTransitionFunction> tf = new HashSet<>();
        for (String[][] tfS : tfStr) {
            tf.add(new TMMultiTapeTransitionFunction(statesMap.get(tfS[0][0]),
                    statesMap.get(tfS[2][0]), tfS[1], tfS[3],
                    getMoves(tfS[4])));
        }
        this.tm = new TuringMachineMultiTape(states, statesMap.get("q0"), finalStates, tf, 2);
    }

    @Test
    public void testWord_01() throws Exception {
        TMMultiTapeSimulator enumSim = new TMMultiTapeSimulator(tm);
        String[] tapes = enumSim.nextWord().getTapes();
        String[] words = tapes[0].replaceAll("B", "").split("#");
        String[] wordsExp = new String[0];
        assertArrayEquals(wordsExp, words);
    }

    @Test
    public void testWord_02() throws Exception {
        TMMultiTapeSimulator enumSim = new TMMultiTapeSimulator(tm);
        int skip = 2;
        while (skip > 0) {
            enumSim.nextWord();
            skip--;
        }
        String[] tapes = enumSim.nextWord().getTapes();
        String[] words = tapes[0].replaceAll("B", "").split("#");
        String[] wordsExp = new String[] { "", "", "ab" };
        assertArrayEquals(wordsExp, words);
    }

    @Test
    public void testWord_03() throws Exception {
        TMMultiTapeSimulator enumSim = new TMMultiTapeSimulator(tm);
        int skip = 3;
        while (skip > 0) {
            enumSim.nextWord();
            skip--;
        }
        String[] tapes = enumSim.nextWord().getTapes();
        String[] words = tapes[0].replaceAll("B", "").split("#");
        String[] wordsExp = new String[] { "", "", "ab", "aabb" };
        assertArrayEquals(wordsExp, words);
    }

    @Test
    public void testWord_04() throws Exception {
        TMMultiTapeSimulator enumSim = new TMMultiTapeSimulator(tm);
        int skip = 4;
        while (skip > 0) {
            enumSim.nextWord();
            skip--;
        }
        String[] tapes = enumSim.nextWord().getTapes();
        String[] words = tapes[0].replaceAll("B", "").split("#");
        String[] wordsExp = new String[] { "", "", "ab", "aabb", "aaabbb" };
        assertArrayEquals(wordsExp, words);
    }

    @Test
    public void testWord_05() throws Exception {
        TMMultiTapeSimulator enumSim = new TMMultiTapeSimulator(tm);
        int skip = 5;
        while (skip > 0) {
            enumSim.nextWord();
            skip--;
        }
        String[] tapes = enumSim.nextWord().getTapes();
        String[] words = tapes[0].replaceAll("B", "").split("#");
        String[] wordsExp = new String[] { "", "", "ab", "aabb", "aaabbb", "aaaabbbb" };
        assertArrayEquals(wordsExp, words);
    }


}
