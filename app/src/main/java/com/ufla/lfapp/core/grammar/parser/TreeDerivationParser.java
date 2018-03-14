package com.ufla.lfapp.core.grammar.parser;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;
import com.ufla.lfapp.utils.MyConsumer;
import com.ufla.lfapp.utils.MyPair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by carlos on 2/15/17.
 */

public class TreeDerivationParser {

    private boolean treeComplete;
    private Grammar grammar;
    private GrammarOrderedAmbiguity grammarOrderedAmbiguity;
    private NodeDerivationParser root;
    private String word;
    private int index;
    private MostLeftDerivationTable mostLeftDerivationTable;
    private NodeDerivationParser actualNode;
    private TreeDerivation treeDerivation;
    private TreeDerivation treeDerivationAux;
    private int countNodes;
    //private static final int MAX_NODES = 250;
    private static final int MAX_LEVEL = 20;
    private List<String> actualWord;
    private AtomicBoolean cancelSearch;
    private static final long MAX_TIME_AMBIGUITY_SEARCH = 1000 * 7;
    private static final double MIN_FREE_MEMORY_PERCENT = 15;
    /**
     * Construtor de árvore de derivação com base em uma gramática _grammar_ e uma palavra a ser
     * derivada _word_.
     *
     * @param grammar gramática a ser usada na derivação
     * @param word    palavra a ser derivada
     */
    public TreeDerivationParser(Grammar grammar, String word) {
        this.grammar = grammar;
        this.word = word;
        index = 0;
    }

    public TreeDerivationParser(Grammar grammar) {
        this.grammar = grammar;
        index = 0;
    }

    class RuleOrderedAmbiguity implements Comparable<RuleOrderedAmbiguity> {
        public Rule rule;
        public int invPriority;

        public RuleOrderedAmbiguity(Rule rule, int invPriority) {
            this.rule = rule;
            this.invPriority = invPriority;
        }

        @Override
        public String toString() {
            return rule.toString();
        }

        @Override
        public int compareTo(@NonNull RuleOrderedAmbiguity o) {
            return invPriority - o.invPriority;
        }
    }

    public void cancelAmbiguitySearch() {
        cancelSearch.set(true);
    }

    class GrammarOrderedAmbiguity {
        private Set<String> variables;
        private Set<String> terminals;
        private String initialSymbol;
        private Set<RuleOrderedAmbiguity> rules;

        public GrammarOrderedAmbiguity(Grammar grammarUnordered) {
            terminals = grammarUnordered.getTerminals();
            initialSymbol = grammarUnordered.getInitialSymbol();
            variables = new LinkedHashSet<>();
            rules = new LinkedHashSet<>();
            defineGrammarOrdered(grammarUnordered);
        }

        public List<Rule> getRules(String variable) {
            List<Rule> rulesOfVariable = new ArrayList<>();
            for (RuleOrderedAmbiguity ruleOrderedAmbiguity : rules) {
                if (ruleOrderedAmbiguity.rule.getLeftSide().equals(variable)) {
                    rulesOfVariable.add(ruleOrderedAmbiguity.rule);
                }
            }
            return rulesOfVariable;
        }

        private void defineGrammarOrdered(Grammar grammarUnordered) {
            Map<String, Integer> variableToInvPriority = calcVariableToInvPriority(grammarUnordered);
            for (String var : grammarUnordered.getVariables()) {
                if (variableToInvPriority.get(var) != Integer.MAX_VALUE) {
                    variables.add(var);
                }
            }
            List<RuleOrderedAmbiguity> rulesList = new ArrayList<>();
            for (String var : variables) {
                for (Rule rule : grammarUnordered.getRules(var)) {
                    Integer invPriority = calcRuleInvPriority(variableToInvPriority, rule);
                    if (invPriority != null && invPriority != Integer.MAX_VALUE) {
                        rulesList.add(new RuleOrderedAmbiguity(rule, invPriority));
                    }
                }
            }
            Collections.sort(rulesList);
            rules.addAll(rulesList);
        }

