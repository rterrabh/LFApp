package com.ufla.lfapp.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.algorithms.ChainRulesActivity;
import com.ufla.lfapp.activities.algorithms.ChomskyNormalFormActivity;
import com.ufla.lfapp.activities.algorithms.EmptyProductionActivity;
import com.ufla.lfapp.activities.HeaderGrammarActivity;
import com.ufla.lfapp.activities.algorithms.NoReachSymbolsActivity;
import com.ufla.lfapp.activities.algorithms.NoTermSymbolsActivity;
import com.ufla.lfapp.activities.algorithms.RemoveInitialSymbolRecursiveActivity;
import com.ufla.lfapp.activities.utils.Algorithm;

/**
 * Created by root on 25/07/16.
 */
public class ChomskyNormalFormMenuActivity extends HeaderGrammarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chomsky_normal_form_menu);
        super.onCreate(savedInstanceState);
    }

    public void startRRecursionInTheInitialSymbolActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.CHOMSKY_NORMAL_FORM.getValue());
        Intent intent = new Intent(this, RemoveInitialSymbolRecursiveActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startREmptyProductionsActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.CHOMSKY_NORMAL_FORM.getValue());
        Intent intent = new Intent(this, EmptyProductionActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRChainRulesActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.CHOMSKY_NORMAL_FORM.getValue());
        Intent intent = new Intent(this, ChainRulesActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRNTermActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.CHOMSKY_NORMAL_FORM.getValue());
        Intent intent = new Intent(this, NoTermSymbolsActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRNReachActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.CHOMSKY_NORMAL_FORM.getValue());
        Intent intent = new Intent(this, NoReachSymbolsActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startChomskyNormalFormActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.CHOMSKY_NORMAL_FORM.getValue());
        Intent intent = new Intent(this, ChomskyNormalFormActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

}
