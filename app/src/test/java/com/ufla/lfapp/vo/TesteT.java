package com.ufla.lfapp.vo;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by carlos on 12/14/16.
 */

public class TesteT {

    @Test
    public void test() {
        //System.out.println(format.format(data));
        Date data = new Date();

        SimpleDateFormat format = new SimpleDateFormat("dd");
        int ano = Integer.parseInt(format.format(data));
        System.out.println("ano " + ano);


    }
}
