package com.ufla.lfapp.activities.machine.tm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.activities.machine.HistoricalGraphActivity;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.dotlang.DotLanguage;
import com.ufla.lfapp.core.machine.dotlang.Edge;
import com.ufla.lfapp.core.machine.dotlang.GraphAdapter;
import com.ufla.lfapp.core.machine.tm.var.TuringMachineMultiTrack;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.utils.ObjectSerializerHelper;
import com.ufla.lfapp.views.machine.layout.EditTMMultiTrackLayout;
import com.ufla.lfapp.views.machine.transition.PDATransitionView;
import com.ufla.lfapp.views.machine.transition.TMMultiTrackTransitionView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.ufla.lfapp.activities.machine.fsa.ArrayAdapterGraph.GRAPH_ADAPTER_EXTRA;

public class EditTMMultiTracksActivity extends AppCompatActivityContext {

    public static final String TURING_MACHINE_MULTI_TRACK = "turingMachineMultiTrack";
    public static final String TURING_MACHINE_POINTS_MULTI_TRACK = "turingMachinePointsMultiTrack";

    private static final String TURING_MACHINE_EXTRA = "turingMachine";
    private static final String LABEL_TO_POINT_EXTRA = "labelToPoint";
    private static final String WORD_EXTRA = "word";

    private EditTMMultiTrackLayout editMachineLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editMachineLayout = new EditTMMultiTrackLayout(this);
        setContentView(editMachineLayout.getRootView());
        Intent intent = getIntent();
        boolean init = false;
        if (intent != null
                && intent.getSerializableExtra(TURING_MACHINE_MULTI_TRACK) != null) {
//            editMachineLayout.drawPushdownAutomaton((PushdownAutomatonExtend) intent
//                    .getSerializableExtra(TURING_MACHINE_ENUM));
        } else if (intent != null
                && intent.getSerializableExtra(GRAPH_ADAPTER_EXTRA) != null) {
            GraphAdapter graphAdapter = (GraphAdapter) intent
                    .getSerializableExtra(GRAPH_ADAPTER_EXTRA);
            int numTapes = 2;
            if (graphAdapter.edgeList.size() > 0) {
                Edge edge = graphAdapter.edgeList.get(0);
                String label = edge.label;
                int lastInd = label.indexOf(']');
                label = label.substring(1, lastInd).trim();
                String[] params = PDATransitionView.clearArray
                        (TMMultiTrackTransitionView.arrayTrim(label.split("[/ ]")));
                numTapes = (params.length-1) / 2;
            }
            editMachineLayout.setNumTapes(numTapes);
            editMachineLayout.drawGraph(graphAdapter);
            init = true;
        } else {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.remove(TURING_MACHINE_MULTI_TRACK);
//            editor.remove(TURING_MACHINE_POINTS_MULTI_TRACK);
//            editor.apply();
            String pushdownAutomatonStr = preferences.getString(TURING_MACHINE_MULTI_TRACK, null);
            String labelToPointStr = preferences.getString(TURING_MACHINE_POINTS_MULTI_TRACK, null);
            if (pushdownAutomatonStr != null && labelToPointStr != null) {
                TuringMachineMultiTrack turingMachine = (TuringMachineMultiTrack)
                        ObjectSerializerHelper
                        .stringToObject(pushdownAutomatonStr);
                Map<String, MyPoint> labelToPoint = (Map<String, MyPoint>) ObjectSerializerHelper
                        .stringToObject(labelToPointStr);
                if (turingMachine != null && labelToPoint != null) {
                    editMachineLayout.setNumTapes(turingMachine.getNumTracks());
                    editMachineLayout.drawTMWithLabel(turingMachine, labelToPoint);
                    init = true;
                }
            }
        }
        if (!init) {
            setNumTapes();
        }
    }

    private void setNumTapes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        final EditTMMultiTrackLayout editTMMultiTrackLayout = editMachineLayout;
        AlertDialog alertDialog = builder.setView(inflater.inflate(R.layout.dialog_num_tracks_tm, null))
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog f = (Dialog) dialog;
                        EditText et = (EditText) f.findViewById(R.id.numTracks);
                        try {
                            editTMMultiTrackLayout.setNumTapes(Integer.parseInt(
                                    et.getText().toString()));
                        } catch (Exception e) {
                            editTMMultiTrackLayout.setNumTapes(2);
                        }
                        dialog.cancel();
                    }
                })
                .create();
        alertDialog.show();
        EditText numTapesEditText= (EditText) alertDialog.findViewById(R.id.numTracks);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(numTapesEditText, InputMethodManager.SHOW_IMPLICIT);

    }

    private void saveData() {
        TuringMachineMultiTrack turingMachine = editMachineLayout.getTuringMachine();
        Map<String, MyPoint> labelToPoint = editMachineLayout.getMapLabelToPoint();
        if (turingMachine == null || labelToPoint == null
                || turingMachine.getStates().size() == 0) {
            return;
        }
        Map<State, MyPoint> stateToPoint = new HashMap<>();
        for (String stateStr : labelToPoint.keySet()) {
            stateToPoint.put(turingMachine.getState(stateStr),
                    labelToPoint.get(stateStr));
        }
        new DbAcess(this).putMachineDotLanguage(new DotLanguage(turingMachine,
                stateToPoint));
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(TURING_MACHINE_MULTI_TRACK, ObjectSerializerHelper
                .objectToString(turingMachine));
        editor.putString(TURING_MACHINE_POINTS_MULTI_TRACK, ObjectSerializerHelper
                .objectToString((Serializable) labelToPoint));
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }


    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tm_multi_track, menu);
        return true;
    }

    private boolean clear() {
        editMachineLayout.clear();
        setNumTapes();
        return true;
    }

    private boolean history() {
        Intent intent = new Intent(this, HistoricalGraphActivity.class);
        intent.putExtra(HistoricalGraphActivity.MACHINE_TYPE_SEL, MachineType.TM_MULTI_TRACK);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.clear:
                return clear();
            case R.id.history:
                return history();
            case R.id.verifyEntryTM:
                //Verify
                return verifyEntryTM();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean verifyEntryTM() {
        final TuringMachineMultiTrack turingMachine = editMachineLayout.getTuringMachine();
        final Map<String, MyPoint> labelToPoint = editMachineLayout.getMapLabelToPoint();
        if (turingMachine != null) {
            if (turingMachine.getInitialState() == null) {
                Toast.makeText(EditTMMultiTracksActivity.this,
                        "Erro! MT Multi-trilhas não possui estado inicial!",
                        Toast.LENGTH_LONG).show();
                return true;
            }
            if (turingMachine.getFinalStates().isEmpty()) {
                Toast.makeText(EditTMMultiTracksActivity.this,
                        "Erro! MT Multi-trilhas reconhecedora não possui estado final!",
                        Toast.LENGTH_LONG).show();
                return true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            builder.setView(inflater.inflate(R.layout.dialog_entry, null))
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Dialog f = (Dialog) dialog;
                            EditText etWord = (EditText) f.findViewById(R.id.word);
                            final String word = etWord.getText().toString();
                            Intent intent = new Intent(EditTMMultiTracksActivity.this,
                                    ProcessWordTMMultiTrackActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(TURING_MACHINE_EXTRA, turingMachine);
                            bundle.putSerializable(LABEL_TO_POINT_EXTRA, (Serializable) labelToPoint);
                            bundle.putSerializable(WORD_EXTRA, word);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            dialog.cancel();
//                                    try {
//                                        if (automatonB.processEntry(word)) {
//                                            Toast.makeText(EditFiniteStateAutomatonActivity.this,
//                                                    "Aceita a palavra '" + word + "'", Toast.LENGTH_LONG)
//                                                    .show();
//                                        } else {
//                                            Toast.makeText(EditFiniteStateAutomatonActivity.this,
//                                                    "Rejeita a palavra '" + word + "'", Toast.LENGTH_LONG)
//                                                    .show();
//                                        }
//                                    } catch (Exception e) {
//                                        Toast.makeText(EditFiniteStateAutomatonActivity.this,
//                                                e.getMessage(), Toast.LENGTH_LONG)
//                                                    .show();
//                                        e.printStackTrace();
//                                    }

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
        }
        return true;
    }


}
