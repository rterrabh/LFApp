package com.ufla.lfapp.activities.grammar.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.activities.grammar.algorithms.AmbiguityVerificationActivity;
import com.ufla.lfapp.activities.grammar.algorithms.cfgtopda.CFGToPDAStage1Activity;
import com.ufla.lfapp.activities.grammar.algorithms.CYKActivity;
import com.ufla.lfapp.activities.grammar.algorithms.ChainRulesActivity;
import com.ufla.lfapp.activities.grammar.algorithms.DerivationMoreLeftActivity;
import com.ufla.lfapp.activities.grammar.algorithms.EmptyProductionActivity;
import com.ufla.lfapp.activities.grammar.algorithms.IdGrammarActivity;
import com.ufla.lfapp.activities.grammar.algorithms.NoReachSymbolsActivity;
import com.ufla.lfapp.activities.grammar.algorithms.NoTermSymbolsActivity;
import com.ufla.lfapp.activities.grammar.algorithms.RemoveInitialSymbolRecursiveActivity;
import com.ufla.lfapp.activities.grammar.algorithms.RemoveLeftDirectRecursionActivity;
import com.ufla.lfapp.utils.Algorithm;
import com.ufla.lfapp.core.grammar.GrammarParser;

/**
 * Created by root on 18/07/16.
 */
public class MenuActivity extends HeaderGrammarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_menu);
        super.onCreate(savedInstanceState);
        if (!GrammarParser.contextFreeGrammar(getGrammar(), new StringBuilder()) &&
                !GrammarParser.regularGrammar(getGrammar(), new StringBuilder())) {
            findViewById(R.id.FNCButton).setVisibility(View.GONE);
            findViewById(R.id.RemoveRecursionDirectLeftButton).setVisibility(View.GONE);
            findViewById(R.id.RemoveRecursionLeftButton).setVisibility(View.GONE);
            findViewById(R.id.FNGButton).setVisibility(View.GONE);
            findViewById(R.id.CykButton).setVisibility(View.GONE);
        }

       setOnClickListenersButtons();
    }

    private void setOnClickListenersButtons() {
        setOnClickListenerGeneric(R.id.idGrammarButton, IdGrammarActivity.class);
        setOnClickListenerGeneric(R.id.derivationMostLeftButton, DerivationMoreLeftActivity.class);
        setOnClickListenerGeneric(R.id.RemoveRecursiveInitialSymbolButton,
                RemoveInitialSymbolRecursiveActivity.class);
        setOnClickListenerGeneric(R.id.RemoveEmptyProductionsButton, EmptyProductionActivity.class);
        setOnClickListenerGeneric(R.id.RemoveChainRulesButton, ChainRulesActivity.class);
        setOnClickListenerGeneric(R.id.TermButton, NoTermSymbolsActivity.class);
        setOnClickListenerGeneric(R.id.ReachButton, NoReachSymbolsActivity.class);
        setOnClickListenerGeneric(R.id.FNCButton, ChomskyNormalFormMenuActivity.class);
        setOnClickListenerGeneric(R.id.RemoveRecursionDirectLeftButton,
                RemoveLeftDirectRecursionActivity.class);
        setOnClickListenerGeneric(R.id.RemoveRecursionLeftButton,
                RemoveLeftRecursionMenuActivity.class);
        setOnClickListenerGeneric(R.id.FNGButton, GreibachNormalFormMenuActivity.class);
        setOnClickListenerGeneric(R.id.CykButton, CYKActivity.class);
        setOnClickListenerGeneric(R.id.ambiguityVerification, AmbiguityVerificationActivity.class);
        setOnClickListenerGeneric(R.id.cfgto_pda, CFGToPDAStage1Activity.class);
    }


    private void setOnClickListenerGeneric(int idView, final Class clazz) {
        findViewById(idView).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString(GRAMMAR_EXTRA, grammar);
                params.putString(WORD_EXTRA, word);
                params.putInt(ALGORITHM_EXTRA, Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, clazz);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }

}
