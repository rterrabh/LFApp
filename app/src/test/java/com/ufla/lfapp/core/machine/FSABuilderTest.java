package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonBuilder;
import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by carlos on 10/22/17.
 */

public class FSABuilderTest {

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    @Test
    public void testFSABuilder() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String symbol = "a";
        FSATransitionFunction transitionFunction1 = new FSATransitionFunction(q0, symbol, q1);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(q0, "b", q1);
        FiniteStateAutomatonBuilder fsaBuilder = new FiniteStateAutomatonBuilder();
        FiniteStateAutomaton fsa = fsaBuilder.addTransition(transitionFunction1)
                .addTransition(q0, "b", q1)
                .withInitialState(q0)
                .defineFinalState(q1)
                .createAutomaton();


        Set<FSATransitionFunction> transitionFunctionSet = new HashSet<>();
        transitionFunctionSet.add(transitionFunction1);
        transitionFunctionSet.add(transitionFunction2);
        Set<State> states = new HashSet<>();
        states.add(q0);
        states.add(q1);

        assertEquals(transitionFunctionSet, fsa.getTransitionFunctions());
        assertEquals(states, fsa.getStates());
        assertEquals(q0, fsa.getInitialState());
        assertEquals(1, fsa.getFinalStates().size());
        assertTrue(fsa.getFinalStates().contains(q1));
    }

    @Test
    public void testFSABuilderFromMachine() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        FSATransitionFunction transitionFunction1 = new FSATransitionFunction(q0, "a", q1);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(q0, "b", q1);
        FSATransitionFunction transitionFunction3 = new FSATransitionFunction(q0, "a", q2);
        FiniteStateAutomatonBuilder fsaBuilder = new FiniteStateAutomatonBuilder();
        FiniteStateAutomaton fsa = fsaBuilder.addTransition(transitionFunction1)
                .addTransition(transitionFunction2)
                .addTransition(q0, "b", q2)
                .addTransition(q2, "a", q1)
                .withInitialState(q0)
                .defineFinalState(q1)
                .removeState(q2)
                .createAutomaton();

        fsa = new FiniteStateAutomatonBuilder(fsa).addTransition(transitionFunction3)
                .createAutomaton();


        Set<FSATransitionFunction> transitionFunctionSet = new HashSet<>();
        transitionFunctionSet.add(transitionFunction1);
        transitionFunctionSet.add(transitionFunction2);
        transitionFunctionSet.add(transitionFunction3);
        Set<State> states = new HashSet<>();
        states.add(q0);
        states.add(q1);
        states.add(q2);

        assertEquals(transitionFunctionSet, fsa.getTransitionFunctions());
        assertEquals(states, fsa.getStates());
        assertEquals(q0, fsa.getInitialState());
        assertEquals(1, fsa.getFinalStates().size());
        assertTrue(fsa.getFinalStates().contains(q1));
    }

    @Test
    public void testFSABuilderGetState() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        String symbol = "a";
        FSATransitionFunction transitionFunction1 = new FSATransitionFunction(q0, symbol, q1);
        FiniteStateAutomatonBuilder fsaBuilder = new FiniteStateAutomatonBuilder();
        fsaBuilder.addTransition(transitionFunction1);

        assertTrue(q0 == fsaBuilder.getState("q0"));
        assertTrue(null == fsaBuilder.getState("q2"));
    }

    @Test
    public void testFSABuilderRemoveState() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        FSATransitionFunction transitionFunction1 = new FSATransitionFunction(q0, "a", q1);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(q0, "b", q1);
        FiniteStateAutomatonBuilder fsaBuilder = new FiniteStateAutomatonBuilder();
        FiniteStateAutomaton fsa = fsaBuilder.addTransition(transitionFunction1)
                .addTransition(transitionFunction2)
                .addTransition(q0, "b", q2)
                .addTransition(q2, "a", q1)
                .withInitialState(q0)
                .defineFinalState(q1)
                .removeState(q2)
                .createAutomaton();


        Set<FSATransitionFunction> transitionFunctionSet = new HashSet<>();
        transitionFunctionSet.add(transitionFunction1);
        transitionFunctionSet.add(transitionFunction2);
        Set<State> states = new HashSet<>();
        states.add(q0);
        states.add(q1);

        assertEquals(transitionFunctionSet, fsa.getTransitionFunctions());
        assertEquals(states, fsa.getStates());
        assertEquals(q0, fsa.getInitialState());
        assertEquals(1, fsa.getFinalStates().size());
        assertTrue(fsa.getFinalStates().contains(q1));
    }

    @Test
    public void testFSABuilderRemoveInitialState() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        FSATransitionFunction transitionFunction1 = new FSATransitionFunction(q0, "a", q1);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(q0, "b", q1);
        FSATransitionFunction transitionFunction3 = new FSATransitionFunction(q0, "b", q2);
        FSATransitionFunction transitionFunction4 = new FSATransitionFunction(q2, "a", q1);
        FiniteStateAutomatonBuilder fsaBuilder = new FiniteStateAutomatonBuilder();
        FiniteStateAutomaton fsa = fsaBuilder.addTransition(transitionFunction1)
                .addTransition(transitionFunction2)
                .addTransition(transitionFunction3)
                .addTransition(transitionFunction4)
                .withInitialState(q0)
                .defineFinalState(q1)
                .removeState(q0)
                .createAutomaton();


        Set<FSATransitionFunction> transitionFunctionSet = new HashSet<>();
        transitionFunctionSet.add(transitionFunction4);
        Set<State> states = new HashSet<>();
        states.add(q1);
        states.add(q2);

        assertEquals(transitionFunctionSet, fsa.getTransitionFunctions());
        assertEquals(states, fsa.getStates());
        assertEquals(null, fsa.getInitialState());
        assertEquals(1, fsa.getFinalStates().size());
        assertTrue(fsa.getFinalStates().contains(q1));
    }

    @Test(expected = RuntimeException.class)
    public void testFSABuilderRemoveTransitionsWhereException() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        TransitionAtt[] transitionAtts = new TransitionAtt[1];
        String[] transitionAttValue = new String[3];
        FSATransitionFunction transitionFunction1 = new FSATransitionFunction(q0, "a", q1);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(q0, "b", q1);
        FSATransitionFunction transitionFunction3 = new FSATransitionFunction(q0, "b", q2);
        FSATransitionFunction transitionFunction4 = new FSATransitionFunction(q2, "a", q1);
        FiniteStateAutomatonBuilder fsaBuilder = new FiniteStateAutomatonBuilder();
        fsaBuilder.addTransition(transitionFunction1)
                .addTransition(transitionFunction2)
                .addTransition(transitionFunction3)
                .addTransition(transitionFunction4)
                .withInitialState(q0)
                .defineFinalState(q1)
                .removeTransitionsWhere(transitionAtts, transitionAttValue)
                .createAutomaton();
    }

    @Test
    public void testFSABuilderRemoveTransitionsWhere1() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        TransitionAtt[] transitionAtts = { TransitionAtt.CURRENT_STATE };
        String[] transitionAttValue = { "q0" };
        FSATransitionFunction transitionFunction1 = new FSATransitionFunction(q0, "a", q1);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(q0, "b", q1);
        FSATransitionFunction transitionFunction3 = new FSATransitionFunction(q0, "b", q2);
        FSATransitionFunction transitionFunction4 = new FSATransitionFunction(q2, "a", q1);
        FiniteStateAutomatonBuilder fsaBuilder = new FiniteStateAutomatonBuilder();
        FiniteStateAutomaton fsa = fsaBuilder.addTransition(transitionFunction1)
                .addTransition(transitionFunction2)
                .addTransition(transitionFunction3)
                .addTransition(transitionFunction4)
                .withInitialState(q0)
                .defineFinalState(q1)
                .removeTransitionsWhere(transitionAtts, transitionAttValue)
                .createAutomaton();


        Set<FSATransitionFunction> transitionFunctionSet = new HashSet<>();
        transitionFunctionSet.add(transitionFunction4);
        Set<State> states = new HashSet<>();
        states.add(q1);
        states.add(q2);

        assertEquals(transitionFunctionSet, fsa.getTransitionFunctions());
        assertEquals(states, fsa.getStates());
        assertEquals(null, fsa.getInitialState());
        assertEquals(1, fsa.getFinalStates().size());
        assertTrue(fsa.getFinalStates().contains(q1));
    }

    @Test
    public void testFSABuilderRemoveTransitionsWhere2() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        TransitionAtt[] transitionAtts = { TransitionAtt.FUTURE_STATE };
        String[] transitionAttValue = { "q1" };
        FSATransitionFunction transitionFunction1 = new FSATransitionFunction(q0, "a", q1);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(q0, "b", q1);
        FSATransitionFunction transitionFunction3 = new FSATransitionFunction(q0, "b", q2);
        FSATransitionFunction transitionFunction4 = new FSATransitionFunction(q2, "a", q1);
        FiniteStateAutomatonBuilder fsaBuilder = new FiniteStateAutomatonBuilder();
        FiniteStateAutomaton fsa = fsaBuilder.addTransition(transitionFunction1)
                .addTransition(transitionFunction2)
                .addTransition(transitionFunction3)
                .addTransition(transitionFunction4)
                .withInitialState(q0)
                .defineFinalState(q1)
                .removeTransitionsWhere(transitionAtts, transitionAttValue)
                .createAutomaton();


        Set<FSATransitionFunction> transitionFunctionSet = new HashSet<>();
        transitionFunctionSet.add(transitionFunction3);
        Set<State> states = new HashSet<>();
        states.add(q0);
        states.add(q2);

        assertEquals(transitionFunctionSet, fsa.getTransitionFunctions());
        assertEquals(states, fsa.getStates());
        assertEquals(q0, fsa.getInitialState());
        assertEquals(0, fsa.getFinalStates().size());
    }

    @Test
    public void testFSABuilderRemoveTransitionsWhere3() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        TransitionAtt[] transitionAtts = { TransitionAtt.SYMBOL };
        String[] transitionAttValue = { "a" };
        FSATransitionFunction transitionFunction1 = new FSATransitionFunction(q0, "a", q1);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(q0, "b", q1);
        FSATransitionFunction transitionFunction3 = new FSATransitionFunction(q0, "b", q2);
        FSATransitionFunction transitionFunction4 = new FSATransitionFunction(q2, "a", q1);
        FiniteStateAutomatonBuilder fsaBuilder = new FiniteStateAutomatonBuilder();
        FiniteStateAutomaton fsa = fsaBuilder.addTransition(transitionFunction1)
                .addTransition(transitionFunction2)
                .addTransition(transitionFunction3)
                .addTransition(transitionFunction4)
                .withInitialState(q0)
                .defineFinalState(q1)
                .removeTransitionsWhere(transitionAtts, transitionAttValue)
                .createAutomaton();


        Set<FSATransitionFunction> transitionFunctionSet = new HashSet<>();
        transitionFunctionSet.add(transitionFunction2);
        transitionFunctionSet.add(transitionFunction3);
        Set<State> states = new HashSet<>();
        states.add(q0);
        states.add(q1);
        states.add(q2);

        assertEquals(transitionFunctionSet, fsa.getTransitionFunctions());
        assertEquals(states, fsa.getStates());
        assertEquals(q0, fsa.getInitialState());
        assertEquals(1, fsa.getFinalStates().size());
        assertTrue(fsa.getFinalStates().contains(q1));
    }

    @Test
    public void testFSABuilderRemoveTransitionsWhere4() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        TransitionAtt[] transitionAtts = { TransitionAtt.CURRENT_STATE, TransitionAtt.FUTURE_STATE };
        String[] transitionAttValue = { "q0", "q1" };
        FSATransitionFunction transitionFunction1 = new FSATransitionFunction(q0, "a", q1);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(q0, "b", q1);
        FSATransitionFunction transitionFunction3 = new FSATransitionFunction(q0, "b", q2);
        FSATransitionFunction transitionFunction4 = new FSATransitionFunction(q2, "a", q1);
        FiniteStateAutomatonBuilder fsaBuilder = new FiniteStateAutomatonBuilder();
        FiniteStateAutomaton fsa = fsaBuilder.addTransition(transitionFunction1)
                .addTransition(transitionFunction2)
                .addTransition(transitionFunction3)
                .addTransition(transitionFunction4)
                .withInitialState(q0)
                .defineFinalState(q1)
                .removeTransitionsWhere(transitionAtts, transitionAttValue)
                .createAutomaton();


        Set<FSATransitionFunction> transitionFunctionSet = new HashSet<>();
        transitionFunctionSet.add(transitionFunction3);
        transitionFunctionSet.add(transitionFunction4);
        Set<State> states = new HashSet<>();
        states.add(q0);
        states.add(q1);
        states.add(q2);

        assertEquals(transitionFunctionSet, fsa.getTransitionFunctions());
        assertEquals(states, fsa.getStates());
        assertEquals(q0, fsa.getInitialState());
        assertEquals(1, fsa.getFinalStates().size());
        assertTrue(fsa.getFinalStates().contains(q1));
    }

    @Test
    public void testFSABuilderRemoveTransitionsWhere5() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        State q2 = new State("q2");
        TransitionAtt[] transitionAtts = { TransitionAtt.CURRENT_STATE, TransitionAtt.SYMBOL,
                TransitionAtt.FUTURE_STATE };
        String[] transitionAttValue = { "q0", "a", "q1" };
        FSATransitionFunction transitionFunction1 = new FSATransitionFunction(q0, "a", q1);
        FSATransitionFunction transitionFunction2 = new FSATransitionFunction(q0, "b", q1);
        FSATransitionFunction transitionFunction3 = new FSATransitionFunction(q0, "b", q2);
        FSATransitionFunction transitionFunction4 = new FSATransitionFunction(q2, "a", q1);
        FiniteStateAutomatonBuilder fsaBuilder = new FiniteStateAutomatonBuilder();
        FiniteStateAutomaton fsa = fsaBuilder.addTransition(transitionFunction1)
                .addTransition(transitionFunction2)
                .addTransition(transitionFunction3)
                .addTransition(transitionFunction4)
                .withInitialState(q0)
                .defineFinalState(q1)
                .removeTransitionsWhere(transitionAtts, transitionAttValue)
                .createAutomaton();


        Set<FSATransitionFunction> transitionFunctionSet = new HashSet<>();
        transitionFunctionSet.add(transitionFunction2);
        transitionFunctionSet.add(transitionFunction3);
        transitionFunctionSet.add(transitionFunction4);
        Set<State> states = new HashSet<>();
        states.add(q0);
        states.add(q1);
        states.add(q2);

        assertEquals(transitionFunctionSet, fsa.getTransitionFunctions());
        assertEquals(states, fsa.getStates());
        assertEquals(q0, fsa.getInitialState());
        assertEquals(1, fsa.getFinalStates().size());
        assertTrue(fsa.getFinalStates().contains(q1));
    }

}
