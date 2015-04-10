package br.com.lfaplataform.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
		return Character.toString(txt.charAt(0));
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
	public static Grammar getGrammarWithInitialSymbolNotRecursive(Grammar g) {
		String initialSymbol = g.getInitialSymbol();
		boolean insert = false;
		for (Rule element : g.getRules()) {
			if (element.getLeftSide().equals(initialSymbol)) {
				if (element.getRightSide().contains(initialSymbol)) {
					insert = true;
				}
			}
		}
		if (insert == true) {
			g.insertRule(initialSymbol + "'", initialSymbol);
			g.setInitialSymbol(initialSymbol + "'");
		}
		
		g.insertVariable(g.getInitialSymbol());
		return g;
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

	public static String replaceEmpty(String aux, Rule element) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(aux);
		for (int i = 0; i < stringBuilder.length(); i++) {
			if (stringBuilder.charAt(i) == '.')
				stringBuilder.setCharAt(i,
						Character.toLowerCase(element.getLeftSide().charAt(0)));
		}
		aux = stringBuilder.toString();
		return aux;
	}

	public static String permutation(String rightSide, Set<String> nullableVariables, int i, String totalSentence) {
		String newSentence = new String();
		String aux = new String();
		if (nullableVariables.contains(Character.toString(rightSide.charAt(i)))) {
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
	public static Grammar getGrammarEssentiallyNoncontracting(Grammar g) {
		// conjunto que irá armazenar o conjunto de variáveis anuláveis
		Set<String> nullableVariables = new HashSet<>();
		Set<String> nullableVariablesAux = new HashSet<>();
		// conjunto que irá armazenar o conjunto de próximas variáveis
		Set<String> prev = new HashSet<>();
		// percorre todas as regras procurando alguma variável que produza vazio
		for (Rule element : g.getRules()) {
			if (element.getRightSide().contains(Character.toString('.')))
				nullableVariables.add(element.getLeftSide());
		}
		// calcula conjunto de variáveis anuláveis
		do {
			prev.addAll(nullableVariables);
			for (Rule element : g.getRules()) {
				String containsVariables = searchVariablesOnRules(
						element.getRightSide(), nullableVariables);
				if (searchVariables(containsVariables, prev)) {
					nullableVariables.add(element.getLeftSide());
				}
			}
		} while (!nullableVariables.equals(prev));
		nullableVariablesAux.addAll(nullableVariables);
		// realiza comparações
		// procura vazio
		Grammar teste = new Grammar();
		for (Rule element : g.getRules()) {
			// verifica se variável está no conjunto anulável e se
			// contém o símbolo lâmbda
			if (nullableVariablesAux.contains(element.getLeftSide())
					&& element.getRightSide().contains(".")) {
				String aux = element.getRightSide();
				aux = replaceEmpty(aux, element);
				teste.insertRule(element.getLeftSide(), aux);
				nullableVariablesAux.remove(element.getLeftSide());
			} else {
				teste.insertRule(element.getLeftSide(), element.getRightSide());
			}
		}
		g.setRule(teste.getRules());
		Grammar teste2 = new Grammar();
		for (Rule element : g.getRules()) {
			if (nullableVariables.contains(element.getLeftSide())) {
				// preciso trabalhar com variáveis do lado direito
				String aux = element.getRightSide() + " | ";
				int i = 0;
				while (i != element.getRightSide().length()) {
					aux += permutation(element.getRightSide(), nullableVariables, i, aux);
					i++;
				}
				String[] productionsOnRightSide = aux.split(" | ");
				for (i = 0; i < productionsOnRightSide.length; i++ ) {
					productionsOnRightSide[i] = productionsOnRightSide[i].trim();
					if (!productionsOnRightSide[i].equals("|"))
						teste2.insertRule(element.getLeftSide(), productionsOnRightSide[i]);
				}
			} else {
				teste2.insertRule(element.getLeftSide(), element.getRightSide());
			}
		}
		
		if (nullableVariables.contains(g.getInitialSymbol())) {
			teste2.insertRule(g.getInitialSymbol(), ".");
			g.setRule(teste2.getRules());
		}
		return g;
	}

	// verifica se a sentença possui regra da cadeia
	public static boolean isChain(String[] auxRightSide) {
		boolean chain = false;
		for (int i = 0; i < auxRightSide.length && chain == false; i++) {
			if (auxRightSide[i].length() == 1 && !auxRightSide[i].contains("|")) {
				if (Character.isUpperCase(auxRightSide[i].charAt(0)))
					chain = true;
			}
		}
		return chain;
	}

	public static boolean foundChainRules(char caracter, String s,
			ArrayList<Rule> noChainRules) {
		// pesquisa se o caracter está no conjunto direto
		boolean found = false;
		for (int i = 0; i < noChainRules.size() && found == false; i++) {
			if (noChainRules.get(i).getLeftSide()
					.equals(Character.toString(caracter))) {
				s += noChainRules.get(i).getRightSide() + " ";
				// CONTINUAR AQUI
				found = true;
			}
		}
		return found;
	}

	public static String replaceChainRules(char caracter, String s,
			ArrayList<Rule> noChainRules) {
		boolean found = false;
		for (int i = 0; i < noChainRules.size() && found == false; i++) {
			if (noChainRules.get(i).getLeftSide()
					.equals(Character.toString(caracter))) {
				s += noChainRules.get(i).getRightSide() + " ";
				// CONTINUAR AQUI
				found = true;
			}
		}
		return s;
	}

	public static void searchChainRules(String[] aux,
			ArrayList<Rule> noChainRules, ArrayList<Rule> chainRules, Rule r) {
		String s = new String();
		String leftSide = new String();
		for (int i = 0; i < aux.length; i++) {
			if (aux[i].length() == 1 && !aux[i].contains("|")
					&& Character.isUpperCase(aux[i].charAt(0))) {
				// primeiramente deve-se verificar se a regra da cadeia pode ser
				// resolvida diretamente
				if (foundChainRules(aux[i].charAt(0), s, noChainRules)) {
					// se a regra da cadeia puder ser selecionada imediatamente
					s = replaceChainRules(aux[i].charAt(0), s, noChainRules);
				}
			} else {
				s += aux[i] + " ";
			}
		}
		r.setRightSide(s);
	}

	public static int returnIndex(ArrayList<Rule> chainRules, String leftside) {
		int i = 0;
		boolean aux = false;
		for (; i < chainRules.size() && aux == false; i++) {
			if (chainRules.get(i).getLeftSide().equals(leftside))
				aux = true;
		}
		return i - 1;
	}
	
	//junta as regras para verificar se existe regras da cadeia
	public static Set<Rule> joinRules(Grammar g, Set<Rule> rulesTogheter) {
		for (String variable : g.getVariables()) {
			Rule r = new Rule();
			String newRule = new String();
			for (Rule element : g.getRules()) {
				if (variable.equals(element.getLeftSide()))
					newRule += element.getRightSide() + " | ";
			}
			r.setLeftSide(variable);
			r.setRightSide(newRule);
			rulesTogheter.add(r);
		}
		return rulesTogheter;
	}

	// remove regras da cadeia
	// retorna gramática sem regras da cadeia
	public static Grammar getGrammarWithoutChainRules(final Grammar g) {
		// primeiro passo, dividir as regras em dois conjuntos: possui chain
		// rules
		// e não possui chain rules
		Grammar gc = (Grammar) g.clone();
		
		ArrayList<Rule> noChainRules = new ArrayList<Rule>();
		ArrayList<Rule> chainRules = new ArrayList<Rule>();
		Set<Rule> rulesTogether = new HashSet<Rule>();
		rulesTogether = joinRules(gc, rulesTogether);
		// padronizando a gramática
		for (Rule element : rulesTogether) {
			System.out.println(element.getRightSide());
			String[] auxRightSide = element.getRightSide().split(" | ");
			for (int i = 0; i < auxRightSide.length; i++) {
				auxRightSide[i] = auxRightSide[i].trim();
			}
			if (isChain(auxRightSide)) {
				Rule r = new Rule();
				r.setLeftSide(element.getLeftSide());
				r.setRightSide(element.getRightSide());
				chainRules.add(r);
			} else {
				Rule r = new Rule();
				r.setLeftSide(element.getLeftSide());
				r.setRightSide(element.getRightSide());
				noChainRules.add(r);
			}
		}
		// enquanto houver chain rules
		while (chainRules.size() != 0) {
			System.out.println(chainRules.size());
			Rule r = new Rule();
			for (int i = 0; i < chainRules.size(); i++) {
				String[] aux = chainRules.get(i).getRightSide().split(" | ");
				for (int j = 0; j < aux.length; j++) {
					aux[j] = aux[j].trim();
				}
				r.setLeftSide(chainRules.get(i).getLeftSide());
				searchChainRules(aux, noChainRules, chainRules, r);
				System.out.println(r.getLeftSide() + " -> " + r.getRightSide());
				chainRules.remove(returnIndex(chainRules, r.getLeftSide()));
				for (int j = 0; j < chainRules.size(); j++)
					System.out.println(chainRules.get(j).getLeftSide() + " -> "
							+ chainRules.get(j).getRightSide());
				System.out.println(chainRules.size());
			}
			noChainRules.add(r);
		}
		System.out.println("--------------------");
		for (int j = 0; j < noChainRules.size(); j++)
			System.out.println(noChainRules.get(j).getLeftSide() + " -> "
					+ noChainRules.get(j).getRightSide());
		// utilizando gramática auxiliar para armazenar novas regras
		Grammar g2 = new Grammar();
		// copia elementos do ArrayList na gramática auxiliar
		for (int i = 0; i < noChainRules.size(); i++) {
			String[] rulesOnTheRightSide = noChainRules.get(i).getRightSide().split(" | ");
			for (int j = 0; j < rulesOnTheRightSide.length; j++) {
				rulesOnTheRightSide[j] = rulesOnTheRightSide[j].trim();
				if (!rulesOnTheRightSide[j].equals("|") && (!rulesOnTheRightSide[j].equals(""))) {
					System.out.println(rulesOnTheRightSide[j]);
					g2.insertRule(noChainRules.get(i).getLeftSide(), rulesOnTheRightSide[j]);
			
				}
			}
		}
		// copia gramática auxiliar na gramática principal
		gc.setRule(g2.getRules());
		for (Rule element : gc.getRules()) {
			System.out.println(element.getLeftSide() + " -> "
					+ element.getRightSide());
		}
		return gc;
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

	public static Grammar getGrammarWithoutNoTerm(Grammar g) {
		Set<String> term = new HashSet<>();
		Set<String> prev = new HashSet<>();
		Set<String> noTerm = new HashSet<String>();
		// preenche conjunto term com as variáveis que são terminais
		for (Rule element : g.getRules()) {
			if (element.getRightSide().length() == 1 && (!element.getRightSide().equals("|"))
						&& (Character.isLowerCase(element.getRightSide().charAt(0)) || element.getRightSide().charAt(0) == '.')) {
					term.add(element.getLeftSide());
			}
		}
		do {
			prev.addAll(term);
			for (Rule element : g.getRules()) {
				boolean insertOnTerm = false;
					if (element.getRightSide().length() == 1) {
						if (g.getTerminals().contains(
								element.getRightSide())
								|| prev.contains(element.getRightSide())) {
							insertOnTerm = true;
						}
					} else {
						for (int j = 0; j < element.getRightSide().length(); j++) {
							if (g.getTerminals().contains(
									Character.toString(element.getRightSide().charAt(j)))
									|| prev.contains(Character.toString(element.getRightSide().charAt(j)))) {
								insertOnTerm = true;
							} else {
								insertOnTerm = false;
							}
						}
				}
				if (insertOnTerm) {
					
						term.add(element.getLeftSide());
					
				} else {
					noTerm.add(element.getLeftSide());
				}
			}
		} while (!term.equals(prev));
		//term = termMinusNoTerm(term, noTerm);
		Grammar aux = new Grammar();
		aux.setVariables(prev);
		aux.setRule(updateRules(prev, g));
		aux.setTerminals(updateTerminals(aux));
		g.setVariables(aux.getVariables());
		g.setTerminals(aux.getTerminals());
		g.setRule(aux.getRules());
		return g;
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

	public static Grammar getGrammarWithoutNoReach(Grammar g) {
		Set<String> reach = new HashSet<>();
		Set<String> prev = new HashSet<>();
		Set<String> New = new HashSet<>();
		reach.add(g.getInitialSymbol());
		do {
			New.addAll(reachMinusPrev(reach, prev));
			prev.addAll(reach);
			for (String element : New) {
				for (Rule secondElement : g.getRules()) {
					if (secondElement.getLeftSide().equals(element)) {
						reach.addAll(variablesInW(reach,
								secondElement.getRightSide()));
					}
				}
			}
		} while (!reach.equals(prev));
		Grammar aux = new Grammar();
		aux.setVariables(prev);
		aux.setInitialSymbol(g.getInitialSymbol());
		aux.setRule(updateRules(prev, g));
		aux.setTerminals(updateTerminals(aux));
		g.setVariables(aux.getVariables());
		g.setTerminals(aux.getTerminals());
		g.setRule(aux.getRules());
		return aux;
	}

	public static boolean existsProduction(String leftSide, String symbol,
			Grammar g, Set<Rule> newSetOfRules) {
		for (Rule element : g.getRules()) {
			if (element.getLeftSide().charAt(0) != 'T') {
				String[] aux = element.getRightSide().split(" | ");
				for (int i = 0; i < aux.length; i++) {
					if (aux[i].equals(symbol)) {
						return true;
					}
				}
			}
		}
		for (Rule element : newSetOfRules) {
			if (element.getLeftSide().charAt(0) != 'T') {
				String[] aux = element.getRightSide().split(" | ");
				for (int i = 0; i < aux.length; i++) {
					if (aux[i].equals(symbol)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String getVariable(String symbol, Grammar g,
			Set<Rule> newSetOfRules) {
		String variable = new String();
		boolean found = false;
		for (Rule element : g.getRules()) {
			String[] aux = element.getRightSide().split(" | ");
			for (int i = 0; i < aux.length && found == false; i++) {
				aux[i] = aux[i].trim();
				if (aux[i].equals(symbol)) {
					variable = element.getLeftSide();
					found = true;
				}
			}
		}
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

	private static int counterLowerCaseLetters(String sentence) {
		int count = 0;
		boolean valid = true;
		for (int i = 1; i < sentence.length() && valid == true; i++) {
			if (Character.isUpperCase(sentence.charAt(i)))
				valid = false;
			else
				count++;
		}
		return count;
	}

	private static String insertRightRide(String sentence, int contInsertions,
			Set<Rule> newSetOfRules, Grammar g) {
		String aux = new String();
		if (sentence.length() == 2
				&& Character.isUpperCase(sentence.charAt(0))
				&& Character
						.isLowerCase(sentence.charAt(sentence.length() - 1))) {
			aux = sentence.substring(0, sentence.length() - 1) + "T"
					+ (contInsertions);
		} else if (Character.isDigit(sentence.charAt(1))
				&& sentence.length() > 2) {
			aux = Character.toString(sentence.charAt(0))
					+ Character.toString(sentence.charAt(1)) + "T"
					+ (contInsertions + 1);
		} else if (Character.isLowerCase(sentence.charAt(1))
				&& (Character.isLowerCase(sentence.charAt(2)))
				&& sentence.length() > 2) {
			newSetOfRules = insertNewRules(newSetOfRules, sentence,
					contInsertions, g);
			contInsertions = updateNumberOfInsertions(newSetOfRules,
					contInsertions);
			aux = Character.toString(sentence.charAt(0)) + "T" + contInsertions;
		} else if (sentence.length() > 2)
			aux = Character.toString(sentence.charAt(0)) + "T"
					+ (contInsertions + 1);
		else if (Character.isLowerCase(sentence.charAt(1))
				&& sentence.length() == 2)
			aux = Character.toString(sentence.charAt(0)) + "T"
					+ (contInsertions + 1);
		else
			aux = sentence;
		return aux;
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

	private static Set<Rule> insertNewRules(Set<Rule> newSetOfRules,
			String sentence, int contInsertions, Grammar g) {
		boolean valid = true;
		contInsertions += 1;
		for (int i = 1; i < sentence.length() && valid == true; i++) {
			if (Character.isLowerCase(sentence.charAt(i))) {
				if (!existsProduction("T" + contInsertions,
						Character.toString(sentence.charAt(i)), g,
						newSetOfRules)) {
					Rule r = new Rule();
					r.setLeftSide("T" + contInsertions);
					r.setRightSide(Character.toString(sentence.charAt(i)));
					newSetOfRules.add(r);
					contInsertions++;
				}
			} else
				valid = false;
		}
		return newSetOfRules;
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

	public static Grammar FNC(Grammar g) {

		Set<Rule> newSetOfRules = new HashSet<>();
		int contInsertions = 1;
		for (Rule element : g.getRules()) {
			String newProduction = new String();
				String sentence = element.getRightSide();
				int cont = 0;
				while (sentence.length() > 2) {
					if (Character.isLowerCase(sentence.charAt(0))) {
						if (cont == 0) {
							if (existsProduction(element.getLeftSide(),
									Character.toString(sentence.charAt(cont)),
									g, newSetOfRules)) {
								if (canInsert(newProduction)) {
									newProduction = getVariable(
											Character.toString(sentence
													.charAt(cont)), g,
											newSetOfRules);
								}
								sentence = sentence.substring(1);
							} else {
								// não há produções deste tipo, então uma
								// inserção é feita
								Rule r = new Rule();
								r.setLeftSide("T" + contInsertions);
								r.setRightSide(Character.toString(sentence
										.charAt(cont)));
								newSetOfRules.add(r);
								if (canInsert(newProduction)) {
									newProduction = "T" + contInsertions;
								}
								sentence = sentence.substring(1);
								contInsertions = updateNumberOfInsertions(
										newSetOfRules, contInsertions);
							}
							cont++;
						} else {
							// é minúsculo e está na segunda posição
							if (canInsert(newProduction)) {
								newProduction += "T" + contInsertions;
							}

							if (existsProduction(element.getLeftSide(),
									Character.toString(sentence.charAt(0)), g,
									newSetOfRules)) {
								Rule rule = new Rule();
								rule.setLeftSide("T" + contInsertions);
								rule.setRightSide(insertRightRide(
										getVariable(Character.toString(sentence
												.charAt(0)), g, newSetOfRules)
												+ sentence, contInsertions,
										newSetOfRules, g));
								newSetOfRules.add(rule);
								sentence = sentence.substring(1);
								contInsertions++;
							} else {
								Rule rule = new Rule();
								rule.setLeftSide("T" + (contInsertions + 1));
								rule.setRightSide(Character.toString(sentence
										.charAt(0)));
								newSetOfRules.add(rule);
								sentence = "T" + (contInsertions + 1)
										+ sentence.substring(1);
								Rule r = new Rule();
								r.setLeftSide("T" + contInsertions);
								r.setRightSide(insertRightRide(sentence,
										contInsertions + 1, newSetOfRules, g));
								newSetOfRules.add(r);
								if (Character.isDigit(sentence.charAt(1))) {
									sentence = sentence.substring(2);
								} else {
									sentence = sentence.substring(1);
								}
								contInsertions = updateNumberOfInsertions(
										newSetOfRules, contInsertions);
							}
						}
					} else if (!Character.isLowerCase(sentence.charAt(0))) {
						// maiúsculo na primeira posição
						if (cont == 0) {
							cont++;
							boolean control = false;
							if (canInsert(newProduction)) {
								newProduction += sentence.charAt(0);
								control = true;
							}
							if (Character.isDigit(sentence.charAt(1))) {
								if (control)
									newProduction += sentence.charAt(1);
								sentence = sentence.substring(2);
							} else {
								sentence = sentence.substring(1);
							}
						} else {
							// maiúsculo na segunda posição
							// verifica se produção já existe
							if (existsProduction(element.getLeftSide(),
									sentence, g, newSetOfRules)) {
								if (canInsert(newProduction)) {
									newProduction += getVariable(sentence, g,
											newSetOfRules);
								}
								Rule r = new Rule();
								r.setLeftSide("T" + contInsertions);
								r.setRightSide(insertRightRide(sentence,
										contInsertions, newSetOfRules, g));
								newSetOfRules.add(r);
								if (sentence.length() == 2)
									sentence += sentence.substring(2);
								else
									sentence += sentence.substring(1);
								contInsertions = updateNumberOfInsertions(
										newSetOfRules, contInsertions);
							} else {
								// se produção não existe
								boolean control = false;
								if (canInsert(newProduction)) {
									newProduction += "T" + contInsertions;
								}
								Rule r = new Rule();
								r.setLeftSide("T" + contInsertions);
								r.setRightSide(insertRightRide(sentence,
										contInsertions, newSetOfRules, g));
								newSetOfRules.add(r);
								contInsertions = updateNumberOfInsertions(
										newSetOfRules, contInsertions);
								if (Character.isDigit(sentence.charAt(1))) {
									if (control)
										newProduction += sentence.charAt(1);
									sentence = sentence.substring(2);
								} else {
									sentence = sentence.substring(1);
								}
							}
						}
					}
				}
				if (sentence.length() == 2) {
					if (Character.isUpperCase(sentence.charAt(0))
							&& Character.isUpperCase(sentence.charAt(1))) {
						if (!existsProduction(element.getLeftSide(), sentence,
								g, newSetOfRules)) {
							if (canInsert(newProduction)) {
								if (newProductionSize(newProduction) == 1) {
									Rule auxRule = new Rule();
									auxRule.setLeftSide("T" + contInsertions);
									auxRule.setRightSide(sentence);
									newSetOfRules.add(auxRule);
									newProduction += "T" + contInsertions;
									contInsertions = updateNumberOfInsertions(
											newSetOfRules, contInsertions);
								} else {
									newProduction += sentence;
								}
							} else {
								Rule lastRule = new Rule();
								lastRule.setLeftSide("T" + contInsertions);
								lastRule.setRightSide(sentence);
								newSetOfRules.add(lastRule);
								contInsertions = updateNumberOfInsertions(
										newSetOfRules, contInsertions);
							}
						} else {
							newProduction += sentence;}
					} else if (Character.isUpperCase(sentence.charAt(0))
							&& Character.isLowerCase(sentence.charAt(1))) {
						String secondChar = sentence.substring(sentence
								.length() - 1);
						if (!existsProduction(element.getLeftSide(),
								secondChar, g, newSetOfRules)) {
							sentence = sentence.substring(0,
									sentence.length() - 1)
									+ "T"
									+ contInsertions;
							Rule lastRule = new Rule();
							lastRule.setLeftSide("T" + contInsertions);
							lastRule.setRightSide(secondChar);
							newSetOfRules.add(lastRule);
						} else {
							sentence = sentence.substring(0,
									sentence.length() - 1)
									+ getVariable(secondChar, g, newSetOfRules);
							if (!existsProduction(element.getLeftSide(), sentence, g, newSetOfRules)) {
								Rule r = new Rule();
								r.setLeftSide("T" + contInsertions);
								r.setRightSide(sentence);
								newSetOfRules.add(r);
							}
						}
						if (canInsert(newProduction)) {
							if (newProductionSize(newProduction) == 1) {
								if (existsProduction(element.getLeftSide(), sentence, g, newSetOfRules)) {
									newProduction += getVariable(sentence, g, newSetOfRules);
								} else {
									newProduction += "T" + contInsertions;
								}
								//
								//newProduction += "T" + contInsertions;
								contInsertions = updateNumberOfInsertions(
										newSetOfRules, contInsertions);
							} else {
								newProduction += sentence;
							}
						} else {
							Rule auxRule = new Rule();
							auxRule.setLeftSide("T" + contInsertions);
							auxRule.setRightSide(sentence);
							newSetOfRules.add(auxRule);
							contInsertions = updateNumberOfInsertions(
									newSetOfRules, contInsertions);
						}
						
					} else if (Character.isLowerCase(sentence.charAt(0))
							&& Character.isUpperCase(sentence.charAt(1))) {
						if (!existsProduction(element.getLeftSide(),
								Character.toString(sentence.charAt(0)), g,
								newSetOfRules)) {
							Rule auxRule = new Rule();
							auxRule.setLeftSide("T" + (contInsertions + 1));
							auxRule.setRightSide(Character.toString(sentence
									.charAt(0)));
							newSetOfRules.add(auxRule);
							sentence = "T" + (contInsertions + 1)
									+ sentence.substring(1);
						} else {
							sentence = getVariable(
									Character.toString(sentence.charAt(0)), g,
									newSetOfRules) + sentence.substring(1);
						}
						if (canInsert(newProduction)) {
							if (newProductionSize(newProduction) == 1) {
								if (!existsProduction(element.getLeftSide(), sentence, g, newSetOfRules)) {
									Rule auxRule = new Rule();
									auxRule.setLeftSide("T" + contInsertions);
									auxRule.setRightSide(sentence);
									newSetOfRules.add(auxRule);
									newProduction += "T" + contInsertions;
									contInsertions = updateNumberOfInsertions(
										newSetOfRules, contInsertions);
								} else {
									newProduction += getVariable(sentence, g, newSetOfRules);
								}
								
							} else {
								newProduction += sentence;
							}
						} else {
							Rule auxRule = new Rule();
							auxRule.setLeftSide("T" + contInsertions);
							auxRule.setRightSide(sentence);
							newSetOfRules.add(auxRule);
							contInsertions = updateNumberOfInsertions(
									newSetOfRules, contInsertions);
						}
					} else if (Character.isLowerCase(sentence.charAt(0))
							&& Character.isLowerCase(sentence.charAt(1))) {
						String secondChar;
						if (!existsProduction(element.getLeftSide(),
								Character.toString(sentence.charAt(0)), g,
								newSetOfRules)) {
							Rule auxRule = new Rule();
							auxRule.setLeftSide("T" + contInsertions);
							auxRule.setRightSide(Character.toString(sentence
									.charAt(0)));
							newSetOfRules.add(auxRule);
							sentence = "T" + (contInsertions)
									+ sentence.substring(1);
							contInsertions = updateNumberOfInsertions(
									newSetOfRules, contInsertions);
							secondChar = Character.toString(sentence.charAt(2));
						} else {
							sentence = getVariable(
									Character.toString(sentence.charAt(0)), g,
									newSetOfRules) + sentence.substring(1);
							secondChar = assignsChar(sentence);
						}
						if (!existsProduction(element.getLeftSide(),
								secondChar, g, newSetOfRules)) {
							Rule auxRule = new Rule();
							auxRule.setLeftSide("T" + contInsertions);
							auxRule.setRightSide(secondChar);
							newSetOfRules.add(auxRule);
							sentence = sentence.substring(0,
									sentence.length() - 1)
									+ "T"
									+ contInsertions;
							contInsertions = updateNumberOfInsertions(
									newSetOfRules, contInsertions);
						} else {
							sentence = sentence.substring(0,
									sentence.length() - 1)
									+ getVariable(secondChar, g, newSetOfRules);
						}
						if (canInsert(newProduction)) {
							if (newProductionSize(newProduction) == 1) {
								Rule auxRule = new Rule();
								auxRule.setLeftSide("T" + contInsertions);
								auxRule.setRightSide(sentence);
								newSetOfRules.add(auxRule);
								newProduction += "T" + contInsertions;
								contInsertions = updateNumberOfInsertions(
										newSetOfRules, contInsertions);
							} else
								newProduction += sentence;
						} else {
							Rule auxRule = new Rule();
							auxRule.setLeftSide("T" + contInsertions);
							auxRule.setRightSide(sentence);
							newSetOfRules.add(auxRule);
							contInsertions = updateNumberOfInsertions(
									newSetOfRules, contInsertions);
						}
					}
				} else if (sentence.length() == 1) {
					if (canInsert(newProduction))
						newProduction += sentence;
				}
				newProduction += " | ";
			if (newProduction.charAt(newProduction.length() - 2) == '|') {
				newProduction = newProduction.substring(0,
						newProduction.length() - 2);
				newProduction = newProduction.trim();
			}
			Rule r = new Rule();
			r.setLeftSide(element.getLeftSide());
			r.setRightSide(newProduction);
			newSetOfRules.add(r);
		}
		g.setRule(newSetOfRules);
		System.out.println();
		for (Rule element : g.getRules())
			System.out.println(element.getLeftSide() + "->" + element.getRightSide());
		return g;
	}

	private static int newProductionSize(String newProduction) {
		int count = 0;
		String[] aux = newProduction.split(" | ");
		for (int i = 0; i < aux[aux.length - 1].length(); i++) {
			if (Character.isLetter(aux[aux.length - 1].charAt(i)))
				count++;
		}
		return count;
	}

	private static String assignsChar(String sentence) {
		String result;
		if (Character.isDigit(sentence.charAt(1)))
			result = sentence.substring(2);
		else
			result = sentence.substring(1);
		return result;
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
		int count = 3;
		
		X = fillOthersLines(X, g, count, line, column, word);
		
		return X;
	}
	
	private static Set<String>[][] fillOthersLines(Set<String>[][] x, Grammar g, int count, int line, int column, String word) {
		while (count != 7) {
			while (count + column <= word.length()) {
				String firstCell = returnsAlphabeticSymbols(x[5][column]);
				String secondCell = returnsAlphabeticSymbols(x[line - 1][column + 1]);
				
				for (int counterOfFirstCell = 0; counterOfFirstCell < firstCell.length(); counterOfFirstCell++) { 
					String sentence = new String();
					sentence += Character.toString(firstCell.charAt(counterOfFirstCell)).trim();
					for (int counterOfSecondCell = 0; counterOfSecondCell < secondCell.length(); counterOfSecondCell++) { 
						sentence += Character.toString(secondCell.charAt(counterOfSecondCell)).trim();
						Set<String> aux = checksEquality(g, word, sentence);
						x[line - 2][column].addAll(aux);
						sentence = sentence.substring(0, sentence.length() - 1);
					}
				}
				
				firstCell = returnsAlphabeticSymbols(x[line - 1][column]);						
				secondCell = returnsAlphabeticSymbols(x[5][column + (count - 1)]);				
				for (int counterOfFirstCell = 0; counterOfFirstCell < firstCell.length(); counterOfFirstCell++) { 
					String sentence = new String();
					sentence += Character.toString(firstCell.charAt(counterOfFirstCell)).trim();
					for (int counterOfSecondCell = 0; counterOfSecondCell < secondCell.length(); counterOfSecondCell++) { 
						sentence += Character.toString(secondCell.charAt(counterOfSecondCell)).trim();
						Set<String> aux = checksEquality(g, word, sentence);
						x[line - 2][column].addAll(aux);
						sentence = sentence.substring(0, sentence.length() - 1);
					}
				}
				column++;
			}
			column = 0;
			count++;
			line--;
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