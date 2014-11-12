package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Connection extends HttpServlet {	
	
	static Grammar parseTxtToGrammar(String txt) {
		//get the rules
		String[] rules = txt.split("\n");
		
		//search for the initial symbol
		char initialSymbol = ' ';
		initialSymbol = txt.charAt(0);
		
		//search for the terminals
		String[] terminalsAux = new String[txt.length()];
		int j = 0;
		for (int i = 0; i < txt.length(); i++) {
				if (Character.isLowerCase(txt.charAt(i))) {
					terminalsAux[j] = Character.toString(txt.charAt(i));
					j++;
			}
		}
		
		//search for the variables
		String[] variablesAux = new String[txt.length()];
		j = 0;
		for (int i = 0; i < txt.length(); i++) {
			if (Character.isUpperCase(txt.charAt(i))) {
				variablesAux[j] = Character.toString(txt.charAt(i));
				j++;
			}
		}
		
		Grammar g = new Grammar(variablesAux, terminalsAux, initialSymbol, rules);
		return g;		
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		String txtGrammar = req.getParameter("txtGrammar");	
		Grammar g = new Grammar();
		
		txtGrammar = txtGrammar.trim();
		g = parseTxtToGrammar(txtGrammar);	
		
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
		ArrayList<Rule> rule = g.getRule();
		
		for (int i = 0; i < rule.size(); i++) {
			out.println(rule.get(i).getleftSide());
			out.println(" -> ");
			out.println(rule.get(i).getrightSide());
			out.println("<br>");
		}
		
		out.println("<html><body>");
		
		
		/*
		char initialSymbol = ' ';
		txtGrammar = txtGrammar.trim();
		initialSymbol = txtGrammar.charAt(0);
		
		String[] terminalsAux = new String[txtGrammar.length()];
		int j = 0;
		for (int i = 0; i < txtGrammar.length(); i++) {
				if (Character.isLowerCase(txtGrammar.charAt(i))) {
					terminalsAux[j] = Character.toString(txtGrammar.charAt(i));
					j++;
			}
		}
		
		String[] variablesAux = new String[txtGrammar.length()];
		j = 0;
		for (int i = 0; i < txtGrammar.length(); i++) {
			if (Character.isUpperCase(txtGrammar.charAt(i))) {
				variablesAux[j] = Character.toString(txtGrammar.charAt(i));
				j++;
		}
	}
	*/
		
		out.println("</body></html>");
	
		
		
		out.close();
		
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
