package com.ufla.lfapp.activities.machine.fsa;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.activities.machine.HistoricalGraphActivity;
import com.ufla.lfapp.activities.machine.tm.EditTuringMachineActivity;
import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.dotlang.DotLanguage;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.views.machine.layout.EditFSAutomatonLayout;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * Created by carlos on 9/21/16.
 */

public class EditFiniteStateAutomatonActivity extends AppCompatActivityContext {

    private static final String FINITE_STATE_AUTOMATON_EXTRA =
            "FiniteStateAutomaton";
    private static final String AUTOMATON_GUI_EDIT_EXTRA =
            "AutomatonGUIEdit";
    private static final String AUTOMATON_GUI_REGEX_EXTRA =
            "AutomatonGUIRegex";
    private static final String EDIT_AUTOMATON_PREFERENCES =
            "editAutomaton";
    private static final String FINITE_STATE_AUTOMATON_GUI_EXTRA =
            "FiniteStateAutomatonGUI";
    private static final String WORD_EXTRA = "word";

    private MyDragListener myDragListener;
    private EditGraphLayout editMachineLayout;
    private FiniteStateAutomatonGUI onChangeOrientation;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //updateMachineLayout(automatonGUI);
        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editMachineLayout = new EditFSAutomatonLayout(this);
        myDragListener = new MyDragListener();
        setContentView(editMachineLayout.getRootView());
        Intent intent = getIntent();
        if (intent != null
                && intent.getSerializableExtra(FINITE_STATE_AUTOMATON_EXTRA) != null) {
            drawAutomaton((FiniteStateAutomatonGUI) intent.getSerializableExtra(FINITE_STATE_AUTOMATON_EXTRA));
        } else {
            SharedPreferences preferences = getSharedPreferences(
                    "EditFiniteStateAutomatonActivity", MODE_PRIVATE);

            String dotLanguageStr = preferences.getString(EDIT_AUTOMATON_PREFERENCES, "");
            if (!dotLanguageStr.isEmpty()) {
                System.out.println(dotLanguageStr);
                try {
                    editMachineLayout.drawGraph(new DotLanguage(dotLanguageStr)
                                                        .toGraphAdapter());
                } catch (Exception e) {
                    preferences.edit().remove(EDIT_AUTOMATON_PREFERENCES).apply();
                    e.printStackTrace();
                }
            }
        }

    }

    public void saveData() {
        FiniteStateAutomatonGUI automatonGUI = editMachineLayout.getAutomatonGUI();
        if (automatonGUI == null) {
            return;
        }
        if (automatonGUI.getStates().size() > 0) {
            new DbAcess(this).putMachineDotLanguage(new DotLanguage(automatonGUI,
                    automatonGUI.getStateGridPositions()));
        }
        SharedPreferences.Editor editor = getSharedPreferences(
                "EditFiniteStateAutomatonActivity", MODE_PRIVATE).edit();
        editor.putString(EDIT_AUTOMATON_PREFERENCES, new DotLanguage(automatonGUI,
                automatonGUI.getStateGridPositions()).getGraph());
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

    public void drawAutomaton(FiniteStateAutomatonGUI automaton) {
        Map<State, Point> statesPoints =
                automaton.getStateGridPoint();
        editMachineLayout.clear();
        for (Map.Entry<State, Point> entry : statesPoints.entrySet()) {
            editMachineLayout.addVertexView(entry.getValue(), entry.getKey().getName());
        }
        for (Map.Entry<Pair<State, State>, SortedSet<String>> entry :
                automaton.getTransitionsAFD().entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String str : entry.getValue()) {
                sb.append(str)
                        .append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            editMachineLayout.addEdgeView(statesPoints.get(entry.getKey().first),
                    statesPoints.get(entry.getKey().second), sb.toString());
        }
        if (automaton.getInitialState() != null) {
            editMachineLayout.setVertexViewAsInitial(statesPoints.get(
                    automaton.getInitialState()));
        }
        for (State state : automaton.getFinalStates()) {
            editMachineLayout.setVertexViewAsFinal(statesPoints.get(
                    state));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fs_automaton, menu);
        return true;
    }

    public boolean completeAutomaton() {
        if (editMachineLayout.isOnMove()) {
            Toast.makeText(this,
                    R.string.exception_select_lock_state_position,
                    Toast.LENGTH_LONG)
                    .show();
            return true;
        }
        final FiniteStateAutomaton finiteStateAutomaton = editMachineLayout.getCompleteAutomatonGUI();
        if (finiteStateAutomaton != null) {
            Set<FSATransitionFunction> FSATransitionFunctionsToComplAuto =
                    finiteStateAutomaton.getTransitionFunctionsToCompleteAutomaton();
            System.out.println(FSATransitionFunctionsToComplAuto);
            if (FSATransitionFunctionsToComplAuto.isEmpty()) {
                Toast.makeText(this, R.string.exception_fsa_already_completed, Toast.LENGTH_SHORT)
                        .show();
            } else {
                editMachineLayout.selectErrorState(finiteStateAutomaton.getStateError().getName(),
                        FSATransitionFunctionsToComplAuto);
                Toast.makeText(this, R.string.set_lock_state_position,
                        Toast.LENGTH_SHORT)
                        .show();
            }

        }
        return true;
    }

    public boolean verifyEntry() {
        final FiniteStateAutomaton finiteStateAutomatonB = editMachineLayout.getCompleteAutomatonGUI();
        if (finiteStateAutomatonB != null) {
            if (finiteStateAutomatonB.getInitialState() == null) {
                Toast.makeText(EditFiniteStateAutomatonActivity.this,
                        "Erro! Autômato finito não possui estado inicial!",
                        Toast.LENGTH_LONG).show();
                return true;
            }
            if (finiteStateAutomatonB.getFinalStates().isEmpty()) {
                Toast.makeText(EditFiniteStateAutomatonActivity.this,
                        "Erro! Autômato finito não possui estado final!",
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
                            Intent intent = new Intent(EditFiniteStateAutomatonActivity.this,
                                    ProcessWordFSAActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(FINITE_STATE_AUTOMATON_GUI_EXTRA, finiteStateAutomatonB);
                            bundle.putSerializable(WORD_EXTRA, word);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            dialog.cancel();
//                                    try {
//                                        if (finiteStateAutomatonB.processEntry(word)) {
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

    public boolean transformAFNDInAFD() {
        FiniteStateAutomaton finiteStateAutomatonC = editMachineLayout.getCompleteAutomatonGUI();
        if (finiteStateAutomatonC != null) {
            if (finiteStateAutomatonC.isAFD()) {
                Toast.makeText(this, R.string.exception_fsa_already_deterministic, Toast.LENGTH_LONG)
                        .show();
            } else {
                FiniteStateAutomaton finiteStateAutomatonAFD = finiteStateAutomatonC.AFNDLambdaToAFD()
                        .getAutomatonWithStatesNameSimplify();
                SortedMap<State, Point> statesPoints =
                        finiteStateAutomatonAFD.getStatesPointsFake();
                editMachineLayout.drawAutomaton(new FiniteStateAutomatonGUI(finiteStateAutomatonAFD,
                        statesPoints));
            }
        }
        return true;
    }

    public boolean transformAFNDInAFDStepByStep() {
        FiniteStateAutomatonGUI automatonF = editMachineLayout.getCompleteAutomatonGUI();
        if (automatonF != null) {
            if (automatonF.isAFD()) {
                Toast.makeText(this, R.string.exception_fsa_already_deterministic, Toast.LENGTH_LONG)
                        .show();
            } else {
                Intent intent = new Intent(this,
                        ConvertAFNDInAFDActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AUTOMATON_GUI_EDIT_EXTRA, automatonF);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
        return true;
    }

    public boolean minimizeAFD() {
        FiniteStateAutomaton finiteStateAutomatonD = editMachineLayout.getCompleteAutomatonGUI();
        if (finiteStateAutomatonD != null) {
            if (!finiteStateAutomatonD.isAFD()) {
                Toast.makeText(this, R.string.exception_fsa_not_deterministic, Toast.LENGTH_LONG)
                        .show();
            } else if (!finiteStateAutomatonD.isComplete()) {
                Toast.makeText(this, R.string.exception_fsa_not_completed, Toast.LENGTH_LONG)
                        .show();
            } else {
                FiniteStateAutomaton finiteStateAutomatonMinimize = finiteStateAutomatonD.getMinimizeAutomaton();
                FiniteStateAutomatonGUI automatonMinimizeGUI = new FiniteStateAutomatonGUI(finiteStateAutomatonMinimize,
                        finiteStateAutomatonMinimize.getStatesPointsFake());
                editMachineLayout.clear();
                editMachineLayout.drawAutomaton(automatonMinimizeGUI);
            }
        }
        return true;
    }

    public boolean minimizeAFDStepByStep() {
        FiniteStateAutomaton finiteStateAutomatonE = editMachineLayout.getCompleteAutomatonGUI();
        if (finiteStateAutomatonE != null) {
            if (!finiteStateAutomatonE.isAFD()) {
                Toast.makeText(this, R.string.exception_fsa_not_deterministic, Toast.LENGTH_LONG)
                        .show();
            } else if (!finiteStateAutomatonE.isComplete()) {
                Toast.makeText(this, R.string.exception_fsa_not_completed, Toast.LENGTH_LONG)
                        .show();
            } else {
                Intent intent = new Intent(EditFiniteStateAutomatonActivity.this,
                        AutomatonMinimizationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AUTOMATON_GUI_EDIT_EXTRA, finiteStateAutomatonE);
                intent.putExtras(bundle);
                EditFiniteStateAutomatonActivity.this.startActivity(intent);
            }
        }
        return true;
    }

    public boolean regexToAFND() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        builder.setView(inflater.inflate(R.layout.dialog_regex_to_afnd, null))
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog f = (Dialog) dialog;
                        EditText etRegex = (EditText) f.findViewById(R.id.regex);
                        String regex = etRegex.getText().toString();
                        if (!FiniteStateAutomatonGUI.isAValidRegex(regex)) {
                            Toast.makeText(EditFiniteStateAutomatonActivity.this,
                                    R.string.exception_invalid_regex, Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            FiniteStateAutomatonGUI automatonGUIRegex =
                                    FiniteStateAutomatonGUI.getAutomatonByRegex(regex);
                            FiniteStateAutomatonGUI automatonGUIEdit =
                                    editMachineLayout.getAutomatonGUI();
                            dialog.cancel();
                            Intent intent = new Intent(EditFiniteStateAutomatonActivity.this,
                                    RegexToAutomataActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(AUTOMATON_GUI_REGEX_EXTRA, automatonGUIRegex);
                            bundle.putSerializable(AUTOMATON_GUI_EDIT_EXTRA, automatonGUIEdit);
                            intent.putExtras(bundle);
                            EditFiniteStateAutomatonActivity.this.startActivity(intent);
                        }
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
        return true;
    }


    public boolean history() {
        Intent intent = new Intent(this, HistoricalGraphActivity.class);
        intent.putExtra(HistoricalGraphActivity.MACHINE_TYPE_SEL, MachineType.FSA);
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
            case R.id.completeAutomaton:
                return completeAutomaton();
            case R.id.verifyEntry:
                return verifyEntry();
//            case R.id.transformAFNDInAFD:
//                return transformAFNDInAFD();
            case R.id.transformAFNDInAFDStepByStep:
                return transformAFNDInAFDStepByStep();
//            case R.id.minimizeAFD:
//                return minimizeAFD();
            case R.id.minimizeAFDStepByStep:
                return minimizeAFDStepByStep();
            case R.id.regexToAFND:
                return regexToAFND();
            case R.id.history:
                return history();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean clear() {
        editMachineLayout.clear();
        return true;
    }


    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }



    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(
                R.drawable.shape_droptarget);
        Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }
}
