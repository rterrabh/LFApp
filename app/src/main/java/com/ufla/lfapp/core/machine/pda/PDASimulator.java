package com.ufla.lfapp.core.machine.pda;

import android.text.Spannable;
import android.text.SpannableStringBuilder;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.utils.ResourcesContext;
import com.ufla.lfapp.utils.Symbols;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by carlos on 4/2/17.
 */

public class PDASimulator {

    private static final int MAX_DEPTH = 1000;
    private PushdownAutomaton pushdownAutomaton;
    private String word;
    private Deque<String> stacks;
    private Deque<Configuration> stackConfiguration;
    private Deque<Configuration> stackActualConfiguration;
    private Deque<String> stack;
    /**
     * Configurações atuais da simulação.
     */
    private PDAConfiguration[] actualConfigurations;

    public PDASimulator(PushdownAutomaton pushdownAutomaton, String word) {
        this.pushdownAutomaton = pushdownAutomaton;
        this.word = word;
        stackConfiguration = new LinkedList<>();
        stackActualConfiguration = new LinkedList<>();
        stacks = new LinkedList<>();
        stack = new LinkedList<>();
    }

    public String[] getStacks() {
        String[] stacksArray = new String[stacks.size()];
        return stacks.toArray(stacksArray);
    }

    public Configuration[] getsConfigurations() {
        Configuration[] configurations = new Configuration[stackActualConfiguration.size()];
        return stackActualConfiguration.toArray(configurations);
    }

    private String stackToString() {
        int n = stack.size();
        String[] symbols = new String[n];
        symbols = stack.toArray(symbols);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(symbols[i]);
        }
        return sb.toString();
    }

    private boolean validityFromAlphabet() {
        return true;
    }

    private List<String> symbolsExternFromAlphabet() {
        return null;
    }

    private void verifyWord() throws Exception {
        if (!validityFromAlphabet()) {
            List<String> symbolsExternFromAlphabet = symbolsExternFromAlphabet();
            StringBuilder messageException = new StringBuilder();
            messageException
                    .append(ResourcesContext.getString(R.string.exception_symbol_out_alphabet))
                    .append(symbolsExternFromAlphabet.get(0));
            for (int i = 1; i < symbolsExternFromAlphabet.size(); i++) {
                messageException.append(", ")
                        .append(symbolsExternFromAlphabet.get(i));
            }
            throw new Exception(messageException.toString());
        }
    }

    public boolean process() throws Exception {
        verifyWord();
        stackConfiguration.push(new Configuration(0, pushdownAutomaton.getInitialState()));
        int depth = 0;
        int index = 0;
        while (!stackConfiguration.isEmpty()) {
            Configuration actualConfiguration = stackConfiguration.pop();
            // Backtracking
            while (index > actualConfiguration.index
                    && !stackActualConfiguration.isEmpty()) {
                Configuration lastConfiguration = stackActualConfiguration.removeLast();
                if (!lastConfiguration.stacking.equals(Symbols.LAMBDA)) {
                    stack.pop();
                }
                if (!lastConfiguration.pops.equals(Symbols.LAMBDA)) {
                    stack.push(lastConfiguration.pops);
                }
                if (!lastConfiguration.symbol.equals(Symbols.LAMBDA)) {
                    index--;
                }
                stacks.removeLast();
            }
            // Run configuration

            stackActualConfiguration.addLast(actualConfiguration);
            if (!actualConfiguration.pops.equals(Symbols.LAMBDA)) {
                stack.pop();
            }
            if (!actualConfiguration.stacking.equals(Symbols.LAMBDA)) {
                stack.push(actualConfiguration.stacking);
            }
            if (!actualConfiguration.symbol.equals(Symbols.LAMBDA)) {
                index++;
            }
            stacks.addLast(stackToString());
            StringBuilder sb = new StringBuilder();
            sb.append(word.substring(0, actualConfiguration.index));
            sb.append(actualConfiguration.state);
            sb.append(word.substring(actualConfiguration.index));
            depth++;
            if (index == word.length() &&
                    pushdownAutomaton.isFinalState(actualConfiguration.state)
                    && stack.isEmpty()) {
                return true;
            }
            boolean generateProcess = false;
            if (actualConfiguration.index < word.length()
                    && stackActualConfiguration.size() < MAX_DEPTH) {
                String topStack = (stack.isEmpty()) ? Symbols.LAMBDA : stack.peek();
                Set<PDATransitionFunction> transitions = pushdownAutomaton
                        .getTransitions(actualConfiguration.state,
                                Symbols.LAMBDA, topStack);
                if (!transitions.isEmpty()) {
                    generateProcess = true;
                }
                for (PDATransitionFunction t : transitions) {
                    Configuration configuration = new Configuration(actualConfiguration.index,
                            t.getFutureState(), t.getStacking(), t.getPops(), t.getSymbol());
                    stackConfiguration.push(configuration);
                }
                transitions = pushdownAutomaton
                        .getTransitions(actualConfiguration.state,
                                Character.toString(word.charAt(actualConfiguration.index)),
                                topStack);
                if (!transitions.isEmpty()) {
                    generateProcess = true;
                }
                for (PDATransitionFunction t : transitions) {
                    Configuration configuration = new Configuration(actualConfiguration.index + 1,
                            t.getFutureState(), t.getStacking(), t.getPops(), t.getSymbol());
                    stackConfiguration.push(configuration);
                }
            } else if (actualConfiguration.index == word.length()
                    && stackActualConfiguration.size() < MAX_DEPTH) {
                String topStack = (stack.isEmpty()) ? Symbols.LAMBDA : stack.peek();
                Set<PDATransitionFunction> transitions = pushdownAutomaton
                        .getTransitions(actualConfiguration.state,
                                Symbols.LAMBDA, topStack);
                if (!transitions.isEmpty()) {
                    generateProcess = true;
                }
                for (PDATransitionFunction t : transitions) {
                    Configuration configuration = new Configuration(actualConfiguration.index,
                            t.getFutureState(), t.getStacking(), t.getPops(), t.getSymbol());
                    stackConfiguration.push(configuration);
                }
            }
        }
        return false;
    }

    public class Configuration {
        int index;
        State state;
        String stacking;
        String pops;
        String symbol;

        public Configuration(int index, State state, String stacking, String pops, String symbol) {
            this.index = index;
            this.state = state;
            this.stacking = stacking;
            this.pops = pops;
            this.symbol = symbol;
        }

        public Configuration(int index, State state) {
            this(index, state, Symbols.LAMBDA, Symbols.LAMBDA, Symbols.LAMBDA);
        }

        @Override
        public String toString() {
            return new StringBuilder("Configuration{")
                    .append("index=").append(index)
                    .append(", state=").append(state)
                    .append(", stacking='").append(stacking).append('\'')
                    .append(", pops='").append(pops).append('\'')
                    .append(", symbol='").append(symbol).append('\'')
                    .append('}')
                    .toString();
        }

        public int getIndex() {
            return index;
        }

        public String getState() {
            return state.getName();
        }

        public boolean isFinalState() {
            return pushdownAutomaton.isFinalState(state);
        }

        public boolean isInitialState() {
            return pushdownAutomaton.isInitialState(state);
        }
    }
}
