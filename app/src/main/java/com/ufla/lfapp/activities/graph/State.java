package com.ufla.lfapp.activities.graph;

/**
 * Created by carlos on 9/21/16.
 * Representa um estado em um autômato ou máquina de estados.
 */
public class State {

    private static int stateNumber = 0;
    //private static Queue<Integer> stateNumbersFree = new PriorityQueue<>();
    private String name;

    public State(String name) {
        this.name = name;
    }

    public State() {
        this.name = "q" + stateNumber;
        stateNumber++;
//        if (stateNumbersFree.isEmpty()) {
//            this.name = "q" + stateNumber;
//            stateNumber++;
//        } else {
//            this.name = "q" + stateNumbersFree.poll();
//        }

    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLabel(String label) {
        this.name = label;
    }


}
