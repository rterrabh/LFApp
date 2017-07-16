package com.ufla.lfapp.core.machine;

import java.io.Serializable;

/**
 * Created by carlos on 1/24/17.
 */

public class State
        implements Serializable, Comparable<State>  {

    private String name;

    public State(String name) {
        this.name = name;
    }

    public String getLabel() {
        if (this.name.charAt(0) == 'q') {
            int i = 1;
            int indAposInit = -1;
            int lenght = this.name.length();
            while (i < lenght && indAposInit == -1) {
                char c = this.name.charAt(i);
                if (c == '\'') {
                    indAposInit = i;
                } else if (!Character.isDigit(c)) {
                    return this.name;
                }
                i++;
            }
            if (indAposInit != -1) {
                while (i < lenght) {
                    char c = this.name.charAt(i);
                    if (c != '\'') {
                        return this.name;
                    }
                }
            }
            int lastDigit = (indAposInit != -1) ? indAposInit : lenght;
            String number = this.name.substring(1, lastDigit);
            String apos = this.name.substring(lastDigit);
            return String.format("q<sub>%s</sub>%s", number, apos);
        }
        return this.name;
    }

    public State copy() {
        return new State(name);
    }

    // MÉTODOS ACESSORES
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(State another) {
        return getName().compareTo(another.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        return name != null ? name.equals(state.name) : state.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getName();
    }

}
