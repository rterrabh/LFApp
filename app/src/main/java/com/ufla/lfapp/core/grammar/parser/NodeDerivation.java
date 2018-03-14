package com.ufla.lfapp.core.grammar.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos on 2/15/17.
 */

public class NodeDerivation {

    private String node;
    private int level;
    private int childInd;
    private List<NodeDerivation> childs;
    private NodeDerivation father;

    private static NodeDerivation getNodeFromParser(NodeDerivationParser node,
                                                    NodeDerivation father) {
        NodeDerivation nodeDerivation = new NodeDerivation();
        nodeDerivation.node = node.getNode();
        nodeDerivation.level = node.getLevel();
        nodeDerivation.childInd = node.getChildInd();
        nodeDerivation.childs = new ArrayList<>();
        nodeDerivation.father = father;
        List<NodeDerivationParser> childsParser = node.getChilds();
        int n = node.getCountChilds();
        for (int i = 0; i < n; i++) {
            nodeDerivation.childs.add(getNodeFromParser(childsParser.get(i),
                    nodeDerivation));
        }
        return nodeDerivation;
    }

    public static NodeDerivation getRootFromParser(NodeDerivationParser node) {
        return getNodeFromParser(node, null);
    }

    public int getCountChilds() {
        return childs.size();
    }

    public String getNode() {
        return node;
    }


    public int getLevel() {
        return level;
    }


    public int getChildInd() {
        return childInd;
    }


    public List<NodeDerivation> getChilds() {
        return childs;
    }


    public NodeDerivation getFather() {
        return father;
    }


    @Override
    public String toString() {
        return node;
    }
}
