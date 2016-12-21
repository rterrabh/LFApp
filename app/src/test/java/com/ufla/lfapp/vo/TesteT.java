package com.ufla.lfapp.vo;

import org.junit.Test;

/**
 * Created by carlos on 12/14/16.
 */

public class TesteT {

    @Test
    public void test() throws InterruptedException {
        try {
            Teste teste = new Teste();
            teste.onDown();
            Thread.sleep(300);
            System.out.println(teste.t.isAlive());
            teste.onDown();
            teste.onDown();
            Thread.sleep(100);
            teste.onDown();
            Thread.sleep(300);
            teste.onDown();
            teste.onDown();
            Thread.sleep(300);
            teste.onDown();
            Thread.sleep(300);
            teste.onDown();
            Thread.sleep(300);
            System.out.println(teste.onOneClickCount + "," + teste.onDoubleTapCount);
        } catch (InterruptedException e) {

        }
    }
}
