package com.ufla.lfapp.core.machine.fsa;

import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.core.machine.State;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by carlos on 12/6/16.
 */


public class FiniteStateAutomatonGUI
        extends FiniteStateAutomaton
        implements Serializable {

    private SortedMap<State, MyPoint> stateGridPositions;



    private static SortedMap<State, MyPoint> convertToMyPoint(SortedMap<State, Point> stateGridPositions) {
        SortedMap<State, MyPoint> stateGridPositionsMyPoint = new TreeMap<>();
        for (SortedMap.Entry<State, Point> entry : stateGridPositions.entrySet()) {
            stateGridPositionsMyPoint.put(entry.getKey(), MyPoint.convertPoint(entry.getValue()));
        }
        return stateGridPositionsMyPoint;
    }

    private  static SortedMap<State, Point> convertToPoint(SortedMap<State, MyPoint> stateGridPositions) {
        SortedMap<State, Point> stateGridPositionsPoint = new TreeMap<>();
        for (Map.Entry<State, MyPoint> entry : stateGridPositions.entrySet()) {
            stateGridPositionsPoint.put(entry.getKey(), entry.getValue().toPoint());
        }
        return stateGridPositionsPoint;
    }

    private FiniteStateAutomatonGUI() {
        super();
        this.stateGridPositions = new TreeMap<>();
        creationDate = new Date();
        id = -1L;
    }

    public FiniteStateAutomatonGUI(FiniteStateAutomaton finiteStateAutomaton,
                                   SortedMap<State, Point> stateGridPositions) {
        super(finiteStateAutomaton);
        this.stateGridPositions = (stateGridPositions == null) ? new TreeMap<State, MyPoint>()
                : FiniteStateAutomatonGUI.convertToMyPoint(stateGridPositions);
        this.creationDate = new Date();
        id = -1L;
    }

    public FiniteStateAutomatonGUI(FiniteStateAutomaton finiteStateAutomaton,
                                   Map<State, MyPoint> stateGridPositions) {
        super(finiteStateAutomaton);
        this.stateGridPositions = (stateGridPositions == null) ? new TreeMap<State, MyPoint>()
                : new TreeMap<>(stateGridPositions);
        this.creationDate = new Date();
        id = -1L;
    }

    private void addTransitionsWithDeslocState(Set<FSATransitionFunction> FSATransitionFunctions,
                                              int desloc) {
        for (FSATransitionFunction FSATransitionFunction : FSATransitionFunctions) {
            int currentStateNumber = Integer.parseInt(FSATransitionFunction.getCurrentState()
                    .getName().substring(1));
            int futureStateNumber = Integer.parseInt(FSATransitionFunction.getFutureState()
                    .getName().substring(1));
            this.FSATransitionFunctions.add(new FSATransitionFunction(
                    getState("q" + (desloc + currentStateNumber)),
                    FSATransitionFunction.getSymbol(),
                    getState("q" + (desloc + futureStateNumber))));
        }
    }

    private void addStatesWithDesloc(int contStates, int desloc) {
        for (int i = 0; i < contStates; i++) {
            states.add(new State("q" + (desloc + i)));
        }
    }

    private FiniteStateAutomatonGUI kleene() {
       // Log.d("kleene", "kleene");
        FiniteStateAutomatonGUI automaton = new FiniteStateAutomatonGUI(this);

        int contState = automaton.states.size();
        for (int i = contState - 1; i > -1; i--) {
            automaton.renameState("q" + i, "q" + (i + 1));
        }

        State state0 = new State("q0");
        State state1 = automaton.getState("q1");
        State stateN_1 = automaton.getState("q" + contState);
        State stateN = new State("q" + (contState + 1));

        automaton.states.add(state0);
        automaton.initialState = state0;
        automaton.states.add(stateN);
        automaton.finalStates.remove(stateN_1);
        automaton.finalStates.add(stateN);

        automaton.FSATransitionFunctions.add(new FSATransitionFunction(state0, LAMBDA, state1));
        automaton.FSATransitionFunctions.add(new FSATransitionFunction(state0, LAMBDA, stateN));
        automaton.FSATransitionFunctions.add(new FSATransitionFunction(stateN_1, LAMBDA, stateN));
        automaton.FSATransitionFunctions.add(new FSATransitionFunction(stateN, LAMBDA, state1));

        contState = automaton.states.size() - 1;
        automaton.stateGridPositions.put(state0, new MyPoint(0, 0));
        MyPoint lastPoint = new MyPoint(0, 0);
        int deslocY = 1 - automaton.getSmallestY();
        for (int i = 1; i < contState; i++) {
            State state = automaton.getState("q" + i);
            MyPoint myPoint = automaton.stateGridPositions.get(state);
            myPoint.x += 2;
            myPoint.y += deslocY;
            lastPoint = myPoint;
        }
        automaton.stateGridPositions.put(stateN, new MyPoint(lastPoint.x + 2, 0));

        return automaton;
    }

    private FiniteStateAutomatonGUI concat(FiniteStateAutomatonGUI automaton) {
        //Log.d("concat", "concat");
        FiniteStateAutomatonGUI result = new FiniteStateAutomatonGUI(this);

        int contStatesA = states.size();
        int lastStateA = contStatesA - 1;
        int contStatesB = automaton.states.size();

        result.addStatesWithDesloc(contStatesB - 1, contStatesA);
        result.addTransitionsWithDeslocState(automaton.getTransitionFunctions(), lastStateA);
        result.finalStates.remove(result.getState("q" + lastStateA));
        result.finalStates.add(result.getState("q" + (lastStateA + contStatesB - 1)));

        int contStates = contStatesB + contStatesA - 1;
        MyPoint lastPoint = result.stateGridPositions.get(result.getState("q" + lastStateA));

        for (int i = contStatesA; i < contStates; i++) {
            MyPoint actualPoint = automaton.stateGridPositions.get(automaton
                    .getState("q" + (i - lastStateA)));
            result.stateGridPositions.put(result.getState("q" + i),
                    new MyPoint(lastPoint.x + actualPoint.x, lastPoint.y + actualPoint.y));
        }

        return result;
    }

    private static FiniteStateAutomatonGUI newAutomaton(String symbol) {
        //Log.d("new", "new -> " + symbol);
        FiniteStateAutomatonGUI automaton = new FiniteStateAutomatonGUI();
        State state0 = new State("q" + 0);
        State stateN = new State("q" + 1);

        automaton.states.add(state0);
        automaton.states.add(stateN);
        automaton.initialState = state0;
        automaton.finalStates.add(stateN);

        automaton.stateGridPositions.put(state0, new MyPoint(0, 0));
        automaton.stateGridPositions.put(stateN, new MyPoint(2, 0));

        automaton.FSATransitionFunctions.add(new FSATransitionFunction(state0, symbol, stateN));

        return automaton;

    }

    private int getSmallestY() {
        int y = 0;
        for (Map.Entry<State, MyPoint> myPointEntry : stateGridPositions.entrySet()) {
            int actualY = myPointEntry.getValue().y;
            if (actualY < y) {
                y = actualY;
            }
        }
        return y;
    }

    private int getBiggestY() {
        int y = 0;
        for (Map.Entry<State, MyPoint> myPointEntry : stateGridPositions.entrySet()) {
            int actualY = myPointEntry.getValue().y;
            if (actualY > y) {
                y = actualY;
            }
        }
        return y;
    }

    public FiniteStateAutomatonGUI or(FiniteStateAutomatonGUI automaton) {
        //Log.d("or", "or");
        FiniteStateAutomatonGUI result = new FiniteStateAutomatonGUI(this);

        int contStatesA = this.states.size();
        int beginStateB = contStatesA + 1;
        int contStatesB = automaton.states.size();
        int lastX = 0;
        int biggerYA = 0;
        int deslocY = 1 + (this.getBiggestY() - this.getSmallestY()) ;
        for (int i = contStatesA - 1; i > -1; i--) {
            result.renameState("q" + i, "q" + (i + 1));
        }
        for (int i = 1; i <= contStatesA; i++) {
            State state = result.getState("q" + i);
            MyPoint actualPoint = result.stateGridPositions.get(state);
            actualPoint.x += 2;
            actualPoint.y -= deslocY;
            if (actualPoint.x > lastX) {
                lastX = actualPoint.x;
            }
            if (actualPoint.y > biggerYA) {
                biggerYA = actualPoint.y;
            }
        }
        result.addStatesWithDesloc(contStatesB, beginStateB);
        result.addTransitionsWithDeslocState(automaton.getTransitionFunctions(), beginStateB);

        int max = contStatesA + 1 + contStatesB;
        deslocY = 1 + (automaton.getBiggestY() - automaton.getSmallestY());
        for (int i = contStatesA + 1; i < max; i++) {
            State state = result.getState("q" + i);
            State stateB = automaton.getState("q" + (i - contStatesA - 1));
            MyPoint actualPoint = new MyPoint(automaton.stateGridPositions.get(stateB));
            actualPoint.x += 2;
            actualPoint.y += deslocY;
            result.stateGridPositions.put(state, actualPoint);
            if (actualPoint.x > lastX) {
                lastX = actualPoint.x;
            }
        }

        State state0 = new State("q0");
        State stateA1 = result.getState("q1");
        State stateAN = result.getState("q" + contStatesA);
        State stateB1 = result.getState("q" + beginStateB);
        State stateBN = result.getState("q" + (contStatesA + contStatesB));
        State stateN = new State("q" + (contStatesA + contStatesB + 1));

        result.states.add(state0);
        result.states.add(stateN);
        result.finalStates.remove(stateAN);
        result.finalStates.add(stateN);
        result.initialState = state0;

        result.FSATransitionFunctions.add(new FSATransitionFunction(state0, LAMBDA, stateA1));
        result.FSATransitionFunctions.add(new FSATransitionFunction(state0, LAMBDA, stateB1));
        result.FSATransitionFunctions.add(new FSATransitionFunction(stateAN, LAMBDA, stateN));
        result.FSATransitionFunctions.add(new FSATransitionFunction(stateBN, LAMBDA, stateN));

        result.stateGridPositions.put(state0, new MyPoint(0, 0));
        result.stateGridPositions.put(stateN, new MyPoint(lastX + 2, 0));

        return result;
    }

    public static boolean isAValidRegex(String regex) {
        if (regex.isEmpty()) {
            return false;
        }
        int contPar = 0;
        for (int i = 0; i < regex.length(); i++) {
            if (regex.charAt(i) == '(') {
                contPar++;
            } else if (regex.charAt(i) == ')') {
                contPar--;
            }
        }
        return contPar == 0;
    }

    public static final char KLENNE_CHAR = '*';
    public static final char UNION_CHAR0 = '|';
    public static final char UNION_CHAR1 = '/';
    public static final char PARENTHESES_OPEN = '(';
    public static final char PARENTHESES_CLOSE = ')';

    //Fecho de kleene, concatenação, união
    @Nullable
    public static FiniteStateAutomatonGUI getAutomatonByRegex(String regex) {
        if (!isAValidRegex(regex)) {
            return null;
        }
        Deque<Character> operators = new ArrayDeque<>();
        Deque<FiniteStateAutomatonGUI> automatons = new ArrayDeque<>();
        for (int i = 0; i < regex.length(); i++) {
            char symbol = regex.charAt(i);
            if (symbol == PARENTHESES_OPEN) {
                operators.push(PARENTHESES_CLOSE);
            } else if (symbol == PARENTHESES_CLOSE) {
                // enquanto operador de união
                while (operators.pop() != PARENTHESES_CLOSE) {
                    FiniteStateAutomatonGUI automatonA = automatons.pop();
                    FiniteStateAutomatonGUI automatonB = automatons.pop();
                    FiniteStateAutomatonGUI newAutomaton = automatonB.or(automatonA);
//                    System.out.println("--OR--INIT");
//                    System.out.println(automatonA);
//                    System.out.println("--OR--");
//                    System.out.println(automatonB);
//                    System.out.println("RESULT");
//                    System.out.println(newAutomaton);
                    automatons.push(newAutomaton);
                }
                if (automatons.size() >= 2
                        && automatons.size() > operators.size() + 1) {
                    FiniteStateAutomatonGUI automatonA = automatons.pop();
                    FiniteStateAutomatonGUI automatonB = automatons.pop();
                    FiniteStateAutomatonGUI newAutomaton = automatonB.concat(automatonA);
//                    System.out.println("--CONCAT--INIT");
//                    System.out.println(automatonA);
//                    System.out.println("--CONCAT--");
//                    System.out.println(automatonB);
//                    System.out.println("RESULT");
//                    System.out.println(newAutomaton);
                    automatons.push(newAutomaton);
                }
                if (i + 1 < regex.length()
                        && regex.charAt(i + 1) == KLENNE_CHAR) {
                    FiniteStateAutomatonGUI automatonA = automatons.pop();
                    FiniteStateAutomatonGUI newAutomaton = automatonA.kleene();
//                    System.out.println("--KLEENE--");
//                    System.out.println(automatonA);
//                    System.out.println("RESULT");
//                    System.out.println(newAutomaton);
                    automatons.push(newAutomaton);
                    i++;
                }
            } else if (symbol == UNION_CHAR0) {
                operators.push(UNION_CHAR0);
            } else {
                FiniteStateAutomatonGUI newAutomaton = newAutomaton(Character.toString(symbol));
//                System.out.println("--CREATE--");
//                System.out.println(newAutomaton);
                if (i + 1 < regex.length()
                        && regex.charAt(i + 1) == KLENNE_CHAR) {
                    FiniteStateAutomatonGUI automatonA = newAutomaton;
                    newAutomaton = newAutomaton.kleene();
//                    System.out.println("--KLEENE--");
//                    System.out.println(automatonA);
//                    System.out.println("RESULT");
//                    System.out.println(newAutomaton);
                    i++;
                }
//                System.out.println("OPERADORS");
//                System.out.println(operators);
                if (automatons.size() >= 2
                        && automatons.size() > operators.size() + 1) {
                    FiniteStateAutomatonGUI automatonA = newAutomaton;
                    FiniteStateAutomatonGUI automatonB = automatons.pop();
                    newAutomaton = automatonB.concat(automatonA);
//                    System.out.println("--CONCAT--INIT");
//                    System.out.println(automatonA);
//                    System.out.println("--CONCAT--");
//                    System.out.println(automatonB);
//                    System.out.println("RESULT");
//                    System.out.println(newAutomaton);
                    newAutomaton = automatons.pop().concat(newAutomaton);
                }
                automatons.push(newAutomaton);
            }
        }
        while (!operators.isEmpty()) {
            FiniteStateAutomatonGUI automatonA = automatons.pop();
            FiniteStateAutomatonGUI automatonB = automatons.pop();
            FiniteStateAutomatonGUI newAutomaton = automatonB.or(automatonA);
//            System.out.println("--OR--INIT");
//            System.out.println(automatonA);
//            System.out.println("--OR--");
//            System.out.println(automatonB);
//            System.out.println("RESULT");
//            System.out.println(newAutomaton);
            automatons.push(newAutomaton);
            operators.pop();
        }
        if (automatons.size() == 2) {
            FiniteStateAutomatonGUI automatonA = automatons.pop();
            FiniteStateAutomatonGUI automatonB = automatons.pop();
            FiniteStateAutomatonGUI newAutomaton = automatonB.concat(automatonA);
//            System.out.println("--CONCAT--INIT");
//            System.out.println(automatonA);
//            System.out.println("--CONCAT--");
//            System.out.println(automatonB);
//            System.out.println("RESULT");
//            System.out.println(newAutomaton);
            automatons.push(newAutomaton);
        }
        FiniteStateAutomatonGUI automatonGUIRegex = automatons.pop();
        automatonGUIRegex.deslocPointsRegex();
        return automatonGUIRegex;
    }

    private void deslocPointsRegex() {
        int deslocY = 2 - getSmallestY();
        for (SortedMap.Entry<State, MyPoint> stateStringEntry : stateGridPositions.entrySet()) {
            MyPoint point = stateStringEntry.getValue();
            point.x += 2;
            point.y += deslocY;
        }
    }

    public FiniteStateAutomatonGUI(FiniteStateAutomaton finiteStateAutomaton, SortedMap<State, Point> stateGridPositions, long id,
                                   String label, int contUses) {
        super(finiteStateAutomaton);
        this.stateGridPositions = (stateGridPositions == null) ? new TreeMap<State, MyPoint>()
                : FiniteStateAutomatonGUI.convertToMyPoint(stateGridPositions);
        this.id = id;
        this.label = label;
        this.contUses = contUses;
        this.creationDate = new Date();
    }

    public FiniteStateAutomatonGUI(FiniteStateAutomaton finiteStateAutomaton,
                                   SortedMap<State, Point> stateGridPositions, Long id,
                                   String label, Integer contUses, Date creationDate) {
        super(finiteStateAutomaton);
        this.stateGridPositions = (stateGridPositions == null) ? new TreeMap<State, MyPoint>()
                : FiniteStateAutomatonGUI.convertToMyPoint(stateGridPositions);
        this.id = id;
        this.label = label;
        this.contUses = contUses;
        this.creationDate = (creationDate == null) ? new Date() : creationDate;
    }

    public FiniteStateAutomatonGUI(FiniteStateAutomaton finiteStateAutomaton,
                                   Map<State, MyPoint> stateGridPositions, Long id,
                                   String label, Integer contUses, Date creationDate) {
        super(finiteStateAutomaton);
        this.stateGridPositions = new TreeMap<>(stateGridPositions);
        this.id = id;
        this.label = label;
        this.contUses = contUses;
        this.creationDate = (creationDate == null) ? new Date() : creationDate;
    }

    private void initStateGridPositions(Map<State, MyPoint> stateGridPositions) {
        this.stateGridPositions = new TreeMap<>();
        if (stateGridPositions == null) {
            return;
        }
        for (Map.Entry<State, MyPoint> entry : stateGridPositions.entrySet()) {
            if (entry.getKey() == null) {
                String point = "null";
                if (entry.getValue() != null) {
                    point = entry.getValue().toString();
                }
                continue;
            }
            this.stateGridPositions.put(getState(entry.getKey().getName()),
                    new MyPoint(entry.getValue()));
        }
    }

    public FiniteStateAutomatonGUI(FiniteStateAutomatonGUI automatonGUI) {
        super(automatonGUI);
        initStateGridPositions(automatonGUI.stateGridPositions);
        creationDate = new Date();
        id = -1L;
    }

    public Point getGridPositionPoint(State state) {
        return stateGridPositions.get(state).toPoint();
    }

    public MyPoint getGridPosition(State state) {
        return stateGridPositions.get(state);
    }

    public SortedMap<State, Point> getStateGridPoint() {
        return FiniteStateAutomatonGUI.convertToPoint(stateGridPositions);
    }

    public Map<State, MyPoint> getStateGridPositions() {
        return stateGridPositions;
    }



}
