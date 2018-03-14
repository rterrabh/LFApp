package com.ufla.lfapp.views.machine.layout;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ufla.lfapp.activities.machine.fsa.ItemAutomatonActivity;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;

/**
 * Created by carlos on 2/8/17.
 */

public class GestureListenerForNonEditAutomaton extends
        GestureDetector.SimpleOnGestureListener {

    private static final String FSA_EXTRA = "FiniteStateAutomatonGUI";
    private EditGraphLayout editGraphLayout;
    private Context context;

    public GestureListenerForNonEditAutomaton(Context context, EditGraphLayout editGraphLayout) {
        this.context = context;
        this.editGraphLayout = editGraphLayout;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Intent intent = new Intent(context, ItemAutomatonActivity.class );
        intent.putExtra(FSA_EXTRA, editGraphLayout.getAutomatonGUI());
        context.startActivity(intent);
        return true;
    }
}