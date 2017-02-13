package com.ufla.lfapp.activities.automata.graph.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

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

}
