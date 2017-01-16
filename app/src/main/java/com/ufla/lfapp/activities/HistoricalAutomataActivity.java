package com.ufla.lfapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.vo.machine.AutomatonGUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos on 12/7/16.
 */

public class HistoricalAutomataActivity extends AppCompatActivity {

    private List<AutomatonGUI> automatonGUIs;
    private List<String> automatonGUILabels;
    private ArrayAdapter listAdapterAutomaton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_automata);
        automatonGUIs = new DbAcess(this).getLastAutomatonGUIs();
        automatonGUILabels = new ArrayList<>();
        for (AutomatonGUI automatonGUI : automatonGUIs) {
            automatonGUILabels.add(automatonGUI.getLabel());
        }
        listAdapterAutomaton = new ArrayAdapterAutomata(this, automatonGUIs);
        ((ListView) findViewById(R.id.automatons)).setAdapter(listAdapterAutomaton);
    }


    public AutomatonGUI getAutomatonWithId(long id) {
        for (AutomatonGUI automatonGUI : automatonGUIs) {
            if (automatonGUI.getId() == id) {
                return automatonGUI;
            }
        }
        return null;
    }

    private void update() {
        DbAcess dbAcess = new DbAcess(HistoricalAutomataActivity.this);
        for (int i = 0; i < automatonGUIs.size(); i++) {
            String newLabel = automatonGUIs.get(i).getLabel();
            if (!newLabel.equals(automatonGUILabels.get(i))) {
                System.out.println("Atualizar: novo label " + newLabel + "; antigo label "
                        + automatonGUILabels.get(i));
                System.out.println(dbAcess.updateMachineDotLanguage(automatonGUIs.get(i)));
            }
        }
        System.out.println("OVER");
                /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbAcess dbAcess = new DbAcess(HistoricalAutomataActivity.this);
                for (int i = 0; i < automatonGUIs.size(); i++) {
                    String newLabel = automatonGUIs.get(i).getLabel();
                    if (!newLabel.equals(automatonGUILabels.get(i))) {
                        System.out.println("Atualizar: novo label " + newLabel + "; antigo label "
                                + automatonGUILabels.get(i));
                        dbAcess.updateMachineDotLanguage(automatonGUIs.get(i));
                    }
                }
            }
        }).start();*/
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        update();
        super.onDestroy();
    }


    public void cleanHistoricalAutomaton (View view) {
        listAdapterAutomaton.clear();
        new DbAcess(this).cleanHistoryMachineDotLanguage();
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
