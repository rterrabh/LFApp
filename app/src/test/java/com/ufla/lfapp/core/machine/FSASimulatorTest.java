package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.core.machine.tm.TMSimulator;
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

public class FSASimulatorTest {

    private FiniteStateAutomaton fsa;

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
                { "q0", "a", "q1" },
                { "q1", "a", "q1" },
                { "q1", "b", "q2" },
                { "q2", "b", "q2" },
                { "q2", "a", "q3" },
                { "q3", "a", "q3" },
                { "q3", "b", "q2" }
        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        this.fsa = new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    @Test
    public void testWord_01() throws Exception {
        assertEquals(true, fsa.processEntry("aba"));
//        System.out.println(tmSimulator.processALL());
//        System.out.println(Arrays.toString(tmSimulator.getTapes()));
//        System.out.println(Arrays.deepToString(tmSimulator.getConfigurations()));
    }

    @Test
    public void testWord_02() throws Exception {
        assertEquals(true, fsa.processEntry("ababa"));
    }

    @Test
    public void testWord_03() throws Exception {
        assertEquals(true, fsa.processEntry("aababbabba"));
    }

    @Test
    public void testWord_04() throws Exception {
        assertEquals(true, fsa.processEntry("aaababbabbbaaa"));
    }

    @Test
    public void testWord_05() throws Exception {
        assertEquals(false, fsa.processEntry("a"));
    }

    @Test
    public void testWord_06() throws Exception {
        assertEquals(false, fsa.processEntry("aab"));
    }

    @Test
    public void testWord_07() throws Exception {
        assertEquals(false, fsa.processEntry("aabb"));
    }

    @Test
    public void testWord_08() throws Exception {
        assertEquals(false, fsa.processEntry("aabbaabab"));
    }

    @Test
    public void testWord_09() throws Exception {
        assertEquals(false, fsa.processEntry("aabbababbb"));
    }

    @Test
    public void testWord_10() throws Exception {
        assertEquals(false, fsa.processEntry("aabbbbababab"));
    }

}
