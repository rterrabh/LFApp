package com.ufla.lfapp.vo.machine;

import android.graphics.Point;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by carlos on 12/7/16.
 */

public class DotLanguage implements Serializable {

    private static final String DIGRAPH = "digraph";
    private static final String SEMICOLON = ";";
    private static final String STYLE_ATTR = "style";
    private static final String LABEL_ATTR = "label";
    private static final String POS_ATTR = "pos";
    private static final String STYLE_INITIAL = "initial";
    private static final String STYLE_FINAL = "final";
    private static final String STYLE_INITIAL_AND_FINAL = "initial/final";
    private static final String EDGE = " -> ";
    private static final String TAB = "\t";
    private static final String END_LINE = "\n";
    private static final String EQUALS = "=";
    private static final String COMMA = ",";
    private static final String UNTITLED = "untitled";

    /**
     * String com o texto que representa o grafo na linguagem dot.
     */
    private long id;
    private String graph;
    private String label;
    private int contUses;
    private Date creationDate;

    /**
     * Cria uma instância de <code>DotLanguage</code> que representa o autômato passado
     * por parâmetro em forma de grafo.
     *
     * @param automatonGUI autômato a ser usado para criar grafo na linguagem dot
     */
    public DotLanguage (AutomatonGUI automatonGUI) {
        id = automatonGUI.getId();
        label = automatonGUI.getLabel();
        contUses = automatonGUI.getContUses();
        creationDate = automatonGUI.getCreationDate();
        this.graph = parseDotLanguageIntern(automatonGUI);
    }

    public DotLanguage(long id, String graph, String label, int contUses, Date creationDate) {
        this.id = id;
        this.graph = graph;
        this.label = label;
        this.contUses = contUses;
        this.creationDate = creationDate;
    }

    public DotLanguage(String graph) {
        this.id = -1;
        this.graph = graph;
        this.label = null;
        this.contUses = -1;
        this.creationDate = null;
    }

    /**
     * Converte as transições de um autômato, do domínio da aplicação LFApp, para arestas
     * de um grafo na linguagem dot.
     *
     * @param automatonGUI autômato com transições a ser convertidas para arestas na
     *                     linguagem dot
     * @return string com a representação textual das transições do autômato como arestas
     * na linguagem dot
     */
    private static String parseDotLanguageEdges(AutomatonGUI automatonGUI) {
        StringBuilder graphSb = new StringBuilder();
        for (TransitionFunction transitionFunc : automatonGUI.transitionFunctions) {
            graphSb.append(TAB).append(transitionFunc.getCurrentState()).append(EDGE)
                    .append(transitionFunc.getFutureState()).append(" [").append(LABEL_ATTR)
                    .append(EQUALS).append(transitionFunc.getSymbol()).append("]")
                    .append(SEMICOLON).append(END_LINE);
        }
        return graphSb.toString();
    }

    /**
     * Converte os estados de um autômato, do domínio da aplicação LFApp, para vértices
     * de um grafo na linguagem dot.
     *
     * @param automatonGUI autômato com estados a ser convertido para vértices na
     *                     linguagem dot
     * @return string com a representação textual dos estados do autômato como vértices
     * na linguagem dot
     */
    private static String parseDotLanguageVertex(AutomatonGUI automatonGUI) {
        StringBuilder graphSb = new StringBuilder();
        for (State state : automatonGUI.states) {
            Point pos = automatonGUI.getGridPosition(state);
            graphSb.append(TAB).append(state.getName()).append(" [").append(POS_ATTR).append(EQUALS)
                    .append(pos.x).append(COMMA).append(pos.y).append("]");
            if (automatonGUI.isInitialState(state) && automatonGUI.isFinalState(state)) {
                graphSb.append(" [").append(STYLE_ATTR).append(EQUALS)
                        .append(STYLE_INITIAL_AND_FINAL).append("]");
            } else if (automatonGUI.isInitialState(state)) {
                graphSb.append(" [").append(STYLE_ATTR).append(EQUALS)
                        .append(STYLE_INITIAL).append("]");
            } else if (automatonGUI.isFinalState(state)) {
                graphSb.append(" [").append(STYLE_ATTR).append(EQUALS)
                        .append(STYLE_FINAL).append("]");
            }
            graphSb.append(SEMICOLON).append(END_LINE);
        }
        return graphSb.toString();
    }

    /**
     * Converte uma representação de um autômato, do domínio da aplicação LFApp, para
     * string com uma representação em grafos na linguagem dot.
     *
     * @param automatonGUI autômato a ser convertido para linguagem dot
     *
     * @return string com o texto da representação do autômato em grafos na
     * linguagem dot
     */
    private static String parseDotLanguageIntern(AutomatonGUI automatonGUI) {
        StringBuilder graphSb = new StringBuilder();
        graphSb.append(DIGRAPH).append(' ').append(UNTITLED).append(" {").append(END_LINE)
                .append(parseDotLanguageVertex(automatonGUI))
                .append(parseDotLanguageEdges(automatonGUI))
                .append("}").append(END_LINE);
        return graphSb.toString();
    }

