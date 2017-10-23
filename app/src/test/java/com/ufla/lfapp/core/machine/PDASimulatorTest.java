package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.machine.pda.PDASimulator;
import com.ufla.lfapp.core.machine.pda.PDATransitionFunction;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.utils.Symbols;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by terra on 10/07/17.
 */

public class PDASimulatorTest {

    private PushdownAutomaton pda;

    static {
        ResourcesContext.isTest = true;
    }

    @Before
    public void setUp() {
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
                {"q0", "a", "q0", Symbols.LAMBDA, "X" },
                {"q0", "b", "q1", "X", Symbols.LAMBDA },
                {"q1", "b", "q1", "X", Symbols.LAMBDA }
        };
        Set<PDATransitionFunction> tf = new HashSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new PDATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2]), tfS[4],
                    tfS[3]));
        }
        this.pda = new PushdownAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    @Test
    public void testWord_01() throws Exception {
        PDASimulator pdaSimulator = new PDASimulator(pda, "ab");
        assertEquals(true, pdaSimulator.process());
    }

    @Test
    public void testWord_02() throws Exception {
        PDASimulator pdaSimulator = new PDASimulator(pda, "aabb");
        assertEquals(true, pdaSimulator.process());
    }

    @Test
    public void testWord_03() throws Exception {
        PDASimulator pdaSimulator = new PDASimulator(pda, "aaabbb");
        assertEquals(true, pdaSimulator.process());
    }

    @Test
    public void testWord_04() throws Exception {
        PDASimulator pdaSimulator = new PDASimulator(pda, "aaaabbbb");
        assertEquals(true, pdaSimulator.process());
    }

    @Test
    public void testWord_05() throws Exception {
        PDASimulator pdaSimulator = new PDASimulator(pda, "a");
        assertEquals(false, pdaSimulator.process());
    }

    @Test
    public void testWord_06() throws Exception {
        PDASimulator pdaSimulator = new PDASimulator(pda, "b");
        assertEquals(false, pdaSimulator.process());
    }

    @Test
    public void testWord_07() throws Exception {
        PDASimulator pdaSimulator = new PDASimulator(pda, "aa");
        assertEquals(false, pdaSimulator.process());
    }

    @Test
    public void testWord_08() throws Exception {
        PDASimulator pdaSimulator = new PDASimulator(pda, "bb");
        assertEquals(false, pdaSimulator.process());
    }

    @Test
    public void testWord_09() throws Exception {
        PDASimulator pdaSimulator = new PDASimulator(pda, "aba");
        assertEquals(false, pdaSimulator.process());
    }

    @Test
    public void testWord_10() throws Exception {
        PDASimulator pdaSimulator = new PDASimulator(pda, "bbaa");
        assertEquals(false, pdaSimulator.process());
    }

}
