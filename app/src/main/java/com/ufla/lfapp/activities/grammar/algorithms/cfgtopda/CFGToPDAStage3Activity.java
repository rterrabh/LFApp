package com.ufla.lfapp.activities.grammar.algorithms.cfgtopda;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.views.machine.layout.NonEditPDAutomatonLayout;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;


public class CFGToPDAStage3Activity extends AppCompatActivityContext {

    private static final String GRAMMAR_EXTRA = "grammar";
    private static final String WORD_EXTRA = "word";

    private String grammarStr;
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfgto_pdastage3);
        readIntent();
        setView();
    }

    public void setView() {
        Grammar grammar = new Grammar(grammarStr);
        Grammar grammarGreibach = grammar.FNGTerra(grammar, new AcademicSupport());
        TextView tvInputGrammar = (TextView) findViewById(R.id.inputGrammar);
        tvInputGrammar.setText(grammarGreibach.toHtmlFormated());
        TextView tvDescr = (TextView) findViewById(R.id.descr);
        tvDescr.setText(Html.fromHtml(getResources().getString(R.string.cfgToPDAStage3Descr)));
        CFGToPDAStage3Model model = new CFGToPDAStage3Model(grammarGreibach);
        TextView tvTransitions = (TextView) findViewById(R.id.transitions);
        tvTransitions.setText(getResources().getString(R.string.new_transitions_2) + model.rulesTransitions());
        NonEditPDAutomatonLayout nonEditPDAutomatonLayout = new NonEditPDAutomatonLayout(this);
        nonEditPDAutomatonLayout.drawPushdownAutomaton(model.getPushdownAutomatonExtended());
        nonEditPDAutomatonLayout.removeSpaces();
        nonEditPDAutomatonLayout.getRootView().setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        ConstraintLayout rootAutomataLayout = (ConstraintLayout) findViewById(R.id.pda);
        rootAutomataLayout.addView(nonEditPDAutomatonLayout.getRootView());
    }

    public void readIntent() {
        Intent intent = getIntent();
        if (intent != null
                && intent.getExtras() != null
                && intent.getExtras().getString(GRAMMAR_EXTRA) != null) {
            Bundle dados = intent.getExtras();
            grammarStr = dados.getString(GRAMMAR_EXTRA);
            word = dados.getString(WORD_EXTRA);
        }
    }

    public void back(View view) {
        Bundle params = new Bundle();
        params.putString(GRAMMAR_EXTRA, grammarStr);
        params.putString(WORD_EXTRA, word);
        Intent intent = new Intent(getBaseContext(), CFGToPDAStage2Activity.class);
        intent.putExtras(params);
        startActivity(intent);
        finish();
    }

    public void next(View view) {
        Bundle params = new Bundle();
        params.putString(GRAMMAR_EXTRA, grammarStr);
        params.putString(WORD_EXTRA, word);
        Intent intent = new Intent(getBaseContext(), CFGToPDAStage4Activity.class);
        intent.putExtras(params);
        startActivity(intent);
        finish();
    }


}
