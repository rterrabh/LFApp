package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by carlos on 30/07/16.
 */
public class GrammarTest {

    private Grammar grammar;

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    /*
    S -> λ | AB
    A -> AB | CB | a
    B -> b | AB
    C -> c | AC
	*/

    @Before
    public void setUp() {
        String[] variables = new String[]{"S", "A", "B", "C"};
        String[] terminals = new String[]{"a", "b", "c"};
        String initialSymbol = "S";
        String[] rules = new String[]{
                "S -> λ | AB",
                "A -> AB | CB | a",
                "B -> b | AB",
                "C -> c | AC"};

        this.grammar = new Grammar(variables, terminals, initialSymbol, rules);
    }

    @Test
    public void testInitialSymbolNotRecursive() {

        Grammar newG = grammar.getGrammarWithInitialSymbolNotRecursive
                (grammar, new AcademicSupport());

        Grammar expectedGrammar = (Grammar) grammar.clone();

        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());

        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testGrammarEssentiallyNonContracting() {

        Grammar newG = grammar.getGrammarEssentiallyNoncontracting(grammar, new AcademicSupport());

        Grammar expectedGrammar = (Grammar) grammar.clone();

        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());

        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testChainRules() {

        Grammar newG = grammar.getGrammarWithoutChainRules(grammar, new AcademicSupport());

        Grammar expectedGrammar = (Grammar) grammar.clone();

        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());

        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testNoTerminals() {

        Grammar newG = grammar.getGrammarWithoutNoTerm(grammar, new AcademicSupport());

        Grammar expectedGrammar = (Grammar) grammar.clone();

        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());

        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testNoReach() {

        Grammar newG = grammar.getGrammarWithoutNoReach(grammar, new AcademicSupport());

        Grammar expectedGrammar = (Grammar) grammar.clone();

        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());

        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testFNC() {

        Grammar newG = grammar.FNC(grammar, new AcademicSupport());

        Grammar expectedGrammar = (Grammar) grammar.clone();

        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());

        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testRemovingTheImmediateLeftRecursion() {

        Grammar newG = grammar.removingTheImmediateLeftRecursion(grammar, new AcademicSupport());

        String[] variables = new String[]{"S", "A", "B", "C", "Z1"};
        String[] terminals = new String[]{"a", "b", "c"};
        String initialSymbol = "S";
        String[] rules = new String[]{
                "S -> λ | AB",
                "A -> CB | a | CBZ1 | aZ1",
                "B -> b | AB",
                "C -> c | AC",
                "Z1 -> B | BZ1"};


        Grammar expectedGrammar = new Grammar(variables, terminals, initialSymbol, rules);

        //ALTERACAO NO ALGORITMO
//        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
//        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
//        assertEquals(expectedGrammar.getRules(), newG.getRules());
//        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
        for (Rule rule : newG.getRules()) {
            assertTrue(!rule.existsLeftRecursion());
        }
    }

    @Test
    public void testRemovingLeftRecursion() {

        Grammar newG = grammar.removingLeftRecursion
                (grammar, new AcademicSupport(), new HashMap<String, String>(),
                        new AcademicSupportRLR());

        String[] variables = new String[]{"S", "A", "B", "C", "Z1"};
        String[] terminals = new String[]{"a", "b", "c"};
        String initialSymbol = "S";
        String[] rules = new String[]{
                "S -> λ | AB",
                "A -> cB | a | cBZ1 | aZ1",
                "B -> b | AB",
                "C -> c | AC",
                "Z1 -> B | CB | BZ1 | CBZ1"};


        Grammar expectedGrammar = new Grammar(variables, terminals, initialSymbol, rules);

        //ALTERACAO NO ALGORITMO
//        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
//        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
//        assertEquals(expectedGrammar.getRules(), newG.getRules());
//        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
        for (Rule rule : newG.getRules()) {
            assertTrue(!rule.existsLeftRecursion());
        }
    }

    @Test
    public void testAcademicSupportRemovingLeftRecursion() {

        AcademicSupportRLR academicSupportLR =
                new AcademicSupportRLR();
        grammar.removingLeftRecursion
                (grammar, new AcademicSupport(), new HashMap<String, String>(),
                        academicSupportLR);

        List<Set<Rule>> deleteRulesStage1 = new ArrayList<>();
        deleteRulesStage1.add(new HashSet<>(Arrays.asList(new Rule[]{new Rule("A", "CB")})));
        deleteRulesStage1.add(new HashSet<>(Arrays.asList(new Rule[]{new Rule("A", "AB"),
                new Rule("A", "ACB")})));
        List<Set<Rule>> newRulesStage1 = new ArrayList<>();
        newRulesStage1.add(new HashSet<>(Arrays.asList(new Rule[]{new Rule("A", "ACB"),
                new Rule("A", "cB")})));
        newRulesStage1.add(new HashSet<>(Arrays.asList(new Rule[]{new Rule("A", "cBZ1"),
                new Rule("A", "aZ1"), new Rule("Z1", "B"), new Rule("Z1", "CB"),
                new Rule("Z1", "BZ1"), new Rule("Z1", "CBZ1")})));
        List<Boolean> isImediateLeftRecursive = new ArrayList<>();
        isImediateLeftRecursive.add(false);
        isImediateLeftRecursive.add(true);

        //ALTERACAO NO ALGORITMO
        //assertEquals(deleteRulesStage1, academicSupportLR.getDeleteRulesStage1());
        //assertEquals(newRulesStage1, academicSupportLR.getNewRulesStage1());
        //assertEquals(isImediateLeftRecursive, academicSupportLR.getIsImediateLeftRecursiveStage1());
    }

