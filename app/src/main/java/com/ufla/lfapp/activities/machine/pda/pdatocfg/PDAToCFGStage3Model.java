package com.ufla.lfapp.activities.machine.pda.pdatocfg;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.Rule;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.pda.PDAExtTransitionFunction;
import com.ufla.lfapp.core.machine.pda.PushdownAutomaton;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.utils.Symbols;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 3/15/17.
 */

public class PDAToCFGStage3Model extends PDAToCFGStage2Model {

    private static final String QK_COLOR = "#14A4E6";
    private static final String QN_COLOR = "#00AAA0";

    class TransitionToRule {

        Integer numRule;
        PDAExtTransitionFunction transition;
        List<Rule> rules;

        public TransitionToRule(PDAExtTransitionFunction transition) {
            this.transition = transition;
            numRule = -1;
            rules = new LinkedList<>();
        }



    }

    List<TransitionToRule> transitionToRules;

    public PDAToCFGStage3Model(PushdownAutomaton pda) {
        super(pda);
        transitionToRules = new LinkedList<>();
    }

    public void defineNewRules() {
        int newRulesCont = 0;
        int transCont = 0;
        Set<String> variables = new TreeSet<>();
        Set<String> terminals = pdaExt.getAlphabet();
        String initialSymbol = Symbols.INITIAL_SYMBOL_GRAMMAR;
        Set<Rule> rulesAux = new LinkedHashSet<>();
        rules = new LinkedHashSet<>();
        SortedSet<State> finalStates = pdaExt.getFinalStates();
        variables.add(Symbols.INITIAL_SYMBOL_GRAMMAR);
        for (State state : finalStates) {
            String rightSide = "<q0," + Symbols.LAMBDA + "," +
                    state.getName() + ">";
            variables.add(rightSide);
            rulesAux.add(new Rule(Symbols.INITIAL_SYMBOL_GRAMMAR, rightSide));
        }

        SortedSet<State> states = pdaExt.getStates();
        Set<PDAExtTransitionFunction> tFPDAExts = pdaExt.getTransitionFunctions();
        for (PDAExtTransitionFunction tFPDAExt : tFPDAExts) {
            TransitionToRule transitionToRule = new TransitionToRule(tFPDAExt);
            String qi = tFPDAExt.getCurrentState().getName();
            String qj = tFPDAExt.getFutureState().getName();
            String pops = tFPDAExt.getPops();
            String symbol = tFPDAExt.getSymbol();
            if (tFPDAExt.getStacking().size() == 1) {
                transitionToRule.numRule = 2;
                String stacking = tFPDAExt.getStacking().get(0);
                for (State state : states) {
                    String qk = state.getName();
                    String leftSide = "<" + qi + ", " + pops + ", " + qk + ">";
                    String rightSide = "<" + qj + ", " + stacking + ", " + qk + ">";
                    variables.add(leftSide);
                    variables.add(rightSide);
                    rightSide = symbol + rightSide;
                    Rule rule = new Rule(leftSide, rightSide);
                    transitionToRule.rules.add(rule);
                    rulesAux.add(rule);
                    rules.add(rule);
                }
            } else {
                transitionToRule.numRule = 3;
                String stack0 = tFPDAExt.getStacking().get(0);
                String stack1 = tFPDAExt.getStacking().get(1);
                for (State stateK : states) {
                    String qk = stateK.getName();
                    for (State stateN : states) {
                        String qn = stateN.getName();
                        String leftSide = "<" + qi + ", " + pops + ", " + qk + ">";
                        String rightSideAux = "<" + qj + ", " + stack0 + ", " + qn + ">";
                        variables.add(rightSideAux);
                        String rightSide = "<" + qn + ", " + stack1 + ", " + qk + ">";
                        variables.add(rightSide);
                        rightSide = symbol + rightSideAux + rightSide;
                        Rule rule = new Rule(leftSide, rightSide);
                        transitionToRule.rules.add(rule);
                        rulesAux.add(rule);
                        rules.add(rule);
                        variables.add(leftSide);
                    }
                }
            }
            if (transitionToRule.numRule != -1) {
                newRulesCont += transitionToRule.rules.size();
                transitionToRules.add(transitionToRule);
            }
        }
        transCont = transitionToRules.size();
        //Log.d("D", newRulesCont + ", " + transCont);
        rulesAux.addAll(rules);
        grammar = new Grammar(variables, terminals, initialSymbol, rulesAux);
        it = transitionToRules.iterator();
    }


