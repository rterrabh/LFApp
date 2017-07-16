package com.ufla.lfapp.activities.grammar.algorithms;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.grammar.HeaderGrammarActivity;
import com.ufla.lfapp.core.grammar.parser.AmbiguityVerification;

import java.util.ArrayList;
import java.util.List;

public class AmbiguityVerificationActivity extends HeaderGrammarActivity {

    private int wordsTest = 10000;
    private List<String> wordsAmbiguity;
    private ArrayAdapter listAdapterWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ambuiguity_verification);
        super.onCreate(savedInstanceState);
        wordsAmbiguity = new ArrayList<>();
        listAdapterWord = new ArrayAdapterWord(this, wordsAmbiguity);
        ((ListView) findViewById(R.id.words)).setAdapter(listAdapterWord);
        verifyAmbiguity();
        //new Thread(new Verification()).start();

    }

    class Verification implements Runnable {

        @Override
        public void run() {
            verifyAmbiguity();
        }

    }

    public void verifyAmbiguity() {
        AmbiguityVerification ambiguityVerification =
                new AmbiguityVerification(getGrammar(),
                        wordsTest);
        List<String> wordsAmbiguityAux = ambiguityVerification.getWordsAmbiguity();
        TextView tvAmbiguity = (TextView) findViewById(R.id.isAmbiguity);
        String text;
        if (wordsAmbiguityAux.isEmpty()) {
            text = getResources().getString(R.string.not_find_ambiguity);
        } else {
            text = getResources().getString(R.string.find_ambiguity);
        }
        tvAmbiguity.setText(text);
        listAdapterWord.addAll(wordsAmbiguityAux);
        listAdapterWord.notifyDataSetChanged();
    }
}
