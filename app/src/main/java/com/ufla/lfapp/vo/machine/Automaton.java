package com.ufla.lfapp.vo.machine;


import android.graphics.Point;
import android.support.v4.util.Pair;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by carlos on 11/23/16.
 */

public class Automaton extends Machine implements Serializable {

    public static final String STATE_ERROR = "err";
    protected SortedSet<TransitionFunction> transitionFunctions;
    protected State stateError;
    public static String LAMBDA = "λ";
    public static String FECHO_LAMBDA = "*";
    protected static final int MAX_PROCESS = 100000;
    protected SortedSet<State> initialStatesFromAFNDLambdaToAFD;

    public SortedSet<State> getInitialStatesFromAFNDLambdaToAFD() {
        if (initialStatesFromAFNDLambdaToAFD == null ||
                initialStatesFromAFNDLambdaToAFD.isEmpty()) {
            initialStatesFromAFNDLambdaToAFD = new TreeSet<>();
            initialStatesFromAFNDLambdaToAFD.add(initialState);
        }
        return initialStatesFromAFNDLambdaToAFD;
    }
    public void setInitialStatesFromAFNDLambdaToAFD(SortedSet<State> initialStates) {
        initialStatesFromAFNDLambdaToAFD = initialStates;
    }

    public boolean isComplete() {
        SortedSet<String> alphabet = getAlphabet();
        for (State state : states) {
            for (String symbol : alphabet) {
                if (getFutureStateOfTransition(state, symbol) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public SortedSet<String> getAlphabet() {
        SortedSet<String> alphabet = new TreeSet<>();
        for (TransitionFunction transitionFunction : transitionFunctions) {
            alphabet.add(transitionFunction.getSymbol());
        }
        return alphabet;
    }

    public Automaton(SortedSet<State> states,
                     State initialState, SortedSet<State> finalStates,
                     SortedSet<TransitionFunction> transitionFunctions) {
        super(states, initialState, finalStates);
        if (transitionFunctions == null) {
            return;
        }
        this.transitionFunctions = new TreeSet<>();
        for (TransitionFunction transitionFunction : transitionFunctions) {
            State currentState = getState(transitionFunction.getCurrentState().getName());
            State futureState = getState(transitionFunction.getFutureState().getName());
            this.transitionFunctions.add(new TransitionFunction(currentState,
                    transitionFunction.getSymbol(), futureState));
        }
    }

    public static SortedSet<TransitionFunction> copyTransictionFunctions(
            Set<TransitionFunction> transitionFunctions) {
        SortedSet<TransitionFunction> copyTransictionFunctions = new TreeSet<>();
        if (transitionFunctions == null) {
            return copyTransictionFunctions;
        }
        for (TransitionFunction transitionFunction : transitionFunctions) {
            copyTransictionFunctions.add(transitionFunction.copy());
        }
        return copyTransictionFunctions;
    }

    public Automaton(Automaton automaton) {
        super(automaton);
        if (automaton == null || automaton.transitionFunctions == null) {
            return;
        }
        transitionFunctions = new TreeSet<>();
        for (TransitionFunction transitionFunction : automaton.transitionFunctions) {
            State currentState = getState(transitionFunction.getCurrentState().getName());
            State futureState = getState(transitionFunction.getFutureState().getName());
            transitionFunctions.add(new TransitionFunction(currentState,
                    transitionFunction.getSymbol(), futureState));
        }
    }

    protected Automaton() {
        states = new TreeSet<>();
        finalStates = new TreeSet<>();
        transitionFunctions = new TreeSet<>();
        initialStatesFromAFNDLambdaToAFD = new TreeSet<>();
    }

    public Set<State> statesNamesWithout(State state) {
        Set<State> statesWithout = new HashSet<>(states);
        statesWithout.remove(state);
        return statesWithout;
    }

    public Map<State, State> getStatesMapSimplify() {
        int contStates = 0;
        Map<State, State> statesMap = new HashMap<>();
        statesMap.put(initialState, new State("q" + contStates));
        contStates++;
        for (State state : statesNamesWithout(initialState)) {
            statesMap.put(state, new State("q" + contStates));
            contStates++;
        }
        return statesMap;
    }


    private class DFAMinimizationTableRow {

        Pair<State, State> statePair;
        boolean statePairDistinct;
        Set<Pair<State, State>> distinctPairPropagation;
        String reason;

        public boolean isDistinct() {
            return statePairDistinct;
        }

        public DFAMinimizationTableRow(Pair<State, State> statePair) {
            this.statePair = statePair;
            statePairDistinct = false;
            distinctPairPropagation = new HashSet<>();
            reason = "";
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append('[')
                    .append(statePair.first)
                    .append(',')
                    .append(statePair.second)
                    .append("]\t")
                    .append(statePairDistinct)
                    .append("\t{");
            for (Pair<State, State> pair :  distinctPairPropagation) {
                sb.append('[')
                        .append(pair.first)
                        .append(',')
                        .append(pair.second)
                        .append("], ");
            }
            if (!distinctPairPropagation.isEmpty()) {
                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("}\t")
                    .append(reason);
            return sb.toString();
        }

    }

    public String collectionToString(Collection c) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Object obj : c) {
            sb.append(obj.toString())
                    .append(", ");
        }
        if (!c.isEmpty()) {
            sb.deleteCharAt(sb.length()-1);
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("}");
        return sb.toString();
    }

    public Automaton getMinimizeAutomaton() {
        int n = states.size() - 1;
        int minimizationTableSize = (n * (n + 1)) / 2;
//        for (int i = 1; i < states.size(); i++) {
//            minimizationTableSize += i;
//        }
        DFAMinimizationTableRow dfaMinimizationTableRows[] =
                new DFAMinimizationTableRow[minimizationTableSize];
        Map<Pair<State, State>, DFAMinimizationTableRow> statePairToMinimizationTable =
                new HashMap<>();
        State statesArray[] = states.toArray(new State[states.size()]);
        int max = statesArray.length - 1;
        int contRows = 0;
        for (int i = 0; i < max; i++) {
            for (int j = i + 1; j < statesArray.length; j++) {
                Pair<State, State> statePair = Pair.create(statesArray[i], statesArray[j]);
                dfaMinimizationTableRows[contRows] = new DFAMinimizationTableRow(statePair);
                statePairToMinimizationTable.put(statePair, dfaMinimizationTableRows[contRows]);
                if ((isFinalState(dfaMinimizationTableRows[contRows].statePair.first) &&
                        !isFinalState(dfaMinimizationTableRows[contRows].statePair.second)) ||
                        (isFinalState(dfaMinimizationTableRows[contRows].statePair.second) &&
                                !isFinalState(dfaMinimizationTableRows[contRows].statePair.first))) {
                    dfaMinimizationTableRows[contRows].statePairDistinct = true;
                }
                contRows++;
            }
        }
        for (DFAMinimizationTableRow dfaMinimizationTableRow : dfaMinimizationTableRows) {
            if ((isFinalState(dfaMinimizationTableRow.statePair.first) &&
                    !isFinalState(dfaMinimizationTableRow.statePair.second)) ||
                    (isFinalState(dfaMinimizationTableRow.statePair.second) &&
                    !isFinalState(dfaMinimizationTableRow.statePair.first))) {
                dfaMinimizationTableRow.statePairDistinct = true;
            }
        }
        boolean propagation;
        SortedSet<String> alphabet = getAlphabet();
        for (DFAMinimizationTableRow dfaMinimizationTableRow : dfaMinimizationTableRows) {
            propagation = false;
            if (dfaMinimizationTableRow.isDistinct()) {
                continue;
            }
            for (String symbol : alphabet) {
                Pair<State, State> futureStatePair = Pair.create(
                        getFutureStateOfTransition(dfaMinimizationTableRow.statePair.first,
                                symbol),
                        getFutureStateOfTransition(dfaMinimizationTableRow.statePair.second,
                                symbol)
                );
                if (futureStatePair.first != null && futureStatePair.second != null) {
                    if (futureStatePair.first.compareTo(futureStatePair.second) > 0) {
                        futureStatePair = Pair.create(futureStatePair.second,
                                futureStatePair.first);
                    }
                    if (!(futureStatePair.first.equals(futureStatePair.second) ||
                            dfaMinimizationTableRow.statePair.equals(futureStatePair))) {
                        DFAMinimizationTableRow dfaMinimizationTableRowFuture =
                                statePairToMinimizationTable.get(futureStatePair);
                        if (dfaMinimizationTableRowFuture.isDistinct()) {
                            dfaMinimizationTableRow.statePairDistinct = true;
                            dfaMinimizationTableRow.reason = symbol;
                            propagation = true;
                            break;
                        } else {
                            dfaMinimizationTableRowFuture.distinctPairPropagation.add(
                                    dfaMinimizationTableRow.statePair);
                        }
                    }
                } else if (futureStatePair.first != null || futureStatePair.second != null) {
                    dfaMinimizationTableRow.statePairDistinct = true;
                    dfaMinimizationTableRow.reason = symbol;
                    propagation = true;
                    break;
                }
            }
            if (propagation) {
                Deque<DFAMinimizationTableRow> dfaMinimizationTableRowsPropagation =
                        new ArrayDeque<>();
                dfaMinimizationTableRowsPropagation.push(dfaMinimizationTableRow);
                while (!dfaMinimizationTableRowsPropagation.isEmpty()) {
                    DFAMinimizationTableRow dfaMinimizationTableRowPropActual =
                            dfaMinimizationTableRowsPropagation.pop();
                    for (Pair<State, State> statePairPropagation :
                            dfaMinimizationTableRowPropActual.distinctPairPropagation) {
                        DFAMinimizationTableRow dfaMinimizationTableRowPropagation =
                                statePairToMinimizationTable.get(statePairPropagation);
                        if (!dfaMinimizationTableRowPropagation.isDistinct()) {
                            dfaMinimizationTableRowPropagation.statePairDistinct = true;
                            dfaMinimizationTableRowPropagation.reason = "[" +
                                    dfaMinimizationTableRowPropActual.statePair.first + ", " +
                                    dfaMinimizationTableRowPropActual.statePair.second + "]";
                            dfaMinimizationTableRowsPropagation.push(
                                    dfaMinimizationTableRowPropagation);
                        }
                    }
                }
            }
        }
        System.out.println("BEGIN_TABLE");
        for (DFAMinimizationTableRow dfaMinimizationTableRow : dfaMinimizationTableRows) {
            System.out.println(dfaMinimizationTableRow);
        }
        System.out.println("END_TABLE");
        SortedSet<Pair<State, State>> statePairEquals = new TreeSet<>(
                new Comparator<Pair<State, State>>() {
            @Override
            public int compare(Pair<State, State> lhs, Pair<State, State> rhs) {
                return (lhs.first.equals(rhs.first)) ? lhs.second.compareTo(rhs.second) :
                        lhs.first.compareTo(rhs.second);
            }
        });
        for (DFAMinimizationTableRow dfaMinimizationTableRow : dfaMinimizationTableRows) {
            if (!dfaMinimizationTableRow.isDistinct()) {
                statePairEquals.add(dfaMinimizationTableRow.statePair);
            }
        }
        Set<Set<State>> setOfStateEquals = new HashSet<>();
        while (!statePairEquals.isEmpty()) {
            Set<State> stateEquals = new HashSet<>();
            Iterator<Pair<State, State>> iterator = statePairEquals.iterator();
            Pair<State, State> statePair = iterator.next();
            stateEquals.add(statePair.first);
            stateEquals.add(statePair.second);
            iterator.remove();
            while(iterator.hasNext()) {
                statePair = iterator.next();
                if (stateEquals.contains(statePair.first) || stateEquals.contains(statePair.second)) {
                    stateEquals.add(statePair.first);
                    stateEquals.add(statePair.second);
                    iterator.remove();
                }
            }
            setOfStateEquals.add(stateEquals);
        }
        for (Set<State> setStates : setOfStateEquals) {
            Log.d("setStates", collectionToString(setStates));
        }
        Automaton automaton = new Automaton(this);
        int contNewStates = 0;
        for (Set<State> stateEquals : setOfStateEquals) {
            while (automaton.states.contains(new State("qc" + contNewStates))) {
                contNewStates++;
            }
            State newState = new State("qc" + contNewStates);
            automaton.states.add(newState);
            contNewStates++;
            for (State state : stateEquals) {
                automaton.states.remove(state);
                if (automaton.finalStates.contains(state)) {
                    automaton.finalStates.remove(state);
                    automaton.finalStates.add(newState);
                }
                if (automaton.initialState != null &&
                        automaton.initialState.equals(state)) {
                    automaton.initialState = newState;
                }
                Log.d("state", state.getName());
                for (TransitionFunction transitionFunction : automaton.getTransitionsFrom(state)) {
                    Log.d("TRANSITIONFUNCT", "TESTE1");
                    automaton.transitionFunctions.add(new TransitionFunction(newState,
                            transitionFunction.getSymbol(), transitionFunction.getFutureState()));
                    automaton.transitionFunctions.remove(transitionFunction);
                }
                for (TransitionFunction transitionFunction : automaton.getTransitionsTo(state)) {
                    Log.d("TRANSITIONFUNCT", "TESTE2");
                    automaton.transitionFunctions.add(new TransitionFunction(
                            transitionFunction.getCurrentState(),
                            transitionFunction.getSymbol(), newState));
                    automaton.transitionFunctions.remove(transitionFunction);
                }
            }
        }
        return automaton;
    }

    public Set<TransitionFunction> getTransitionFunctions() {
        return transitionFunctions;
    }

    public Automaton getAutomatonWithStatesNameSimplify() {
        Automaton automaton = new Automaton(this);
//        Map<State, State> statesMap = getStatesMapSimplify();
        int contStates = 1;
        automaton.renameState(automaton.initialState.getName(), "q0");
        for (State state : automaton.statesNamesWithout(automaton.initialState)) {
            automaton.renameState(state.getName(), "q" + contStates);
            contStates++;
        }
//        automaton.initialState = new State("q0");
//        automaton.states = new TreeSet<>();
//        for (State state : statesMap.values()) {
//            states.add(state);
//        }
//        automaton.finalStates = new TreeSet<>();
//        for (State state : finalStates) {
//            automaton.finalStates.add(statesMap.get(state));
//        }
//        automaton.transitionFunctions = new TreeSet<>();
//        for (TransitionFunction t : transitionFunctions) {
//            automaton.transitionFunctions.add(new TransitionFunction(
//                    statesMap.get(t.getCurrentState()),
//                    t.getSymbol(), statesMap.get(t.getFutureState())));
//        }
        return automaton;
    }

    public SortedMap<State, Point> getStatesPointsFake() {
        SortedMap<State, Point> statesPoints = new TreeMap<>();
        int qtdMaxPoint = (int) Math.ceil(Math.sqrt(states.size()));
        int cont=0, x=2, y=2;
        for (State state : states) {
            statesPoints.put(state, new Point(x, y));
            x += 3;
            cont++;
            if (cont == qtdMaxPoint) {
                x = 2;
                y += 3;
            }
        }
        return statesPoints;
    }

    public boolean isAFND() {
        return !isAFD();
    }

    public boolean isAFNDLambda() {
        for (TransitionFunction t : transitionFunctions) {
            if(t.symbol.equals(LAMBDA)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAFD() {
        Set<Pair<State, String>> setOfStatesAndSymbols = new HashSet<>();
        for (TransitionFunction t : transitionFunctions) {
            Pair<State, String> stateAndSymbol = Pair.create(t.currentState, t.symbol);
            if (setOfStatesAndSymbols.contains(stateAndSymbol) || t.symbol.equals(LAMBDA)) {
                return false;
            }
            setOfStatesAndSymbols.add(stateAndSymbol);
        }
        return true;
    }

    public void insertFechoLambda(SortedMap<Pair<State, String>, SortedSet<State>> transitionTable) {
        for (State state : states) {
            SortedSet<State> fechoLambda = new TreeSet<>();
            Set<State> statesVisited = new HashSet<>();
            Deque<State> statesToVisited = new ArrayDeque<>();
            statesToVisited.push(state);
            statesVisited.add(state);
            fechoLambda.add(state);
            while (!statesToVisited.isEmpty()) {
                for (State stateForVisite : transitionTable.get(
                        Pair.create(statesToVisited.pop(), LAMBDA))) {
                    if (!statesVisited.contains(stateForVisite)) {
                        statesToVisited.push(stateForVisite);
                        statesVisited.add(stateForVisite);
                        fechoLambda.add(stateForVisite);
                    }
                }
            }
            transitionTable.put(Pair.create(state, FECHO_LAMBDA), fechoLambda);
        }
    }

    private Automaton copy() {
        Automaton automaton = new Automaton();
        automaton.states = new TreeSet<>(states);
        automaton.finalStates = new TreeSet<>(finalStates);
        automaton.initialState = initialState;
        automaton.transitionFunctions = new TreeSet<>();
        for (TransitionFunction t : transitionFunctions) {
            automaton.transitionFunctions.add(t.copy());
        }
        return automaton;
    }

    /**
     * Encontra o conjunto de estados alcançáveis partindo de um determinado estado e consumindo
     * um determinado símbolo.
     * @param fromState estado de partida para os estados alcançáveis
     * @param symbol símbolo de leitura para os estados alcançáveis
     * @return retorna conjunto de estados alcançáveis
     */
    public SortedSet<State> getStatesReachWithSymbol(State fromState, String symbol) {
        SortedSet<State> states = new TreeSet<>();
        
        for (TransitionFunction transitionFunction : transitionFunctions) {
            if (transitionFunction.isCurrentState(fromState) &&
                    transitionFunction.isSymbol(symbol)) {
                states.add(transitionFunction.futureState);
            }
        }

        return states;
    }

    /**
     * Encontra o fecho-lambda de um estado. O fecho-lambda de um estado é um conjunto de estados
     * em que se pode alcançar consumindo apenas lambda. O fecho-lambda de um estado no mínimo
     * possui o próprio estado.
     *
     * @param state estado a ser encontrado seu fecho-lambda
     * @return fecho-lambda do estado
     */
    public SortedSet<State> getLambdaClosure(State state) {
        SortedSet<State> states = new TreeSet<>();
        SortedSet<State> statesNotVerify = new TreeSet<>();
        SortedSet<State> newStates = new TreeSet<>();

        statesNotVerify.add(state);
        while (!statesNotVerify.isEmpty()) {
            for (State stateForVerify : statesNotVerify) {
                newStates.addAll(getStatesReachWithSymbol(state, LAMBDA));
                states.add(stateForVerify);
            }
            newStates.removeAll(states);
            statesNotVerify.clear();
            statesNotVerify.addAll(newStates);
            states.addAll(newStates);
            newStates.clear();
        }

        return states;
    }

    public Automaton AFNDLambdaToAFND() {
        if (!isAFNDLambda()) {
            return copy();
        }
        Automaton automatonAFND = new Automaton();
        SortedMap<Pair<State, String>, SortedSet<State>> transitionTable = getTransitionTableAFND();
        automatonAFND.transitionFunctions = new TreeSet<>();
        insertFechoLambda(transitionTable);
        for (SortedMap.Entry<Pair<State, String>, SortedSet<State>> entry :
                transitionTable.entrySet()) {
            if (entry.getKey().second.equals(LAMBDA) || entry.getKey().second.equals(FECHO_LAMBDA)) {
                continue;
            }
            // Adicionando transições através do fecho-lambda mais uma leitura
            for (State fechoLambda : transitionTable.get(Pair.create(entry.getKey().first,
                    FECHO_LAMBDA))) {
                for (State futureState : transitionTable.get(Pair.create(fechoLambda,
                        entry.getKey().second))) {
                    automatonAFND.transitionFunctions.add(new TransitionFunction(entry.getKey().first,
                            entry.getKey().second, futureState));
                }
            }
            // Adicionando transições através de uma leitura e também do fecho-lambda após essa
            // leitura
            for (State futureState : entry.getValue()) {
                automatonAFND.transitionFunctions.add(new TransitionFunction(entry.getKey().first,
                        entry.getKey().second, futureState));
                for (State futureStateB : transitionTable.get(Pair.create(futureState,
                        FECHO_LAMBDA))) {
                    automatonAFND.transitionFunctions.add(new TransitionFunction(entry.getKey().first,
                            entry.getKey().second, futureStateB));
                }
            }
        }
        automatonAFND.initialStatesFromAFNDLambdaToAFD = transitionTable.get(
                Pair.create(initialState, FECHO_LAMBDA));
        automatonAFND.initialState = initialState;
        automatonAFND.finalStates = new TreeSet<>(finalStates);
        for (State state : states) {
            if (finalStates.contains(state)) {
                continue;
            }
            for (State finalState : finalStates) {
                if (transitionTable.get(Pair.create(state,
                        FECHO_LAMBDA)).contains(finalState)) {
                    automatonAFND.finalStates.add(state);
                }
            }
        }
        automatonAFND.states = new TreeSet<>();
        for (TransitionFunction t : automatonAFND.transitionFunctions) {
            automatonAFND.states.add(t.getCurrentState());
            automatonAFND.states.add(t.getFutureState());
        }
        return automatonAFND;
    }

    public SortedSet<String> getSymbols(State currentState, State futureState) {
        SortedSet<String> symbols = new TreeSet<>();
        for (TransitionFunction t : transitionFunctions) {
            if (t.currentState.equals(currentState) && t.futureState.equals(futureState))  {
                symbols.add(t.symbol);
            }
        }
        return symbols;
    }

    public Automaton AFNDtoAFD() {
        if (isAFD()) {
            return copy();
        }
        Automaton automatonAFD = new Automaton();
        boolean isFinalState = false;
        StringBuilder sbInitialState = new StringBuilder();
        SortedSet<State> initialStateSet = new TreeSet<>();
        if (initialStatesFromAFNDLambdaToAFD != null && !initialStatesFromAFNDLambdaToAFD.isEmpty()) {
            initialStateSet = new TreeSet<>(initialStatesFromAFNDLambdaToAFD);
            sbInitialState.append('<');
            for (State state: initialStatesFromAFNDLambdaToAFD) {
                if (finalStates.contains(state)) {
                    isFinalState = true;
                }
                sbInitialState.append(state)
                        .append(',');
            }
            sbInitialState.deleteCharAt(sbInitialState.length() - 1);
            sbInitialState.append('>');
        } else {
            initialStateSet.add(initialState);
            if (finalStates.contains(initialState)) {
                isFinalState = true;
            }
            sbInitialState.append('<')
                    .append(initialState)
                    .append('>');
        }
        automatonAFD.initialState = new State(sbInitialState.toString());
        automatonAFD.states = new TreeSet<>();
        automatonAFD.finalStates = new TreeSet<>();
        automatonAFD.states.add(automatonAFD.initialState);
        if (isFinalState) {
            automatonAFD.finalStates.add(automatonAFD.initialState);
        }
        automatonAFD.transitionFunctions = new TreeSet<>();
        SortedMap<Pair<State, String>, SortedSet<State>> transitionTable = getTransitionTableAFND();
        Deque<Pair<State, SortedSet<State>>> newStatesQueue = new ArrayDeque<>();
        newStatesQueue.offer(Pair.create(automatonAFD.initialState, initialStateSet));
        while (!newStatesQueue.isEmpty()) {
            Pair<State, SortedSet<State>> currentState = newStatesQueue.poll();
            for (State currentStatePartial : currentState.second) {
                for (String symbol : getAlphabet()) {
                    SortedSet<State> futureStateSet = transitionTable.get(
                            Pair.create(currentStatePartial, symbol));
                    if (!futureStateSet.isEmpty()) {
                        isFinalState = false;
                        StringBuilder futureStateSb = new StringBuilder();
                        futureStateSb.append('<');
                        for (State fState : futureStateSet) {
                            futureStateSb.append(fState)
                                    .append(',');
                            if (finalStates.contains(fState)) {
                                isFinalState = true;
                            }
                        }
                        futureStateSb.deleteCharAt(futureStateSb.length() - 1);
                        futureStateSb.append('>');
                        State futureState = new State(futureStateSb.toString());
                        automatonAFD.transitionFunctions.add(new TransitionFunction(currentState.first,
                                symbol, futureState));
                        if (!automatonAFD.states.contains(futureState)) {
                            automatonAFD.states.add(futureState);
                            if (isFinalState) {
                                automatonAFD.finalStates.add(futureState);
                            }
                            newStatesQueue.offer(Pair.create(futureState, futureStateSet));
                        }
                    }
                }
            }
        }
        return automatonAFD;
    }

    public Automaton AFNDLambdaToAFD() {
        return AFNDLambdaToAFND().AFNDtoAFD();
    }

    private boolean validityFromAlphabet(String word) {
        SortedSet<String> alphabet = getAlphabet();
        for (int i = 0; i < word.length(); i++) {
            if (!alphabet.contains(Character.toString(word.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    private List<String> symbolsExternFromAlphabet(String word) {
        List<String> symbolsExternFromAlphabet = new ArrayList<>();
        SortedSet<String> alphabet = getAlphabet();
        for (int i = 0; i < word.length(); i++) {
            String symbol = Character.toString(word.charAt(i));
            if (!alphabet.contains(symbol)) {
                symbolsExternFromAlphabet.add(symbol);
            }
        }
        return symbolsExternFromAlphabet;
    }

    private class Process {
        int position;
        State actualState;

        Process (int position, State actualState) {
            this.position = position;
            this.actualState = actualState;
        }

    }

    public boolean processEntry(String word) throws Exception {
        if (!validityFromAlphabet(word)) {
            List<String> symbolsExternFromAlphabet = symbolsExternFromAlphabet(word);
            StringBuilder messageException = new StringBuilder();
            messageException.append("Palavra contém símbolos que não fazem parte do alfabeto")
                .append(" do autômato. Símbolos: ")
                .append(symbolsExternFromAlphabet.get(0));
            for (int i = 1; i < symbolsExternFromAlphabet.size(); i++) {
                messageException.append(", ")
                        .append(symbolsExternFromAlphabet.get(i));
            }
            throw new Exception(messageException.toString());
        }
        Deque<Process> stackProcess = new ArrayDeque<>();
        stackProcess.push(new Process(0, initialState));
        int cont = 0;
        while (!stackProcess.isEmpty()) {
            Process actualProcess = stackProcess.pop();
            if (actualProcess.position == word.length() &&
                    finalStates.contains(actualProcess.actualState)) {
                return true;
            }
            if (actualProcess.position < word.length()) {
                Set<TransitionFunction> transitions = getTransitions(actualProcess.actualState,
                        LAMBDA);
                for (TransitionFunction t : transitions) {
                    stackProcess.push(new Process(actualProcess.position , t.getFutureState()));
                }
                transitions = getTransitions(actualProcess.actualState,
                        Character.toString(word.charAt(actualProcess.position)));
                for (TransitionFunction t : transitions) {
                    stackProcess.push(new Process(actualProcess.position + 1, t.getFutureState()));
                }
            }
            if (cont == MAX_PROCESS) {
                throw new Exception("O processamento não será terminado devido a possível " +
                        "existência de loops");
            }
            cont++;
        }
        return false;
    }

    public State getStateError() {
        return stateError;
    }

    public Set<TransitionFunction> getTransitions(State fromState, String symbol) {
        Set<TransitionFunction> transitions = new HashSet<>();
        for (TransitionFunction t : transitionFunctions) {
            if (t.currentState.equals(fromState) && t.symbol.equals(symbol)) {
                transitions.add(t);
            }
        }
        return transitions;
    }

    public Set<TransitionFunction> getTransitionFunctionsToCompleteAutomaton() {
        if (states.contains(new State(STATE_ERROR))) {
            int cont = 0;
            while (states.contains(new State(STATE_ERROR + cont))) {
                cont++;
            }
            stateError = new State(STATE_ERROR + cont);
        } else {
            stateError = new State(STATE_ERROR);
        }
        State states[] = this.states.toArray(new State[this.states.size()]);
        SortedSet<String> alphabetSet = getAlphabet();
        String alphabet[] = alphabetSet.toArray(new String[alphabetSet.size()]);
        Set<TransitionFunction> transitionFunctionsToComplAuto = new HashSet<>();
        for (int i = 0; i < states.length; i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (getTransition(states[i], alphabet[j]) == null) {
                    transitionFunctionsToComplAuto.add(new TransitionFunction(states[i], alphabet[j],
                            stateError));
                }
            }
        }
        if (!transitionFunctionsToComplAuto.isEmpty()) {
            for (int j = 0; j < alphabet.length; j++) {
                transitionFunctionsToComplAuto.add(new TransitionFunction(stateError, alphabet[j],
                        stateError));
            }
        }
        return transitionFunctionsToComplAuto;
    }

    public Automaton getCompleteAutomaton() {
        if (states.contains(new State(STATE_ERROR))) {
            int cont = 0;
            while (states.contains(new State(STATE_ERROR + cont))) {
                cont++;
            }
            stateError = new State(STATE_ERROR + cont);
        } else {
            stateError = new State(STATE_ERROR);
        }
        Automaton automaton = new Automaton();
        automaton.initialState = initialState;
        automaton.states = new TreeSet<>(states);
        automaton.finalStates = new TreeSet<>(finalStates);
        State states[] = this.states.toArray(new State[this.states.size()]);
        SortedSet<String> alphabetSet = getAlphabet();
        String alphabet[] = alphabetSet.toArray(new String[alphabetSet.size()]);
        for (int i = 0; i < states.length; i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (getTransition(states[i], alphabet[j]) == null) {
                    automaton.states.add(stateError);
                    automaton.transitionFunctions.add(new TransitionFunction(states[i], alphabet[j],
                            stateError));
                }
            }
        }
        return automaton;
    }


    public State getFutureStateOfTransition(State fromState, String symbol) {
        TransitionFunction t = getTransition(fromState, symbol);
        if (t != null) {
            return t.futureState;
        }
        return null;
    }

    public Set<TransitionFunction> getTransitionsFrom(State state) {
        Set<TransitionFunction> transitionFunctions = new HashSet<>();
        for (TransitionFunction t : this.transitionFunctions) {
            if (t.currentState.equals(state)) {
                transitionFunctions.add(t);
            }
        }
        return transitionFunctions;
    }

    public Set<TransitionFunction> getTransitionsTo(State state) {
        Set<TransitionFunction> transitionFunctions = new HashSet<>();
        for (TransitionFunction t : this.transitionFunctions) {
            if (t.futureState.equals(state)) {
                transitionFunctions.add(t);
            }
        }
        return transitionFunctions;
    }

    public TransitionFunction getTransition(State fromState, String symbol) {
        for (TransitionFunction t : transitionFunctions) {
            if (t.currentState.equals(fromState) && t.symbol.equals(symbol)) {
                return t;
            }
        }
        return null;
    }

    public TransitionFunction getTransitionWithStates(State fromState, State toState) {
        for (TransitionFunction t : transitionFunctions) {
            if (t.currentState.equals(fromState) && t.futureState.equals(toState)) {
                return t;
            }
        }
        return null;
    }


    public SortedSet<State> getFutureStates(State currentState, String symbol) {
        SortedSet<State> futureState = new TreeSet<>();
        for (TransitionFunction t : transitionFunctions) {
            if (t.currentState.equals(currentState) && t.symbol.equals(symbol)) {
                futureState.add(t.futureState);
            }
        }
        return futureState;
    }

    public Map<Pair<State, State>, SortedSet<String>> getTransitionsAFD() {
        Map<Pair<State, State>, SortedSet<String>> transitionsAFD = new HashMap<>();
        for (TransitionFunction t : transitionFunctions) {
            Pair<State, State> states = Pair.create(t.getCurrentState(), t.getFutureState());
            if (!transitionsAFD.containsKey(states)) {
                transitionsAFD.put(states, new TreeSet<String>());
            }
            transitionsAFD.get(states).add(t.getSymbol());
        }
        return transitionsAFD;
    }

    public SortedMap<Pair<State, String>, SortedSet<State>> getTransitionTableAFND() {
        SortedMap<Pair<State, String>, SortedSet<State>> transitionTableAFND =
                new TreeMap<>(new Comparator<Pair<State, String>>() {
                    @Override
                    public int compare(Pair<State, String> lhs, Pair<State, String> rhs) {
                        if (lhs.first.equals(rhs.first)) {
                            return lhs.second.compareTo(rhs.second);
                        }
                        return lhs.first.compareTo(rhs.first);
                    }
                });
        for (State state : states) {
            for (String symbol : getAlphabet()) {
                transitionTableAFND.put(Pair.create(state, symbol), getFutureStates(state, symbol));
            }
        }
        return transitionTableAFND;
    }

    public TransitionFunction[][] getTransitionTable() {
        SortedSet<String> alphabetSet = getAlphabet();
        TransitionFunction[][] transitionTable =
                new TransitionFunction[states.size()][alphabetSet.size()];
        State states[] = this.states.toArray(new State[this.states.size()]);
        String alphabet[] = alphabetSet.toArray(new String[alphabetSet.size()]);
        for (int i = 0; i < states.length; i++) {
                for (int j = 0; j < alphabet.length; j++) {
                    transitionTable[i][j] = getTransition(states[i], alphabet[j]);
                }
        }
        return transitionTable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\nTransições: \n");
        for (TransitionFunction transitionFunction : transitionFunctions) {
            sb.append(transitionFunction.toString())
                    .append('\n');
        }
        return sb.toString();
    }
}
