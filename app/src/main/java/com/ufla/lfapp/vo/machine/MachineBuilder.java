package com.ufla.lfapp.vo.machine;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Responsável por construir uma máquina.
 *
 * Created by carlos on 12/7/16.
 */
public abstract class MachineBuilder {

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
        for (State state: states) {
            if (state.getName().equals(stateName)) {
                return state;
            }
        }
        return null;
    }

    public void renameState(String oldName, String newName) {
        getState(oldName).setName(newName);
    }

    /**
     * Define o estado inicial da máquina.
     *
     * @param initialState estado inicial da máquina
     * @return próprio construtor de máquina
     */
    public MachineBuilder withInitialState(State initialState) {
        addState(initialState);
        this.initialState = initialState;
        return this;
    }

    /**
     * Adiciona um estado na máquina.
     *
     * @param state estado adicionado na máquina
     * @return próprio construtor de máquina
     */
    public MachineBuilder addState(State state) {
        states.add(state);
        return this;
    }

    /**
     * Adiciona uma coleção de estados na máquina.
     *
     * @param states coleção de estados adicionados na máquina
     * @return próprio construtor de máquina
     */
    public MachineBuilder addStates(Collection<State> states) {
        for (State state : states) {
            addState(state);
        }
        return this;
    }

    /**
     * Remove um estado da máquina.
     *
     * @param state estado removido da máquina
     * @return próprio construtor de máquina
     */
    public MachineBuilder removeState(State state) {
        if (state.equals(initialState)) {
            initialState = null;
        }
        finalStates.remove(state);
        states.remove(state);
        return this;
    }

    /**
     * Remove uma coleção de estados da máquina.
     *
     * @param states coleção de estados removidas da máquina
     * @return próprio construtor de máquina
     */
    public MachineBuilder removeStates(Collection<State> states) {
        for (State state : states) {
            removeState(state);
        }
        return this;
    }

    /**
     * Define um estado da máquina como final. Insere-o no conjunto dos estados finais. Se o
     * estado ainda não é um estado da máquina, este estado é adicionado a máquina.
     *
     * @param finalState estado final da máquina
     * @return próprio construtor de máquina
     */
    public MachineBuilder defineFinalState(State finalState) {
        addState(finalState);
        finalStates.add(finalState);
        return this;
    }

    /**
     * Define uma coleção de estados da máquina como finais. Insere-os no conjunto dos estados
     * finais. Se algum destes estados ainda não é um estado da máquina, este estado é adicionado
     * a máquina.
     *
     * @param finalStates coleção de estados finais adicionados na máquina
     * @return próprio construtor de máquina
     */
    public MachineBuilder addFinalStates(Collection<State> finalStates) {
        for (State finalState : finalStates) {
            defineFinalState(finalState);
        }
        return this;
    }

    /**
     * Remove um estado do conjunto dos estados finais da máquina.
     *
     * @param finalState estado final a ser removido do conjunto dos estados finais da máquina
     * @return próprio construtor de máquina
     */
    public MachineBuilder removeFinalState(State finalState) {
        finalStates.remove(finalState);
        return this;
    }

    /**
     * Remove uma coleção de estados do conjunto dos estados finais da máquina.
     *
     * @param finalStates coleção de estados que serão removidos do conjunto dos estados finais
     *                    da máquina
     * @return próprio construtor de máquina
     */
    public MachineBuilder removeFinalStates(Collection<State> finalStates) {
        for (State finalState : finalStates) {
            removeFinalState(finalState);
        }
        return this;
    }

}
