package vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Grammar {

	// attributes
	private Set<String> variables;
	private Set<String> terminals;
	private char initialSymbol;
	private Set<Rule> rule;

	// builders
	public Grammar() {
	}

	public Grammar(String[] variables, String[] terminals, char initialSymbol,
			String[] rules) {
		this.variables = new HashSet<>();
		for (String element : variables) {
			this.variables.add(element);
		}
		this.terminals = new HashSet<>();
		for (String element : terminals) {
			this.terminals.add(element);
		}
		this.initialSymbol = initialSymbol;
		this.rule = new HashSet<Rule>();
		Rule r = new Rule();
		String[] auxRule;
		for (String x : rules) {
			auxRule = x.split("->");
			r.setleftSide(auxRule[0].trim());
			r.setrightSide(auxRule[1].trim());
			this.rule.add(new Rule(r));
		}
	}
	
	public Grammar(String txt) {
		//get the rules
				String[] rules = txt.split("\n");
				
				//search for the initial symbol
				char initialSymbol = ' ';
				initialSymbol = txt.charAt(0);
				
				//search for the terminals
				String[] terminals = new String[txt.length()];
				int j = 0;
				for (int i = 0; i < txt.length(); i++) {
						if (Character.isLowerCase(txt.charAt(i))) {
							terminals[j] = Character.toString(txt.charAt(i));
							j++;
					}
				}
				
				//search for the variables
				String[] variables = new String[txt.length()];
				j = 0;
				for (int i = 0; i < txt.length(); i++) {
					if (Character.isUpperCase(txt.charAt(i))) {
						variables[j] = Character.toString(txt.charAt(i));
						j++;
					}
				}
				
				this.variables = new HashSet<>();
				for (String element : variables) {
					this.variables.add(element);
				}
				this.terminals = new HashSet<>();
				for (String element : terminals) {
					this.terminals.add(element);
				}
				this.initialSymbol = initialSymbol;
				this.rule = new HashSet<Rule>();
				Rule r = new Rule();
				String[] auxRule;
				for (String x : rules) {
					auxRule = x.split("->");
					r.setleftSide(auxRule[0].trim());
					r.setrightSide(auxRule[1].trim());
					this.rule.add(new Rule(r));
				}				
	}
	// methods

	// accessors

	public Set<String> getVariables() {
		return variables;
	}

	public void setVariables(String variables) {
		this.variables.add(variables);
	}

	public Set<String> getTerminals() {
		return terminals;
	}

	public void setTerminals(String terminals) {
		this.terminals.add(terminals);
	}

	public char getInitialSymbol() {
		return initialSymbol;
	}

	public void setInitialSymbol(char initialSymbol) {
		this.initialSymbol = initialSymbol;
	}

	public Set<Rule> getRule() {
		return rule;
	}

	public void setRule(Set<Rule> rule) {
		this.rule = rule;
	}

	// algorithms
	

}
