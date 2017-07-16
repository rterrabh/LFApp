package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.core.machine.tm.TMSimulator;
import com.ufla.lfapp.core.machine.tm.TMTransitionFunction;
import com.ufla.lfapp.core.machine.tm.TuringMachine;
import com.ufla.lfapp.utils.ResourcesContext;

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

public class TMSimulatorTest {

    private TuringMachine tm;

    static {
        ResourcesContext.isTest = true;
        TMMove.test = true;
    }

    @Before
    public void setUp() {
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
                { "q1", "a", "q1", "a", "R" },
                { "q1", "b", "q1", "b", "R" },
                { "q1", "b", "q2", "b", "R" },
                { "q2", "B", "q3", "B", "R" }
        };
        Set<TMTransitionFunction> tf = new HashSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new TMTransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2]), tfS[3],
                    TMMove.getInstance(tfS[4])));
        }
        this.tm = new TuringMachine(states, statesMap.get("q0"), finalStates, tf);
    }

    @Test
    public void testWord_01() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "aabab");
        assertEquals(true, tmSimulator.processWithoutSpan());
//        System.out.println(tmSimulator.processALL());
//        System.out.println(Arrays.toString(tmSimulator.getTapes()));
//        System.out.println(Arrays.deepToString(tmSimulator.getConfigurations()));
    }

    @Test
    public void testWord_02() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "ababab");
        assertEquals(true, tmSimulator.processWithoutSpan());
    }

    @Test
    public void testWord_03() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "abab");
        assertEquals(true, tmSimulator.processWithoutSpan());
    }

    @Test
    public void testWord_04() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "bb");
        assertEquals(true, tmSimulator.processWithoutSpan());
    }

    @Test
    public void testWord_05() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "babaa");
        assertEquals(false, tmSimulator.processWithoutSpan());
    }

    @Test
    public void testWord_06() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "a");
        assertEquals(false, tmSimulator.processWithoutSpan());
    }

    @Test
    public void testWord_07() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "ababa");
        assertEquals(false, tmSimulator.processWithoutSpan());
    }

    @Test
    public void testWord_08() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "ababaabaa");
        assertEquals(false, tmSimulator.processWithoutSpan());
    }

    @Test
    public void testWord_09() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "aaa");
        assertEquals(false, tmSimulator.processWithoutSpan());
    }

    @Test
    public void testWord_10() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "bbbba");
        assertEquals(false, tmSimulator.processWithoutSpan());
    }
}