    @Test
    public void testFNG() {
        Grammar newG = grammar.FNGTerra(grammar, new AcademicSupport());
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
    public void testFNGGrammar() {

        Grammar newG = grammar.FNGTerra(grammar, new AcademicSupport());

        String[] variables = new String[]{"S", "A", "B", "C", "Z1"};
        String[] terminals = new String[]{"a", "b", "c"};
        String initialSymbol = "S";
        String[] rules = new String[]{
                "S -> λ | cBB | aB | cBZ1B | aZ1B",
                "A -> cB | a | cBZ1 | aZ1",
                "B -> b | cBB | aB | cBZ1B | aZ1B",
                "C -> c | cBC | aC | cBZ1C | aZ1C",
                "Z1 -> b | cBB | aB | cBZ1B | aZ1B | cB | cBCB | aCB | cBZ1CB | aZ1CB | " +
                        "bZ1 | cBBZ1 | aBZ1 | cBZ1BZ1 | aZ1BZ1 | cBZ1 | cBCBZ1 | aCBZ1 | " +
                        "cBZ1CBZ1 | aZ1CBZ1"};

        Grammar expectedGrammar = new Grammar(variables, terminals, initialSymbol, rules);
        // ALTERACAO  NO ALGORITMO
//        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
//        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
//        assertEquals(expectedGrammar.getRules(), newG.getRules());
//        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
        String initialSymbolNewG = newG.getInitialSymbol();
        for (Rule rule : newG.getRules()) {
            assertTrue(rule.isFng(initialSymbolNewG));
        }
    }

    @Test
    public void testAcademicSupportFNG() {

        AcademicSupportFNG academicSupportFNG = new AcademicSupportFNG();
        grammar.FNGTerra(grammar, new AcademicSupport(), academicSupportFNG);

        List<Set<Rule>> deleteRulesStage2 = new ArrayList<>();
        deleteRulesStage2.add(new HashSet<>(Arrays.asList(new Rule[]{new Rule("C", "AC")})));
        deleteRulesStage2.add(new HashSet<>(Arrays.asList(new Rule[]{new Rule("B", "AB")})));
        deleteRulesStage2.add(new HashSet<>(Arrays.asList(new Rule[]{new Rule("S", "AB")})));
        List<Set<Rule>> newRulesStage2 = new ArrayList<>();
        newRulesStage2.add(new HashSet<>(Arrays.asList(new Rule[]{new Rule("C", "cBC"),
                new Rule("C", "aC"), new Rule("C", "cBZ1C"), new Rule("C", "aZ1C")})));
        newRulesStage2.add(new HashSet<>(Arrays.asList(new Rule[]{new Rule("B", "cBB"),
                new Rule("B", "aB"), new Rule("B", "cBZ1B"), new Rule("B", "aZ1B")})));
        newRulesStage2.add(new HashSet<>(Arrays.asList(new Rule[]{new Rule("S", "cBB"),
                new Rule("S", "aB"), new Rule("S", "cBZ1B"), new Rule("S", "aZ1B")})));
        List<Set<Rule>> deleteRulesStage3 = new ArrayList<>();
        deleteRulesStage3.add(new HashSet<>(Arrays.asList(new Rule[]{new Rule("Z1", "B"),
                new Rule("Z1", "CB"), new Rule("Z1", "BZ1"), new Rule("Z1", "CBZ1")})));
        List<Set<Rule>> newRulesStage3 = new ArrayList<>();
        newRulesStage3.add(new HashSet<>(Arrays.asList(new Rule[]{new Rule("Z1", "cBB"),
                new Rule("Z1", "aB"), new Rule("Z1", "cBZ1B"), new Rule("Z1", "aZ1B"),
                new Rule("Z1", "b"), new Rule("Z1", "cB"), new Rule("Z1", "cBCB"),
                new Rule("Z1", "aCB"), new Rule("Z1", "cBZ1CB"), new Rule("Z1", "aZ1CB"),
                new Rule("Z1", "cBBZ1"), new Rule("Z1", "aBZ1"), new Rule("Z1", "cBZ1BZ1"),
                new Rule("Z1", "aZ1BZ1"), new Rule("Z1", "bZ1"), new Rule("Z1", "cBZ1"),
                new Rule("Z1", "cBCBZ1"), new Rule("Z1", "aCBZ1"), new Rule("Z1", "cBZ1CBZ1"),
                new Rule("Z1", "aZ1CBZ1")})));

        // ALTERACAO ALGORITMO
//        assertEquals(deleteRulesStage2, academicSupportFNG.getDeleteRulesStage2());
//        assertEquals(deleteRulesStage3, academicSupportFNG.getDeleteRulesStage3());
//        assertEquals(newRulesStage2, academicSupportFNG.getNewRulesStage2());
//        assertEquals(newRulesStage3, academicSupportFNG.getNewRulesStage3());
    }


}