    Iterator<TransitionToRule> it;
    public String nextNewTransition() {
        StringBuilder sb = new StringBuilder();
        if (it.hasNext()) {
            TransitionToRule t = it.next();
            if (t.numRule == 3) {
                sb.append(PDAToCFGStage3Activity.SPAN_RULE_3)
                        .append(rule3(t));
            } else if (t.numRule == 2) {
                sb.append(PDAToCFGStage3Activity.SPAN_RULE_2)
                        .append(rule2(t));
            }
            return sb.toString();
        }
        return null;
    }

    private String genericRule(TransitionToRule t) {
        if (t.numRule == 2) {
            return new StringBuilder()
                    .append("\t<b>•</b> <")
                    .append(t.transition.getCurrentStateLabelWithSpan())
                    .append(", ")
                    .append(t.transition.getPops())
                    .append(", q<sub><small>k</small></sub>> → ")
                    .append(t.transition.getSymbol())
                    .append("<")
                    .append(t.transition.getFutureStateLabelWithSpan())
                    .append(", ")
                    .append(t.transition.stackingToString())
                    .append(", q<sub><small>k</small></sub>>\n")
                    .toString();
        }
        if (t.numRule == 3) {
            return new StringBuilder()
                    .append("\t<b>•</b> <")
                    .append(t.transition.getCurrentStateLabelWithSpan())
                    .append(", ")
                    .append(t.transition.getPops())
                    .append(", q<sub><small>k</small></sub>> → ")
                    .append(t.transition.getSymbol())
                    .append("<")
                    .append(t.transition.getFutureStateLabelWithSpan())
                    .append(", ")
                    .append(t.transition.getStacking().get(1))
                    .append(", q<sub><small>n</small></sub>>")
                    .append("<q<sub><small>n</small></sub>, ")
                    .append(t.transition.getStacking().get(0))
                    .append(", q<sub><small>k</small></sub>>\n")
                    .toString();
        }
        return null;
    }

    private static String getStateSpannableWithColor(String rule, int index, int indexF,
                                                     String color) {
        return new StringBuilder()
                .append("<cb:")
                .append(color)
                .append('>')
                .append(getStateSpannable(rule, index, indexF))
                .append("</cb>")
                .toString();
    }

    private static String getStateSpannable(String rule, int index, int indexF) {
        if (rule.charAt(index) == 'q') {
            return new StringBuilder()
                    .append('q')
                    .append("<sub><small>")
                    .append(rule.substring(index + 1, indexF))
                    .append("</small></sub>")
                    .toString();
        }
        return rule.substring(index, indexF);
    }

    private static String rule2Spannable(String rule) {
        StringBuilder sb = new StringBuilder("\t\t<b>•</b> <");
        int index = 1;
        int indexF = rule.indexOf(',', index);
        sb.append(getStateSpannable(rule, index, indexF))
            .append(rule.substring(indexF, indexF + 2));
        index = indexF + 2;
        indexF = rule.indexOf(',', index);
        sb.append(rule.substring(index, indexF + 2));
        index = indexF + 2;
        indexF = rule.indexOf('>', index);
        sb.append(getStateSpannableWithColor(rule, index, indexF, QK_COLOR));
        index = indexF;
        indexF = rule.indexOf('<', index);
        sb.append(rule.substring(index, indexF + 1));
        index = indexF + 1;
        indexF = rule.indexOf(',', index);
        sb.append(getStateSpannable(rule, index, indexF))
                .append(rule.substring(indexF, indexF + 2));
        index = indexF + 2;
        indexF = rule.indexOf(',', index);
        sb.append(rule.substring(index, indexF + 2));
        index = indexF + 2;
        indexF = rule.indexOf('>', index);

        return sb.append(getStateSpannableWithColor(rule, index, indexF, QK_COLOR))
                .append(rule.substring(indexF, rule.length()))
                .append("\t -q<sub><small>k</small></sub> = ")
                .append(getStateSpannableWithColor(rule, index, indexF, QK_COLOR))
                .append('\n')
                .toString();
    }

