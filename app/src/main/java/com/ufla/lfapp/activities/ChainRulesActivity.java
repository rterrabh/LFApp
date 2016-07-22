package com.ufla.lfapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ufla.lfapp.R;
import com.ufla.lfapp.vo.AcademicSupport;
import com.ufla.lfapp.vo.Grammar;

/**
 * Created by root on 22/07/16.
 */
public class ChainRulesActivity extends HeaderGrammarActivity {


    @Override
    protected Grammar getGrammar() {
        switch(algorithm) {
            case CHOMSKY_NORMAL_FORM:
            case GREIBACH_NORMAL_FORM:
                Grammar g = new Grammar(grammar);
                g = g.getGrammarWithInitialSymbolNotRecursive(g, new
                        AcademicSupport());
                return g.getGrammarEssentiallyNoncontracting(g, new
                        AcademicSupport());
            default: return super.getGrammar();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.next:
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", algorithm.getValue());
                Intent intent = new Intent(this, EmptyProductionActivity.class);
                intent.putExtras(params);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chain_rules);
        super.onCreate(savedInstanceState);
    }

}
