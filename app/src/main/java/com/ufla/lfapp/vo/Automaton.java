package com.ufla.lfapp.vo;


import android.graphics.Point;
import android.support.v4.util.Pair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
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

public class Automaton extends Machine {

    public static final String STATE_ERROR = "err";
    private Set<TransitionFunction> transitionFunctions;
    private String stateError;
    private static String LAMBDA = ".";
    private static String FECHO_LAMBDA = "*";
    private static final int MAX_PROCESS = 100000;
    private Set<String> initialStatesFromAFNDLambdaToAFD;

    public Automaton(Set<String> states, Set<String> alphabet,
                   String initialState, Set<String> finalStates,
                     Set<TransitionFunction> transitionFunctions) {
        super(states, alphabet, initialState, finalStates);
        this.transitionFunctions = transitionFunctions;
    }

    protected Automaton() {

    }

    public Set<String> statesWithout(String state) {
        Set<String> statesWithout = new HashSet<>(states);
        statesWithout.remove(state);
        return statesWithout;
    }

    public Map<String, String> getStatesMapSimplify() {
        int contStates = 0;
        Map<String, String> statesMap = new HashMap<>();
        statesMap.put(initialState, "q" + contStates);
        contStates++;
        for (String state : statesWithout(initialState)) {
            statesMap.put(state, "q" + contStates);
            contStates++;
        }
        return statesMap;
    }

    public Set<TransitionFunction> getTransitionFunctions() {
        return transitionFunctions;
    }

    public Automaton getAutomatonWithStatesNameSimplify() {
        Automaton automaton = new Automaton();
        Map<String, String> statesMap = getStatesMapSimplify();
        automaton.initialState = "q0";
        automaton.states = new HashSet<>(statesMap.values());
        automaton.finalStates = new HashSet<>();
        for (String state : finalStates) {
            automaton.finalStates.add(statesMap.get(state));
        }
        automaton.alphabet = new HashSet<>(alphabet);
        automaton.transitionFunctions = new HashSet<>();
        for (TransitionFunction t : transitionFunctions) {
            automaton.transitionFunctions.add(new TransitionFunction(
                    statesMap.get(t.getCurrentState()),
                    t.getSymbol(), statesMap.get(t.getFutureState())));
        }
        return automaton;
    }

