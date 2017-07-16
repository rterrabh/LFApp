package com.ufla.lfapp.activities.grammar.algorithms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ufla.lfapp.R;

import java.util.List;

/**
 * Created by carlos on 3/1/17.
 */

public class ArrayAdapterWord extends ArrayAdapter<String> {

    private static final String GRAMMAR_EXTRA = "grammar";
    private static final String WORD_EXTRA = "word";

    private AmbiguityVerificationActivity activity;


    public ArrayAdapterWord(Context context, List<String> words) {
        super(context, R.layout.word_item_view, words);
        activity = (AmbiguityVerificationActivity) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String singleWordItem = getItem(position);
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View view = buckysInflater.inflate(R.layout.word_item_view, parent,
                false);

        ((TextView) view.findViewById(R.id.word)).setText(singleWordItem);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString(GRAMMAR_EXTRA, activity.getGrammarString());
                params.putString(WORD_EXTRA, singleWordItem);
                Intent intent = new Intent(activity, DerivationMoreLeftActivity.class);
                intent.putExtras(params);
                activity.startActivity(intent);
            }
        });
        return view;
    }

}