        private Map<String, Integer> calcVariableToInvPriority(Grammar grammarUnordered) {
            Map<String, Integer> variableToInvPriority = new HashMap<>();
            Set<String> variables = grammarUnordered.getVariables();
            Set<String> variablesCalcInvPriority = new HashSet<>();
            for (String var : variables) {
                for (Rule rule : grammarUnordered.getRules(var)) {
                    if (rule.producesOnlyTerminal()) {
                        variablesCalcInvPriority.add(var);
                        variableToInvPriority.put(var, 0);
                        break;
                    }
                }
            }
            variables.removeAll(variablesCalcInvPriority);
            do {
                variablesCalcInvPriority.clear();
                for (String var : variables) {
                    Integer minInvPriority = Integer.MAX_VALUE;
                    Integer minInvPriorityAux;
                    for (Rule rule : grammarUnordered.getRules(var)) {
                        minInvPriorityAux = calcRuleInvPriority(variableToInvPriority, rule);
                        if (minInvPriorityAux != null) {
                            minInvPriority = Math.min(minInvPriority, minInvPriorityAux);
                        }
                    }
                    if (minInvPriority != Integer.MAX_VALUE) {
                        variablesCalcInvPriority.add(var);
                        variableToInvPriority.put(var, minInvPriority);
                    }
                }
                variables.removeAll(variablesCalcInvPriority);
            } while (!variablesCalcInvPriority.isEmpty());
            for (String var : variables) {
                variableToInvPriority.put(var, Integer.MAX_VALUE);
            }
            return variableToInvPriority;
        }

        private Integer calcRuleInvPriority(Map<String, Integer> variableToInvPriority,
                                            Rule rule) {
            Set<String> symbols = rule.getSymbolsOfRightSide();
            int hasVar = 0;
            Integer invPriority = 0;
            Integer invPriorityAux;
            for (String symbol : symbols) {
                if (symbol.length() != 1 || !Character.isLowerCase(symbol.charAt(0))) {
                    hasVar = 1;
                    invPriorityAux = variableToInvPriority.get(symbol);
                    if (invPriorityAux == null) {
                        return null;
                    }
                    invPriority = Math.max(invPriority, invPriorityAux);
                }
            }
            return invPriority + hasVar;
        }
    }

    public String getWord() {
        return word;
    }

