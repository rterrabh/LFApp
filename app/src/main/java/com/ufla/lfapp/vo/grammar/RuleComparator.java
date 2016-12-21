package com.ufla.lfapp.vo.grammar;

import java.util.Comparator;

/**
 * Created by carlos on 9/13/16.
 */
public class RuleComparator implements Comparator<Rule> {

    private String initialSymbol;

    public RuleComparator(String initialSymbol) {
        this.initialSymbol = initialSymbol;
    }

    @Override
    public int compare(Rule rule, Rule t1) {
        if (initialSymbol == null) {
            return rule.compareTo(t1);
        }
        if (rule.getLeftSide().equals(initialSymbol)) {
            if (t1.getLeftSide().equals(initialSymbol)) {
                return rule.getRightSide().compareTo(t1.getRightSide());
            } else {
                return -1;
            }
        }
        if (t1.getLeftSide().equals(initialSymbol)) {
            return 1;
        }
        return rule.compareTo(t1);
    }
}
