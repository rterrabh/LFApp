package com.ufla.lfapp.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.algorithms.CYKActivity;
import com.ufla.lfapp.activities.algorithms.ChainRulesActivity;
import com.ufla.lfapp.activities.algorithms.DerivationMoreLeftActivity;
import com.ufla.lfapp.activities.algorithms.EmptyProductionActivity;
import com.ufla.lfapp.activities.HeaderGrammarActivity;
import com.ufla.lfapp.activities.algorithms.IdGrammarActivity;
import com.ufla.lfapp.activities.algorithms.NoReachSymbolsActivity;
import com.ufla.lfapp.activities.algorithms.NoTermSymbolsActivity;
import com.ufla.lfapp.activities.algorithms.RemoveInitialSymbolRecursiveActivity;
import com.ufla.lfapp.activities.algorithms.RemoveLeftDirectRecursionActivity;
import com.ufla.lfapp.activities.algorithms.RemoveLeftRecursionActivity;
import com.ufla.lfapp.activities.utils.Algorithm;
import com.ufla.lfapp.vo.AcademicSupport;
import com.ufla.lfapp.vo.AcademicSupportForRemoveLeftRecursion;
import com.ufla.lfapp.vo.GrammarParser;

/**
 * Created by root on 18/07/16.
 */
public class MenuActivity extends HeaderGrammarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_menu);
        super.onCreate(savedInstanceState);
        if (!GrammarParser.contextFreeGrammar(getGrammar(), new StringBuilder())) {
            findViewById(R.id.FNCButton).setVisibility(View.GONE);
            findViewById(R.id.RemoveRecursionDirectLeftButton).setVisibility(View.GONE);
            findViewById(R.id.RemoveRecursionLeftButton).setVisibility(View.GONE);
            findViewById(R.id.FNGButton).setVisibility(View.GONE);
            findViewById(R.id.CykButton).setVisibility(View.GONE);
        }

        findViewById(R.id.idGrammarButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, IdGrammarActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        findViewById(R.id.derivationMostLeftButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, DerivationMoreLeftActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        findViewById(R.id.RemoveRecursiveInitialSymbolButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, RemoveInitialSymbolRecursiveActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        findViewById(R.id.RemoveEmptyProductionsButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, EmptyProductionActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        findViewById(R.id.RemoveChainRulesButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, ChainRulesActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });


        findViewById(R.id.TermButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, NoTermSymbolsActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        findViewById(R.id.ReachButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, NoReachSymbolsActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        findViewById(R.id.FNCButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, ChomskyNormalFormMenuActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        findViewById(R.id.RemoveRecursionDirectLeftButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, RemoveInitialSymbolRecursiveActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        findViewById(R.id.RemoveRecursionLeftButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, RemoveLeftRecursionActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        findViewById(R.id.FNGButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, GreibachNormalFormMenuActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        findViewById(R.id.CykButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(MenuActivity.this, CYKActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
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

//    public void startRNTermActivity(View view) {
//        Bundle params = new Bundle();
//        params.putString("grammar", grammar);
//        params.putString("word", word);
//        params.putInt("algorithm", Algorithm.NONE.getValue());
//        Intent intent = new Intent(this, NoTermSymbolsActivity.class);
//        intent.putExtras(params);
//        startActivity(intent);
//    }

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

//    public void startRRecursionDirAndIndLeftActivity(View view) {
//        Bundle params = new Bundle();
//        params.putString("grammar", grammar);
//        params.putString("word", word);
//        params.putInt("algorithm", Algorithm.NONE.getValue());
//        Intent intent = new Intent(this, RemoveLeftRecursionActivity.class);
//        intent.putExtras(params);
//        startActivity(intent);
//    }

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