    /**
     * Converte uma representação de um autômato, do domínio da aplicação LFApp, para
     * uma representação em grafos na linguagem dot.
     *
     * @param automatonGUI autômato a ser convertido para linguagem dot
     *
     * @return representação do autômato em grafos na linguagem dot
     */
    public static DotLanguage parseDotLanguage(AutomatonGUI automatonGUI) {
        return new DotLanguage(automatonGUI);
    }


    public AutomatonGUI toAutomatonGUI() {
        AutomatonGUIBuilder automatonGUIBuilder = new AutomatonGUIBuilder();
        String lines[] = graph.split(END_LINE);
        int length = lines.length - 1;
        int ind = 0;
        if (label == null) {
            label = UNTITLED;
        }
        automatonGUIBuilder.setLabel(label);
        String aux[] = lines[ind].split(" ");
        if (aux.length >= 2) {
            //this.label = aux[1];
            //automatonGUIBuilder.setLabel(label);
        }
        ind++;
        String posAttr = "[" + POS_ATTR + EQUALS;
        while (ind < length && !lines[ind].contains(EDGE)) {
            aux = lines[ind].split("[ \\t]");
            int auxLenght = aux.length;
            int auxInd = 0;
            String stateStr = "";
            while (!aux[auxInd].contains(posAttr)) {
                stateStr += aux[auxInd] + " ";
                auxInd++;
            }
            stateStr = stateStr.trim();
            int initialInd = aux[auxInd].indexOf(EQUALS) + 1;
            int commaInd = aux[auxInd].indexOf(',');
            int finalInd = aux[auxInd].length() - 1;
            if (aux[auxInd].charAt(finalInd) == ';') {
                finalInd--;
            }
            Point statePosition = new Point();
            statePosition.x = Integer.parseInt(aux[auxInd].substring(initialInd, commaInd));
            statePosition.y = Integer.parseInt(aux[auxInd].substring(commaInd + 1, finalInd));
            auxInd++;
            State state = new State(stateStr);
            automatonGUIBuilder.addOrChangeStatePosition(state, statePosition);
            if (auxInd < auxLenght) {
                initialInd = aux[auxInd].indexOf(EQUALS) + 1;
                finalInd = aux[auxInd].length() - 2;
                String style = aux[auxInd].substring(initialInd, finalInd);
                if (STYLE_FINAL.equals(style)) {
                    automatonGUIBuilder.defineFinalState(state);
                } else if (STYLE_INITIAL.equals(style)) {
                    automatonGUIBuilder.withInitialState(state);
                } else if (STYLE_INITIAL_AND_FINAL.equals(style)) {
                    automatonGUIBuilder.withInitialState(state);
                    automatonGUIBuilder.defineFinalState(state);
                }
            }
            ind++;
        }
        String labelAttr = "[" + LABEL_ATTR + EQUALS;
        String edgeTrim = EDGE.trim();
        while (ind < length) {
            aux = lines[ind].split("[ \\t]");
            int auxLenght = aux.length;
            int auxInd = 0;
            String currentState = "";
            while (auxInd < auxLenght && !aux[auxInd].contains(edgeTrim)) {
                currentState += aux[auxInd] + " ";
                auxInd++;
            }
            auxInd++;
            currentState = currentState.trim();
            String futureState = "";
            while (auxInd < auxLenght && !aux[auxInd].contains(labelAttr)) {
                futureState += aux[auxInd] + " ";
                auxInd++;
            }
            futureState = futureState.trim();
            int initialInd = labelAttr.length();
            String symbol = "";
            if (auxInd == auxLenght - 1) {
                symbol = aux[auxInd].substring(initialInd, aux[auxInd].length() - 2);
            } else if (auxInd < auxLenght) {
                symbol = aux[auxInd].substring(initialInd, aux[auxInd].length());
                while (auxInd < auxLenght - 1) {
                    symbol += " " + aux[auxInd];
                    auxInd++;
                }
                symbol += " " + aux[auxInd].substring(0, aux[auxInd].length());
            }
            State currentStateSt = automatonGUIBuilder.getState(currentState);
            State futureStateSt = automatonGUIBuilder.getState(futureState);
            if (currentStateSt == null || symbol == null || futureStateSt == null) {
                ind++;
                continue;
            }
            automatonGUIBuilder.addTransition(currentStateSt, symbol, futureStateSt);
            ind++;
        }
        automatonGUIBuilder.setLabel(label)
                .setId(id)
                .setContUses(contUses)
                .setCreationDate(creationDate);
        return automatonGUIBuilder.createAutomatonGUI();
    }

    public String getGraph() {
        return graph;
    }

    public String getLabel() {
        return label;
    }

    public int getContUses() {
        return contUses;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public long getId() {
        return id;
    }


    /**
     * Retorna a string com o texto na linguagem dot que representa o grafo.
     *
     * @return string com o texto na linguagem dot que representa o grafo.
     */
    @Override
    public String toString() {
        return graph;
    }

}
