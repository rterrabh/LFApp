package com.ufla.lfapp.core.machine;

import android.support.v4.util.Pair;

import com.ufla.lfapp.core.machine.dotlang.DotLanguage;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.utils.Symbols;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by carlos on 11/7/17.
 */

public class FSATest {

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    static final String FSA_09 = "digraph fsa_09 {\n" +
            "\n" +
            "\tdpi = 480;\n" +
            "\trankdir=LR;\n" +
            "\n" +
            "\t\"startRes\" [shape=point, style=\"invis\"];\n" +
            "\tnode [shape = doublecircle, color=\"#968D8D\", fontcolor=black, fillcolor=\"#FFCCCC\", style=\"filled\"];\n" +
            "\t\"q2\", \"q3\";\n" +
            "\n" +
            "\tnode [shape = circle];\n" +
            "\n" +
            "\t\"q0\" [label=<q<sub>0</sub>>, pos=\"3,3!\"];\n" +
            "\t\"q1\" [label=<q<sub>1</sub>>, pos=\"7,3!\"];\n" +
            "\t\"q2\" [label=<q<sub>2</sub>>, pos=\"5,6!\"];\n" +
            "\t\"q3\" [label=<q<sub>3</sub>>, pos=\"5,6!\"];\n" +
            "\n" +
            "\t\"startRes\" -> \"q0\";\n" +
            "\n" +
            "\t\"q0\" -> \"q1\" [label=\"λ\"];\n" +
            "\t\"q0\" -> \"q3\" [label=\"λ\"];\n" +
            "\t\"q1\" -> \"q2\" [label=\"a\"];\n" +
            "\t\"q2\" -> \"q1\" [label=\"b\"];\n" +
            "\t\"q3\" -> \"q3\" [label=\"a\"];\n" +
            "\n" +
            "\n" +
            "}";

    static final String FSA_09_TO_STRING_EXPECTED = "States: [q0, q1, q2, q3]\n" +
            " Alphabet: [a, b, λ]\n" +
            " Final states: [q2, q3]\n" +
            " Start state: q0\n" +
            "Transition:: \n" +
            "(q0, λ) -> (q1)\n" +
            "(q0, λ) -> (q3)\n" +
            "(q1, a) -> (q2)\n" +
            "(q2, b) -> (q1)\n" +
            "(q3, a) -> (q3)\n";

    static final String FSA_09_TO_STRING_TEST_EXPECTED = "States: [q0, q1, q2, q3]\n" +
            "Alphabet: [a, b, λ]\n" +
            "FinalStates: [q2, q3]\n" +
            "InitialState: q0\n" +
            "Transitions: \n" +
            "(q0, λ) -> (q1)\n" +
            "(q0, λ) -> (q3)\n" +
            "(q1, a) -> (q2)\n" +
            "(q2, b) -> (q1)\n" +
            "(q3, a) -> (q3)\n";

    @Test
    public void testToString() {
        DotLanguage dot = new DotLanguage(FSA_09);
        dot.setMachineType(MachineType.FSA);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> fsaPair = dot.toFSA();
        FiniteStateAutomaton fsa = fsaPair.first;

        assertEquals(FSA_09_TO_STRING_EXPECTED, fsa.toString());
    }

    @Test
    public void testToStringTest() {
        DotLanguage dot = new DotLanguage(FSA_09);
        dot.setMachineType(MachineType.FSA);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> fsaPair = dot.toFSA();
        FiniteStateAutomaton fsa = fsaPair.first;

        assertEquals(FSA_09_TO_STRING_TEST_EXPECTED, fsa.toStringTest());
    }

    @Test
    public void testHashCode() {
        DotLanguage dot = new DotLanguage(FSA_09);
        dot.setMachineType(MachineType.FSA);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> fsaPair = dot.toFSA();
        FiniteStateAutomaton fsa = fsaPair.first;
        int hashCodeExpected = 444584176;
        assertEquals(hashCodeExpected, fsa.hashCode());
    }

    @Test
    public void testGetTransitionsAFD() {
        DotLanguage dot = new DotLanguage(FSA_09);
        dot.setMachineType(MachineType.FSA);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> fsaPair = dot.toFSA();
        FiniteStateAutomaton fsa = fsaPair.first;
        State q0 = fsa.getState("q0");
        State q1 = fsa.getState("q1");
        State q2 = fsa.getState("q2");
        State q3 = fsa.getState("q3");
        Map<Pair<State, State>, SortedSet<String>> transitionsAFD = new HashMap<>();
        transitionsAFD.put(Pair.create(q3, q3), new TreeSet<>(Arrays.asList("a")));
        transitionsAFD.put(Pair.create(q1, q2), new TreeSet<>(Arrays.asList("a")));
        transitionsAFD.put(Pair.create(q2, q1), new TreeSet<>(Arrays.asList("b")));
        transitionsAFD.put(Pair.create(q0, q3), new TreeSet<>(Arrays.asList(Symbols.LAMBDA)));
        transitionsAFD.put(Pair.create(q0, q1), new TreeSet<>(Arrays.asList(Symbols.LAMBDA)));

        assertEquals(transitionsAFD, fsa.getTransitionsAFD());
    }

