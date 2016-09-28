package com.ufla.lfapp.activities.automata;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;

import com.ufla.lfapp.vo.TransitionFunction;

/**
 * Created by carlos on 9/21/16.
 */
public class TransitionViewB extends View {

    private float stateRadius;
    private TransitionFunction transitionFunc;

    public TransitionViewB(Context context, TransitionFunction transitionFunc) {
        super(context);
        this.transitionFunc = transitionFunc;
    }

    private RectF getRectF(float x0, float y0, float x1, float y1) {
        float d = (float) Math.sqrt(stateRadius*stateRadius / 2);
        if (x0 > x1) {
            if (y0 > y1) {
                return new RectF(x1+d, y1+d, x0-d, y0-d);
            } else {
                return new RectF(x1+d, y0+d, x0-d, y1-d);
            }
        } else {
            if (y0 > y1) {
                return new RectF(x0+d, y1+d, x1-d, y0-d);
            } else {
                return new RectF(x0+d, y0+d, x1-d, y1-d);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        State currentState = mapNameToState.get(transitionFunc.getCurrentState());
//        State futureState = mapNameToState.get(transitionFunc.getFutureState());
//        Path path = new Path();
//        path.addArc(getRectF(currentState.x, currentState.y, futureState.x, futureState.y), 180.0f, 180.0f);
//        canvas.drawTextOnPath(transitionFunc.getSymbol(), path, 0.0f, 0.2f, mTextPaint);
//        canvas.drawPath(path, mTransitionPaint);
    }
}
