package vo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GrammarParser {
	
	private GrammarParser() {}
	
	public static Set<String> extractVariablesFromFull(String txt) {
		Set<String> Variables = new HashSet<>();
		
		//search for the variables
		String[] variables = new String[txt.length()];
		int j = 0;
		for (int i = 0; i < txt.length(); i++) {
			if (Character.isUpperCase(txt.charAt(i))) {
				variables[j] = Character.toString(txt.charAt(i));
				j++;
			}
		}
		
		for (String element : variables) {
			if (element != null)
				Variables.add(element);
		}
		
		return Variables;
	}
	
	public static Set<String> extractTerminalsFromFull(String txt) {
		Set<String> Terminals = new HashSet<>();
		
		//search for the terminals
		String[] terminals = new String[txt.length()];
		int j = 0;
		for (int i = 0; i < txt.length(); i++) {
				if (Character.isLowerCase(txt.charAt(i))) {
					terminals[j] = Character.toString(txt.charAt(i));
					j++;
			}
		}
		
		for (String element : terminals) {
			if (element != null)
				Terminals.add(element);
		}
		
		return Terminals;
	}
	
	public static Set<Rule> extractRulesFromFull(String txt) {
		Set<Rule> rule;
		rule = new HashSet<Rule>();
		Rule r = new Rule();
		
		//get the rules
		String[] rules = txt.split("\n");
		
		String[] auxRule;
		for (String x : rules) {
			auxRule = x.split("->");
			r.setleftSide(auxRule[0].trim());
			r.setrightSide(auxRule[1].trim());
			rule.add(new Rule(r));
		}	
		return rule;
	}
	
	public static char extractInitialSymbolFromFull(String txt) {
		return txt.charAt(0);
	}
	
	public static boolean compareInitialSymbolWithParameter(char initialSymbol, String rules) {
		for (int i = 0; i < rules.length(); i++) {
			if (initialSymbol == rules.charAt(i))
				return true;
		}		
		return false;
	}
	
	public static boolean compareSymbolWithParameter(String variables, String rules) {
		int i = 0;
		boolean search = false;
		while (i < variables.length()) {
			for (int j = 0; j < rules.length() && search == false; j++) {
				if (variables.charAt(i) == rules.charAt(j))
					search = true;
			}
			if (search == false)
				return false;
			else
				search = false;
			i++;
		}
		return true;
	}
	
	public static Set<String> formatTerminals(String terminals) {
		Set<String> terminalsFormated = new HashSet<>();
		for (int i = 0; i < terminals.length(); i++) {
			if (Character.isLowerCase(terminals.charAt(i)))
				terminalsFormated.add(Character.toString(terminals.charAt(i)));
		}
		return terminalsFormated;
	}
	
	public static Set<String> formatVariables(String variables) {
		Set<String> variablesFormated = new HashSet<>();
		
		for (int i = 0; i < variables.length(); i++) {
			if (Character.isUpperCase(variables.charAt(i)))
				variablesFormated.add(Character.toString(variables.charAt(i)));
		}
		return variablesFormated;
	}
	
	public static char formatInitialSymbol(char initialSymbol) {
		return Character.toUpperCase(initialSymbol);
	}
	
	public static Grammar removeInitialSymbolRecursive(Grammar g) {
		char initialSymbol = g.getInitialSymbol();
		String auxLeft = new String();
		String auxRight = new String();
		Iterator it = g.getRule().iterator();
		
		
		for (Rule element : g.getRule()) {
			if (element.getleftSide().equals(Character.toString(initialSymbol))) {
				if (element.getrightSide().contains(Character.toString(initialSymbol))) {
					auxLeft = element.getleftSide();
					auxRight = element.getrightSide();
				}
			}
		}
		g.insertRule(Character.toString(initialSymbol) + "'", Character.toString(initialSymbol));
		g.insertRule(Character.toString(initialSymbol), auxRight.replace(initialSymbol, ' '));
		g.removeRule(auxLeft, auxRight);
		/*String teste = aux.replace(initialSymbol,' ');
		//Iterator it = g.getRule().iterator();
		while (it.hasNext()) {
			Rule r =  (Rule) it.next(); 
			if (r.getrightSide().equals(aux))
				it.remove();
		}*/
		return g;
	}
}
