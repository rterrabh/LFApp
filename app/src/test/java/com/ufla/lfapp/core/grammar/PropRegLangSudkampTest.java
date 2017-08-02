package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.*;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by carlos on 02/08/17.
 */

public class PropRegLangSudkampTest {


    static {
        ResourcesContext.isTest = true;
        TMMove.test = true;
    }

    private FiniteStateAutomaton getAutomatonExample_6_1_1() {
        String q0 = "q0";
        String q1 = "q1";
        String q2 = "q2";
        String q3 = "q3";
        String q4 = "q4";
        String q5 = "q5";
        String q6 = "q6";
        String q7 = "q7";
        String q8 = "q8";
        String q9 = "q9";
        String q10 = "q10";
        String q11 = "q11";
        String[] statesStr = { q0, q1, q2, q3, q4, q5, q6, q7, q8, q9 };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get(q9));
        String[][] tfStr = {
                { q0, "λ", q1 },
                { q0, "λ", q7 },
                { q1, "λ", q2 },
                { q1, "λ", q4 },
                { q2, "a", q3 },
                { q3, "λ", q6 },
                { q4, "b", q5 },
                { q5, "λ", q6 },
                { q6, "λ", q7 },
                { q7, "λ", q1 },
                { q7, "b", q8 },
                { q8, "a", q9 }

        };
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    // pag 192 FEATURE - Properties of Regular Languages - FSA from Regex
    @org.junit.Test
    public void example_6_1_1Test() throws Exception {
        FiniteStateAutomaton expectNFA = getAutomatonExample_6_1_1();
        FiniteStateAutomaton resultNFA =
                FiniteStateAutomatonGUI.getAutomatonByRegex("(a|b)*ba");
        assertEquals(expectNFA, resultNFA);
    }

}
