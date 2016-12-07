package com.ufla.lfapp.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.vo.AutomatonGUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by carlos on 12/7/16.
 */

public class ArrayAdapterAutomata extends ArrayAdapter<AutomatonGUI> {

    private static String CLEAN_BUTTON = "clean";

    public ArrayAdapterAutomata(Context context, List<AutomatonGUI> automatonGUIs) {
        super(context, R.layout.automaton_item_view, automatonGUIs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AutomatonGUI singleAutomatonItem = getItem(position);
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View view = buckysInflater.inflate(R.layout.automaton_item_view, parent,
                false);
        ((TextView) view.findViewById(R.id.id)).setText(Long.toString(singleAutomatonItem.getId()));
        ((TextView) view.findViewById(R.id.creationDate)).setText("Criado em: " +
                singleAutomatonItem.getCreationDate().toString());
        ((TextView) view.findViewById(R.id.label)).setText("Máquina: " +
                singleAutomatonItem.getLabel());
        ((TextView) view.findViewById(R.id.initialState)).setText("Conjunto de estados: " +
                singleAutomatonItem.getStates());
        ((TextView) view.findViewById(R.id.finalStates)).setText("Alfabeto: " +
                singleAutomatonItem.getAlphabet());
        ((TextView) view.findViewById(R.id.initialState)).setText("Estado inicial: " +
                singleAutomatonItem.getInitialState());
        ((TextView) view.findViewById(R.id.initialState)).setText("Conjunto dos estados finais: " +
                singleAutomatonItem.getFinalStates());
        TableLayout transictionTable = (TableLayout) view.findViewById(R.id.transictionTable);
        List<String> states = new ArrayList<>(singleAutomatonItem.getStates());
        Collections.sort(states);
        TableRow tr = new TableRow(getContext());
        tr.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        tr.addView(new TextView(getContext()));
        for (String state : states) {
            TextView textView = new TextView(getContext());
            textView.setText(state);
            tr.addView(textView);
        }
        transictionTable.addView(tr);
        for (int i = 0; i < states.size(); i++) {
            tr = new TableRow(getContext());
            tr.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            TextView textView = new TextView(getContext());
            textView.setText(states.get(i));
            tr.addView(textView);
            for (int j = 0; j < states.size(); j++) {
                textView = new TextView(getContext());
                textView.setText(singleAutomatonItem.getSymbols(states.get(i), states.get(j))
                        .toString());
                tr.addView(textView);
            }
            transictionTable.addView(tr);
        }

        return view;
    }

}
