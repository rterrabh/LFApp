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
import com.ufla.lfapp.activities.machine.pda.EditPushdownAutomatonActivity;
import com.ufla.lfapp.views.machine.layout.NonEditPDAutomatonLayout;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;

public class CFGToPDAStage4Activity extends AppCompatActivityContext {

    private static final String GRAMMAR_EXTRA = "grammar";
    private static final String WORD_EXTRA = "word";

    private String grammarStr;
    private String word;
    private CFGToPDAStage4Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfgto_pdastage4);
        readIntent();
        setView();
    }

    public void setView() {
        Grammar grammar = new Grammar(grammarStr);
        Grammar grammarGreibach = grammar.FNGTerra(grammar, new AcademicSupport());
        TextView tvInputGrammar = (TextView) findViewById(R.id.inputGrammar);
        tvInputGrammar.setText(grammarGreibach.toHtmlFormated());
        TextView tvDescr = (TextView) findViewById(R.id.descr);
        tvDescr.setText(Html.fromHtml(getResources().getString(R.string.cfgToPDAStage4Descr)));
        model = new CFGToPDAStage4Model(grammarGreibach);
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
        Intent intent = new Intent(getBaseContext(), CFGToPDAStage3Activity.class);
        intent.putExtras(params);
        startActivity(intent);
        finish();
    }

    public void copyPDA(View view) {
        Intent intent = new Intent(this, EditPushdownAutomatonActivity.class);
        intent.putExtra(EditPushdownAutomatonActivity.PUSHDOW_AUTOMATON_EXTEND,
                model.getPushdownAutomatonExtended());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
