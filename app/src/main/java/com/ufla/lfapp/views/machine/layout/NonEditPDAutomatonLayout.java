package com.ufla.lfapp.views.machine.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by carlos on 3/3/17.
 */

public class NonEditPDAutomatonLayout extends EditPDAutomatonLayout {

    public NonEditPDAutomatonLayout(Context context, float sizeReference) {
        super(context, sizeReference);
    }

    public NonEditPDAutomatonLayout(Context context) {
        super(context);
    }

    public NonEditPDAutomatonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonEditPDAutomatonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return true;
    }

}
