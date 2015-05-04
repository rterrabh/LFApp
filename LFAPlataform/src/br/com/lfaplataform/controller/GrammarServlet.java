package br.com.lfaplataform.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.lfaplataform.vo.Grammar;
import br.com.lfaplataform.vo.GrammarParser;
import br.com.lfaplataform.vo.Rule;

public class GrammarServlet extends HttpServlet {

	/*
	 * static Grammar parseTxtToGrammar(String txt) { Grammar g = new
	 * Grammar(txt); return g; }
	 */

	static void controlInputs(HttpServletRequest req, HttpServletResponse resp, String txtGrammar, Grammar g) throws ServletException, IOException {
		//Grammar g = new Grammar();
		String initialSymbol;
		if (!req.getParameter("initialSymbol").equals("")) {
			initialSymbol= req.getParameter("initialSymbol");
		} else {
			initialSymbol = "";
		}
		initialSymbol = Character.toString(Character.toUpperCase(initialSymbol.charAt(0)));
		String terminals = req.getParameter("terminals");
		String variables = req.getParameter("variables");
		
		if (initialSymbol.equals("")) {
			// símbolo inicial não foi especificado
			if (variables.equals("")
					&& !terminals.equals("")) {
				// conjunto de variáveis não inseridas, porém, símbolos
				// terminais foram
				
				//verifica se os terminais inseridos fazem sentido
				if (GrammarParser.compareSymbolWithParameter(terminals, req.getParameter("txtGrammar"))) {
					//é preciso verificar se faltam terminais a serem especificados ou se o usuário
					//especificou todos
					if (GrammarParser.formatTerminals(terminals).equals(GrammarParser.extractTerminalsFromFull(txtGrammar))) {
						// usuário inseriu todos os terminais, logo, os terminais obtidos via formulário são atribuídos
						g.setTerminals(GrammarParser.formatTerminals(terminals));
					}
					else {
						//usuário não inseriu todas as variáveis, é necessário extraí-las
						g.setTerminals(GrammarParser.extractTerminalsFromFull(txtGrammar));
					}						
					//extrai variáveis das regras
					g.setVariables(GrammarParser.extractVariablesFromFull(txtGrammar));
					//extrai símbolo inicial das regras
					g.setInitialSymbol(GrammarParser.extractInitialSymbolFromFull(txtGrammar));
					//realiza atribuição das regras
					g.setRules(GrammarParser.extractRulesFromFull(txtGrammar));
				}
				else {
					//terminais inseridos não fazem sentido
					req.getRequestDispatcher("warning.jsp").forward(req, resp);
				}
			} else {
				// conjunto de variáveis inseridas, porém, símbolos terminais
				// não foram
				
				//verifica se as variáveis inseridas fazem sentido
				if(GrammarParser.compareSymbolWithParameter(variables, req.getParameter("txtGrammar"))) {
					//é preciso verificar se faltam variáveis a serem especificadas ou se o usuário
					//especificou todas
					if (GrammarParser.formatVariables(variables).equals(GrammarParser.extractVariablesFromFull(txtGrammar))) {
						// usuário inseriu todas as variáveis, logo, as variáveis obtidas via formulário são atribuídas
						g.setVariables(GrammarParser.formatVariables(variables));
					}
					else {
						//usuário não inseriu todas as variáveis, é necessário extraí-las
						g.setVariables(GrammarParser.extractVariablesFromFull(txtGrammar));
					}	
					//extrai terminais das regras
					g.setTerminals(GrammarParser.extractTerminalsFromFull(txtGrammar));
					//extrai símbolo inicial das regras
					g.setInitialSymbol(GrammarParser.extractInitialSymbolFromFull(txtGrammar));
					//realiza atribuição das regras
					g.setRules(GrammarParser.extractRulesFromFull(txtGrammar));
				}
				else {
					//variáveis inseridas não fazem sentido
					req.getRequestDispatcher("warning.jsp").forward(req, resp);
				}
			}
		} else if (!initialSymbol.equals("")) {
			// símbolo inicial especificado
			if (variables.equals("")
					&& !terminals.equals("")) {
				// conjunto de variáveis não inseridas, porém, símbolos
				// terminais foram
				
				//verifica se os terminais inseridos fazem sentido
				if (GrammarParser.compareSymbolWithParameter(terminals, req.getParameter("txtGrammar"))) {
					//é preciso verificar se faltam terminais a serem especificados ou se o usuário
					//especificou todos
					if (GrammarParser.formatTerminals(terminals).equals(GrammarParser.extractTerminalsFromFull(txtGrammar))) {
						// usuário inseriu todos os terminais, logo, os terminais obtidos via formulário são atribuídos
						g.setTerminals(GrammarParser.formatTerminals(terminals));
					}
					else {
						//usuário não inseriu todas as variáveis, é necessário extraí-las
						g.setTerminals(GrammarParser.extractTerminalsFromFull(txtGrammar));
					}	
					//extrai variáveis das regras
					g.setVariables(GrammarParser.extractVariablesFromFull(txtGrammar));
					//extrai símbolo inicial das regras
					g.setInitialSymbol(GrammarParser.extractInitialSymbolFromFull(txtGrammar));
					//realiza atribuição das regras
					g.setRules(GrammarParser.extractRulesFromFull(txtGrammar));
				}
				else {
					//terminais inseridos não fazem sentido
					req.getRequestDispatcher("warning.jsp").forward(req, resp);
				}				
				
			} else if (!variables.equals("")
					&& terminals.equals("")) {
				// conjunto de variáveis inseridas, porém, símbolos terminais
				// não foram
				
				//verifica se as variáveis inseridas fazem sentido
				if(GrammarParser.compareSymbolWithParameter(variables, req.getParameter("txtGrammar"))) {
					//é preciso verificar se faltam variáveis a serem especificadas ou se o usuário
					//especificou todas
					if (GrammarParser.formatVariables(variables).equals(GrammarParser.extractVariablesFromFull(txtGrammar))) {
						// usuário inseriu todas as variáveis, logo, as variáveis obtidas via formulário são atribuídas
						g.setVariables(GrammarParser.formatVariables(variables));
					}
					else {
						//usuário não inseriu todas as variáveis, é necessário extraí-las
						g.setVariables(GrammarParser.extractVariablesFromFull(txtGrammar));
					}		
					//atribui símbolo inicial
					g.setInitialSymbol(initialSymbol);
					//realiza atribuição das regras
					g.setRules(GrammarParser.extractRulesFromFull(txtGrammar));
				}
				else {
					//variáveis inseridas não fazem sentido
					req.getRequestDispatcher("warning.jsp").forward(req, resp);
				}				
			} 
			else {
				// símbolo inicial, conjunto de variáveis e símbolos terminais
				// foram inseridos
				
				//verifica se os parâmetros inseridos fazem sentido
				if (GrammarParser.compareInitialSymbolWithParameter(initialSymbol, req.getParameter("txtGrammar")) &&
						GrammarParser.compareSymbolWithParameter(terminals, req.getParameter("txtGrammar")) &&
						GrammarParser.compareSymbolWithParameter(variables, req.getParameter("txtGrammar")))	{
					//é preciso verificar se faltam variáveis a serem especificadas ou se o usuário
					//especificou todas
					if (GrammarParser.formatVariables(variables).equals(GrammarParser.extractVariablesFromFull(txtGrammar))) {
						// usuário inseriu todas as variáveis, logo, as variáveis obtidas via formulário são atribuídas
						g.setVariables(GrammarParser.formatVariables(variables));
					}
					else {
						//usuário não inseriu todas as variáveis, é necessário extraí-las
						g.setVariables(GrammarParser.extractVariablesFromFull(txtGrammar));
					}		
					//é preciso verificar se faltam terminais a serem especificados ou se o usuário
					//especificou todos
					if (GrammarParser.formatTerminals(terminals).equals(GrammarParser.extractTerminalsFromFull(txtGrammar))) {
						// usuário inseriu todos os terminais, logo, os terminais obtidos via formulário são atribuídos
						g.setTerminals(GrammarParser.formatTerminals(terminals));
					}
					else {
						//usuário não inseriu todas as variáveis, é necessário extraí-las
						g.setTerminals(GrammarParser.extractTerminalsFromFull(txtGrammar));
					}	
					
					
					//realiza atribuições aos atributos do objeto
					g.setInitialSymbol(GrammarParser.formatInitialSymbol(initialSymbol));					
					g.setRules(GrammarParser.extractRulesFromFull(txtGrammar));
				}
				else {
					//algum parâmetro inserido não faz sentido
					req.getRequestDispatcher("warning.jsp").forward(req, resp);
				}
			}
		}
		else {
			//símbolo inicial não faz sentido
			req.getRequestDispatcher("warning.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		String txtGrammar = req.getParameter("txtGrammar");
		Grammar g;

		if (req.getParameter("txtGrammar").equals("")) {
			//senão houver regras estipuladas, o usuário é redirecionado para uma página
			//de erros, pois não é possível construir gramática
			req.getRequestDispatcher("warning.jsp").forward(req, resp);
		} else {
			txtGrammar = txtGrammar.trim();
			if (req.getParameter("variables").equals("")
					&& req.getParameter("terminals").equals("")
					&& req.getParameter("initialSymbol").equals("")) {
				// variáveis, terminais e símbolo inicial não foram informados, logo, todos os
				// dados devem ser extraídos do conjunto de regras
				g = new Grammar(txtGrammar);
					
			} else {
				//verifica o que foi inserido no formulário
				g = new Grammar();
				controlInputs(req,resp,txtGrammar,g);
			}

			//clona objeto grammar para fazer as devidas modificações
			//clonar objeto G
			//FALTA CLONAR OBJETO!!!
			
			// REALIZA CONTROLE DE AÇÕES A SEREM FEITAS
			switch (req.getParameter("action")) {
				case "Símbolo inicial não recursivo":
					// chama algoritmo para remoção de símbolo inicial não recursivo
					g = GrammarParser.getGrammarWithInitialSymbolNotRecursive(g);
					break;
				case "Remoção de símbolo lâmbda":
					// chama algoritmo para remoção de símbolo lâmbda
					g = GrammarParser.getGrammarEssentiallyNoncontracting(g);
					break;
				case "Remoção de regras da cadeia":
					//realiza remoção de regras da cadeia
					g = GrammarParser.getGrammarWithoutChainRules(g);
					break;
				case "Remoção de regras inalcançáveis":
					//realiza remoção de regras inalcançáveis
					g = GrammarParser.getGrammarWithoutNoReach(g);
					break;
				case "Remoção de regras não terminais":
					//realiza remoção de regras que não geram símbolos terminais
					g = GrammarParser.getGrammarWithoutNoTerm(g);
					break;
				case "Forma Normal de Chomsky":
					g = GrammarParser.FNC(g);
					break;
				case "Remoção de recursão à esquerda imediata":
					g = GrammarParser.removingTheImmediateLeftRecursion(g);
					break;
				case "Remoção de recursão à esquerda direta e indireta":
					g = GrammarParser.removingLeftRecursion(g);
					break;
				case "CYK":
					String word = req.getParameter("word");
					Set<String>[][] CYK = GrammarParser.CYK(g, word);
					String[][] cykOut = GrammarParser.turnsTreesetOnArray(CYK, word);
					req.setAttribute("CYK", cykOut);
					req.setAttribute("word", word);
					req.getRequestDispatcher("cyk_out.jsp").forward(req, resp);
					break;
			}
			for (String variable : g.getVariables()) {
				for (Rule element : g.getRules()) {
					if (element.getLeftSide().equals(variable)) {
						out.print(element.getLeftSide() + "->" + element.getRightSide()+ "<br/>");
					}
				}				
			}
			
			/*
			out.println("<html><body>");

			out.println(g.getInitialSymbol());
			out.println("<br>");

			for (String v : g.getVariables()) {
				if (v != null)
					out.println(v);
			}
			out.println("<br>");
			for (String t : g.getTerminals()) {
				if (t != null)
					out.println(t);
			}

			out.println("<br>");

			

			out.println("</body></html>");*/

			out.close();

		}	

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		out.println("<html><body>Testando Servlet</body></html>");
	}

}
