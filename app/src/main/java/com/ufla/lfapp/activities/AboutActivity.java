package com.ufla.lfapp.activities;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ufla.lfapp.R;

public class AboutActivity extends AppCompatActivity {

    private GestureDetectorCompat mDetector;
    private boolean back = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //setTextViewTeam();
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        if(back) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onTouchEvent(event);
    }

    /**
     * Retorna para a activity principal
     * @param : view a view da 
     */
    public void close(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Define a formatação do texto com os nomes dos resonsáveis pelo aplicativo.
     */
    private void setTextViewTeam() {
        TextView textViewTeam = (TextView) findViewById(R.id.textViewTeam);
        if(textViewTeam != null) {
            String[] lines = getResources().getStringArray(R.array.team);
            StringBuilder sbTeam = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                sbTeam.append(lines[i]);
                if (i % 2 == 1 && i < lines.length-1) {
                    sbTeam.append("\n");
                }
            }
            SpannableString styledText = new SpannableString(sbTeam);
            int indice = 0;
            for (int i = 0; i < lines.length; i++) {
                if (i % 2 == 0) {
                    styledText.setSpan(new StyleSpan(R.style.equipe_nomes), indice,
                            indice + lines[i].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    styledText.setSpan(new StyleSpan(R.style.equipe_emails), indice,
                            indice + lines[i].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    indice++;
                }
                indice += lines[i].length();
            }
            textViewTeam.setText(styledText, TextView.BufferType.SPANNABLE);
        }
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
