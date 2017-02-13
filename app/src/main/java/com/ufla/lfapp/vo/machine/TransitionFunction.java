package com.ufla.lfapp.vo.machine;

import java.io.Serializable;

public class TransitionFunction implements Serializable, Comparable<TransitionFunction> {
	
	protected State currentState;
	protected String symbol;
	protected State futureState;
	
	
//	public TransitionFunction() {
//		this("", "", "");
//	}
	
//	public TransitionFunction(String currentState, String symbol, String futureState) {
//		super();
//		this.currentState = currentState;
//		this.symbol = symbol;
//		this.futureState = futureState;
//	}

	public boolean isCurrentState(State currentState) {
		return this.currentState.equals(currentState);
	}

	public boolean isSymbol(String symbol) {
		return this.symbol.equals(symbol);
	}

	public boolean isFutureState(State futureState) {
		return this.futureState.equals(futureState);
	}

	public TransitionFunction(State currentState, String symbol, State futureState) {
		super();
		this.currentState = currentState;
		this.symbol = symbol;
		this.futureState = futureState;
	}
	
	public TransitionFunction(TransitionFunction transitionFunction) {
		this(new State(transitionFunction.currentState.getName()), transitionFunction.symbol,
				new State(transitionFunction.futureState.getName()));
	}

	public TransitionFunction copy() {
		return new TransitionFunction(new State(currentState.getName()), symbol,
				new State(futureState.getName()));
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public State getFutureState() {
		return futureState;
	}

	public void setFutureState(State futureState) {
		this.futureState = futureState;
	}

	@Override
	public String toString() {
		return "(" + this.getCurrentState() + ", " + this.getSymbol() + ") -> (" +
				this.getFutureState() + ")";
	}

	@Override
	public int compareTo(TransitionFunction another) {
		if (!currentState.equals(another.currentState)) {
			return currentState.compareTo(another.currentState);
		}
		if (!symbol.equals(another.symbol)) {
			return symbol.compareTo(another.symbol);
		}
		return futureState.compareTo(another.futureState);
	}
}
