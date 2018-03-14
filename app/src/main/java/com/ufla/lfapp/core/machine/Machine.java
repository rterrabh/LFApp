package com.ufla.lfapp.core.machine;

import android.support.v4.util.Pair;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.ResourcesContext;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Representa uma máquina abstrata.
 */
public abstract class Machine implements Serializable {

    /**
     * Conjunto dos estados da máquina.
     */
    protected SortedSet<State> states;

    public void setStates(SortedSet<State> states) {
        this.states = states;
    }

    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

    public void setFinalStates(SortedSet<State> finalStates) {
        this.finalStates = finalStates;
    }

    /**
     * Estado inicial da máquina.
     */
    protected State initialState;
    /**
     * Conjunto dos estados finais da máquina.
     */
    protected SortedSet<State> finalStates;
    protected Long id;
    protected String label;
    protected Date creationDate;
    protected Integer contUses = 1;

    public abstract Set<? extends TransitionFunction> getTransitionFunctionsGen();

    public abstract MachineType getMachineType();

    /**
     * Construtor vazio. Inicia os atributos agregados com objetos vazios. O estado inicial é
     * definido como <code>null</code>.
     */
    public Machine() {
        this(new TreeSet<State>(), null, new TreeSet<State>());
    }

    /**
     * Construtor base de máquina. Constrói uma máquina com seus 3 atributos. Se algum
     * dos atributos agregados forem passados sem nenhum valor, null, será instanciado um objeto
     * agregado vazio para o atributo.
     *
     * @param states       estados da máquina
     * @param initialState estado inicial da máquina
     * @param finalStates  conjunto dos estados finais da máquina
     */
    public Machine(SortedSet<String> states, String initialState, SortedSet<String> finalStates) {
        this.states = new TreeSet<>();
        if (states != null) {
            for (String state : states) {
                this.states.add(new State(state));
            }
        }
        this.initialState = new State(initialState);
        this.finalStates = new TreeSet<>();
        if (finalStates != null) {
            for (String state : finalStates) {
                this.finalStates.add(new State(state));
            }
        }
    }

    public Machine(SortedSet<State> states, State initialState, SortedSet<State> finalStates) {
        initStates(states);
        initInitialState(initialState);
        initFinalStates(finalStates);
    }

    public Map<Pair<State, State>, StringBuilder> getTransitionsForDotLang() {
        Set<? extends TransitionFunction> transitionFunctions = getTransitionFunctions();
        Map<Pair<State, State>, StringBuilder> transitionsDot = new HashMap<>();
        for (TransitionFunction t : transitionFunctions) {
            Pair<State, State> statePair = t.getPairState();
            String label = t.getLabel();
            StringBuilder actualLabel = transitionsDot.get(statePair);
            if (actualLabel == null) {
                transitionsDot.put(statePair, new StringBuilder(label));
            } else {
                transitionsDot.get(statePair)
                        .append("\\n")
                        .append(label);
            }
        }
        return transitionsDot;
    }

    /**
     * Constrói uma cópia da máquina passada por parâmetro.
     *
     * @param machine máquina a ser copiada
     */
    public Machine(Machine machine) {
        this(machine.states, machine.initialState, machine.finalStates);
    }

    public abstract Set<? extends TransitionFunction> getTransitionFunctions();


    public State getState(String stateName) {
        for (State state : states) {
            if (state.getName().equals(stateName)) {
                return state;
            }
        }
        return null;
    }

    public void renameState(String oldName, String newName) {
        getState(oldName).setName(newName);
    }

    private void initFinalStates(SortedSet<State> finalStates) {
        this.finalStates = new TreeSet<>();
        if (finalStates == null) {
            return;
        }

        for (State state : finalStates) {
            this.finalStates.add(getState(state.getName()));
        }
    }

    private void initStates(SortedSet<State> states) {
        this.states = new TreeSet<>();
        if (states == null) {
            return;
        }
        for (State state : states) {
            if (state == null) {
                continue;
            }
            this.states.add(new State(state.getName()));
        }
    }

    private void initInitialState(State initialState) {
        if (initialState != null) {
            this.initialState = getState(initialState.getName());
        }
    }

    /**
     * Verifica se o estado passado por parâmetro é o estado inicial da máquina.
     *
     * @param state estado a ser verificado se é o inicial da máquina
     * @return <code>true</code>, se o estado <code>state</code> é o estado inicial da máquina,
     * caso contrário <code>false</code>
     */
    public boolean isInitialState(String state) {
        return isInitialState(new State(state));
    }

    public boolean isInitialState(State state) {
        return (state == null) ? false : state.equals(initialState);
    }

    /**
     * Verifica se o estado passado por parâmetro é um estado final da máquina.
     *
     * @param state estado a ser verificado se é um estado final da máquina
     * @return <code>true</code>, se o estado <code>state</code> pertence ao conjunto dos estados
     * finais da máquina, caso contrário <code>false</code>
     */
    public boolean isFinalState(String state) {
        return isFinalState(new State(state));
    }

    public boolean isFinalState(State state) {
        return (state == null) ? false : finalStates.contains(state);
    }

    // MÉTODOS ACESSORES
    public SortedSet<State> getStates() {
        return states;
    }

    public State getInitialState() {
        return initialState;
    }

    public SortedSet<State> getFinalStates() {
        return finalStates;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Integer getContUses() {
        return contUses;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContUses(Integer contUses) {
        this.contUses = contUses;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Constrói e retorna o conjunto de símbolos reconhecidos pela máquina, o alfabeto da máquina.
     *
     * @return conjunto de símbolos reconhecidos pela máquina, o alfabeto da máquina.
     */
    public abstract SortedSet<String> getAlphabet();

    /**
     * Converte a máquina em uma <code>string</code> com seus atributos.
     *
     * @return <code>string</code> com atributos de identificação da máquina
     */
    @Override
    public String toString() {
        String[] parameters = ResourcesContext.getString(
                R.string.machine_to_string_parameters).split(",");
        return new StringBuilder(parameters[0])
                .append(": ")
                .append(states.toString())
                .append('\n')
                .append(parameters[1])
                .append(": ")
                .append(getAlphabet())
                .append('\n')
                .append(parameters[2])
                .append(": ")
                .append(getFinalStates())
                .append('\n')
                .append(parameters[3])
                .append(": ")
                .append(getInitialState()).toString();
    }

    public String toStringTest() {
        return new StringBuilder("States")
                .append(": ")
                .append(states.toString())
                .append('\n')
                .append("Alphabet")
                .append(": ")
                .append(getAlphabet())
                .append('\n')
                .append("FinalStates")
                .append(": ")
                .append(getFinalStates())
                .append('\n')
                .append("InitialState")
                .append(": ")
                .append(getInitialState()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Machine machine = (Machine) o;

        if (states != null ? !states.equals(machine.states) : machine.states != null) return false;
        if (initialState != null ? !initialState.equals(machine.initialState) : machine.initialState != null)
            return false;
        return finalStates != null ? finalStates.equals(machine.finalStates) : machine.finalStates == null;

    }

    @Override
    public int hashCode() {
        int result = states != null ? states.hashCode() : 0;
        result = 31 * result + (initialState != null ? initialState.hashCode() : 0);
        result = 31 * result + (finalStates != null ? finalStates.hashCode() : 0);
        return result;
    }
}
