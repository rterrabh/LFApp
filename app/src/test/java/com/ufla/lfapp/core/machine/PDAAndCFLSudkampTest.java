package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.machine.pda.GrammarToPDAExt;
import com.ufla.lfapp.core.machine.pda.PDAExtTransitionFunction;
import com.ufla.lfapp.core.machine.pda.PDASimulator;
import com.ufla.lfapp.core.machine.pda.PDAToGrammar;
import com.ufla.lfapp.core.machine.pda.PDATransitionFunction;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.core.machine.pda.PushdownAutomatonExtend;
import com.ufla.lfapp.utils.ResourcesContext;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by carlos on 02/08/17.
 */

public class PDAAndCFLSudkampTest {

    static {
        ResourcesContext.isTest = true;
    }

    private PushdownAutomaton getAutomatonExample_7_1_1() {
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
                { "q0", "a", "q0", "λ", "B" },
                { "q0", "b", "q0", "λ", "A" },
                { "q0", "c", "q1", "λ", "λ" },
                { "q1", "a", "q1", "B", "λ" },
                { "q1", "b", "q1", "A", "λ" }

        };
        SortedSet<PDATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new PDATransitionFunction(statesMap.get(tfS[0]),
                    tfS[1], statesMap.get(tfS[2]), tfS[4], tfS[3]));
        }
        return new PushdownAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    // pag 225 FEATURE - Pushdow Automaton And Context-Free Languages - PDA Simulator
    @Test
    public void example_7_1_1Test() throws Exception {
        PushdownAutomaton pda = getAutomatonExample_7_1_1();
        String string = "abcba";
        PDASimulator pdaSimulator = new PDASimulator(pda,string);
        assertTrue(pdaSimulator.process());
    }

    private PushdownAutomaton getAutomatonExample_7_2_2() {
        String[] statesStr = { "q0", "q1", "q2", "q3", "q4" };
        SortedSet<State> states = new TreeSet<>();
        Map<String, State> statesMap = new HashMap<>();
        for (String st : statesStr) {
            State state = new State(st);
            states.add(state);
            statesMap.put(st, state);
        }
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(statesMap.get("q4"));
        String[][] tfStr = {
                { "q0", "λ", "q1", "λ", "Z" },
                { "q1", "a", "q1", "B", "λ" },
                { "q1", "a", "q2", "Z", "Z" },
                { "q1", "a", "q2", "A", "A" },
                { "q1", "b", "q1", "A", "λ" },
                { "q1", "b", "q3", "Z", "Z" },
                { "q1", "b", "q3", "B", "B" },
                { "q1", "λ", "q4", "Z", "λ" },
                { "q2", "λ", "q1", "λ", "A" },
                { "q3", "λ", "q1", "λ", "B" }

        };
        SortedSet<PDATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new PDATransitionFunction(statesMap.get(tfS[0]),
                    tfS[1], statesMap.get(tfS[2]), tfS[4], tfS[3]));
        }
        return new PushdownAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    // pag 231 FEATURE - Pushdow Automaton And Context-Free Languages - PDA Simulator
    @Test
    public void example_7_2_2Test() throws Exception {
        PushdownAutomaton pda = getAutomatonExample_7_2_2();
        String string = "abba";
        PDASimulator pdaSimulator = new PDASimulator(pda,string);
        assertTrue(pdaSimulator.process());
    }

    private PushdownAutomatonExtend getPDAExtExample_7_3_0() {
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
                { "q0", "a", "q1", "λ", "B" },
                { "q0", "a", "q1", "λ", "A B" },
                { "q1", "a", "q1", "A", "B" },
                { "q1", "a", "q1", "A", "A B" },
                { "q1", "b", "q1", "B", "λ" }
        };
        Set<PDAExtTransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new PDAExtTransitionFunction(statesMap.get(tfS[0]),
                    tfS[1], statesMap.get(tfS[2]), Arrays.asList(tfS[4].split(" ")), tfS[3]));
        }
        return new PushdownAutomatonExtend(states, statesMap.get("q0"), finalStates, tf);
    }

    // pag 232 FEATURE - Pushdow Automaton And Context-Free Languages - CFG to PDA
    @Test
    public void example_7_3_0Test() throws Exception {
        String grammarTxt =
                "S -> aAB | aB\n" +
                "A -> aAB | aB" +
                "B -> b";
        Grammar grammar = new Grammar(grammarTxt);
        PushdownAutomatonExtend pdaExtResult = GrammarToPDAExt.toPDAutomatonExt(grammar);
        PushdownAutomatonExtend pdaExtExpect = getPDAExtExample_7_3_0();
        Assert.assertEquals(pdaExtExpect, pdaExtResult);
    }

    private PushdownAutomaton getAutomatonExample_7_3_1() {
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
                { "q0", "a", "q0", "λ", "A" },
                { "q0", "c", "q1", "λ", "λ" },
                { "q1", "b", "q1", "A", "λ" }

        };
        SortedSet<PDATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new PDATransitionFunction(statesMap.get(tfS[0]),
                    tfS[1], statesMap.get(tfS[2]), tfS[4], tfS[3]));
        }
        return new PushdownAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    public Grammar getGrammarExample_7_3_1() {
        return null;
    }

    // pag 235 FEATURE - Pushdow Automaton And Context-Free Languages - PDA to CFG
    @Test
    public void example_7_3_1Test() throws Exception {
        PushdownAutomaton pda = getAutomatonExample_7_3_1();

        Grammar resultGrammar = PDAToGrammar.toGrammar(pda);
        String q0Lq1 = "A";
        //System.out.println(resultGrammar);
        String expectedGrammarTxt =
                "S -> <q0,λ,q1>\n" +
                        "<q0,λ,q0> -> a<q0,A,q0> | c<q1,λ,q0> | λ\n" +
                        "<q0,λ,q1> -> a<q0,A,q1> | c<q1,λ,q1>\n" +
                        "<q0,A,q0> -> a<q0,A,q0><q0,A,q0> | a<q0,A,q1><q1,A,q0> | c<q1,A,q0>\n" +
                        "<q0,A,q1> -> a<q0,A,q0><q0,A,q1> | a<q0,A,q1><q1,A,q1> | c<q1,A,q1>\n" +
                        "<q1,A,q0> -> b<q1,λ,q0>\n" +
                        "<q1,A,q1> -> b<q1,λ,q1>\n" +
                        "<q1,λ,q1> -> λ";
        Set<String> variables = new LinkedHashSet<>(Arrays.asList(
                "S", "<q0,λ,q0>", "<q0,λ,q1>", "<q0,A,q0>", "<q0,A,q1>",
                "<q1,A,q0>", "<q1,A,q1>", "<q1,λ,q1>", "<q1,λ,q0>"));
        Set<String> terminals = new LinkedHashSet<>(Arrays.asList("a", "b", "c"));
        String initialSymbol = "S";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        expectedGrammar = new Grammar(variables, terminals, initialSymbol, expectedGrammar.getRules());
        //System.out.println(expectedGrammar);
        Assert.assertEquals(expectedGrammar, resultGrammar);
    }

}
