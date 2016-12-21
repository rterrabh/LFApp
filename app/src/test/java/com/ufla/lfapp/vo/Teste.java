package com.ufla.lfapp.vo;

/**
 * Created by carlos on 12/14/16.
 */

public class Teste {

    Thread t;
    int onOneClickCount;
    int onDoubleTapCount;

    public Teste() {
        t = getThread();
    }

    public Thread getThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(250);
                    onOneClick();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean onOneClick() {
        onOneClickCount++;
        return true;
    }

    public boolean onDown() {
        if (t.isAlive()) {
            t.interrupt();
            onDoubleTap();
        } else {
            t  = getThread();
            t.start();
        }

        return true;
    }

    public boolean onDoubleTap() {
        onDoubleTapCount++;
        return true;
    }



}
