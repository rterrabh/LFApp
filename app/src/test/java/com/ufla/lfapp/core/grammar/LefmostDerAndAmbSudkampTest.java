package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.core.grammar.parser.TreeDerivation;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationParser;
import com.ufla.lfapp.utils.ResourcesContext;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

/**
 * Created by carlos on 01/08/17.
 */

public class LefmostDerAndAmbSudkampTest {

    static {
        ResourcesContext.isTest = true;
    }

    // pag. 91 FEATURE - Grammar - Leftmost Derivation and Ambiguity
    @Test
    public void example_3_5_1Test() {
        String grammarTxt = "S -> aS | Sa | a";
        String string = "aa";
        Grammar grammar = new Grammar(grammarTxt);
        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar, string);
        treeDerivationParser.parser();
        TreeDerivation tree1 = treeDerivationParser.getTreeDerivation();
        TreeDerivation tree2 = treeDerivationParser.getTreeDerivationAux();
        assertTrue(tree2 != null);
        assertTrue(treeDerivationParser.isAmbiguity());
    }

    //pag. 92 FEATURE - Grammar - Leftmost Derivation and Ambiguity
    @Test
    public void example_3_5_2Test() {
        String grammarTxt = "S -> bS | Sb | a";
        String string = "bab";
        Grammar grammar = new Grammar(grammarTxt);
        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar, string);
        treeDerivationParser.parser();
        TreeDerivation tree1 = treeDerivationParser.getTreeDerivation();
        TreeDerivation tree2 = treeDerivationParser.getTreeDerivationAux();
        assertTrue(tree2 != null);
        assertTrue(treeDerivationParser.isAmbiguity());
    }


}
