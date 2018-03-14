package com.ufla.lfapp.core.grammar.parser;

import android.graphics.Point;

import com.ufla.lfapp.utils.MyPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by carlos on 2/21/17.
 */

public class TreeDerivationPosition implements Serializable {

    private NodeDerivationPosition root;
    private Map<Integer, List<List<NodeDerivationPosition>>> nodesByLevel;
    private int levelMax;
    private int minXPosition = 1;

    public TreeDerivationPosition(NodeDerivation root) {
        this.root = NodeDerivationPosition.getRootFromNode(root);
        fillNodesByLevel();
        setPositions();
        updateXToPositive();
    }

    /**
     * Recupera todos os nós da árvore.
     * @return lista com todos os nós da árvore
     */
    public List<NodeDerivationPosition> getNodes() {
        List<NodeDerivationPosition> nodes = new ArrayList<>();
        Collection<List<List<NodeDerivationPosition>>> tree = nodesByLevel.values();
        for (List<List<NodeDerivationPosition>> nodesLevel : tree) {
            for (List<NodeDerivationPosition> nodesSingleFather : nodesLevel) {
                nodes.addAll(nodesSingleFather);
            }
        }
        return nodes;
    }

    public NodeDerivationPosition getRoot() {
        return root;
    }

    public Map<Integer, List<List<NodeDerivationPosition>>> getNodesByLevel() {
        return nodesByLevel;
    }

    /**
     * Atualiza o valor do x de todas as posições dos nós em relação ao menor x da árvore. Para que
     * o menor x da árvore se torne 0.
     */
    private void updateXToPositive() {
        List<NodeDerivationPosition> nodes = getNodes();
        minXPosition--;
        for (NodeDerivationPosition node : nodes) {
            MyPoint position = node.getPosition();
            position.x -= minXPosition;
        }
    }

    /**
     * Recupera a posição do nó pai.
     * @param node nó em que deseja a posição do pai
     * @return posição do nó pai
     */
    private MyPoint getFatherPosition(NodeDerivationPosition node) {
        return node.getFather().getPosition();
    }

    /**
     * Realiza um movimento à esquerda de tamanho _move_ em todos os nós pertencentes as primeiras
     * _numberOfLists_ - 1 pertencetes a lista de lista recebida por parâmetro.
     * _numberOfLists_ é um limite superior de índice não incluso.
     * @param nodes listas de nós, onde um subconjunto de nós serão movidos à esquerda
     * @param move tamanho do movimento à esquerda
     * @param numberOfLists limitante superior do índice das listas a serem movidas à esquerda,
     *                      limite não incluso
     */
    private void moveLeft(List<List<NodeDerivationPosition>> nodes, int move,
                             int numberOfLists) {
        int minX = nodes.get(0).get(0).getPosition().x - move;
        if (minX < minXPosition) {
            minXPosition = minX;
        }
        for (int i = 0; i < numberOfLists; i++) {
            List<NodeDerivationPosition> nodesSingleFather = nodes.get(i);
            int maxNodes = nodesSingleFather.size();
            for (int j = 0; j < maxNodes; j++) {
                NodeDerivationPosition node = nodesSingleFather.get(j);
                MyPoint position = node.getPosition();
                position.x -= move;
            }
        }
    }

