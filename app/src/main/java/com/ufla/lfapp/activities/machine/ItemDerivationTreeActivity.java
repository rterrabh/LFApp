package com.ufla.lfapp.activities.machine;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationPosition;

/**
 * Created by carlos on 2/22/17.
 */

public class ItemDerivationTreeActivity extends AppCompatActivityContext {

    private static final String DERIVATION_TREE_EXTRA = "DerivationTree";
    private TreeDerivationPosition treeDerivation;
    private EditGraphLayout editGraphLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_automaton);
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null
                || intent.getExtras().getSerializable(DERIVATION_TREE_EXTRA) == null) {
            Toast.makeText(this, R.string.exception_derivation_tree_not_found, Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Bundle bundle = intent.getExtras();
        treeDerivation = (TreeDerivationPosition) bundle.getSerializable(DERIVATION_TREE_EXTRA);
        editGraphLayout = new EditGraphLayout(this);
        editGraphLayout.drawDerivationTree(treeDerivation);
        View view = editGraphLayout.getRootView();
        final GestureDetector gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        ItemDerivationTreeActivity.this.onBackPressed();
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
