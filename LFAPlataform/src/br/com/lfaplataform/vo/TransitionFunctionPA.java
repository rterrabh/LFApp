package br.com.lfaplataform.vo;

public class TransitionFunctionPA extends TransitionFunction {
	
	private String stacking;
	private String pops;
	
	
	public TransitionFunctionPA() {
		super();
	}
	
	public TransitionFunctionPA(String currentState, String symbol,	String futureState, String stacking, String pops) {
		super(currentState, symbol, futureState);
		this.stacking = stacking;
		this.pops = pops;
	}
	
	public TransitionFunctionPA(TransitionFunctionPA transitionFunctionPA) {
		super(transitionFunctionPA);
		this.stacking = transitionFunctionPA.getStacking();
		this.pops = transitionFunctionPA.getPops();
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
