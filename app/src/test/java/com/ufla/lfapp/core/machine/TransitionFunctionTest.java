package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.machine.fsa.FSAConfiguration;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.pda.PDAExtTransitionFunction;
import com.ufla.lfapp.core.machine.pda.PDATransitionFunction;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.core.machine.tm.TMTransitionFunction;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTapeTransitionFunction;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTrackTransitionFunction;
import com.ufla.lfapp.utils.ResourcesContext;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by carlos on 10/22/17.
 */

public class TransitionFunctionTest {

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    @Test
    public void testState() {
        State q0 = new State("q0''");
        String labelExpected = "q<sub>0</sub>''";
        assertEquals(labelExpected, q0.getLabel());
        State q0Copy = q0.copy();
        assertEquals(q0, q0Copy);
        State s = new State("s");
        assertEquals("s", s.getLabel());
        State q0t = new State("q0''t");
        assertEquals("q0''t", q0t.getLabel());
        State q0t2 = new State("q0t1''");
        assertEquals("q0t1''", q0t2.getLabel());
    }

    @Test
    public void testTapeEmptyCharUtils() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = TapeEmptyCharUtils.class.getDeclaredConstructor();
        assertFalse(constructor.isAccessible());
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testTransitionAtt() {
        assertEquals(TransitionAtt.CURRENT_STATE, TransitionAtt.CURRENT_STATE);
    }

    @Test
    public void testFSATransitionFunction() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String symbol = "a";
        FSATransitionFunction transitionFunction = new FSATransitionFunction(q0, symbol, q1);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(transitionFunction);
        assertEquals("q<sub><small>0</small></sub>", FSATransitionFunction.stateWithSpan(q0.getName()));
        assertTrue(transitionFunction.equalsCurrentState(q0));
        assertTrue(transitionFunction.equalsSymbol(symbol));
        transitionFunction2.setSymbol("b");
        transitionFunction2.setCurrentState(q0);
        transitionFunction2.setFutureState(q1);
        assertEquals("b", transitionFunction2.getSymbol());
        assertEquals("(q0, a) -> (q1)", transitionFunction.toString());

