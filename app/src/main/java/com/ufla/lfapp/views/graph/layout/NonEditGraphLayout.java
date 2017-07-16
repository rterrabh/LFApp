package com.ufla.lfapp.views.graph.layout;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ufla.lfapp.views.graph.EdgeView;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.tm.TuringMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

/**
 * Created by carlos on 1/23/17.
 */

public class NonEditGraphLayout extends EditGraphLayout {


    public NonEditGraphLayout(Context context, float sizeReference) {
        super(context, sizeReference);
    }

    public NonEditGraphLayout(Context context) {
        super(context);
    }

    public NonEditGraphLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonEditGraphLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return true;
    }

    public void drawTuringMachineWithLabel(TuringMachine turingMachine,
                                           Map<String, MyPoint> labelToPoint) {
        Map<State, MyPoint> stateToPoint = new HashMap<>();
        for (State state : turingMachine.getStates()) {
            stateToPoint.put(state, labelToPoint.get(state.getName()));
        }
        //UtilActivities.logMap(stateToPoint);
        drawTuringMachine(turingMachine, stateToPoint);


    }

    public void drawTuringMachine(TuringMachine turingMachine,
                                  Map<State, MyPoint> stateToPoint) {
        for (State state : turingMachine.getStates()) {
            addVertexView(stateToPoint.get(state).toPoint(), state.getName());
        }
        setVertexViewAsInitial(stateToPoint.get(turingMachine.getInitialState()).toPoint());
        for (State state : turingMachine.getFinalStates()) {
            setVertexViewAsFinal(stateToPoint.get(state).toPoint());
        }
        for (Map.Entry<Pair<State, State>, SortedSet<String>> entry :
                turingMachine.getTransitionsTM().entrySet()) {
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
}
