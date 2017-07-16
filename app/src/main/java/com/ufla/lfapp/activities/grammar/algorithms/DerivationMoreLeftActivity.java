package com.ufla.lfapp.activities.grammar.algorithms;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.views.graph.layout.GestureListenerForNonEditGraphTree;
import com.ufla.lfapp.views.graph.layout.NonEditGraphLayout;
import com.ufla.lfapp.views.graph.EdgeView;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.parser.TreeDerivation;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationParser;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationPosition;

/**
 * Created by root on 21/07/16.
 */
public class DerivationMoreLeftActivity extends HeaderGrammarActivity {

    private EditGraphLayout derivationTree1;
    private EditGraphLayout derivationTree2;

    @Override
    protected void onStart() {
        super.onStart();
        EdgeView.EMPTY_LABEL = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_derivation_more_left);
        super.onCreate(savedInstanceState);
        String word = getWord();
        Grammar grammar = getGrammar();
        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar, word);
        treeDerivationParser.parser();
        TreeDerivation tree1 = treeDerivationParser.getTreeDerivation();
        TreeDerivation tree2 = treeDerivationParser.getTreeDerivationAux();
        TextView tvWord = (TextView) findViewById(R.id.word);
        tvWord.setText(getResources().getString(R.string.word) + " " + word);
        TextView tvDerivations = (TextView) findViewById(R.id.derivations);
        if (tree1 == null) {
            tvDerivations.setText(R.string.no_derivation_for_the_word);
        } else {
            if (tree2 == null) {
                tvDerivations.setText(getString(R.string.derivation) + tree1.getDerivation());
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.two_derivations_1))
                        .append(tree1.getDerivation())
                        .append(getString(R.string.two_derivations_2))
                        .append(tree2.getDerivation());
                tvDerivations.setText(sb.toString());
            }
        }
        if (tree1 != null) {
            derivationTree1 = new NonEditGraphLayout(this);
            TreeDerivationPosition treePos1 = new TreeDerivationPosition(tree1.getRoot());
            derivationTree1.drawDerivationTree(treePos1);
            derivationTree1.getRootView().setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            final GestureDetector gestureDetector1 = new GestureDetector(this,
                    new GestureListenerForNonEditGraphTree(this, derivationTree1, treePos1));
            derivationTree1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector1.onTouchEvent(event);
                }
            });
            if (tree2 == null) {
                ConstraintLayout clDerivationTree = (ConstraintLayout)
                        findViewById(R.id.derivationTree);
                clDerivationTree.removeAllViews();
                clDerivationTree.addView(derivationTree1.getRootView());
            } else {
                TreeDerivationPosition treePos2 = new TreeDerivationPosition(tree2.getRoot());
                derivationTree2 = new NonEditGraphLayout(this);
                derivationTree2.drawDerivationTree(treePos2);
                derivationTree2.getRootView().setLayoutParams(
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                ConstraintLayout clDerivationTree1 = (ConstraintLayout)
                        findViewById(R.id.derivationTree1);
                clDerivationTree1.addView(derivationTree1.getRootView());
                ConstraintLayout clDerivationTree2 = (ConstraintLayout)
                        findViewById(R.id.derivationTree2);
                clDerivationTree2.addView(derivationTree2.getRootView());
                final GestureDetector gestureDetector2 = new GestureDetector(this,
                        new GestureListenerForNonEditGraphTree(this, derivationTree1, treePos2));
                derivationTree2.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector2.onTouchEvent(event);
                    }
                });
            }
            ViewTreeObserver viewTreeObserver = derivationTree1.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    private int getRelativeTop(View myView) {
                        if (myView.getParent() == myView.getRootView()) {
                            return myView.getTop();
                        }
                        return myView.getTop() + getRelativeTop((View) myView.getParent());
                    }

                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            DerivationMoreLeftActivity.this.derivationTree1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            DerivationMoreLeftActivity.this.derivationTree1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        size.y -= getRelativeTop(DerivationMoreLeftActivity.this.derivationTree1);
                        int height = DerivationMoreLeftActivity.this.derivationTree1.getHeight();
                        if (DerivationMoreLeftActivity.this.derivationTree2 != null) {
                            size.x /= 2;
                            DerivationMoreLeftActivity.this.derivationTree2
                                    .resizeTo(size.x, size.y);
                        }
                        DerivationMoreLeftActivity.this.derivationTree1
                                .resizeTo(size.x, size.y);
                    }
                });
            }
        }
    }

}