    /**
     * Desloca todos os nós dos níveis _level_ até o nível 1, ambos inclusos. O deslocamento é
     * realizado em relação aos nós pais recebidos por parâmetro, até o nó _father1_ o deslocamento
     * é à esquerda depois deste nó é a direita. No entanto, se o nó _father1_ é igual ao nó
     * _father2_ este nó não será movido. O tamanho do deslocamento é _desloc_.
     * @param level nível da árvore onde deve começar o deslocamento
     * @param desloc tamanho do deslocamento
     * @param father1 nó limitante superior do deslocamento à esquerda
     * @param father2 nó limitante inferior do deslocamento à direita
     */
    private void deslocFathers(int level, int desloc, NodeDerivationPosition father1,
                          NodeDerivationPosition father2) {
        while (level > 0) {
            List<List<NodeDerivationPosition>> nodes = nodesByLevel.get(level);
            desloc = -desloc;
            int minX = nodes.get(0).get(0).getPosition().x + desloc;
            if (minX < minXPosition) {
                minXPosition = minX;
            }
            int maxLists = nodes.size();
            if (father1.equals(father2)) {
                for (int i = 0; i < maxLists; i++) {
                    List<NodeDerivationPosition> nodesSingleFather = nodes.get(i);
                    int maxNodes = nodesSingleFather.size();
                    for (int j = 0; j < maxNodes; j++) {
                        NodeDerivationPosition node = nodesSingleFather.get(j);
                        if (node.equals(father1)) {
                            desloc = -desloc;
                        } else {
                            MyPoint position = node.getPosition();
                            position.x += desloc;
                        }
                    }
                }
            } else {
                for (int i = 0; i < maxLists; i++) {
                    List<NodeDerivationPosition> nodesSingleFather = nodes.get(i);
                    int maxNodes = nodesSingleFather.size();
                    for (int j = 0; j < maxNodes; j++) {
                        NodeDerivationPosition node = nodesSingleFather.get(j);
                        MyPoint position = node.getPosition();
                        position.x += desloc;
                        if (node.equals(father1)) {
                            desloc = -desloc;
                        }
                    }
                }
            }
            level--;
            father1 = father1.getFather();
            father2 = father2.getFather();
        }
    }

    /**
     * Define as posições dos nós na árvore de derivação.
     */
    private void setPositions() {
        root.setPosition(new Point(1, 1));
        for (int i = 1; i < levelMax; i++) {
            List<List<NodeDerivationPosition>> nodes = nodesByLevel.get(i);
            int maxLists = nodes.size();
            int maxXPosition = 0;
            for (int j = 0; j < maxLists; j++) {
                List<NodeDerivationPosition> nodesSingleFather = nodes.get(j);
                MyPoint fatherPosition = getFatherPosition(nodesSingleFather.get(0));
                int y = fatherPosition.y + 2;
                int maxNodes = nodesSingleFather.size();
                int x = fatherPosition.x - (maxNodes - 1);
                if (j == 0 && x < minXPosition) {
                    minXPosition = x;
                } else if (j > 0 && x < maxXPosition) {
                    int dif = maxXPosition - x;
                    if (dif % 2 == 1) {
                        dif++;
                    }
                    dif /= 2;
                    x += dif;
                    moveLeft(nodes, dif, j);
                    NodeDerivationPosition father1 = nodes.get(j-1).get(0).getFather();
                    NodeDerivationPosition father2 = nodesSingleFather.get(0).getFather();
                    deslocFathers(i - 1, dif, father1, father2);
                }
                for (int k = 0; k < maxNodes; k++) {
                    NodeDerivationPosition node = nodesSingleFather.get(k);
                    node.setPosition(new Point(x, y));
                    x += 2;
                }
                maxXPosition = x + 2;
            }
        }

    }

    /**
     * Preenche o mapa dos nós pelo nível da árvore, cada nível é formado por uma lista de lista de
     * nós. A lista interior na estrutura corresponde aos nós que possuem um mesmo pai.
     */
    private void fillNodesByLevel() {
        nodesByLevel = new HashMap<>();
        List<List<NodeDerivationPosition>> nodes = new ArrayList<>();
        List<NodeDerivationPosition> nodesSingleFather = new ArrayList<>();
        nodesSingleFather.add(root);
        nodes.add(nodesSingleFather);
        int contLevel = 0;
        while (!nodes.isEmpty()) {
            nodesByLevel.put(contLevel, nodes);
            contLevel++;
            List<List<NodeDerivationPosition>> newNodes = new ArrayList<>();
            for (List<NodeDerivationPosition> nodesGroup : nodes) {
                for (NodeDerivationPosition node : nodesGroup) {
                    nodesSingleFather = node.getChilds();
                    if (nodesSingleFather != null && !nodesSingleFather.isEmpty()) {
                        newNodes.add(nodesSingleFather);
                    }
                }
            }
            nodes = newNodes;
        }
        levelMax = contLevel;
    }

}
