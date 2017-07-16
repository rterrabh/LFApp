package com.ufla.lfapp.activities.machine.fsa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.views.graph.layout.NonEditGraphLayout;

import java.util.List;

public class ProcessWordFSAActivity extends AppCompatActivityContext {

    private static final String FINITE_STATE_AUTOMATON_GUI_EXTRA = "FiniteStateAutomatonGUI";
    private static final String WORD_EXTRA = "word";

    private FiniteStateAutomatonGUI automatonGUI;
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_word_fsa);
        readIntent();
        setView();
    }

    private void setView() {
        NonEditGraphLayout nonEditGraphLayout = new NonEditGraphLayout(this);
        nonEditGraphLayout.drawAutomaton(automatonGUI);
        nonEditGraphLayout.removeSpaces();
        ( (ViewGroup) nonEditGraphLayout.getRootView()).removeAllViews();
        ((ViewGroup) nonEditGraphLayout.getParent()).setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        ConstraintLayout rootAutomataLayout = (ConstraintLayout) findViewById(R.id.fsa);
        rootAutomataLayout.addView((View)nonEditGraphLayout.getParent());
        TextView tvGrammarAct = (TextView) findViewById(R.id.word);
        tvGrammarAct.setText(word);
        TextView tvResult = (TextView) findViewById(R.id.result);
        try {
            if (automatonGUI.processEntry(word)) {
                tvResult.setText(R.string.accept);
            } else {
                tvResult.setText(R.string.reject);
            }
        } catch (Exception e) {
            tvResult.setText(e.getMessage());
            e.printStackTrace();
        }

        LinearLayout lvNewTransitions = (LinearLayout) findViewById(R.id.configurations);
        List<SpannableString> spannables = automatonGUI.configurationsSpan;
        List<String> newTransitionsStr = automatonGUI.configurations;
        for (SpannableString str : spannables) {
            TextView textView = new TextView(this);
            textView.setTextColor(Color.BLACK);
            textView.setText(str);
            lvNewTransitions.addView(textView);
        }
//        for (String conf : newTransitionsStr) {
//            TextView textView = new TextView(this);
//            textView.setTextColor(Color.BLACK);
//            textView.setText(conf);
//            lvNewTransitions.addView(textView);
//        }
    }

    public void readIntent() {
        Intent intent = getIntent();
        if (intent != null
                && intent.getExtras() != null
                && intent.getExtras().getSerializable(FINITE_STATE_AUTOMATON_GUI_EXTRA) != null
                && intent.getExtras().getSerializable(WORD_EXTRA) != null) {

            Bundle dados = intent.getExtras();
            automatonGUI = (FiniteStateAutomatonGUI) dados.getSerializable(FINITE_STATE_AUTOMATON_GUI_EXTRA);
            word = (String) dados.getSerializable(WORD_EXTRA);
        }
    }
}
