package com.ufla.lfapp.core.grammar.parser;

import android.graphics.Point;

import com.ufla.lfapp.utils.MyPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos on 2/21/17.
 */

public class NodeDerivationPosition implements Serializable {

    private String node;
    private int level;
    private int childInd;
    private List<NodeDerivationPosition> childs;
    private NodeDerivationPosition father;
    private MyPoint position;

    private static NodeDerivationPosition getNode(NodeDerivation node,
                                                          NodeDerivationPosition father) {
        NodeDerivationPosition nodeDerivationPos = new NodeDerivationPosition();
        nodeDerivationPos.node = node.getNode();
        nodeDerivationPos.level = node.getLevel();
        nodeDerivationPos.childInd = node.getChildInd();
        nodeDerivationPos.childs = new ArrayList<>();
        nodeDerivationPos.father = father;
        List<NodeDerivation> childs = node.getChilds();
        int n = node.getCountChilds();
        for (int i = 0; i < n; i++) {
            nodeDerivationPos.childs.add(getNode(childs.get(i), nodeDerivationPos));
        }
        return nodeDerivationPos;
    }

    public static NodeDerivationPosition getRootFromNode(NodeDerivation node) {
        NodeDerivationPosition rootNodePosition = new NodeDerivationPosition();
        rootNodePosition.node = node.getNode();
        rootNodePosition.level = node.getLevel();
        rootNodePosition.childInd = node.getChildInd();
        rootNodePosition.childs = new ArrayList<>();
        rootNodePosition.father = null;
        List<NodeDerivation> childs = node.getChilds();
        int n = node.getCountChilds();
        for (int i = 0; i < n; i++) {
            rootNodePosition.childs.add(getNode(childs.get(i),
                    rootNodePosition));
        }
        return rootNodePosition;
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

    public List<NodeDerivationPosition> getChilds() {
        return childs;
    }

    public void setChilds(List<NodeDerivationPosition> childs) {
        this.childs = childs;
    }

    public NodeDerivationPosition getFather() {
        return father;
    }

    public void setFather(NodeDerivationPosition father) {
        this.father = father;
    }

    public MyPoint getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = new MyPoint(position);
    }

    @Override
    public String toString() {
        return level + ", " + node + " " + position;
    }

}
