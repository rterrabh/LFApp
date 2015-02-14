package vo;

public class GrammarParser {

	private GrammarParser() {}
	
	public static String CYK(Grammar g, String word) {
		
		String[][] X = new String[word.length()][word.length()];
		
		for (int i = 0; i < word.length(); i++) {
			for (Rule element : g.getRule()) {
				if (element.getrightSide().contains(Character.toString(word.charAt(i)))) {
					if (!X[i][i].contains(Character.toString(word.charAt(i))))
						X[i][i] += element.getleftSide();
				}
					 
			}
		}
		
		for (int step = 1; step < word.length(); step++) {
			for (int i = 0; i < (word.length() - step + 1); i++) {
				for (int k = i; k < (i + step - 2); k++) {
					for (Rule element : g.getRule()) {
						for (Rule aux : g.getRule()) {
							for (Rule aux2 : g.getRule()) {
								if (X[i][k].contains(aux.getleftSide()) &&
										X[k+1][i+step-1].contains(aux2.getleftSide()) &&
										element.getrightSide().contains(aux.getleftSide() + aux2.getleftSide()))
									X[i][i+step-1] += element.getleftSide();
							}
						}
					}
				}
			}
		}
		
		if (X[0][word.length()].contains(g.getInitialSymbol())) {
			return "Palavra pertence à linguagem.";
		}
		else {
			return "Palavra não pertence à linguagem.";
		}
	}
	
	
}
