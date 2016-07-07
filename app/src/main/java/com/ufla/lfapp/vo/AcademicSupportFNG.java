package com.ufla.lfapp.vo;

import java.util.List;

/**
 * Created by root on 05/07/16.
 */
public class AcademicSupportFNG {

    private List<List<String>> orderVariables;

    public List<List<String>> getOrderVariables() {
        return orderVariables;
    }

    public void addListVariables(List<String> variables) {
        this.orderVariables.add(variables);
    }
}
