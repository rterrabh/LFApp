package com.ufla.lfapp.vo.machine;

import java.io.Serializable;

public class TransitionFunctionPA extends TransitionFunction implements Serializable {
	
	private String stacking;
	private String pops;
	
	
	public TransitionFunctionPA() {
		this("", "", "", "", "");
	}
	
	public TransitionFunctionPA(String currentState, String symbol,	String futureState,
								String stacking, String pops) {
		super(currentState, symbol, futureState);
		this.stacking = stacking;
		this.pops = pops;
	}
	
	public TransitionFunctionPA(TransitionFunctionPA transitionFunctionPA) {
		this(transitionFunctionPA.getCurrentState(), transitionFunctionPA.getSymbol(),
				transitionFunctionPA.getFutureState(), transitionFunctionPA.getStacking(),
				transitionFunctionPA.getPops());
	}	

	public String getStacking() {
		return stacking;
	}

	public void setStacking(String stacking) {
		this.stacking = stacking;
	}

	public String getPops() {
		return pops;
	}

	public void setPops(String pops) {
		this.pops = pops;
	}

	@Override
	public String toString() {
		return "δ(" + this.getCurrentState() + ", " + this.getSymbol() + ", " + this.getPops() + ") = {[" +
				this.getFutureState() + ", " + this.getStacking() + "]}";
	}

	
	

	
	
	
	
	
	
	
	
}
