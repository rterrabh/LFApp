package com.ufla.lfapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.vo.AcademicSupport;
import com.ufla.lfapp.vo.Grammar;

/**
 * Created by root on 18/07/16.
 */
public class MenuActivity extends AppCompatActivity {


    private String grammar;
    private String word;

    private void defineHeightOfScrollViews() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dpHeight = displayMetrics.heightPixels;
        ScrollView grammarScrollView = (ScrollView) findViewById(R.id
                .grammarView);
        ScrollView buttonsScrollView = (ScrollView) findViewById(R.id
                .buttonsView);
        assert buttonsScrollView != null;
        assert grammarScrollView != null;
//        int totalHeight = grammarScrollView.getLayoutParams().height
//                +buttonsScrollView.getLayoutParams().height;
//        int heightGrammar = min(totalHeight / 3, grammarScrollView.getLayoutParams().height);
//        grammarScrollView.getLayoutParams().height = heightGrammar;
//        buttonsScrollView.getLayoutParams().height = totalHeight-heightGrammar;
//        System.out.println(totalHeight+";"+heightGrammar+";"+(totalHeight-heightGrammar));
        grammarScrollView.getLayoutParams().height = dpHeight / 3;
        buttonsScrollView.getLayoutParams().height = dpHeight - dpHeight/3;
//        buttonsScrollView.setMinimumHeight((int) (dpHeight - dpHeight/3));
        System.out.println(dpHeight+";"+(grammarScrollView.getLayoutParams()
                .height+ buttonsScrollView.getLayoutParams().height));
    }

    private void setGrammar() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle dados = intent.getExtras();
            if (dados != null) {
                grammar = dados.getString("grammar");
                word = dados.getString("word");
                Grammar g = new Grammar(grammar);
                if (grammar != null) {
                    TextView inputGrammar = (TextView) findViewById(R.id.inputGrammar);
                    AcademicSupport academic = new AcademicSupport();
                    academic.setResult(g);
                    assert inputGrammar != null;
                    inputGrammar.setText(Html.fromHtml(academic.getResult()));
                    inputGrammar.setTextColor(getResources().getColor(R.color.DarkGray));
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        defineHeightOfScrollViews();
        setGrammar();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
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

    public void startIdentificationGrammarActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        Intent intent = new Intent(this, IdGrammarActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startDerivationLeftmostActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtras(params);
        startActivity(intent);

    }

    public void startRRecursionInTheInitialSymbolActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startREmptyProductionsActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRChainRulesActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRNTermActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRNReachActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startChomskyNormalFormActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRRecursionDirectLeftActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startRRecursionDirAndIndLeftActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startGreibachNormalFormActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void startCYKActivity(View view) {
        Bundle params = new Bundle();
        params.putString("grammar", grammar);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(params);
        startActivity(intent);
    }





}
