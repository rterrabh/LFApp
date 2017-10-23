package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.core.machine.tm.TMSimulator;
import com.ufla.lfapp.core.machine.tm.TMTransitionFunction;
import com.ufla.lfapp.core.machine.tm.TuringMachine;
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

import static org.junit.Assert.assertEquals;

/**
 * Created by lfapp on 12/06/17.
 */

public class LBASimulatorTest {

    private TuringMachine tm;

    static {
        ResourcesContext.isTest = true;
    }

    @Before
    public void setUp() {
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
        String[][] tfStr = { {"q0", "<", "q1", "<", "R" },
                { "q1", "a", "q2", "X", "R" },
                { "q2", "Y", "q2", "Y", "R" },
                { "q2", "a", "q2", "a", "R" },
                { "q2", "b", "q3", "Y", "R" },
                { "q3", "Z", "q3", "Z", "R" },
                { "q3", "b", "q3", "b", "R" },
                { "q3", "c", "q4", "Z", "R" },
                { "q4", "c", "q4", "c", "R" },
                { "q4", ">", "q5", ">", "L" },
                { "q5", "Z", "q5", "Z", "L" },
                { "q5", "Y", "q5", "Y", "L" },
                { "q5", "c", "q5", "c", "L" },
                { "q5", "b", "q5", "b", "L" },
                { "q5", "a", "q5", "a", "L" },
                { "q5", "X", "q1", "X", "R" },
                { "q1", "Y", "q6", "Y", "R" },
                { "q6", "Y", "q6", "Y", "R" },
                { "q6", "Z", "q6", "Z", "R" },
                { "q6", ">", "q7", ">", "L" },
                { "q7", "Z", "q7", "Z", "L" },
                { "q7", "Y", "q7", "Y", "L" },
                { "q7", "X", "q7", "X", "L" },
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
        TMSimulator tmSimulator = new TMSimulator(tm, "abc");
        assertEquals(true, tmSimulator.processALL());
//        System.out.println(tmSimulator.processALL());
//        System.out.println(Arrays.toString(tmSimulator.getTapes()));
//        System.out.println(Arrays.deepToString(tmSimulator.getConfigurations()));
    }

    @Test
    public void testWord_02() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "aabbcc");
        assertEquals(true, tmSimulator.processALL());
    }

    @Test
    public void testWord_03() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "aaabbbccc");
        assertEquals(true, tmSimulator.processALL());
    }

    @Test
    public void testWord_04() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "aaaabbbbcccc");
        assertEquals(true, tmSimulator.processALL());
    }

    @Test
    public void testWord_05() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "aabc");
        assertEquals(false, tmSimulator.processALL());
    }

    @Test
    public void testWord_06() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "abbc");
        assertEquals(false, tmSimulator.processALL());
    }

    @Test
    public void testWord_07() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "abcc");
        assertEquals(false, tmSimulator.processALL());
    }

    @Test
    public void testWord_08() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "aabbc");
        assertEquals(false, tmSimulator.processALL());
    }

    @Test
    public void testWord_09() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "abbcc");
        assertEquals(false, tmSimulator.processALL());
    }

    @Test
    public void testWord_10() throws Exception {
        TMSimulator tmSimulator = new TMSimulator(tm, "aabcc");
        assertEquals(false, tmSimulator.processALL());
    }
}
