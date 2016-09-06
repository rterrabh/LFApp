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
        setContentView(R.layout.activity_menu_chomsky_normal_form);
        super.onCreate(savedInstanceState);setOnClickListenersButtons();
    }

    private void setOnClickListenersButtons() {
        setOnClickListenerGeneric(R.id.removeRecursiveInitialSymbol,
                RemoveInitialSymbolRecursiveActivity.class);
        setOnClickListenerGeneric(R.id.removeEmptyProduction, EmptyProductionActivity.class);
        setOnClickListenerGeneric(R.id.removeChainRule, ChainRulesActivity.class);
        setOnClickListenerGeneric(R.id.term, NoTermSymbolsActivity.class);
        setOnClickListenerGeneric(R.id.reach, NoReachSymbolsActivity.class);
        setOnClickListenerGeneric(R.id.chomskyNormalForm, ChomskyNormalFormActivity.class);
    }

    private void setOnClickListenerGeneric(int idView, final Class clazz) {
        findViewById(idView).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.CHOMSKY_NORMAL_FORM.getValue());
                Intent intent = new Intent(ChomskyNormalFormMenuActivity.this, clazz);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }

}