    private Map<String, TreeDerivation> words;

    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }


    public void checkAmbiguity(final MyConsumer<TreeDerivationParser> callback, final Context context) {
        cancelSearch = new AtomicBoolean(false);
        final Thread runAmbiguity = new Thread(new Runnable() {
            @Override
            public void run() {
                grammarOrderedAmbiguity = new GrammarOrderedAmbiguity(grammar);
                int cont = 0;
                String initialVariable = grammar.getInitialSymbol();
                List<String> initialList = new ArrayList<>();
                initialList.add(initialVariable);
                NodeAmbiguity rootAmbiguity = new NodeAmbiguity(initialList, 0, null, -1);
                List<NodeAmbiguity> actualNodeAmbiguity = new ArrayList<>();
                List<NodeAmbiguity> nextNodeAmbiguity = new ArrayList<>();
                actualNodeAmbiguity.add(rootAmbiguity);
//                root = new NodeDerivationParser(initialVariable, 0, null, -1);
//                actualNode = root;
//                actualWord = new ArrayList<>();
                words = new LinkedHashMap<>();
                cont = 0;
                while (!treeComplete && !cancelSearch.get()) {
                    for (NodeAmbiguity nodeAmbiguity : actualNodeAmbiguity) {
                        List<NodeAmbiguity> childs = nodeAmbiguity.generateChilds(grammarOrderedAmbiguity);
                        for (NodeAmbiguity child : childs) {
                            MyPair<String, TreeDerivation> derivation = child.verifyNode();
                            if (derivation != null) {
                                if (words.containsKey(derivation.first)) {
                                    if (derivation.second.getDerivation()
                                            .equals(words.get(derivation.first).getDerivation())) {
                                        continue;
                                    }
                                    word = derivation.first;
                                    treeComplete = true;
                                    treeDerivation = words.get(derivation.first);
                                    treeDerivationAux = derivation.second;
                                } else {
                                    words.put(derivation.first, derivation.second);
                                }
                            } else {
                                nextNodeAmbiguity.add(child);
                            }
                        }
                        if (treeComplete || cancelSearch.get()) {
                            break;
                        }
                    }
                    cont++;
                    actualNodeAmbiguity = nextNodeAmbiguity;
                    nextNodeAmbiguity = new ArrayList<>();
                }
//                System.out.println("Level max -> " + actualNodeAmbiguity.get(0).getLevel());
                System.out.println("Level max -> " + cont);
//                System.out.println("###Derivation founds###");
//                for (Map.Entry<String, TreeDerivation> derivationEntry : words.entrySet()) {
//                    System.out.print(derivationEntry.getKey() + ": \n" +
//                            derivationEntry.getValue().getDerivation() + "\n");
//                }
                grammarOrderedAmbiguity = null;
                root = null;
                actualNode = null;
                actualWord = null;
                words = null;
                treeComplete = false;
                //System.out.println("callback");
                callback.accept(TreeDerivationParser.this);
            }
        });
        runAmbiguity.start();
        final Thread killAmbiguity = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(MAX_TIME_AMBIGUITY_SEARCH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                if (context == null) {
//                    try {
//                        Thread.sleep(MAX_TIME_AMBIGUITY_SEARCH);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    ActivityManager.MemoryInfo actualMemory = getAvailableMemory(context);
//                    System.out.println("totalMem -> " + actualMemory.totalMem);
//                    double memoryFreePercent = actualMemory.availMem / (double) actualMemory.totalMem * 100.0d;
//                    System.out.println("availMem -> " + actualMemory.availMem +
//                            " | percentFree -> " + memoryFreePercent);
//                    while (memoryFreePercent > MIN_FREE_MEMORY_PERCENT) {
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        actualMemory = getAvailableMemory(context);
//                        memoryFreePercent = actualMemory.availMem / (double) actualMemory.totalMem * 100.0d;
//                        System.out.println("availMem -> " + actualMemory.availMem +
//                                " | percentFree -> " + memoryFreePercent);
//                    }
//                }
                System.out.println("Cancelando busca");
                cancelSearch.set(true);
            }
        });
        killAmbiguity.start();

    }

    private String getActualWordString() {
        StringBuilder sb = new StringBuilder();
        for (String token : actualWord) {
            sb.append(token);
        }
        return sb.toString();
    }

    private void parserNodeAmb() {
        if (actualNode.isVariable()) {
            if (!actualNode.stackRulesIsDefinied()) {
                setStackRulesAmb();
            }
            if (actualNode.hasRulesOnStack()) {
                derivateNode();
            } else {
                actualNode.setStackRules(null);
                backAmb();
            }
        } else {
            if (!actualNode.isLambda()) {
                actualWord.add(actualNode.getNode());
            }
            if (!findNextLeafNode()) {
                word = getActualWordString();
                treeDerivationAux = new TreeDerivation(root);
                if (words.containsKey(word)) {
                    treeDerivation = words.get(word);
                    treeComplete = true;
                } else {
                    words.put(word, treeDerivationAux);
                    findLastNodeOfBranch();
                    backAmb();
                }
            }
        }
    }

    private void setStackRulesAmb() {
        String variable = actualNode.getNode();
        //actualNode.setStackRules(grammarOrderedAmbiguity.getRules(variable));
    }

    private void backAmb() {
        boolean findNode = false;
        while (!findNode) {
            if (actualNode.isVariable()) {
                if (actualNode.stackRulesIsDefinied() && actualNode.hasRulesOnStack()) {
                    simpleCutBranch();
                    findNode = true;
                } else if (actualNode == root) {
                    treeComplete = true;
                    findNode = true;
                } else {
                    findLastNode();
                }
            } else {
                if (!actualNode.isLambda()) {
                    actualWord.remove(actualWord.size() - 1);
                }
                findLastNode();
            }
        }
    }