    public SortedMap<String, Point> getStatesPoints() {
        SortedMap<String, Point> statesPoints = new TreeMap<>();
        int qtdMaxPoint = (int) Math.ceil(Math.sqrt(states.size()));
        int cont=0, x=2, y=2;
        for (String state : states) {
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
        Set<Pair<String, String>> setOfStatesAndSymbols = new HashSet<>();
        for (TransitionFunction t : transitionFunctions) {
            Pair<String, String> stateAndSymbol = Pair.create(t.currentState, t.symbol);
            if (setOfStatesAndSymbols.contains(stateAndSymbol) || t.symbol.equals(LAMBDA)) {
                return false;
            }
            setOfStatesAndSymbols.add(stateAndSymbol);
        }
        return true;
    }

    public void insertFechoLambda(SortedMap<Pair<String, String>, SortedSet<String>> transitionTable) {
        for (String state : states) {
            SortedSet<String> fechoLambda = new TreeSet<>();
            Set<String> statesVisited = new HashSet<>();
            Deque<String> statesToVisited = new ArrayDeque<>();
            statesToVisited.push(state);
            statesVisited.add(state);
            fechoLambda.add(state);
            while (!statesToVisited.isEmpty()) {
                for (String stateForVisite : transitionTable.get(
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
        automaton.states = new HashSet<>(states);
        automaton.alphabet = new HashSet<>(alphabet);
        automaton.finalStates = new HashSet<>(finalStates);
        automaton.initialState = initialState;
        automaton.transitionFunctions = new HashSet<>();
        for (TransitionFunction t : transitionFunctions) {
            automaton.transitionFunctions.add(t.copy());
        }
        return automaton;
    }

    public Automaton AFNDLambdaToAFND() {
        if (!isAFNDLambda()) {
            return copy();
        }
        Automaton automatonAFND = new Automaton();
        SortedMap<Pair<String, String>, SortedSet<String>> transitionTable = getTransitionTableAFND();
        automatonAFND.transitionFunctions = new HashSet<>();
        insertFechoLambda(transitionTable);
        for (SortedMap.Entry<Pair<String, String>, SortedSet<String>> entry :
                transitionTable.entrySet()) {
            if (entry.getKey().second.equals(LAMBDA) || entry.getKey().second.equals(FECHO_LAMBDA)) {
                continue;
            }
            // Adicionando transições através do fecho-lambda mais uma leitura
            for (String fechoLambda : transitionTable.get(Pair.create(entry.getKey().first,
                    FECHO_LAMBDA))) {
                for (String futureState : transitionTable.get(Pair.create(fechoLambda,
                        entry.getKey().second))) {
                    automatonAFND.transitionFunctions.add(new TransitionFunction(entry.getKey().first,
                            entry.getKey().second, futureState));
                }
            }
            // Adicionando transições através de uma leitura e também do fecho-lambda após essa
            // leitura
            for (String futureState : entry.getValue()) {
                automatonAFND.transitionFunctions.add(new TransitionFunction(entry.getKey().first,
                        entry.getKey().second, futureState));
                for (String futureStateB : transitionTable.get(Pair.create(futureState,
                        FECHO_LAMBDA))) {
                    automatonAFND.transitionFunctions.add(new TransitionFunction(entry.getKey().first,
                            entry.getKey().second, futureStateB));
                }
            }
        }
        automatonAFND.initialStatesFromAFNDLambdaToAFD = transitionTable.get(
                Pair.create(initialState, FECHO_LAMBDA));
        automatonAFND.initialState = initialState;
        automatonAFND.finalStates = new HashSet<>(finalStates);
        for (String state : states) {
            if (finalStates.contains(state)) {
                continue;
            }
            for (String finalState : finalStates) {
                if (transitionTable.get(Pair.create(state,
                        FECHO_LAMBDA)).contains(finalState)) {
                    automatonAFND.finalStates.add(state);
                }
            }
        }
        automatonAFND.states = new HashSet<>();
        automatonAFND.alphabet = new HashSet<>();
        for (TransitionFunction t : automatonAFND.transitionFunctions) {
            automatonAFND.states.add(t.getCurrentState());
            automatonAFND.states.add(t.getFutureState());
            automatonAFND.alphabet.add(t.getSymbol());
        }
        return automatonAFND;
    }

    public Automaton AFNDtoAFD() {
        if (isAFD()) {
            return copy();
        }
        Automaton automatonAFD = new Automaton();
        boolean isFinalState = false;
        StringBuilder sbInitialState = new StringBuilder();
        SortedSet<String> initialStateSet = new TreeSet<>();
        if (initialStatesFromAFNDLambdaToAFD != null && !initialStatesFromAFNDLambdaToAFD.isEmpty()) {
            initialStateSet = new TreeSet<>(initialStatesFromAFNDLambdaToAFD);
            sbInitialState.append('<');
            for (String state: initialStatesFromAFNDLambdaToAFD) {
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
        automatonAFD.initialState = sbInitialState.toString();
        automatonAFD.states = new HashSet<>();
        automatonAFD.finalStates = new HashSet<>();
        automatonAFD.states.add(automatonAFD.initialState);
        if (isFinalState) {
            automatonAFD.finalStates.add(automatonAFD.initialState);
        }
        automatonAFD.alphabet = new HashSet<>(alphabet);
        automatonAFD.transitionFunctions = new HashSet<>();
        SortedMap<Pair<String, String>, SortedSet<String>> transitionTable = getTransitionTableAFND();
        Deque<Pair<String, SortedSet<String>>> newStatesQueue = new ArrayDeque<>();
        newStatesQueue.offer(Pair.create(automatonAFD.initialState, initialStateSet));
        while (!newStatesQueue.isEmpty()) {
            Pair<String, SortedSet<String>> currentState = newStatesQueue.poll();
            for (String currentStatePartial : currentState.second) {
                for (String symbol : alphabet) {
                    SortedSet<String> futureStateSet = transitionTable.get(
                            Pair.create(currentStatePartial, symbol));
                    if (!futureStateSet.isEmpty()) {
                        isFinalState = false;
                        StringBuilder futureStateSb = new StringBuilder();
                        futureStateSb.append('<');
                        for (String fState : futureStateSet) {
                            futureStateSb.append(fState)
                                    .append(',');
                            if (finalStates.contains(fState)) {
                                isFinalState = true;
                            }
                        }
                        futureStateSb.deleteCharAt(futureStateSb.length() - 1);
                        futureStateSb.append('>');
                        String futureState = futureStateSb.toString();
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
        for (int i = 0; i < word.length(); i++) {
            if (!alphabet.contains(Character.toString(word.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    private List<String> symbolsExternFromAlphabet(String word) {
        List<String> symbolsExternFromAlphabet = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            String symbol = Character.toString(word.charAt(i));
            if (!alphabet.contains(symbol)) {
                symbolsExternFromAlphabet.add(symbol);
            }
        }
        return symbolsExternFromAlphabet;
    }

    class Process {
        int position;
        String actualState;

        Process (int position, String actualState) {
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

    public String getStateError() {
        return stateError;
    }

    public Set<TransitionFunction> getTransitions(String fromState, String symbol) {
        Set<TransitionFunction> transitions = new HashSet<>();
        for (TransitionFunction t : transitionFunctions) {
            if (t.currentState.equals(fromState) && t.symbol.equals(symbol)) {
                transitions.add(t);
            }
        }
        return transitions;
    }

    public Set<TransitionFunction> getTransitionFunctionsToCompleteAutomaton() {
        if (states.contains(STATE_ERROR)) {
            int cont = 0;
            while (states.contains(STATE_ERROR + cont)) {
                cont++;
            }
            stateError = STATE_ERROR + cont;
        } else {
            stateError = STATE_ERROR;
        }
        String states[] = this.states.toArray(new String[this.states.size()]);
        String alphabet[] = this.alphabet.toArray(new String[this.alphabet.size()]);
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
        if (states.contains(STATE_ERROR)) {
            int cont = 0;
            while (states.contains(STATE_ERROR + cont)) {
                cont++;
            }
            stateError = STATE_ERROR + cont;
        } else {
            stateError = STATE_ERROR;
        }
        Automaton automaton = new Automaton();
        automaton.alphabet = new HashSet<>(alphabet);
        automaton.initialState = initialState;
        automaton.states = new HashSet<>(states);
        automaton.finalStates = new HashSet<>(finalStates);
        String states[] = this.states.toArray(new String[this.states.size()]);
        String alphabet[] = this.alphabet.toArray(new String[this.alphabet.size()]);
        for (int i = 0; i < states.length; i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (getTransition(states[i], alphabet[j]) == null) {
                    automaton.states.add(stateError);
                    automaton.transitionFunctions.add(new TransitionFunction(states[i], alphabet[j],
                            STATE_ERROR));
                }
            }
        }
        return automaton;
    }

    public TransitionFunction getTransition(String fromState, String symbol) {
        for (TransitionFunction t : transitionFunctions) {
            if (t.currentState.equals(fromState) && t.symbol.equals(symbol)) {
                return t;
            }
        }
        return null;
    }


    public SortedSet<String> getFutureStates(String currentState, String symbol) {
        SortedSet<String> futureState = new TreeSet<>();
        for (TransitionFunction t : transitionFunctions) {
            if (t.currentState.equals(currentState) && t.symbol.equals(symbol)) {
                futureState.add(t.futureState);
            }
        }
        return futureState;
    }

    public Map<Pair<String, String>, SortedSet<String>> getTransitionsAFD() {
        Map<Pair<String, String>, SortedSet<String>> transitionsAFD = new HashMap<>();
        for (TransitionFunction t : transitionFunctions) {
            Pair<String, String> states = Pair.create(t.getCurrentState(), t.getFutureState());
            if (!transitionsAFD.containsKey(states)) {
                transitionsAFD.put(states, new TreeSet<String>());
            }
            transitionsAFD.get(states).add(t.getSymbol());
        }
        return transitionsAFD;
    }

    public SortedMap<Pair<String, String>, SortedSet<String>> getTransitionTableAFND() {
        SortedMap<Pair<String, String>, SortedSet<String>> transitionTableAFND =
                new TreeMap<>(new Comparator<Pair<String, String>>() {
                    @Override
                    public int compare(Pair<String, String> lhs, Pair<String, String> rhs) {
                        if (lhs.first.equals(rhs.first)) {
                            return lhs.second.compareTo(rhs.second);
                        }
                        return lhs.first.compareTo(rhs.first);
                    }
                });
        for (String state : states) {
            for (String symbol : alphabet) {
                transitionTableAFND.put(Pair.create(state, symbol), getFutureStates(state, symbol));
            }
        }
        return transitionTableAFND;
    }

    public TransitionFunction[][] getTransitionTable() {
        TransitionFunction[][] transitionTable =
                new TransitionFunction[states.size()][alphabet.size()];
        String states[] = this.states.toArray(new String[this.states.size()]);
        String alphabet[] = this.alphabet.toArray(new String[this.alphabet.size()]);
        for (int i = 0; i < states.length; i++) {
                for (int j = 0; j < alphabet.length; j++) {
                    transitionTable[i][j] = getTransition(states[i], alphabet[j]);
                }
        }
        return transitionTable;
    }

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
