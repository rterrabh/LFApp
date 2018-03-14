package com.ufla.lfapp.core.machine;


import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.parser.TreeDerivation;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationParser;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationPosition;
import com.ufla.lfapp.core.machine.dotlang.DotLanguage;
import com.ufla.lfapp.core.machine.dotlang.Edge;
import com.ufla.lfapp.core.machine.dotlang.GraphAdapter;
import com.ufla.lfapp.core.machine.dotlang.IllegalMachineTypeException;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.utils.Symbols;


import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by carlos on 11/7/17.
 */

public class DotLanguageTest {

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    static final String FSA_01 = "digraph fsa_01 {\n" +
            "\n" +
            "\tdpi = 480;\n" +
            "\trankdir=LR;\n" +
            "\n" +
            "\t\"startRes\" [shape=point, style=\"invis\"];\n" +
            "\tnode [shape = doublecircle, color=\"#968D8D\", fontcolor=black, fillcolor=\"#FFCCCC\", style=\"filled\"];\n" +
            "\t\"q3\";\n" +
            "\n" +
            "\tnode [shape = circle];\n" +
            "\n" +
            "\t\"q0\" [label=<q<sub>0</sub>>, pos=\"2,3!\"];\n" +
            "\t\"q1\" [label=<q<sub>1</sub>>, pos=\"5,3!\"];\n" +
            "\t\"q2\" [label=<q<sub>2</sub>>, pos=\"8,3!\"];\n" +
            "\t\"q3\" [label=<q<sub>3</sub>>, pos=\"11,3!\"];\n" +
            "\n" +
            "\t\"startRes\" -> \"q0\";\n" +
            "\n" +
            "\t\"q0\" -> \"q1\" [label=\"a\"];\n" +
            "\t\"q1\" -> \"q1\" [label=\"a\"];\n" +
            "\t\"q1\" -> \"q2\" [label=\"b\"];\n" +
            "\t\"q2\" -> \"q3\" [label=\"a\"];\n" +
            "\t\"q2\" -> \"q2\" [label=\"b\"];\n" +
            "\t\"q3\" -> \"q3\" [label=\"a\"];\n" +
            "\t\"q3\" -> \"q2\" [label=\"b\"];\n" +
            "\n" +
            "\n" +
            "}\n";

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

