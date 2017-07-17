package com.ufla.lfapp.core.machine.dotlang;

import android.graphics.Point;
import android.support.v4.util.Pair;

import com.ufla.lfapp.core.grammar.parser.NodeDerivationPosition;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationPosition;
import com.ufla.lfapp.core.machine.Machine;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.core.machine.fsa.FiniteStateFiniteStateAutomatonGUIBuilder;
import com.ufla.lfapp.core.machine.pda.PDAExtTransitionFunction;
import com.ufla.lfapp.core.machine.pda.PDATransitionFunction;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.core.machine.pda.PushdownAutomatonExtend;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.core.machine.tm.TMTransitionFunction;
import com.ufla.lfapp.core.machine.tm.TuringMachine;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTapeTransitionFunction;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTrackTransitionFunction;
import com.ufla.lfapp.core.machine.tm.var.TuringMachineMultiTape;
import com.ufla.lfapp.core.machine.tm.var.TuringMachineMultiTrack;
import com.ufla.lfapp.utils.MyPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
    private String graph;
    private Long id;
    private MachineType machineType;
    private String label;
    private Integer contUses;
    private Date creationDate;

//    /**
//     * Cria uma instância de <code>DotLanguage</code> que representa o autômato passado
//     * por parâmetro em forma de grafo.
//     *
//     * @param automatonGUI autômato a ser usado para criar grafo na linguagem dot
//     */
//    public DotLanguage (FiniteStateAutomatonGUI automatonGUI) {
//        this.id = automatonGUI.getId();
//        this.label = automatonGUI.getLabel();
//        this.contUses = automatonGUI.getContUses();
//        this.creationDate = automatonGUI.getCreationDate();
//        this.graph = parseDotLanguageIntern(automatonGUI);
//    }

    public MachineType getMachineType() {
        return machineType;
    }

    public void setMachineType(MachineType machineType) {
        this.machineType = machineType;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public DotLanguage(Machine machine, Map<State, MyPoint> stateToPoint) {
        this.id =  machine.getId();
        this.label = machine.getLabel();
        this.contUses = machine.getContUses();
        this.creationDate = machine.getCreationDate();
        this.machineType = machine.getMachineType();
        this.graph = parseDotLanguageIntern(machine, stateToPoint);
        if (this.creationDate == null) {
            creationDate = new Date();
        }
    }



    public DotLanguage(long id, String graph, String label, int contUses, Date creationDate) {
        this.id = id;
        this.graph = graph;
        this.label = label;
        this.contUses = contUses;
        this.creationDate = creationDate;
    }



    public DotLanguage(long id, String graph, String label, int contUses, Date creationDate,
                       MachineType machineType) {
        this.id = id;
        this.graph = graph;
        this.label = label;
        this.contUses = contUses;
        this.creationDate = creationDate;
        this.machineType = machineType;
    }

    public DotLanguage(String graph) {
        this.graph = graph;
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
    private static String parseDotLanguageEdges(FiniteStateAutomatonGUI automatonGUI) {
        StringBuilder graphSb = new StringBuilder();
        for (FSATransitionFunction transitionFunc : automatonGUI.getTransitionFunctions()) {
            graphSb.append(TAB)
                    .append(transitionFunc.getCurrentState())
                    .append(EDGE)
                    .append(transitionFunc.getFutureState())
                    .append(" [")
                    .append(LABEL_ATTR)
                    .append(EQUALS)
                    .append(transitionFunc.getSymbol())
                    .append("]")
                    .append(SEMICOLON)
                    .append(END_LINE);
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
    private static String parseDotLanguageVertex(FiniteStateAutomatonGUI automatonGUI) {
        StringBuilder graphSb = new StringBuilder();
        for (State state : automatonGUI.getStates()) {
            Point pos = automatonGUI.getGridPosition(state);
            graphSb.append(TAB)
                    .append(state.getName())
                    .append(" [")
                    .append(POS_ATTR)
                    .append(EQUALS)
                    .append(pos.x)
                    .append(COMMA)
                    .append(pos.y)
                    .append("]");
            if (automatonGUI.isInitialState(state) && automatonGUI.isFinalState(state)) {
                graphSb.append(" [")
                        .append(STYLE_ATTR)
                        .append(EQUALS)
                        .append(STYLE_INITIAL_AND_FINAL)
                        .append("]");
            } else if (automatonGUI.isInitialState(state)) {
                graphSb.append(" [")
                        .append(STYLE_ATTR)
                        .append(EQUALS)
                        .append(STYLE_INITIAL)
                        .append("]");
            } else if (automatonGUI.isFinalState(state)) {
                graphSb.append(" [")
                        .append(STYLE_ATTR)
                        .append(EQUALS)
                        .append(STYLE_FINAL)
                        .append("]");
            }
            graphSb.append(SEMICOLON)
                    .append(END_LINE);
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
    private static String parseDotLanguageIntern(FiniteStateAutomatonGUI automatonGUI) {
        StringBuilder graphSb = new StringBuilder();
        graphSb.append(DIGRAPH)
                .append(' ')
                .append(UNTITLED)
                .append(" {")
                .append(END_LINE)
                .append(parseDotLanguageVertex(automatonGUI))
                .append(parseDotLanguageEdges(automatonGUI))
                .append("}")
                .append(END_LINE);
        return graphSb.toString();
    }

    private static String getFinalStatesStr(Machine machine) {
        Set<State> finalStates = machine.getFinalStates();
        if (finalStates.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder('\t');
        for (State s : finalStates) {
            sb.append('\"')
                    .append(s)
                    .append("\", ");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.setCharAt(sb.length()-1, ';');
        return sb.toString();
    }


    private static String parseDotLanguageVertex(Machine machine,
                                                 Map<State, MyPoint> stateToPoint) {
        StringBuilder sb = new StringBuilder("");
        Set<State> states = machine.getStates();
        for (State s : states) {
            MyPoint p = stateToPoint.get(s);
            sb.append("\t\"")
                    .append(s)
                    .append("\" [label=<")
                    .append(s.getLabel())
                    .append(">, pos=\"")
                    .append(p.x)
                    .append(',')
                    .append(p.y)
                    .append("!\"];\n");
        }
        return sb.toString();
    }

    private static String parseDotLanguageEdges(Machine machine) {
        Map<Pair<State, State>, StringBuilder> transitions =
                machine.getTransitionsForDotLang();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Pair<State, State>, StringBuilder> e : transitions.entrySet()) {
            Pair<State, State> p = e.getKey();
            sb.append("\t\"")
                    .append(p.first)
                    .append("\" -> \"")
                    .append(p.second)
                    .append("\" [label=\"")
                    .append(e.getValue())
                    .append("\"];\n");
        }
        return sb.toString();
    }

    private static String parseDotLanguageIntern(Machine machine,
                                                 Map<State, MyPoint> stateToPoint) {
        return String.format("digraph %s {\n\n" +
                "\tdpi = 480;\n" +
                "\trankdir=LR;\n\n" +
                "\t\"startRes\" [shape=point, style=\"invis\"];\n" +
                "\tnode [shape = doublecircle, color=\"#968D8D\", fontcolor=black, fillcolor=\"#FFCCCC\", style=\"filled\"];\n" +
                "%s\n\n" +
                "\tnode [shape = circle];\n\n" +
                "%s\n" +
                "\t\"startRes\" -> \"%s\";\n\n" +
                "%s\n\n}",
                UNTITLED,
                getFinalStatesStr(machine),
                parseDotLanguageVertex(machine, stateToPoint),
                machine.getInitialState(),
                parseDotLanguageEdges(machine));
    }

    public String[][] parametersMachine() {
        String[][] parameters = new String[4][];
        String[] lines = graph.split("\n");
        int ind = 0;
        int length = lines.length;
        // BEGIN - FINAL_STATES_PARAMETER
        while (ind < length
                && !lines[ind].startsWith("\tnode ")) {
            ind++;
        }
        ind++;
        lines[ind] = lines[ind].trim();
        if (!lines[ind].isEmpty()) {
            parameters[0] = lines[ind].substring(1, lines[ind].length() - 2).split("\", \"");
        } else {
            parameters[0] = new String[0];
        }
        // END - FINAL_STATES_PARAMETER
        // BEGIN - STATES_POINT_PARAMETER
        ind += 4;
        List<String> statesList = new ArrayList<>();
        final String PATTERN_POS = ", pos=\"";
        final int PATTERN_POS_LENGTH = PATTERN_POS.length();
        while (!lines[ind].isEmpty()) {
            int init = 2;
            int end = lines[ind].indexOf('"', init);
            StringBuilder sb = new StringBuilder();
            sb.append(lines[ind].substring(init, end))
                    .append('\n');
            init = lines[ind].indexOf(PATTERN_POS, end) + PATTERN_POS_LENGTH;
            end = lines[ind].length() - 4;
            sb.append(lines[ind].substring(init, end));
            statesList.add(sb.toString());
            ind++;
        }
        parameters[1] = statesList.toArray(new String[0]);
        statesList.clear();
        ind++;
        // END - STATES_POINT_PARAMETER
        // BEGIN - START_STATE_PARAMETER
        int end = lines[ind].length()-2;
        int init = lines[ind].lastIndexOf(" \"") + 2;
        parameters[2] = new String[] { lines[ind].substring(init, end) };
        // END - START_STATE_PARAMETER
        // BEGIN - TRANSITIONS_PARAMETER
        ind += 2;
        final String PATTERN_ARROW = "\" -> \"";
        final int PATTERN_ARROW_LENGHT = PATTERN_ARROW.length();
        final String PATTERN_LABEL = "\" [label=\"";
        final int PATTERN_LABEL_LENGHT = PATTERN_LABEL.length();
        List<String> transitionsList = new ArrayList<>();
        while (!lines[ind].isEmpty()) {
            StringBuilder sb = new StringBuilder();
            init = 2;
            end = lines[ind].indexOf(PATTERN_ARROW);
            sb.append(lines[ind].substring(init, end))
                    .append('\n');
            init = end + PATTERN_ARROW_LENGHT;
            end = lines[ind].indexOf(PATTERN_LABEL, init);
            sb.append(lines[ind].substring(init, end))
                    .append('\n');
            init = end + PATTERN_LABEL_LENGHT;
            end = lines[ind].length()-3;
            sb.append(lines[ind].substring(init, end));
            transitionsList.add(sb.toString());
            ind++;
        }
        parameters[3] = transitionsList.toArray(new String[0]);
        // END - TRANSITIONS_PARAMETER
        return parameters;
    }


//    /**
//     * Converte uma representação de um autômato, do domínio da aplicação LFApp, para
//     * uma representação em grafos na linguagem dot.
//     *
//     * @param automatonGUI autômato a ser convertido para linguagem dot
//     *
//     * @return representação do autômato em grafos na linguagem dot
//     */
//    public static DotLanguage parseDotLanguage(FiniteStateAutomatonGUI automatonGUI) {
//        return new DotLanguage(automatonGUI);
//    }

    private static String parseDotLanguageEdges(TreeDerivationPosition tree) {
        Map<Integer, List<List<NodeDerivationPosition>>> nodesByLevel =
                tree.getNodesByLevel();
        StringBuilder graphSb = new StringBuilder();
        for (Map.Entry<Integer, List<List<NodeDerivationPosition>>> entry :
                nodesByLevel.entrySet()) {
            if (entry.getKey() == 0) {
                continue;
            }
            for (List<NodeDerivationPosition> nodesSingleFather : entry.getValue()) {
                NodeDerivationPosition father = nodesSingleFather.get(0).getFather();
                for (NodeDerivationPosition node : nodesSingleFather) {
                    graphSb.append(TAB)
                            .append(father.getNode())
                            .append(EDGE)
                            .append(node.getNode())
                            .append(SEMICOLON).append(END_LINE);
                }
            }
        }
        return graphSb.toString();
    }

    private static String parseDotLanguageVertex(TreeDerivationPosition tree) {
        List<NodeDerivationPosition> nodes = tree.getNodes();
        StringBuilder graphSb = new StringBuilder();
        for (NodeDerivationPosition node : nodes) {
            Point pos = node.getPosition().toPoint();
            graphSb.append(TAB)
                    .append(node.getNode())
                    .append(" [")
                    .append(POS_ATTR)
                    .append(EQUALS)
                    .append(pos.x)
                    .append(COMMA)
                    .append(pos.y)
                    .append("]")
                    .append(SEMICOLON)
                    .append(END_LINE);
        }
        return graphSb.toString();
    }


    private static String parseDotLanguageIntern(TreeDerivationPosition tree) {
        StringBuilder graphSb = new StringBuilder();
        graphSb.append(DIGRAPH)
                .append(' ')
                .append(UNTITLED)
                .append(" {")
                .append(END_LINE)
                .append(parseDotLanguageVertex(tree))
                .append(parseDotLanguageEdges(tree))
                .append("}")
                .append(END_LINE);
        return graphSb.toString();
    }


    public static DotLanguage parseDotLanguage(TreeDerivationPosition tree) {
        String graph = parseDotLanguageIntern(tree);
        return new DotLanguage(graph);
    }

    public SortedSet<FSATransitionFunction> getFSATransitionFunctions(String[] labels,
                                                          Map<String, State> nameToState) {
        SortedSet<FSATransitionFunction> transitionFunctions = new TreeSet<>();
        for (String label : labels) {
            String param[] = label.split("\n");
            State current = nameToState.get(param[0]);
            State future = nameToState.get(param[1]);
            String[] transitionsStr = param[2].split("\\\\n");
            for (String transStr : transitionsStr) {
                transitionFunctions.add(new FSATransitionFunction(current, transStr, future));
            }
        }
        return transitionFunctions;
    }

    public SortedSet<PDATransitionFunction> getPDATransitionFunctions(String[] labels,
                                                                      Map<String, State> nameToState) {
        SortedSet<PDATransitionFunction> transitionFunctions = new TreeSet<>();
        for (String label : labels) {
            String param[] = label.split("\n");
            State current = nameToState.get(param[0]);
            State future = nameToState.get(param[1]);
            String[] transitionsStr = param[2].split("\\\\n");
            for (String transStr : transitionsStr) {
                String[] transParam = transStr.split("[/ ]");
                transitionFunctions.add(new PDATransitionFunction(current, transParam[0], future,
                        transParam[2], transParam[1]));
            }
        }
        return transitionFunctions;
    }

    public List<String> stringToList(String str) {
        List<String> list = new ArrayList<>();
        int n = str.length();
        for (int i = 0; i < n; i++) {
            list.add(String.valueOf(str.charAt(i)));
        }
        return list;
    }

    public SortedSet<PDAExtTransitionFunction> getPDAExtTransitionFunctions(String[] labels,
                                                                      Map<String, State> nameToState) {
        SortedSet<PDAExtTransitionFunction> transitionFunctions = new TreeSet<>();
        for (String label : labels) {
            String param[] = label.split("\n");
            State current = nameToState.get(param[0]);
            State future = nameToState.get(param[1]);
            String[] transitionsStr = param[2].split("\\\\n");
            for (String transStr : transitionsStr) {
                String[] transParam = transStr.split("[/ ]");

                transitionFunctions.add(new PDAExtTransitionFunction(current, transParam[0], future,
                        stringToList(transParam[2]), transParam[1]));
            }
        }
        return transitionFunctions;
    }

    public SortedSet<TMTransitionFunction> getTMTransitionFunctions(String[] labels,
                                                                            Map<String, State> nameToState) {
        SortedSet<TMTransitionFunction> transitionFunctions = new TreeSet<>();
        for (String label : labels) {
            String param[] = label.split("\n");
            State current = nameToState.get(param[0]);
            State future = nameToState.get(param[1]);
            String[] transitionsStr = param[2].split("\\\\n");
            for (String transStr : transitionsStr) {
                String[] transParam = transStr.split("[/ ]");
                transitionFunctions.add(new TMTransitionFunction(current, transParam[0], future,
                        transParam[1], TMMove.getInstance(transParam[2])));
            }
        }
        return transitionFunctions;
    }

    public String[][] tapesToTapesParam(String[] tapes) {
        int n = tapes.length;
        String[][] tapesParam = new String[n][];
        for (int i = 0; i < n; i++) {
            tapesParam[i] = tapes[i].split("[/ ]");
        }
        return tapesParam;
    }

    public TMMove[] movesInTapesParam(String[][] tapesParam) {
        int n = tapesParam.length;
        TMMove[] moves = new TMMove[n];
        for (int i = 0; i < n; i++) {
            moves[i] = TMMove.getInstance(tapesParam[i][2]);
        }
        return moves;
    }

    public String[] readSymbolsInTapesParam(String[][] tapesParam) {
        int n = tapesParam.length;
        String[] readSymbols = new String[n];
        for (int i = 0; i < n; i++) {
            readSymbols[i] = tapesParam[i][0];
        }
        return readSymbols;
    }

    public String[] writeSymbolsInTapesParam(String[][] tapesParam) {
        int n = tapesParam.length;
        String[] writeSymbols = new String[n];
        for (int i = 0; i < n; i++) {
            writeSymbols[i] = tapesParam[i][1];
        }
        return writeSymbols;
    }

    public SortedSet<TMMultiTapeTransitionFunction> getTMMultiTapeTransitionFunctions(String[] labels,
                                                                                       Map<String, State> nameToState) {
        SortedSet<TMMultiTapeTransitionFunction> transitionFunctions = new TreeSet<>();
        for (String label : labels) {
            String param[] = label.split("\n");
            State current = nameToState.get(param[0]);
            State future = nameToState.get(param[1]);
            String[] transitionsStr = param[2].split("\\\\n");
            for (String transStr : transitionsStr) {
                String[] transTapes = transStr.split(", ");
                transTapes[0] = transTapes[0].substring(1);
                int nTape = transTapes.length - 1;
                int nTapeLength = transTapes[nTape].length();
                transTapes[nTape] = transTapes[nTape].substring(0, nTapeLength-1);
                String[][] tapesParam = tapesToTapesParam(transTapes);
                transitionFunctions.add(new TMMultiTapeTransitionFunction(current,
                        future,
                        readSymbolsInTapesParam(tapesParam),
                        writeSymbolsInTapesParam(tapesParam),
                        movesInTapesParam(tapesParam)));
            }
        }
        return transitionFunctions;
    }

    public String[] readSymbolsInTracksParam(String[] tracksParam) {
        int n = (tracksParam.length-1) / 2;
        String[] readSymbols = new String[n];
        for (int i = 0; i < n; i++) {
            readSymbols[i] = tracksParam[i*2];
        }
        return readSymbols;
    }

    public String[] writeSymbolsInTracksParam(String[] tracksParam) {
        int n = (tracksParam.length-1) / 2;
        String[] writeSymbols = new String[n];
        for (int i = 0; i < n; i++) {
            writeSymbols[i] = tracksParam[(i*2) + 1];
        }
        return writeSymbols;
    }

    public SortedSet<TMMultiTrackTransitionFunction> getTMMultiTrackTransitionFunctions(String[] labels,
                                                                                      Map<String, State> nameToState) {
        SortedSet<TMMultiTrackTransitionFunction> transitionFunctions = new TreeSet<>();
        for (String label : labels) {
            String param[] = label.split("\n");
            State current = nameToState.get(param[0]);
            State future = nameToState.get(param[1]);
            String[] transitionsStr = param[2].split("\\\\n");
            for (String transStr : transitionsStr) {
                String[] tracksParam = transStr.split("[/ ]");
                tracksParam[0] = tracksParam[0].substring(1);
                int nTape = tracksParam.length - 1;
                int nTapeLength = tracksParam[nTape].length();
                tracksParam[nTape] = tracksParam[nTape].substring(0, nTapeLength-1);
                transitionFunctions.add(new TMMultiTrackTransitionFunction(current,
                        future,
                        readSymbolsInTracksParam(tracksParam),
                        writeSymbolsInTracksParam(tracksParam),
                        TMMove.getInstance(tracksParam[nTape])));
            }
        }
        return transitionFunctions;
    }

    public Map<String, State> createNameToState(String[] names) {
        Map<String, State> nameToState = new HashMap<>();
        for (String name : names) {
            int end = name.indexOf('\n');
            String stateName = name.substring(0, end);
            nameToState.put(stateName, new State(stateName));
        }
        return nameToState;
    }

    public Map<State, MyPoint> createStateToMyPoint(String[] names, Map<String, State> nameToState) {
        Map<State, MyPoint> stateToMyPoint = new HashMap<>();
        for (String name : names) {
            String[] param = name.split("\n");
            String[] coord = param[1].split(",");
            int x = Integer.parseInt(coord[0]);
            int y = Integer.parseInt(coord[1]);
            stateToMyPoint.put(nameToState.get(param[0]), new MyPoint(x, y));
        }
        return stateToMyPoint;
    }

    public SortedSet<State> getFinalStates(String[] names, Map<String, State> nameToState) {
        SortedSet<State> finalStates = new TreeSet<>();
        for (String name : names) {
            finalStates.add(nameToState.get(name));
        }
        return finalStates;
    }

    public Pair<FiniteStateAutomaton, Map<State, MyPoint>> toFSA() {
        if (machineType == null || !machineType.equals(MachineType.FSA)) {
            throw new IllegalMachineTypeException(machineType, MachineType.FSA);
        }
        String[][] parametersMachine = parametersMachine();
        Map<String, State> nameToState = createNameToState(parametersMachine[1]);
        Map<State, MyPoint> stateToPoint = createStateToMyPoint(parametersMachine[1], nameToState);
        SortedSet<State> states = new TreeSet<>(nameToState.values());
        SortedSet<State> finalStates = getFinalStates(parametersMachine[0], nameToState);
        State startState = nameToState.get(parametersMachine[2][0]);
        SortedSet<FSATransitionFunction> transitionFunctions =
                getFSATransitionFunctions(parametersMachine[3], nameToState);
        FiniteStateAutomaton fsa = new FiniteStateAutomaton(states, startState,
                finalStates, transitionFunctions);
        this.defineMachine(fsa);
        return Pair.create(fsa, stateToPoint);
    }

    public GraphAdapter toGraphAdapter() {
        GraphAdapter graphAdapter = new GraphAdapter();
        graphAdapter.dotLanguage = this;
        String[][] parametersMachine = parametersMachine();
        Map<String, State> nameToState = createNameToState(parametersMachine[1]);
        graphAdapter.stateMyPointMap = createStateToMyPoint(parametersMachine[1], nameToState);
        graphAdapter.stateSet = new TreeSet<>(nameToState.values());
        graphAdapter.stateFinals = getFinalStates(parametersMachine[0], nameToState);
        graphAdapter.startState = nameToState.get(parametersMachine[2][0]);
        graphAdapter.edgeList = new ArrayList<>();
        for (String edgeStr : parametersMachine[3]) {
            String[] params = edgeStr.split("\n");
            Edge edge = new Edge();
            edge.current = nameToState.get(params[0]);
            edge.future = nameToState.get(params[1]);
            edge.label = params[2].replaceAll("\\\\n", "\n");
            graphAdapter.edgeList.add(edge);
        }
        return graphAdapter;
    }

    public Pair<PushdownAutomaton, Map<State, MyPoint>> toPDA() {
        if (machineType == null || !machineType.equals(MachineType.PDA)) {
            throw new IllegalMachineTypeException(machineType, MachineType.PDA);
        }
        String[][] parametersMachine = parametersMachine();
        Map<String, State> nameToState = createNameToState(parametersMachine[1]);
        Map<State, MyPoint> stateToPoint = createStateToMyPoint(parametersMachine[1], nameToState);
        SortedSet<State> states = new TreeSet<>(nameToState.values());
        SortedSet<State> finalStates = getFinalStates(parametersMachine[0], nameToState);
        State startState = nameToState.get(parametersMachine[2][0]);
        SortedSet<PDATransitionFunction> transitionFunctions =
                getPDATransitionFunctions(parametersMachine[3], nameToState);
        PushdownAutomaton pda = new PushdownAutomaton(states, startState, finalStates, transitionFunctions);
        defineMachine(pda);
        return Pair.create(pda, stateToPoint);
    }

    public Pair<PushdownAutomatonExtend, Map<State, MyPoint>> toPDAExt() {
        if (machineType == null || !machineType.equals(MachineType.PDA_EXT)) {
            throw new IllegalMachineTypeException(machineType, MachineType.PDA_EXT);
        }
        String[][] parametersMachine = parametersMachine();
        Map<String, State> nameToState = createNameToState(parametersMachine[1]);
        Map<State, MyPoint> stateToPoint = createStateToMyPoint(parametersMachine[1], nameToState);
        SortedSet<State> states = new TreeSet<>(nameToState.values());
        SortedSet<State> finalStates = getFinalStates(parametersMachine[0], nameToState);
        State startState = nameToState.get(parametersMachine[2][0]);
        SortedSet<PDAExtTransitionFunction> transitionFunctions =
                getPDAExtTransitionFunctions(parametersMachine[3], nameToState);
        PushdownAutomatonExtend pda = new PushdownAutomatonExtend(states, startState, finalStates, transitionFunctions);
        defineMachine(pda);
        return Pair.create(pda, stateToPoint);
    }

    public Pair<TuringMachine, Map<State, MyPoint>> toTM() {
        if (machineType == null || !machineType.equals(MachineType.TM)) {
            throw new IllegalMachineTypeException(machineType, MachineType.TM);
        }
        String[][] parametersMachine = parametersMachine();
        Map<String, State> nameToState = createNameToState(parametersMachine[1]);
        Map<State, MyPoint> stateToPoint = createStateToMyPoint(parametersMachine[1], nameToState);
        SortedSet<State> states = new TreeSet<>(nameToState.values());
        SortedSet<State> finalStates = getFinalStates(parametersMachine[0], nameToState);
        State startState = nameToState.get(parametersMachine[2][0]);
        SortedSet<TMTransitionFunction> transitionFunctions =
                getTMTransitionFunctions(parametersMachine[3], nameToState);
        TuringMachine tm = new TuringMachine(states, startState, finalStates, transitionFunctions);
        defineMachine(tm);
        return Pair.create(tm, stateToPoint);
    }

    public Pair<TuringMachineMultiTape, Map<State, MyPoint>> toTMMultiTape() {
        if (machineType == null || !machineType.equals(MachineType.TM_MULTI_TAPE)) {
            throw new IllegalMachineTypeException(machineType, MachineType.TM_MULTI_TAPE);
        }
        String[][] parametersMachine = parametersMachine();
        Map<String, State> nameToState = createNameToState(parametersMachine[1]);
        Map<State, MyPoint> stateToPoint = createStateToMyPoint(parametersMachine[1], nameToState);
        SortedSet<State> states = new TreeSet<>(nameToState.values());
        SortedSet<State> finalStates = getFinalStates(parametersMachine[0], nameToState);
        State startState = nameToState.get(parametersMachine[2][0]);
        SortedSet<TMMultiTapeTransitionFunction> transitionFunctions =
                getTMMultiTapeTransitionFunctions(parametersMachine[3], nameToState);
        int numTapes = 2;
        for (TMMultiTapeTransitionFunction tf : transitionFunctions) {
            numTapes = tf.getNumTapes();
            break;
        }
        TuringMachineMultiTape tm = new TuringMachineMultiTape(states, startState, finalStates,
                transitionFunctions, numTapes);
        defineMachine(tm);
        return Pair.create(tm, stateToPoint);
    }

    public void defineMachine(Machine m) {
        m.setContUses(this.contUses);
        m.setId(this.id);
        m.setCreationDate(this.creationDate);
        m.setLabel(this.label);
    }

    public Pair<TuringMachineMultiTrack, Map<State, MyPoint>> toTMMultiTrack() {
        if (machineType == null || !machineType.equals(MachineType.TM_MULTI_TRACK)) {
            throw new IllegalMachineTypeException(machineType, MachineType.TM_MULTI_TRACK);
        }
        String[][] parametersMachine = parametersMachine();
        Map<String, State> nameToState = createNameToState(parametersMachine[1]);
        Map<State, MyPoint> stateToPoint = createStateToMyPoint(parametersMachine[1], nameToState);
        SortedSet<State> states = new TreeSet<>(nameToState.values());
        SortedSet<State> finalStates = getFinalStates(parametersMachine[0], nameToState);
        State startState = nameToState.get(parametersMachine[2][0]);
        SortedSet<TMMultiTrackTransitionFunction> transitionFunctions =
                getTMMultiTrackTransitionFunctions(parametersMachine[3], nameToState);
        int numTracks = 2;
        for (TMMultiTrackTransitionFunction tf : transitionFunctions) {
            numTracks = tf.getNumTapes();
            break;
        }
        TuringMachineMultiTrack tm = new TuringMachineMultiTrack(states, startState, finalStates,
                transitionFunctions, numTracks);
        defineMachine(tm);
        return Pair.create(tm, stateToPoint);
    }

//    private List<String> clean(List<String> str) {
//        List<String> list = new ArrayList<>();
//        for (String st : str) {
//            if (!st.isEmpty()) {
//                list.add(st);
//            }
//        }
//        return list;
//    }
//
//    //private Map<State, MyPoint> stateMyPointMap = new HashMap<>();
//
//    private int defineStates(Machine machine) {
//        machine.setId(id);
//        machine.setCreationDate(creationDate);
//        machine.setContUses(contUses);
//        machine.setLabel(label);
//        SortedSet<State> states = new TreeSet<>();
//        SortedSet<State> finalStates = new TreeSet<>();
//        final String p1 = "style=\"filled\"];\n\t";
//        int index = graph.indexOf(p1) + p1.length();
//        int indexAux = index + 1;
//        while (graph.charAt(indexAux) != ';') {
//            indexAux++;
//        }
//        Set<String> finalStatesStr = new HashSet<>(
//                clean(Arrays.asList(
//                        graph.substring(index, indexAux).split(", "))));
//        indexAux++;
//        while (graph.charAt(indexAux) != '\n') {
//            indexAux++;
//        }
//        while (graph.charAt(indexAux) == '\n' || graph.charAt(indexAux) == '\t') {
//            indexAux++;
//        }
//        String p2 = "pos=\"";
//        do {
//            index = indexAux;
//            while (graph.charAt(indexAux) != '[') {
//                indexAux++;
//            }
//            String stateStr = graph.substring(index, indexAux-1);
//            State state = new State(stateStr);
//            states.add(state);
//            if (finalStatesStr.contains(stateStr)) {
//                finalStates.add(state);
//            }
//            index = graph.indexOf(p2, indexAux) + p2.length();
//            indexAux = graph.indexOf(',', index);
//            MyPoint myPoint = new MyPoint();
//            myPoint.x = Integer.parseInt(graph.substring(index, indexAux));
//            index = indexAux + 1;
//            indexAux = graph.indexOf('!', index);
//            myPoint.y = Integer.parseInt(graph.substring(index, indexAux));
//            stateMyPointMap.put(state, myPoint);
//            indexAux += 6;
//        } while (graph.charAt(indexAux) != '\n');
//        String p3 = "start -> ";
//        while (graph.charAt(indexAux) == '\n' || graph.charAt(indexAux) == '\t') {
//            indexAux++;
//        }
//        indexAux += p3.length();
//        index = indexAux;
//        indexAux = graph.indexOf(';', index);
//        String initialState = graph.substring(index, indexAux);
//        machine.setStates(states);
//        machine.setFinalStates(finalStates);
//        machine.setInitialState(machine.getState(initialState));
//        while (graph.charAt(indexAux) == '\n' || graph.charAt(indexAux) == '\t') {
//            indexAux++;
//        }
//        return indexAux;
//    }
//
//    private String[] transitionArgs(int index) {
//        String[] args = new String[4];
//        if (graph.charAt(index) == '\n') {
//            return null;
//        }
//        String p1 = " -> ";
//        String p2 = " [label=\"";
//        int indexAux = graph.indexOf(p1, index);
//        args[0] = graph.substring(index, indexAux);
//        index = indexAux + p1.length();
//        indexAux = graph.indexOf(p2, index);
//        args[1] = graph.substring(index, indexAux);
//        index = indexAux + p2.length();
//        indexAux = graph.indexOf("\"];", index);
//        args[2] = graph.substring(index, indexAux);
//        index = indexAux + 5;
//        args[3] = Integer.toString(index);
//        return args;
//    }
//
//    public Map<State, MyPoint> getStateMyPointMap() {
//        return stateMyPointMap;
//    }
//
//    private PushdownAutomaton toPDA() {
//        PushdownAutomaton pda = new PushdownAutomaton(new TreeSet<State>(), null,
//                new TreeSet<State>(), new TreeSet<PDATransitionFunction>());
//        int index = defineStates(pda);
//        String[] args;
//        Set<PDATransitionFunction> transitionFunctions = new TreeSet<>();
//        while ((args = transitionArgs(index)) != null) {
//            transitionFunctions.addAll(pda.createTransitions(args[0], args[1], args[2]));
//            index = Integer.parseInt(args[3]);
//        }
//        pda.setTransictionFunction(transitionFunctions);
//        return pda;
//    }
//
//    private TuringMachine toTuringMachine() {
//        TuringMachine tm = new TuringMachine(new TreeSet<State>(), null,
//                new TreeSet<State>(), new TreeSet<TMTransitionFunction>());
//        int index = defineStates(tm);
//        String[] args;
//        Set<TMTransitionFunction> transitionFunctions = new TreeSet<>();
//        while ((args = transitionArgs(index)) != null) {
//            transitionFunctions.addAll(tm.createTransitions(args[0], args[1], args[2]));
//            index = Integer.parseInt(args[3]);
//        }
//        tm.setTransitionFunction(transitionFunctions);
//        return tm;
//    }

//    public int getNumTapes() {
//        return numTapes;
//    }
//
//    public void setNumTapes(int numTapes) {
//        this.numTapes = numTapes;
//    }

    //private int numTapes;
//
//    private TuringMachineMultiTape toTuringMachineMultiTape() {
//        TuringMachineMultiTape tm = new TuringMachineMultiTape(new TreeSet<State>(), null,
//                new TreeSet<State>(), new TreeSet<TMMultiTapeTransitionFunction>(), numTapes);
//        int index = defineStates(tm);
//        String[] args;
//        Set<TMMultiTapeTransitionFunction> transitionFunctions = new TreeSet<>();
//        while ((args = transitionArgs(index)) != null) {
//            transitionFunctions.addAll(tm.createTransitions(args[0], args[1], args[2]));
//            index = Integer.parseInt(args[3]);
//        }
//        tm.setTransitionFunction(transitionFunctions);
//        return tm;
//    }
//
//    private TuringMachineMultiTrack toTuringMachineMultiTrack() {
//        TuringMachineMultiTrack tm = new TuringMachineMultiTrack(new TreeSet<State>(), null,
//                new TreeSet<State>(), new TreeSet<TMMultiTrackTransitionFunction>(), numTapes);
//        int index = defineStates(tm);
//        String[] args;
//        Set<TMMultiTrackTransitionFunction> transitionFunctions = new TreeSet<>();
//        while ((args = transitionArgs(index)) != null) {
//            transitionFunctions.addAll(tm.createTransitions(args[0], args[1], args[2]));
//            index = Integer.parseInt(args[3]);
//        }
//        tm.setTransitionFunction(transitionFunctions);
//        return tm;
//    }
//
//
//    private FiniteStateAutomatonGUI toAutomatonGUINew() {
//        FiniteStateAutomaton fsa = new FiniteStateAutomaton(new TreeSet<State>(), null,
//                new TreeSet<State>(), new TreeSet<FSATransitionFunction>());
//        int index = defineStates(fsa);
//        String[] args;
//        Set<FSATransitionFunction> transitionFunctions = new TreeSet<>();
//        while ((args = transitionArgs(index)) != null) {
//            State a = fsa.getState(args[0]);
//            State b = fsa.getState(args[1]);
//            String[] symbols = args[2].split("\\n");
//            for (int i = 0; i < symbols.length; i++) {
//                transitionFunctions.add(new FSATransitionFunction(a, symbols[i], b));
//            }
//            index = Integer.parseInt(args[3]);
//        }
//        return new FiniteStateAutomatonGUI(fsa, getStatePoint());
//    }

    public  FiniteStateAutomatonGUI newToAutomatonGUI() {
        Pair<FiniteStateAutomaton, Map<State, MyPoint>> pairFSA = toFSA();
        return new FiniteStateAutomatonGUI(pairFSA.first, pairFSA.second, id, label, contUses,
                creationDate);
    }

    public FiniteStateAutomatonGUI toAutomatonGUI() {
        FiniteStateFiniteStateAutomatonGUIBuilder finiteStateAutomatonGUIBuilder =
                new FiniteStateFiniteStateAutomatonGUIBuilder();
        String lines[] = graph.split(END_LINE);
        int length = lines.length - 1;
        int ind = 0;
        if (label == null) {
            label = UNTITLED;
        }
        finiteStateAutomatonGUIBuilder.setLabel(label);
        String aux[] = lines[ind].split(" ");
        if (aux.length >= 2) {
            //this.label = aux[1];
            //finiteStateAutomatonGUIBuilder.setLabel(label);
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
            finiteStateAutomatonGUIBuilder.addOrChangeStatePosition(state, statePosition);
            if (auxInd < auxLenght) {
                initialInd = aux[auxInd].indexOf(EQUALS) + 1;
                finalInd = aux[auxInd].length() - 2;
                String style = aux[auxInd].substring(initialInd, finalInd);
                if (STYLE_FINAL.equals(style)) {
                    finiteStateAutomatonGUIBuilder.defineFinalState(state);
                } else if (STYLE_INITIAL.equals(style)) {
                    finiteStateAutomatonGUIBuilder.withInitialState(state);
                } else if (STYLE_INITIAL_AND_FINAL.equals(style)) {
                    finiteStateAutomatonGUIBuilder.withInitialState(state);
                    finiteStateAutomatonGUIBuilder.defineFinalState(state);
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
            State currentStateSt = finiteStateAutomatonGUIBuilder.getState(currentState);
            State futureStateSt = finiteStateAutomatonGUIBuilder.getState(futureState);
            if (currentStateSt == null || symbol == null || futureStateSt == null) {
                ind++;
                continue;
            }
            finiteStateAutomatonGUIBuilder.addTransition(currentStateSt, symbol, futureStateSt);
            ind++;
        }
        finiteStateAutomatonGUIBuilder.setLabel(label)
                .setId(id)
                .setContUses(contUses)
                .setCreationDate(creationDate);
        return finiteStateAutomatonGUIBuilder.createAutomatonGUI();
    }
//
//    public SortedMap<State, Point> getStatePoint() {
//        SortedMap<State, Point> statePointMap = new TreeMap<>();
//        for (Map.Entry<State, MyPoint> e : stateMyPointMap.entrySet()) {
//            statePointMap.put(e.getKey(), e.getValue().toPoint());
//        }
//        return statePointMap;
//    }

    public String getGraph() {
        return graph;
    }

    public String getLabel() {
        return label;
    }

    public Integer getContUses() {
        return contUses;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Long getId() {
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

    public Set<String>[] getSetParameterMachine() {
        String[][] parametersMachine = parametersMachine();
        int n = parametersMachine.length;
        Set<String>[] setParametersMachine = new HashSet[n];
        for (int i = 0; i < n; i++) {
            setParametersMachine[i] = new HashSet<>(
                    Arrays.asList(parametersMachine[i]));
        }
        return setParametersMachine;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DotLanguage that = (DotLanguage) o;

        Set<String>[] setParameterMachine = getSetParameterMachine();
        Set<String>[] thatSetParameterMachine = that.getSetParameterMachine();
        if (setParameterMachine != null
                ? !Arrays.deepEquals(setParameterMachine, thatSetParameterMachine)
                : thatSetParameterMachine != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (machineType != that.machineType) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (contUses != null ? !contUses.equals(that.contUses) : that.contUses != null)
            return false;
        return creationDate != null ? creationDate.equals(that.creationDate) : that.creationDate == null;

    }

    @Override
    public int hashCode() {
        int result = graph != null ? graph.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (machineType != null ? machineType.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (contUses != null ? contUses.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }
}