    @Test
    public void testGetFutureStateOfTransition() {
        DotLanguage dot = new DotLanguage(FSA_09);
        dot.setMachineType(MachineType.FSA);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> fsaPair = dot.toFSA();
        FiniteStateAutomaton fsa = fsaPair.first;
        assertEquals(null, fsa.getFutureStateOfTransition(new State("q10"), "a"));
    }

    @Test
    public void testDFAMinimizationTableRowToString() {
        DotLanguage dot = new DotLanguage(FSA_09);
        dot.setMachineType(MachineType.FSA);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> fsaPair = dot.toFSA();
        FiniteStateAutomaton fsa = fsaPair.first;
        FiniteStateAutomaton.DFAMinimizationTableRow dfaMinimizationTableRow = fsa.getDFAMinTableRow(Pair.create(new State("q0"), new State("q1")));
        assertEquals("[q0,q1]\tfalse\t{}\t", dfaMinimizationTableRow.toString());
    }

    @Test
    public void testGetSymbols() {
        DotLanguage dot = new DotLanguage(FSA_09);
        dot.setMachineType(MachineType.FSA);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> fsaPair = dot.toFSA();
        FiniteStateAutomaton fsa = fsaPair.first;
        State q0 = fsa.getState("q0");
        State q1 = fsa.getState("q1");

        assertEquals(new TreeSet<>(Arrays.asList(Symbols.LAMBDA)), fsa.getSymbols(q0, q1));
    }

    @Test
    public void testIsAFND() {
        DotLanguage dot = new DotLanguage(FSA_09);
        dot.setMachineType(MachineType.FSA);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> fsaPair = dot.toFSA();
        FiniteStateAutomaton fsa = fsaPair.first;

        assertEquals(true, fsa.isAFND());
    }

    @Test
    public void testSymbolsExternFromAlphabet() {
        DotLanguage dot = new DotLanguage(FSA_09);
        dot.setMachineType(MachineType.FSA);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> fsaPair = dot.toFSA();
        FiniteStateAutomaton fsa = fsaPair.first;

        assertEquals(new ArrayList<>(Arrays.asList("o", "l")), fsa.symbolsExternFromAlphabet("ola"));
    }

    static final String FSA_09_ALT = "digraph fsa_09 {\n" +
            "\n" +
            "\tdpi = 480;\n" +
            "\trankdir=LR;\n" +
            "\n" +
            "\t\"startRes\" [shape=point, style=\"invis\"];\n" +
            "\tnode [shape = doublecircle, color=\"#968D8D\", fontcolor=black, fillcolor=\"#FFCCCC\", style=\"filled\"];\n" +
            "\t\"q2\";\n" +
            "\n" +
            "\tnode [shape = circle];\n" +
            "\n" +
            "\t\"q0\" [label=<q<sub>0</sub>>, pos=\"3,3!\"];\n" +
            "\t\"q1\" [label=<q<sub>1</sub>>, pos=\"7,3!\"];\n" +
            "\t\"q2\" [label=<q<sub>2</sub>>, pos=\"5,6!\"];\n" +
            "\n" +
            "\t\"startRes\" -> \"q0\";\n" +
            "\n" +
            "\t\"q0\" -> \"q1\" [label=\"a\"];\n" +
            "\t\"q1\" -> \"q2\" [label=\"a\"];\n" +
            "\t\"q2\" -> \"q1\" [label=\"b\"];\n" +
            "\n" +
            "\n" +
            "}";

    @Test
    public void testGetTransitionFunctionsToCompleteAutomaton() {
        DotLanguage dot = new DotLanguage(FSA_09_ALT);
        dot.setMachineType(MachineType.FSA);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> fsaPair = dot.toFSA();
        FiniteStateAutomaton fsa = fsaPair.first;
        Set<FSATransitionFunction> transitionFunctions = new HashSet<>();
        State error = new State("err");
        State q0 = fsa.getState("q0");
        State q1 = fsa.getState("q1");
        State q2 = fsa.getState("q2");
        transitionFunctions.add(new FSATransitionFunction(error, "a", error));
        transitionFunctions.add(new FSATransitionFunction(error, "b", error));
        transitionFunctions.add(new FSATransitionFunction(q1, "b", error));
        transitionFunctions.add(new FSATransitionFunction(q2, "a", error));
        transitionFunctions.add(new FSATransitionFunction(q0, "b", error));

        assertEquals(transitionFunctions, fsa.getTransitionFunctionsToCompleteAutomaton());
    }
}
