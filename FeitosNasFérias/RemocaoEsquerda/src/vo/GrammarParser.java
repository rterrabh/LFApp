package vo;

import java.util.HashSet;
import java.util.Set;

public class GrammarParser {
	
	private GrammarParser() {}
	
	public static Grammar removalOfDirectLeftRecursion(Grammar g) {
		
		Set<Rule> newSetOfRules = new HashSet<>();
		Set<String> newSetOfVariables = new HashSet<>();
		
		newSetOfVariables.addAll(g.getVariables());
		
		for (Rule element : g.getRule()) {
			String[] aux = element.getrightSide().split(" | ");
			String newRule = new String();
			//verificar se existe recursão à esquerda na linha
			if (existDirectLeftRecursion())
		}
		
		return null;
	}

}
