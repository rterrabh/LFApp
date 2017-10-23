package com.ufla.lfapp.core.machine;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Responsável por construir uma máquina.
 * <p>
 * Created by carlos on 12/7/16.
 */
public abstract class MachineBuilder<MachineType extends MachineBuilder> {

    /**
     * Conjunto dos estados da máquina.
     */
    protected SortedSet<State> states;
    /**
     * Estado inicial da máquina.
     */
    protected State initialState;
    /**
     * Conjunto dos estados finais da máquina.
     */
    protected SortedSet<State> finalStates;


    /**
     * Constrói uma máquina vazia.
     */
    public MachineBuilder() {
        this.states = new TreeSet<>();
        this.finalStates = new TreeSet<>();
    }

    /**
     * Constrói uma máquina com os atributos da máquina passada por parâmetro.
     *
     * @param machine máquina usada para construir a máquina deste construtor de máquina
     */
    public MachineBuilder(Machine machine) {
        this.states = machine.getStates();
        this.initialState = machine.getInitialState();
        this.finalStates = machine.getFinalStates();
    }

    public State getState(String stateName) {
        for (State state : states) {
            if (state.getName().equals(stateName)) {
                return state;
            }
        }
        return null;
    }


    /**
     * Define o estado inicial da máquina.
     *
     * @param initialState estado inicial da máquina
     * @return próprio construtor de máquina
     */
    public MachineType withInitialState(State initialState) {
        addState(initialState);
        this.initialState = initialState;
        return (MachineType) this;
    }

    /**
     * Adiciona um estado na máquina.
     *
     * @param state estado adicionado na máquina
     * @return próprio construtor de máquina
     */
    protected MachineType addState(State state) {
        states.add(state);
        return (MachineType) this;
    }

    /**
     * Remove um estado da máquina.
     *
     * @param state estado removido da máquina
     * @return próprio construtor de máquina
     */
    protected MachineType removeState(State state) {
        if (state.equals(initialState)) {
            initialState = null;
        }
        finalStates.remove(state);
        states.remove(state);
        return (MachineType) this;
    }


    /**
     * Define um estado da máquina como final. Insere-o no conjunto dos estados finais. Se o
     * estado ainda não é um estado da máquina, este estado é adicionado a máquina.
     *
     * @param finalState estado final da máquina
     * @return próprio construtor de máquina
     */
    public MachineType defineFinalState(State finalState) {
        addState(finalState);
        finalStates.add(finalState);
        return (MachineType) this;
    }

}
