package com.ufla.lfapp.activities;

import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ufla.lfapp.R;

public class AboutActivity extends AppCompatActivityContext {

    private GestureDetectorCompat mDetector;
    private boolean back = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTextViewTeamByHtml();
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        if(back) {
            onBackPressed();
        }
        return super.onTouchEvent(event);
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

    /**
     * Define a formatação do texto com os nomes dos responsáveis pelo aplicativo.
     */
    private void setTextViewTeamByHtml() {
        TextView textViewTeam = (TextView) findViewById(R.id.textViewTeam);
        assert textViewTeam != null;
        textViewTeam.setText(Html.fromHtml(getResources().getString(R.string.team_html)));
    }


    /**
     * Classe que identifica movimento na tela.
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private int swipe_Min_Distance = 100;
        private int swipe_Max_Distance = 350;
        private int swipe_Min_Velocity = 100;

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            final float xDistance = Math.abs(event1.getX() - event2.getX());
            velocityX = Math.abs(velocityX);
            if(xDistance > swipe_Min_Distance && xDistance < swipe_Max_Distance
                    && velocityX > swipe_Min_Velocity) {
                if (event1.getX() < event2.getX()) {
                    back = true;
                }
            }
            return true;
        }
    }

}
