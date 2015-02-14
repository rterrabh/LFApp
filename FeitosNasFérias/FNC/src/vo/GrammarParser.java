package vo;

import java.util.ArrayList;
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
	
	public static String extractInitialSymbolFromFull(String txt) {
		return Character.toString(txt.charAt(0));
	}
	
	public static boolean compareInitialSymbolWithParameter(String initialSymbol, String rules) {
		for (int i = 0; i < rules.length(); i++) {
			if (initialSymbol.equals(rules.charAt(i)))
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
	
	public static String formatInitialSymbol(String initialSymbol) {
		return Character.toString(Character.toUpperCase(initialSymbol.charAt(0)));
	}
	
	//retira símbolo inicial recursivo
	//retorna gramática sem símbolo inicial recursivo
	public static Grammar getGrammarWithInitialSymbolNotRecursive(Grammar g) {
		String initialSymbol = g.getInitialSymbol();
		String auxLeft = new String();
		String auxRight = new String();
		Iterator it = g.getRule().iterator();
		boolean insert = false;
		
		for (Rule element : g.getRule()) {
			if (element.getleftSide().equals(initialSymbol)) {
				if (element.getrightSide().contains(initialSymbol)) {
					auxLeft = element.getleftSide();
					auxRight = element.getrightSide();
					insert = true;
				}
			}
		}
		if (insert == true) {
			g.insertRule(initialSymbol + "'", initialSymbol);
			g.setInitialSymbol(initialSymbol + "'");
		}
		//g.removeRule(auxLeft, auxRight);
		/*String teste = aux.replace(initialSymbol,' ');
		//Iterator it = g.getRule().iterator();
		while (it.hasNext()) {
			Rule r =  (Rule) it.next(); 
			if (r.getrightSide().equals(aux))
				it.remove();
		}*/
		return g;
	}
	
	public static String searchVariablesOnRules(String element) {
		String containsVariables = new String();
		boolean found = false;
		String[] verifyRules = element.split(" | ");
		for (String aux : verifyRules) {
			for (int i = 0; i < aux.length(); i++) {
				if (Character.isLowerCase(aux.charAt(i)))
					found = true;
			}
			if (found == false) {
				for (int i = 0; i < aux.length(); i++) {
					if (Character.isUpperCase(aux.charAt(i)))
						containsVariables += Character.toString(aux.charAt(i));
				}
			}
			found = false;
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
		/*for (int i = 0; i < aux.length(); i++) {
			if (aux.charAt(i) == '.')
				
		}*/
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(aux);
		for (int i = 0; i < stringBuilder.length(); i++) {
			if (stringBuilder.charAt(i) == '.')
				stringBuilder.setCharAt(i, Character.toLowerCase(element.getleftSide().charAt(0)));
		}
		//aux.replaceAll(".", "d");
		//Character.toString(Character.toLowerCase(element.getleftSide().charAt(0)))
		aux = stringBuilder.toString();
		return aux;
	}
	
	public static String permutation(String variables, int index, String aux) {
		String newSentence = new String();
		if (aux.length() != 1) {
			newSentence = " | ";
			for (int i = 0; i < aux.length(); i++) {
				if (i != index)
					newSentence += Character.toString(aux.charAt(i));
			}
		}
		return newSentence;
	}
	
	public static String cancelsVariables(String variables, Set<String> nullableVariables) {
		String[] searchVariables = variables.split(" | ");
		for (String aux : searchVariables) {
			for (int i = 0; i < aux.length(); i++) {
				if (nullableVariables.contains(Character.toString(aux.charAt(i)))) {
					//se a variável que estou percorrendo é anulável, deve ocorrer a permutação
					variables += permutation(variables, i, aux);
				}
			}
		}		
		return variables;
	}
	
	//remove símbolos terminais de regras
	//retorna gramática essencialmente não-contrátil
	public static Grammar getGrammarEssentiallyNoncontracting(Grammar g) {
		
		//conjunto que irá armazenar o conjunto de variáveis anuláveis
		Set<String> nullableVariables = new HashSet<>();
		Set<String> nullableVariablesAux = new HashSet<>();
		
		//conjunto que irá armazenar o conjunto de próximas variáveis
		Set<String> prev = new HashSet<>();
		
		//percorre todas as regras procurando alguma variável que produza vazio
		for (Rule element : g.getRule()) {
			if (element.getrightSide().contains(Character.toString('.')))
				nullableVariables.add(element.getleftSide());
		}
		
		
		
		//calcula conjunto de variáveis anuláveis
		do {
			prev.addAll(nullableVariables);
			for (Rule element : g.getRule()) {
				String containsVariables = searchVariablesOnRules(element.getrightSide());
				if (searchVariables(containsVariables, prev)) {
					nullableVariables.add(element.getleftSide());
				}
			}
		} while (!nullableVariables.equals(prev));
		
		nullableVariablesAux.addAll(nullableVariables);
		
		//realiza comparações 
		//procura vazio
		Grammar teste = new Grammar();
		for (Rule element : g.getRule()) {
			//verifica se variável está no conjunto anulável e se
			//contém o símbolo lâmbda
			if (nullableVariablesAux.contains(element.getleftSide())
					&& element.getrightSide().contains(".")) {
				String aux = element.getrightSide();
				aux = replaceEmpty(aux, element);
				teste.insertRule(element.getleftSide(), aux);
				nullableVariablesAux.remove(element.getleftSide());
			}
			else {
				teste.insertRule(element.getleftSide(), element.getrightSide());
			}
		}
		
		
		g.setRule(teste.getRule());
		
		/*for (Rule element : g.getRule()) {
			 System.out.print(element.getleftSide());
			 System.out.print(" -> ");
			 System.out.print(element.getrightSide());
			 System.out.println();
		 }
		System.out.println("---------------------");
		*/
		
		
		Grammar teste2 = new Grammar();
		
		for (Rule element : g.getRule()) {
			if (nullableVariablesAux.contains(element.getleftSide())) {
				//preciso trabalhar com variáveis do lado direito
				String aux = element.getrightSide();
				aux = cancelsVariables(aux, nullableVariables);
				teste2.insertRule(element.getleftSide(), aux);
			}
			else {
				teste2.insertRule(element.getleftSide(), element.getrightSide());
			}
		}
		
		
		g.setRule(teste2.getRule());
		
		
		
				
		
		if (nullableVariables.contains(g.getInitialSymbol())) {
			Grammar teste3 = new Grammar();
			for (Rule element : g.getRule()) {
				if (element.getleftSide().equals(g.getInitialSymbol())) {
					teste3.insertRule(element.getleftSide(), element.getrightSide() + " | .");
				}
				else {
					teste3.insertRule(element.getleftSide(), element.getrightSide());
				}
			}
			g.setRule(teste3.getRule());
		}
		
				
		
				
		return g;
	}
	
	//verifica se a sentença possui regra da cadeia
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
	
	public static boolean foundChainRules(char caracter, String s, ArrayList<Rule> noChainRules) {
		//pesquisa se o caracter está no conjunto direto
		boolean found = false;
		for (int i = 0; i < noChainRules.size() && found == false; i++) {
			if (noChainRules.get(i).getleftSide().equals(Character.toString(caracter))) {
				s += noChainRules.get(i).getrightSide() + " ";				
				//CONTINUAR AQUI
				found = true;
			}
		}
		return found;
	}
	
	public static String replaceChainRules(char caracter, String s, ArrayList<Rule> noChainRules) {
		boolean found = false;
		for (int i = 0; i < noChainRules.size() && found == false; i++) {
			if (noChainRules.get(i).getleftSide().equals(Character.toString(caracter))) {
				s += noChainRules.get(i).getrightSide() + " ";				
				//CONTINUAR AQUI
				found = true;
			}
		}
		return s;
	}
	
	public static void searchChainRules(String[] aux, ArrayList<Rule> noChainRules, ArrayList<Rule> chainRules, Rule r) {
		String s = new String();
		String leftSide = new String();
		for (int i = 0; i < aux.length; i++) {
			if (aux[i].length() == 1 && !aux[i].contains("|") && Character.isUpperCase(aux[i].charAt(0))) {
				//primeiramente deve-se verificar se a regra da cadeia pode ser 
				//resolvida diretamente
				if (foundChainRules(aux[i].charAt(0), s, noChainRules)) {
					//se a regra da cadeia puder ser selecionada imediatamente
					s = replaceChainRules(aux[i].charAt(0), s, noChainRules);					
				}
			} 
			else {
				s += aux[i] + " "; 
			}
		}
		r.setrightSide(s);

	}
	
	public static int returnIndex(ArrayList<Rule> chainRules, String leftside) {
		int i = 0;
		boolean aux = false;
		for (;i < chainRules.size() && aux == false; i++) {
			if (chainRules.get(i).getleftSide().equals(leftside))
				aux = true;
		}
		return i - 1;
	}
	
	
	//remove regras da cadeia
	//retorna gramática sem regras da cadeia
	public static Grammar getGrammarWithoutChainRules(Grammar g) {
		//primeiro passo, dividir as regras em dois conjuntos: possui chain rules
		// e não possui chain rules
		ArrayList<Rule> noChainRules = new ArrayList<Rule>();
		ArrayList<Rule> chainRules = new ArrayList<Rule>();
		
		//padronizando a gramática
		for (Rule element : g.getRule()) {
			System.out.println(element.getrightSide());
			String[] auxRightSide = element.getrightSide().split(" | ");
			for (int i = 0; i < auxRightSide.length; i++) {
				auxRightSide[i] = auxRightSide[i].trim();
			}
			if (isChain(auxRightSide)) {
				Rule r = new Rule();
				r.setleftSide(element.getleftSide());
				r.setrightSide(element.getrightSide());
				chainRules.add(r);
			}
			else {
				Rule r = new Rule();
				r.setleftSide(element.getleftSide());
				r.setrightSide(element.getrightSide());
				noChainRules.add(r);		
			}
		}
		
		//enquanto houver chain rules
		while (chainRules.size() != 0) {
			System.out.println(chainRules.size());
			Rule r = new Rule();
			for (int i = 0; i < chainRules.size(); i++) {
				String[] aux = chainRules.get(i).getrightSide().split(" | ");
				for (int j = 0; j < aux.length; j++) {
					aux[j] = aux[j].trim();
				}
				r.setleftSide(chainRules.get(i).getleftSide());
				searchChainRules(aux, noChainRules, chainRules, r);
				System.out.println(r.getleftSide() + " -> " + r.getrightSide());
				//concertar o loop
				chainRules.remove(returnIndex(chainRules, r.getleftSide()));
				for (int j = 0; j < chainRules.size(); j++)
					System.out.println(chainRules.get(j).getleftSide() + " -> " + chainRules.get(j).getrightSide());
				System.out.println(chainRules.size());
			}			
			noChainRules.add(r);
		}
		System.out.println("--------------------");
		for (int j = 0; j < noChainRules.size(); j++)
			System.out.println(noChainRules.get(j).getleftSide() + " -> " + noChainRules.get(j).getrightSide());
		//PRA TERMINAR \o/ : copiar o arraylist noChainRules em uma nova gramática e 
		//substituir a gramática antiga pela nova! END OF CHAIN RULES!!!
		
		//utilizando gramática auxiliar para armazenar novas regras
		Grammar g2 = new Grammar();
		
		//copia elementos do ArrayList na gramática auxiliar
		for (int i = 0; i < noChainRules.size(); i++) {
			g2.insertRule(noChainRules.get(i).getleftSide(), noChainRules.get(i).getrightSide());;
		}
		
		//copia gramática auxiliar na gramática principal
		g.setRule(g2.getRule());
		
		for (Rule element : g.getRule()) {
			System.out.println(element.getleftSide() + " -> " + element.getrightSide());
		}		
		return g;
	}
	
	//atualiza as regras da gramática após rodar algoritmos de remoção
	// de símbolos inúteis
	public static Set<Rule> updateRules(Set<String> prev, Grammar g) {
		Set<Rule> newRules = new HashSet<>();
		for (Rule element : g.getRule()) {
			String newRule = new String();
			String[] aux = element.getrightSide().split(" | ");
			for (int i = 0; i < aux.length; i++) {
				aux[i] = aux[i].trim();
				boolean insertOnNewRule = false;
				for (int j = 0; j < aux[i].length(); j++) {	
					if (Character.isUpperCase(aux[i].charAt(j))) {
						if (prev.contains(Character.toString(aux[i].charAt(j))))
							insertOnNewRule = true;
						else
							insertOnNewRule = false;
					}
				}
				if (insertOnNewRule) {
					newRule += aux[i] + " | ";
				}
			}
			if (newRule.length() != 0) {   
				Rule r = new Rule();
				r.setleftSide(element.getleftSide());
				r.setrightSide(newRule);
				newRules.add(r);
			}
		}
		return newRules;
	}
	
	//atualiza os terminais da gramática após remover variáveis inúteis
	public static Set<String> updateTerminals(Grammar g) {
		Set<String> newTerminals = new HashSet<>();
		for (Rule element : g.getRule()) {
			for (int i = 0; i < element.getrightSide().length(); i++) {
				if (Character.isLowerCase(element.getrightSide().charAt(i)))
					newTerminals.add(Character.toString(element.getrightSide().charAt(i)));
			}
		}
		return newTerminals;
	}
	
	public static Grammar getGrammarWithoutNoTerm(Grammar g) {
		Set<String> term = new HashSet<>();
		Set<String> prev = new HashSet<>();
		
		//preenche conjunto term com as variáveis que são terminais
		for (Rule element : g.getRule()) {
			String[] aux = element.getrightSide().split(" | ");
			for (int i = 0; i < aux.length; i++) {
				aux[i] = aux[i].trim();
				if (aux[i].length() == 1 && (!aux[i].equals("|")) && Character.isLowerCase(aux[i].charAt(0))) {
					term.add(element.getleftSide());
				}
			}
		}
		
		
		
		do {
			prev.addAll(term);
			for (Rule element : g.getRule()) {
				String[] aux = element.getrightSide().split(" | ");
				for (int i = 0; i < aux.length; i++) {
					aux[i] = aux[i].trim();
				}
				boolean insertOnTerm = false;
				for (int i = 0; i < aux.length && insertOnTerm == false; i++) {
					if (aux[i].length() == 1) {
						if (g.getTerminals().contains(Character.toString(aux[i].charAt(0))) ||
							prev.contains(Character.toString(aux[i].charAt(0)))) {
								insertOnTerm = true;
							}
					}
					else {
						for (int j = 0; j < aux[i].length(); j++) {
							if (g.getTerminals().contains(Character.toString(aux[i].charAt(j))) ||
									prev.contains(Character.toString(aux[i].charAt(j)))) {
								insertOnTerm = true;
							}
							else {
								insertOnTerm = false;
							}
						}			
					}
				}
				if (insertOnTerm) {
					term.add(element.getleftSide());
				}
			}
		} while (!term.equals(prev));
		
		Grammar aux = new Grammar();
		
		aux.setVariables(prev);
		aux.setInitialSymbol(g.getInitialSymbol());
		aux.setRule(updateRules(prev, g));
		aux.setTerminals(updateTerminals(aux));
		
		g.setVariables(aux.getVariables());
		g.setTerminals(aux.getTerminals());
		g.setRule(aux.getRule());
		
		return g;
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
				for (Rule secondElement : g.getRule()) {
					if (secondElement.getleftSide().equals(element)) {
						reach.addAll(variablesInW(reach, secondElement.getrightSide()));
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
		g.setRule(aux.getRule());
		
		return g;
	}
	
	//verifica se a produção é do tipo AA
	public static boolean inThePattern(String sentence) {
		boolean value = true;
		for (int i = 0; i < sentence.length() && value == true; i++) {
			if (!Character.isUpperCase(sentence.charAt(i)) || i > 2)
				value = false;
		}
		return value;
	}
	
	public static boolean productionContainsElement(String getrightSide,
			Set<Rule> newSetOfRules) {
		
		boolean estaInserido = false;
		for (Rule element : newSetOfRules) {
			if (element.getrightSide().equals(getrightSide))
				estaInserido = true;
		}
		
		return estaInserido;
	}
	
	public static String searchProduction(String getrightSide, Set<Rule> newSetOfRules) {
		String aux = new String();
		for (Rule element : newSetOfRules) {
			if (element.getrightSide().equals(getrightSide))
				aux = element.getleftSide();
		}
		return aux;
	}
	
	public static Grammar FNC(Grammar g) {
		
		Set<String> newSetOfVariables = new HashSet<>();
		Set<Rule> newSetOfRules = new HashSet<>();
		
		newSetOfVariables.addAll(g.getVariables());
		
		for (Rule element : g.getRule()) {
			int contTemp = 1;
			
			String[] aux = element.getrightSide().split(" | ");
			for (int i = 0; i < aux.length; i++) {
				aux[i] = aux[i].trim();
				String newProduction = new String();
				Rule r = new Rule();
				//elimina o processamento das variáveis que produzem lambda ou terminais
				if (aux[i].length() != 1) {
					//verifica se a produção é do tipo AA
					if (aux[i].length() == 2) {
						if (!inThePattern(aux[i])) {
							//produção possui tamanho 2 mas não está na forma AA
							if (Character.isLowerCase(aux[i].charAt(0)) && Character.isUpperCase(aux[i].charAt(1))) {
								//produção da forma aA
								r.setrightSide(Character.toString(aux[i].charAt(0)));
								if (productionContainsElement(r.getrightSide(), newSetOfRules)) {
									newProduction += searchProduction(r.getrightSide(), newSetOfRules) + aux[i].charAt(1) + " | ";
								}
								else {
									newProduction += "T" + contTemp + aux[i].charAt(1) + " | ";
									newSetOfVariables.add("T" + contTemp);								
									r.setleftSide("T" + contTemp);
									newSetOfRules.add(r);
									contTemp++;
								}
							}
							else if (Character.isUpperCase(aux[i].charAt(0)) && Character.isLowerCase(aux[i].charAt(1))) {
								//produção da forma Aa
								r.setrightSide(Character.toString(aux[i].charAt(1)));
								if (productionContainsElement(r.getrightSide(), newSetOfRules)) {
									newProduction += aux[i].charAt(0) + searchProduction(r.getrightSide(), newSetOfRules) + " | ";
								}
								else {
									newProduction += aux[i].charAt(0) + "T" + contTemp + " | ";
									newSetOfVariables.add("T" + contTemp);								
									r.setleftSide("T" + contTemp);
									newSetOfRules.add(r);
									contTemp++;
								}
									
							}
							else {								
								//produção da forma aa
								r.setrightSide(Character.toString(aux[i].charAt(0)));
								if (productionContainsElement(r.getrightSide(), newSetOfRules)) {
									newProduction += searchProduction(r.getrightSide(), newSetOfRules);
								}
								else {
									newProduction += "T" + contTemp;
									newSetOfVariables.add("T" + contTemp);								
									r.setleftSide("T" + contTemp);
									newSetOfRules.add(r);
									contTemp++;	
								}
								r.setrightSide(Character.toString(aux[i].charAt(1)));
								if (productionContainsElement(r.getrightSide(), newSetOfRules)) {
									newProduction += searchProduction(r.getrightSide(), newSetOfRules) + " | ";
								}
								else {
									newProduction += "T" + contTemp + " | ";
									newSetOfVariables.add("T" + contTemp);								
									r.setleftSide("T" + contTemp);
									newSetOfRules.add(r);
									contTemp++;	
								}
							}													
						}
					}
				}
			}
		}
	
		return g;
	}	
}
