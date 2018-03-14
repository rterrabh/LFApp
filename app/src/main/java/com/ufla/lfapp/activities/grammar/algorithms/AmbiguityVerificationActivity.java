package com.ufla.lfapp.activities.grammar.algorithms;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.core.grammar.parser.AmbiguityVerification;
import com.ufla.lfapp.core.grammar.parser.TreeDerivation;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationParser;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationPosition;
import com.ufla.lfapp.utils.MyConsumer;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.views.graph.layout.GestureListenerForNonEditGraphTree;
import com.ufla.lfapp.views.graph.layout.NonEditGraphLayout;

import java.util.ArrayList;
import java.util.List;

public class AmbiguityVerificationActivity extends HeaderGrammarActivity {

    //    private int wordsTest = 100;
//    private List<String> wordsAmbiguity;
//    private ArrayAdapter listAdapterWord;
    private TreeDerivationParser treeDerivationParserAct;
    private EditGraphLayout derivationTree1;
    private EditGraphLayout derivationTree2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ambuiguity_verification);
        super.onCreate(savedInstanceState);
//        wordsAmbiguity = new ArrayList<>();
//        listAdapterWord = new ArrayAdapterWord(this, wordsAmbiguity);
//        ((ListView) findViewById(R.id.words)).setAdapter(listAdapterWord);
        derivationTree1 = new NonEditGraphLayout(this);
        derivationTree2 = new NonEditGraphLayout(this);
        verifyAmbiguity();
    }


    class CallbackRunnableAmbiguity implements Runnable {

        private TreeDerivationParser treeDerivationParser;

        public CallbackRunnableAmbiguity(TreeDerivationParser treeDerivationParser) {
            this.treeDerivationParser = treeDerivationParser;
        }

        @Override
        public void run() {
            Button cancelSearch = (Button) findViewById(R.id.cancel_search);
            cancelSearch.setVisibility(View.GONE);
            TextView tvAmbiguity = (TextView) findViewById(R.id.isAmbiguity);
            if (treeDerivationParser.isAmbiguity()) {
                TreeDerivation tree1 = treeDerivationParser.getTreeDerivation();
                TreeDerivation tree2 = treeDerivationParser.getTreeDerivationAux();
                tvAmbiguity.setText(String.format("%s%s'.", getResources().getString(R.string.find_ambiguity), treeDerivationParser.getWord()));
                //TextView tvWord = (TextView) findViewById(R.id.word);
                TextView tvDerivations = (TextView) findViewById(R.id.derivations);
                //tvWord.setText(treeDerivationParser.getWord());
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.two_derivations_1_simple))
                        .append(tree1.getDerivation())
                        .append(getString(R.string.two_derivations_2))
                        .append(tree2.getDerivation());
                tvDerivations.setText(sb.toString());
                TreeDerivationPosition treePos1 = new TreeDerivationPosition(tree1.getRoot());
                derivationTree1.drawDerivationTree(treePos1);
                derivationTree1.getRootView().setLayoutParams(
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                final GestureDetector gestureDetector1 = new GestureDetector(AmbiguityVerificationActivity.this,
                        new GestureListenerForNonEditGraphTree(AmbiguityVerificationActivity.this, derivationTree1, treePos1));
                derivationTree1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector1.onTouchEvent(event);
                    }
                });
                TreeDerivationPosition treePos2 = new TreeDerivationPosition(tree2.getRoot());
                derivationTree2.drawDerivationTree(treePos2);
                derivationTree2.getRootView().setLayoutParams(
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                final GestureDetector gestureDetector2 = new GestureDetector(AmbiguityVerificationActivity.this,
                        new GestureListenerForNonEditGraphTree(AmbiguityVerificationActivity.this, derivationTree1, treePos2));
                derivationTree2.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector2.onTouchEvent(event);
                    }
                });
                ConstraintLayout clDerivationTree2 = (ConstraintLayout)
                        findViewById(R.id.derivationTree2);
                clDerivationTree2.addView(derivationTree2.getRootView());

                ConstraintLayout clDerivationTree1 = (ConstraintLayout)
                        findViewById(R.id.derivationTree1);
                clDerivationTree1.addView(derivationTree1.getRootView());

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
                                derivationTree1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                derivationTree1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            Display display = getWindowManager().getDefaultDisplay();
                            Point size = new Point();
                            display.getSize(size);
                            size.y -= getRelativeTop(derivationTree1);
                            int height = derivationTree1.getHeight();
                            if (derivationTree2 != null) {
                                size.x /= 2;
                                derivationTree2
                                        .resizeTo(size.x, size.y);
                            }
                            derivationTree1
                                    .resizeTo(size.x, size.y);
                        }
                    });
                }
            } else {
                tvAmbiguity.setText(getResources().getString(R.string.not_find_ambiguity));
            }

        }
    }


    public void cancelSearch(View view) {
        treeDerivationParserAct.cancelAmbiguitySearch();
    }

    public void verifyAmbiguity() {
        treeDerivationParserAct = new TreeDerivationParser(getGrammar());
        treeDerivationParserAct.checkAmbiguity(new MyConsumer<TreeDerivationParser>() {
            @Override
            public void accept(final TreeDerivationParser treeDerivationParser) {
                runOnUiThread(new CallbackRunnableAmbiguity(treeDerivationParser));


            }
        }, this);
    }

//    public void verifyAmbiguityOld() {
//        AmbiguityVerification ambiguityVerification =
//                new AmbiguityVerification(getGrammar(),
//                        wordsTest);
//        List<String> wordsAmbiguityAux = ambiguityVerification.getWordsAmbiguity();
//        TextView tvAmbiguity = (TextView) findViewById(R.id.isAmbiguity);
//        String text;
//        if (wordsAmbiguityAux.isEmpty()) {
//            text = getResources().getString(R.string.not_find_ambiguity);
//        } else {
//            text = getResources().getString(R.string.find_ambiguity);
//        }
//        tvAmbiguity.setText(text);
////        listAdapterWord.addAll(wordsAmbiguityAux);
////        listAdapterWord.notifyDataSetChanged();
//    }
}
