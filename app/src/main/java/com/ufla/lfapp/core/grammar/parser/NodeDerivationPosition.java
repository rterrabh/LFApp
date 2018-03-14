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
        return getNode(node, null);
    }


    public String getNode() {
        return node;
    }

    public List<NodeDerivationPosition> getChilds() {
        return childs;
    }

    public NodeDerivationPosition getFather() {
        return father;
    }

    public MyPoint getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = new MyPoint(position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeDerivationPosition that = (NodeDerivationPosition) o;

        if (level != that.level) return false;
        if (node != null ? !node.equals(that.node) : that.node != null) return false;
        if (father != null ? !father.equals(that.father) : that.father != null) return false;
        return position != null ? position.equals(that.position) : that.position == null;

    }

    @Override
    public int hashCode() {
        int result = node != null ? node.hashCode() : 0;
        result = 31 * result + level;
        result = 31 * result + (father != null ? father.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return level + ", " + node + " " + position;
    }

}
