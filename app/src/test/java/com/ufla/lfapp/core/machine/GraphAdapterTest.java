package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.machine.dotlang.Edge;
import com.ufla.lfapp.core.machine.dotlang.GraphAdapter;
import com.ufla.lfapp.core.machine.dotlang.IllegalMachineTypeException;
import com.ufla.lfapp.core.machine.dotlang.Vertex;
import com.ufla.lfapp.utils.ResourcesContext;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by carlos on 10/22/17.
 */

public class GraphAdapterTest {

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    @Test
    public void testEdge() {
        Edge edge = new Edge();
        edge.current = new State("q1");
        edge.future = new State("q2");
        edge.label = "a";
        String expected = "(q1, q2) -> a";

        assertEquals(expected, edge.toString());
    }

    @Test
    public void testVertex() {
        Vertex vertex = new Vertex("a");
        String expected = "a";
        assertEquals(expected, vertex.label);
    }

    @Test(expected = IllegalMachineTypeException.class)
    public void testIllegalMachineTypeException() {
        throw new IllegalMachineTypeException(MachineType.FSA, MachineType.PDA);
    }

    @Test
    public void testGraphAdapter() {
        GraphAdapter graphAdapter = new GraphAdapter();
        State q0 = new State("q0");
        State q1 = new State("q1");
        graphAdapter.startState = q0;
        graphAdapter.stateFinals = new HashSet<>(Arrays.asList(q1));
        graphAdapter.stateSet = new HashSet<>(Arrays.asList(q0, q1));
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge());
        graphAdapter.edgeList = new ArrayList<>(edges);
        assertEquals(edges, graphAdapter.edgeList);
    }
}
