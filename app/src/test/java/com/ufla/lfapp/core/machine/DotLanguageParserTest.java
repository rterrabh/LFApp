package com.ufla.lfapp.core.machine;

import android.support.v4.util.Pair;

import com.ufla.lfapp.core.machine.dotlang.DotLanguage;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.core.machine.pda.PushdownAutomatonExtend;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.core.machine.tm.TuringMachine;
import com.ufla.lfapp.core.machine.tm.var.TuringMachineMultiTape;
import com.ufla.lfapp.core.machine.tm.var.TuringMachineMultiTrack;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.utils.Symbols;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.crypto.Mac;

import static org.junit.Assert.assertEquals;

/**
 * Created by terra on 10/07/17.
 */

public class DotLanguageParserTest {

    private Map<State, MyPoint> stateToPoint;

    static {
        ResourcesContext.isTest = true;
        TMMove.test = true;
    }

    public FiniteStateAutomaton getFSA() {
        String[] statesStr = new String[] { "q0", "q1", "q2", "q3" };
        SortedSet<State> states = new TreeSet<>();
        this.stateToPoint = new HashMap<>();
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
                { "q0", "b", "q1" },
                { "q1", "a", "q1" },
                { "q1", "b", "q2" },
                { "q2", "b", "q2" },
                { "q2", "a", "q3" },
                { "q3", "a", "q3" },
                { "q3", "b", "q2" }
        };
        stateToPoint.put(statesMap.get("q0"), new MyPoint(2, 2));
        stateToPoint.put(statesMap.get("q1"), new MyPoint(5, 2));
        stateToPoint.put(statesMap.get("q2"), new MyPoint(8, 2));
        stateToPoint.put(statesMap.get("q3"), new MyPoint(11, 2));
        SortedSet<FSATransitionFunction> tf = new TreeSet<>();
        for (String[] tfS : tfStr) {
            tf.add(new FSATransitionFunction(statesMap.get(tfS[0]), tfS[1], statesMap.get(tfS[2])));
        }
        return new FiniteStateAutomaton(states, statesMap.get("q0"), finalStates, tf);
    }

    @Test
    public void test() {
        FiniteStateAutomaton fsa = getFSA();
        DotLanguage dot = new DotLanguage(fsa, stateToPoint);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> pairFSA = dot.toFSA();
        assertEquals(fsa, pairFSA.first);
        assertEquals(stateToPoint, pairFSA.second);

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

    @Test
    public void fsa0Test() {
        String a = "[B/B R, B/B R]";
        String[] params = a.split("[/, ]");
    }

    @Test
    public void fsa09Test() {
        DotLanguage dot = new DotLanguage(FSA_09);
        dot.setLabel("fsa_09");
        dot.setMachineType(MachineType.FSA);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> pairFSA = dot.toFSA();
        DotLanguage newDot = new DotLanguage(pairFSA.first, pairFSA.second);
        assertEquals(dot, dot);

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

    @Test
    public void fsa01Test() {
        DotLanguage dot = new DotLanguage(FSA_01);
        dot.setLabel("fsa_01");
        dot.setMachineType(MachineType.FSA);
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> pairFSA = dot.toFSA();
        DotLanguage newDot = new DotLanguage(pairFSA.first, pairFSA.second);
        assertEquals(dot, dot);

    }

    static String LBA_01 = "digraph lba_01 {\n" +
            "\n" +
            "\tdpi = 480;\n" +
            "\trankdir=LR;\n" +
            "\n" +
            "\t\"startRes\" [shape=point, style=\"invis\"];\n" +
            "\tnode [shape = doublecircle, color=\"#968D8D\", fontcolor=black, fillcolor=\"#FFCCCC\", style=\"filled\"];\n" +
            "\t\"q7\";\n" +
            "\n" +
            "\tnode [shape = circle];\n" +
            "\n" +
            "\t\"q0\" [label=<q<sub>0</sub>>, pos=\"3,3!\"];\n" +
            "\t\"q1\" [label=<q<sub>1</sub>>, pos=\"6,3!\"];\n" +
            "\t\"q2\" [label=<q<sub>2</sub>>, pos=\"9,3!\"];\n" +
            "\t\"q3\" [label=<q<sub>3</sub>>, pos=\"12,3!\"];\n" +
            "\t\"q4\" [label=<q<sub>4</sub>>, pos=\"15,3!\"];\n" +
            "\t\"q5\" [label=<q<sub>5</sub>>, pos=\"18,3!\"];\n" +
            "\t\"q6\" [label=<q<sub>6</sub>>, pos=\"6,6!\"];\n" +
            "\t\"q7\" [label=<q<sub>7</sub>>, pos=\"9,6!\"];\n" +
            "\n" +
            "\t\"startRes\" -> \"q0\";\n" +
            "\n" +
            "\t\"q0\" -> \"q1\" [label=\"</< R\"];\n" +
            "\t\"q1\" -> \"q2\" [label=\"a/X R\"];\n" +
            "\t\"q1\" -> \"q6\" [label=\"Y/Y R\"];\n" +
            "\t\"q2\" -> \"q2\" [label=\"Y/Y R\\na/a R\"];\n" +
            "\t\"q2\" -> \"q3\" [label=\"b/Y R\"];\n" +
            "\t\"q3\" -> \"q3\" [label=\"Z/Z R\\nb/b R\"];\n" +
            "\t\"q3\" -> \"q4\" [label=\"c/Z R\"];\n" +
            "\t\"q4\" -> \"q4\" [label=\"c/c R\"];\n" +
            "\t\"q4\" -> \"q5\" [label=\">/> L\"];\n" +
            "\t\"q5\" -> \"q5\" [label=\"c/c L\\nZ/Z L\\nb/b L\\nY/Y L\\na/a L\"];\n" +
            "\t\"q5\" -> \"q1\" [label=\"X/X R\"];\n" +
            "\t\"q6\" -> \"q6\" [label=\"Y/Y R\\nZ/Z R\"];\n" +
            "\t\"q6\" -> \"q7\" [label=\">/> L\"];\n" +
            "\t\"q7\" -> \"q7\" [label=\"Z/Z L\\nY/Y L\\nX/X L\"];\n" +
            "\n" +
            "\n" +
            "}\n";

    @Test
    public void lba01Test() {
        DotLanguage dot = new DotLanguage(LBA_01);
        dot.setLabel("lba_01");
        dot.setMachineType(MachineType.TM);
        Pair<TuringMachine, Map<State, MyPoint>> pairTM = dot.toTM();
        DotLanguage newDot = new DotLanguage(pairTM.first, pairTM.second);
        assertEquals(dot, dot);
    }

    static String TM_01 = "digraph tm_01 {\n" +
            "\n" +
            "\tdpi = 480;\n" +
            "\trankdir=LR;\n" +
            "\n" +
            "\t\"startRes\" [shape=point, style=\"invis\"];\n" +
            "\tnode [shape = doublecircle, color=\"#968D8D\", fontcolor=black, fillcolor=\"#FFCCCC\", style=\"filled\"];\n" +
            "\t\"q10\";\n" +
            "\n" +
            "\tnode [shape = circle];\n" +
            "\n" +
            "\t\"q0\" [label=<q<sub>0</sub>>, pos=\"3,6!\"];\n" +
            "\t\"q1\" [label=<q<sub>1</sub>>, pos=\"6,6!\"];\n" +
            "\t\"q2\" [label=<q<sub>2</sub>>, pos=\"8,3!\"];\n" +
            "\t\"q3\" [label=<q<sub>3</sub>>, pos=\"11,3!\"];\n" +
            "\t\"q4\" [label=<q<sub>4</sub>>, pos=\"14,6!\"];\n" +
            "\t\"q5\" [label=<q<sub>5</sub>>, pos=\"10,6!\"];\n" +
            "\t\"q6\" [label=<q<sub>6</sub>>, pos=\"8,9!\"];\n" +
            "\t\"q7\" [label=<q<sub>7</sub>>, pos=\"11,9!\"];\n" +
            "\t\"q8\" [label=<q<sub>8</sub>>, pos=\"6,9!\"];\n" +
            "\t\"q9\" [label=<q<sub>9</sub>>, pos=\"6,12!\"];\n" +
            "\t\"q10\" [label=<q<sub>10</sub>>, pos=\"9,12!\"];\n" +
            "\n" +
            "\t\"startRes\" -> \"q0\";\n" +
            "\n" +
            "\t\"q0\" -> \"q1\" [label=\"B/B R\"];\n" +
            "\t\"q1\" -> \"q2\" [label=\"a/X R\"];\n" +
            "\t\"q1\" -> \"q6\" [label=\"b/Y R\"];\n" +
            "\t\"q1\" -> \"q8\" [label=\"[/[ R\"];\n" +
            "\t\"q2\" -> \"q2\" [label=\"a/a R\\nb/b R\"];\n" +
            "\t\"q2\" -> \"q3\" [label=\"[/[ R\"];\n" +
            "\t\"q3\" -> \"q3\" [label=\"X/X R\\nY/Y R\"];\n" +
            "\t\"q3\" -> \"q4\" [label=\"a/X L\"];\n" +
            "\t\"q4\" -> \"q4\" [label=\"X/X L\\nY/Y L\"];\n" +
            "\t\"q4\" -> \"q5\" [label=\"[/[ L\"];\n" +
            "\t\"q5\" -> \"q5\" [label=\"a/a L\\nb/b L\"];\n" +
            "\t\"q5\" -> \"q1\" [label=\"X/X R\\nY/Y R\"];\n" +
            "\t\"q6\" -> \"q6\" [label=\"a/a R\\nb/b R\"];\n" +
            "\t\"q6\" -> \"q7\" [label=\"[/[ R\"];\n" +
            "\t\"q7\" -> \"q7\" [label=\"X/X R\\nY/Y R\"];\n" +
            "\t\"q7\" -> \"q4\" [label=\"b/Y L\"];\n" +
            "\t\"q8\" -> \"q8\" [label=\"X/X R\\nY/Y R\"];\n" +
            "\t\"q8\" -> \"q9\" [label=\"]/] R\"];\n" +
            "\t\"q9\" -> \"q10\" [label=\"B/B L\"];\n" +
            "\t\"q10\" -> \"q10\" [label=\"X/a L\\nY/b L\\n[/[ L\\n]/] L\"];\n" +
            "\n" +
            "\n" +
            "}\n";

    @Test
    public void tm01Test() {
        DotLanguage dot = new DotLanguage(TM_01);
        dot.setLabel("tm_01");
        dot.setMachineType(MachineType.TM);
        Pair<TuringMachine, Map<State, MyPoint>> pairTM = dot.toTM();
        DotLanguage newDot = new DotLanguage(pairTM.first, pairTM.second);
        assertEquals(dot, dot);

    }

    static String PDA_01 = "digraph pda_01 {\n" +
            "\n" +
            "\tdpi = 480;\n" +
            "\trankdir=LR;\n" +
            "\n" +
            "\t\"startRes\" [shape=point, style=\"invis\"];\n" +
            "\tnode [shape = doublecircle, color=\"#968D8D\", fontcolor=black, fillcolor=\"#FFCCCC\", style=\"filled\"];\n" +
            "\t\"q1\";\n" +
            "\n" +
            "\tnode [shape = circle];\n" +
            "\n" +
            "\t\"q0\" [label=<q<sub>0</sub>>, pos=\"2,3!\"];\n" +
            "\t\"q1\" [label=<q<sub>1</sub>>, pos=\"5,3!\"];\n" +
            "\n" +
            "\t\"startRes\" -> \"q0\";\n" +
            "\n" +
            "\t\"q0\" -> \"q0\" [label=\"a λ/X\"];\n" +
            "\t\"q0\" -> \"q1\" [label=\"b X/λ\"];\n" +
            "\t\"q1\" -> \"q1\" [label=\"b X/λ\"];\n" +
            "\n" +
            "\n" +
            "}\n";

    @Test
    public void pda01Test() {
        DotLanguage dot = new DotLanguage(PDA_01);
        dot.setLabel("pda_01");
        dot.setMachineType(MachineType.PDA);
        Pair<PushdownAutomaton, Map<State, MyPoint>> pairPDA = dot.toPDA();
        DotLanguage newDot = new DotLanguage(pairPDA.first, pairPDA.second);
        assertEquals(dot, dot);

    }

    static String PDA_EXT_01 = "digraph pdaExt_01 {\n" +
            "\n" +
            "\tdpi = 480;\n" +
            "\trankdir=LR;\n" +
            "\n" +
            "\t\"startRes\" [shape=point, style=\"invis\"];\n" +
            "\tnode [shape = doublecircle, color=\"#968D8D\", fontcolor=black, fillcolor=\"#FFCCCC\", style=\"filled\"];\n" +
            "\t\"q1\";\n" +
            "\n" +
            "\tnode [shape = circle];\n" +
            "\n" +
            "\t\"q0\" [label=<q<sub>0</sub>>, pos=\"2,3!\"];\n" +
            "\t\"q1\" [label=<q<sub>1</sub>>, pos=\"5,3!\"];\n" +
            "\n" +
            "\t\"startRes\" -> \"q0\";\n" +
            "\n" +
            "\t\"q0\" -> \"q1\" [label=\"a λ/SB\\na λ/B\"];\n" +
            "\t\"q1\" -> \"q1\" [label=\"a S/SB\\na S/B\\nb B/λ\"];\n" +
            "\n" +
            "\n" +
            "}\n";

    @Test
    public void pdaExt01Test() {
        DotLanguage dot = new DotLanguage(PDA_EXT_01);
        dot.setLabel("pdaExt_01");
        dot.setMachineType(MachineType.PDA_EXT);
        Pair<PushdownAutomatonExtend, Map<State, MyPoint>> pairTM = dot.toPDAExt();
        DotLanguage newDot = new DotLanguage(pairTM.first, pairTM.second);
        assertEquals(dot, dot);

    }

    static String TM_MULTI_TAPE_ENUM_01 = "digraph tm_multi_tape_enum_01 {\n" +
            "\n" +
            "\tdpi = 480;\n" +
            "\trankdir=LR;\n" +
            "\n" +
            "\t\"startRes\" [shape=point, style=\"invis\"];\n" +
            "\tnode [shape = doublecircle, color=\"#968D8D\", fontcolor=black, fillcolor=\"#FFCCCC\", style=\"filled\"];\n" +
            "\n" +
            "\n" +
            "\tnode [shape = circle];\n" +
            "\n" +
            "\t\"q0\" [label=<q<sub>0</sub>>, pos=\"3,3!\"];\n" +
            "\t\"q1\" [label=<q<sub>1</sub>>, pos=\"6,3!\"];\n" +
            "\t\"q2\" [label=<q<sub>2</sub>>, pos=\"9,3!\"];\n" +
            "\t\"q3\" [label=<q<sub>3</sub>>, pos=\"12,3!\"];\n" +
            "\t\"q4\" [label=<q<sub>4</sub>>, pos=\"12,6!\"];\n" +
            "\n" +
            "\t\"startRes\" -> \"q0\";\n" +
            "\n" +
            "\t\"q0\" -> \"q1\" [label=\"[B/B R, B/B R]\"];\n" +
            "\t\"q1\" -> \"q2\" [label=\"[B/# R, B/B S]\"];\n" +
            "\t\"q2\" -> \"q3\" [label=\"[B/# R, B/X S]\"];\n" +
            "\t\"q3\" -> \"q3\" [label=\"[B/a R, X/X L]\"];\n" +
            "\t\"q3\" -> \"q4\" [label=\"[B/B S, B/B R]\"];\n" +
            "\t\"q4\" -> \"q3\" [label=\"[B/# R, B/X S]\"];\n" +
            "\t\"q4\" -> \"q4\" [label=\"[B/b R, X/X R]\"];\n" +
            "\n" +
            "\n" +
            "}\n";

    @Test
    public void tmMultiTapeEnum01Test() {

        DotLanguage dot = new DotLanguage(TM_MULTI_TAPE_ENUM_01);
        dot.setLabel("tm_multi_tape_enum_01");
        dot.setMachineType(MachineType.TM_MULTI_TAPE);
        TMMove.test = true;
        Pair<TuringMachineMultiTape, Map<State, MyPoint>> pairTM = dot.toTMMultiTape();
        DotLanguage newDot = new DotLanguage(pairTM.first, pairTM.second);
        assertEquals(dot, dot);

    }

    static String TM_MULTI_TRACK_01 = "digraph tm_multi_track_01 {\n" +
            "\n" +
            "\tdpi = 480;\n" +
            "\trankdir=LR;\n" +
            "\n" +
            "\t\"startRes\" [shape=point, style=\"invis\"];\n" +
            "\tnode [shape = doublecircle, color=\"#968D8D\", fontcolor=black, fillcolor=\"#FFCCCC\", style=\"filled\"];\n" +
            "\t\"q4\";\n" +
            "\n" +
            "\tnode [shape = circle];\n" +
            "\n" +
            "\t\"q0\" [label=<q<sub>0</sub>>, pos=\"3,3!\"];\n" +
            "\t\"q1\" [label=<q<sub>1</sub>>, pos=\"6,3!\"];\n" +
            "\t\"q2\" [label=<q<sub>2</sub>>, pos=\"9,3!\"];\n" +
            "\t\"q3\" [label=<q<sub>3</sub>>, pos=\"12,3!\"];\n" +
            "\t\"q4\" [label=<q<sub>4</sub>>, pos=\"12,6!\"];\n" +
            "\n" +
            "\t\"startRes\" -> \"q0\";\n" +
            "\n" +
            "\t\"q0\" -> \"q1\" [label=\"[B/B B/B R]\"];\n" +
            "\t\"q1\" -> \"q2\" [label=\"[B/# B/B S]\"];\n" +
            "\t\"q2\" -> \"q3\" [label=\"[B/# B/X S]\"];\n" +
            "\t\"q3\" -> \"q3\" [label=\"[B/a X/X L]\"];\n" +
            "\t\"q3\" -> \"q4\" [label=\"[B/B B/B R]\"];\n" +
            "\t\"q4\" -> \"q3\" [label=\"[B/# B/X S]\"];\n" +
            "\t\"q4\" -> \"q4\" [label=\"[B/b X/X R]\"];\n" +
            "\n" +
            "\n" +
            "}";

    @Test
    public void tmMultiTrack01Test() {
        DotLanguage dot = new DotLanguage(TM_MULTI_TRACK_01);
        dot.setLabel("tm_multi_track_01");
        dot.setMachineType(MachineType.TM_MULTI_TRACK);
        Pair<TuringMachineMultiTrack, Map<State, MyPoint>> pairTM = dot.toTMMultiTrack();
        DotLanguage newDot = new DotLanguage(pairTM.first, pairTM.second);
        assertEquals(dot, dot);

    }

}