    @Test
    public void testConstruct() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;
        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate);

        assertEquals(dot.getCreationDate(), creationDate);
        assertEquals(dot.getId(), id);
        assertEquals(dot.getGraph(), FSA_09);
        assertEquals(dot.getLabel(), label);
    }

    @Test
    public void testConstructWithMachineType() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;
        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);

        assertEquals(dot.getCreationDate(), creationDate);
        assertEquals(dot.getId(), id);
        assertEquals(dot.getGraph(), FSA_09);
        assertEquals(dot.getLabel(), label);
        assertEquals(dot.getMachineType(), MachineType.FSA);
    }

    @Test(expected = IllegalMachineTypeException.class)
    public void testIllegalMachineTypeExceptionFSA() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate);
        dot.toFSA();
    }

    @Test(expected = IllegalMachineTypeException.class)
    public void testIllegalMachineTypeExceptionPDA() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate);
        dot.toPDA();
    }

    @Test(expected = IllegalMachineTypeException.class)
    public void testIllegalMachineTypeExceptionPDAExt() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate);
        dot.toPDAExt();
    }

    @Test(expected = IllegalMachineTypeException.class)
    public void testIllegalMachineTypeExceptionTM() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate);
        dot.toTM();
    }

    @Test(expected = IllegalMachineTypeException.class)
    public void testIllegalMachineTypeExceptionTMMultiTape() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate);
        dot.toTMMultiTape();
    }

    @Test(expected = IllegalMachineTypeException.class)
    public void testIllegalMachineTypeExceptionTMMultiTrack() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate);
        dot.toTMMultiTrack();
    }

    @Test
    public void testToString() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);

        assertEquals(dot.toString(), FSA_09);
    }

    @Test
    public void testEquals_01() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);

        assertFalse(dot.equals(null));
    }

    @Test
    public void testEquals_02() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);

        assertFalse(dot.equals("str"));
    }

    @Test
    public void testEquals_03() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);
        DotLanguage dot2 = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);

        assertTrue(dot.equals(dot2));
    }

    @Test
    public void testEquals_04() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);
        DotLanguage dot2 = new DotLanguage(1, FSA_09, label, contUses, creationDate, MachineType.FSA);

        assertFalse(dot.equals(dot2));
    }

    @Test
    public void testEquals_05() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);
        DotLanguage dot2 = new DotLanguage(id, FSA_09, "fsa", contUses, creationDate, MachineType.FSA);

        assertFalse(dot.equals(dot2));
    }

    @Test
    public void testEquals_06() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);
        DotLanguage dot2 = new DotLanguage(id, FSA_09, label, 2, creationDate, MachineType.FSA);

        assertFalse(dot.equals(dot2));
    }

    @Test
    public void testEquals_07() {
        Date creationDate = new Date();
        Date creationDate2 = new Date(10000);
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);
        DotLanguage dot2 = new DotLanguage(id, FSA_09, label, contUses, creationDate2, MachineType.FSA);

        assertFalse(dot.equals(dot2));
    }

    @Test
    public void testEquals_08() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);
        DotLanguage dot2 = new DotLanguage(id, FSA_01, label, contUses, creationDate, MachineType.FSA);

        assertFalse(dot.equals(dot2));
    }

    @Test
    public void testEquals_09() {
        Date creationDate = new Date();
        Date creationDate2 = new Date(10000);
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);
        DotLanguage dot2 = new DotLanguage(id, FSA_09, label, contUses, creationDate2, MachineType.PDA);

        assertFalse(dot.equals(dot2));
    }

    @Test
    public void testHashCode() {
        int hashCodeExpected = 1588730164;

        DotLanguage dot = new DotLanguage(FSA_09);

        assertEquals(hashCodeExpected, dot.hashCode());
    }

    @Test
    public void testEdgeHashCode() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        Edge edge = new Edge(q0, q1, Symbols.LAMBDA);
        int hashCodeExpected = 3523578;

        assertEquals(hashCodeExpected, edge.hashCode());
    }

    @Test
    public void testToGraphAdapter() {
        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);
        GraphAdapter graphAdapter = dot.toGraphAdapter();

        GraphAdapter graphAdapterExpected = new GraphAdapter();
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        State q3 = new State("q3");
        graphAdapterExpected.startState = q0;
        graphAdapterExpected.dotLanguage = dot;
        graphAdapterExpected.stateFinals.add(q2);
        graphAdapterExpected.stateFinals.add(q3);
        graphAdapterExpected.stateSet.add(q0);
        graphAdapterExpected.stateSet.add(q1);
        graphAdapterExpected.stateSet.add(q2);
        graphAdapterExpected.stateSet.add(q3);
        graphAdapterExpected.edgeList.add(new Edge(q0, q1, Symbols.LAMBDA));
        graphAdapterExpected.edgeList.add(new Edge(q0, q3, Symbols.LAMBDA));
        graphAdapterExpected.edgeList.add(new Edge(q1, q2, "a"));
        graphAdapterExpected.edgeList.add(new Edge(q2, q1, "b"));
        graphAdapterExpected.edgeList.add(new Edge(q3, q3, "a"));
        graphAdapterExpected.stateMyPointMap.put(q0, new MyPoint(3, 3));
        graphAdapterExpected.stateMyPointMap.put(q1, new MyPoint(7, 3));
        graphAdapterExpected.stateMyPointMap.put(q2, new MyPoint(5, 6));
        graphAdapterExpected.stateMyPointMap.put(q3, new MyPoint(5, 6));

        assertEquals(graphAdapterExpected.startState, graphAdapter.startState);
        assertEquals(graphAdapterExpected.dotLanguage, graphAdapter.dotLanguage);
        assertEquals(graphAdapterExpected.stateFinals, graphAdapter.stateFinals);
        assertEquals(graphAdapterExpected.stateSet, graphAdapter.stateSet);
        assertEquals(graphAdapterExpected.edgeList, graphAdapter.edgeList);
        assertEquals(graphAdapterExpected.stateMyPointMap, graphAdapter.stateMyPointMap);
    }

    static final String FSA_09_ALT_OLD = "digraph untitled {\n" +
            "\tq0 [pos=3,3] [style=initial/final];\n" +
            "\tq1 [pos=7,3];\n" +
            "\tq2 [pos=5,6];\n" +
            "\tq3 [pos=5,6] [style=final];\n" +
            "\tq0 -> q1 [label=λ];\n" +
            "\tq0 -> q3 [label=λ];\n" +
            "\tq1 -> q2 [label=a];\n" +
            "\tq2 -> q1 [label=b];\n" +
            "\tq3 -> q3 [label=a];\n" +
            "}\n";

    @Test
    public void testDotLanguageOld() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        State q3 = new State("q3");
        FSATransitionFunction tf0 = new FSATransitionFunction(q0, Symbols.LAMBDA, q1);
        FSATransitionFunction tf1 = new FSATransitionFunction(q0, Symbols.LAMBDA, q3);
        FSATransitionFunction tf2 = new FSATransitionFunction(q1, "a", q2);
        FSATransitionFunction tf3 = new FSATransitionFunction(q2, "b", q1);
        FSATransitionFunction tf4 = new FSATransitionFunction(q3, "a", q3);
        SortedSet<State> states = new TreeSet<>(Arrays.asList(q0, q1, q2, q3));
        SortedSet<State> finalStates = new TreeSet<>(Arrays.asList(q0, q3));
        SortedSet<FSATransitionFunction> transitionFunctions = new TreeSet<>(Arrays.asList(tf0, tf1, tf2, tf3, tf4));
        FiniteStateAutomaton finiteStateAutomaton = new FiniteStateAutomaton(states, q0, finalStates, transitionFunctions);
        Map<State, MyPoint> stateMyPointMap = new HashMap<>();
        stateMyPointMap.put(q0, new MyPoint(3, 3));
        stateMyPointMap.put(q1, new MyPoint(7, 3));
        stateMyPointMap.put(q2, new MyPoint(5, 6));
        stateMyPointMap.put(q3, new MyPoint(5, 6));

        FiniteStateAutomatonGUI automatonGUI = new FiniteStateAutomatonGUI(finiteStateAutomaton, stateMyPointMap);

        DotLanguage dot = DotLanguage.parseDotLanguageOld(automatonGUI);

        assertEquals(FSA_09_ALT_OLD, dot.getGraph());
    }

    static final String FSA_09_OLD = "digraph untitled {\n" +
            "\tq0 [pos=3,3] [style=initial];\n" +
            "\tq1 [pos=7,3];\n" +
            "\tq2 [pos=5,6] [style=final];\n" +
            "\tq3 [pos=5,6] [style=final];\n" +
            "\tq0 -> q1 [label=λ];\n" +
            "\tq0 -> q3 [label=λ];\n" +
            "\tq1 -> q2 [label=a];\n" +
            "\tq2 -> q1 [label=b];\n" +
            "\tq3 -> q3 [label=a];\n" +
            "}\n";

    @Test
    public void testDotLanguageOld2() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        State q3 = new State("q3");
        FSATransitionFunction tf0 = new FSATransitionFunction(q0, Symbols.LAMBDA, q1);
        FSATransitionFunction tf1 = new FSATransitionFunction(q0, Symbols.LAMBDA, q3);
        FSATransitionFunction tf2 = new FSATransitionFunction(q1, "a", q2);
        FSATransitionFunction tf3 = new FSATransitionFunction(q2, "b", q1);
        FSATransitionFunction tf4 = new FSATransitionFunction(q3, "a", q3);
        SortedSet<State> states = new TreeSet<>(Arrays.asList(q0, q1, q2, q3));
        SortedSet<State> finalStates = new TreeSet<>(Arrays.asList(q2, q3));
        SortedSet<FSATransitionFunction> transitionFunctions = new TreeSet<>(Arrays.asList(tf0, tf1, tf2, tf3, tf4));
        FiniteStateAutomaton finiteStateAutomaton = new FiniteStateAutomaton(states, q0, finalStates, transitionFunctions);
        Map<State, MyPoint> stateMyPointMap = new HashMap<>();
        stateMyPointMap.put(q0, new MyPoint(3, 3));
        stateMyPointMap.put(q1, new MyPoint(7, 3));
        stateMyPointMap.put(q2, new MyPoint(5, 6));
        stateMyPointMap.put(q3, new MyPoint(5, 6));

        FiniteStateAutomatonGUI automatonGUI = new FiniteStateAutomatonGUI(finiteStateAutomaton, stateMyPointMap);

        DotLanguage dot = DotLanguage.parseDotLanguageOld(automatonGUI);

        assertEquals(FSA_09_OLD, dot.getGraph());
    }

    @Test
    public void testToAutomatonGuiOld() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        State q3 = new State("q3");
        FSATransitionFunction tf0 = new FSATransitionFunction(q0, Symbols.LAMBDA, q1);
        FSATransitionFunction tf1 = new FSATransitionFunction(q0, Symbols.LAMBDA, q3);
        FSATransitionFunction tf2 = new FSATransitionFunction(q1, "a", q2);
        FSATransitionFunction tf3 = new FSATransitionFunction(q2, "b", q1);
        FSATransitionFunction tf4 = new FSATransitionFunction(q3, "a", q3);
        SortedSet<State> states = new TreeSet<>(Arrays.asList(q0, q1, q2, q3));
        SortedSet<State> finalStates = new TreeSet<>(Arrays.asList(q0, q3));
        SortedSet<FSATransitionFunction> transitionFunctions = new TreeSet<>(Arrays.asList(tf0, tf1, tf2, tf3, tf4));
        FiniteStateAutomaton finiteStateAutomaton = new FiniteStateAutomaton(states, q0, finalStates, transitionFunctions);
        Map<State, MyPoint> stateMyPointMap = new HashMap<>();
        stateMyPointMap.put(q0, new MyPoint(3, 3));
        stateMyPointMap.put(q1, new MyPoint(7, 3));
        stateMyPointMap.put(q2, new MyPoint(5, 6));
        stateMyPointMap.put(q3, new MyPoint(5, 6));

        FiniteStateAutomatonGUI automatonGUIExpected = new FiniteStateAutomatonGUI(finiteStateAutomaton, stateMyPointMap);

        DotLanguage dot = DotLanguage.parseDotLanguageOld(automatonGUIExpected);

        FiniteStateAutomatonGUI automatonGUI = dot.toAutomatonGUI();

        assertEquals(automatonGUIExpected.states, automatonGUI.states);
        assertEquals(automatonGUIExpected.initialState, automatonGUI.initialState);
        assertEquals(automatonGUIExpected.finalStates, automatonGUI.finalStates);
        assertEquals(automatonGUIExpected.getTransitionFunctions(), automatonGUI.getTransitionFunctions());
    }

    @Test
    public void testNewAutomatonGui() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        State q3 = new State("q3");
        FSATransitionFunction tf0 = new FSATransitionFunction(q0, Symbols.LAMBDA, q1);
        FSATransitionFunction tf1 = new FSATransitionFunction(q0, Symbols.LAMBDA, q3);
        FSATransitionFunction tf2 = new FSATransitionFunction(q1, "a", q2);
        FSATransitionFunction tf3 = new FSATransitionFunction(q2, "b", q1);
        FSATransitionFunction tf4 = new FSATransitionFunction(q3, "a", q3);
        SortedSet<State> states = new TreeSet<>(Arrays.asList(q0, q1, q2, q3));
        SortedSet<State> finalStates = new TreeSet<>(Arrays.asList(q2, q3));
        SortedSet<FSATransitionFunction> transitionFunctions = new TreeSet<>(Arrays.asList(tf0, tf1, tf2, tf3, tf4));
        FiniteStateAutomaton finiteStateAutomaton = new FiniteStateAutomaton(states, q0, finalStates, transitionFunctions);
        Map<State, MyPoint> stateMyPointMap = new HashMap<>();
        stateMyPointMap.put(q0, new MyPoint(3, 3));
        stateMyPointMap.put(q1, new MyPoint(7, 3));
        stateMyPointMap.put(q2, new MyPoint(5, 6));
        stateMyPointMap.put(q3, new MyPoint(5, 6));

        FiniteStateAutomatonGUI automatonGUIExpected = new FiniteStateAutomatonGUI(finiteStateAutomaton, stateMyPointMap);

        Date creationDate = new Date();
        Long id = 0l;
        String label = "fsa_09";
        Integer contUses = 1;

        DotLanguage dot = new DotLanguage(id, FSA_09, label, contUses, creationDate, MachineType.FSA);

        FiniteStateAutomatonGUI automatonGUI = dot.newToAutomatonGUI();

        assertEquals(automatonGUIExpected.states, automatonGUI.states);
        assertEquals(automatonGUIExpected.initialState, automatonGUI.initialState);
        assertEquals(automatonGUIExpected.finalStates, automatonGUI.finalStates);
        assertEquals(automatonGUIExpected.getTransitionFunctions(), automatonGUI.getTransitionFunctions());
    }

    private static String TREE_DERIVATION_00 = "digraph untitled {\n" +
            "\tX [pos=0,0];\n" +
            "\tA [pos=0,0];\n" +
            "\tX [pos=0,0];\n" +
            "\tX [pos=0,0];\n" +
            "\ta [pos=0,0];\n" +
            "\tX [pos=0,0];\n" +
            "\tX [pos=0,0];\n" +
            "\tX [pos=0,0];\n" +
            "\tb [pos=0,0];\n" +
            "\tX [pos=0,0];\n" +
            "\tλ [pos=0,0];\n" +
            "\tλ [pos=0,0];\n" +
            "\tλ [pos=0,0];\n" +
            "\tX -> A;\n" +
            "\tA -> X;\n" +
            "\tX -> X;\n" +
            "\tX -> a;\n" +
            "\tX -> X;\n" +
            "\tX -> X;\n" +
            "\tX -> X;\n" +
            "\tX -> b;\n" +
            "\tX -> X;\n" +
            "\tX -> λ;\n" +
            "\tX -> λ;\n" +
            "\tX -> λ;\n" +
            "}\n";

    @Test
    public void testTreeDerivation() {
        Grammar grammar = new Grammar("X -> XaX | XbX | c | X | A | " + Grammar.LAMBDA + "\n A -> X");
        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar, "ab");
        treeDerivationParser.parser();
        TreeDerivation treeDerivation = treeDerivationParser.getTreeDerivation();
        TreeDerivationPosition treeDerivationPosition = new TreeDerivationPosition(treeDerivation.getRoot());



        DotLanguage dot = DotLanguage.parseDotLanguage(treeDerivationPosition);

        assertEquals(TREE_DERIVATION_00, dot.getGraph());

    }

}
