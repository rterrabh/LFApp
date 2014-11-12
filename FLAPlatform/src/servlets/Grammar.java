package servlets;

import java.util.ArrayList;

public class Grammar {
	
	//attributes
	private String[] variables;
	private String[] terminals;
	private char initialSymbol;
	private ArrayList<Rule> rule;
	
	//builders
	public Grammar() {}
	
	public Grammar(String[] variables, String[] terminals, char initialSymbol, String[] rules ) {
		this.variables = variables;
		this.terminals = terminals;
		this.initialSymbol = initialSymbol;
		this.rule = new ArrayList<Rule>();
		Rule r = new Rule();
		String[] auxRule;
		for (String x : rules) {
			auxRule = x.split("->");			
			r.setleftSide(auxRule[0].trim());
			r.setrightSide(auxRule[1].trim());
			this.rule.add(new Rule(r));
		}
	}

	//methods
	
	//accessors
	
	public String[] getVariables() {
		return variables;
	}

	
	public void setVariables(String[] variables) {
		this.variables = variables;
	}

	public String[] getTerminals() {
		return terminals;
	}

	public void setTerminals(String[] terminals) {
		this.terminals = terminals;
	}

	public char getInitialSymbol() {
		return initialSymbol;
	}

	public void setInitialSymbol(char initialSymbol) {
		this.initialSymbol = initialSymbol;
	}

	public ArrayList<Rule> getRule() {
		return rule;
	}

	public void setRule(ArrayList<Rule> rule) {
		this.rule = rule;
	}
	
	//algorithms
	
	
}
