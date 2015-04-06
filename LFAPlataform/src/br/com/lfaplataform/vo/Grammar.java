package br.com.lfaplataform.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Grammar implements Cloneable {

	// attributes
	private Set<String> variables;
	private Set<String> terminals;
	private String initialSymbol;
	private Set<Rule> rule;

	// builders
	public Grammar() {
		this.variables = new HashSet<>();
		this.terminals = new HashSet<>();
		this.initialSymbol = new String();
		this.rule = new HashSet<>();
	}

	public Grammar(String[] variables, String[] terminals, String initialSymbol,
			String[] rules) {
		
		//assigns variables
		this.variables = new HashSet<>();
		for (String element : variables) {
			this.variables.add(element);
		}
		//assigns terminals
		this.terminals = new HashSet<>();
		for (String element : terminals) {
			this.terminals.add(element);
		}
		//assigns initial symbol 
		this.initialSymbol = initialSymbol;
		//assigns rules
		this.rule = new HashSet<Rule>();
		Rule r = new Rule();
		String[] auxRule;
		for (String x : rules) {
			auxRule = x.split("->");
			r.setleftSide(auxRule[0].trim());
			String[] rulesOnTheRightSide = auxRule[1].split(" | ");
			for (String production : rulesOnTheRightSide) {
				production = production.trim();
				if (!production.isEmpty() && !production.equals("|")) {
					r.setrightSide(production);
					this.rule.add(new Rule(r));
				}
			}
		}				
	}
	
	public Grammar(String txt) {				
				//search for the initial symbol
				String initialSymbol = GrammarParser.extractInitialSymbolFromFull(txt);
				
				//assigns variables
				this.variables = GrammarParser.extractVariablesFromFull(txt);
				
				//assigns terminals
				this.terminals = GrammarParser.extractTerminalsFromFull(txt);
				
				//assign initial symbol
				this.initialSymbol = initialSymbol;
				
				//assigns rules
				this.rule = GrammarParser.extractRulesFromFull(txt);
				
					
	}
	// methods

	// accessors

	public Set<String> getVariables() {
		return variables;
	}

	public void setVariables(Set<String> set) {
		//this.variables.addAll(set);
		this.variables = set;
	}

	public Set<String> getTerminals() {
		return terminals;
	}

	public void setTerminals(Set<String> set) {
		this.terminals = set;
	}

	public String getInitialSymbol() {
		return initialSymbol;
	}

	public void setInitialSymbol(String string) {
		this.initialSymbol = string;
	}

	public Set<Rule> getRule() {
		return rule;
	}

	public void setRule(Set<Rule> set) {
		this.rule = set;
	}
	
	public void insertRule(String leftSide, String rightSide) {
		Rule r = new Rule();
		r.setleftSide(leftSide);
		r.setrightSide(rightSide);	
		System.out.println(r.getleftSide() + " " + r.getrightSide());
		this.rule.add(new Rule(r));
		for (Rule element : rule) {
			System.out.println(element.getleftSide() + "->" + element.getrightSide());
		}
	}
	
	public void removeRule(String leftSide, String rightSide) {
		Rule r = new Rule();
		r.setleftSide(leftSide);
		r.setrightSide(rightSide);
		this.rule.remove(r);
	}
	
	/*
	@Override
	public Object clone(Grammar g) throws CloneNotSupportedException {
		Grammar g1 = new Grammar();
		g1.setInitialSymbol(g.getInitialSymbol());
		g1.setVariables(new HashSet<String>(g.getVariables()));
		g1.setTerminals(new HashSet<String>(g.getTerminals()));
		
		Set<Rule> rules = new HashSet<Rule>();
		for (Rule r : g.getRule()){
			rules.add(new Rule(r.getleftSide(),r.getrightSide()));
		}
		g1.setRule(rules);
		return g1;
	}*/

	// algorithms
	

}
