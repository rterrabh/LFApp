package com.ufla.lfapp.views.machine.transition;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.Machine;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTrackTransitionFunction;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.views.graph.EdgeView;
import com.ufla.lfapp.views.machine.layout.EditTMMultiTrackLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by carlos on 4/9/17.
 */

public class TMMultiTrackTransitionView extends EdgeView {

    public int getNumTapes() {
        return ((EditTMMultiTrackLayout) getParent()).getNumTapes();
    }

    @Override
    protected String[] getLabelLines() {
        return getTransitionsMultiTrack(label);
    }

    @Override
    public void setInitialLabel() {
        int numTapes = getNumTapes();
        String emptySymbol = getContext().getString(R.string.empty_char_tape);
        String staticMove = TMMove.STATIC.toString();
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < numTapes; i++) {
            sb.append(emptySymbol)
                    .append('/')
                    .append(emptySymbol)
                    .append(' ');
        }
        sb.append(staticMove).append(']');
        label = sb.toString();
    }

    public TMMultiTrackTransitionView(Context context, boolean fastEdition) {
        super(context, false);
    }

    public TMMultiTrackTransitionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TMMultiTrackTransitionView(Context context) {
        super(context);
    }

    class IntegerWrapper {
        int value;
    }

    private String[] getTransitionsMultiTrack(String label) {
        return TMMultiTrackTransitionView.arrayTrim(label.split("[,\n]"));
    }

    private boolean verifyTransitionsMultiTrack(String label) {
        String[] transitions = getTransitionsMultiTrack(label);
        if (transitions == null) {
            return false;
        }
        final Set<String> directions = new HashSet<>(
                Arrays.asList(getDirections()));
        int numTapes = getNumTapes();
        for (int i = 0; i < transitions.length; i++) {
            transitions[i] = transitions[i].trim();
            String[] paramsTransition = paramsTransitionTMMTrack(transitions[i], numTapes);
            if (paramsTransition == null) {
                return false;
            }
            int numParams = paramsTransition.length;
            if (paramsTransition[0].charAt(0) != '['
                    || paramsTransition[numParams-1].charAt(
                    paramsTransition[numParams-1].length()-1) != ']'
                    || paramsTransition[numParams-1].length() != 2
                    || !directions.contains(paramsTransition[numParams-1].substring(0,1))) {
                return false;
            }
        }
        return true;
    }

    public static String[] getDirections() {
        String[] directions = new String[3];
        directions[0] = ResourcesContext.getString(R.string.direction_left);
        directions[1] = ResourcesContext.getString(R.string.direction_right);
        directions[2] = ResourcesContext.getString(R.string.direction_static);
        return directions;
    }



    private String[] paramsTransitionTMMTrack(String transition, int numTapes) {
        transition = transition.trim();
        String[] paramsAux = transition.split("[/ ]");
        paramsAux = arrayTrim(paramsAux);
        int contEmpty = 0;
        for (String str : paramsAux) {
            if (str.isEmpty()) {
                contEmpty++;
            }
        }
        if (paramsAux.length != numTapes * 2 + 1 + contEmpty) {
            return null;
        }
        if (contEmpty == 0) {
            return paramsAux;
        }
        String[] params = new String[numTapes * 2 + 1];
        contEmpty = 0;
        for (int i = 0; i < paramsAux.length; i++) {
            if (paramsAux[i].isEmpty()) {
                contEmpty++;
            } else {
                params[i-contEmpty] = paramsAux[i];
            }
        }
        return params;
    }

    @Override
    public void onLongPressAction(MotionEvent e) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        final LinearLayout dialogEdge = (LinearLayout) inflater.inflate(
                R.layout.dialog_label_tm_transition_text, null);
        final EditText editLabel = (EditText) dialogEdge.findViewById(
                R.id.label);
        editLabel.setText(label);
        editLabel.setEnabled(true);
        editLabel.setSelectAllOnFocus(true);
        final Set<String> directions = new HashSet<>(
                Arrays.asList(TMMultiTapeTransitionView.getDirections()));
        final AlertDialog alertDialog = builder.setView(dialogEdge)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String labelAux = editLabel.getText().toString();
                        if (!verifyTransitionsMultiTrack(labelAux)) {
                            Toast.makeText(TMMultiTrackTransitionView.this.getContext(),
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

    public static String[] arrayTrim(String[] array) {
        int n = array.length;
        String arrayTrim[] = new String[n];
        for (int i = 0; i < n; i++) {
            arrayTrim[i] = array[i].trim();
        }
        return arrayTrim;
    }

    private String[] getReadSymbols(String transition) {
        int begin = transition.indexOf('[', 0) + 1;
        int end = transition.indexOf(']', begin);
        return arrayTrim(transition.substring(begin, end).split(","));
    }

    private String[] getWriteSymbols(String transition) {
        int begin = transition.indexOf(']', 0);
        begin = transition.indexOf('[', begin) + 1;
        int end = transition.indexOf(']', begin);
        return arrayTrim(transition.substring(begin, end).split(","));
    }

    private TMMove getMove(String transition) {
        int begin = transition.indexOf(']', 0) + 1;
        begin = transition.indexOf(']', begin) + 1;
        return TMMove.getInstance(transition.substring(begin, transition.length()).trim());
    }

    private TMMove[] getMoves(String transition) {
        int begin = transition.indexOf(']', 0) + 1;
        begin = transition.indexOf(']', begin) + 1;
        begin = transition.indexOf('[', begin) + 1;
        int end = transition.indexOf(']', begin);
        String[] movesStr = arrayTrim(transition.substring(begin, end).split(","));
        int n = movesStr.length;
        TMMove[] moves = new TMMove[n];
        for (int i = 0; i < n; i++) {
            moves[i] = TMMove.getInstance(movesStr[i]);
        }
        return moves;
    }

    public Set<TMMultiTrackTransitionFunction> getTransitionFuctionsTM(Machine machine) {
        State currentState = machine.getState(vertices.first.getLabel());
        State futureState = machine.getState(vertices.second.getLabel());
        Set<TMMultiTrackTransitionFunction> transitionFunctions = new HashSet<>();
        String[] transitions  = PDATransitionView.clearArray
                (TMMultiTrackTransitionView.arrayTrim(getTransitionsMultiTrack(label)));
        int n = transitions.length;
        int numTapes = getNumTapes();
        int paramsLength = 2;
        for (int i = 0; i < n; i++) {
            String[] params = paramsTransitionTMMTrack(transitions[i], numTapes);
            int nParams = params.length;
            params[0] = params[0].substring(1);
            params[nParams-1] = params[nParams-1].substring(0, params[nParams-1].length()-1);
            String[] readSymbols = new String[numTapes];
            String[] writeSymbols = new String[numTapes];
            TMMove move = TMMove.getInstance(params[nParams-1]);
            for (int j = 0; j < numTapes; j++) {
                readSymbols[j] = params[j*paramsLength];
                writeSymbols[j] = params[j*paramsLength+1];
            }
            transitionFunctions.add(
                    new TMMultiTrackTransitionFunction(
                            currentState,
                            futureState,
                            readSymbols,
                            writeSymbols,
                            move
                    )
            );
        }
        return transitionFunctions;
    }
}
