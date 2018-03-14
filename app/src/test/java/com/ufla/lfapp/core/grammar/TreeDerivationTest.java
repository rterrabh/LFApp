package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.core.grammar.parser.MostLeftDerivationTable;
import com.ufla.lfapp.core.grammar.parser.NodeDerivation;
import com.ufla.lfapp.core.grammar.parser.NodeDerivationParser;
import com.ufla.lfapp.core.grammar.parser.NodeDerivationPosition;
import com.ufla.lfapp.core.grammar.parser.TreeDerivation;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationParser;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationPosition;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.*;
import org.junit.Test;
import org.w3c.dom.Node;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by carlos on 10/21/17.
 */

public class TreeDerivationTest {

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    @Test
    public void testMostLeftDerTableToString() {
        Grammar grammar = new Grammar("X -> XaX | XbX | c | X | A | " + Grammar.LAMBDA + "\n A -> X");
        MostLeftDerivationTable mostLeftDerivationTable = new MostLeftDerivationTable(grammar);
        String tableExpected = "  a           b           c           λ           \n" +
                "X {A, X, XaX} {A, X, XbX} {c, A, X}   {λ}         \n" +
                "A {X}         {X}         {X}         {X}         \n";
        assertEquals(tableExpected, mostLeftDerivationTable.toString());
    }

    @Test
    public void testMostLeftDerTableToString2() {
        Grammar grammar = new Grammar("X -> XaX | XbX | c | X | A | " + Grammar.LAMBDA + "\n A -> a");
        MostLeftDerivationTable mostLeftDerivationTable = new MostLeftDerivationTable(grammar);
        String tableExpected =  "  a           b           c           λ           \n" +
                "X {A, X, XaX} {X, XbX}    {c, X}      {λ}         \n" +
                "A {a}         {}          {}          {}          \n";
        assertEquals(tableExpected, mostLeftDerivationTable.toString());
    }

