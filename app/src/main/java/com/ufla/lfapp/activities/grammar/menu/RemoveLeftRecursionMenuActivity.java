package com.ufla.lfapp.activities.grammar.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.activities.grammar.algorithms.ChainRulesActivity;
import com.ufla.lfapp.activities.grammar.algorithms.ChomskyNormalFormActivity;
import com.ufla.lfapp.activities.grammar.algorithms.EmptyProductionActivity;
import com.ufla.lfapp.activities.grammar.algorithms.NoReachSymbolsActivity;
import com.ufla.lfapp.activities.grammar.algorithms.NoTermSymbolsActivity;
import com.ufla.lfapp.activities.grammar.algorithms.RemoveInitialSymbolRecursiveActivity;
import com.ufla.lfapp.activities.grammar.algorithms.RemoveLeftRecursionActivity;
import com.ufla.lfapp.activities.utils.Algorithm;

/**
 * Created by carlos on 2/13/17.
 */

public class RemoveLeftRecursionMenuActivity extends HeaderGrammarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_menu_remove_left_recursion);
        super.onCreate(savedInstanceState);
        setOnClickListenersButtons();
    }

    private void setOnClickListenersButtons() {
        setOnClickListenerGeneric(R.id.removeRecursiveInitialSymbol,
                RemoveInitialSymbolRecursiveActivity.class);
        setOnClickListenerGeneric(R.id.removeEmptyProduction, EmptyProductionActivity.class);
        setOnClickListenerGeneric(R.id.removeChainRule, ChainRulesActivity.class);
        setOnClickListenerGeneric(R.id.term, NoTermSymbolsActivity.class);
        setOnClickListenerGeneric(R.id.reach, NoReachSymbolsActivity.class);
        setOnClickListenerGeneric(R.id.chomskyNormalForm, ChomskyNormalFormActivity.class);
        setOnClickListenerGeneric(R.id.removeRecursionLeft, RemoveLeftRecursionActivity.class);
    }

    private void setOnClickListenerGeneric(int idView, final Class clazz) {
        findViewById(idView).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.REMOVE_LEFT_RECURSION.getValue());
                Intent intent = new Intent(RemoveLeftRecursionMenuActivity.this, clazz);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }
}
