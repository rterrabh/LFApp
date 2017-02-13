package com.ufla.lfapp.activities.automata.graph.layout;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ufla.lfapp.activities.automata.ItemAutomatonActivity;

/**
 * Created by carlos on 2/8/17.
 */

public class GestureListenerForNonEditAutomaton extends
        GestureDetector.SimpleOnGestureListener {

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
        intent.putExtra("AutomatonGUI", editGraphLayout.getAutomatonGUI());
        context.startActivity(intent);
        return true;
    }
}