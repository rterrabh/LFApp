package com.ufla.lfapp.activities.automata;

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
import android.support.v7.app.AppCompatActivity;
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
import com.ufla.lfapp.activities.automata.graph.layout.EditGraphLayout;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.vo.machine.Automaton;
import com.ufla.lfapp.vo.machine.AutomatonGUI;
import com.ufla.lfapp.vo.machine.DotLanguage;
import com.ufla.lfapp.vo.machine.State;
import com.ufla.lfapp.vo.machine.TransitionFunction;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * Created by carlos on 9/21/16.
 */

public class EditAutomataActivity extends AppCompatActivity {

    private MyDragListener myDragListener;
    private EditGraphLayout editMachineLayout;
    private AutomatonGUI onChangeOrientation;

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
        editMachineLayout = new EditGraphLayout(this);
        myDragListener = new MyDragListener();
        setContentView(editMachineLayout.getRootView());
        Intent intent = getIntent();
        if (intent != null
                && intent.getSerializableExtra("Automaton") != null) {
            drawAutomaton((AutomatonGUI) intent.getSerializableExtra("Automaton"));
        } else {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            String dotLanguageStr = preferences.getString("editAutomaton", "");
            if (!dotLanguageStr.isEmpty()) {
                editMachineLayout.drawAutomaton(new DotLanguage(dotLanguageStr)
                        .toAutomatonGUI());
            }
        }

    }

    @Override
    protected void onPause() {
        super.onStop();
        AutomatonGUI automatonGUI = editMachineLayout.getAutomatonGUI();
        if (automatonGUI.getStates().size() > 0) {
            new DbAcess(this).putAutomatonGUI(automatonGUI);
        }
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("editAutomaton", DotLanguage.parseDotLanguage(
                editMachineLayout.getAutomatonGUI()).toString());
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AutomatonGUI automatonGUI = editMachineLayout.getAutomatonGUI();
        if (automatonGUI.getStates().size() > 0) {
            new DbAcess(this).putAutomatonGUI(automatonGUI);
        }
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("editAutomaton", DotLanguage.parseDotLanguage(
                editMachineLayout.getAutomatonGUI()).toString());
        editor.apply();
    }


    @Override
    protected void onDestroy() {
        AutomatonGUI automatonGUI = editMachineLayout.getAutomatonGUI();
        if (automatonGUI.getStates().size() > 0) {
            new DbAcess(this).putAutomatonGUI(automatonGUI);
        }
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("editAutomaton", DotLanguage.parseDotLanguage(
                editMachineLayout.getAutomatonGUI()).toString());
        editor.apply();
        super.onDestroy();

    }

    public void drawAutomaton(AutomatonGUI automaton) {
        Map<State, Point> statesPoints =
                automaton.getStateGridPositions();
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
        getMenuInflater().inflate(R.menu.menu_edit_machine, menu);
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
            case R.id.completeAutomaton:
                if (editMachineLayout.isOnMove()) {
                    Toast.makeText(this, "Erro!\n" +
                        "Indique para onde deve mover o estado selecionado, antes de gerar o " +
                            "autômato completo.", Toast.LENGTH_LONG)
                            .show();
                    return true;
                }
                final Automaton automaton = editMachineLayout.getCompleteAutomatonGUI();
                if (automaton != null) {
                    Set<TransitionFunction> transitionFunctionsToComplAuto =
                            automaton.getTransitionFunctionsToCompleteAutomaton();
                    if (transitionFunctionsToComplAuto.isEmpty()) {
                        Toast.makeText(this, "Autômato já está completo!", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        editMachineLayout.selectErrorState(automaton.getStateError().getName(),
                                transitionFunctionsToComplAuto);
                        Toast.makeText(this, "Indique a posição para inserir o estado de erro!",
                                Toast.LENGTH_SHORT)
                                .show();
                    }

                }
                return true;
            case R.id.verifyEntry:
                final Automaton automatonB = editMachineLayout.getCompleteAutomatonGUI();
                if (automatonB != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
                    builder.setView(inflater.inflate(R.layout.dialog_entry, null))
                            .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Dialog f = (Dialog) dialog;
                                    EditText etWord = (EditText) f.findViewById(R.id.word);
                                    String word = etWord.getText().toString();
                                    dialog.cancel();
                                    try {
                                        if (automatonB.processEntry(word)) {
                                            Toast.makeText(EditAutomataActivity.this,
                                                    "Aceita a palavra '" + word + "'", Toast.LENGTH_LONG)
                                                    .show();
                                        } else {
                                            Toast.makeText(EditAutomataActivity.this,
                                                    "Rejeita a palavra '" + word + "'", Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(EditAutomataActivity.this,
                                                e.getMessage(), Toast.LENGTH_LONG)
                                                    .show();
                                        e.printStackTrace();
                                    }

                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();
                }
                return true;
            case R.id.transformAFNDInAFD:
                Automaton automatonC = editMachineLayout.getCompleteAutomatonGUI();
                if (automatonC != null) {
                    if (automatonC.isAFD()) {
                        Toast.makeText(this, "Autômato já é um AFD!", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Automaton automatonAFD = automatonC.AFNDLambdaToAFD()
                                .getAutomatonWithStatesNameSimplify();
                        SortedMap<State, Point> statesPoints =
                                automatonAFD.getStatesPointsFake();
                        editMachineLayout.drawAutomaton(new AutomatonGUI(automatonAFD,
                                statesPoints));
                    }
                }
                return true;
            case R.id.transformAFNDInAFDStepByStep:
                AutomatonGUI automatonF = editMachineLayout.getCompleteAutomatonGUI();
                if (automatonF != null) {
                    if (automatonF.isAFD()) {
                        Toast.makeText(this, "Autômato já é um AFD!", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Intent intent = new Intent(this,
                                ConvertAFNDInAFDActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("AutomatonGUIEdit", automatonF);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
                return true;
            case R.id.minimizeAFD:
                Automaton automatonD = editMachineLayout.getCompleteAutomatonGUI();
                if (automatonD != null) {
                    if (!automatonD.isAFD()) {
                        Toast.makeText(this, "Autômato não é um AFD!", Toast.LENGTH_LONG)
                                .show();
                    } else if (!automatonD.isComplete()) {
                        Toast.makeText(this, "Autômato não é um AFD completo!", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Automaton automatonMinimize = automatonD.getMinimizeAutomaton();
                        AutomatonGUI automatonMinimizeGUI = new AutomatonGUI(automatonMinimize,
                                automatonMinimize.getStatesPointsFake());
                        editMachineLayout.clear();
                        editMachineLayout.drawAutomaton(automatonMinimizeGUI);
                    }
                }
                return true;
            case R.id.minimizeAFDStepByStep:
                Automaton automatonE = editMachineLayout.getCompleteAutomatonGUI();
                if (automatonE != null) {
                    if (!automatonE.isAFD()) {
                        Toast.makeText(this, "Autômato não é um AFD!", Toast.LENGTH_LONG)
                                .show();
                    } else if (!automatonE.isComplete()) {
                        Toast.makeText(this, "Autômato não é um AFD completo!", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Intent intent = new Intent(EditAutomataActivity.this,
                                AutomatonMinimizationActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("AutomatonGUIEdit", automatonE);
                        intent.putExtras(bundle);
                        EditAutomataActivity.this.startActivity(intent);
                    }
                }
                return true;
            case R.id.regexToAFND:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                builder.setView(inflater.inflate(R.layout.dialog_regex_to_afnd, null))
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Dialog f = (Dialog) dialog;
                                EditText etRegex = (EditText) f.findViewById(R.id.regex);
                                String regex = etRegex.getText().toString();
                                if (!AutomatonGUI.isAValidRegex(regex)) {
                                    Toast.makeText(EditAutomataActivity.this,
                                            "Regex inválida!", Toast.LENGTH_LONG)
                                            .show();
                                } else {
                                    AutomatonGUI automatonGUIRegex =
                                            AutomatonGUI.getAutomatonByRegex(regex);
                                    AutomatonGUI automatonGUIEdit =
                                            editMachineLayout.getAutomatonGUI();
                                    dialog.cancel();
                                    Intent intent = new Intent(EditAutomataActivity.this,
                                            RegexToAutomataActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("AutomatonGUIRegex", automatonGUIRegex);
                                    bundle.putSerializable("AutomatonGUIEdit", automatonGUIEdit);
                                    intent.putExtras(bundle);
                                    EditAutomataActivity.this.startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
                return true;
            case R.id.history:
                Intent intent = new Intent(this, HistoricalAutomataActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
