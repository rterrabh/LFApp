package com.ufla.lfapp.core;

import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.machine.pda.PDAToGrammar;
import com.ufla.lfapp.core.grammar.parser.Permut;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationParser;
import com.ufla.lfapp.core.grammar.parser.WordEnumerator;
import com.ufla.lfapp.core.machine.pda.GrammarToPDAExt;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.pda.PDATransitionFunction;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 12/14/16.
 */

public class TreeDerivationParserTest {

    @Test
    public void test() {
        Set<Integer> a = new HashSet<>();
        System.out.println(a.add(10));
        System.out.println(a.add(11));
        System.out.println(a.add(12));
        System.out.println(a.add(13));
        System.out.println(a.add(14));
        System.out.println(a.add(10));
        Set<Integer> b = new HashSet<>();
        b.add(10);
        b.add(15);
        b.add(11);
        System.out.println(a.addAll(b));
        System.out.println(a.addAll(b));
    }

    @Test
    public void testPDA2() {
        State q0 = new State("q0");
        State q1 = new State("q1");
        SortedSet<State> states = new TreeSet<>();
        states.add(q0);
        states.add(q1);
        SortedSet<State> finalStates = new TreeSet<>();
        finalStates.add(q1);
        Set<PDATransitionFunction> tFPA = new TreeSet<>();
        tFPA.add(new PDATransitionFunction(q0, "a", q0, "A", Symbols.LAMBDA));
        tFPA.add(new PDATransitionFunction(q0, "c", q1, Symbols.LAMBDA, Symbols.LAMBDA));
        tFPA.add(new PDATransitionFunction(q1, "b", q1, Symbols.LAMBDA, "A"));
        PushdownAutomaton automaton = new PushdownAutomaton(states, q0, finalStates, tFPA);
        Grammar grammar = PDAToGrammar.toGrammar(automaton);
        System.out.println(grammar);

    }

    @Test
    public void testPDA() {
        String variables[] = new String[]{"S", "A", "B"};
        String terminals[] = new String[]{"a", "b"};
        String initialSymbol = "S";
        String rules[] = new String[]{
                "S -> aAB | aB",
                "A -> aAB | aB",
                "B -> b"
        };
        Grammar grammar = new Grammar(variables, terminals, initialSymbol, rules);
        System.out.print(GrammarToPDAExt.toPDAutomatonExt(grammar));

    }

    @Test
    public void testTreeParserGrammar1() {
        String variables[] = new String[]{"S"};
        String terminals[] = new String[]{"a", "b", Grammar.LAMBDA};
        String initialSymbol = "S";
        String rules[] = new String[]{
                "S -> aSb | aSbb | " + Grammar.LAMBDA
        };

        Grammar grammar = new Grammar(variables, terminals, initialSymbol, rules);

        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar, "aabbb");
        treeDerivationParser.parser();
        treeDerivationParser = new TreeDerivationParser(grammar, "abbb");
        treeDerivationParser.parser();
    }

    @Test
    public void testTreeParserGrammar2() {
        String variables[] = new String[]{"S"};
        String terminals[] = new String[]{"a", "b"};
        String initialSymbol = "S";
        String rules[] = new String[]{
                "S -> aS | Sa | a"
        };

        Grammar grammar = new Grammar(variables, terminals, initialSymbol, rules);

        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar, "aa");

        treeDerivationParser.parser();
    }


    @Test
    public void testPermut() {
        String alphabet[] = new String[]{ "a", "b" };
        Permut permut = new Permut(3, new TreeSet<>(Arrays.asList(alphabet)));
        WordEnumerator wordEnumerator = new WordEnumerator();
        wordEnumerator.setAlphabet(new TreeSet<>(Arrays.asList(alphabet)));
        Set<String> setPermut = permut.getWords();
        System.out.println(setPermut.size());
        System.out.println(Arrays.toString(setPermut.toArray()));
    }

    @Test
    public void testTreeParserGrammar3() {
        String variables[] = new String[]{"E"};
        String terminals[] = new String[]{"i", "a", "m", "p", "o"};
        String initialSymbol = "E";
        String rules[] = new String[]{
                "E -> EaE | EmE | pEo | i"
        };
        Grammar grammar = new Grammar(variables, terminals, initialSymbol, rules);

        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar, "iaimi");
        treeDerivationParser.parser();
    }
}
