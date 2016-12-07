package com.ufla.lfapp.vo;

import java.io.Serializable;

public class TransitionFunction implements Serializable {
	
	protected String currentState;
	protected String symbol;
	protected String futureState;
	
	
	public TransitionFunction() {
		this("", "", "");
	}
	
	public TransitionFunction(String currentState, String symbol, String futureState) {
		super();
		this.currentState = currentState;
		this.symbol = symbol;
		this.futureState = futureState;
	}
	
	public TransitionFunction(TransitionFunction transitionFunction) {
		this(transitionFunction.getCurrentState(), transitionFunction.getSymbol(),
				transitionFunction.getFutureState());
	}

	public TransitionFunction copy() {
		TransitionFunction t = new TransitionFunction();
		t.currentState = currentState;
		t.symbol = symbol;
		t.futureState = futureState;
		return t;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getFutureState() {
		return futureState;
	}

	public void setFutureState(String futureState) {
		this.futureState = futureState;
	}

	@Override
	public String toString() {
		return "(" + this.getCurrentState() + ", " + this.getSymbol() + ") -> (" +
				this.getFutureState() + ")";
	}	
}
