package com.ufla.lfapp.vo.machine;

import java.util.Set;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class PushdownAutomaton extends Machine {


	private Set<String> stackAlphabet;
	private Set<TransitionFunctionPA> transitionFunction;


	public PushdownAutomaton() {
		this(new TreeSet<String>(), "", new TreeSet<String>(),
				new HashSet<String>(), new HashSet<TransitionFunctionPA>());
	}

	@Override
	public SortedSet<String> getAlphabet() {
		return null;
	}

	//Construtor base
	public PushdownAutomaton(SortedSet<String> states, String initialState,
							 SortedSet<String> finalStates, Set<String> stackAlphabet,
							 Set<TransitionFunctionPA> transitionFunction) {
		super(states, initialState, finalStates);
		this.stackAlphabet = stackAlphabet;
		this.transitionFunction = transitionFunction;
	}

//	public PushdownAutomaton(PushdownAutomaton automaton) {
//		this(automaton.getStates(),
//				automaton.getInitialState(), automaton.getFinalStates(),
//				automaton.getStackAlphabet(), automaton.getTransictionFunction());
//	}

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
