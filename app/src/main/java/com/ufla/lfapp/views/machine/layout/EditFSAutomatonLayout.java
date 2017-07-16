package com.ufla.lfapp.views.machine.layout;

import android.content.Context;
import android.util.AttributeSet;

import com.ufla.lfapp.views.graph.EdgeView;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.views.machine.transition.FSATransitionView;

/**
 * Created by carlos on 3/3/17.
 */

public class EditFSAutomatonLayout extends EditGraphLayout {

    public EditFSAutomatonLayout(Context context, float sizeReference) {
        super(context, sizeReference);
    }

    public EditFSAutomatonLayout(Context context) {
        super(context);
    }

    public EditFSAutomatonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditFSAutomatonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public EdgeView newEdgeView(boolean toast) {
        return new FSATransitionView(getContext(), toast);
    }
}
