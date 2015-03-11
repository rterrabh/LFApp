package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import vo.Grammar;
import vo.GrammarParser;
import vo.Rule;

public class Main {

	public static boolean verificaVariavel(String gramatica, char aux) {
		boolean found = false;
		char aux1 = '\n';
		for (int i = 0; i < gramatica.length(); i++) {
			if (gramatica.charAt(i) == aux && found == false) {
				return true;
			}
			if (gramatica.charAt(i) == aux1) {
				found = false;
			}
			if (gramatica.charAt(i) == '-') {
				found = true;
			}
		}
		return false;

	}

	/*
	 * public static void insereNovaString(String gramatica, String aux) {
	 * String newString = new String();
	 * 
	 * }
	 */

	public static void main(String[] args) throws IOException {
			File arquivo = new File("entrada.txt");
			File arquivo_saida = new File("saida.txt");

			// escreve no arquivo
			FileWriter fw = new FileWriter(arquivo_saida, true);
			BufferedWriter bw = new BufferedWriter(fw);

			// lê do arquivo
			FileReader fr = new FileReader(arquivo);
			BufferedReader br = new BufferedReader(fr);
			String gramatica = new String();
			while (br.ready()) {
				gramatica += br.readLine();
				gramatica += "\n";
			}

			br.close();
			fr.close();
			gramatica = gramatica.trim();
			//System.out.println(gramatica);
			System.out.println("----------------------------");

			Grammar g = new Grammar(gramatica);

			g = GrammarParser.getGrammarWithInitialSymbolNotRecursive(g);
			g = GrammarParser.getGrammarEssentiallyNoncontracting(g);
			g = GrammarParser.getGrammarWithoutChainRules(g);
			g = GrammarParser.getGrammarWithoutNoTerm(g);
			g = GrammarParser.getGrammarWithoutNoReach(g);
			g = GrammarParser.FNC(g);
			/*
			 * IMPRESS System.out.println(g.getInitialSymbol());
			 * 
			 * for (String element : g.getVariables()) { if (element != null)
			 * System.out.println(element); }
			 * 
			 * for (Rule element : g.getRule()) {
			 * System.out.print(element.getleftSide());
			 * System.out.print(" -> ");
			 * System.out.print(element.getrightSide()); System.out.println(); }
			 * 
			 * for (String element : g.getTerminals()) { if (element != null)
			 * System.out.println(element); }
			 */
	}

}
