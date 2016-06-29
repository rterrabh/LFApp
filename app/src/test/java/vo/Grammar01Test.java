package vo;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;


public class Grammar01Test {

	private Grammar g;

	/*
	 S -> AB | BC | BV
	 A -> BA | a
	 B -> aa | VC | CV | CC | b
	 C -> AB
	 V -> a
	*/

	@Before
	public void setUp() {
		String variables[] = new String[] { "S", "A", "B", "C", "V" };
		String terminals[] = new String[] { "a", "b" };
		String initialSymbol = "S";
		String rules[] = new String[] {
				"S -> AB | BC | BV",
				"A -> BA | a",
				"B -> aa | VC | CV | CC | b",
				"C -> AB",
				"V -> a" };
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}

	@Test
	public void testInitialSymbolNotRecursive() {

		Grammar newG = g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		Grammar expectedGrammar = setGrammar();

		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());

		assertEquals(expectedGrammar.getRules(), newG.getRules());

		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

	@Test
	public void testGrammarEssentiallyNonContracting() {

		Grammar newG = g.getGrammarEssentiallyNoncontracting(this.g, new AcademicSupport());
		Grammar expectedGrammar = setGrammar();

		assertEquals(expectedGrammar.getRules(), newG.getRules());

		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());

		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

	@Test
	public void testChainRules() {

		Grammar newG = g.getGrammarWithoutChainRules(this.g, new AcademicSupport());
		Grammar expectedGrammar = setGrammar();

		assertEquals(expectedGrammar.getRules(), newG.getRules());

		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());

		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

	@Test
	public void testNoTerminals() {
		Grammar newG = g.getGrammarWithoutNoTerm(this.g, new AcademicSupport());
		Grammar expectedGrammar = setGrammar();

		assertEquals(expectedGrammar.getRules(), newG.getRules());

		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());

		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

	@Test
	public void testNoReach() {
		Grammar newG = g.getGrammarWithoutNoReach(this.g, new AcademicSupport());
		Grammar expectedGrammar = setGrammar();

		assertEquals(expectedGrammar.getRules(), newG.getRules());

		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());

		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}


	@Test
	public void testFNC() {
		Grammar newG = g.FNC(this.g, new AcademicSupport());

		String[] expectedVariables = new String[] { "S", "A", "B", "C", "V", "T1" };
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> AB | BC | BV",
				"A -> BA | a",
				"B -> T1T1 | VC | CV | CC | b",
				"C -> AB",
				"V -> a",
				"T1 -> a" };

		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);

		assertEquals(expectedGrammar.getRules(), newG.getRules());

		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());

		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}


	@Test
	public void testFNG() {
		Grammar newG = g.FNG(g, new AcademicSupport());
		boolean fng = true;
		for (Rule element : newG.getRules()) {
			int counter = 0;
			if (!element.getLeftSide().equals(newG.getInitialSymbol()) && element.getRightSide().equals("")) {
				fng = false;
			} else {
				for (int i = 0; i < element.getRightSide().length() && fng; i++) {
					if (Character.isLowerCase(element.getRightSide().charAt(i))) {
						counter++;
					}
				}
				if (counter > 1) {
					fng = false;
				}
			}
		}

		assertEquals(true, fng);
		assertTrue(newG.isFNG());
	}



	@Test
	public void testCYK() {
		Set<String>[][] matrix = Grammar.CYK(g, "bbabaa");

		assertNotNull(matrix);
		assertNotNull(matrix[0][0]);
		assertNotEquals("", matrix[0][0]);

		Set<String> topVariables = matrix[0][0];

		assertEquals(2, topVariables.size());

		assertTrue(topVariables.contains("A"));
		assertTrue(topVariables.contains("S"));
	}
	
	
	public Grammar setGrammar() {
		String[] expectedVariables = new String[] { "S", "A", "B", "C", "V" };
		String[] expectedTerminals = new String[] { "a", "b" };
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> AB | BC | BV",
				"A -> BA | a",
				"B -> aa | VC | CV | CC | b",
				"C -> AB",
				"V -> a" };

		return new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);
	}

}
