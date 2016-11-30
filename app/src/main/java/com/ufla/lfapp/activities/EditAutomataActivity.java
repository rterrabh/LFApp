package com.ufla.lfapp.activities;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.ufla.lfapp.activities.graph.layout.EditGraphLayout;
import com.ufla.lfapp.vo.Automaton;
import com.ufla.lfapp.vo.TransitionFunction;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editMachineLayout = new EditGraphLayout(this);
//        for (int i = 0; i < 15; i += 5) {
//            for (int j = 0; j < 15; j += 5) {
//                automataView.addVertexView(new Point(i, j));
//            }
//        }
//        Point p1 = new Point(3, 1);
//        Point p2 = new Point(1, 3);
//        automataView.addVertexView(p1);
//        automataView.addVertexView(p2);
        //automataView.addEdgeView(automataView.getVertexView(p1), automataView.getVertexView(p2));
        myDragListener = new MyDragListener();
        setContentView(editMachineLayout.getRootView());

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
            case R.id.creationMode:
                editMachineLayout.setMode(EditGraphLayout.CREATION_MODE);
                Toast.makeText(this, "Modo de criação selecionado!", Toast.LENGTH_SHORT)
                        .show();
                return true;
            case R.id.editionMode:
                editMachineLayout.setMode(EditGraphLayout.EDITION_MODE);
                Toast.makeText(this, "Modo de edição selecionado!", Toast.LENGTH_SHORT)
                        .show();
                return true;
            case R.id.completeAutomaton:
                Automaton automaton = editMachineLayout.getAutomaton();
                if (automaton != null) {
                    Log.d("automato", automaton.toString());
                    Set<TransitionFunction> transitionFunctionsToComplAuto =
                            automaton.getTransitionFunctionsToCompleteAutomaton();
                    if (transitionFunctionsToComplAuto.isEmpty()) {
                        Toast.makeText(this, "Autômato já está completo!", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        editMachineLayout.selectErrorState(automaton.getStateError(),
                                transitionFunctionsToComplAuto);
                        Toast.makeText(this, "Selecione a posição para inserir o estado de erro!",
                                Toast.LENGTH_SHORT)
                                .show();
                    }

                }
                return true;
            case R.id.defineInitialState:
                editMachineLayout.defineInitialStateMode();
                Toast.makeText(this, "Selecione o estado inicial!", Toast.LENGTH_SHORT)
                        .show();
                return true;
            case R.id.verifyEntry:
                final Automaton automatonB = editMachineLayout.getAutomaton();
                if (automatonB != null) {
                    Log.d("automato", automatonB.toString());
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
                Automaton automatonC = editMachineLayout.getAutomaton();
                editMachineLayout.clear();
                if (automatonC != null) {
                    if (automatonC.isAFD()) {
                        Toast.makeText(this, "Autômato já é um AFD!", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Automaton automatonAFD = automatonC.AFNDLambdaToAFD();
                        Automaton automatonAFDSimplify =
                                automatonAFD.getAutomatonWithStatesNameSimplify();
                        SortedMap<String, Point> statesPoints =
                                automatonAFDSimplify.getStatesPoints();
                        editMachineLayout.clear();
                        for (SortedMap.Entry<String, Point> entry : statesPoints.entrySet()) {
                            editMachineLayout.addVertexView(entry.getValue(), entry.getKey());
                        }
                        for (Map.Entry<Pair<String, String>, SortedSet<String>> entry :
                                automatonAFDSimplify.getTransitionsAFD().entrySet()) {
                            StringBuilder sb = new StringBuilder();
                            for (String str : entry.getValue()) {
                                sb.append(str)
                                        .append(',');
                            }
                            sb.deleteCharAt(sb.length() - 1);
                            editMachineLayout.addEdgeView(statesPoints.get(entry.getKey().first),
                                    statesPoints.get(entry.getKey().second), sb.toString());
                        }
                        editMachineLayout.setVertexViewAsInitial(statesPoints.get(
                                automatonAFDSimplify.getInitialState()));
                        for (String state : automatonAFDSimplify.getFinalStates()) {
                            editMachineLayout.setVertexViewAsFinal(statesPoints.get(
                                    state));
                        }
                        Log.d("automato", automatonC.toString());
                        Log.d("automatoAFD", automatonAFD.toString());
                    }
                }
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
