package com.ufla.lfapp.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;


public class GrammarParser {

    private GrammarParser() {}

    /**
     * Extrai variáveis da gramática
     * @param txt : gramática informada
     * @return : variáveis extraídas
     */
    public static Set<String> extractVariablesFromFull(String txt) {
        Set<String> Variables = new HashSet<>();
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

    /**
     * Extrai terminais da gramática
     * @param txt : gramática informada
     * @return : terminais extraídos
     */
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

    /**
     * Extrai regras da gramática
     * @param txt : gramática informada
     * @return : regras extraídas
     */
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

    /**
     * Extrai símbolo inicial da gramática
     * @param txt : gramática informada
     * @return : símbolo inicial extraído
     */
    public static String extractInitialSymbolFromFull(String txt) {
        String initialSymbol = new String();
        for (int i = 0; txt.charAt(i) != ' ' && txt.charAt(i) != '-'; i++) {
            initialSymbol += Character.toString(txt.charAt(i));
        }
        initialSymbol = initialSymbol.trim();
        return initialSymbol;
    }

    /**
     * Compara símbolo extraído com o símbolo informado
     * @param initialSymbol : símbolo extraído da gramática
     * @param rules : regras extraídas da gramática
     * @return
     */
    public static boolean compareInitialSymbolWithParameter(String initialSymbol, String rules) {
        for (int i = 0; i < rules.length(); i++) {
            if (initialSymbol.equals(rules.charAt(i)))
                return true;
        }
        return false;
    }

    /**
     * Compara variáveis extraídas da gramática com variáveis informadas
     * @param variables : variáveis extraídas da gramática
     * @param rules : regras extraídas da gramática
     * @return
     */
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

    /**
     * Formata os terminais informados.
     * @param : terminais informados
     * @return : terminais formatados
     */
    public static Set<String> formatTerminals(String terminals) {
        Set<String> terminalsFormated = new HashSet<>();
        for (int i = 0; i < terminals.length(); i++) {
            if (Character.isLowerCase(terminals.charAt(i)))
                terminalsFormated.add(Character.toString(terminals.charAt(i)));
        }
        return terminalsFormated;
    }

    /**
     * Formata variáveis informadas.
     * @param variables : variáveis informadas.
     * @return : variáveis formatadas.
     */
    public static Set<String> formatVariables(String variables) {
        Set<String> variablesFormated = new HashSet<>();

        for (int i = 0; i < variables.length(); i++) {
            if (Character.isUpperCase(variables.charAt(i)))
                variablesFormated.add(Character.toString(variables.charAt(i)));
        }
        return variablesFormated;
    }

    /**
     * Formata símbolo inicial
     * @param initialSymbol
     * @return
     */
    public static String formatInitialSymbol(String initialSymbol) {
        return Character.toString(Character.toUpperCase(initialSymbol.charAt(0)));
    }



    public static boolean verifyInputGrammar(final String txtGrammar) {
        boolean test = true;

        StringTokenizer tokenizer1 = new StringTokenizer(txtGrammar, "\n");
        while (tokenizer1.hasMoreTokens() & test) {
            String rule = tokenizer1.nextToken();
            int cont = 0;
            StringTokenizer tokenizer2 = new StringTokenizer(rule, "->");
            while (tokenizer2.hasMoreTokens() & test) {
                tokenizer2.nextToken();
                cont++;
            }
            if (cont != 2) {
                test = false;
            }
        }

        tokenizer1 = new StringTokenizer(txtGrammar, "\n");
        while (tokenizer1.hasMoreTokens() & test) {
            String element = tokenizer1.nextToken().trim();
            StringTokenizer tokenizer2 = new StringTokenizer(element, "->");
            int aux = 0;
            while (tokenizer2.hasMoreTokens()) {
                String sentence = tokenizer2.nextToken().trim();
                StringTokenizer tokenizer3 = new StringTokenizer(sentence, "|");
                while (tokenizer3.hasMoreTokens() && aux != 0) {
                    String token = tokenizer3.nextToken().trim();
                    if (Character.isDigit(token.charAt(0))) {
                        test = false;
                    }
                    for (int i = 0; i < token.length() & test; i++) {
                        if (!Character.isLetter(token.charAt(i)) && !Character.isDigit(token.charAt(i)) && token.charAt(i) != 'λ') {
                            test = false;
                        }
                    }
                }
                if (aux == 0) {
                    String token = tokenizer3.nextToken().trim();
                    if (!Character.isLetter(token.charAt(0))) {
                        test = false;
                    }
                }
                aux++;
            }
        }

        return test;
    }


    public static boolean inputValidate (final String txtGrammar, final StringBuilder reason) {
        boolean test = true;
        //Set<String> setOfVariables = extractVariablesFromFull(txtGrammar);
        Set<String> setOfVariables = new HashSet<>();
        String m = setOfVariables.toString();

        StringTokenizer token1 = new StringTokenizer(txtGrammar, "\n");
        while (token1.hasMoreTokens()) {
            String rule = token1.nextToken().trim();
            StringTokenizer sides = new StringTokenizer(rule, "->");
            int cont = 0;
            while (sides.hasMoreTokens() && cont == 0) {
                String leftSide = sides.nextToken().trim();
                setOfVariables.add(leftSide);
                cont++;
            }
        }

        StringBuilder variable = new StringBuilder();
        for (int i = 0; i < txtGrammar.length() && test; i++) {
            if (Character.isLetter(txtGrammar.charAt(i)) && Character.isUpperCase(txtGrammar.charAt(i))) {
                variable.append(txtGrammar.charAt(i));
                int j = i + 1;
                while (j < txtGrammar.length() && Character.isDigit(txtGrammar.charAt(j))) {
                    variable.append(txtGrammar.charAt(j));
                }
                if (!setOfVariables.contains(variable.toString())) {
                    test= false;
                    reason.append("Não foram atribuídas produções à variável '" + variable + "'.");
                }
            }
            variable.delete(0, variable.length());
        }
        return test;
    }


    /**
     * Verifica se a gramática informada é regular
     * @param g : Gramática inserida
     * @return
     */
    public static boolean regularGrammar(final Grammar g, final StringBuilder academic) {
        int counter = 0;
        boolean regular = true;
        Iterator<Rule> itRules = g.getRules().iterator();
        while (itRules.hasNext() && regular) {
            Rule r = itRules.next();
            for (int i = 0; i < r.getRightSide().length(); i++) {
                if (g.getVariables().contains(Character.toString(r.getRightSide().charAt(i)))) {
                    counter++;
                }
            }

            if (counter == 1) {
                if (!g.getVariables().contains(Character.toString(r.getRightSide().charAt(0))) && !g.getVariables().contains(Character.toString(r.getRightSide().charAt(r.getRightSide().length() - 1)))) {
                    regular = false;
                    academic.append("- Na gramática informada, a " + r + " não pertence ao conjunto das gramáticas regulares.\n");
                }
            } else if (counter > 1) {
                regular = false;
                academic.append("- Na gramática informada, a regra " + r + " não pertence ao conjunto das gramáticas regulares.\n");
            }
            counter = 0;
        }
        return regular;
    }




    /**
     * Verifica se a gramática informada é livre de contexto
     * @param g : Gramática inserida
     * @return
     */
    public static boolean contextFreeGrammar(final Grammar g, StringBuilder academic) {
        boolean contextFree = true;
        Iterator<Rule> itRules = g.getRules().iterator();
        while (itRules.hasNext() && contextFree) {
            Rule r = itRules.next();
            if (!g.getVariables().contains(r.getLeftSide())) {
                contextFree = false;
                academic.append("- Na gramática informada, a regra " + r + "não pertence ao conjunto das gramáticas livres de contexto.\n");
            }
        }
        return contextFree;
    }

    /**
     * Verifica se a gramática informada é sensível ao contexto
     * @param g : gramática inserida
     * @return
     */
    private static boolean contextSensibleGrammar(final Grammar g, StringBuilder academic) {
        boolean contextSensible = true;
        for (Rule element : g.getRules()) {
            if (!containsSentence(g, element.getLeftSide()) || !containsSentence(g, element.getRightSide())
                    || element.getRightSide().length() < element.getLeftSide().length()) {
                contextSensible = false;
                academic.append("- Na gramática informada, a regra " + element + "não pertence ao conjunto das gramáticas sensíveis contexto.\n");
            }
        }
        return contextSensible;
    }

    /**
     * Verifica se a gramática informada é irrestrita
     * @param g : gramática inserida
     * @return
     */
    private static boolean unrestrictedGrammar(final Grammar g, StringBuilder academic) {
        boolean unrestricted = true;
        for (Rule element : g.getRules()) {
            if ((!containsSentence(g, element.getLeftSide()) || element.getLeftSide().equals(Grammar.LAMBDA)) ||
                    (!containsSentence(g, element.getRightSide()) && !element.getRightSide().equals(Grammar.LAMBDA))) {
                unrestricted = false;
                academic.append("- Na gramática informada, a regra " + element + "não pertence ao conjunto das gramáticas irrestritas.\n");
            }
        }
        return unrestricted;
    }

    /**
     * Verifica se uma sentença está contida no conjunto de variáveis e no conjunto de terminais
     * @param g
     * @param sentence
     * @return
     */
    private static boolean containsSentence(final Grammar g, String sentence) {
        boolean contains = true;
        StringBuilder element = new StringBuilder();
        for (int i = 0; i < sentence.length(); i++) {
            if (!Character.isDigit(sentence.charAt(i))) {
                element.append(sentence.charAt(i));
                int j = i + 1;
                while (j < sentence.length() && Character.isDigit(sentence.charAt(j))) {
                    element.append(sentence.charAt(j));
                    j++;
                }
                if (!(g.getVariables().contains(element.toString()) || g.getTerminals().contains(element.toString()))) {
                    contains = false;
                }
                element.delete(0, element.length());
            }
        }
        return contains;
    }

    /**
     * Classifica a gramática
     * @return Classificação da gramática
     */
    public static String classifiesGrammar(final Grammar g, final StringBuilder academic) {
        String grammarType = new String();

        //verifica se a gramática é regular
        grammarType = (regularGrammar(g, academic)) ? ("Logo, a gramática inserida é uma Gramática Regular (GR).") : ("");

        if (grammarType.isEmpty()) {
            grammarType = (contextFreeGrammar(g, academic) ? ("Logo, a gramática inserida é uma Gramática Livre de Contexto (GLC).") : (""));
        }

        if (grammarType.isEmpty()) {
            grammarType = (contextSensibleGrammar(g, academic) ? ("Logo, a gramática inserida é uma Gramática Sensível ao Contexto (GSC).") : (""));
        }

        if (grammarType.isEmpty()) {
            grammarType = (unrestrictedGrammar(g, academic) ? ("Logo, a gramática inserida é uma Gramática Irrestrita GI.") : (""));
        }

        if (grammarType.isEmpty()) {
            grammarType = "A gramática informada é inexistente.";
        }
        return grammarType;
    }





    /**
     * Procura variáveis anuláveis nas regras
     * @param element
     * @param nullableVariables
     * @return
     */
    public static String searchVariablesOnRules(String element, Set<String> nullableVariables) {
        String containsVariables = new String();
        boolean found = true;
        String[] verifyRules = element.split(" | ");
        for (String aux : verifyRules) {
            for (int j = 0; j < aux.length() && found == true; j++) {
                if (Character.isLowerCase(aux.charAt(j)))
                    found = false;
            }
            for (int i = 0; i < aux.length() && found == true; i++) {
                if (nullableVariables.contains(Character.toString(aux.charAt(i)))) {
                    containsVariables += Character.toString(aux.charAt(i));
                }
            }
            found = true;
        }
        return containsVariables;
    }

    /* INICIA PROCESSO PARA DERIVAÇÃO DE GRAMÁTICAS */

    /**
     * Converte uma gramática para um automato com pilha
     * @param g
     * @return
     */
    private static PushdownAutomaton turnsGrammarToPushdownDescendingAutomaton(final Grammar g) {
        //remover recursão à esquerda da gramática

        PushdownAutomaton automaton = new PushdownAutomaton();

        //adiciona conjunto de estados
        Set<String> states = new HashSet<String>();
        states.add("q0");
        states.add("q1");
        states.add("q2");
        automaton.setStates(states);

        //adiciona estado inicial
        automaton.setInitialState("q0");

        //adiciona estado finail
        Set<String> finalState = new HashSet<String>();
        finalState.add("q2");
        automaton.setFinalStates(finalState);

        //adiciona o alfabeto da máquina
        automaton.setAlphabet(g.getTerminals());

        //adiciona o alfabeto da pilha
        Set<String> stackAlphabet = new HashSet<String>();
        stackAlphabet.addAll(g.getVariables());
        stackAlphabet.addAll(g.getTerminals());
        stackAlphabet.add(Grammar.LAMBDA);
        automaton.setStackAlphabet(stackAlphabet);

        //adiciona função de transição
        //adiciona transição inicial
        Set<TransitionFunctionPA> transitions = new HashSet<TransitionFunctionPA>();
        transitions.add(new TransitionFunctionPA("q0", g.getInitialSymbol(), "q1", Grammar.LAMBDA, Grammar.LAMBDA));

        //adiciona transições
        for (String variable : g.getVariables()) {
            for (Rule element : g.getRules()) {
                if (variable.equals(element.getLeftSide())) {
                    transitions.add(new TransitionFunctionPA("q1", Grammar.LAMBDA, "q1", element.getRightSide(), element.getLeftSide()));
                }
            }
        }

        //adiciona transições que esvaziam a pilha
        for (String terminal : g.getTerminals()) {
            transitions.add(new TransitionFunctionPA("q1", terminal, "q1", Grammar.LAMBDA, terminal));
        }

        //adiciona transição final
        transitions.add(new TransitionFunctionPA("q1", Grammar.LAMBDA, "q2", Grammar.LAMBDA, Grammar.LAMBDA));

        automaton.setTransictionFunction(transitions);
        return automaton;
    }

    private static Set<String> variablesStackingTerminals(final PushdownAutomaton automaton, final Grammar g) {
        Set<String> variables = new HashSet<String>();
        for (TransitionFunctionPA transition : automaton.getTransictionFunction()) {
            if (g.getTerminals().contains(transition.getStacking())) {
                variables.add(transition.getPops());
            }
        }
        return variables;
    }


    /*public static void moreLeftDerivation(Grammar g, final StringBuilder derivation, final String word, final StringBuilder academic) {
        Grammar gc = (Grammar) g.clone();

        StringBuilder wordCopy = new StringBuilder(word);

        Grammar gcAux = (Grammar) gc.clone();


        //coloca gramática na Forma Normal de Chomsky, caso ainda não esteja
        if (!GrammarParser.isFNC(gcAux)) {
            gcAux = gcAux.FNC(gcAux);
        }
        //primeiro, é necessário verificar se a palavra pertence à linguagem
        Set<String>[][] cykOut = Grammar.CYK(gcAux, word);
        String[][] result = GrammarParser.turnsTreesetOnArray(cykOut, word);
        if (result[0][0].contains(gcAux.getInitialSymbol())) {

            PushdownAutomaton automaton = turnsGrammarToPushdownDescendingAutomaton(gcAux);
            int currentIndex = 0;
            int counter = 0;
            int counterOfAttempts = 0;
            boolean flag = false;

            //declara estruturas auxiliares para o processo
            StringBuilder generatedSentence = new StringBuilder();
            ArrayList<TransitionFunctionPA> usedTransitions = new ArrayList<TransitionFunctionPA>();
            ArrayList<ArrayList<String>> stacks = new ArrayList<ArrayList<String>>();
            TransitionFunctionPA currentTransition = new TransitionFunctionPA();
            ArrayList<ArrayList<TransitionFunctionPA>> listOfAttempts = new ArrayList<ArrayList<TransitionFunctionPA>>();
            ArrayList<TransitionFunctionPA> attempt = new ArrayList<TransitionFunctionPA>();
            Set<String> variablesThatGeneratedTerminals = new HashSet<String>();
            variablesThatGeneratedTerminals = variablesStackingTerminals(automaton, gc);
            //TransitionFunctionPA auxTransition = new TransitionFunctionPA();

            //realiza passo 1 do algoritmo: empilha símbolo inicial
            automaton.push(gcAux.getInitialSymbol());
            //System.activity_out.println(automaton.getStack());
            attempt.add(new TransitionFunctionPA("q0", gc.getInitialSymbol(), "q1", Grammar.LAMBDA, Grammar.LAMBDA));

            loop:
            do {
//				System.activity_out.println("Stack: " + automaton.getStack());
//				System.activity_out.println("Top: " + automaton.getTopOfStack());
                if (gcAux.getVariables().contains(automaton.getTopOfStack())) {
                    //variável no topo da pilha
                    //pega o topo da pilha
                    String variableAtTop = automaton.getTopOfStack();

                    //seleciona uma transição válida
                    currentTransition = selectTransition(automaton, attempt, listOfAttempts, variableAtTop, counterOfAttempts);
                    //currentTransition = selectTransition(usedTransitions, automaton);

                    //salva estado atual da pilha
                    if (saveStack(variableAtTop, automaton) && !stacks.contains(automaton.getStack()) && compareWithLengthOfWord(word, generatedSentence, automaton)) {
                        stacks.add(new ArrayList<String>(automaton.getStack()));
                    }

                    //desempilha o topo atual
                    automaton.pop();
                    //System.activity_out.println(automaton.getStack());

                    //empilha
                    automaton.push(currentTransition.getStacking());
                    //System.activity_out.println(automaton.getStack());
                    attempt.add(new TransitionFunctionPA(currentTransition));
                    //attempt.add(new PushdownAutomaton(automaton));

                } else if (gcAux.getTerminals().contains(automaton.getTopOfStack()) || automaton.getTopOfStack().equals(Grammar.LAMBDA)) {
                    //terminal no topo da pilha
                    //verifica o próximo terminal da expressão está no topo da pilha
                    StringBuilder charAtVariable = new StringBuilder();
                    if (currentIndex > word.length()) {
                        usedTransitions.addAll(attempt);
                        counter--;
                        //automaton.setStack(new ArrayList<String>(stacks.get(getIndexOfStack(stacks, automaton, currentIndex, word, variablesThatGeneratedTerminals))));
                        automaton.setStack(new ArrayList<String>(stacks.get((currentIndex < word.length()) ? (currentIndex - 1) : (stacks.size() - 1))));
                        //counterOfStacks--;
                        currentIndex = updateCurrentIndex(automaton, stacks);
                        listOfAttempts.add(new ArrayList<TransitionFunctionPA>(attempt));
                        attempt = new ArrayList<TransitionFunctionPA>(getNewList(attempt, automaton));
                        counterOfAttempts++;
                        break loop;
                    } else {
                        charAtVariable.append(word.charAt(currentIndex));
                    }
                    //charAtVariable.append(word.charAt((currentIndex < word.length()) ? (currentIndex) : (word.length() - 1)));
                    //charAtVariable.append(word.charAt(generatedSentence.length()));
                    if (automaton.getTopOfStack().equals(charAtVariable.toString()) && currentIndex < word.length()) {
                        currentIndex++;
                        generatedSentence.append(automaton.getTopOfStack());
                        automaton.pop();
                        usedTransitions = new ArrayList<TransitionFunctionPA>();
                        counter++;
                    } else {
                        usedTransitions.addAll(attempt);
                        counter--;
                        //automaton.setStack(new ArrayList<String>(stacks.get(getIndexOfStack(stacks, automaton, currentIndex, word, variablesThatGeneratedTerminals))));
                        automaton.setStack(new ArrayList<String>(stacks.get((currentIndex < word.length()) ? (currentIndex - 1) : (stacks.size() - 1))));
                        //counterOfStacks--;
                        currentIndex = updateCurrentIndex(automaton, stacks);
                        listOfAttempts.add(new ArrayList<TransitionFunctionPA>(attempt));
                        attempt = new ArrayList<TransitionFunctionPA>(getNewList(attempt, automaton));
                        counterOfAttempts++;
                    }
                } else {
                    counter--;
                    currentIndex--;
                    automaton.setStack(stacks.get(counter));
                }
            } while (!automaton.getStack().isEmpty());
        }
			/*ArrayList<TransitionFunctionPA> resultOfDerivation = new ArrayList<TransitionFunctionPA>(attempt);
			int i = 0;
			while (i < 1000 && !flag) {
				automaton = turnsGrammarToPushdownDescendingAutomaton(g);
				currentIndex = 0;
				counter = 0;
				counterOfAttempts = 0;
				counterOfStacks = 0;

				//declara estruturas auxiliares para o processo
				generatedSentence = new StringBuilder();
				usedTransitions = new ArrayList<TransitionFunctionPA>();
				stacks = new ArrayList<ArrayList<String>>();
				currentTransition = new TransitionFunctionPA();
				listOfAttempts = new ArrayList<ArrayList<TransitionFunctionPA>>();
				attempt = new ArrayList<TransitionFunctionPA>();
				variablesThatGeneratedTerminals = new HashSet<String>();
				variablesThatGeneratedTerminals = variablesStackingTerminals(automaton, gc);
				//TransitionFunctionPA auxTransition = new TransitionFunctionPA();

				//realiza passo 1 do algoritmo: empilha símbolo inicial
				automaton.push(g.getInitialSymbol());
				System.activity_out.println(automaton.getStack());
				attempt.add(new TransitionFunctionPA("q0", gc.getInitialSymbol(), "q1", Grammar.LAMBDA, Grammar.LAMBDA));

				do {
					System.activity_out.println("Stack: " + automaton.getStack());
					System.activity_out.println("Top: " + automaton.getTopOfStack());
					if (g.getVariables().contains(automaton.getTopOfStack())) {
						//variável no topo da pilha
						//pega o topo da pilha
						String variableAtTop = automaton.getTopOfStack();

						//seleciona uma transição válida
						currentTransition = selectTransition(automaton, attempt, listOfAttempts, variableAtTop, counterOfAttempts);
						//currentTransition = selectTransition(usedTransitions, automaton);

						//salva estado atual da pilha
						if (saveStack(variableAtTop, automaton) && !stacks.contains(automaton.getStack()) && compareWithLengthOfWord(word, generatedSentence, automaton)) {
							stacks.add(new ArrayList<String>(automaton.getStack()));
							counterOfStacks = stacks.size() - 1;
						}

						//desempilha o topo atual
						automaton.pop();
						System.activity_out.println(automaton.getStack());

						//empilha
						automaton.push(currentTransition.getStacking());
						System.activity_out.println(automaton.getStack());
						attempt.add(new TransitionFunctionPA(currentTransition));
						//attempt.add(new PushdownAutomaton(automaton));

					} else if (g.getTerminals().contains(automaton.getTopOfStack())) {
						//terminal no topo da pilha
						//verifica o próximo terminal da expressão está no topo da pilha
						StringBuilder charAtVariable = new StringBuilder();
						charAtVariable.append(word.charAt((currentIndex < word.length()) ? (currentIndex) : (word.length() - 1)));
						//charAtVariable.append(word.charAt(generatedSentence.length()));
						if (automaton.getTopOfStack().equals(charAtVariable.toString()) && currentIndex < word.length()) {
							currentIndex++;
							generatedSentence.append(automaton.getTopOfStack());
							automaton.pop();
							usedTransitions = new ArrayList<TransitionFunctionPA>();
							counter++;
						} else {
							usedTransitions.addAll(attempt);
							counter--;
							//automaton.setStack(new ArrayList<String>(stacks.get(getIndexOfStack(stacks, automaton, currentIndex, word, variablesThatGeneratedTerminals))));
							automaton.setStack(new ArrayList<String>(stacks.get((currentIndex < word.length()) ? (currentIndex - 1) : (stacks.size() - 1))));
							//counterOfStacks--;
							currentIndex = updateCurrentIndex(automaton, stacks);
							listOfAttempts.add(new ArrayList<TransitionFunctionPA>(attempt));
							attempt = new ArrayList<TransitionFunctionPA>(getNewList(attempt, automaton));
							counterOfAttempts++;
						}
					} else {
						counter--;
						currentIndex--;
						automaton.setStack(stacks.get(counter));
					}
				} while (!automaton.getStack().isEmpty());
				if (resultOfDerivation.equals(attempt)) {
					flag = true;
				}
				i++;
			}
		} else {
			academic.append("A palavra inserida não pertence à gramática, logo, não existe possíveis derivações.");
		}*/
    //}

    /**
     * verifica quais variáveis estão presentes no conjunto Prev
     * @param variables
     * @param prev
     * @return
     */
    public static boolean searchVariables(String variables, Set<String> prev) {
        boolean found = false;
        for (int i = 0; i < variables.length() && found == false; i++) {
            if (prev.contains(Character.toString(variables.charAt(i))))
                found = true;
        }
        return found;
    }

    /**
     * Substitui LAMBDA por vazio
     * @param element
     * @param gc
     * @return
     */
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

    private static Set<Set<Integer>> combinationsP(List<Integer> groupSize) {
        Set<Set<Integer>> combinations = new HashSet<>();
        for(int i = 1; i <= groupSize.size(); i++) {
            combinations.addAll(combinationsIntern(groupSize, i));
        }
        return combinations;
    }

    private static Set<Set<Integer>> combinationsIntern(List<Integer> groupSize, int k) {

        Set<Set<Integer>> allCombos = new HashSet<Set<Integer>> ();
        // base cases for recursion
        if (k == 0) {
            // There is only one combination of size 0, the empty team.
            allCombos.add(new HashSet<Integer>());
            return allCombos;
        }
        if (k > groupSize.size()) {
            // There can be no teams with size larger than the group size,
            // so return allCombos without putting any teams in it.
            return allCombos;
        }

        // Create a copy of the group with one item removed.
        List<Integer> groupWithoutX = new ArrayList<Integer> (groupSize);
        Integer x = groupWithoutX.remove(groupWithoutX.size()-1);

        Set<Set<Integer>> combosWithoutX = combinationsIntern(groupWithoutX, k);
        Set<Set<Integer>> combosWithX = combinationsIntern(groupWithoutX, k-1);
        for (Set<Integer> combo : combosWithX) {
            combo.add(x);
        }
        allCombos.addAll(combosWithoutX);
        allCombos.addAll(combosWithX);
        return allCombos;
    }

    public static String combination(String rightSide, Set<String> nullableVariables) {
        List<Integer> indiceCombinations = new ArrayList<>();
        for(int j = 0; j < rightSide.length(); j++) {
            if(nullableVariables.contains(Character.toString(rightSide.charAt(j)))) {
                indiceCombinations.add(j);
            }
        }
        Set<Set<Integer>> combinations = combinationsP(indiceCombinations);
        StringBuilder newProduction = new StringBuilder(rightSide + " | ");
        char[] productionArray = rightSide.toCharArray();
        boolean  emptyProduction = true;
        for(int i = 0; i < productionArray.length; i++) {
            if(!indiceCombinations.contains(i)) {
                newProduction.append(productionArray[i]);
               emptyProduction = false;
            }
        }
        if(!emptyProduction) {
            newProduction.append(" | ");
        }
        for(Set<Integer> combination : combinations) {
            for(int i = 0; i < productionArray.length; i++) {
                if(!indiceCombinations.contains(i) || combination.contains(i)) {
                    newProduction.append(productionArray[i]);
                }
            }
            newProduction.append(" | ");
        }
        return newProduction.toString();
    }




    /**
     *
     * @param set
     * @return conjunto de variáveis anuláveis
     */
    public static String printSet(Set<String> set) {
        String variablesInSet = new String();
        for (String variable : set) {
            variablesInSet += variable + ", ";
        }
        return variablesInSet;
    }

    public static boolean newProductions(String production, String leftSide, Grammar g) {
        for (Rule element : g.getRules()) {
            if (element.getLeftSide().equals(leftSide)) {
                if (element.getRightSide().equals(production)) {
                    return false;
                }
            }
        }
        return true;
    }




    /**
     * Verifica a existência de variáveis no conjunto Prev

     * @param prev
     * @param element
     * @return
     */
    public static boolean prevContainsVariable(Set<String> prev, String element) {
        for (int i = 0; i < element.length(); i++) {
            if (!Character.isUpperCase(element.charAt(i)) || !prev.contains(Character.toString(element.charAt(i)))) {
                return false;
            }
        }
        return true;
    }


    /**
     * Retorna os elementos que estão no conjunto Chain mas não estão no conjunto Prev
     * @param chain
     * @param prev
     * @return
     */
    public static Set<String> chainMinusPrev(Set<String> chain, Set<String> prev) {
        Set<String> aux = new HashSet<String>();
        for (String element : chain) {
            if (!prev.contains(element)) {
                aux.add(element);
            }
        }
        return aux;
    }

    /**
     * Atualiza as regras da gramática após remover símbolos inúteis
     * @param prev
     * @param g
     * @return
     */
    public static Set<Rule> updateRules(Set<String> prev, Grammar g, final AcademicSupport academic) {
        Set<Rule> newRules = new HashSet<>();
        for (Rule element : g.getRules()) {
            if (prev.contains(element.getLeftSide())) {
                String newRule = new String();
                boolean insertOnNewRule = true;
                for (int j = 0; j < element.getRightSide().length() && insertOnNewRule != false; j++) {
                    if (Character.isUpperCase(element.getRightSide().charAt(j))) {
                        if (prev.contains(Character.toString(element.getRightSide().charAt(j))))
                            insertOnNewRule = true;
                        else
                            insertOnNewRule = false;
                    } else if (Character.isLowerCase(element.getRightSide().charAt(j))) {
                        insertOnNewRule = true;
                    } else if (element.getRightSide().charAt(j) == '.') {
                        insertOnNewRule = true;
                    } else {
                        insertOnNewRule = false;
                    }
                }
                if (insertOnNewRule) {
                    newRule += element.getRightSide();
                } else {
                    academic.insertIrregularRule(new Rule(element));
                }
                if (newRule.length() != 0) {
                    Rule r = new Rule();
                    r.setLeftSide(element.getLeftSide());
                    r.setRightSide(newRule);
                    newRules.add(r);
                }
            } else {
                academic.insertIrregularRule(new Rule(element));
            }
        }
        return newRules;
    }

    /**
     * Atualiza terminais da gramática após remover símbolos inúteis.
     * @param g
     * @return
     */
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


    /**
     * Retorna os elementos que pertencem ao conjunto Term e não pertencem ao conjunto noTerm
     * @param term
     * @param noTerm
     * @return
     */
    public static Set<String> termMinusNoTerm(Set<String> term, Set<String> noTerm) {
        for (String element : noTerm) {
            if (term.contains(element)) {
                term.remove(element);
            }
        }
        return term;
    }

    /**
     * Retorna os elementos que pertentem ao conjunto Reach mas não pertencem ao conjunto Prev
     * @param reach
     * @param prev
     * @return
     */
    public static Set<String> reachMinusPrev(Set<String> reach, Set<String> prev) {
        Set<String> aux = new HashSet<>();
        for (String element : reach) {
            if (!prev.contains(element)) {
                aux.add(element);
            }
        }
        return aux;
    }

    /**
     *
     * @param reach
     * @param rightSide
     * @return
     */
    public static Set<String> variablesInW(Set<String> reach, String rightSide) {
        for (int i = 0; i < rightSide.length(); i++) {
            if (Character.isUpperCase(rightSide.charAt(i)))
                reach.add(Character.toString(rightSide.charAt(i)));
        }
        return reach;
    }


    /**
     * Verifica a existência de determinada produção.
     * @param leftSide
     * @param symbol
     * @param newSetOfRules
     * @return
     */
    public static boolean existsProduction(String leftSide, String symbol, Set<Rule> newSetOfRules) {
        for (Rule element : newSetOfRules) {
            if (element.getRightSide().equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna determinada variável.
     * @param symbol
     * @param newSetOfRules
     * @return
     */
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

    /**
     * Verifica o tamanho de uma dada sentença.
     * @param sentence
     * @return
     */
    static int sentenceSize(String sentence) {
        int count = 0;
        for (int i = 0; i < sentence.length(); i++) {
            if (Character.isLetter(sentence.charAt(i)))
                count++;
        }
        return count;
    }

    /**
     * Atualiza o número de inserções realizadas.
     * @param newSetOfRules
     * @param contInsertions
     * @return
     */
    static int updateNumberOfInsertions(Set<Rule> newSetOfRules,
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

    /**
     * Verifica se é possível realizar uma nova inserção.
     * @param sentence
     * @return
     */
    static boolean canInsert(String sentence) {
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

    public static boolean isFNC(final Grammar gc) {
        for(Rule rule : gc.getRules()) {
            if(!rule.isFNC(gc.getInitialSymbol())) {
                return false;
            }
        }
        return true;
    }
    /**
     *
     * @param gc : GLC
     * @return : verdadeiro se estiver em FNC e falso caso contrário.
     */
    public static boolean isFNC2(final Grammar gc) {
        boolean test = true;
        Iterator<Rule> it = gc.getRules().iterator();
        while (it.hasNext() && test) {
            Rule element = (Rule) it.next();
            if (!element.getRightSide().equals(".") &&
                    !Character.isLowerCase(element.getRightSide().charAt(0)) &&
                    counterOfRightSide(element.getRightSide()) != 2) {
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

    /**
     * Realiza split de uma determinada sentença.
     * @param newSentence
     * @return
     */
    static String splitSentence(String newSentence) {
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

    /**
     * Realiza split de uma determinada sentença.
     * @param newSentence
     * @return
     */
    static String splitSentence(int cont, String newSentence, int contInsertions) {
        String aux = new String();
        if (newProductionSize(newSentence) ==  2) {
            aux =  newSentence;
        } else {
            aux = Character.toString(newSentence.charAt(0));
            if (newSentence.charAt(0) == 'T') {
                for (int i = 1; !Character.isLetter(newSentence.charAt(i)); i++) {
                    aux += Character.toString(newSentence.charAt(i));
                }
            }
            aux += "T" + (contInsertions + 1);
        }
        return aux;
    }

    /**
     *
     * @param sentence
     * @return
     */
    static String partialSentence(String sentence) {
        String partial = new String();
        if (sentence.charAt(0) == 'T') {
            partial += "T";
            for (int i =1; !Character.isLetter(sentence.charAt(i)); i++)
                partial += Character.toString(sentence.charAt(i));
        } else {
            partial = Character.toString(sentence.charAt(0));
        }
        return partial;
    }

    /**
     * Verifica o tamanho de uma nova produção.
     * @param newProduction
     * @return
     */
    private static int newProductionSize(String newProduction) {
        int count = 0;
        for (int i = 0; i < newProduction.length(); i++) {
            if (Character.isLetter(newProduction.charAt(i)))
                count++;
        }
        return count;
    }

    /**
     * Verifica a existência de regras de cadeia.
     * @param element
     * @return
     */
    public static boolean verifyChains(Rule element) {
        boolean chain = true;
        for (int i = 0; i < element.getRightSide().length() && chain == true; i++) {
            if (!element.getLeftSide().equals(Character.toString(element.getRightSide().charAt(i)))) {
                chain = false;
            }
        }
        return chain;
    }

    /**
     * verifica se gramática é não contrátil ou essencialmente não contrátil e se possui recursão no símbolo inicial
     * @param g
     * @return
     */
    static boolean checksGrammar(final Grammar g, AcademicSupport academicSupport) {
        boolean grammarTest = true;
        for (Rule element : g.getRules()) {
            //verifica se é essencialmente não contrátil
            if (element.getRightSide().equals(Grammar.LAMBDA) && !g.getInitialSymbol().equals(element.getLeftSide())) {
                grammarTest = false;
                academicSupport.setSolutionDescription("A gramática inserida possui produções vazias.");
            }
        }
        //verifica se possui recursão no símbolo inicial
        for (Rule element : g.getRules()) {
            if (element.getLeftSide().equals(g.getInitialSymbol()) && element.getRightSide().contains(g.getInitialSymbol())) {
                grammarTest = false;
                academicSupport.setSolutionDescription("A gramática inserida possui recursão no símbolo inicial.");
            }
        }
        //verifica se as produções possuem ciclos
        for (Rule element : g.getRules()) {
            if (verifyChains(element)) {
                grammarTest = false;
                academicSupport.setSolutionDescription("A gramática inserida possui ciclos.\nA regra " + element + " é um ciclo.");
            }
        }
        return grammarTest;
    }


    /**
     * Retorna o índice do primeiro elemento de uma produção
     */
    static int indexOfFirstChar(String rightSide) {
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
    static int counterOfRightSide(String rightSide) {
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
     * @param
     */
    static boolean existsDirectRecursion(Grammar g) {
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
    static boolean existsRecursion(final Grammar g, Map<String, String> variablesInOrder, Set<String> olderVariables) {
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
    static String getsFirstCharacter(String rightSide) {
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


    /**
     * Verifica se a gramática possui ciclos.
     * @param g
     * @return
     */
    static boolean grammarWithCycles(final Grammar g) {
        boolean cycle = true;
        for (Rule element : g.getRules()) {
            if (verifyChains(element)) {
                cycle = false;
            }
        }
        return cycle;
    }

    /**
     * Verifica se a gramática dada está na forma normal de Greibach.
     * @param rules
     * @return
     */
    static boolean isFNG(Set<Rule> rules) {
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
        AcademicSupport academic = new AcademicSupport();

        if (!isFNG(gc.getRules())) {
            gc = g.FNG(gc, academic);
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




    public static Set<String>[][] fillOthersLines(Set<String>[][] x, Grammar g, int count, int line, int column, String word) {

        int counterLine = 1;
        int counterColumn = 1;
        int targetLine = word.length() - 3;
        int targetColumn = 0;
        int counterLineOfSecondElement = line - 1;
        int delimiter = word.length() - 2;
        int auxFlag = 1;

        int lineFirstElement = line;
        int lineSecondElement = line - 1;
        int columnFirstElement = column;
        int columnSecondElement = column + 1;


        while (count != word.length() + 1) {


            while (counterColumn + auxFlag != word.length()) {
                while (lineFirstElement >= delimiter ) {
                    String firstCell = returnsAlphabeticSymbols(x[lineFirstElement][columnFirstElement]);
                    String secondCell = returnsAlphabeticSymbols(x[lineSecondElement][columnSecondElement]);
                    for (int counterOfFirstCell = 0; counterOfFirstCell < firstCell.length(); ) {
                        String sentence = new String();
                        //sentence += Character.toString(firstCell.charAt(counterOfFirstCell)).trim();
                        sentence += firstCell.substring(counterOfFirstCell, getLengthOfSentence(firstCell, counterOfFirstCell));
                        for (int counterOfSecondCell = 0; counterOfSecondCell < secondCell.length(); ) {
                            //sentence += Character.toString(secondCell.charAt(counterOfSecondCell)).trim();
                            sentence += secondCell.substring(counterOfSecondCell, getLengthOfSentence(secondCell, counterOfSecondCell));
                            Set<String> aux = checksEquality(g, word, sentence);
                            x[targetLine][targetColumn].addAll(aux);
                            //sentence = sentence.substring(0, sentence.length() - 1);
                            sentence = sentence.substring(0, getIndexOfLastVariable(sentence));
                            counterOfSecondCell = updateCounter(secondCell, counterOfSecondCell);
                        }
                        counterOfFirstCell = updateCounter(firstCell, counterOfFirstCell);
                    }
                    lineFirstElement--;
                    lineSecondElement++;
                    columnSecondElement++;
                }
                targetColumn++;
                columnFirstElement++;
                lineFirstElement = line;
                lineSecondElement = line - counterLine;
                counterColumn++;
                columnSecondElement = column + counterColumn;
            }
            auxFlag++;
            counterLine++;
            counterLineOfSecondElement--;
            lineFirstElement = line;
            columnFirstElement = column;
            lineSecondElement = counterLineOfSecondElement;
            columnSecondElement = column + 1;
            targetColumn = 0;
            targetLine--;
            count++;
            delimiter--;
            counterColumn = 1;

        }
        return x;
    }

   static Set<String>[][] fillSecondLine(Set<String>[][] x, Grammar g,
                                                  String word) {
        for (int j = 0; j < word.length() - 1; j++) {
            String firstCell = returnsAlphabeticSymbols(x[word.length() - 1][j]);
            String secondCell = returnsAlphabeticSymbols(x[word.length() - 1][j + 1]);
            int counterOfFirstCell = 0;
            while (counterOfFirstCell < firstCell.length()) {
                String sentence = new String();
                //counterOfFirstCell = getBeginOfSentence(firstCell, counterOfFirstCell);
                sentence += firstCell.substring(counterOfFirstCell, getLengthOfSentence(firstCell, counterOfFirstCell));
                int counterOfSecondCell = 0;
                while (counterOfSecondCell < secondCell.length()) {
                    sentence += secondCell.substring(counterOfSecondCell, getLengthOfSentence(secondCell, counterOfSecondCell));
                    Set<String> aux = checksEquality(g, word, sentence);
                    x[word.length() - 2][j].addAll(aux);
                    sentence = sentence.substring(0, getIndexOfLastVariable(sentence));
                    counterOfSecondCell = updateCounter(secondCell, counterOfSecondCell);
                }
                counterOfFirstCell = updateCounter(firstCell, counterOfFirstCell);
            }
        }
        return x;
    }

    private static int updateCounter (final String cell, final int current) {
        int index = current;
        if (Character.isDigit(cell.charAt(index))) {
            while (index < cell.length() && Character.isDigit(index)) {
                index++;
            }
        } else {
            index++;
            while (index < cell.length() && Character.isDigit(cell.charAt(index))) {
                index++;
            }
        }
        return index;
    }

    private static int getBeginOfSentence (final String cell, final int current) {
        int index = current;
        if (index + 1 < cell.length()) {
            while (index + 1 < cell.length() && Character.isDigit(cell.charAt(index + 1))) {
                index++;
            }
        } else if (Character.isDigit(cell.charAt(index))) {
            while (index < cell.length() && Character.isDigit(cell.charAt(index))) {
                index++;
            }
        }
        return index;
    }

    private static int getIndexOfLastVariable (final String sentence) {
        int lenght = sentence.length() - 1;
        while (Character.isDigit(sentence.charAt(lenght))) {
            lenght--;
        }
        return lenght;
    }


    private static int getLengthOfSentence (final String cell, final int begin) {
        int end = begin + 1;
        while (end < cell.length() && Character.isDigit(cell.charAt(end))) {
            end++;
        }
        return end;
    }

    //remove caracteres que não sejam alfabéticos
    private static String returnsAlphabeticSymbols(Set<String> set) {
        String aux = new String();
        for (String element : set) {
            if (Character.isLetter(element.charAt(0)))
                aux += element;
        }
        return aux;
    }

    //preenche a primeira linha da tabela
   static Set<String>[][] fillFirstLine(Set<String>[][] x, Grammar g, String word) {
        for (int j = 0; j < word.length(); j++) {
            x[word.length() - 1][j] = checksEquality(g, word,
                    Character.toString(word.charAt(j)));
        }
        return x;
    }

    private static Set<String> checksEquality(Grammar g, String word, String letter) {
        Set<String> found = new TreeSet<>();
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
                    Set sentence = CYK[i][j];
                    String newSentence = new String();
                    newSentence = sentence.toString();
                    if (i + 1 == word.length()+1) {
                        newSentence = newSentence.replace("[", "");
                        newSentence = newSentence.replace("]", "");
                    } else {
                        newSentence = newSentence.replace("[", "{");
                        newSentence = newSentence.replace("]", "}");
                    }
                    newSentence = (newSentence.equals("{}") ? ("-") : (newSentence));
                    cykOut[i][j] = newSentence;
                } else {
                    cykOut[i][j] = "";
                }
            }
        }
        return cykOut;
    }

}