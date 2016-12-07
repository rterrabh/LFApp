package com.ufla.lfapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.vo.AutomatonGUI;

import java.util.List;

/**
 * Created by carlos on 12/7/16.
 */

public class HistoricalAutomataActivity extends AppCompatActivity {

    private List<AutomatonGUI> automatonGUIs;
    private ArrayAdapter listAdapterAutomaton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_automata);
        automatonGUIs = new DbAcess(this).getAutomatons();
        listAdapterAutomaton = new ArrayAdapterAutomata(this, automatonGUIs);
        ((ListView) findViewById(R.id.automatons)).setAdapter(listAdapterAutomaton);
    }

    public void copyAutomaton(View view) {
        Bundle bundle = new Bundle();
        View completeView = ((View) (view.getParent().getParent()));
        long machineId = (Integer.parseInt(String.valueOf(((TextView) completeView
                .findViewById(R.id.id)).getText())));
        Intent intent = new Intent(this, EditAutomataActivity.class);
        intent.putExtra("Automaton", getAutomatonWithId(machineId));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public AutomatonGUI getAutomatonWithId(long id) {
        for (AutomatonGUI automatonGUI : automatonGUIs) {
            if (automatonGUI.getId() == id) {
                return automatonGUI;
            }
        }
        return null;
    }

    public void deleteAutomaton(View view) {
        View completeView = ((View) (view.getParent().getParent()));
        long machineId = (Integer.parseInt(String.valueOf(((TextView) completeView
                .findViewById(R.id.id)).getText())));
        listAdapterAutomaton.remove(getAutomatonWithId(machineId));
        new DbAcess(this).deleteAutomaton(machineId);
    }

    public void cleanHistoricalAutomaton (View view) {
        listAdapterAutomaton.clear();
        new DbAcess(this).cleanHistoryAutomaton();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