    private static String rule3Spannable(String rule) {
        StringBuilder sb = new StringBuilder("\t\t<b>•</b> <");
        int index = 1;
        int indexF = rule.indexOf(',', index);
        sb.append(getStateSpannable(rule, index, indexF))
                .append(rule.substring(indexF, indexF + 2));
        index = indexF + 2;
        indexF = rule.indexOf(',', index) + 2;
        sb.append(rule.substring(index, indexF));
        index = indexF;
        indexF = rule.indexOf('>', index);
        sb.append(getStateSpannableWithColor(rule, index, indexF, QK_COLOR));
        index = indexF;
        indexF = rule.indexOf('<', index) + 1;
        sb.append(rule.substring(index, indexF));
        index = indexF;
        indexF = rule.indexOf(',', index);
        sb.append(getStateSpannable(rule, index, indexF))
                .append(rule.substring(indexF, indexF + 2));
        index = indexF + 2;
        indexF = rule.indexOf(',', index) + 2;
        sb.append(rule.substring(index, indexF));
        index = indexF;
        indexF = rule.indexOf('>', index);
        sb.append(getStateSpannableWithColor(rule, index, indexF, QN_COLOR));
        String qn = rule.substring(index, indexF);
        index = indexF;
        indexF = rule.indexOf('<', index) + 1;
        sb.append(rule.substring(index, indexF));
        index = indexF;
        indexF = rule.indexOf(',', index);
        sb.append(getStateSpannableWithColor(rule, index, indexF, QN_COLOR))
                .append(rule.substring(indexF, indexF + 2));
        index = indexF + 2;
        indexF = rule.indexOf(',', index) + 2;
        sb.append(rule.substring(index, indexF));
        index = indexF;
        indexF = rule.indexOf('>', index);
        return sb.append(getStateSpannableWithColor(rule, index, indexF, QK_COLOR))
                .append(rule.substring(indexF, rule.length()))
                .append("\t -q<sub><small>k</small></sub> = ")
                .append(getStateSpannableWithColor(rule, index, indexF, QK_COLOR))
                .append(", -q<sub><small>n</small></sub> = ")
                .append(getStateSpannableWithColor(qn, 0, qn.length(), QN_COLOR))
                .append('\n')
                .toString();
    }

    private String rule3(TransitionToRule t) {
        String[] parameters = ResourcesContext
                .getString(R.string.pda_to_cfg_stage3_parameters).split("#");
        PDAExtTransitionFunction tf = t.transition;
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t<b>•</b> ")
                .append(parameters[0])
                .append(" [")
                .append(tf.getFutureStateLabelWithSpan())
                .append(", ")
                .append(tf.stackingToString())
                .append("] ∈ <i>δ</i>(")
                .append(tf.getCurrentState())
                .append(", ")
                .append(tf.getSymbol())
                .append(", ")
                .append(tf.getPops())
                .append("), ")
                .append(parameters[1])
                .append("\n")
                .append(genericRule(t))
                .append("\t<b>•</b> ")
                .append(parameters[2])
                .append(":\n");
        List<Rule> newRules = t.rules;
        for (Rule rule : newRules) {
            sb.append(rule3Spannable(rule.toString()));
        }
        return sb.toString();
    }

    private String rule2(TransitionToRule t) {
        String[] parameters = ResourcesContext
                .getString(R.string.pda_to_cfg_stage3_parameters).split("#");
        PDAExtTransitionFunction tf = t.transition;
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t<b>•</b> ")
                .append(parameters[0])
                .append(" [")
                .append(tf.getFutureStateLabelWithSpan())
                .append(", ")
                .append(tf.stackingToString())
                .append("] ∈ <i>δ</i>(")
                .append(tf.getCurrentStateLabelWithSpan())
                .append(", ")
                .append(tf.getSymbol())
                .append(", ")
                .append(tf.getPops())
                .append("), ")
                .append(parameters[1])
                .append("\n")
                .append(genericRule(t))
                .append("\t• ")
                .append(parameters[2])
                .append(":\n");
        List<Rule> newRules = t.rules;
        for (Rule rule : newRules) {
            sb.append(rule2Spannable(rule.toString()));
        }
        return sb.toString();
    }
}
