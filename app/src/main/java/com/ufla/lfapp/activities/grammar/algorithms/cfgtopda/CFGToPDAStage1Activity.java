package com.ufla.lfapp.activities.grammar.algorithms.cfgtopda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;

public class CFGToPDAStage1Activity extends AppCompatActivityContext {

    private static final String GRAMMAR_EXTRA = "grammar";
    private static final String WORD_EXTRA = "word";

    private String grammarStr;
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfgto_pdastage1);
        readIntent();
        setView();
    }

    public void setView() {
        Grammar grammar = new Grammar(grammarStr);
        TextView tvInputGrammar = (TextView) findViewById(R.id.inputGrammar);
        tvInputGrammar.setText(grammar.toHtmlFormated());
        Grammar grammarGreibach = grammar.FNGTerra(grammar, new AcademicSupport());
        TextView tvGrammarFNG = (TextView) findViewById(R.id.grammarFNG);
        tvGrammarFNG.setText(getResources().getString(R.string.grammar_in_gnf)
                + grammarGreibach.toHtmlFormated());
        TextView tvDescr = (TextView) findViewById(R.id.descr);
        tvDescr.setText(Html.fromHtml(getResources().getString(R.string.cfgToPDAStage1Descr)));
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

    public void next(View view) {
        Bundle params = new Bundle();
        params.putString(GRAMMAR_EXTRA, grammarStr);
        params.putString(WORD_EXTRA, word);
        Intent intent = new Intent(getBaseContext(), CFGToPDAStage2Activity.class);
        intent.putExtras(params);
        startActivity(intent);
        finish();
    }

}
