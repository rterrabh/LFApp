package com.ufla.lfapp.vo;

import java.util.Set;
import java.util.HashSet;

public class PushdownAutomaton extends Machine {


	private Set<String> stackAlphabet;
	private Set<TransitionFunctionPA> transitionFunction;


	public PushdownAutomaton() {
		this(new HashSet<String>(), new HashSet<String>(), "", new HashSet<String>(),
				new HashSet<String>(), new HashSet<TransitionFunctionPA>());
	}

	//Construtor base
	public PushdownAutomaton(Set<String> states, Set<String> alphabet, String initialState,
							 Set<String> finalStates, Set<String> stackAlphabet,
							 Set<TransitionFunctionPA> transitionFunction) {
		super(states, alphabet, initialState, finalStates);
		this.stackAlphabet = stackAlphabet;
		this.transitionFunction = transitionFunction;
	}

	public PushdownAutomaton(PushdownAutomaton automaton) {
		this(automaton.getStates(), automaton.getAlphabet(),
				automaton.getInitialState(), automaton.getFinalStates(),
				automaton.getStackAlphabet(), automaton.getTransictionFunction());
	}

	public Set<String> getStackAlphabet() {
		return stackAlphabet;
	}


	public void setStackAlphabet(Set<String> stackAlphabet) {
		this.stackAlphabet = stackAlphabet;
	}


	public Set<TransitionFunctionPA> getTransictionFunction() {
		return transitionFunction;
	}


	public void setTransictionFunction(Set<TransitionFunctionPA> transictionFunction) {
		this.transitionFunction = transictionFunction;
	}

}
