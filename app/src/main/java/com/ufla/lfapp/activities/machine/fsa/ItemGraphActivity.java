package com.ufla.lfapp.activities.machine.fsa;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.core.machine.dotlang.GraphAdapter;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;

/**
 * Created by carlos on 15/07/17.
 */

public class ItemGraphActivity extends AppCompatActivityContext {

    public static final String GRAPH_ADAPTER_EXTRA = "GraphAdapterExtra";
    private GraphAdapter graphAdapter;
    private EditGraphLayout editGraphLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_automaton);
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null
                || intent.getExtras().getSerializable(GRAPH_ADAPTER_EXTRA) == null) {
            Toast.makeText(this, R.string.exception_automaton_not_found, Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Bundle bundle = intent.getExtras();
        graphAdapter = (GraphAdapter) bundle.getSerializable(GRAPH_ADAPTER_EXTRA);
        editGraphLayout = new EditGraphLayout(this);
        editGraphLayout.drawGraph(graphAdapter);
        View view = editGraphLayout.getRootView();
        final GestureDetector gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        ItemGraphActivity.this.onBackPressed();
                        return true;
                    }
                }
        );
        editGraphLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ((ConstraintLayout) findViewById(R.id.itemAutomataLayout)).addView(view);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        editGraphLayout.resizeTo(size.x, size.y);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historical_automata, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
