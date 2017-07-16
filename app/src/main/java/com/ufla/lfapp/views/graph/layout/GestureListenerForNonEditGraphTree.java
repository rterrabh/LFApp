package com.ufla.lfapp.views.graph.layout;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ufla.lfapp.activities.machine.ItemDerivationTreeActivity;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationPosition;

/**
 * Created by carlos on 2/22/17.
 */

public class GestureListenerForNonEditGraphTree extends
        GestureDetector.SimpleOnGestureListener {

    private static final String DERIVATION_TREE_EXTRA = "DerivationTree";
    private EditGraphLayout editGraphLayout;
    private TreeDerivationPosition tree;
    private Context context;

    public GestureListenerForNonEditGraphTree(Context context, EditGraphLayout editGraphLayout,
                                              TreeDerivationPosition tree) {
        this.context = context;
        this.editGraphLayout = editGraphLayout;
        this.tree = tree;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Intent intent = new Intent(context, ItemDerivationTreeActivity.class );
        intent.putExtra(DERIVATION_TREE_EXTRA, tree);
        context.startActivity(intent);
        return true;
    }
}
