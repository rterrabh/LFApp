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
	protected SortedSet<String> states;
	/**
	 * Estado inicial da máquina.
	 */
	protected String initialState;
	/**
	 * Conjunto dos estados finais da máquina.
	 */
	protected SortedSet<String> finalStates;


	/**
	 * Construtor vazio. Inicia os atributos agregados com objetos vazios. O estado inicial é
	 * definido como <code>null</code>.
	 */
	public Machine() {
		this(new TreeSet<String>(), null, new TreeSet<String>());
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
		this.states = (states == null) ? new TreeSet<String>() : new TreeSet<>(states);
		this.initialState = initialState;
		this.finalStates = (finalStates == null) ? new TreeSet<String>() :
				new TreeSet<>(finalStates);
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
		return (state == null) ? false : finalStates.contains(state);
	}

	// MÉTODOS ACESSORES
	public SortedSet<String> getStates() {
		return new TreeSet<>(states);
	}

	public String getInitialState() {
		return initialState;
	}

	public SortedSet<String> getFinalStates() {
		return new TreeSet<>(finalStates);
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
