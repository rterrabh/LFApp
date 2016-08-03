package com.ufla.lfapp.activities;

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
import com.ufla.lfapp.activities.utils.Algorithm;
import com.ufla.lfapp.vo.AcademicSupport;
import com.ufla.lfapp.vo.Grammar;

/**
 * Created by root on 20/07/16.
 */
public abstract class HeaderGrammarActivity extends AppCompatActivity {


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
        if(algorithm != Algorithm.NONE) {
            RelativeLayout backAndNextButtons = (RelativeLayout)
                    findViewById(R.id.backAndNextButtons);
            backAndNextButtons.setVisibility(View.VISIBLE);
        }
    }

    protected Grammar getGrammar() {
        return new Grammar(grammar);
    }

    private void setGrammar() {
        Intent intent = getIntent();
        if (intent != null
                && intent.getExtras() != null
                && intent.getExtras().getString("grammar") != null) {
            Bundle dados = intent.getExtras();
            grammar = dados.getString("grammar");
            word = dados.getString("word");
            algorithm = Algorithm.getAlgorithm(dados.getInt("algorithm"));
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void changeActivity(Context context, Class<?> cls) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", algorithm.getValue());
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
