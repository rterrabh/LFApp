package br.com.lfaplataform.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Grammar implements Cloneable {

	// attributes
	private Set<String> variables;
	private Set<String> terminals;
	private String initialSymbol;
	private Set<Rule> rules;

	// builders
	public Grammar() {
		this.variables = new HashSet<>();
		this.terminals = new HashSet<>();
		this.initialSymbol = new String();
		this.rules = new HashSet<>();
	}

	public Grammar(String[] variables, String[] terminals,
			String initialSymbol, String[] rules) {

		// assigns variables
		this.variables = new HashSet<>();
		for (String element : variables) {
			this.variables.add(element);
		}
		// assigns terminals
		this.terminals = new HashSet<>();
		for (String element : terminals) {
			this.terminals.add(element);
		}
		// assigns initial symbol
		this.initialSymbol = initialSymbol;
		// assigns rules
		this.rules = new HashSet<Rule>();
		Rule r = new Rule();
		String[] auxRule;
		for (String x : rules) {
			auxRule = x.split("->");
			r.setLeftSide(auxRule[0].trim());
			String[] rulesOnTheRightSide = auxRule[1].split(" | ");
			for (String production : rulesOnTheRightSide) {
				production = production.trim();
				if (!production.isEmpty() && !production.equals("|")) {
					r.setRightSide(production);
					this.rules.add(new Rule(r));
				}
			}
		}
	}

	public Grammar(String txt) {
		// search for the initial symbol
		String initialSymbol = GrammarParser.extractInitialSymbolFromFull(txt);

		// assigns variables
		this.variables = GrammarParser.extractVariablesFromFull(txt);

		// assigns terminals
		this.terminals = GrammarParser.extractTerminalsFromFull(txt);

		// assign initial symbol
		this.initialSymbol = initialSymbol;

		// assigns rules
		this.rules = GrammarParser.extractRulesFromFull(txt);

	}

	// methods
	public Set<String> getVariables() {
		return variables;
	}

	public void setVariables(Set<String> set) {
		// this.variables.addAll(set);
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

	public Set<Rule> getRules() {
		return rules;
	}

	public void setRule(Set<Rule> set) {
		this.rules = set;
	}
	
	public void insertVariable(String newVariable) {
		this.variables.add(newVariable);
	}
	
	public void removeVariable(String variable) {
		this.variables.remove(variable);
	}
	
	public void insertTerminal(String newTerminal) {
		this.terminals.add(newTerminal);
	}
	
	public void removeTerminal(String terminal) {
		this.terminals.remove(terminal);
	}

	public void insertRule(String leftSide, String rightSide) {
		Rule r = new Rule();
		r.setLeftSide(leftSide);
		r.setRightSide(rightSide);
		System.out.println(r.getLeftSide() + " " + r.getRightSide());
		this.rules.add(new Rule(r));
		for (Rule element : rules) {
			System.out.println(element.getLeftSide() + "->"
					+ element.getRightSide());
		}
	}

	public void removeRule(String leftSide, String rightSide) {
		Rule r = new Rule();
		r.setLeftSide(leftSide);
		r.setRightSide(rightSide);
		this.rules.remove(r);
	}

	@Override
	public Object clone() {
		Grammar gc = new Grammar();
		gc.setInitialSymbol(initialSymbol);
		gc.setVariables(new HashSet<String>(this.variables));
		gc.setTerminals(new HashSet<String>(this.terminals));

		Set<Rule> rules = new HashSet<Rule>();
		for (Rule r : this.rules) {
			rules.add((Rule)r.clone());
		}
		gc.setRule(rules);
		return gc;
	}

	// algorithms

}
