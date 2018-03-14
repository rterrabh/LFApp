package com.ufla.lfapp.activities.grammar;

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
import com.ufla.lfapp.activities.AppCompatActivityContext;
import com.ufla.lfapp.persistence.DbAcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by carlos on 03/08/16.
 */
public class HistoricalGrammarsActivity extends AppCompatActivityContext {

    private static final String CLEAN = "clean";
    private static final String GRAMMAR_EXTRA = "grammar";
    private static Map<String, Integer> grammarsStatic;

    private ArrayAdapter listAdapterGrammar;

    public static int getGrammarId(String grammar) {
        return grammarsStatic.get(grammar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_grammars);
        Map<String, Integer> grammars = new DbAcess(this).readGrammars();
        grammarsStatic = grammars;
        List<String> grammarsList = new ArrayList<>(grammars.keySet());
        grammarsList.add(CLEAN);
        listAdapterGrammar = new ArrayAdapterGrammar(this, grammarsList);
        ((ListView) findViewById(R.id.grammars)).setAdapter(listAdapterGrammar);
    }

    public void copyGrammar(View view) {
        Bundle params = new Bundle();
        params.putString(GRAMMAR_EXTRA, String.valueOf(((TextView)
                ((View) (view.getParent().getParent())).findViewById(R.id.grammar)).getText()));
        Intent intent = new Intent(this, GrammarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(params);
        startActivity(intent);
        finish();
    }

    public void deleteGrammar(View view) {
        View completeView = ((View) (view.getParent().getParent()));
        String grammar = ((TextView) completeView.findViewById(R.id.grammar)).getText().toString();
        int id = (Integer.parseInt(String.valueOf(((TextView) completeView
                .findViewById(R.id.id)).getText())));
        listAdapterGrammar.remove(grammar);
        new DbAcess(this).deleteGrammar(id);
    }

    public void cleanHistoricalGrammar (View view) {
        listAdapterGrammar.clear();
        new DbAcess(this).cleanHistoryGrammar();
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
