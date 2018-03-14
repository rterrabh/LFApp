package com.ufla.lfapp.views.machine.layout;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.pda.PDATransitionFunction;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.core.machine.pda.PushdownAutomatonExtend;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.views.graph.EdgeView;
import com.ufla.lfapp.views.graph.VertexView;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.views.machine.transition.PDATransitionView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 3/3/17.
 */

public class EditPDAutomatonLayout extends EditGraphLayout {

    public EditPDAutomatonLayout(Context context, float sizeReference) {
        super(context, sizeReference);
    }

    public EditPDAutomatonLayout(Context context) {
        super(context);
    }

    public EditPDAutomatonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditPDAutomatonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public EdgeView newEdgeView(boolean toast) {
        return new PDATransitionView(getContext(), toast);
    }

    public PushdownAutomaton getCompletePushdownAutomaton() {
        if (initialState == null) {
            Toast.makeText(getContext(), R.string.exception_not_find_initial_state,
                    Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        PushdownAutomaton pushdownAutomaton = getPushdownAutomaton();
        if (pushdownAutomaton.getFinalStates().isEmpty()) {
            Toast.makeText(getContext(), R.string.exception_not_find_final_state, Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        return pushdownAutomaton;
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

    public PushdownAutomaton getPushdownAutomaton() {
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
        Set<PDATransitionFunction> transitionFunctions = new TreeSet<>();
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
        PushdownAutomaton pda = new PushdownAutomaton(states, initState, finalStates,
                transitionFunctions);
        for (EdgeView edgeView : edgeViews) {
            transitionFunctions.addAll(((PDATransitionView) edgeView).getTransitionFuctionsPDA(pda));
        }

        return pda;
    }


    public void drawPDAWithLabel(PushdownAutomaton pda, Map<String, MyPoint> labelToPoint) {
        Map<State, MyPoint> stateToPoint = new HashMap<>();
        for (State state : pda.getStates()) {
            stateToPoint.put(state, labelToPoint.get(state.getName()));
        }
        //UtilActivities.logMap(stateToPoint);
        drawPDA(pda, stateToPoint);
    }

    public void drawPDA(PushdownAutomaton pda, Map<State, MyPoint> stateToPoint) {
        for (State state : pda.getStates()) {
            addVertexView(stateToPoint.get(state).toPoint(), state.getName());
        }
        if (stateToPoint.containsKey(pda.getInitialState())) {
            setVertexViewAsInitial(stateToPoint.get(pda.getInitialState()).toPoint());
        }
        for (State state : pda.getFinalStates()) {
            setVertexViewAsFinal(stateToPoint.get(state).toPoint());
        }
        for (Map.Entry<Pair<State, State>, SortedSet<String>> entry :
                pda.getTransitionsPDA().entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String str : entry.getValue()) {
                sb.append(str)
                        .append(EdgeView.LABEL_ITEM_SEP);
            }
            sb.deleteCharAt(sb.length() - 1);
            addEdgeView(stateToPoint.get(entry.getKey().first).toPoint(),
                    stateToPoint.get(entry.getKey().second).toPoint(),
                    sb.toString());
        }
        naiveVerifyReflexives();
        invalidate();
    }

    public void drawAutomatonExtendWithLabelMyPoint(PushdownAutomatonExtend pushdownAutomatonExtend,
                                             Map<String, MyPoint> labelToPoint) {
        Map<State, Point> stateToPoint = new HashMap<>();
        for (State state : pushdownAutomatonExtend.getStates()) {
            stateToPoint.put(state, labelToPoint.get(state.getName()).toPoint());
        }
        drawAutomatonExtend(pushdownAutomatonExtend, stateToPoint);
    }

    public void drawAutomatonExtendWithLabel(PushdownAutomatonExtend pushdownAutomatonExtend,
                                    Map<String, Point> labelToPoint) {
        Map<State, Point> stateToPoint = new HashMap<>();
        for (State state : pushdownAutomatonExtend.getStates()) {
            stateToPoint.put(state, labelToPoint.get(state.getName()));
        }
        drawAutomatonExtend(pushdownAutomatonExtend, stateToPoint);
    }

    public void drawAutomatonExtend(PushdownAutomatonExtend pushdownAutomatonExtend,
                                    Map<State, Point> stateToPoint) {
        for (State state : pushdownAutomatonExtend.getStates()) {
            addVertexView(stateToPoint.get(state), state.getName());
        }
        if (stateToPoint.containsKey(pushdownAutomatonExtend.getInitialState())) {
            setVertexViewAsInitial(stateToPoint.get(pushdownAutomatonExtend.getInitialState()));
        }
        for (State state : pushdownAutomatonExtend.getFinalStates()) {
            setVertexViewAsFinal(stateToPoint.get(state));
        }
        for (Map.Entry<Pair<State, State>, SortedSet<String>> entry :
                pushdownAutomatonExtend.getTransitionsPDA().entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String str : entry.getValue()) {
                sb.append(str)
                        .append(EdgeView.LABEL_ITEM_SEP);
            }
            sb.deleteCharAt(sb.length() - 1);
            addEdgeView(stateToPoint.get(entry.getKey().first),
                    stateToPoint.get(entry.getKey().second),
                    sb.toString());
        }
        naiveVerifyReflexives();
        invalidate();

    }



    public void drawPushdownAutomaton(PushdownAutomatonExtend  pushdownAutomaton) {
        Map<State, Point> stateGridPositions = new HashMap<>();
        for (State state : pushdownAutomaton.getStates()) {
            if (pushdownAutomaton.isInitialState(state)) {
                Point point = new Point(1, 2);
                stateGridPositions.put(state, point);
                addVertexView(point, state.getName());
                setVertexViewAsInitial(point);

            } else if (pushdownAutomaton.isFinalState(state) ){
                Point point = new Point(5, 2);
                stateGridPositions.put(state, point);
                addVertexView(point, state.getName());
                setVertexViewAsFinal(point);
            }
        }
        for (Map.Entry<Pair<State, State>, SortedSet<String>> entry :
                pushdownAutomaton.getTransitionsPDA().entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String str : entry.getValue()) {
                sb.append(str)
                        .append(EdgeView.LABEL_ITEM_SEP);
            }
            sb.deleteCharAt(sb.length() - 1);
            addEdgeView(stateGridPositions.get(entry.getKey().first),
                    stateGridPositions.get(entry.getKey().second),
                    sb.toString());
        }
        naiveVerifyReflexives();
        invalidate();
    }


}