        assertEquals("q<sub><small>0</small></sub>", transitionFunction.getCurrentStateLabelWithSpan());
        assertEquals("q<sub><small>1</small></sub>", transitionFunction.getFutureStateLabelWithSpan());
    }

    @Test
    public void testFSATransitionFunctionEquals() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String symbol = "a";
        FSATransitionFunction transitionFunction = new FSATransitionFunction(q0, symbol, q1);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(transitionFunction);

        transitionFunction2.setCurrentState(q1);
        assertNotEquals(transitionFunction, transitionFunction2);

        assertEquals(transitionFunction, transitionFunction);
        assertNotEquals(transitionFunction, null);

        transitionFunction2.setCurrentState(q0);
        assertEquals(transitionFunction, transitionFunction2);

        PDATransitionFunction transitionFunction3 = new PDATransitionFunction(q0, symbol, q1, "", "");
        assertNotEquals(transitionFunction, transitionFunction3);
        assertEquals(-1, transitionFunction.compareTo(transitionFunction3));
    }

    @Test(expected = RuntimeException.class)
    public void testTMMoveException() {
        TMMove.getInstance("exception");
    }

    @Test
    public void testPDATransitionFunction() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String symbol = "a";
        String stacking = "a";
        String pops = "a";
        PDATransitionFunction transitionFunction = new PDATransitionFunction(q0, symbol, q1,
                stacking, pops);

        assertFalse(transitionFunction.popsLambda());
        assertTrue(transitionFunction.equalsPops(pops));
        assertEquals("δ(q0, a, a) = {[q1, a]}", transitionFunction.toString());
    }

    @Test
    public void testPDATransitionFunctionEquals() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String symbol = "a";
        String stacking = "a";
        String pops = "a";
        PDATransitionFunction transitionFunction = new PDATransitionFunction(q0, symbol, q1,
                stacking, pops);
        PDATransitionFunction transitionFunction1 = new PDATransitionFunction(transitionFunction);

        assertFalse(transitionFunction.popsLambda());
        assertTrue(transitionFunction.equalsPops(pops));
        assertEquals(transitionFunction, transitionFunction);
        assertNotEquals(transitionFunction, null);
        assertEquals(transitionFunction, transitionFunction1);
        transitionFunction1.setStacking("b");
        assertNotEquals(transitionFunction, transitionFunction1);
    }

    @Test
    public void testPDAExtTransitionFunctionToString() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String symbol = "a";
        List<String> stacking = new ArrayList<>();
        stacking.add("a");
        stacking.add("b");
        String pops = "a";
        PDAExtTransitionFunction transitionFunction = new PDAExtTransitionFunction(q0, symbol, q1,
                stacking, pops);

        assertEquals("δ(q0, a, a) = [ q1, ab]", transitionFunction.toString());

    }

    @Test
    public void testPDAExtTransitionFunctionEquals() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String symbol = "a";
        List<String> stacking = new ArrayList<>();
        stacking.add("a");
        stacking.add("b");
        String pops = "a";
        PDAExtTransitionFunction transitionFunction = new PDAExtTransitionFunction(q0, symbol, q1,
                stacking, pops);
        PDAExtTransitionFunction transitionFunction1 = new PDAExtTransitionFunction(q0, symbol, q1,
                new ArrayList<>(stacking), pops);

        assertEquals(transitionFunction, transitionFunction);
        assertNotEquals(transitionFunction, null);
        assertEquals(transitionFunction, transitionFunction1);
        transitionFunction1.getStacking().add("b");
        assertNotEquals(transitionFunction, transitionFunction1);
    }

    @Test
    public void testTMTransitionFunctionCompareTo() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String symbol = "a";
        String writeSymbol = "b";
        TMMove tmMove = TMMove.LEFT;
        TMTransitionFunction transitionFunction = new TMTransitionFunction(q0, symbol, q1,
                writeSymbol, tmMove);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(q0, symbol, q1);

        assertEquals(-1, transitionFunction.compareTo(transitionFunction2));
    }

    @Test
    public void testTMTransitionFunctionEquals() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String symbol = "a";
        String writeSymbol = "b";
        TMMove tmMove = TMMove.LEFT;
        TMTransitionFunction transitionFunction = new TMTransitionFunction(q0, symbol, q1,
                writeSymbol, tmMove);
        TMTransitionFunction transitionFunction1 = new TMTransitionFunction(q0, symbol, q1,
                writeSymbol, tmMove);

        assertEquals(transitionFunction, transitionFunction);
        assertNotEquals(transitionFunction, null);
        assertEquals(transitionFunction, transitionFunction1);
        transitionFunction1.setWriteSymbol("a");
        assertNotEquals(transitionFunction, transitionFunction1);
    }

    @Test(expected = RuntimeException.class)
    public void testTMMultiTrackTransitionFunctionInvalid1() {
        State q1 = new State("q1");
        new TMMultiTrackTransitionFunction(null, q1, new String[2], new String[2], TMMove.LEFT);
    }

    @Test(expected = RuntimeException.class)
    public void testTMMultiTrackTransitionFunctionInvalid2() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        new TMMultiTrackTransitionFunction(q0, q1, null, new String[2], TMMove.LEFT);
    }

    @Test(expected = RuntimeException.class)
    public void testTMMultiTrackTransitionFunctionInvalid3() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        new TMMultiTrackTransitionFunction(q0, q1, new String[3], new String[2], TMMove.LEFT);
    }

    @Test(expected = RuntimeException.class)
    public void testTMMultiTrackTransitionFunctionInvalid4() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        new TMMultiTrackTransitionFunction(q0, q1, new String[2], new String[2], TMMove.LEFT);
    }

    @Test(expected = RuntimeException.class)
    public void testTMMultiTrackTransitionFunctionInvalid5() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "a"};
        String[] writeSymbols = {"a", "a"};
        new TMMultiTrackTransitionFunction(q0, q1, readSymbols, writeSymbols, null);
    }

    @Test(expected = RuntimeException.class)
    public void testTMMultiTapeTransitionFunctionInvalid1() {
        State q1 = new State("q1");
        new TMMultiTapeTransitionFunction(null, q1, new String[2], new String[2], new TMMove[2]);
    }

    @Test(expected = RuntimeException.class)
    public void testTMMultiTapeTransitionFunctionInvalid2() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "a"};
        String[] writeSymbols = {"a", "a"};
        new TMMultiTapeTransitionFunction(q0, q1, readSymbols, writeSymbols, null);
    }

    @Test(expected = RuntimeException.class)
    public void testTMMultiTapeTransitionFunctionInvalid3() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "a"};
        String[] writeSymbols = {"a", "a"};
        new TMMultiTapeTransitionFunction(q0, q1, readSymbols, writeSymbols, new TMMove[3]);
    }

    @Test(expected = RuntimeException.class)
    public void testTMMultiTapeTransitionFunctionInvalid4() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "a"};
        String[] writeSymbols = {"a", "a"};
        new TMMultiTapeTransitionFunction(q0, q1, readSymbols, writeSymbols, new TMMove[2]);
    }

    @Test
    public void testTMMultiTrackTransitionFunctionHasNumTapes() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "b"};
        String[] writeSymbols = {"b", "a"};
        TMMultiTrackTransitionFunction transitionFunction =
                new TMMultiTrackTransitionFunction(q0, q1, readSymbols, writeSymbols, TMMove.LEFT);
        assertTrue(transitionFunction.hasNumTapes(2));
        assertFalse(transitionFunction.hasNumTapes(3));
    }

    @Test
    public void testTMMultiTapeTransitionFunctionHasNumTapes1() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "b"};
        String[] writeSymbols = {"b", "a"};
        TMMove[] moves2 = { TMMove.LEFT, TMMove.LEFT };
        TMMove[] moves3 = { TMMove.RIGHT, TMMove.LEFT, TMMove.LEFT };
        TMMultiTapeTransitionFunction transitionFunction =
                new TMMultiTapeTransitionFunction(q0, q1, readSymbols, writeSymbols, moves2);
        assertTrue(transitionFunction.hasNumTapes(2));
        assertFalse(transitionFunction.hasNumTapes(3));
        transitionFunction.setMoves(moves3);
        assertFalse(transitionFunction.hasNumTapes(2));
    }

    @Test
    public void testTMMultiTrackTransitionFunctionGetSymbols() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "b"};
        String[] writeSymbols = {"b", "a"};
        TMMultiTrackTransitionFunction transitionFunction =
                new TMMultiTrackTransitionFunction(q0, q1, readSymbols, writeSymbols, TMMove.LEFT);
        assertEquals("a/b b/a", transitionFunction.getSymbols());
        assertArrayEquals(readSymbols, transitionFunction.getReadSymbols());
    }

    @Test
    public void testTMMultiTrackTransitionFunctionEquals() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "b"};
        String[] writeSymbols = {"b", "a"};
        TMMultiTrackTransitionFunction transitionFunction =
                new TMMultiTrackTransitionFunction(q0, q1, readSymbols, writeSymbols, TMMove.LEFT);
        TMMultiTrackTransitionFunction transitionFunction1 =
                new TMMultiTrackTransitionFunction(q0, q1, readSymbols, writeSymbols, TMMove.LEFT);

        assertEquals(transitionFunction, transitionFunction);
        assertNotEquals(transitionFunction, null);
        assertEquals(transitionFunction, transitionFunction1);

        transitionFunction1.setReadSymbols(writeSymbols);
        assertNotEquals(transitionFunction, transitionFunction1);

        transitionFunction1.setReadSymbols(readSymbols);
        transitionFunction1.setWriteSymbols(readSymbols);
        assertNotEquals(transitionFunction, transitionFunction1);

        transitionFunction1.setWriteSymbols(writeSymbols);
        transitionFunction1.setMove(TMMove.RIGHT);
        assertNotEquals(transitionFunction, transitionFunction1);
    }

    @Test
    public void testTMMultiTrackTransitionFunctionToString() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "b"};
        String[] writeSymbols = {"b", "a"};
        TMMultiTrackTransitionFunction transitionFunction =
                new TMMultiTrackTransitionFunction(q0, q1, readSymbols, writeSymbols, TMMove.LEFT);
        Set<TMMultiTrackTransitionFunction> set = new HashSet<>();
        set.add(transitionFunction);
        assertEquals("δ(q0, [a, b]) = [q1, [b, a], L]", transitionFunction.toString());
    }

    @Test
    public void testTMMultiTrackTransitionFunctionCompareTo() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "b"};
        String[] writeSymbols = {"b", "a"};
        TMMultiTrackTransitionFunction transitionFunction =
                new TMMultiTrackTransitionFunction(q0, q1, readSymbols, writeSymbols, TMMove.LEFT);
        TMMultiTrackTransitionFunction transitionFunction1 =
                new TMMultiTrackTransitionFunction(q0, q1, 3);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(q0, "a", q1);

        assertEquals(-1, transitionFunction.compareTo(transitionFunction2));
        assertTrue(transitionFunction.compareTo(transitionFunction1) < 0);

        transitionFunction1.setReadSymbols(writeSymbols);
        transitionFunction1.setWriteSymbols(readSymbols);
        assertTrue(transitionFunction.compareTo(transitionFunction1) < 0);

        transitionFunction1.setReadSymbols(readSymbols);
        assertTrue(transitionFunction.compareTo(transitionFunction1) > 0);

        transitionFunction1.setWriteSymbols(writeSymbols);
        transitionFunction1.setMove(TMMove.LEFT);
        System.out.println(transitionFunction.compareTo(transitionFunction1));
        assertTrue(transitionFunction.compareTo(transitionFunction1) == 0);
    }

    @Test
    public void testTMMultiTapeTransitionFunctionToString() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "b"};
        String[] writeSymbols = {"b", "a"};
        TMMove[] moves = { TMMove.LEFT, TMMove.LEFT };
        TMMultiTapeTransitionFunction transitionFunction =
                new TMMultiTapeTransitionFunction(q0, q1, readSymbols, writeSymbols, moves);

        assertEquals("δ(q0, [a, b]) = [q1, [b, a], [L, L]]", transitionFunction.toString());
    }

    @Test
    public void testTMMultiTapeTransitionFunctionEquals() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "b"};
        String[] writeSymbols = {"b", "a"};
        TMMove[] moves = { TMMove.LEFT, TMMove.LEFT };
        TMMultiTapeTransitionFunction transitionFunction =
                new TMMultiTapeTransitionFunction(q0, q1, readSymbols, writeSymbols, moves);
        TMMultiTapeTransitionFunction transitionFunction1 =
                new TMMultiTapeTransitionFunction(q0, q1, readSymbols, writeSymbols, moves);


        assertEquals(transitionFunction, transitionFunction);
        assertNotEquals(transitionFunction, null);
        assertEquals(transitionFunction, transitionFunction1);

        transitionFunction1.setWriteSymbols(readSymbols);
        assertNotEquals(transitionFunction, transitionFunction1);
    }

    @Test
    public void testTMMultiTapeTransitionFunctionCompareTo() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String[] readSymbols = {"a", "b"};
        String[] writeSymbols = {"b", "a"};
        TMMove[] moves = { TMMove.LEFT, TMMove.LEFT };
        TMMove[] moves2 = { TMMove.RIGHT, TMMove.LEFT };
        TMMultiTapeTransitionFunction transitionFunction =
                new TMMultiTapeTransitionFunction(q0, q1, readSymbols, writeSymbols, moves);
        TMMultiTapeTransitionFunction transitionFunction1 =
                new TMMultiTapeTransitionFunction(q0, q1, 3);

        transitionFunction1.setReadSymbols(readSymbols);
        transitionFunction1.setWriteSymbols(writeSymbols);
        assertTrue(transitionFunction.compareTo(transitionFunction1) < 0);

        transitionFunction1.setMoves(moves2);
        assertTrue(transitionFunction.compareTo(transitionFunction1) > 0);

        transitionFunction1.setMoves(moves);
        assertTrue(transitionFunction.compareTo(transitionFunction1) == 0);
    }
}
