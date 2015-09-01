package com.lfapp.lfapp_01;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity extends ActionBarActivity {

    private Button button_OK, button_Lambda, button_Pipe, button_Arrow;
    private EditText inputGrammar;
    private String txtGrammar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //configura EditText InputGrammar
        this.inputGrammar = (EditText) findViewById(R.id.InputGrammar);

        //configura botão ->
        this.button_Arrow = (Button) findViewById(R.id.button_Arrow);
        button_Arrow.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
                String contentEditingArea = inputGrammar.getText().toString();
                String arrow = " -> ";
                inputGrammar.setText(contentEditingArea + arrow);
                inputGrammar.setSelection(inputGrammar.getText().length());
            }
        });

        //configura botão |
        this.button_Pipe = (Button) findViewById(R.id.button_Pipe);
        this.button_Pipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentEditingArea = inputGrammar.getText().toString();
                String arrow = " | ";
                inputGrammar.setText(contentEditingArea + arrow);
                inputGrammar.setSelection(inputGrammar.getText().length());
            }
        });

        //configura botão .
        this.button_Lambda = (Button) findViewById(R.id.button_Lambda);
        this.button_Lambda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentEditingArea = inputGrammar.getText().toString();
                String arrow = " .";
                inputGrammar.setText(contentEditingArea + arrow);
                inputGrammar.setSelection(inputGrammar.getText().length());
            }
        });

        //configura  botão OK
        this.button_OK = (Button) findViewById(R.id.button_OK);
        button_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGrammar = inputGrammar.getText().toString();
                Intent TrocaTela = new Intent(MainActivity.this, MainActivity2.class);
                if (!txtGrammar.isEmpty()) {
                    Bundle params = new Bundle();
                    params.putString("msg", txtGrammar);
                    TrocaTela.putExtras(params);
                }
                MainActivity.this.startActivity(TrocaTela);
                MainActivity.this.finish();
            }
        });

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
