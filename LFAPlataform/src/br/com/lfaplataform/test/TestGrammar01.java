package br.com.lfaplataform.test;

import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;

import br.com.lfaplataform.vo.Grammar;
import br.com.lfaplataform.vo.GrammarParser;

public class TestGrammar01 extends TestCase {

	private Grammar g;

	@Override
	protected void setUp() throws Exception {
		String variables[] = new String[] { "S", "S1", "A", "B", "V" };
		String terminals[] = new String[] { "a", "b" };
		String initialSymbol = "S";
		String rules[] = new String[] { "S -> AB | BS1 | BV", "S1 -> AB",
				"A -> BA | a", "B -> aa | VS1 | S1V | S1S1 | b", "V -> a" };
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}

	@Override
	protected void tearDown() throws Exception {
	}

	@Test
	public void testCYK() {
		String[][] matrix = GrammarParser.CYK(g, "bbabaa");
		
		assertNotNull(matrix);
		assertNotNull(matrix[0][0]);
		assertNotEquals("", matrix[0][0]);
		
		String[] topVariables = matrix[0][0].split(",");
		
		assertEquals(2, topVariables.length);

		assertTrue(Arrays.asList(topVariables).contains("A"));
		assertTrue(Arrays.asList(topVariables).contains("S"));
	}

}
