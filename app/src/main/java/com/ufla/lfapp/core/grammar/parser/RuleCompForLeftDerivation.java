package com.ufla.lfapp.core.grammar.parser;

import com.ufla.lfapp.core.grammar.Rule;

import java.util.Comparator;

/**
 * Created by carlos on 2/21/17.
 */

public class RuleCompForLeftDerivation implements Comparator<Rule> {

    @Override
    public int compare(Rule rule1, Rule rule2) {
        String leftSide = rule1.getLeftSide();
        String rightSide1 = rule1.getRightSide();
        String rightSide2 = rule2.getRightSide();
        if (Character.isLowerCase(rightSide1.charAt(0))) {
            if (Character.isLowerCase(rightSide2.charAt(0))) {
                if (rightSide2.length() == rightSide1.length()) {
                    return rightSide1.compareTo(rightSide2);
                }
                return rightSide1.length() - rightSide2.length();
            }
            return -1;
        }
        if (Character.isLowerCase(rightSide2.charAt(0))) {
            return 1;
        }
        if (rightSide1.charAt(0) == leftSide.charAt(0)) {
            if (rightSide2.charAt(0) == leftSide.charAt(0)) {
                if (rightSide2.length() == rightSide1.length()) {
                    return rightSide1.compareTo(rightSide2);
                }
                return rightSide1.length() - rightSide2.length();
            }
            return 1;
        }
        return -1;
    }
}
