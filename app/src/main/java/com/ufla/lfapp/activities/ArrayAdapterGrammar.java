package com.ufla.lfapp.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ufla.lfapp.R;

import java.util.List;

/**
 * Created by root on 03/08/16.
 */
public class ArrayAdapterGrammar extends ArrayAdapter<String> {

    private static String CLEAN_BUTTON = "clean";

    public ArrayAdapterGrammar(Context context, List<String> grammars) {
        super(context, R.layout.grammar_item_view, grammars);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String singleGrammarItem = getItem(position);
        if(singleGrammarItem.equals(CLEAN_BUTTON)) {
            LayoutInflater buckysInflater = LayoutInflater.from(getContext());
            return buckysInflater.inflate(R.layout.clean_historical_button, parent,
                    false);
        }
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View arrayAdapterGrammar = buckysInflater.inflate(R.layout.grammar_item_view, parent,
                false);

        ((TextView) arrayAdapterGrammar.findViewById(R.id.grammar)).setText(singleGrammarItem);
        ((TextView) arrayAdapterGrammar.findViewById(R.id.id)).setText
                (String.valueOf(HistoricalGrammarsActivity
                .getGrammarId(singleGrammarItem)));
        return arrayAdapterGrammar;
    }

}