//    private void printNodeRec(NodeDerivationParser node) {
//        if (node.getChilds() == null) {
//            return;
//        }
//        for (NodeDerivationParser child : node.getChilds()) {
//            System.out.println(child);
//        }
//        for (NodeDerivationParser child : node.getChilds()) {
//            printNodeRec(child);
//        }
//
//    }
//
//    static int printCont = 0;
//
//    private void printActualTree() {
//        System.out.println("----------------------------");
//        System.out.println(root);
//        printNodeRec(root);
//        System.out.println("----------------------------");
//    }


    public TreeDerivation getTreeDerivation() {
        return treeDerivation;
    }

    public TreeDerivation getTreeDerivationAux() {
        return treeDerivationAux;
    }

    /**
     * Cria tabela de derivação mais à esquerda para a gramática _grammar_, e define a raiz da
     * árvore para realizar a derivação.
     */
    private void createDataForParser() {
        mostLeftDerivationTable = new MostLeftDerivationTable(grammar);
        String initialVariable = grammar.getInitialSymbol();
        root = new NodeDerivationParser(initialVariable, 0, null, -1);
        actualNode = root;
    }

    /**
     * Limpa a tabela de derivação mais à esquerda e a árvore de derivação para liberar memória.
     */
    private void clearDataForParser() {
        mostLeftDerivationTable = null;
        root = null;
        actualNode = null;
    }

    /**
     * Busca uma árvore de derivação mais à esquerda _treeDerivation_ na gramática _grammar_ para
     * a palavra _word_. Também tenta encontrar uma segunda árvore de derivação mais à esquerda
     * para identificar ambiguidade na gramática.
     */
    public void parser() {
        createDataForParser();
        while (!treeComplete && treeDerivationAux == null) {
            parserNode();
//            printCont++;
//            if (printCont > 100) {
//                printActualTree();
//            }
        }
        clearDataForParser();
    }

    /**
     * Analisa o nó atual na árvore de derivação.
     * Se o nó é uma variável e a pilha de regras ainda não foi definida, então define a pilha de
     * regras.
     * Se o nó é uma variável e possui regras na pilha, então deriva o nó.
     * Se o nó é uma variável e a pilha de regras esteja vazia, então define pilha de regras como
     * _null_ e volta para último nó derivável.
     * Se o nó é um terminal e este terminal não é válido para a posição atual da palavra, então
     * corta o ramo.
     * Se o nó é um terminal válido, então se for diferente de lambda incrementa a posição atual da
     * palavra, e busca o próximo nó derivável. Caso não for encontrado nó derivável verifica se a
     * derivação é válida. E busca o último nó derivável da árvore.
     */
    private void parserNode() {
        if (actualNode.isVariable()) {
            if (!actualNode.stackRulesIsDefinied()) {
                setStackRules();
            }
            if (actualNode.hasRulesOnStack()) {
                derivateNode();
            } else {
                actualNode.setStackRules(null);
                back();
            }
        } else if (!isAValidSymbol()) {
            cutBranch();
        } else {
            if (!actualNode.isLambda()) {
                index++;
            }
            if (!findNextLeafNode()) {
                verifyDerivate();
                findLastNodeOfBranch();
                back();
            }
        }
    }

    /**
     * Deriva um nó (uma variável que ainda possui regras na pilha) na árvore de derivação.
     */
    private void derivateNode() {
        Rule rule = actualNode.getRuleDerivate();
        String rightSide = rule.getRightSide();
        int n = rightSide.length();
        countNodes += n;
        if (actualNode.getLevel() == MAX_LEVEL) {
            countNodes -= n;
            return;
        }
        List<NodeDerivationParser> childs = new ArrayList<>();
        int level = actualNode.getLevel() + 1;
        for (int i = 0; i < n; i++) {
            String node = Character.toString(rightSide.charAt(i));
            childs.add(new NodeDerivationParser(node, level, actualNode, i));
        }
        actualNode.setChilds(childs);
        actualNode = childs.get(0);
    }

    /**
     * Volta ao último nó derivável da árvore de derivação. Nó derivável é um nó variável que
     * possui regras à ser derivadas na pilha.
     * Se o nó é uma variável e possui regras em sua pilha, então corta o ramo em que este nó é a
     * raíz e finaliza a busca.
     * Se o nó é a raíz da árvore e não possui regras em sua pilha, então não é possível gerar
     * nenhuma outra árvore, finaliza a busca.
     * Se o nó é variável e não possui regras na pilha ou se é terminal, então volta para o
     * último nó na árvore.
     * Se o nó é terminal e diferente de lambda atualiza o índice da palavra.
     */
    private void back() {
        boolean findNode = false;
        while (!findNode) {
            if (actualNode.isVariable()) {
                if (actualNode.stackRulesIsDefinied() && actualNode.hasRulesOnStack()) {
                    simpleCutBranch();
                    findNode = true;
                } else if (actualNode == root) {
                    treeComplete = true;
                    findNode = true;
                } else {
                    findLastNode();
                }
            } else {
                if (!actualNode.isLambda()) {
                    index--;
                }
                findLastNode();
            }
        }
    }

    /**
     * Define as regras que este nó pode gerar de acordo com as regras definidas pela tabela
     * de derivação mais à esquerda e coloca-as na pilha.
     */
    private void setStackRules() {
        if (index > word.length()) {
            cutBranch();
            return;
        }
        String variable = actualNode.getNode();
        String terminal = (index == word.length()) ? Grammar.LAMBDA :
                Character.toString(word.charAt(index));
        Deque<Rule> rules = mostLeftDerivationTable.getRules(variable, terminal);
        actualNode.setStackRules(rules);
    }

    /**
     * Atualiza o índice da palavra para cortar o ramo, onde o parâmetro rootBranch é a raiz do
     * ramo.
     * Atualiza o contador de nós.
     *
     * @param rootBranch raíz do ramo a ser cortado
     */
    private void updateIndexWordForCutBranch(NodeDerivationParser rootBranch) {
        List<NodeDerivationParser> childs = rootBranch.getChilds();
        int countChilds = rootBranch.getCountChilds();
        countNodes -= countChilds;
        String nodeStr = rootBranch.getNode();
        if (Character.isLowerCase(nodeStr.charAt(0))) {
            index--;
        }
        for (int i = 0; i < countChilds; i++) {
            updateIndexWordForCutBranch(childs.get(i));
        }
    }

    /**
     * Realiza um corte simples em um ramo, onde o actualNode é a raíz do ramo. Não busca a raíz
     * do ramo nem atualiza o índice da palavra.
     */
    private void simpleCutBranch() {
        actualNode.setChilds(null);
    }

    /**
     * Realiza um corte no ramo, onde a raíz do ramo é o nó que é uma variável e possui a maior
     * posição, considerando uma ordem pré-ordem na árvore, logo abaixo da posição do actualNode.
     * Atualiza o índice da palavra.
     * Atualiza o contador de nós.
     */
    private void cutBranch() {
        int indFilho = actualNode.getChildInd();
        NodeDerivationParser parentNode = actualNode.getFather();
        int symbols = -1;
        do {
            findLastNode();
            if (!actualNode.isLambda()) {
                symbols++;
            }
        } while (!actualNode.isVariable());
        if (actualNode == parentNode) {
            List<NodeDerivationParser> childNodes = actualNode.getChilds();
            int countChilds = actualNode.getCountChilds();
            countNodes -= countChilds - indFilho;
            for (int i = countChilds - 1; i >= indFilho; i--) {
                childNodes.remove(i);
            }
            updateIndexWordForCutBranch(actualNode);
        } else {
            countNodes -= symbols;
            index -= symbols;
        }
        actualNode.setChilds(null);
        if (actualNode.getStackRules().isEmpty()) {
            actualNode.setStackRules(null);
            back();
        }
    }

    /**
     * Verifica se o símbolo do nó atual (actualNode) é um símbolo válido para a posição atual da
     * palavra. O símbolo é válido quando é igual ao terminal da palavra no índice atual ou quando
     * é lambda.
     * Caso o índice atual da palavra é o tamanho da palavra nenhum símbolo será válido.
     *
     * @return true se o símbolo é válido, caso contrário, false
     */
    private boolean isAValidSymbol() {
        String symbol = actualNode.getNode();
        if (index == word.length()) {
            return symbol.equals(Grammar.LAMBDA);
        }
        return symbol.charAt(0) == word.charAt(index)
                || symbol.equals(Grammar.LAMBDA);
    }

    /**
     * Verifica uma árvore de derivação. Se a árvore derivou a palavra inteira, esta árvore é
     * uma árvore válida portanto salva esta árvore em treeDerivation. Caso já havia encontrado
     * uma árvore válida, salva esta árvore em treeDerivationAux e para a execução do parser,
     * pois encontramos ambiguidade na gramática.
     */
    private void verifyDerivate() {
        if (index == word.length()) {
               if (treeDerivation == null) {
//                System.out.println("ÁRVORE COMPLETA!");
                treeDerivation = new TreeDerivation(root);
            } else {
                treeDerivationAux = new TreeDerivation(root);
            }
        }
    }

    /**
     * Busca um próximo nó folha na árvore, a ordem para essa busca é a pré-ordem. Caso não seja
     * encontrado um nó define o nó atual como a raiz, isto também indica que uma árvore foi
     * completada.
     *
     * @return true caso encontre um próximo nó folha, ou false caso contrário
     */
    private boolean findNextLeafNode() {
        int childInd = actualNode.getChildInd();
        NodeDerivationParser parentNode = actualNode.getFather();
        int countChild = parentNode.getCountChilds();
        while (childInd == countChild - 1 && parentNode.getFather() != null) {
            actualNode = parentNode;
            childInd = actualNode.getChildInd();
            parentNode = actualNode.getFather();
            countChild = parentNode.getCountChilds();
        }
        boolean find = childInd != countChild - 1;
        if (find) {
            actualNode = parentNode.getNodeChild(childInd + 1);
        } else {
            actualNode = parentNode;
        }
        return find;
    }

    /**
     * Busca o último nó considerando a ordem pré-ordem.
     */
    private void findLastNode() {
        int childInd = actualNode.getChildInd() - 1;
        actualNode = actualNode.getFather();
        if (childInd > -1) {
            actualNode = actualNode.getChilds().get(childInd);
            findLastNodeOfBranch();
        }
    }

    /**
     * Busca o último nó do ramo, onde o actualNode é a raíz do ramo.
     */
    private void findLastNodeOfBranch() {
        while (actualNode.getChilds() != null) {
            actualNode = actualNode.getNodeChild(actualNode.getCountChilds() - 1);
        }
    }


    public boolean isAmbiguity() {
        return treeDerivationAux != null && treeDerivation != null;
    }
}
