package com.ufla.lfapp.activities.machine.pda.pdatocfg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ufla.lfapp.R;

import java.util.List;

/**
 * Created by carlos on 3/24/17.
 */

public class ArrayAdapterTransition extends ArrayAdapter<String> {


    public ArrayAdapterTransition(Context context, List<String> transitions) {
        super(context, R.layout.item_new_transition_view, transitions);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        String transitionItem = getItem(position);
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View view = buckysInflater.inflate(R.layout.item_new_transition_view, parent,
        false);

        ((TextView) view.findViewById(R.id.newTransition)).setText(transitionItem);
        return view;
    }
}
