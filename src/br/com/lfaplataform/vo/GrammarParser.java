package br.com.lfaplataform.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GrammarParser {

	private GrammarParser() {
	}

	public static Set<String> extractVariablesFromFull(String txt) {
		Set<String> Variables = new HashSet<>();

		// search for the variables
		String[] variables = new String[txt.length()];
		int j = 0;
		for (int i = 0; i < txt.length(); i++) {
			if (Character.isUpperCase(txt.charAt(i))) {
				String variable = Character.toString(txt.charAt(i));				
				if (i + 1 < txt.length() && (!Character.isLetter(txt.charAt(i + 1)))) {
					for (int k = i + 1; (Character.isDigit(txt.charAt(k)) || Character.toString(txt.charAt(k)).equals("'")) ; k++) {
						variable += Character.toString(txt.charAt(k));
					}
				}
				variable = variable.trim();
				variables[j] = variable;
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

		// search for the terminals
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

		// get the rules
		String[] rules = txt.split("\n");

		String[] auxRule;
		for (String x : rules) {
			auxRule = x.split("->");
			r.setLeftSide(auxRule[0].trim());
			String[] rulesOnRightSide = auxRule[1].split(" | ");
			for (int i = 0; i < rulesOnRightSide.length; i++) {
				rulesOnRightSide[i] = rulesOnRightSide[i].trim();
				if (!rulesOnRightSide[i].equals("|") && !rulesOnRightSide[i].isEmpty()) {
					r.setRightSide(rulesOnRightSide[i]);
					rule.add(new Rule(r));
				}
			}
		}
		return rule;
	}

	public static String extractInitialSymbolFromFull(String txt) {
		String initialSymbol = new String();
		for (int i = 0; txt.charAt(i) != ' ' && txt.charAt(i) != '-'; i++) {
			initialSymbol += Character.toString(txt.charAt(i));
		}
		initialSymbol = initialSymbol.trim();
		return initialSymbol;
	}

	public static boolean compareInitialSymbolWithParameter(
			String initialSymbol, String rules) {
		for (int i = 0; i < rules.length(); i++) {
			if (initialSymbol.equals(rules.charAt(i)))
				return true;
		}
		return false;
	}

	public static boolean compareSymbolWithParameter(String variables,
			String rules) {
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

	public static String formatInitialSymbol(String initialSymbol) {
		return Character
				.toString(Character.toUpperCase(initialSymbol.charAt(0)));
	}

	// retira símbolo inicial recursivo
	// retorna gramática sem símbolo inicial recursivo
	public static Grammar getGrammarWithInitialSymbolNotRecursive(final Grammar g) {
		Grammar gc = (Grammar) g.clone();
		
		String initialSymbol = gc.getInitialSymbol();
		boolean insert = false;
		for (Rule element : gc.getRules()) {
			if (element.getRightSide().contains(initialSymbol)) {
				insert = true;
			}
		}
		if (insert == true) {
			gc.insertRule(initialSymbol + "'", initialSymbol);
			gc.setInitialSymbol(initialSymbol + "'");
		}
		
		gc.insertVariable(gc.getInitialSymbol());
		return gc;
	}

	public static String searchVariablesOnRules(String element,
			Set<String> nullableVariables) {
		String containsVariables = new String();
		boolean found = true;
		String[] verifyRules = element.split(" | ");
		for (String aux : verifyRules) {
			for (int j = 0; j < aux.length() && found == true; j++) {
				if (Character.isLowerCase(aux.charAt(j)))
					found = false;
			}
			for (int i = 0; i < aux.length() && found == true; i++) {
				if (nullableVariables
						.contains(Character.toString(aux.charAt(i)))) {
					containsVariables += Character.toString(aux.charAt(i));
				}
			}
			found = true;
		}
		return containsVariables;
	}

	public static boolean searchVariables(String variables, Set<String> prev) {
		boolean found = false;
		for (int i = 0; i < variables.length() && found == false; i++) {
			if (prev.contains(Character.toString(variables.charAt(i))))
				found = true;
		}
		return found;
	}

	public static String replaceEmpty(Rule element, Grammar gc) {
		String aux = element.getRightSide();
		if (!element.getLeftSide().equals(gc.getInitialSymbol())) {
			if (gc.getTerminals().contains(Character.toString(Character.toLowerCase(element.getLeftSide().charAt(0))))) {
				aux = Character.toString(Character.toLowerCase(element.getLeftSide().charAt(0)));
			} else {
				aux = "";
			}
		}
		return aux;
	}

	public static String permutation(String rightSide, Set<String> nullableVariables, int i, String totalSentence) {
		String newSentence = new String();
		String aux = new String();
		if (nullableVariables.contains(Character.toString(rightSide.charAt(i))) && (rightSide.length() != 1)) {
			for (int j = 0; j < rightSide.length(); j++) {
				if (j != i)
					aux += Character.toString(rightSide.charAt(j));
			}
			newSentence = aux + " | ";
		}
		
		for (int j = 0; j < aux.length(); j++) {
			String temporarySentence = aux;
			int k = j;
			while (k != temporarySentence.length()) {
				if (nullableVariables.contains(Character.toString(temporarySentence.charAt(k)))) {
					temporarySentence = updateTemporarySentence(temporarySentence, k);
					if (existingProduction(totalSentence + " | " + newSentence, temporarySentence))
						newSentence += temporarySentence + " | ";
					k = 0;
				} else {
					k++;
				}
			}
		}		
		return newSentence;
	}
		

	private static boolean existingProduction(String newSentence,
			String temporarySentence) {
		String[] productions = newSentence.split(" | ");
		for (int i = 0; i < productions.length; i++) {
			productions[i] = productions[i].trim();
			if (productions[i].equals(temporarySentence))
				return false;
		}
		return true;
	}

	private static String updateTemporarySentence(String temporarySentence,
			int k) {
		String newSentence = new String();
		for (int i = 0; i < temporarySentence.length(); i++) {
			if (i != k)
				newSentence += Character.toString(temporarySentence.charAt(i));
		}
		return newSentence;
	}	
	
	// remove símbolos terminais de regras
	// retorna gramática essencialmente não-contrátil
	public static Grammar getGrammarEssentiallyNoncontracting(final Grammar g) {
		Grammar gc = (Grammar) g.clone();
		Set<String> nullable = new HashSet<String>();
		Set<Rule> setOfRules = new HashSet<Rule>();
		
		// nullable = nullable U A -> . |  A E V 
		for (Rule element : gc.getRules()) {
			if (element.getRightSide().equals(".")) {
				nullable.add(element.getLeftSide());
			} else {
				Rule r = new Rule(element.getLeftSide(), element.getRightSide());
				setOfRules.add(r);
			}
		}
		
		//gera conjuntos de variáveis anuláveis
		Set<String> prev = new HashSet<String>();
		do {
			prev.addAll(nullable);
			for (Rule element : gc.getRules()) {
				if (prevContainsVariable(prev, element.getRightSide())) {
					nullable.add(element.getLeftSide());
				}
			}
		} while (!prev.equals(nullable));		
		Set<Rule> newSetOfRules = new HashSet<Rule>();		
		for (Rule element : setOfRules) {
			String aux = element.getRightSide() + " | ";				
			int i = 0;
			while (i != element.getRightSide().length()) {
					aux += permutation(element.getRightSide(), nullable, i, aux);
					i++;
			}
			String[] productionsOnRightSide = aux.split(" | ");
			for (i = 0; i < productionsOnRightSide.length; i++ ) {
				productionsOnRightSide[i] = productionsOnRightSide[i].trim();
				if (!productionsOnRightSide[i].equals("|")) {
					Rule r = new Rule(element.getLeftSide(), productionsOnRightSide[i]);
					newSetOfRules.add(r);
				} 
			}
		}
		
		if (nullable.contains(gc.getInitialSymbol())) {
			Rule r = new Rule(gc.getInitialSymbol(), ".");
			newSetOfRules.add(r);
		}
		gc.setRules(newSetOfRules);		
		return gc;
	}
	
	public static boolean prevContainsVariable(Set<String> prev, String element) {
		boolean test = true;
		for (int i = 0; i < element.length() && test == true; i++) {
			if (!Character.isUpperCase(element.charAt(i)) || !prev.contains(Character.toString(element.charAt(i)))) {
				test = false;
			}
		}
		return test;
	}

	// remove regras da cadeia
	// retorna gramática sem regras da cadeia	
	public static Grammar getGrammarWithoutChainRules(final Grammar g) {
		Grammar gc = (Grammar) g.clone();
		
		//primeiramente, deve-se construir os subconjuntos
		Map<String, Set<String>> setOfChains = new HashMap<String, Set<String>>();
		for (String variable : gc.getVariables()) {
			// conjunto que representa o chain de determinada variável
			Set<String> chain = new HashSet<String>();
			Set<String> prev = new HashSet<String>();
			Set<String> New = new HashSet<String>();
			chain.add(variable);
			do {
				New = chainMinusPrev(chain, prev);
				prev.addAll(chain);
				for (String variableInNew : New) {
					for (Rule element : gc.getRules()) {
						if (variableInNew.equals(element.getLeftSide()) && (element.getRightSide().length() == 1 && Character.isUpperCase(element.getRightSide().charAt(0)))) {
							chain.add(element.getRightSide());
						}
					}
				}
			} while (!chain.equals(prev));
			setOfChains.put(variable, chain);
		}
						
		//iterações sobre os conjuntos de chains
		Set<Rule> newSetOfRules = new HashSet<Rule>();
		for (String variable : gc.getVariables()) {
			Set<String> chainsOfVariable = setOfChains.get(variable);
			for (String variableChain : chainsOfVariable) {
				for (Rule element : gc.getRules()) {
					if (element.getLeftSide().equals(variableChain)) {
						if (element.getRightSide().length() != 1 || !Character.isUpperCase(element.getRightSide().charAt(0))) {
							Rule r = new Rule(variable, element.getRightSide());
							newSetOfRules.add(r);
						}
					}
				}
			}
		}
		
		gc.setRules(newSetOfRules);		
		return gc;
	}
	
	public static Set<String> chainMinusPrev(Set<String> chain, Set<String> prev) {
		Set<String> aux = new HashSet<String>();
		for (String element : chain) {
			if (!prev.contains(element)) {
				aux.add(element);
			}
		}
		return aux;
	}

	// atualiza as regras da gramática após rodar algoritmos de remoção
	// de símbolos inúteis
	public static Set<Rule> updateRules(Set<String> prev, Grammar g) {
		Set<Rule> newRules = new HashSet<>();
		for (Rule element : g.getRules()) {
			if (prev.contains(element.getLeftSide())) {
				String newRule = new String();
					boolean insertOnNewRule = true;
					for (int j = 0; j < element.getRightSide().length()
							&& insertOnNewRule != false; j++) {
						if (Character.isUpperCase(element.getRightSide().charAt(j))) {
							if (prev.contains(Character.toString(element.getRightSide()
									.charAt(j))))
								insertOnNewRule = true;
							else
								insertOnNewRule = false;
						} else if (Character.isLowerCase(element.getRightSide().charAt(j))) {
							insertOnNewRule = true;
						} else if (element.getRightSide().charAt(j) == '.'){
							insertOnNewRule = true;
						} else {
							insertOnNewRule = false;
						}
					}
					if (insertOnNewRule) {
						newRule += element.getRightSide();
					}
				if (newRule.length() != 0) {
					Rule r = new Rule();
					r.setLeftSide(element.getLeftSide());
					r.setRightSide(newRule);
					newRules.add(r);
				}
			}
		}
		return newRules;
	}

	// atualiza os terminais da gramática após remover variáveis inúteis
	public static Set<String> updateTerminals(Grammar g) {
		Set<String> newTerminals = new HashSet<>();
		for (Rule element : g.getRules()) {
			for (int i = 0; i < element.getRightSide().length(); i++) {
				if (Character.isLowerCase(element.getRightSide().charAt(i)))
					newTerminals.add(Character.toString(element.getRightSide()
							.charAt(i)));
			}
		}
		return newTerminals;
	}

	public static Grammar getGrammarWithoutNoTerm(final Grammar g) {
		Set<String> term = new HashSet<>();
		Set<String> prev = new HashSet<>();
		Grammar gc = (Grammar) g.clone();

		// preenche conjunto term com as variáveis que são terminais
		for (Rule element : gc.getRules()) {
			if (element.getRightSide().length() == 1 && (!element.getRightSide().equals("|"))
						&& (Character.isLowerCase(element.getRightSide().charAt(0)) || element.getRightSide().charAt(0) == '.')) {
					term.add(element.getLeftSide());
			}
		}
		
		do {
			prev.addAll(term);
			for (Rule element : gc.getRules()) {
				boolean insertOnTerm = true;
				for (int j = 0; j < element.getRightSide().length() && insertOnTerm ==  true; j++) {
					if (Character.isLowerCase(element.getRightSide().charAt(j)) && !gc.getTerminals().contains(Character.toString(element.getRightSide().charAt(j)))) {
								insertOnTerm = false;
					} else if (Character.isUpperCase(element.getRightSide().charAt(j)) && !prev.contains(Character.toString(element.getRightSide().charAt(j)))) {
							insertOnTerm = false;
					}
				}
				if (insertOnTerm) {					
						term.add(element.getLeftSide());	
				}
			}
		} while (!term.equals(prev));
		Grammar aux = new Grammar();
		aux.setVariables(prev);
		aux.setRules(updateRules(prev, gc));
		aux.setTerminals(updateTerminals(aux));
		gc.setVariables(aux.getVariables());
		gc.setTerminals(aux.getTerminals());
		gc.setRules(aux.getRules());
		return gc;
	}
	
	public static Set<String> termMinusNoTerm(Set<String> term, Set<String> noTerm) {
		for (String element : noTerm) {
			if (term.contains(element)) {
				term.remove(element);
			}
		}
		return term;
	}

	public static Set<String> reachMinusPrev(Set<String> reach, Set<String> prev) {
		Set<String> aux = new HashSet<>();
		for (String element : reach) {
			if (!prev.contains(element)) {
				aux.add(element);
			}
		}
		return aux;
	}

	public static Set<String> variablesInW(Set<String> reach, String rightSide) {
		for (int i = 0; i < rightSide.length(); i++) {
			if (Character.isUpperCase(rightSide.charAt(i)))
				reach.add(Character.toString(rightSide.charAt(i)));
		}
		return reach;
	}

	public static Grammar getGrammarWithoutNoReach(final Grammar g) {
		Set<String> reach = new HashSet<>();
		Set<String> prev = new HashSet<>();
		Set<String> New = new HashSet<>();
		Grammar gc = (Grammar) g.clone();
		
		reach.add(gc.getInitialSymbol());
		do {
			New.addAll(reachMinusPrev(reach, prev));
			prev.addAll(reach);
			for (String element : New) {
				for (Rule secondElement : gc.getRules()) {
					if (secondElement.getLeftSide().equals(element)) {
						reach.addAll(variablesInW(reach,
								secondElement.getRightSide()));
					}
				}
			}
		} while (!reach.equals(prev));
		Grammar aux = new Grammar();
		aux.setVariables(prev);
		aux.setInitialSymbol(gc.getInitialSymbol());
		aux.setRules(updateRules(prev, gc));
		aux.setTerminals(updateTerminals(aux));
		gc.setVariables(aux.getVariables());
		gc.setTerminals(aux.getTerminals());
		gc.setRules(aux.getRules());
		
		return aux;
	}

	public static boolean existsProduction(String leftSide, String symbol, Set<Rule> newSetOfRules) {
		for (Rule element : newSetOfRules) {
				if (element.getRightSide().equals(symbol)) {
						return true;
				}
		}
		return false;
	}

	public static String getVariable(String symbol, Set<Rule> newSetOfRules) {
		String variable = new String();
		boolean found = false;
		for (Rule element : newSetOfRules) {
			String[] aux = element.getRightSide().split(" | ");
			for (int i = 0; i < aux.length && found == false; i++) {
				aux[i] = aux[i].trim();
				if (aux[i].equals(symbol)) {
					variable = element.getLeftSide();
					found = true;
				}
			}
		}
		return variable;
	}

	private static int sentenceSize(String sentence) {
		int count = 0;
		for (int i = 0; i < sentence.length(); i++) {
			if (Character.isLetter(sentence.charAt(i)))
				count++;
		}
		return count;
	}

	private static int updateNumberOfInsertions(Set<Rule> newSetOfRules,
			int contInsertions) {
		int counter = 0;
		for (Rule element : newSetOfRules) {
			if (element.getLeftSide().contains("T")) {
				String aux = element.getLeftSide().substring(1);
				if (Integer.parseInt(aux) > counter)
					counter = Integer.parseInt(aux);
			}
		}
		return (counter + 1);
	}

	private static boolean canInsert(String sentence) {
		String[] productions = sentence.split(" | ");
		boolean insert = false;
		int contNumbers = 0;
		String target = productions[productions.length - 1];
		for (int i = 0; i < target.length(); i++) {
			if (Character.isDigit(target.charAt(i)))
				contNumbers++;
		}
		if ((contNumbers == 0 && target.length() != 2)
				|| (contNumbers == 1 && target.length() != 3)
				|| (contNumbers == 2 && target.length() != 4))
			insert = true;
		return insert;
	}

	public static Grammar FNC(final Grammar g) {
		
		Grammar gc = (Grammar) g.clone();
		
		gc = GrammarParser.getGrammarWithInitialSymbolNotRecursive(gc);
		gc = GrammarParser.getGrammarEssentiallyNoncontracting(gc); 
		gc = GrammarParser.getGrammarWithoutChainRules(gc);
		gc = GrammarParser.getGrammarWithoutNoTerm(gc);
		gc = GrammarParser.getGrammarWithoutNoReach(gc);
		
		if (!isFNC(gc)) {
						
			Set<Rule> newSetOfRules = new HashSet<Rule>();
			Set<Rule> auxSetOfRules = new HashSet<Rule>();
			Set<String> newSetOfVariables = new HashSet<String>();
			int contInsertions = 1;
			for (Rule element : gc.getRules()) {
				String newProduction = new String();
					String sentence = element.getRightSide();
					int cont = 0;
					int changeCounter = 0;
					//primeiro, cria-se produções para todos os símbolos terminais
					String newSentence = new String();
					if (sentenceSize(sentence) >= 2) {
						for (int i = 0; i < sentence.length(); i++) {
							if (Character.isLowerCase(sentence.charAt(i))) {
								if (!existsProduction(element.getLeftSide(), Character.toString(sentence.charAt(i)), newSetOfRules)) {
									Rule r = new Rule();
									r.setLeftSide("T" + contInsertions);
									r.setRightSide(Character.toString(sentence.charAt(i)));
									newSetOfRules.add(r);
									newSentence += "T" + contInsertions;
									newSetOfVariables.add("T" + contInsertions);//
									contInsertions = updateNumberOfInsertions(newSetOfRules, contInsertions);
								} else {
									newSentence += getVariable(Character.toString(sentence.charAt(i)), newSetOfRules);
								}
							} else {
								newSentence += Character.toString(sentence.charAt(i));
							}
						}
					} else {
						Rule r = new Rule();
						r.setLeftSide(element.getLeftSide());
						r.setRightSide(element.getRightSide());
						auxSetOfRules.add(r);
					}
					if (sentenceSize(newSentence) > 2) {
						while (cont < newSentence.length()) {
							if (Character.isLetter(newSentence.charAt(cont))) {
								if (changeCounter == 1) {
									if (canInsert(newProduction)) {
										newProduction += "T" + contInsertions;								
									}
									String insertOnRightSide = splitSentence(cont, newSentence, contInsertions);
									newSentence = splitSentence(newSentence);
									Rule r = new Rule();
									r.setLeftSide("T" + contInsertions);
									r.setRightSide(insertOnRightSide);
									newSetOfRules.add(r);
									newSetOfVariables.add("T" + contInsertions);
									contInsertions = updateNumberOfInsertions(newSetOfRules, contInsertions);
									changeCounter = 0;
									cont = -1;
								} else {
									if (canInsert(newProduction)) {
										newProduction += partialSentence(newSentence);
										newSentence = splitSentence(newSentence);
									}
									changeCounter++;
								}
							}
							cont++;
						}
						Rule r = new Rule();
						r.setLeftSide(element.getLeftSide());
						r.setRightSide(newProduction);
						newSetOfRules.add(r);
					} else  if (sentenceSize(newSentence) == 2){
						Rule r = new Rule();
						r.setLeftSide(element.getLeftSide());
						r.setRightSide(newSentence);
						newSetOfRules.add(r);
					}
			}
			//update the variables
			for (String variable : newSetOfVariables) {
				gc.insertVariable(variable);
			}
			
			//update the rules
			newSetOfRules.addAll(auxSetOfRules);
			gc.setRules(newSetOfRules);
		}	
		return gc;
	}
	
	/**
	 * 
	 * @param gc : GLC
	 * @return : verdadeiro se estiver em FNC e falso caso contrário.
	 */
	private static boolean isFNC(final Grammar gc) {
		boolean test = true;
		Iterator<Rule> it = gc.getRules().iterator();
		while (it.hasNext() && test) {
			Rule element = (Rule) it.next();
			if (!element.getRightSide().equals(".") && !Character.isLowerCase(element.getRightSide().charAt(0)) && counterOfRightSide(element.getRightSide()) != 2) {
				test = false;
			} else if (counterOfRightSide(element.getRightSide()) == 2) {
				for (int i = 0; i <  element.getRightSide().length() && test; i++) {
					if (Character.isLowerCase(element.getRightSide().charAt(i))) {
						test = false;
					}
				}
			}
		}
		return test;
	}

	private static String splitSentence(String newSentence) {
		if (newSentence.charAt(0) != 'T') {
			newSentence = newSentence.substring(1);
		} else {
			newSentence = newSentence.substring(1);
			while (Character.isDigit(newSentence.charAt(0))) {
				newSentence = newSentence.substring(1);
			}				
		}
		return newSentence;
	}

	private static String splitSentence(int cont, String newSentence, int contInsertions) {
		String aux = new String();
		if (newProductionSize(newSentence) ==  2) {
			aux =  newSentence;
		} else {
			aux = Character.toString(newSentence.charAt(0));
			if (newSentence.charAt(0) == 'T') {
				for (int i = 1; !Character.isAlphabetic(newSentence.charAt(i)); i++) {
					aux += Character.toString(newSentence.charAt(i));
				}
			}			
			aux += "T" + (contInsertions + 1);			
		}
		return aux;
	}

	private static String partialSentence(String sentence) {
		String partial = new String();
		if (sentence.charAt(0) == 'T') {
			partial += "T";
			for (int i =1; !Character.isAlphabetic(sentence.charAt(i)); i++)
				partial += Character.toString(sentence.charAt(i));
		} else {
			partial = Character.toString(sentence.charAt(0));
		}		
		return partial;
	}

	private static int newProductionSize(String newProduction) {
		int count = 0;
		for (int i = 0; i < newProduction.length(); i++) {
			if (Character.isLetter(newProduction.charAt(i)))
				count++;
		}
		return count;
	}
	
	public static boolean verifyChains(Rule element) {
		boolean chain = true;
		for (int i = 0; i < element.getRightSide().length() && chain == true; i++) {
			if (!element.getLeftSide().equals(Character.toString(element.getRightSide().charAt(i)))) {
				chain = false;
			}
		}
		return chain;
	}

	//verifica se gramática é não contrátil ou essencialmente não contrátil e se possui recursão no símbolo inicial
	private static boolean checksGrammar(final Grammar g) {
		boolean grammarTest = true;
		for (Rule element : g.getRules()) {
			//verifica se é essencialmente não contrátil
			if (element.getRightSide().equals(".") && !g.getInitialSymbol().equals(element.getLeftSide())) {
				grammarTest = false;
			}			
		}
		//verifica se possui recursão no símbolo inicial
		for (Rule element : g.getRules()) {
			if (element.getLeftSide().equals(g.getInitialSymbol()) && element.getRightSide().contains(g.getInitialSymbol())) {
				grammarTest = false;
			}
		}
		//verifica se as produções possuem ciclos
		for (Rule element : g.getRules()) {
			if (verifyChains(element)) {
				grammarTest = false;
			}
		}
		return grammarTest;
	}	
	
	//remoção de recursão à esquerda imediata
	public static Grammar removingTheImmediateLeftRecursion(final Grammar g) {
		Grammar gc = (Grammar) g.clone();
		
		//se gramática não for não contrátil ou essencialmente não contrátil, método não aceita e retorna 
		// a mesma gramática, caso contrário, a remoção é feita
		if (checksGrammar(gc)) {
			//primeira coisa: verificar quais variáveis possuem recursão à esquerda
			Set<String> haveRecursion = new HashSet<String>();
			for (Rule element : gc.getRules()) {
				if (element.getLeftSide().equals(Character.toString(element.getRightSide().charAt(0)))) {
					haveRecursion.add(element.getLeftSide());
				}			
			}
			
			//estabelece relacao entre variável que gera recursão e a variável que irá resolver esta solução
			Map<String, String> variablesMapped = new HashMap<String, String>();
			int counter = 1;
			while (gc.getVariables().contains("Z" + counter)) {
				counter++;
			}
			for (String element : haveRecursion) {
				if (!variablesMapped.containsKey(element)) {
					variablesMapped.put(element, "Z" + counter);
					counter++;
				}
			}
			
			//já é posśivel saber quem possui recursão e onde ela está, sendo possível removê-la
			Set<Rule> newSetOfRules = new HashSet<Rule>();
			Set<String> newSetOfVariables = new HashSet<String>();
			for (Rule element : gc.getRules()) {
				if (variablesMapped.containsKey(element.getLeftSide()) && !element.getRightSide().equals(".")) {
					if (element.getLeftSide().equals(Character.toString(element.getRightSide().charAt(0)))) {
						//recursão encontrada
						Rule firstProduction = new Rule();
						firstProduction.setLeftSide(variablesMapped.get(element.getLeftSide()));
						firstProduction.setRightSide(element.getRightSide().substring(1) + variablesMapped.get(element.getLeftSide()));
						newSetOfRules.add(firstProduction);
						newSetOfVariables.add(variablesMapped.get(element.getLeftSide()));
						Rule secondProduction = new Rule();
						secondProduction.setLeftSide(firstProduction.getLeftSide());
						secondProduction.setRightSide(element.getRightSide().substring(1));
						newSetOfRules.add(secondProduction);
					} else {
						//sem recursão, mas tratamento é necessário
						Rule firstProduction = new Rule();
						firstProduction.setLeftSide(element.getLeftSide());
						firstProduction.setRightSide(element.getRightSide());
						newSetOfRules.add(firstProduction);
						Rule secondProduction = new Rule();
						secondProduction.setLeftSide(firstProduction.getLeftSide());
						secondProduction.setRightSide(element.getRightSide() + variablesMapped.get(element.getLeftSide()));
						newSetOfRules.add(secondProduction);
					}				
				} else {
					//variável não produz recursão à esquerda
					newSetOfRules.add(element);
				}			
			}
			
			//seta as regras alteradas à gramática clonada 
			gc.setRules(newSetOfRules);
			
			//adiciona variáveis criadas no processo à gramática clonada
			for (String variable : newSetOfVariables) {
				gc.insertVariable(variable);
			}		
		}	
		
		return gc;
	}
	
		//remoção de recursão à esquerda direta e indireta
		public static Grammar removingLeftRecursion(final Grammar g) {
			Grammar gc = (Grammar) g.clone();			
			Set<String> olderVariables = gc.getVariables();
			
			if (checksGrammar(gc)) {
				//ordenando os símbolos não terminais
				Map<String, String> variablesInOrder = new HashMap<String, String>();
				int counter = 1;
				variablesInOrder.put(gc.getInitialSymbol(), Integer.toString(counter));
				for (String element : gc.getVariables()) {
					if (!element.equals(gc.getInitialSymbol())) {
						counter++;
						variablesInOrder.put(element, Integer.toString(counter));		
					}
				}
							
				while (existsRecursion(gc, variablesInOrder, olderVariables)) {
					//verifica se gramática possui recursão direta
					if (existsDirectRecursion(gc)) {
						gc = GrammarParser.removingTheImmediateLeftRecursion(gc);
					}
					
					//gramática possui recursão indireta
					Set<Rule> newSetOfRules = new HashSet<Rule>();
					for (String variable : gc.getVariables()) {
						if (olderVariables.contains(variable)) {
							for (Rule element : gc.getRules()) {
								if (variable.equals(element.getLeftSide())) {
									int u = Integer.parseInt(variablesInOrder.get(variable));
									String rightSide = getsFirstCharacter(element.getRightSide());	
									if (Character.isLowerCase(rightSide.charAt(0)) || ".".equals(rightSide)) {
										Rule r = new Rule(variable, element.getRightSide());
										newSetOfRules.add(r);
									} else {
										int v = Integer.parseInt(variablesInOrder.get(rightSide));
										if (u > v) {
											for (Rule secondElement : gc.getRules()) {
												if (rightSide.equals(secondElement.getLeftSide())) {
													String newProduction = secondElement.getRightSide();
													newProduction += element.getRightSide().substring(indexOfFirstChar(element.getRightSide()));
													Rule r = new Rule(variable, newProduction);
													newSetOfRules.add(r);
												}
											}
										} else {
											Rule r = new Rule(variable, element.getRightSide());
											newSetOfRules.add(r);
										}
									}
								}
							}
						}
					}
					//atualiza newSetOfRules
					for  (Rule element : gc.getRules()) {
						if (!olderVariables.contains(element.getLeftSide())) {
							Rule r = new Rule(element.getLeftSide(), element.getRightSide());
							newSetOfRules.add(r);
						}
					}
					gc.setRules(newSetOfRules);		
				}
			}		
			return gc;		
		}
		
		/**
		 * Retorna o índice do primeiro elemento de uma produção
		 */
		private static int indexOfFirstChar(String rightSide) {
			boolean firstCharacter = true;
			int i = 0;
			if (counterOfRightSide(rightSide) != 1) {
				i = 1;
				while (firstCharacter) {
					if (!Character.isDigit(rightSide.charAt(i))) {
						firstCharacter = false;
					} else {
						i++;
					}
				}
			}
			return i;
		}
		
		/**
		 * 
		 * @param rightSide : string
		 * @return : retorna a quantidade de elementos na string
		 */
		private static int counterOfRightSide(String rightSide) {
			int i = 0;
			int j = 0;
			while (i != rightSide.length()) {
				if (Character.isLetter(rightSide.charAt(i)))
					j++;
				i++;
			}
			return j;
		}

		/**
		 * Verifica se existe recursão direta
		 * @param olderVariables 
		 */
		private static boolean existsDirectRecursion(Grammar g) {
			boolean recursion = false;
			for (String variable : g.getVariables()) {
				for (Rule element : g.getRules()) {
					if (variable.equals(element.getLeftSide())) {
						if (variable.equals(Character.toString(element.getRightSide().charAt(0)))) {
							recursion = true;
						}
					}
				}
			}
			return recursion;
		}

		/**
		 * Retorna verdadeiro se existir recursão
		 * @param olderVariables 
		 */
		private static boolean existsRecursion(final Grammar g, Map<String, String> variablesInOrder, Set<String> olderVariables) {
			boolean recursion = false;
			for (String variable : g.getVariables()) {
				if (olderVariables.contains(variable)) {
					for (Rule element : g.getRules()) {
						if (variable.equals(element.getLeftSide()) && Character.isUpperCase(element.getRightSide().charAt(0))) {
							int u = Integer.parseInt(variablesInOrder.get(variable));
							String rightSide = getsFirstCharacter(element.getRightSide()); 
							int v = Integer.parseInt(variablesInOrder.get(rightSide));
							if (u >= v) {
								recursion = true;
							}
						}
					}
				}
			}
			return recursion;
		}

		/**
		 * Retorna a primeira variável da sentaça à esquerda
		 */
		private static String getsFirstCharacter(String rightSide) {
			boolean concatenate = true;
			String newRightSide = Character.toString(rightSide.charAt(0));
			for (int i = 1; i < rightSide.length() && concatenate == true; i++) {
				if (Character.isDigit(rightSide.charAt(i))) {
					newRightSide += Character.toString(rightSide.charAt(i));
				} else {
					concatenate = false;
				}
			}
			return newRightSide;
		}

		//realiza a formalização de Greibach: recebe uma gramática e retorna uma gramática em FNG
		public static Grammar FNG(final Grammar g) {
			Grammar gc = (Grammar) g.clone();
			gc = GrammarParser.FNC(gc);
			System.out.println("______________________");
			for (String v : gc.getVariables()) {
				for (Rule element : gc.getRules()) {
					System.out.println(element);
				}
			}
			System.out.println(gc.getVariables());
			gc = GrammarParser.removingLeftRecursion(gc);
			
			if (grammarWithCycles(gc)) {
				while (!isFNG(gc.getRules())) {
					Set<Rule> newSetOfRules = new HashSet<Rule>();
					for (String variable : gc.getVariables()) {
						for (Rule element : gc.getRules()) {
							if (variable.equals(element.getLeftSide())) {
								if (!Character.isLowerCase(element.getRightSide().charAt(0)) && !element.getRightSide().equals(".")) {
									String firstCharacter = getsFirstCharacter(element.getRightSide());
									for (Rule secondElement : gc.getRules()) {
										if (secondElement.getLeftSide().equals(firstCharacter)) {
											String newRightSide = secondElement.getRightSide();
											if (counterOfRightSide(element.getRightSide()) != 1) { 
												newRightSide +=  element.getRightSide().substring(indexOfFirstChar(element.getRightSide()));
											}
											Rule r = new Rule(element.getLeftSide(), newRightSide);
											newSetOfRules.add(r);
										}
									}
								} else {
									Rule r = new Rule(variable, element.getRightSide());
									newSetOfRules.add(r);
								}
							}
						}
					}
					gc.setRules(newSetOfRules);
				}			
			}			
			return gc;
		}
		
		
		private static boolean grammarWithCycles(final Grammar g) {
			boolean cycle = true;
			for (Rule element : g.getRules()) {
				if (verifyChains(element)) {
					cycle = false;
				}
			}
			return cycle;
		}

		private static boolean isFNG(Set<Rule> rules) {
			boolean fng = true;
			Iterator<Rule> it = rules.iterator();
			while (it.hasNext() && fng) {
				Rule element = (Rule) it.next();
				if (!Character.isLowerCase(element.getRightSide().charAt(0)) && !".".equals(element.getRightSide())) {
					fng = false;
				}
			}
			return fng;
		}	
	

	//cria novas regras realizando as substituições necessárias
	public static Set<Rule> createNewRules(String variable, Grammar gc, Map<String, String> variablesInOrder) {
		Set<Rule> newSetOfRules = new HashSet<Rule>();
		for (Rule element : gc.getRules()) {
			String newProduction = new String();
			if (variable.equals(element.getLeftSide())) {
				int leftValue = Integer.parseInt(variablesInOrder.get(variable));
				int rightValue;
				boolean test = true;
				for (int i = 0; i < element.getRightSide().length() && test; i++) {
					if (Character.isLetter(element.getRightSide().charAt(i)) && Character.isUpperCase(element.getRightSide().charAt(i))) {
						rightValue = Integer.parseInt(variablesInOrder.get(determinesRightSide(element.getRightSide(), i)));	
						if (leftValue > rightValue) {
							test = false;
							String searchVariable = determinesRightSide(element.getRightSide(), i);
							for (Rule secondElement : gc.getRules()) {
								if (searchVariable.equals(secondElement.getLeftSide())) {
									newProduction = element.getRightSide();
									newProduction = newProduction.replace(searchVariable, secondElement.getRightSide());
									Rule r = new Rule(element.getLeftSide(), newProduction);
									newSetOfRules.add(r);
								}								
							}
						} 
					} else if (Character.isLowerCase(element.getRightSide().charAt(i))){
						Rule r = new Rule(variable, element.getRightSide());
						newSetOfRules.add(r);
					}
				}
				if (test) {
					Rule r = new Rule(variable, element.getRightSide());
					newSetOfRules.add(r);
				}
			}
		}
		return newSetOfRules;
	}

	//verifica se é necessário realizar substituições
	public static boolean canReplace(String variable, final Grammar g,  final Map<String, String> variablesInOrder) {
		boolean test = false;
		for (Rule element : g.getRules()) {
			if (variable.equals(element.getLeftSide())) {
				int leftValue = Integer.parseInt(variablesInOrder.get(variable));
				int rightValue;
				for (int i = 0; i < element.getRightSide().length(); i++) {
					if (Character.isUpperCase(element.getRightSide().charAt(i))) {
						rightValue = Integer.parseInt(variablesInOrder.get(determinesRightSide(element.getRightSide(), i)));	
						if (leftValue > rightValue) {
							test = true;
						}
					}
				}				
			}
		}
		return test;
	}

	//determina qual variável é a primeira de determinada produção
	public static String determinesRightSide(String rightSide, int counter) {
		while (Character.isDigit(rightSide.charAt(counter))) {
			counter++;
		}
		String variable = Character.toString(rightSide.charAt(counter));
		boolean test = true;
		while (rightSide.length() > counter + 1 &&  test) {
			if (Character.isDigit(rightSide.charAt(counter + 1))) {
				variable += Character.toString(rightSide.charAt(counter + 1));
			}			
			counter++;
			if (Character.isLetter(rightSide.charAt(counter))) {
				test = false;
			}
		}
		return variable;
	}

	//verifica se uma determinada regra está ou não em uma FNG
	public static boolean isInFNG(String variable, final Grammar g) {
		boolean test = true;
		Iterator it = g.getRules().iterator();
		Rule element = new Rule();
		while (it.hasNext() && test) {
			element = (Rule) it.next();
			if (variable.equals(element.getLeftSide())) {
				if (!element.getRightSide().equals(".")) {
					int counterOfLowerCase = 0;
					for (int i = 0; i < element.getRightSide().length(); i++) {
						if (Character.isLowerCase(element.getRightSide().charAt(i))) {
							counterOfLowerCase++;
						}
					}
					test = (counterOfLowerCase == 1) ? (true) : (false); 
				}
			}
		}
		return test;
	}
	
	
	/**
	 * 
	 * @param g: gramática livre de contexto
	 * @return automaton: gramática livre de contexto convertida em autômato de pilha 
	 */
	public static PushdownAutomaton turnsGrammarToPushdownAutomata(final Grammar g) {
		Grammar gc = (Grammar) g.clone();
		
		if (!isFNG(gc.getRules())) {
			gc = GrammarParser.FNG(gc);
		}
		
		PushdownAutomaton automaton = new PushdownAutomaton();
		
		if (isFNG(gc.getRules())) {
			//inicializando o automato
			//adiciona estado final
			Set<String> finalStates = new HashSet<String>();
			finalStates.add("q1");
			automaton.setFinalStates(finalStates);
			
			//adiciona estado inicial
			automaton.setInitialState("q0");
			
			//adiciona os estados que serão utilizados
			Set<String> states = new HashSet<String>();
			states.add("q0");
			states.add("q1");
			automaton.setStates(states);
			
			//adiciona alfabeto da pilha
			automaton.setStackAlphabet(gc.getVariables());
			
			//adiciona alfabeto
			automaton.setAlphabet(gc.getTerminals());
			
			//adicionando função de transição
			Set<TransitionFunctionPA> transitionTable = new HashSet<TransitionFunctionPA>();
			for (Rule element : gc.getRules()) {
				TransitionFunctionPA transitionFunction = new TransitionFunctionPA();
				if (element.getLeftSide().equals(gc.getInitialSymbol())) {
					transitionFunction.setCurrentState("q0");
					transitionFunction.setFutureState("q1");
					transitionFunction.setPops(".");
					transitionFunction.setSymbol(Character.toString(element.getRightSide().charAt(0)));
					String stacking = (".".equals(element.getRightSide())? (element.getRightSide()): (element.getRightSide().substring(1)));
					transitionFunction.setStacking(stacking);
				} else {
					transitionFunction.setCurrentState("q1");
					transitionFunction.setFutureState("q1");
					transitionFunction.setPops(element.getLeftSide());
					transitionFunction.setSymbol(Character.toString(element.getRightSide().charAt(0)));
					transitionFunction.setStacking(element.getRightSide().substring(1));
				}			
				transitionTable.add(transitionFunction);
			}
			automaton.setTransictionFunction(transitionTable);
		}	
		
		return automaton;
	}
	

	// ALGORITMO DE RECONHECIMENTO CYK
	public static Set<String>[][] CYK(Grammar g, String word) {
		// inicializando a tabela
		Set<String>[][] X = new TreeSet[word.length() + 1][word.length()];
		for (int i = 0; i < word.length() + 1; i++) {
			for (int j = 0; j < word.length(); j++) {
				X[i][j] = new TreeSet<String>();
			}
		}

		// inserindo a palavra na base da tabela
		for (int i = 0; i < word.length(); i++) {
			X[word.length()][i].add(Character.toString(word.charAt(i)));
		}

		// preenchendo a primeira linha da tabela
		X = fillFirstLine(X, g, word);

		// preenchendo a segunda linha da tabela
		X = fillSecondLine(X, g, word);

		// preenchendo o restante da tabela
		int line = word.length() - 1;
		int column = 0;
		int count = 2;
		
		X = fillOthersLines(X, g, count, line, column, word);
		
		return X;
	}
	
	private static Set<String>[][] fillOthersLines(Set<String>[][] x, Grammar g, int count, int line, int column, String word) {
		
		int counterLine = 0;
		int counterColumn = 0;
		int targetLine = 3;
		int targetColumn = 0;
		int delimiter = word.length() - (count + 1);
		int auxFlag = 3;
		boolean flag = true;
			
		int lineFirstElement = line;
		int lineSecondElement = line - 1;
		int columnFirstElement = column;
		int columnSecondElement = column + 1;
		int counterIndex = lineFirstElement + lineSecondElement;
		
		while (count != word.length()) {
			
			
			while (flag) {	
				while (lineFirstElement != delimiter) {
					if (lineFirstElement + lineSecondElement == counterIndex) {
						String firstCell = returnsAlphabeticSymbols(x[lineFirstElement][columnFirstElement]);
						String secondCell = returnsAlphabeticSymbols(x[lineSecondElement][columnSecondElement]);
						for (int counterOfFirstCell = 0; counterOfFirstCell < firstCell.length(); counterOfFirstCell++) { 
							String sentence = new String();
							sentence += Character.toString(firstCell.charAt(counterOfFirstCell)).trim();
							for (int counterOfSecondCell = 0; counterOfSecondCell < secondCell.length(); counterOfSecondCell++) { 
								sentence += Character.toString(secondCell.charAt(counterOfSecondCell)).trim();
								Set<String> aux = checksEquality(g, word, sentence);
								x[targetLine][targetColumn].addAll(aux);
								sentence = sentence.substring(0, sentence.length() - 1);
							}
						}
					}
					lineFirstElement--;
					lineSecondElement++;
					columnSecondElement++;
				}
				targetColumn++;
				columnFirstElement++;
				lineFirstElement = line;
				lineSecondElement = line - 1 - counterLine;
				counterColumn++;
				flag = (columnSecondElement == word.length()) ? (false) : (true);
				columnSecondElement = column + 1 + counterColumn;
			}	
			counterLine++;
			lineFirstElement = line;
			columnFirstElement = column;
			lineSecondElement = line - counterLine - 1;
			columnSecondElement = column + 1;
			targetColumn = 0;
			targetLine--;
			count++;
			delimiter--;
			counterColumn = 0;
			counterIndex--;
			flag = true;
		}
		return x;
	}

	private static Set<String>[][] fillSecondLine(Set<String>[][] x, Grammar g,
			String word) {
		for (int j = 0; j < word.length() - 1; j++) {
			String firstCell = returnsAlphabeticSymbols(x[word.length() - 1][j]); 
			String secondCell = returnsAlphabeticSymbols(x[word.length() - 1][j + 1]);

			for (int counterOfFirstCell = 0; counterOfFirstCell < firstCell.length(); counterOfFirstCell++) {
				String sentence = new String();
				sentence += Character.toString(firstCell.charAt(counterOfFirstCell)).trim();
				for (int counterOfSecondCell = 0; counterOfSecondCell < secondCell.length(); counterOfSecondCell++) {
					sentence += Character.toString(secondCell.charAt(counterOfSecondCell)).trim();
					Set<String> aux = checksEquality(g, word, sentence);
					x[word.length() - 2][j].addAll(aux);
					sentence = sentence.substring(0, sentence.length()-1);
				}
			}
		}
		return x;
	}
	
	//remove caracteres que não sejam alfabéticos
	private static String returnsAlphabeticSymbols(Set<String> set) {
		String aux = new String();
		for (String element : set) {
			if (Character.isAlphabetic(element.charAt(0)))
				aux += element;
		}
		return aux;
	}

	//preenche a primeira linha da tabela
	private static Set<String>[][] fillFirstLine(Set<String>[][] x, Grammar g, String word) {
		for (int j = 0; j < word.length(); j++) {
			x[word.length() - 1][j] = checksEquality(g, word,
					Character.toString(word.charAt(j)));
		}
		return x;
	}

	private static Set<String> checksEquality(Grammar g, String word, String letter) {
		Set<String> found = new TreeSet<String>();
		for (Rule element : g.getRules()) {
			if (element.getRightSide().equals(letter)) {
					found.add(element.getLeftSide());
			}
		}
		return found;
	}

	public static String[][] turnsTreesetOnArray(Set<String>[][] CYK, String word) {
		String[][] cykOut = new String[word.length()+1][word.length()];
		for (int i = 0; i < word.length()+1; i++) {
			for (int j = 0; j < word.length(); j++) {
				if (j <= i) {
					String sentence = returnsAlphabeticSymbols(CYK[i][j]);
					String newSentence = new String();
					for (int k = 0; k < sentence.length(); k++) {
						newSentence += Character.toString(sentence.charAt(k)) + " ";
					}
					newSentence = (newSentence.equals("") ? ("-") : (newSentence.substring(0, newSentence.length()-1)));
					cykOut[i][j] = newSentence;
				} else {
					cykOut[i][j] = "";
				}
			}
		}
		return cykOut;
	}

}