package com.ufla.lfapp.activities;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.graph.layout.EditGraphLayout;

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

                return true;
            case R.id.defineInitialState:
                editMachineLayout.defineInitialStateMode();
                Toast.makeText(this, "Selecione o estado inicial!", Toast.LENGTH_SHORT)
                        .show();
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
