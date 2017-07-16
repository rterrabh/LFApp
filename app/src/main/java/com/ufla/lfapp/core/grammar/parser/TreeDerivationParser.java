package com.ufla.lfapp.core.grammar.parser;

import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Created by carlos on 2/15/17.
 */

public class TreeDerivationParser {

    private boolean treeComplete;
    private Grammar grammar;
    private NodeDerivationParser root;
    private String word;
    private int index;
    private MostLeftDerivationTable mostLeftDerivationTable;
    private NodeDerivationParser actualNode;
    private TreeDerivation treeDerivation;
    private TreeDerivation treeDerivationAux;
    private int countNodes;
    private static final int MAX_NODES = 250;
    private static final int MAX_LEVEL = 100;

    /**
     * Construtor de árvore de derivação com base em uma gramática _grammar_ e uma palavra a ser
     * derivada _word_.
     * @param grammar gramática a ser usada na derivação
     * @param word palavra a ser derivada
     */
    public TreeDerivationParser(Grammar grammar, String word) {
        this.word = word;
        this.grammar = grammar;
        index = 0;
    }

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
        if (countNodes >= MAX_NODES || actualNode.getLevel() == MAX_LEVEL) {
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
        if (index == word.length()) {
            cutBranch();
            return;
        }
        String variable = actualNode.getNode();
        String terminal = Character.toString(word.charAt(index));
        Deque<Rule> rules = mostLeftDerivationTable.getRules(variable, terminal);
        actualNode.setStackRules(rules);
    }

    /**
     * Atualiza o índice da palavra para cortar o ramo, onde o parâmetro rootBranch é a raiz do
     * ramo.
     * Atualiza o contador de nós.
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
        return index != word.length() && (symbol.charAt(0) == word.charAt(index)
                || symbol.equals(Grammar.LAMBDA));
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
                treeDerivation = new TreeDerivation(root);
            } else {
                treeDerivationAux = new TreeDerivation(root);
            }
        } else {
        }
    }

    /**
     * Busca um próximo nó folha na árvore, a ordem para essa busca é a pré-ordem. Caso não seja
     * encontrado um nó define o nó atual como a raiz, isto também indica que uma árvore foi
     * completada.
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
    

    /**
     * Converte essa arvóre de derivação para texto.
     * @return texto com informações da arvóre de derivação
     */
    @Override
    public String toString() {
        TreeDerivation treeParser = new TreeDerivation(root);
        return treeParser.getDerivation();
    }

}
