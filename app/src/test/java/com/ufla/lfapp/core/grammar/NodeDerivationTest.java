package com.ufla.lfapp.core.grammar;

import android.graphics.Point;

import com.ufla.lfapp.core.grammar.parser.NodeDerivation;
import com.ufla.lfapp.core.grammar.parser.NodeDerivationParser;
import com.ufla.lfapp.core.grammar.parser.NodeDerivationPosition;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by carlos on 10/21/17.
 */

public class NodeDerivationTest {

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    @Test
    public void testToString1() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        assertEquals("A, null", nodeDerivationParser.toString());
    }

    @Test
    public void testToString2() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        nodeDerivationParser.setStackRules(new ArrayDeque<Rule>());
        assertEquals("A, []", nodeDerivationParser.toString());
    }

    @Test
    public void testToString3() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        Deque<Rule> stackRules = new ArrayDeque<>();
        stackRules.add(new Rule("S", "a"));
        nodeDerivationParser.setStackRules(stackRules);
        assertEquals("A, [a]", nodeDerivationParser.toString());
    }

    @Test
    public void testSpace() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        assertEquals("", nodeDerivationParser.getSpace());
    }

    @Test
    public void testRulesDerivated() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        assertEquals(null, nodeDerivationParser.getRuleDerivate());
    }

    @Test
    public void testCountChilds() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        NodeDerivation nodeDerivation = NodeDerivation.getRootFromParser(nodeDerivationParser);
        assertEquals(0, nodeDerivation.getCountChilds());
    }

    @Test
    public void testLevel() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        NodeDerivation nodeDerivation = NodeDerivation.getRootFromParser(nodeDerivationParser);
        assertEquals(0, nodeDerivation.getLevel());
    }

    @Test
    public void testToString4() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        NodeDerivation nodeDerivation = NodeDerivation.getRootFromParser(nodeDerivationParser);
        assertEquals("A", nodeDerivation.toString());
    }

    @Test
    public void testNodePositionToString() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        NodeDerivation nodeDerivation = NodeDerivation.getRootFromParser(nodeDerivationParser);
        NodeDerivationPosition nodeDerivationPosition = NodeDerivationPosition.getRootFromNode(nodeDerivation);

        assertEquals("0, A null", nodeDerivationPosition.toString());
    }

    @Test
    public void testNodePositionNode() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        NodeDerivation nodeDerivation = NodeDerivation.getRootFromParser(nodeDerivationParser);
        NodeDerivationPosition nodeDerivationPosition = NodeDerivationPosition.getRootFromNode(nodeDerivation);

        assertEquals("A", nodeDerivationPosition.getNode());
    }

    @Test
    public void testNodePositionChilds() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        NodeDerivation nodeDerivation = NodeDerivation.getRootFromParser(nodeDerivationParser);
        NodeDerivationPosition nodeDerivationPosition = NodeDerivationPosition.getRootFromNode(nodeDerivation);
        List<NodeDerivationPosition> expected = new ArrayList<>();
        assertEquals(expected, nodeDerivationPosition.getChilds());
    }

    @Test
    public void testNodePositionFather() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        nodeDerivationParser.setChilds(new ArrayList<NodeDerivationParser>());
        nodeDerivationParser.getChilds().add(new NodeDerivationParser("B", 1, nodeDerivationParser, 0));
        NodeDerivation nodeDerivation = NodeDerivation.getRootFromParser(nodeDerivationParser);
        NodeDerivationPosition nodeDerivationPosition = NodeDerivationPosition.getRootFromNode(nodeDerivation);

        assertEquals(null, nodeDerivationPosition.getFather());
    }

    @Test
    public void testNodePositionPosition() {
        NodeDerivationParser nodeDerivationParser = new NodeDerivationParser("A", 0, null, -1);
        NodeDerivation nodeDerivation = NodeDerivation.getRootFromParser(nodeDerivationParser);
        NodeDerivationPosition nodeDerivationPosition = NodeDerivationPosition.getRootFromNode(nodeDerivation);
        nodeDerivationPosition.setPosition(new Point(10, 20));
        MyPoint expected = MyPoint.convertPoint(new Point(10, 20));
        assertEquals(expected, nodeDerivationPosition.getPosition());
    }

}
