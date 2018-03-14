package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by carlos on 10/21/17.
 */

public class AcademicSupportTest {

    private AcademicSupport academicSupport;

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    @Before
    public void setUp() {
        academicSupport = new AcademicSupport();
    }

    @Test
    public void testConstructor() {
        Grammar result = new Grammar("S → a | A12\nA12 → b | c");
        academicSupport = new AcademicSupport("", false, new LinkedHashMap<Integer, String>(),
                result, "", new LinkedHashSet<Rule>(), new LinkedHashSet<Rule>(),
                new ArrayList<Set<String>>(), new ArrayList<Set<String>>(),
                new ArrayList<Set<String>>(), new Grammar());

        String resultExpexted = "S → a | A<sub>12</sub><br>A<sub>12</sub> → b | c<br>";
        assertEquals(resultExpexted, academicSupport.getResult());
    }

    @Test
    public void testComments() {
        String comments = "comments";
        academicSupport.setComments(comments);
        assertEquals(comments, academicSupport.getComments());
    }

    @Test
    public void testSituation() {
        boolean situation = true;
        academicSupport.setSituation(situation);
        assertEquals(situation, academicSupport.getSituation());
    }

    @Test
    public void testFoundProblems() {
        Map<Integer, String> foundProblems = new LinkedHashMap<>();
        foundProblems.put(1, "Problem 1");
        Map<Integer, String> foundProblemsExpected = new LinkedHashMap<>(foundProblems);
        academicSupport.setFoundProblems(foundProblems);
        assertEquals(foundProblemsExpected, academicSupport.getFoundProblems());
    }

    @Test
    public void testGrammar() {
        String grammarTxt = "S → a";
        Grammar grammar = new Grammar(grammarTxt);
        Grammar grammarExpected = new Grammar(grammarTxt);
        academicSupport.setGrammar(grammar);
        assertEquals(grammarExpected, academicSupport.getGrammar());
    }

    @Test
    public void testResultGrammar() {
        Grammar result = new Grammar("S → a | A12'\nA12' → b | c | A12");
        String resultExpexted = "S → a | A<sub>12</sub>'<br>A<sub>12</sub>' → b | c | " +
                "A<sub>12</sub><br>";
        academicSupport.setResult(result);
        assertEquals(resultExpexted, academicSupport.getResult());
    }

    @Test
    public void testSolutionDescription() {
        String solutionDescription = "solutionDescription";
        academicSupport.setSolutionDescription(solutionDescription);
        assertEquals(solutionDescription, academicSupport.getSolutionDescription());
    }

    @Test
    public void testInsertedRules() {
        Set<Rule> insertedRules = new LinkedHashSet<>();
        insertedRules.add(new Rule("S", "a"));
        Set<Rule> insertedRulesExpected = new LinkedHashSet<>(insertedRules);
        insertedRulesExpected.add(new Rule("S", "A"));
        academicSupport.setInsertedRules(insertedRules);
        academicSupport.insertNewRule(new Rule("S", "A"));
        assertEquals(insertedRulesExpected, academicSupport.getInsertedRules());
    }

    @Test
    public void testIrregularRules() {
        Set<Rule> irregularRules = new LinkedHashSet<>();
        irregularRules.add(new Rule("S", "a"));
        Set<Rule> irregularRulesExpected = new LinkedHashSet<>(irregularRules);
        irregularRulesExpected.add(new Rule("S", "A"));
        academicSupport.setIrregularRules(irregularRules);
        academicSupport.insertIrregularRule(new Rule("S", "A"));
        assertEquals(irregularRulesExpected, academicSupport.getIrregularRules());
    }

    @Test
    public void testFirstSet() {
        Set<String> set1 = new LinkedHashSet<>();
        set1.add("ele1");
        Set<String> set2 = new LinkedHashSet<>();
        set1.add("ele2");
        List<Set<String>> fistSet = new ArrayList<>();
        fistSet.add(set1);
        List<Set<String>> firstSetExpected = new ArrayList<>(fistSet);
        firstSetExpected.add(set2);
        academicSupport.setFirstSet(fistSet);
        academicSupport.insertOnFirstSet(set2, "decision");
        assertEquals(firstSetExpected, academicSupport.getFirstSet());
    }

    @Test
    public void testSecondSet() {
        Set<String> set1 = new LinkedHashSet<>();
        set1.add("ele1");
        Set<String> set2 = new LinkedHashSet<>();
        set1.add("ele2");
        List<Set<String>> secondSet = new ArrayList<>();
        secondSet.add(set1);
        List<Set<String>> secondSetExpected = new ArrayList<>(secondSet);
        secondSetExpected.add(set2);
        academicSupport.setSecondSet(secondSet);
        academicSupport.insertOnSecondSet(set2, "decision");
        assertEquals(secondSetExpected, academicSupport.getSecondSet());
    }

    @Test
    public void testThirdSet() {
        Set<String> set1 = new LinkedHashSet<>();
        set1.add("ele1");
        Set<String> set2 = new LinkedHashSet<>();
        set1.add("ele2");
        List<Set<String>> thirdSet = new ArrayList<>();
        thirdSet.add(set1);
        List<Set<String>> thirdSetExpected = new ArrayList<>(thirdSet);
        thirdSetExpected.add(set2);
        academicSupport.setThirdSet(thirdSet);
        academicSupport.insertOnThirdSet(set2, "decision");
        assertEquals(thirdSetExpected, academicSupport.getThirdSet());
    }

}
