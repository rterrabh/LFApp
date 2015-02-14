package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import vo.Grammar;
import vo.GrammarParser;

public class Main {

	public static void main(String[] args) throws IOException {
		
		File arquivo = new File("entrada.txt");

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
	
		Scanner in = new Scanner(System.in);
		String word = new String();
		
		System.out.println("Entre com a palavra: ");
		word = in.next();
		
		String out = new String();
		
		out = GrammarParser.CYK(g, word); 
		
		System.out.println(out);
		
	}

	
	
}
