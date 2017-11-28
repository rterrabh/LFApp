package com.ufla.lfapp.views.machine.transition;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.pda.PDATransitionFunction;
import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.views.graph.ArrayAdapterLabel;
import com.ufla.lfapp.views.graph.EdgeView;
import com.ufla.lfapp.core.machine.Machine;
import com.ufla.lfapp.core.machine.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by carlos on 3/1/17.
 */

public class PDATransitionView extends EdgeView {

    private final static String INITIAL_LABEL = Symbols.LAMBDA + " " + Symbols.LAMBDA + "/" +
            Symbols.LAMBDA;

    @Override
    protected String[] getLabelLines() {
        String[] labelLines = TMMultiTrackTransitionView.arrayTrim(label.split("[,\n]"));
        if (labelLines.length == 0) {
            setInitialLabel();
            return  TMMultiTrackTransitionView.arrayTrim(label.split("[,\n]"));
        }
        return labelLines;
    }

    @Override
    public void setInitialLabel() {
        label = INITIAL_LABEL;
    }

    public PDATransitionView(Context context, boolean fastEdition) {
        super(context, false);
    }

    public PDATransitionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PDATransitionView(Context context) {
        super(context);
    }

    public boolean verifyTransition(String labelTrans) {
        String[] transitions = TMMultiTrackTransitionView.arrayTrim(labelTrans.split("[,\n]"));
        for (int i = 0; i < transitions.length; i++) {
            String[] transitonTokens = transitions[i].split("[ /]");
            if (transitonTokens.length != 3) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onLongPressAction(MotionEvent e) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        final LinearLayout dialogEdge = (LinearLayout) inflater.inflate(
                R.layout.dialog_multiple_labels_pda_transition, null);
        final EditText editLabel = (EditText) dialogEdge.findViewById(
                R.id.label);
        editLabel.setText(label);
        editLabel.setEnabled(true);
        editLabel.setSelectAllOnFocus(true);

        final AlertDialog alertDialog = builder.setView(dialogEdge)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String labelAux = editLabel.getText().toString().trim();
                        labelAux = labelAux.replaceAll("\\?", Symbols.LAMBDA);
                        if (!verifyTransition(labelAux)) {
                            Toast.makeText(PDATransitionView.this.getContext(),
                                    R.string.exception_transition_def,
                                    Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            setLabel(labelAux);
                        }
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
        alertDialog.show();
    }

    public static String[] clearArray(String[] strings) {
        List<String> clearArray = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            if (!strings[i].isEmpty()) {
                clearArray.add(strings[i]);
            }
        }
        return clearArray.toArray(new String[0]);
    }

    public Set<PDATransitionFunction> getTransitionFuctionsPDA(Machine machine) {
        State currentState = machine.getState(vertices.first.getLabel());
        State futureState = machine.getState(vertices.second.getLabel());
        Set<PDATransitionFunction> transitionFunctions = new HashSet<>();
        String[] transitions = PDATransitionView.clearArray
                (TMMultiTrackTransitionView.arrayTrim(label.split("[,\n]")));
        for (String transition : transitions) {
            String transitionTokens[] = transition.trim().split("[ /]");
            transitionFunctions.add(new PDATransitionFunction(currentState, transitionTokens[0],
                    futureState, transitionTokens[2], transitionTokens[1]));
        }
        return transitionFunctions;
    }

}
