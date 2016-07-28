package com.ufla.lfapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.utils.Algorithm;

/**
 * Created by root on 18/07/16.
 */
public class MenuActivity extends HeaderGrammarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_menu);
        super.onCreate(savedInstanceState);
    }


    public void startIdentificationGrammarActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.NONE.getValue());
        Intent intent = new Intent(this, IdGrammarActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startDerivationLeftmostActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.NONE.getValue());
        Intent intent = new Intent(this, DerivationMoreLeftActivity.class);
        intent.putExtras(params);
        startActivity(intent);

    }

    public void startRRecursionInTheInitialSymbolActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.NONE.getValue());
        Intent intent = new Intent(this, RemoveInitialSymbolRecursiveActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startREmptyProductionsActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.NONE.getValue());
        Intent intent = new Intent(this, EmptyProductionActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRChainRulesActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.NONE.getValue());
        Intent intent = new Intent(this, ChainRulesActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRNTermActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.NONE.getValue());
        Intent intent = new Intent(this, NoTermSymbolsActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRNReachActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.NONE.getValue());
        Intent intent = new Intent(this, NoReachSymbolsActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startChomskyNormalFormActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.NONE.getValue());
        Intent intent = new Intent(this, ChomskyNormalFormMenuActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRRecursionDirectLeftActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.NONE.getValue());
        Intent intent = new Intent(this, RemoveLeftDirectRecursionActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRRecursionDirAndIndLeftActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.NONE.getValue());
        Intent intent = new Intent(this, RemoveLeftRecursionActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startGreibachNormalFormActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.NONE.getValue());
        Intent intent = new Intent(this, GreibachNormalFormMenuActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startCYKActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        params.putString("word", word);
        params.putInt("algorithm", Algorithm.NONE.getValue());
        Intent intent = new Intent(this, CYKActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }





}
