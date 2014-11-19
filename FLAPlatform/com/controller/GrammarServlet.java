package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.Grammar;
import vo.Rule;

public class GrammarServlet extends HttpServlet {	
	
	static Grammar parseTxtToGrammar(String txt) {
		Grammar g = new Grammar(txt);
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
		
		//REALIZA CONTROLE DE AÇÕES A SEREM FEITAS
		String control = new String();
		if (req.getParameter("SINR") != null)
			control = "SINR";
		else if (req.getParameter("RSL") != null)
			control = "RSL";
		
		switch (control) {
			case "SINR" :
				//chama algoritmo de remoção de símbolo inicial não recursivo
				break;
			case "RSL":
				//chama algoritmo de remoção de lambda
				break;
		}
		
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
		
		
		for (Rule r : g.getRule()) {
			out.println(r.getleftSide());
			out.println(" -> ");
			out.println(r.getrightSide());
			out.println("<br>");
		}
		
		
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
