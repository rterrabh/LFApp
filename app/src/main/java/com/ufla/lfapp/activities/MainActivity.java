package com.ufla.lfapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.menu.MenuActivity;
import com.ufla.lfapp.activities.utils.Algorithm;
import com.ufla.lfapp.activities.utils.LFAppKeyboard;
import com.ufla.lfapp.activities.utils.OnKeyboardStateChangedListener;
import com.ufla.lfapp.persistence.DbAcess;
import com.ufla.lfapp.vo.GrammarParser;

public class MainActivity extends AppCompatActivity implements OnKeyboardStateChangedListener {

    private EditText inputGrammar, inputWord;
    private LFAppKeyboard mCustomKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.inputGrammar = (EditText) findViewById(R.id.inputGrammar);
        this.inputWord = (EditText) findViewById(R.id.inputWord);
        mCustomKeyboard = new LFAppKeyboard(this, R.id.lfappKeyboardView, R.xml.lfapp_keyboard);
        mCustomKeyboard.registerEditText(R.id.inputGrammar);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final EditText inputGrammar = (EditText) findViewById(R.id.inputGrammar);
        inputGrammar.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                MainActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams
                        .SOFT_INPUT_ADJUST_UNSPECIFIED);
                return false;
            }
        });
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        this.inputGrammar.setText(preferences.getString("inputGrammar", ""));
        inputWord.setText(preferences.getString("inputWord", ""));
        Intent intent = getIntent();
        if (intent != null
                && intent.getExtras() != null
                && intent.getExtras().getString("grammar") != null) {
            this.inputGrammar.setText(intent.getExtras().getString("grammar"));
        }
    }



    @Override
    public void OnDisplay(View currentview, KeyboardView currentKeyboard) {
        ScrollView mScroll = (ScrollView) findViewById(R.id.principal);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ABOVE,  currentKeyboard.getId());
        mScroll.setLayoutParams(params);
        mScroll.scrollTo(0, currentview.getBaseline()); //Scrolls to focused EditText

    }

    @Override
    public void OnHide(KeyboardView currentKeyboard) {
        ScrollView mScroll = (ScrollView) findViewById(R.id.principal);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mScroll.setLayoutParams(params);

    }

    @Override
    protected void onStop() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("inputGrammar", inputGrammar.getText().toString());
        editor.putString("inputWord", inputWord.getText().toString());
        editor.apply();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("inputGrammar", inputGrammar.getText().toString());
        editor.putString("inputWord", inputWord.getText().toString());
        editor.apply();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(mCustomKeyboard.isLFAppKeyboardVisible()) {
            mCustomKeyboard.hideLFAppKeyboard();
        } else {
//            super.onBackPressed();
//        }
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            dialog.cancel();
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.cancel();
                            break;
                    }
                }
            };
            //Solicita confirmação de saída do lfapp
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sair");
            builder.setMessage("Sair do LFApp?");
            builder.setPositiveButton("Sim", dialogClickListener);
            builder.setNegativeButton("Não", dialogClickListener);
            builder.show();
        }
    }

    public void insertLambda(View view) {
        int selection = inputGrammar.getSelectionStart();
        inputGrammar.setText(inputGrammar.getText()
                .insert(selection, " λ"));
        inputGrammar.setSelection(selection + 2);
    }

    public void insertArrow(View view) {
        int selection = inputGrammar.getSelectionStart();
        inputGrammar.setText(inputGrammar.getText()
                .insert(selection, " -> "));
        inputGrammar.setSelection(selection + 4);
    }

    public void insertPipe(View view) {
        int selection = inputGrammar.getSelectionStart();
        inputGrammar.setText(inputGrammar.getText()
                .insert(selection, " | "));
        inputGrammar.setSelection(selection + 3);
    }

    public void confirmGrammar(View view) {
        String txtGrammar = inputGrammar.getText().toString();
        String word = inputWord.getText().toString();
        Toast.makeText(this, "oi", Toast.LENGTH_LONG);
        if (!txtGrammar.isEmpty()) {
            StringBuilder reason = new StringBuilder();
            Toast.makeText(this, txtGrammar, Toast.LENGTH_LONG);
            if (GrammarParser.verifyInputGrammar(txtGrammar) &&
                    GrammarParser.inputValidate(txtGrammar, reason)) {
                new DbAcess(this).putGrammar(txtGrammar);
                Bundle params = new Bundle();
                params.putString("grammar", txtGrammar);
                params.putString("word", word);
                params.putInt("algorithm", Algorithm.NONE.getValue());
                Intent intent = new Intent(this, MenuActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            } else {
                if (reason.length() == 0) {
                    reason.append("Gramática inválida.");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Aviso");
                builder.setMessage(reason);
                builder.setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        }
    }

    public void startAboutActivity(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void startHistoricalGrammarsActivity(View view) {
        Intent intent = new Intent(this, HistoricalGrammarsActivity.class);
        startActivity(intent);
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
