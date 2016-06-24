package vo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;

public class Grammar implements Cloneable {

	public static final String LAMBDA = ".";
	public static final String RULE_SEPARATOR = "|";
	public static final String RULE_PRODUCTION = "->";
	public static final String CHOMSKY_PREFIX = "T";
	public static final String RECURSIVE_REMOVAL_PREFIX = "Z";

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
			String[] rulesOnTheRightSide = auxRule[1].split("[|]");
			for (String production : rulesOnTheRightSide) {
				production = production.trim();
				if (!production.isEmpty()) {
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
		String align = "justify";
		comments.append("<p align="+ align + ">O símbolo inicial deve se limitar a iniciar derivações, não podendo ser uma variável recursiva. " +
				" Logo, não deve ser possível ter derivações do tipo " + gc.getInitialSymbol() + " ⇒<sup>∗</sup> αSβ.<br><br>");
		Map<Integer, String> problems = new HashMap<>();
		String initialSymbol = gc.getInitialSymbol();
		boolean insert = false;
		int counter = 1;
		for (Rule element : gc.getRules()) {
			if (element.getRightSide().contains(initialSymbol)) {
				insert = true;
				//problems.put(counter, "Recursão encontrada na regra: " + element.getLeftSide() +" -> " + element.getRightSide() + "\n");
				counter++;
			}
		}
		boolean situation = false;
		StringBuilder solutionDescription = new StringBuilder();
		if (insert == true) {
			situation = true;
			solutionDescription.append("A gramática G = (V, Σ, P, " + gc.getInitialSymbol() + ") possui o símbolo inicial " + gc.getInitialSymbol() + " recursivo. Logo,");
			solutionDescription.append(" existe uma GLC G' = (V ∪ {" + gc.getInitialSymbol() + "' }, Σ, P ∪ {" +
			 gc.getInitialSymbol() + "' → " + gc.getInitialSymbol() + "}, " + gc.getInitialSymbol() + "' ), tal que L(G') = L(G) e o novo símbolo inicial " + gc.getInitialSymbol() +" não é recursivo.</p><br>");
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
		Set<String> nullable = new HashSet<>();
		Set<String> prev = new HashSet<>();
		Set<Rule> setOfRules = new HashSet<>();
		boolean nullableVars = false;

		// nullable = nullable U A -> . | A E V
		Map<Integer, String> foundProblems = new HashMap<>();
		int counter = 1;
		for (Rule element : gc.getRules()) {
			if (element.getRightSide().equals(LAMBDA)) {
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
		academicSupport.insertOnFirstSet(nullable, "Lambda");
		Set<String> auxSet = new HashSet<>();
		auxSet.add("");
		academicSupport.insertOnSecondSet(auxSet, "Lambda");
		nullableVars = false;
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

		Set<Rule> newSetOfRules = new HashSet<>();
		//academicSupportAux.delete(0, academicSupportAux.length());
		nullableVars = false;
		for (Rule element : setOfRules) {
			String aux = GrammarParser.combination(element.getRightSide(), nullable);
			aux = aux.substring(0, aux.length()-3);
			String[] productionsOnRightSide = aux.split("[|]");
			for (int i = 0; i < productionsOnRightSide.length; i++) {
				productionsOnRightSide[i] = productionsOnRightSide[i].trim();
				if (GrammarParser.newProductions(productionsOnRightSide[i], element.getLeftSide(), gc)) {
					nullableVars = true;
				}
				if(productionsOnRightSide.length > 0 && !productionsOnRightSide.equals("")) {
					Rule r = new Rule(element.getLeftSide(), productionsOnRightSide[i]);
					newSetOfRules.add(r);
					if (!g.getRules().contains(r)) {
						academicSupport.insertNewRule(r);
					}
				}
			}
		}

		if (nullable.contains(gc.getInitialSymbol())) {
			Rule r = new Rule(gc.getInitialSymbol(), LAMBDA);
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
							academicSupport.insertIrregularRule(element);
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
		HashSet<String> auxSet = new HashSet<>();
		auxSet.add("");
		academicSupport.insertOnSecondSet(auxSet, "REACH");
		academicSupport.insertOnThirdSet(auxSet, "REACH");
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
	public Grammar FNC(final Grammar g, final AcademicSupport academic) {

		Grammar gc = (Grammar) g.clone();
		gc = getGrammarWithInitialSymbolNotRecursive(gc, new AcademicSupport());
		gc = getGrammarEssentiallyNoncontracting(gc, new AcademicSupport());
		gc = getGrammarWithoutChainRules(gc, new AcademicSupport());
		gc = getGrammarWithoutNoTerm(gc, new AcademicSupport());
		gc = getGrammarWithoutNoReach(gc, new AcademicSupport());
		if (!GrammarParser.isFNC(gc)) {
			academic.setSituation(true);
			Set<Rule> newSetOfRules = new HashSet<>();
			Set<Rule> auxSetOfRules = new HashSet<>();
			Set<String> newSetOfVariables = new HashSet<>();
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
							if (!GrammarParser.existsProduction(element.getLeftSide(), Character.toString(sentence.charAt(i)), newSetOfRules)) {
								Rule r = new Rule();
								r.setLeftSide(CHOMSKY_PREFIX + contInsertions);
								r.setRightSide(Character.toString(sentence.charAt(i)));
								academic.insertNewRule(r);
								newSetOfRules.add(r);
								newSentence += CHOMSKY_PREFIX + contInsertions;
								newSetOfVariables.add(CHOMSKY_PREFIX + contInsertions);//
								contInsertions = GrammarParser.updateNumberOfInsertions(newSetOfRules, contInsertions);
							} else {
								newSentence += GrammarParser.getVariable(Character.toString(sentence.charAt(i)), newSetOfRules);
							}
						} else {
							newSentence += Character.toString(sentence.charAt(i));
						}
					}
					academic.insertIrregularRule(new Rule(element));
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
									newProduction += CHOMSKY_PREFIX + contInsertions;
								}
								String insertOnRightSide = GrammarParser.splitSentence(cont, newSentence, contInsertions);
								newSentence = GrammarParser.splitSentence(newSentence);
								Rule r = new Rule();
								r.setLeftSide(CHOMSKY_PREFIX + contInsertions);
								r.setRightSide(insertOnRightSide);
								newSetOfRules.add(r);
								academic.insertNewRule(r);
								newSetOfVariables.add(CHOMSKY_PREFIX + contInsertions);
								contInsertions = GrammarParser.updateNumberOfInsertions(newSetOfRules, contInsertions);
								changeCounter = 0;
								cont = -1;
							} else {
								if (GrammarParser.canInsert(newProduction)) {
									newProduction += GrammarParser.partialSentence(newSentence);
									newSentence = GrammarParser.splitSentence(newSentence);
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
					academic.insertNewRule(r);
				} else if (GrammarParser.sentenceSize(newSentence) == 2) {
					Rule r = new Rule();
					r.setLeftSide(element.getLeftSide());
					r.setRightSide(newSentence);
					newSetOfRules.add(r);
					academic.insertNewRule(r);
				}
				//academic.insertNewRule(new Rule(element.getLeftSide(), newProduction));
			}
			// update the variables
			for (String variable : newSetOfVariables) {
				gc.insertVariable(variable);
			}

			// update the rules
			newSetOfRules.addAll(auxSetOfRules);
			newSetOfRules = gc.removeEqualProductions(newSetOfRules);
			gc.setRules(newSetOfRules);
		}
		return gc;
	}

	public Set<Rule> removeEqualProductions (Set<Rule> setOfRules) {
		List<String> ruleEquals = new ArrayList<>();
		for(Rule rule : setOfRules) {
			if(rule.getLeftSide().charAt(0)=='T' &&
					rule.getLeftSide().length() > 1) {
				String numberRule1 = rule.getLeftSide().substring(1);
				for(Rule ruleAux : setOfRules) {
					if(ruleAux.getLeftSide().charAt(0)=='T' &&
							ruleAux.getLeftSide().length() > 1 &&
							rule.getRightSide().equals(ruleAux.getRightSide()) &&
							!rule.equals(ruleAux)) {
						boolean equal = true;
						String numberRule2 = ruleAux.getLeftSide().substring(1);
						if(Integer.parseInt(numberRule1) > Integer.parseInt(numberRule2)) {
							String aux = numberRule1;
							numberRule1 = numberRule2;
							numberRule2 = aux;
						}
						for(int i = 0; i < ruleEquals.size(); i+=2) {
							if(ruleEquals.get(i+1).equals(numberRule2) &&
									ruleEquals.get(i).equals(numberRule1)) {
								equal = false;
								break;
							}
						}
						if(equal) {
							ruleEquals.add(numberRule1);
							ruleEquals.add(numberRule2);
						}
					}
				}
			}
		}
		for(Iterator<Rule> it = setOfRules.iterator(); it.hasNext(); ) {
			Rule rule = it.next();
			for(int i = 0; i < ruleEquals.size(); i+=2) {
				if(rule.getRightSide().contains("T"+ruleEquals.get(i+1))) {
					rule.setRightSide(rule.getRightSide().replace("T"+ruleEquals.get(i+1), "T"+ruleEquals.get(i)));
				}
				if(rule.getLeftSide().equals("T"+ruleEquals.get(i+1))) {
					variables.remove("T"+ruleEquals.get(i+1));
					it.remove();
					break;
				}
			}
		}
		return setOfRules;
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
				academicSupport.insertIrregularRule(element);
			}
		}
		if (!haveRecursion.isEmpty()) {
			academicSupport.setSituation(true);
		}

		// estabelece relacao entre variável que gera recursão e a variável que
		// irá resolver essa recursão
		Map<String, String> variablesMapped = new HashMap<String, String>();
		int counter = 1;
		while (gc.getVariables().contains(RECURSIVE_REMOVAL_PREFIX + counter)) {
			counter++;
		}

		for (String element : haveRecursion) {
			if (!variablesMapped.containsKey(element)) {
				variablesMapped.put(element, RECURSIVE_REMOVAL_PREFIX + counter);
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
					academicSupport.insertNewRule(firstProduction);
					academicSupport.insertNewRule(secondProduction);
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
					academicSupport.insertNewRule(secondProduction);
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
	public Grammar removingLeftRecursion(final Grammar g, final AcademicSupport academicSupport, final Map<String, String> sortedVariables) {
		Grammar gc = (Grammar) g.clone();
		Set<String> olderVariables = gc.getVariables();
		Set<Rule> irregularRules = new HashSet<>();
		Set<Rule> newRules = new HashSet<>();

		if (GrammarParser.checksGrammar(gc, academicSupport)) {
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

			sortedVariables.putAll(variablesInOrder);
			if (GrammarParser.existsRecursion(gc, variablesInOrder, olderVariables) || GrammarParser.existsDirectRecursion(gc)) {
				academicSupport.setSituation(true);
			}

			while (GrammarParser.existsRecursion(gc, variablesInOrder, olderVariables)) {
				// verifica se gramática possui recursão direta
				if (GrammarParser.existsDirectRecursion(gc)) {
					gc = removingTheImmediateLeftRecursion(gc, academicSupport);
				}

				// gramática possui recursão indireta
				Set<Rule> newSetOfRules = new HashSet<Rule>();
				for (String variable : gc.getVariables()) {
					if (olderVariables.contains(variable)) {
						for (Rule element : gc.getRules()) {
							if (variable.equals(element.getLeftSide())) {
								int u = Integer.parseInt(variablesInOrder.get(variable));
								String rightSide = GrammarParser.getsFirstCharacter(element.getRightSide());
								if (Character.isLowerCase(rightSide.charAt(0)) || LAMBDA.equals(rightSide)) {
									Rule r = new Rule(variable, element.getRightSide());
									newSetOfRules.add(r);
								} else {
									int v = Integer.parseInt(variablesInOrder.get(rightSide));
									if (u > v) {
										if (academicSupport.getGrammar().getInitialSymbol().isEmpty()) {
											irregularRules.add(new Rule(variable, element.getRightSide()));
										}
										for (Rule secondElement : gc.getRules()) {
											if (rightSide.equals(secondElement.getLeftSide())) {
												String newProduction = secondElement.getRightSide();
												newProduction += element.getRightSide().substring(GrammarParser.indexOfFirstChar(element.getRightSide()));
												Rule r = new Rule(variable, newProduction);
												newSetOfRules.add(r);
												newRules.add(new Rule(variable, newProduction));
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

				if (academicSupport.getGrammar().getInitialSymbol().isEmpty()) {
					academicSupport.setGrammar(gc);
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
		academicSupport.setInsertedRules(newRules);
		academicSupport.setIrregularRules(irregularRules);
		return gc;
	}

	
	/**
	 * 
	 * @param : gramática livre de contexto
	 * @return : gramática livre de contexto na Forma Normal de Greibach
	 */
	public Grammar FNG(final Grammar g, final AcademicSupport academic) {
		Grammar gc = (Grammar) g.clone();

		Set<Rule> newSetOfRules = new HashSet<Rule>();
		if (GrammarParser.grammarWithCycles(gc)) {
			while (!GrammarParser.isFNG(gc.getRules())) {
				academic.setSituation(true);
				for (String variable : gc.getVariables()) {
					for (Rule element : gc.getRules()) {
						if (variable.equals(element.getLeftSide())) {
							if (!Character.isLowerCase(element.getRightSide().charAt(0)) && !element.getRightSide().equals(LAMBDA)) {
								academic.insertIrregularRule(element);
								String firstCharacter = GrammarParser.getsFirstCharacter(element.getRightSide());
								for (Rule secondElement : gc.getRules()) {
									if (secondElement.getLeftSide().equals(firstCharacter)) {
										String newRightSide = secondElement.getRightSide();
										if (GrammarParser.counterOfRightSide(element.getRightSide()) != 1) {
											newRightSide += element.getRightSide().substring(GrammarParser.indexOfFirstChar(element.getRightSide()));
										}
										Rule r = new Rule(element.getLeftSide(), newRightSide);
										newSetOfRules.add(r);
										academic.insertNewRule(r);
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
				newSetOfRules.clear();

			}
		}
		return gc;
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
		X = GrammarParser.fillFirstLine(X, g, word);

		if (word.length() >= 2) {
			// preenchendo a segunda linha da tabela
			X = GrammarParser.fillSecondLine(X, g, word);

			if (word.length() >= 3) {
				// preenchendo o restante da tabela
				int line = word.length() - 1;
				int column = 0;
				int count = 3;

				X = GrammarParser.fillOthersLines(X, g, count, line, column, word);
			}
		}
		return X;
	}

	public Map<String, Set<String>> getRulesMapUToV() {
		Map<String, Set<String>> rulesMapUToV = new TreeMap<>();
		Set<String> rightSide;
		for(Rule rule : rules) {
			if(rulesMapUToV.containsKey(rule.getLeftSide())) {
				rightSide = rulesMapUToV.get(rule.getLeftSide());
				rightSide.add(rule.getRightSide());
				rulesMapUToV.put(rule.getLeftSide(), rightSide);
			} else {
				rightSide = new TreeSet<String>();
				rightSide.add(rule.getRightSide());
				rulesMapUToV.put(rule.getLeftSide(), rightSide);
			}
		}
		return rulesMapUToV;
	}

	public String toStringRulesMapUToV() {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, Set<String>> entry : getRulesMapUToV().entrySet()) {
			sb.append(entry.getKey() + " -> ");
			for(String rightSide : entry.getValue()) {
				sb.append(rightSide + " | ");
			}
			sb.delete(sb.length()-3, sb.length());
			sb.append("\n");
		}
		return sb.toString();
	}

}
