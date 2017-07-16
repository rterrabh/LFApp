package com.ufla.lfapp.views.machine.layout;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.AttributeSet;

import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTapeTransitionFunction;
import com.ufla.lfapp.core.machine.tm.var.TuringMachineMultiTape;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.utils.UtilActivities;
import com.ufla.lfapp.views.graph.EdgeView;
import com.ufla.lfapp.views.graph.VertexView;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.views.machine.transition.TMMultiTapeTransitionView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 4/9/17.
 */

public class EditTMMultiTapeLayout extends EditGraphLayout {

    private int numTapes;

    public int getNumTapes() {
        return numTapes;
    }

    public void setNumTapes(int numTapes) {
        this.numTapes = numTapes;
    }

    public EditTMMultiTapeLayout(Context context, float sizeReference) {
        super(context, sizeReference);
    }

    public EditTMMultiTapeLayout(Context context) {
        super(context);
    }

    public EditTMMultiTapeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTMMultiTapeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Map<String, MyPoint> getMapLabelToPoint() {
        Map<String, MyPoint> labelTopoint = new HashMap<>();
        for (int i = 0; i < viewsOnGrid.length; i++) {
            for (int j = 0; j < viewsOnGrid[i].length; j++) {
                if (viewsOnGrid[i][j] instanceof VertexView) {
                    VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                    labelTopoint.put(vertexView.getLabel(), new MyPoint(j, i));
                }
            }
        }
        return labelTopoint;
    }

    public TuringMachineMultiTape getTuringMachine() {
        State initState = (initialState == null) ? null :
                new State(initialState.getLabel());
        SortedSet<State> states = new TreeSet<>();
        SortedSet<State> finalStates = new TreeSet<>();
        if (initState != null) {
            states.add(initState);
            if (initialState.isFinalState()) {
                finalStates.add(initState);
            }
        }
        Set<TMMultiTapeTransitionFunction> transitionFunctions = new TreeSet<>();
        for (int i = 0; i < viewsOnGrid.length; i++) {
            for (int j = 0; j < viewsOnGrid[i].length; j++) {
                if (viewsOnGrid[i][j] instanceof VertexView) {
                    VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                    if (vertexView.isInitialState()) {
                        continue;
                    }
                    State label = new State(vertexView.getLabel());
                    states.add(label);
                    if (vertexView.isFinalState()) {
                        finalStates.add(label);
                    }
                }
            }
        }
        TuringMachineMultiTape turingMachine =
                new TuringMachineMultiTape(states, initState, finalStates,
                transitionFunctions, getNumTapes());
        for (EdgeView edgeView : edgeViews) {
            transitionFunctions.addAll(((TMMultiTapeTransitionView) edgeView)
                    .getTransitionFuctionsTM(turingMachine));
        }

        return turingMachine;
    }

    public void drawTMWithLabel(TuringMachineMultiTape tm, Map<String, MyPoint> labelToPoint) {
        Map<State, MyPoint> stateToPoint = new HashMap<>();
        for (State state : tm.getStates()) {
            stateToPoint.put(state, labelToPoint.get(state.getName()));
        }
        UtilActivities.logMap(stateToPoint);
        drawTM(tm, stateToPoint);
    }

    public void drawTM(TuringMachineMultiTape tm, Map<State, MyPoint> stateToPoint) {
        for (State state : tm.getStates()) {
            addVertexView(stateToPoint.get(state).toPoint(), state.getName());
        }
        if (stateToPoint.containsKey(tm.getInitialState())) {
            setVertexViewAsInitial(stateToPoint.get(tm.getInitialState()).toPoint());
        }
        for (State state : tm.getFinalStates()) {
            setVertexViewAsFinal(stateToPoint.get(state).toPoint());
        }
        for (Map.Entry<Pair<State, State>, SortedSet<String>> entry :
                tm.getTransitionsTMMultiTape().entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String str : entry.getValue()) {
                sb.append(str)
                        .append('\n');
            }
            sb.deleteCharAt(sb.length() - 1);
            addEdgeView(stateToPoint.get(entry.getKey().first).toPoint(),
                    stateToPoint.get(entry.getKey().second).toPoint(),
                    sb.toString());
        }
        naiveVerifyReflexives();
        invalidate();
    }


    @Override
    public EdgeView newEdgeView(boolean toast) {
        return new TMMultiTapeTransitionView(getContext(), toast);
    }

}
