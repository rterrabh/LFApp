package com.ufla.lfapp.vo;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

public abstract class Machine implements Serializable {

	protected Set<String> states; 
	protected Set<String> alphabet;
	protected String initialState;
	protected Set<String> finalStates;
	
	
	public Machine() {
		this(new HashSet<String>(), new HashSet<String>(),
				"", new HashSet<String>());
	}

	//Construtor base
	public Machine(Set<String> states, Set<String> alphabet,
				   String initialState, Set<String> finalStates) {
		super();
		this.states = states;
		this.alphabet = alphabet;
		this.initialState = initialState;
		this.finalStates = finalStates;
		if (this.states == null) {
			this.states = new HashSet<>();
		}
		if (this.alphabet == null) {
			this.alphabet = new HashSet<>();
		}
		if (this.finalStates == null) {
			this.finalStates = new HashSet<>();
		}
	}

	public Set<String> getStates() {
		return states;
	}

	public void setStates(Set<String> states) {
		this.states = states;
	}

	public Set<String> getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(Set<String> alphabet) {
		this.alphabet = alphabet;
	}

	public String getInitialState() {
		return initialState;
	}

	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}

	public Set<String> getFinalStates() {
		return finalStates;
	}

	public void setFinalStates(Set<String> finalStates) {
		this.finalStates = finalStates;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Estados: ")
				.append(states.toString())
				.append("\nAlfabeto: ")
				.append(alphabet.toString())
				.append("\nEstados finais: ")
				.append(finalStates.toString())
				.append("\nEstado inicial: ")
				.append(initialState);
		return sb.toString();
	}
}
