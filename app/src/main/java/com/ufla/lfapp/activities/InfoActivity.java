package com.ufla.lfapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ufla.lfapp.R;

/**
 * Created by juventino on 21/03/16.
 */
public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Button infoButton = (Button) findViewById(R.id.button_voltar);
        assert infoButton != null;
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trocaTela = new Intent(InfoActivity.this, MainActivity.class);
                InfoActivity.this.startActivity(trocaTela);
                InfoActivity.this.finish();
            }
        });
        System.out.println("onCreate-InfoActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop-InfoActivity");
    }

    @Override
    protected void onDestroy() {
        System.out.println("onDestroy-InfoActivity");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        System.out.println("onPause-InfoActivity");
        super.onPause();
    }

    @Override
    protected void onResume() {
        System.out.println("onResume-InfoActivity");
        super.onResume();
    }

    @Override
    protected void onStart() {
        System.out.println("onStart-InfoActivity");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        System.out.println("onRestart-InfoActivity");
        super.onRestart();
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
