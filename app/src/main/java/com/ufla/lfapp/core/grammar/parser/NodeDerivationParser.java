package com.ufla.lfapp.core.grammar.parser;

import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * Created by carlos on 2/15/17.
 */

public class NodeDerivationParser {

    private Deque<Rule> stackRules;
    private String node;
    private int level;
    private int childInd;
    private List<NodeDerivationParser> childs;
    private NodeDerivationParser father;

    public NodeDerivationParser(String node, int level, NodeDerivationParser father,
                                int childInd) {
        this.node = node;
        this.level = level;
        this.father = father;
        this.childInd = childInd;
    }

    public boolean stackRulesIsDefinied() {
        return stackRules != null;
    }

    public boolean hasRulesOnStack() {
        return stackRules != null && !stackRules.isEmpty();
    }

    public boolean isVariable() {
        return Character.isUpperCase(node.charAt(0));
    }

    public boolean isLambda() {
        return node.equals(Grammar.LAMBDA);
    }

    public String getSpace() {
        char[] space = new char[level * 2];
        Arrays.fill(space, ' ');
        return new String(space);
    }

    public NodeDerivationParser getNodeChild(int ind) {
        return childs.get(ind);
    }

    public Rule getRuleDerivate() {
        if (stackRules == null || stackRules.isEmpty()) {
            return null;
        }
        return stackRules.pop();
    }

    // métodos acessores

    public void setChilds(List<NodeDerivationParser> childs) {
        this.childs = childs;
    }

    public int getCountChilds() {
        if (childs == null) {
            return -1;
        }
        return childs.size();
    }

    public void setStackRules(Deque<Rule> stackRules) {
        this.stackRules = stackRules;
    }

    public NodeDerivationParser getFather() {
        return father;
    }

    public Deque<Rule> getStackRules() {
        return stackRules;
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

    public List<NodeDerivationParser> getChilds() {
        return childs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getSpace())
                .append(node)
                .append(", ");
        if (stackRules == null) {
            return sb.append("null").toString();
        }
        if (stackRules.isEmpty()) {
            return sb.append("[]").toString();
        }
        sb.append("[");
        for (Rule rule : stackRules) {
            sb.append(rule.getRightSide()).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1).setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

}