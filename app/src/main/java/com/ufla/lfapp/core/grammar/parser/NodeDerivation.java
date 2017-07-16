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
        NodeDerivation rootDerivationAccept = new NodeDerivation();
        rootDerivationAccept.node = node.getNode();
        rootDerivationAccept.level = node.getLevel();
        rootDerivationAccept.childInd = node.getChildInd();
        rootDerivationAccept.childs = new ArrayList<>();
        rootDerivationAccept.father = null;
        List<NodeDerivationParser> childsParser = node.getChilds();
        int n = node.getCountChilds();
        for (int i = 0; i < n; i++) {
            rootDerivationAccept.childs.add(getNodeFromParser(childsParser.get(i),
                    rootDerivationAccept));
        }
        return rootDerivationAccept;
    }

    public int getCountChilds() {
        if (childs == null) {
            return -1;
        }
        return childs.size();
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getChildInd() {
        return childInd;
    }

    public void setChildInd(int childInd) {
        this.childInd = childInd;
    }

    public List<NodeDerivation> getChilds() {
        return childs;
    }

    public void setChilds(List<NodeDerivation> childs) {
        this.childs = childs;
    }

    public NodeDerivation getFather() {
        return father;
    }

    public void setFather(NodeDerivation father) {
        this.father = father;
    }

    @Override
    public String toString() {
        return node;
    }
}
