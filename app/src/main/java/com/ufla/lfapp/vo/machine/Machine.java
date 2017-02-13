package com.ufla.lfapp.vo.machine;

import java.io.Serializable;
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

	/**
	 * Estado inicial da máquina.
	 */
	protected State initialState;
	/**
	 * Conjunto dos estados finais da máquina.
	 */
	protected SortedSet<State> finalStates;


	/**
	 * Construtor vazio. Inicia os atributos agregados com objetos vazios. O estado inicial é
	 * definido como <code>null</code>.
	 */
	public Machine() {
		this(new TreeSet<State>(), null, new TreeSet<State>());
	}

	protected static SortedSet<State> copyStates(SortedSet<State> states) {
		SortedSet<State> copyStates = new TreeSet<>();
		if (states == null) {
			return copyStates;
		}
		for (State state: states) {
			copyStates.add(state.copy());
		}
		return copyStates;
	}
	/**
	 * Construtor base de máquina. Constrói uma máquina com seus 3 atributos. Se algum
	 * dos atributos agregados forem passados sem nenhum valor, null, será instanciado um objeto
	 * agregado vazio para o atributo.
	 *
	 * @param states estados da máquina
	 * @param initialState estado inicial da máquina
	 * @param finalStates conjunto dos estados finais da máquina
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

	private void initFinalStates(SortedSet<State> finalStates) {
		this.finalStates = new TreeSet<>();
		if (finalStates == null) {
			return;
		}
		for (State state: finalStates) {
			this.finalStates.add(getState(state.getName()));
		}
	}

	private void initStates(SortedSet<State> states) {
		this.states = new TreeSet<>();
		if (states == null) {
			return;
		}
		for (State state: states) {
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

	public Machine(SortedSet<State> states, State initialState, SortedSet<State> finalStates) {
		initStates(states);
		initInitialState(initialState);
		initFinalStates(finalStates);
	}

	/**
	 * Constrói uma cópia da máquina passada por parâmetro.
	 *
	 * @param machine máquina a ser copiada
     */
	public Machine(Machine machine) {
		this(machine.states, machine.initialState, machine.finalStates);
	}

	/**
	 * Verifica se o estado passado por parâmetro é o estado inicial da máquina.
	 *
	 * @param state estado a ser verificado se é o inicial da máquina
	 *
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
	 *
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
		StringBuilder sb = new StringBuilder();
		sb.append("Estados: ").append(states.toString())
				.append("\nAlfabeto: ").append(getAlphabet().toString())
				.append("\nEstados finais: ").append(finalStates.toString())
				.append("\nEstado inicial: ").append(initialState);
		return sb.toString();
	}
}
