package com.ufla.lfapp.core.grammar.parser;

import com.ufla.lfapp.core.grammar.Rule;
import com.ufla.lfapp.utils.MyPair;
import com.ufla.lfapp.utils.Symbols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by carlos on 13/03/18.
 */

public class NodeAmbiguity {



    public int getLevel() {
        return level;
    }

    private List<String> symbols;
    private int level;
    private int childInd;
    private List<Rule> rules;
    private List<Integer> indexRules;
    private List<NodeAmbiguity> childs;
    private NodeAmbiguity father;

    public NodeAmbiguity(List<String> symbols, int level, NodeAmbiguity father,
                                int childInd) {
        this.symbols = symbols;
        this.level = level;
        this.father = father;
        this.childInd = childInd;
    }

    public String getNodeSymbolsStr() {
        StringBuilder sb = new StringBuilder();
        for (String symbol : symbols) {
            if (!symbol.equals(Symbols.LAMBDA)) {
                sb.append(symbol);
            }
        }
        return sb.toString();
    }

    public String getNodeSymbolsStrParam(List<String> symbolsParam) {
        StringBuilder sb = new StringBuilder();
        for (String symbol : symbolsParam) {
            sb.append(symbol);
        }
        return sb.toString();
    }

    public String getDerivation() {
        if (father != null) {
            return father.getDerivation() + " => " + getNodeSymbolsStr();
        }
        return getNodeSymbolsStr();
    }

    public List<Rule> getRulesThatGenerate() {
        Rule[] rulesThatGenerate = new Rule[level];
        father.addRulesThatGenerateRec(rulesThatGenerate, childInd);
        return Arrays.asList(rulesThatGenerate);
    }

    public void addRulesThatGenerateRec(Rule[] rulesThatGenerate, int index) {
        rulesThatGenerate[level] = rules.get(index);
        if (father != null) {
            father.addRulesThatGenerateRec(rulesThatGenerate, childInd);
        }
    }

    public List<Integer> getIndexRulesThatGenerate() {
        Integer[] indexRulesThatGenerate = new Integer[level];
        father.addIndexRulesThatGenerateRec(indexRulesThatGenerate, childInd);
        return Arrays.asList(indexRulesThatGenerate);
    }

    public void addIndexRulesThatGenerateRec(Integer[] indexRulesThatGenerate, int index) {
        indexRulesThatGenerate[level] = indexRules.get(index);
        if (father != null) {
            father.addIndexRulesThatGenerateRec(indexRulesThatGenerate, childInd);
        }
    }


    public List<List<String>> getSymbolsList() {
        List<List<String>> listSymbols = new ArrayList<>(level + 1);
        addSymbolsListRec(listSymbols);
        return listSymbols;
    }

    public TreeDerivation createTreeDerivation() {
        List<Rule> rulesThatGenerate = getRulesThatGenerate();
        List<Integer> indexRulesThatGenerate = getIndexRulesThatGenerate();
        SortedMap<Integer, NodeDerivationParser> nodes = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        NodeDerivationParser root = new NodeDerivationParser(
                rulesThatGenerate.get(0).getLeftSide(), 0, null, 0);
        int ind = 0;
        List<NodeDerivationParser> childsParser = new ArrayList<>();
        for (String str : rulesThatGenerate.get(0).getListOfSymbolsOnRightSide()) {
            NodeDerivationParser node = new NodeDerivationParser(
                    str, 1, root, ind);
            childsParser.add(node);
            if (node.getNode().length() != 1 || !Character.isLowerCase(node.getNode().charAt(0))) {
                nodes.put(ind + indexRulesThatGenerate.get(0), node);
            }
            ind++;
        }
        root.setChilds(childsParser);
        for (int i = 1; i < rulesThatGenerate.size(); i++) {
            NodeDerivationParser fatherParser = nodes.remove(indexRulesThatGenerate.get(i));
            List<String> symbolsRules = rulesThatGenerate.get(i).getListOfSymbolsOnRightSide();
            if (symbolsRules.size() > 1) {
                List<Integer> reinsert = new ArrayList<>();
                for (SortedMap.Entry<Integer, NodeDerivationParser> nodesEntry : nodes.entrySet()) {
                    int key = nodesEntry.getKey();
                    if (key > indexRulesThatGenerate.get(i)) {
                        reinsert.add(key);
                    } else {
                        break;
                    }

                }
                for (Integer key : reinsert) {
                    NodeDerivationParser node = nodes.remove(key);
                    nodes.put(key + symbolsRules.size() - 1, node);
                }
            }
            ind = 0;
            childsParser = new ArrayList<>();
            for (String str : symbolsRules) {
                NodeDerivationParser node = new NodeDerivationParser(
                        str, fatherParser.getLevel() + 1, fatherParser, ind);
                childsParser.add(node);
                if (node.getNode().length() != 1 || !Character.isLowerCase(node.getNode().charAt(0))) {
                    nodes.put(ind + indexRulesThatGenerate.get(i), node);
                }
                ind++;
            }
            fatherParser.setChilds(childsParser);
        }
        return new TreeDerivation(root);
    }

    public void addSymbolsListRec(List<List<String>> listSymbols) {
        if (father != null) {
            father.addSymbolsListRec(listSymbols);
        }
        listSymbols.add(level, symbols);
    }

    public List<NodeAmbiguity> generateChilds(TreeDerivationParser.GrammarOrderedAmbiguity grammarOrderedAmbiguity) {
        childs = new ArrayList<>();
        indexRules = new ArrayList<>();
        rules = new ArrayList<>();
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).length() != 1 || Character.isUpperCase(symbols.get(i).charAt(0))) {
                List<Rule> newRules = grammarOrderedAmbiguity.getRules(symbols.get(i));
                List<String> preSymbols = symbols.subList(0, i);
                List<String> postSymbols = symbols.subList(i + 1, symbols.size());
                for (Rule rule : newRules) {
                    List<String> symbolsChild = new ArrayList<>(preSymbols);
                    symbolsChild.addAll(rule.getListOfSymbolsOnRightSide());
                    symbolsChild.addAll(postSymbols);
                    indexRules.add(i);
                    childs.add(new NodeAmbiguity(symbolsChild, level + 1, this,
                            childs.size()));
                }
                rules.addAll(newRules);
            }
        }
        return childs;
    }

    public void clearReferenceChild(int childIndex) {
        childs.set(childIndex, null);
        boolean onlyNull = true;
        for (NodeAmbiguity child : childs) {
            if (child != null) {
                onlyNull = false;
                break;
            }
        }
        if (onlyNull) {
            father.clearReferenceChild(childInd);
            setNull();
        }
    }

    public void setNull() {
        this.symbols = null;
        this.indexRules = null;
        this.rules = null;
        this.father = null;
        this.childs = null;
    }

    public MyPair<String, TreeDerivation> verifyNode() {
        boolean leaf = true;
        for (String symbol : symbols) {
            if (symbol.length() > 1 || Character.isUpperCase(symbol.charAt(0))) {
                leaf = false;
                break;
            }
        }
        if (leaf) {
            MyPair<String, TreeDerivation> derivationPair = new MyPair(getNodeSymbolsStr(), createTreeDerivation());
            //System.out.println("[" + derivationPair.first + "] " + getDerivation());
            father.clearReferenceChild(childInd);
            setNull();
            return derivationPair;
        }
        return null;
    }


}
