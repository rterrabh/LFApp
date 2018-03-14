package com.ufla.lfapp.activities.grammar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.utils.Algorithm;
import com.ufla.lfapp.core.grammar.AcademicSupport;
import com.ufla.lfapp.core.grammar.Grammar;

/**
 * Created by carlos on 20/07/16.
 */
public abstract class HeaderGrammarActivity extends AppCompatActivityContext {


    protected static final String GRAMMAR_EXTRA = "grammar";
    protected static final String WORD_EXTRA = "word";
    protected static final String ALGORITHM_EXTRA = "algorithm";

    protected String grammar;
    protected String word;
    protected Algorithm algorithm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGrammar();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (algorithm != Algorithm.NONE) {
            RelativeLayout backAndNextButtons = (RelativeLayout)
                    findViewById(R.id.backAndNextButtons);
            backAndNextButtons.setVisibility(View.VISIBLE);
        }
    }

    public String getWord() {
        return word;
    }

    protected Grammar getGrammar() {
        return new Grammar(grammar);
    }

    public String getGrammarString() {
        return grammar;
    }

    private void setGrammar() {
        Intent intent = getIntent();
        if (intent != null
                && intent.getExtras() != null
                && intent.getExtras().getString(GRAMMAR_EXTRA) != null) {
            Bundle dados = intent.getExtras();
            grammar = dados.getString(GRAMMAR_EXTRA);
            word = dados.getString(WORD_EXTRA);
            algorithm = Algorithm.getAlgorithm(dados.getInt(ALGORITHM_EXTRA));
            Grammar g = getGrammar();
            if (grammar != null) {
                TextView inputGrammar = (TextView) findViewById(R.id.inputGrammar);
                AcademicSupport academic = new AcademicSupport();
                academic.setResult(g);
                assert inputGrammar != null;
                inputGrammar.setText(Html.fromHtml(academic.getResult()));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void changeActivity(Context context, Class<?> cls) {
        Bundle params = new Bundle();
        params.putString(GRAMMAR_EXTRA, grammar);
        params.putString(WORD_EXTRA, word);
        params.putInt(ALGORITHM_EXTRA, algorithm.getValue());
        Intent intent = new Intent(context, cls);
        intent.putExtras(params);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
