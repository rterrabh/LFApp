package com.ufla.lfapp.activities.automata;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ufla.lfapp.vo.TransitionFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufla.lfapp.R;

/**
 * Created by carlos on 9/20/16.
 */
public class AutomataView extends ViewGroup {

    private Paint mStateInternPaint;
    private Paint mStateLinePaint;
    private Paint mTransitionPaint;
    private Paint mTextPaint;
    private float stateRadius;
    private RectF mAutomataBounds = new RectF();
    private Map<String, State> mapNameToState;
    private List<State> states;
    private List<State> finalStates;
    private int countStates = 0;
    private State initialState;
    private List<TransitionFunction> transitions;
    //private List<StateView> stateViews;
    //private List<TransitionView> transitionViews;

    public AutomataView(Context context) {
        super(context);
        init();
        initDefault();
        //povoar();
    }

    public AutomataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AutomataView,
                0, 0
        );
        a.recycle();
        init();
        initDefault();
        //povoar();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }


    private void povoar() {
//        State s0 = new State("q0", 30, 50);
//        states.add(s0);
//        mapNameToState.put("q0", s0);
//        stateViews.add(new StateView(getContext()));
//
//        State s1 = new State("q1", 200, 120);
//        states.add(s1);
//        mapNameToState.put("q1", s1);
//        stateViews.add(new StateView(getContext()));
//
//        State s2 = new State("q2", 400, 200);
//        states.add(s2);
//        mapNameToState.put("q2", s2);
//        stateViews.add(new StateView(getContext()));
//
//        for (StateView stateView : stateViews) {
//            addView(stateView);
//        }

//        transitions.add(new TransitionFunction("q0", "a", "q1"));
//        transitions.add(new TransitionFunction("q1", "b", "q2"));

//        for (State state : states) {
//            StateView stateView = new StateView(getContext(), state);
//            addView(stateView);
//            stateViews.add(stateView);
//        }
//
//        for (TransitionFunction transition : transitions) {
//            TransitionView transitionView = new TransitionView(getContext(), transition);
//            addView(transitionView);
//            transitionViews.add(transitionView);
//        }
    }




    private void init() {
        mStateInternPaint = new Paint();
        mStateInternPaint.setStyle(Paint.Style.FILL);

        mStateLinePaint = new Paint();
        mStateLinePaint.setAntiAlias(true);
        mStateLinePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(2.0f);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mTransitionPaint = new Paint();
        mTransitionPaint.setAntiAlias(true);
        mTransitionPaint.setStyle(Paint.Style.STROKE);

        mapNameToState = new HashMap<>();
        states = new ArrayList<>();
        finalStates = new ArrayList<>();
        transitions = new ArrayList<>();
        //stateViews = new ArrayList<>();
        //transitionViews = new ArrayList<>();
    }

    private void initDefault() {
        mStateLinePaint.setColor(Color.BLACK);
        mStateLinePaint.setStrokeWidth(5.0f);

        mStateInternPaint.setColor(Color.TRANSPARENT);

        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(40.0f);

        mTransitionPaint.setColor(Color.BLACK);
        mTransitionPaint.setStrokeWidth(5.0f);

        stateRadius = 60.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(400,400, 350, mStateLinePaint);

    }

//    private class State {
//        private String name;
//        private float x;
//        private float y;
//
//        public State(String name, float x, float y) {
//            this.setName(name);
//            this.setX(x);
//            this.setY(y);
//        }
//
//        public State() {
//            super();
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public float getX() {
//            return x;
//        }
//
//        public void setX(float x) {
//            this.x = x;
//        }
//
//        public float getY() {
//            return y;
//        }
//
//        public void setY(float y) {
//            this.y = y;
//        }
//    }
//
//    private class StateView extends View {
//
//        private State state;
//
//        public StateView(Context context) {
//            super(context);
//        }
//
//        public StateView(Context context, State state) {
//            super(context);
//            this.state = state;
//        }
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//            canvas.drawCircle(state.getX(), state.getY(), stateRadius, mStateInternPaint);
//            canvas.drawText(state.getName(), state.getX(), state.getY(), mTextPaint);
//            canvas.drawCircle(state.getX(), state.getY(), stateRadius, mStateLinePaint);
//        }
//
//    }
//
//    private class TransitionView extends View {
//
//        private State state;
//
//        public TransitionView(Context context) {
//            super(context);
//        }
//
//        public TransitionView(Context context, State state) {
//            super(context);
//            this.state = state;
//        }
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//            canvas.drawCircle(state.getX(), state.getY(), stateRadius, mStateInternPaint);
//            canvas.drawText(state.getName(), state.getX(), state.getY(), mTextPaint);
//            canvas.drawCircle(state.getX(), state.getY(), stateRadius, mStateLinePaint);
//        }
//
//    }

}
