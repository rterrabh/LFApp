package com.ufla.lfapp.views.graph;

/**
 * Created by carlos on 3/3/17.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.Symbols;

import java.util.List;


/**
 * Created by carlos on 3/1/17.
 */

public class ArrayAdapterLabel extends ArrayAdapter<StringBuilder> {


    public ArrayAdapterLabel(Context context, List<StringBuilder> words) {
        super(context, R.layout.word_item_view, words);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final StringBuilder singleWordItem = getItem(position);
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View view = buckysInflater.inflate(R.layout.word_item_view, parent,
                false);

        ((TextView) view.findViewById(R.id.word)).setText(singleWordItem.toString());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE );
                final LinearLayout dialogEdge = (LinearLayout) inflater.inflate(
                        R.layout.dialog_label_pda_transition, null);
                final EditText etSymbolPda = (EditText) dialogEdge.findViewById(
                        R.id.symbol_pda_transition);
                final EditText etPushPda = (EditText) dialogEdge.findViewById(
                        R.id.push_pda_transition);
                final EditText etPopPda = (EditText) dialogEdge.findViewById(
                        R.id.pop_pda_transition);
                String labelTokens[] = singleWordItem.toString().split("[ /]");
                etSymbolPda.setText(labelTokens[0]);
                etSymbolPda.setEnabled(true);
                etSymbolPda.setSelectAllOnFocus(true);
                etPopPda.setText(labelTokens[1]);
                etPopPda.setEnabled(true);
                etPopPda.setSelectAllOnFocus(true);
                etPushPda.setText(labelTokens[2]);
                etPushPda.setEnabled(true);
                etPushPda.setSelectAllOnFocus(true);
                final AlertDialog alertDialog = builder.setView(dialogEdge)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                singleWordItem.delete(0, singleWordItem.length());
                                String labelAux = etSymbolPda.getText().toString();
                                singleWordItem.append(Symbols.emptyIsLambda(labelAux))
                                    .append(' ');
                                labelAux = etPopPda.getText().toString();
                                singleWordItem.append(Symbols.emptyIsLambda(labelAux))
                                        .append('/');
                                labelAux = etPushPda.getText().toString();
                                singleWordItem.append(Symbols.emptyIsLambda(labelAux));
                                ArrayAdapterLabel.this.notifyDataSetChanged();
                                dialog.cancel();

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                //  Your code when user clicked on Cancel
                            }
                        })
                        .create();
                etSymbolPda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            alertDialog.getWindow().setSoftInputMode(WindowManager
                                    .LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }
                });
                alertDialog.show();
                int idButtonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).getId();
                etPushPda.setNextFocusDownId(idButtonPositive);
                etPushPda.setNextFocusForwardId(idButtonPositive);
            }
        });
        return view;
    }

}