    @Test
    public void testTreeDerivation() {
        Grammar grammar = new Grammar("X -> XaX | XbX | c | X | A | " + Grammar.LAMBDA + "\n A -> X");
        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar, "ab");
        treeDerivationParser.parser();
        TreeDerivation treeDerivation = treeDerivationParser.getTreeDerivation();
        String expected = "X => A => X => XaX => XaX => λaX => aX => aXbX => aλbX => abX => abλ => ab";
        assertEquals(expected, treeDerivation.getDerivation());
    }

    private void setFieldNodeDerivationPosition(String name, Object object, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field field = NodeDerivationPosition.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(object, value);
    }

    private void setFieldIntNodeDerivationPosition(String name, Object object, int value) throws IllegalAccessException, NoSuchFieldException {
        Field field = NodeDerivationPosition.class.getDeclaredField(name);
        field.setAccessible(true);
        field.setInt(object, value);
    }

    private NodeDerivationPosition getNodeDerivationPosition(String node, int level, NodeDerivationPosition father,
                                                             MyPoint position) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, NoSuchFieldException {
        Constructor constructor = NodeDerivationPosition.class.getConstructor();
        constructor.setAccessible(true);
        NodeDerivationPosition nodeDerivationPosition = (NodeDerivationPosition) constructor.newInstance();
        setFieldNodeDerivationPosition("node", nodeDerivationPosition, node);
        setFieldIntNodeDerivationPosition("level", nodeDerivationPosition, level);
        setFieldNodeDerivationPosition("father", nodeDerivationPosition, father);
        setFieldNodeDerivationPosition("position", nodeDerivationPosition, position);
        return nodeDerivationPosition;
    }

    @Test
    public void testNodeDerivationPositionHashCode() throws InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        NodeDerivationPosition nodeDerivationPosition = getNodeDerivationPosition("X", 0, null, new MyPoint(7, 0));
        assertEquals(2621825, nodeDerivationPosition.hashCode());
    }

    @Test
    public void testTreeDerivationPosition1() throws NoSuchMethodException, InvocationTargetException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        Grammar grammar = new Grammar("X -> XaX | XbX | c | X | A | " + Grammar.LAMBDA + "\n A -> X");
        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar, "ab");
        treeDerivationParser.parser();
        TreeDerivation treeDerivation = treeDerivationParser.getTreeDerivation();
        TreeDerivationPosition treeDerivationPosition = new TreeDerivationPosition(treeDerivation.getRoot());
        NodeDerivationPosition node0_7_0 =
                getNodeDerivationPosition("X", 0, null, new MyPoint(7, 0));
        NodeDerivationPosition node1_7_0 =
                getNodeDerivationPosition("A", 1, node0_7_0, new MyPoint(7, 0));
        NodeDerivationPosition node2_7_0 =
                getNodeDerivationPosition("X", 2, node1_7_0, new MyPoint(7, 0));
        NodeDerivationPosition node3_1_0 =
                getNodeDerivationPosition("X", 3, node2_7_0, new MyPoint(7, 0));
        NodeDerivationPosition node3_9_0 =
                getNodeDerivationPosition("a", 3, node2_7_0, new MyPoint(9, 0));
        NodeDerivationPosition node3_11_0 =
                getNodeDerivationPosition("X", 3, node2_7_0, new MyPoint(7, 0));
        NodeDerivationPosition node4_1_0 =
                getNodeDerivationPosition("X", 4, node3_1_0, new MyPoint(1, 0));
        NodeDerivationPosition node4_6_0 =
                getNodeDerivationPosition("X", 4, node3_11_0, new MyPoint(8, 0));
        NodeDerivationPosition node4_10_0b =
                getNodeDerivationPosition("b", 4, node3_11_0, new MyPoint(10, 0));
        NodeDerivationPosition node4_10_0X =
                getNodeDerivationPosition("X", 4, node3_11_0, new MyPoint(8, 0));
        NodeDerivationPosition node5_4_0 =
                getNodeDerivationPosition(Grammar.LAMBDA, 5, node4_1_0, new MyPoint(4, 0));
        NodeDerivationPosition node5_5_0 =
                getNodeDerivationPosition(Grammar.LAMBDA, 5, node4_6_0, new MyPoint(5, 0));
        NodeDerivationPosition node5_7_0 =
                getNodeDerivationPosition(Grammar.LAMBDA, 5, node4_10_0X, new MyPoint(7, 0));


        List<NodeDerivationPosition> childsNode0_7_0 =
                new ArrayList<>(Arrays.asList(node1_7_0));
        List<NodeDerivationPosition> childsNode1_7_0 =
                new ArrayList<>(Arrays.asList(node2_7_0));
        List<NodeDerivationPosition> childsNode2_7_0 =
                new ArrayList<>(Arrays.asList(node3_1_0, node3_9_0, node3_11_0));
        List<NodeDerivationPosition> childsNode3_1_0 =
                new ArrayList<>(Arrays.asList(node4_1_0));
        List<NodeDerivationPosition> childsNode3_9_0 =
                new ArrayList<>(Arrays.asList(node4_6_0, node4_10_0b, node4_10_0X));
        List<NodeDerivationPosition> childsNode4_1_0 =
                new ArrayList<>(Arrays.asList(node5_4_0));
        List<NodeDerivationPosition> childsNode4_6_0 =
                new ArrayList<>(Arrays.asList(node5_5_0));
        List<NodeDerivationPosition> childsNode4_10_0X =
                new ArrayList<>(Arrays.asList(node5_7_0));
        setFieldNodeDerivationPosition("childs", node0_7_0, childsNode0_7_0);
        setFieldNodeDerivationPosition("childs", node0_7_0, childsNode1_7_0);
        setFieldNodeDerivationPosition("childs", node0_7_0, childsNode2_7_0);
        setFieldNodeDerivationPosition("childs", node0_7_0, childsNode3_1_0);
        setFieldNodeDerivationPosition("childs", node0_7_0, childsNode3_9_0);
        setFieldNodeDerivationPosition("childs", node0_7_0, childsNode4_1_0);
        setFieldNodeDerivationPosition("childs", node0_7_0, childsNode4_6_0);
        setFieldNodeDerivationPosition("childs", node0_7_0, childsNode4_10_0X);

        List<NodeDerivationPosition> nodes = new ArrayList<>(Arrays.asList(node0_7_0, node1_7_0,
                node2_7_0, node3_1_0, node3_9_0, node3_11_0, node4_1_0, node4_6_0,
                node4_10_0b, node4_10_0X, node5_4_0, node5_5_0, node5_7_0));
        NodeDerivationPosition root = node0_7_0;
        Map<Integer, List<List<NodeDerivationPosition>>> nodesByLevel = new HashMap<>();
        List<List<NodeDerivationPosition>> nodesLevel = new ArrayList<>();
        nodesLevel.add(new ArrayList<>(Arrays.asList(node0_7_0)));
        nodesByLevel.put(0, nodesLevel);
        nodesLevel = new ArrayList<>();
        nodesLevel.add(new ArrayList<>(Arrays.asList(node1_7_0)));
        nodesByLevel.put(1, nodesLevel);
        nodesLevel = new ArrayList<>();
        nodesLevel.add(new ArrayList<>(Arrays.asList(node2_7_0)));
        nodesByLevel.put(2, nodesLevel);
        nodesLevel = new ArrayList<>();
        nodesLevel.add(new ArrayList<>(Arrays.asList(node3_1_0, node3_9_0,
                node3_11_0)));
        nodesByLevel.put(3, nodesLevel);
        nodesLevel = new ArrayList<>();
        nodesLevel.add(new ArrayList<>(Arrays.asList(node4_1_0)));
        nodesLevel.add(new ArrayList<>(Arrays.asList(node4_6_0,
                node4_10_0b, node4_10_0X)));
        nodesByLevel.put(4, nodesLevel);
        nodesLevel = new ArrayList<>();
        nodesLevel.add(new ArrayList<>(Arrays.asList(node5_4_0)));
        nodesLevel.add(new ArrayList<>(Arrays.asList(node5_5_0)));
        nodesLevel.add(new ArrayList<>(Arrays.asList(node5_7_0)));
        nodesByLevel.put(5, nodesLevel);

        System.out.println(treeDerivationPosition.getRoot().getChilds());
        assertEquals(root, treeDerivationPosition.getRoot());
        assertEquals(nodes, treeDerivationPosition.getNodes());
        assertEquals(nodesByLevel, treeDerivationPosition.getNodesByLevel());
        String expected = "X => A => X => XaX => XaX => λaX => aX => aXbX => aλbX => abX => abλ => ab";
        assertEquals(expected, treeDerivation.getDerivation());
    }

    @Test
    public void test() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("X", 0, null, -1);
        TreeDerivation treeDerivation = new TreeDerivation(nodeDerivationParser);
        assertEquals("X =>  =", treeDerivation.getDerivation());
    }



}
