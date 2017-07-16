package com.ufla.lfapp.core.machine.fsa;


import android.graphics.Point;
import android.support.v4.util.Pair;
import android.text.SpannableString;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.Machine;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.TransitionFunction;
import com.ufla.lfapp.utils.ResourcesContext;

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

public class FiniteStateAutomaton
        extends Machine
        implements Serializable {

    public static final String STATE_ERROR = "err";
    protected SortedSet<FSATransitionFunction> FSATransitionFunctions;
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
        for (FSATransitionFunction FSATransitionFunction : FSATransitionFunctions) {
            alphabet.add(FSATransitionFunction.getSymbol());
        }
        return alphabet;
    }

    public FiniteStateAutomaton(SortedSet<State> states,
                                State initialState, SortedSet<State> finalStates,
                                SortedSet<FSATransitionFunction> FSATransitionFunctions) {
        super(states, initialState, finalStates);
        if (FSATransitionFunctions == null) {
            return;
        }
        this.FSATransitionFunctions = new TreeSet<>();
        for (FSATransitionFunction FSATransitionFunction : FSATransitionFunctions) {
            State currentState = getState(FSATransitionFunction.getCurrentState().getName());
            State futureState = getState(FSATransitionFunction.getFutureState().getName());
            this.FSATransitionFunctions.add(new FSATransitionFunction(currentState,
                    FSATransitionFunction.getSymbol(), futureState));
        }
    }

    public static SortedSet<FSATransitionFunction> copyTransictionFunctions(
            Set<FSATransitionFunction> FSATransitionFunctions) {
        SortedSet<FSATransitionFunction> copyTransictionFunctions = new TreeSet<>();
        if (FSATransitionFunctions == null) {
            return copyTransictionFunctions;
        }
        for (FSATransitionFunction FSATransitionFunction : FSATransitionFunctions) {
            copyTransictionFunctions.add(FSATransitionFunction.copy());
        }
        return copyTransictionFunctions;
    }

    public FiniteStateAutomaton(FiniteStateAutomaton finiteStateAutomaton) {
        super(finiteStateAutomaton);
        if (finiteStateAutomaton == null || finiteStateAutomaton.FSATransitionFunctions == null) {
            return;
        }
        FSATransitionFunctions = new TreeSet<>();
        for (FSATransitionFunction FSATransitionFunction : finiteStateAutomaton.FSATransitionFunctions) {
            State currentState = getState(FSATransitionFunction.getCurrentState().getName());
            State futureState = getState(FSATransitionFunction.getFutureState().getName());
            FSATransitionFunctions.add(new FSATransitionFunction(currentState,
                    FSATransitionFunction.getSymbol(), futureState));
        }
    }

    @Override
    public Set<? extends TransitionFunction> getTransitionFunctionsGen() {
        return FSATransitionFunctions;
    }

    protected FiniteStateAutomaton() {
        states = new TreeSet<>();
        finalStates = new TreeSet<>();
        FSATransitionFunctions = new TreeSet<>();
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
            StringBuilder sb = new StringBuilder();
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

    public FiniteStateAutomaton getMinimizeAutomaton() {
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
        FiniteStateAutomaton finiteStateAutomaton = new FiniteStateAutomaton(this);
        int contNewStates = 0;
        for (Set<State> stateEquals : setOfStateEquals) {
            while (finiteStateAutomaton.states.contains(new State("qc" + contNewStates))) {
                contNewStates++;
            }
            State newState = new State("qc" + contNewStates);
            finiteStateAutomaton.states.add(newState);
            contNewStates++;
            for (State state : stateEquals) {
                finiteStateAutomaton.states.remove(state);
                if (finiteStateAutomaton.finalStates.contains(state)) {
                    finiteStateAutomaton.finalStates.remove(state);
                    finiteStateAutomaton.finalStates.add(newState);
                }
                if (finiteStateAutomaton.initialState != null &&
                        finiteStateAutomaton.initialState.equals(state)) {
                    finiteStateAutomaton.initialState = newState;
                }
                for (FSATransitionFunction FSATransitionFunction : finiteStateAutomaton.getTransitionsFrom(state)) {
                    finiteStateAutomaton.FSATransitionFunctions.add(new FSATransitionFunction(newState,
                            FSATransitionFunction.getSymbol(), FSATransitionFunction.getFutureState()));
                    finiteStateAutomaton.FSATransitionFunctions.remove(FSATransitionFunction);
                }
                for (FSATransitionFunction FSATransitionFunction : finiteStateAutomaton.getTransitionsTo(state)) {
                    finiteStateAutomaton.FSATransitionFunctions.add(new FSATransitionFunction(
                            FSATransitionFunction.getCurrentState(),
                            FSATransitionFunction.getSymbol(), newState));
                    finiteStateAutomaton.FSATransitionFunctions.remove(FSATransitionFunction);
                }
            }
        }
        return finiteStateAutomaton;
    }

    public Set<FSATransitionFunction> getTransitionFunctions() {
        return FSATransitionFunctions;
    }

    public FiniteStateAutomaton getAutomatonWithStatesNameSimplify() {
        FiniteStateAutomaton finiteStateAutomaton = new FiniteStateAutomaton(this);
//        Map<State, State> statesMap = getStatesMapSimplify();
        int contStates = 1;
        finiteStateAutomaton.renameState(finiteStateAutomaton.initialState.getName(), "q0");
        for (State state : finiteStateAutomaton.statesNamesWithout(finiteStateAutomaton.initialState)) {
            finiteStateAutomaton.renameState(state.getName(), "q" + contStates);
            contStates++;
        }
//        finiteStateAutomaton.initialState = new State("q0");
//        finiteStateAutomaton.states = new TreeSet<>();
//        for (State state : statesMap.values()) {
//            states.add(state);
//        }
//        finiteStateAutomaton.finalStates = new TreeSet<>();
//        for (State state : finalStates) {
//            finiteStateAutomaton.finalStates.add(statesMap.get(state));
//        }
//        finiteStateAutomaton.FSATransitionFunctions = new TreeSet<>();
//        for (FSATransitionFunction t : FSATransitionFunctions) {
//            finiteStateAutomaton.FSATransitionFunctions.add(new FSATransitionFunction(
//                    statesMap.get(t.getCurrentState()),
//                    t.getSymbol(), statesMap.get(t.getFutureState())));
//        }
        return finiteStateAutomaton;
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
        for (FSATransitionFunction t : FSATransitionFunctions) {
            if(t.getSymbol().equals(LAMBDA)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAFD() {
        Set<Pair<State, String>> setOfStatesAndSymbols = new HashSet<>();
        for (FSATransitionFunction t : FSATransitionFunctions) {
            Pair<State, String> stateAndSymbol = Pair.create(t.getCurrentState(), t.getSymbol());
            if (setOfStatesAndSymbols.contains(stateAndSymbol) || t.getSymbol().equals(LAMBDA)) {
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

    private FiniteStateAutomaton copy() {
        FiniteStateAutomaton finiteStateAutomaton = new FiniteStateAutomaton();
        finiteStateAutomaton.states = new TreeSet<>(states);
        finiteStateAutomaton.finalStates = new TreeSet<>(finalStates);
        finiteStateAutomaton.initialState = initialState;
        finiteStateAutomaton.FSATransitionFunctions = new TreeSet<>();
        for (FSATransitionFunction t : FSATransitionFunctions) {
            finiteStateAutomaton.FSATransitionFunctions.add(t.copy());
        }
        return finiteStateAutomaton;
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
        
        for (FSATransitionFunction FSATransitionFunction : FSATransitionFunctions) {
            if (FSATransitionFunction.equalsCurrentState(fromState) &&
                    FSATransitionFunction.equalsSymbol(symbol)) {
                states.add(FSATransitionFunction.getFutureState());
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

    public FiniteStateAutomaton AFNDLambdaToAFND() {
        if (!isAFNDLambda()) {
            return copy();
        }
        System.out.println("OK\nOK\nOK");
        FiniteStateAutomaton finiteStateAutomatonAFND = new FiniteStateAutomaton();
        SortedMap<Pair<State, String>, SortedSet<State>> transitionTable = getTransitionTableAFND();
        finiteStateAutomatonAFND.FSATransitionFunctions = new TreeSet<>();
        insertFechoLambda(transitionTable);
        for (SortedMap.Entry<Pair<State, String>, SortedSet<State>> entry :
                transitionTable.entrySet()) {
            if (entry.getKey().second.equals(LAMBDA) || entry.getKey().second.equals(FECHO_LAMBDA)) {
                continue;
            }
            // Adicionando transições através do fecho-lambda mais uma leitura e do fecho lambda novamente
            for (State fechoLambda : transitionTable.get(Pair.create(entry.getKey().first,
                    FECHO_LAMBDA))) {
                for (State futureState : transitionTable.get(Pair.create(fechoLambda,
                        entry.getKey().second))) {
                    for (State fechoLambdaFuture : transitionTable.get(Pair.create(futureState,
                            FECHO_LAMBDA))) {
                        finiteStateAutomatonAFND.FSATransitionFunctions.add(new FSATransitionFunction(entry.getKey().first,
                                entry.getKey().second, fechoLambdaFuture));
                    }
                }
            }
            // Adicionando transições através de uma leitura e também do fecho-lambda após essa
            // leitura
            for (State futureState : entry.getValue()) {
                finiteStateAutomatonAFND.FSATransitionFunctions.add(new FSATransitionFunction(entry.getKey().first,
                        entry.getKey().second, futureState));
                for (State futureStateB : transitionTable.get(Pair.create(futureState,
                        FECHO_LAMBDA))) {
                    finiteStateAutomatonAFND.FSATransitionFunctions.add(new FSATransitionFunction(entry.getKey().first,
                            entry.getKey().second, futureStateB));
                }
            }
        }
        finiteStateAutomatonAFND.initialStatesFromAFNDLambdaToAFD = transitionTable.get(
                Pair.create(initialState, FECHO_LAMBDA));
        finiteStateAutomatonAFND.initialState = initialState;
        finiteStateAutomatonAFND.finalStates = new TreeSet<>(finalStates);
        for (State state : states) {
            if (finalStates.contains(state)) {
                continue;
            }
            for (State finalState : finalStates) {
                if (transitionTable.get(Pair.create(state,
                        FECHO_LAMBDA)).contains(finalState)) {
                    finiteStateAutomatonAFND.finalStates.add(state);
                }
            }
        }
        finiteStateAutomatonAFND.states = new TreeSet<>();
        for (FSATransitionFunction t : finiteStateAutomatonAFND.FSATransitionFunctions) {
            finiteStateAutomatonAFND.states.add(t.getCurrentState());
            finiteStateAutomatonAFND.states.add(t.getFutureState());
        }
        return finiteStateAutomatonAFND;
    }

    public SortedSet<String> getSymbols(State currentState, State futureState) {
        SortedSet<String> symbols = new TreeSet<>();
        for (FSATransitionFunction t : FSATransitionFunctions) {
            if (t.getCurrentState().equals(currentState) && t.getFutureState().equals(futureState))  {
                symbols.add(t.getSymbol());
            }
        }
        return symbols;
    }

    public FiniteStateAutomaton AFNDtoAFD() {
        if (isAFD()) {
            return copy();
        }
        FiniteStateAutomaton finiteStateAutomatonAFD = new FiniteStateAutomaton();
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
        finiteStateAutomatonAFD.initialState = new State(sbInitialState.toString());
        finiteStateAutomatonAFD.states = new TreeSet<>();
        finiteStateAutomatonAFD.finalStates = new TreeSet<>();
        finiteStateAutomatonAFD.states.add(finiteStateAutomatonAFD.initialState);
        if (isFinalState) {
            finiteStateAutomatonAFD.finalStates.add(finiteStateAutomatonAFD.initialState);
        }
        finiteStateAutomatonAFD.FSATransitionFunctions = new TreeSet<>();
        SortedMap<Pair<State, String>, SortedSet<State>> transitionTable = getTransitionTableAFND();
        Deque<Pair<State, SortedSet<State>>> newStatesQueue = new ArrayDeque<>();
        newStatesQueue.offer(Pair.create(finiteStateAutomatonAFD.initialState, initialStateSet));
        while (!newStatesQueue.isEmpty()) {
            Pair<State, SortedSet<State>> currentState = newStatesQueue.poll();
            for (String symbol : getAlphabet()) {
                SortedSet<State> futureStateSet = new TreeSet<>();
                for (State currentStatePartial : currentState.second) {
                    futureStateSet.addAll(transitionTable.get(
                            Pair.create(currentStatePartial, symbol)));
                }
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
                    finiteStateAutomatonAFD.FSATransitionFunctions.add(new FSATransitionFunction(currentState.first,
                            symbol, futureState));
                    if (!finiteStateAutomatonAFD.states.contains(futureState)) {
                        finiteStateAutomatonAFD.states.add(futureState);
                        if (isFinalState) {
                            finiteStateAutomatonAFD.finalStates.add(futureState);
                        }
                        newStatesQueue.offer(Pair.create(futureState, futureStateSet));
                    }
                }
            }
        }
        return finiteStateAutomatonAFD;
    }

    public FiniteStateAutomaton AFNDLambdaToAFD() {
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

    public List<String> configurations;
    public List<SpannableString> configurationsSpan;

    public boolean processEntry(String word) throws Exception {
        if (!validityFromAlphabet(word)) {
            List<String> symbolsExternFromAlphabet = symbolsExternFromAlphabet(word);
            StringBuilder messageException = new StringBuilder();
            messageException.append(ResourcesContext.getString(R.string.exception_symbol_out_alphabet))
                .append(symbolsExternFromAlphabet.get(0));
            for (int i = 1; i < symbolsExternFromAlphabet.size(); i++) {
                messageException.append(", ")
                        .append(symbolsExternFromAlphabet.get(i));
            }
            throw new Exception(messageException.toString());
        }
        configurations = new ArrayList<>();
        configurationsSpan = new ArrayList<>();
        Deque<Process> stackProcess = new ArrayDeque<>();
        stackProcess.push(new Process(0, initialState));
        int cont = 0;
        while (!stackProcess.isEmpty()) {
            Process actualProcess = stackProcess.pop();
            StringBuilder sb = new StringBuilder();
            sb.append(word.substring(0, actualProcess.position));
            sb.append(actualProcess.actualState);
            sb.append(word.substring(actualProcess.position));
            SpannableString span = new SpannableString(sb.toString());
//            span.setSpan(new BackgroundColorSpan(ResourcesContext.getColor(R.color.PaleGreen2)), actualProcess.position,
//                    actualProcess.position + actualProcess.actualState.toString().length(),
//                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            configurations.add(sb.toString());
            configurationsSpan.add(span);
            if (actualProcess.position == word.length() &&
                    finalStates.contains(actualProcess.actualState)) {
                return true;
            }
            boolean generateProcess = false;
            if (actualProcess.position < word.length()) {
                Set<FSATransitionFunction> transitions = getTransitions(actualProcess.actualState,
                        LAMBDA);
                if (!transitions.isEmpty()) {
                    generateProcess = true;
                }
                for (FSATransitionFunction t : transitions) {
                    stackProcess.push(new Process(actualProcess.position , t.getFutureState()));
                }
                transitions = getTransitions(actualProcess.actualState,
                        Character.toString(word.charAt(actualProcess.position)));
                if (!transitions.isEmpty()) {
                    generateProcess = true;
                }
                for (FSATransitionFunction t : transitions) {
                    stackProcess.push(new Process(actualProcess.position + 1, t.getFutureState()));
                }
            }
            if (!generateProcess) {
                configurations.remove(configurations.size() - 1);
                configurationsSpan.remove(configurationsSpan.size() - 1);
            }
            if (cont == MAX_PROCESS) {
                throw new Exception(ResourcesContext.getString(R.string.exception_loop_process));
            }
            cont++;
        }
        return false;
    }

    public State getStateError() {
        return stateError;
    }

    public Set<FSATransitionFunction> getTransitions(State fromState, String symbol) {
        Set<FSATransitionFunction> transitions = new HashSet<>();
        for (FSATransitionFunction t : FSATransitionFunctions) {
            if (t.getCurrentState().equals(fromState)
                    && t.getSymbol().equals(symbol)) {
                transitions.add(t);
            }
        }
        return transitions;
    }

    public Set<FSATransitionFunction> getTransitionFunctionsToCompleteAutomaton() {
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
        Set<FSATransitionFunction> FSATransitionFunctionsToComplAuto = new HashSet<>();
        for (int i = 0; i < states.length; i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (getTransition(states[i], alphabet[j]) == null) {
                    FSATransitionFunctionsToComplAuto.add(new FSATransitionFunction(states[i], alphabet[j],
                            stateError));
                }
            }
        }
        if (!FSATransitionFunctionsToComplAuto.isEmpty()) {
            for (int j = 0; j < alphabet.length; j++) {
                FSATransitionFunctionsToComplAuto.add(new FSATransitionFunction(stateError, alphabet[j],
                        stateError));
            }
        }
        return FSATransitionFunctionsToComplAuto;
    }

    public FiniteStateAutomaton getCompleteAutomaton() {
        if (states.contains(new State(STATE_ERROR))) {
            int cont = 0;
            while (states.contains(new State(STATE_ERROR + cont))) {
                cont++;
            }
            stateError = new State(STATE_ERROR + cont);
        } else {
            stateError = new State(STATE_ERROR);
        }
        FiniteStateAutomaton finiteStateAutomaton = new FiniteStateAutomaton();
        finiteStateAutomaton.initialState = initialState;
        finiteStateAutomaton.states = new TreeSet<>(states);
        finiteStateAutomaton.finalStates = new TreeSet<>(finalStates);
        finiteStateAutomaton.FSATransitionFunctions = new TreeSet<>(FSATransitionFunctions);
        State states[] = this.states.toArray(new State[this.states.size()]);
        SortedSet<String> alphabetSet = getAlphabet();
        String alphabet[] = alphabetSet.toArray(new String[alphabetSet.size()]);
        for (int i = 0; i < states.length; i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (getTransition(states[i], alphabet[j]) == null) {
                    finiteStateAutomaton.states.add(stateError);
                    finiteStateAutomaton.FSATransitionFunctions.add(new FSATransitionFunction(states[i], alphabet[j],
                            stateError));
                }
            }
        }
        for (int j = 0; j < alphabet.length; j++) {
            finiteStateAutomaton.FSATransitionFunctions.add(new FSATransitionFunction(stateError, alphabet[j],
                    stateError));
        }
        return finiteStateAutomaton;
    }


    public State getFutureStateOfTransition(State fromState, String symbol) {
        FSATransitionFunction t = getTransition(fromState, symbol);
        if (t != null) {
            return t.getFutureState();
        }
        return null;
    }

    public Set<FSATransitionFunction> getTransitionsFrom(State state) {
        Set<FSATransitionFunction> FSATransitionFunctions = new HashSet<>();
        for (FSATransitionFunction t : this.FSATransitionFunctions) {
            if (t.getCurrentState().equals(state)) {
                FSATransitionFunctions.add(t);
            }
        }
        return FSATransitionFunctions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FiniteStateAutomaton that = (FiniteStateAutomaton) o;

        return FSATransitionFunctions != null ? FSATransitionFunctions.equals(that.FSATransitionFunctions) : that.FSATransitionFunctions == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (FSATransitionFunctions != null ? FSATransitionFunctions.hashCode() : 0);
        return result;
    }

    public Set<FSATransitionFunction> getTransitionsTo(State state) {
        Set<FSATransitionFunction> FSATransitionFunctions = new HashSet<>();
        for (FSATransitionFunction t : this.FSATransitionFunctions) {
            if (t.getFutureState().equals(state)) {
                FSATransitionFunctions.add(t);
            }
        }
        return FSATransitionFunctions;

    }

    public FSATransitionFunction getTransition(State fromState, String symbol) {
        for (FSATransitionFunction t : FSATransitionFunctions) {
            if (t.getCurrentState().equals(fromState)
                    && t.getSymbol().equals(symbol)) {
                return t;
            }
        }
        return null;
    }

    public FSATransitionFunction getTransitionWithStates(State fromState, State toState) {
        for (FSATransitionFunction t : FSATransitionFunctions) {
            if (t.getCurrentState().equals(fromState)
                    && t.getFutureState().equals(toState)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public MachineType getMachineType() {
        return MachineType.FSA;
    }

    public Set<FSATransitionFunction> getTransitionsWithStates(State fromState, State toState) {
        Set<FSATransitionFunction> transitions = new HashSet<>();
        for (FSATransitionFunction t : FSATransitionFunctions) {
            if (t.getCurrentState().equals(fromState)
                    && t.getFutureState().equals(toState)) {
                transitions.add(t);
            }
        }
        return transitions;
    }

    public SortedSet<State> getFutureStates(State currentState, String symbol) {
        SortedSet<State> futureState = new TreeSet<>();
        for (FSATransitionFunction t : FSATransitionFunctions) {
            if (t.getCurrentState().equals(currentState)
                    && t.getSymbol().equals(symbol)) {
                futureState.add(t.getFutureState());
            }
        }
        return futureState;
    }

    public Map<Pair<State, State>, SortedSet<String>> getTransitionsAFD() {
        Map<Pair<State, State>, SortedSet<String>> transitionsAFD = new HashMap<>();
        for (FSATransitionFunction t : FSATransitionFunctions) {
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

    public FSATransitionFunction[][] getTransitionTable() {
        SortedSet<String> alphabetSet = getAlphabet();
        FSATransitionFunction[][] transitionTable =
                new FSATransitionFunction[states.size()][alphabetSet.size()];
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
        StringBuilder sb = new StringBuilder(super.toString())
                .append('\n')
        .append(ResourcesContext.getString(R.string.transition))
        .append(": \n");
        Set<FSATransitionFunction> FSATransitionFunctions = getTransitionFunctions();
        for (FSATransitionFunction FSATransitionFunction : FSATransitionFunctions) {
            sb.append(FSATransitionFunction)
                    .append('\n');
        }
        return sb.toString();
    }

    public String toStringTest() {
        StringBuilder sb = new StringBuilder(super.toStringTest())
                .append('\n')
                .append("Transitions")
                .append(": \n");
        Set<FSATransitionFunction> FSATransitionFunctions = getTransitionFunctions();
        for (FSATransitionFunction FSATransitionFunction : FSATransitionFunctions) {
            sb.append(FSATransitionFunction)
                    .append('\n');
        }
        return sb.toString();
    }
}
