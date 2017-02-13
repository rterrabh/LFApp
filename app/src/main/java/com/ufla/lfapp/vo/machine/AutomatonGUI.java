package com.ufla.lfapp.vo.machine;

import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.Log;

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


public class AutomatonGUI extends Automaton implements Serializable {


    private Long id;
    private String label;
    private Date creationDate;
    private int contUses = 1;
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

    private AutomatonGUI() {
        super();
        this.stateGridPositions = new TreeMap<>();
        creationDate = new Date();
        id = -1L;
    }

    public AutomatonGUI(Automaton automaton, SortedMap<State, Point> stateGridPositions) {
        super(automaton);
        this.stateGridPositions = (stateGridPositions == null) ? new TreeMap<State, MyPoint>()
                : AutomatonGUI.convertToMyPoint(stateGridPositions);
        this.creationDate = new Date();
        id = -1L;
    }

    private void addTransitionsWithDeslocState(Set<TransitionFunction> transitionFunctions,
                                              int desloc) {
        for (TransitionFunction transitionFunction : transitionFunctions) {
            int currentStateNumber = Integer.parseInt(transitionFunction.getCurrentState()
                    .getName().substring(1));
            int futureStateNumber = Integer.parseInt(transitionFunction.getFutureState()
                    .getName().substring(1));
            this.transitionFunctions.add(new TransitionFunction(
                    getState("q" + (desloc + currentStateNumber)),
                    transitionFunction.getSymbol(),
                    getState("q" + (desloc + futureStateNumber))));
        }
    }

    private void addStatesWithDesloc(int contStates, int desloc) {
        for (int i = 0; i < contStates; i++) {
            states.add(new State("q" + (desloc + i)));
        }
    }

    private void displayStateGridPositions() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<State, MyPoint> myPointEntry : stateGridPositions.entrySet()) {
            stringBuilder.append("{" + myPointEntry.getKey().toString() + " -> " +
                myPointEntry.getValue().toString() + "}");
        }
    }

    private AutomatonGUI kleene() {
        Log.d("kleene", "kleene");
        AutomatonGUI automaton = new AutomatonGUI(this);

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

        automaton.transitionFunctions.add(new TransitionFunction(state0, LAMBDA, state1));
        automaton.transitionFunctions.add(new TransitionFunction(state0, LAMBDA, stateN));
        automaton.transitionFunctions.add(new TransitionFunction(stateN_1, LAMBDA, stateN));
        automaton.transitionFunctions.add(new TransitionFunction(stateN, LAMBDA, state1));

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

    private AutomatonGUI concat(AutomatonGUI automaton) {
        Log.d("concat", "concat");
        AutomatonGUI result = new AutomatonGUI(this);

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

    private static AutomatonGUI newAutomaton(String symbol) {
        Log.d("new", "new -> " + symbol);
        AutomatonGUI automaton = new AutomatonGUI();
        State state0 = new State("q" + 0);
        State stateN = new State("q" + 1);

        automaton.states.add(state0);
        automaton.states.add(stateN);
        automaton.initialState = state0;
        automaton.finalStates.add(stateN);

        automaton.stateGridPositions.put(state0, new MyPoint(0, 0));
        automaton.stateGridPositions.put(stateN, new MyPoint(2, 0));

        automaton.transitionFunctions.add(new TransitionFunction(state0, symbol, stateN));

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

    public AutomatonGUI or(AutomatonGUI automaton) {
        Log.d("or", "or");
        AutomatonGUI result = new AutomatonGUI(this);

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

        result.transitionFunctions.add(new TransitionFunction(state0, LAMBDA, stateA1));
        result.transitionFunctions.add(new TransitionFunction(state0, LAMBDA, stateB1));
        result.transitionFunctions.add(new TransitionFunction(stateAN, LAMBDA, stateN));
        result.transitionFunctions.add(new TransitionFunction(stateBN, LAMBDA, stateN));

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

    //Fecho de kleene, concatenação, união
    @Nullable
    public static AutomatonGUI getAutomatonByRegex(String regex) {
        if (!isAValidRegex(regex)) {
            return null;
        }
        Deque<Character> operators = new ArrayDeque<>();
        Deque<AutomatonGUI> automatons = new ArrayDeque<>();
        int parent = 0;
        for (int i = 0; i < regex.length(); i++) {
            char symbol = regex.charAt(i);
            if (symbol != '(' && symbol != ')' && symbol != '*' && symbol != '|'
                    && symbol != '/') {
                AutomatonGUI newAutomaton = newAutomaton(Character.toString(symbol));
                if (i + 1 < regex.length() && regex.charAt(i + 1) == '*') {
                    newAutomaton = newAutomaton.kleene();
                    i++;
                }
                if (automatons.size() > 2 && automatons.size() > operators.size() + 1) {
                    newAutomaton = automatons.pop().concat(newAutomaton);
                }
                automatons.push(newAutomaton);
            } else if (symbol == '(') {
                operators.push('(');
                parent++;
            } else if (symbol == ')') {
                while (operators.pop() != '(') {
                    AutomatonGUI newAutomaton = automatons.pop();
                    newAutomaton = automatons.pop().or(newAutomaton);
                    automatons.push(newAutomaton);
                }
                parent--;
                if (automatons.size() > 2 && automatons.size() > operators.size() + 1) {
                    AutomatonGUI newAutomaton = automatons.pop();
                    newAutomaton = automatons.pop().concat(newAutomaton);
                    automatons.push(newAutomaton);
                }
                if (i + 1 < regex.length() && regex.charAt(i + 1) == '*') {
                    AutomatonGUI newAutomaton = automatons.pop().kleene();
                    automatons.push(newAutomaton);
                    i++;
                }
            } else {
                operators.push('|');
            }
        }
        while (!operators.isEmpty()) {
            AutomatonGUI newAutomaton = automatons.pop();
            newAutomaton = automatons.pop().or(newAutomaton);
            automatons.push(newAutomaton);
            operators.pop();
        }
        if (automatons.size() == 2) {
            AutomatonGUI newAutomaton = automatons.pop();
            newAutomaton = automatons.pop().concat(newAutomaton);
            automatons.push(newAutomaton);
        }
        AutomatonGUI automatonGUIRegex = automatons.pop();
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

    public AutomatonGUI(Automaton automaton, SortedMap<State, Point> stateGridPositions, long id,
                        String label, int contUses) {
        super(automaton);
        this.stateGridPositions = (stateGridPositions == null) ? new TreeMap<State, MyPoint>()
                : AutomatonGUI.convertToMyPoint(stateGridPositions);
        this.id = id;
        this.label = label;
        this.contUses = contUses;
        this.creationDate = new Date();
    }

    public AutomatonGUI(Automaton automaton, SortedMap<State, Point> stateGridPositions, long id,
                        String label, int contUses, Date creationDate) {
        super(automaton);
        this.stateGridPositions = (stateGridPositions == null) ? new TreeMap<State, MyPoint>()
                : AutomatonGUI.convertToMyPoint(stateGridPositions);
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

    public AutomatonGUI(AutomatonGUI automatonGUI) {
        super(automatonGUI);
        initStateGridPositions(automatonGUI.stateGridPositions);
        creationDate = new Date();
        id = -1L;
    }

    public Point getGridPosition(State state) {
        return stateGridPositions.get(state).toPoint();
    }

    public SortedMap<State, Point> getStateGridPositions() {
        return AutomatonGUI.convertToPoint(stateGridPositions);
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public int getContUses() {
        return contUses;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContUses(int contUses) {
        this.contUses = contUses;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
