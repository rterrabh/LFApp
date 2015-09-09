package vo;

import android.support.annotation.NonNull;

import com.lfapp.lfapp_01.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Grammar implements Cloneable {

	public final String lambda = "λ";
	public final String ruleSeparator = "|";
	public final String ruleProduction = "->";
	public final String chomskyPrefix = "T";
	public final String recursiveRemovalPrefix = "Z";

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
		this.variables.add(initialSymbol);

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

	public void setRules(Set<Rule> set) {
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

	public void insertRule (Rule r) {
		rules.add(r);
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
			rules.add((Rule) r.clone());
		}
		gc.setRules(rules);
		return gc;
	}

	// algorithms

	/**
	 * 
	 * @param : gramática livre de contexto
	 * @return : gramática livre de contexto sem recursão no símbolo inicial
	 */
	public Grammar getGrammarWithInitialSymbolNotRecursive(final Grammar g, final AcademicSupport academicSupport) {
		Grammar gc = (Grammar) g.clone();
		StringBuilder comments = new StringBuilder();
		comments.append("O símbolo inicial deve se limitar a iniciar derivações, não podendo ser uma variável recursiva." +
				"Logo, não deve ser possível ter derivações do tipo " + gc.getInitialSymbol() + " ⇒∗ αSβ.\n");
		Map<Integer, String> problems = new HashMap<>();
		String initialSymbol = gc.getInitialSymbol();
		boolean insert = false;
		int counter = 1;
		for (Rule element : gc.getRules()) {
			if (element.getLeftSide().equals(initialSymbol) && element.getRightSide().contains(initialSymbol)) {
				insert = true;
				problems.put(counter, "Recursão encontrada na regra: " + element.getLeftSide() +" -> " + element.getRightSide() + "\n");
				counter++;
			}
		}
		boolean situation = false;
		StringBuilder solutionDescription = new StringBuilder();
		if (insert == true) {
			situation = true;
			solutionDescription.append("A gramática inserida possui o símbolo inicial recursivo. Logo, é necessário realizar a seguinte transformação: \n");
			solutionDescription.append("\tAssuma a GLC G = (V , Σ, P, " + gc.getInitialSymbol() + ") onde S é recursivo;\n");
			solutionDescription.append("\tEntão existe um GLC G' = (V ∪ {" + gc.getInitialSymbol() + "' }, Σ, P ∪ {" +
			 gc.getInitialSymbol() + "' → " + gc.getInitialSymbol() + "}, " + gc.getInitialSymbol() + "' );\n");
			solutionDescription.append("\tL(G ) = L(G);\n");
			solutionDescription.append("\tSímbolo inicial S de G não é mais recursivo.\n");
			Rule r = new Rule(initialSymbol + "'", initialSymbol);
			gc.insertRule(r);
			gc.setInitialSymbol(initialSymbol + "'");
			academicSupport.insertNewRule(r);
		} else {
			situation = false;
		}
		gc.insertVariable(gc.getInitialSymbol());

		//seta feedback acadêmico no objeto
		academicSupport.setComments(comments.toString());
		academicSupport.setFoundProblems(problems);
		academicSupport.setResult(gc);
		academicSupport.setSituation(situation);
		academicSupport.setSolutionDescription(solutionDescription.toString());
		return gc;
	}

	/**
	 * 
	 * @param : gramática livre de contexto
	 * @return : gramática livre de contexto essencialmente não contrátil
	 */
	public Grammar getGrammarEssentiallyNoncontracting(final Grammar g, final AcademicSupport academicSupport) {
		Grammar gc = (Grammar) g.clone();
		Set<String> nullable = new HashSet<String>();
		Set<Rule> setOfRules = new HashSet<Rule>();
		boolean nullableVars = false;

		// nullable = nullable U A -> . | A E V
		Map<Integer, String> foundProblems = new HashMap<>();
		int counter = 1;
		for (Rule element : gc.getRules()) {
			if (element.getRightSide().equals(g.lambda)) {
				nullable.add(element.getLeftSide());
				nullableVars = true;
				academicSupport.insertIrregularRule(element);
				foundProblems.put(counter, "- A regra " + element + " é uma produção vazia.");
				counter++;
			} else {
				Rule r = new Rule(element.getLeftSide(), element.getRightSide());
				setOfRules.add(r);
			}
		}
		academicSupport.setSituation(nullableVars);

		// gera conjuntos de variáveis anuláveis
		academicSupport.insertOnFirstSet(nullable, "Lambda");
		nullableVars = false;
		Set<String> prev = new HashSet<String>();
		do {
			prev.addAll(nullable);
			academicSupport.insertOnSecondSet(prev, "Lambda");
			for (Rule element : gc.getRules()) {
				if (GrammarParser.prevContainsVariable(prev, element.getRightSide())) {
					nullable.add(element.getLeftSide());
					nullableVars = true;
					academicSupport.insertOnFirstSet(nullable, "Lambda");

				}
			}
		} while (!prev.equals(nullable));

		Set<Rule> newSetOfRules = new HashSet<Rule>();
		//academicSupportAux.delete(0, academicSupportAux.length());
		nullableVars = false;
		for (Rule element : setOfRules) {
			String aux = element.getRightSide() + " | ";
			int i = 0;
			while (i != element.getRightSide().length()) {
				aux += GrammarParser.permutation(element.getRightSide(), nullable, i, aux);
				i++;
			}
			String[] productionsOnRightSide = aux.split(" | ");
			for (i = 0; i < productionsOnRightSide.length; i++) {
				productionsOnRightSide[i] = productionsOnRightSide[i].trim();
				if (!productionsOnRightSide[i].equals("|")) {
					if (GrammarParser.newProductions(productionsOnRightSide[i], element.getLeftSide(), gc)) {
						nullableVars = true;
					}
					Rule r = new Rule(element.getLeftSide(), productionsOnRightSide[i]);
					newSetOfRules.add(r);
					if (!g.getRules().contains(r)) {
						academicSupport.insertNewRule(r);
					}
				}
			}
		}

		if (nullable.contains(gc.getInitialSymbol())) {
			Rule r = new Rule(gc.getInitialSymbol(), g.lambda);
			newSetOfRules.add(r);

		}

		//seta feedback acadêmico no objeto
		academicSupport.setFoundProblems(foundProblems);
		academicSupport.setResult(gc);

		gc.setRules(newSetOfRules);

		return gc;
	}

	/**
	 * 
	 * @param : gramática livre de contexto
	 * @return : gramática livre de contexto sem regras da cadeia
	 */
	public Grammar getGrammarWithoutChainRules(final Grammar g, final AcademicSupport academicSupport) {
		Grammar gc = (Grammar) g.clone();

		// primeiramente, deve-se construir os subconjuntos
		Map<String, Set<String>> setOfChains = new HashMap<String, Set<String>>();
		StringBuilder academicSupportAux = new StringBuilder();
		for (String variable : gc.getVariables()) {
			// conjunto que representa o chain de determinada variável
			Set<String> chain = new HashSet<String>();
			Set<String> prev = new HashSet<String>();
			Set<String> New = new HashSet<String>();
			chain.add(variable);
			do {
				New = GrammarParser.chainMinusPrev(chain, prev);
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
			Set<String> setOfVariables = new HashSet<>();
			setOfVariables.add(variable);
			academicSupport.insertOnFirstSet(setOfVariables, "Chain");
			academicSupport.insertOnSecondSet(chain, "Chain");
		}

		// iterações sobre os conjuntos de chains
		Set<Rule> newSetOfRules = new HashSet<Rule>();
		for (String variable : gc.getVariables()) {
			Set<String> chainsOfVariable = setOfChains.get(variable);
			for (String variableChain : chainsOfVariable) {
				for (Rule element : gc.getRules()) {
					if (element.getLeftSide().equals(variableChain)) {
						if (element.getRightSide().length() != 1 || !Character.isUpperCase(element.getRightSide().charAt(0))) {
							Rule r = new Rule(variable, element.getRightSide());
							newSetOfRules.add(r);
							if (chainsOfVariable.size() != 1 && !gc.getRules().contains(r)) {
								academicSupport.insertNewRule(r);
							}
						}
					}
				}
			}
		}

		gc.setRules(newSetOfRules);
		return gc;
	}

	/**
	 * 
	 * @param : gramática livre de contexto
	 * @return : gramática livre de contexto sem símbolos não terminais
	 */
	public Grammar getGrammarWithoutNoTerm(final Grammar g, final AcademicSupport academicSupport) {
		Set<String> term = new HashSet<>();
		Set<String> prev = new HashSet<>();
		Grammar gc = (Grammar) g.clone();

		// preenche conjunto term com as variáveis que são terminais
		int cont = 1;
		for (Rule element : gc.getRules()) {
			if (element.getRightSide().length() == 1 && (!element.getRightSide().equals("|")) && (Character.isLowerCase(element.getRightSide().charAt(0)) || element.getRightSide().charAt(0) == '.')) {
				if (!term.contains(element.getLeftSide())) {
					term.add(element.getLeftSide());
					cont++;
				}
			}
		}

		academicSupport.insertOnFirstSet(term, "TERM");
		academicSupport.insertOnSecondSet(prev, "TERM");
		String term1 = term.toString();
		do {
			prev.addAll(term);
			String prev1 = prev.toString();
			academicSupport.insertOnSecondSet(prev, "TERM");
			for (Rule element : gc.getRules()) {
				boolean insertOnTerm = true;
				for (int j = 0; j < element.getRightSide().length()
						&& insertOnTerm == true; j++) {
					if (Character.isLowerCase(element.getRightSide().charAt(j))	&& !gc.getTerminals().contains(Character.toString(element.getRightSide().charAt(j)))) {
						insertOnTerm = false;
					} else if (Character.isUpperCase(element.getRightSide().charAt(j)) && !prev.contains(Character.toString(element.getRightSide().charAt(j)))) {
						insertOnTerm = false;
					}
				}
				if (insertOnTerm) {
					if (!term.contains(element.getLeftSide())) {
						term.add(element.getLeftSide());
						academicSupport.insertOnFirstSet(term, "TERM");
						String term2 = term.toString();
						cont++;
					}
				}
			}
		} while (!term.equals(prev));

		if (term.size() != gc.getVariables().size()) {
			academicSupport.setSituation(true);
		} else {
			academicSupport.setSituation(false);
		}

		Grammar aux = new Grammar();
		aux.setVariables(prev);
		aux.setRules(GrammarParser.updateRules(prev, gc, academicSupport));
		aux.setTerminals(GrammarParser.updateTerminals(aux));
		gc.setVariables(aux.getVariables());
		gc.setTerminals(aux.getTerminals());
		gc.setRules(aux.getRules());

		return gc;
	}

	/**
	 * 
	 * @param : gramática livre de contexto
	 * @return : gramática livre de contexto sem símbolos não alcançáveis
	 */
	public Grammar getGrammarWithoutNoReach(final Grammar g, final AcademicSupport academicSupport) {
		Set<String> reach = new HashSet<>();
		Set<String> prev = new HashSet<>();
		Set<String> New = new HashSet<>();
		Grammar gc = (Grammar) g.clone();
		reach.add(gc.getInitialSymbol());
		academicSupport.insertOnFirstSet(reach, "REACH");
		academicSupport.setSituation(true);
		do {
			New = GrammarParser.reachMinusPrev(reach, prev);
			academicSupport.insertOnThirdSet(New, "REACH");
			prev.addAll(reach);
			academicSupport.insertOnSecondSet(prev, "REACH");
			for (String element : New) {
				for (Rule secondElement : gc.getRules()) {
					if (secondElement.getLeftSide().equals(element)) {
						reach.addAll(GrammarParser.variablesInW(reach, secondElement.getRightSide()));
						academicSupport.insertOnFirstSet(reach, "REACH");
					}
				}
			}
		} while (!reach.equals(prev));
		Grammar aux = new Grammar();
		aux.setVariables(prev);
		aux.setInitialSymbol(gc.getInitialSymbol());
		aux.setRules(GrammarParser.updateRules(prev, gc, academicSupport));
		aux.setTerminals(GrammarParser.updateTerminals(aux));
		gc.setVariables(aux.getVariables());
		gc.setTerminals(aux.getTerminals());
		gc.setRules(aux.getRules());
		return aux;
	}

	/**
	 * 
	 * @param : gramática livre de contexto
	 * @return : gramática livre de contexto na Forma Normal de Chomsky
	 */
	public Grammar FNC(final Grammar g) {

		Grammar gc = (Grammar) g.clone();

		//gc = g.getGrammarWithInitialSymbolNotRecursive(gc);
		//gc = g.getGrammarEssentiallyNoncontracting(gc);
		//gc = g.getGrammarWithoutChainRules(gc);
		//gc = g.getGrammarWithoutNoTerm(gc);
		//gc = g.getGrammarWithoutNoReach(gc);

		if (!GrammarParser.isFNC(gc)) {

			Set<Rule> newSetOfRules = new HashSet<Rule>();
			Set<Rule> auxSetOfRules = new HashSet<Rule>();
			Set<String> newSetOfVariables = new HashSet<String>();
			int contInsertions = 1;
			for (Rule element : gc.getRules()) {
				String newProduction = new String();
				String sentence = element.getRightSide();
				int cont = 0;
				int changeCounter = 0;
				// primeiro, cria-se produções para todos os símbolos terminais
				String newSentence = new String();
				if (GrammarParser.sentenceSize(sentence) >= 2) {
					for (int i = 0; i < sentence.length(); i++) {
						if (Character.isLowerCase(sentence.charAt(i))) {
							if (!GrammarParser.existsProduction(
									element.getLeftSide(),
									Character.toString(sentence.charAt(i)),
									newSetOfRules)) {
								Rule r = new Rule();
								r.setLeftSide("T" + contInsertions);
								r.setRightSide(Character.toString(sentence
										.charAt(i)));
								newSetOfRules.add(r);
								newSentence += "T" + contInsertions;
								newSetOfVariables.add("T" + contInsertions);//
								contInsertions = GrammarParser
										.updateNumberOfInsertions(
												newSetOfRules, contInsertions);
							} else {
								newSentence += GrammarParser.getVariable(
										Character.toString(sentence.charAt(i)),
										newSetOfRules);
							}
						} else {
							newSentence += Character.toString(sentence
									.charAt(i));
						}
					}
				} else {
					Rule r = new Rule();
					r.setLeftSide(element.getLeftSide());
					r.setRightSide(element.getRightSide());
					auxSetOfRules.add(r);
				}
				if (GrammarParser.sentenceSize(newSentence) > 2) {
					while (cont < newSentence.length()) {
						if (Character.isLetter(newSentence.charAt(cont))) {
							if (changeCounter == 1) {
								if (GrammarParser.canInsert(newProduction)) {
									newProduction += "T" + contInsertions;
								}
								String insertOnRightSide = GrammarParser
										.splitSentence(cont, newSentence,
												contInsertions);
								newSentence = GrammarParser
										.splitSentence(newSentence);
								Rule r = new Rule();
								r.setLeftSide("T" + contInsertions);
								r.setRightSide(insertOnRightSide);
								newSetOfRules.add(r);
								newSetOfVariables.add("T" + contInsertions);
								contInsertions = GrammarParser
										.updateNumberOfInsertions(
												newSetOfRules, contInsertions);
								changeCounter = 0;
								cont = -1;
							} else {
								if (GrammarParser.canInsert(newProduction)) {
									newProduction += GrammarParser
											.partialSentence(newSentence);
									newSentence = GrammarParser
											.splitSentence(newSentence);
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
				} else if (GrammarParser.sentenceSize(newSentence) == 2) {
					Rule r = new Rule();
					r.setLeftSide(element.getLeftSide());
					r.setRightSide(newSentence);
					newSetOfRules.add(r);
				}
			}
			// update the variables
			for (String variable : newSetOfVariables) {
				gc.insertVariable(variable);
			}

			// update the rules
			newSetOfRules.addAll(auxSetOfRules);
			gc.setRules(newSetOfRules);
		}
		return gc;
	}

	/**
	 * 
	 * @param : gramática livre de contexto
	 * @return : gramática livre de contexto sem recursão direta
	 */
	public Grammar removingTheImmediateLeftRecursion(final Grammar g, final AcademicSupport academicSupport) {

		Grammar gc = (Grammar) g.clone();

		// se gramática não for não contrátil ou essencialmente não contrátil,
		// método não aceita e retorna
		// a mesma gramática, caso contrário, a remoção é feita

		// primeira coisa: verificar quais variáveis possuem recursão à esquerda
		Set<String> haveRecursion = new HashSet<String>();
		int aux = 1;
		for (Rule element : gc.getRules()) {
				if (element.getLeftSide().equals(Character.toString(element.getRightSide().charAt(0)))) {
				haveRecursion.add(element.getLeftSide());
			}
		}

		// estabelece relacao entre variável que gera recursão e a variável que
		// irá resolver esta recursão
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

		// já é posśivel saber quem possui recursão e onde ela está, sendo
		// possível removê-la
		Set<Rule> newSetOfRules = new HashSet<Rule>();
		Set<String> newSetOfVariables = new HashSet<String>();
		for (Rule element : gc.getRules()) {
			if (variablesMapped.containsKey(element.getLeftSide()) && !element.getRightSide().equals(".")) {
				if (element.getLeftSide().equals(Character.toString(element.getRightSide().charAt(0)))) {
					// recursão encontrada
					Rule firstProduction = new Rule();
					firstProduction.setLeftSide(variablesMapped.get(element.getLeftSide()));
					firstProduction.setRightSide(element.getRightSide().substring(1) + variablesMapped.get(element.getLeftSide()));
					newSetOfRules.add(firstProduction);
					newSetOfVariables.add(variablesMapped.get(element.getLeftSide()));
					Rule secondProduction = new Rule();
					secondProduction.setLeftSide(firstProduction.getLeftSide());
					secondProduction.setRightSide(element.getRightSide().substring(1));
					newSetOfRules.add(secondProduction);
					//academicSupport.append("("+aux+") Recursão encontrada em "+element+". Adicionando regras " + firstProduction + " e " + secondProduction + ".\n");
					aux++;
				} else {
					// sem recursão, mas tratamento é necessário
					Rule firstProduction = new Rule();
					firstProduction.setLeftSide(element.getLeftSide());
					firstProduction.setRightSide(element.getRightSide());
					newSetOfRules.add(firstProduction);
					Rule secondProduction = new Rule();
					secondProduction.setLeftSide(firstProduction.getLeftSide());
					secondProduction.setRightSide(element.getRightSide() + variablesMapped.get(element.getLeftSide()));
					newSetOfRules.add(secondProduction);
					//academicSupport.append("("+aux+") Não existe recursão na regra " + element +", porém tratamento é necessário. Pois outras regras produzidas" +
					//		"por esta variável são recursivas. São inseridas as regras " + firstProduction + " e " + secondProduction + "\n.");
					aux++;
				}
			} else {
				// variável não produz recursão à esquerda
				newSetOfRules.add(element);
			}
		}

		// seta as regras alteradas à gramática clonada
		gc.setRules(newSetOfRules);

		// adiciona variáveis criadas no processo à gramática clonada
		for (String variable : newSetOfVariables) {
			gc.insertVariable(variable);
		}

		return gc;
	}

	/**
	 * 
	 * @param : gramática livre de contexto
	 * @return : gramática livre de contexto sem recursão direta e indireta
	 */
	public Grammar removingLeftRecursion(final Grammar g, final StringBuilder academicSupport) {
		Grammar gc = (Grammar) g.clone();
		Set<String> olderVariables = gc.getVariables();

		int aux = 2;
		academicSupport.append("(1) Ordenando as variáveis");

		if (GrammarParser.checksGrammar(gc)) {
			// ordenando os símbolos não terminais
			Map<String, String> variablesInOrder = new HashMap<String, String>();
			int counter = 1;
			variablesInOrder.put(gc.getInitialSymbol(),	Integer.toString(counter));
			for (String element : gc.getVariables()) {
				if (!element.equals(gc.getInitialSymbol())) {
					counter++;
					variablesInOrder.put(element, Integer.toString(counter));
				}
			}

			while (GrammarParser.existsRecursion(gc, variablesInOrder, olderVariables)) {
				// verifica se gramática possui recursão direta
				if (GrammarParser.existsDirectRecursion(gc)) {
					gc = GrammarParser.removingTheImmediateLeftRecursion(gc);
				}

				// gramática possui recursão indireta
				Set<Rule> newSetOfRules = new HashSet<Rule>();
				for (String variable : gc.getVariables()) {
					if (olderVariables.contains(variable)) {
						for (Rule element : gc.getRules()) {
							if (variable.equals(element.getLeftSide())) {
								int u = Integer.parseInt(variablesInOrder.get(variable));
								String rightSide = GrammarParser.getsFirstCharacter(element.getRightSide());
								if (Character.isLowerCase(rightSide.charAt(0)) || ".".equals(rightSide)) {
									Rule r = new Rule(variable, element.getRightSide());
									newSetOfRules.add(r);
								} else {
									int v = Integer.parseInt(variablesInOrder.get(rightSide));
									if (u > v) {
										for (Rule secondElement : gc.getRules()) {
											if (rightSide.equals(secondElement.getLeftSide())) {
												String newProduction = secondElement.getRightSide();
												newProduction += element.getRightSide().substring(GrammarParser.indexOfFirstChar(element.getRightSide()));
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
				// atualiza newSetOfRules
				for (Rule element : gc.getRules()) {
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
	 * 
	 * @param : gramática livre de contexto
	 * @return : gramática livre de contexto na Forma Normal de Greibach
	 */
	public Grammar FNG(final Grammar g) {
		Grammar gc = (Grammar) g.clone();
		gc = g.FNC(gc);
		//gc = g.removingLeftRecursion(gc);

		if (GrammarParser.grammarWithCycles(gc)) {
			while (!GrammarParser.isFNG(gc.getRules())) {
				Set<Rule> newSetOfRules = new HashSet<Rule>();
				for (String variable : gc.getVariables()) {
					for (Rule element : gc.getRules()) {
						if (variable.equals(element.getLeftSide())) {
							if (!Character.isLowerCase(element.getRightSide().charAt(0)) && !element.getRightSide().equals(".")) {
								String firstCharacter = GrammarParser.getsFirstCharacter(element.getRightSide());
								for (Rule secondElement : gc.getRules()) {
									if (secondElement.getLeftSide().equals(firstCharacter)) {
										String newRightSide = secondElement.getRightSide();
										if (GrammarParser.counterOfRightSide(element.getRightSide()) != 1) {
											newRightSide += element.getRightSide().substring(GrammarParser.indexOfFirstChar(element.getRightSide()));
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

}
