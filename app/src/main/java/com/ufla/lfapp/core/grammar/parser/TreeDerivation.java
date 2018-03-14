package com.ufla.lfapp.core.grammar.parser;

import com.ufla.lfapp.core.grammar.Grammar;

import java.util.List;

/**
 * Created by carlos on 2/15/17.
 */

public class TreeDerivation {

    private NodeDerivation root;

    public TreeDerivation(NodeDerivationParser root) {
        this.root = NodeDerivation.getRootFromParser(root);
    }

    public NodeDerivation getRoot() {
        return root;
    }

    public String getDerivation(NodeDerivation node) {
        List<NodeDerivation> childs = node.getChilds();
        StringBuilder sbDerivation = new StringBuilder();
        int n = node.getCountChilds();
        for (int i = 0; i < n; i++) {
            sbDerivation.append(childs.get(i).getNode());
        }
        return sbDerivation.toString();
    }

    public String getDerivation() {
        StringBuilder sbDerivation = new StringBuilder();
        sbDerivation.append(root.getNode())
                .append(" => ");
        StringBuilder lastDerivation = new StringBuilder(getDerivation(root));
        sbDerivation.append(lastDerivation)
                .append(" => ");
        NodeDerivation actualNode = root;
        List<NodeDerivation> childs = actualNode.getChilds();
        if (childs == null || childs.isEmpty()) {
            String derivation = sbDerivation.toString();
            derivation = derivation.substring(0, derivation.length() - 2);
            return derivation;
        }
        actualNode = childs.get(0);
        NodeDerivation parentNode = actualNode.getFather();
        int childInd;
        int indice = 0;
        do {
            childs = actualNode.getChilds();
            if (childs != null)
                if (childs != null && !childs.isEmpty()) {
                    String derivation = getDerivation(actualNode);
                    lastDerivation.deleteCharAt(indice);
                    lastDerivation.insert(indice, derivation);
                    sbDerivation.append(lastDerivation)
                            .append(" => ");
                    actualNode = childs.get(0);
                    int indexLambda = lastDerivation.indexOf(Grammar.LAMBDA);
                    if (indexLambda > -1) {
                        lastDerivation.deleteCharAt(indexLambda);
                        sbDerivation.append(lastDerivation)
                                .append(" => ");
                        indice--;
                    }
                } else {
                    indice++;
                    childInd = actualNode.getChildInd();
                    parentNode = actualNode.getFather();
                    int countChild = parentNode.getCountChilds();
                    while (childInd == countChild - 1 && parentNode.getFather() != null) {
                        actualNode = parentNode;
                        childInd = actualNode.getChildInd();
                        parentNode = actualNode.getFather();
                        countChild = parentNode.getCountChilds();
                    }
                    if (childInd != countChild - 1) {
                        actualNode = parentNode.getChilds().get(childInd + 1);
                    } else {
                        parentNode = null;
                    }
                }
        } while (parentNode != null);
        String derivation = sbDerivation.toString();
        derivation = derivation.substring(0, derivation.length() - 4);
        return derivation;
    }

}
