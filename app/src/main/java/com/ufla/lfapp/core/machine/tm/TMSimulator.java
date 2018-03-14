package com.ufla.lfapp.core.machine.tm;

import android.text.Spannable;
import android.text.SpannableStringBuilder;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.TapeEmptyCharUtils;
import com.ufla.lfapp.utils.ResourcesContext;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by carlos on 4/2/17.
 */

public class TMSimulator {

    private TuringMachine turingMachine;
    private String word;
    private StringBuilder tapeALL;
    private StringBuilder tape;
    private Deque<String> tapes;
    private Deque<Configuration> stackConfiguration;
    private Deque<Configuration> stackActualConfiguration;
    private static final int MAX_DEPTH = 10000;

    public TMSimulator(TuringMachine turingMachine, String word) {
        this.turingMachine = turingMachine;
        this.word = word;
        stackConfiguration = new LinkedList<>();
        tapes = new LinkedList<>();
        stackActualConfiguration = new LinkedList<>();
        tape = new StringBuilder()
                .append(TapeEmptyCharUtils.EMPTY_CHAR)
                .append(word)
                .append(TapeEmptyCharUtils.increaseEmptyString());
        tapeALL = new StringBuilder()
                .append('<')
                .append(word)
                .append('>');
    }

    public String[] getTapes() {
        String[] tapes = new String[this.tapes.size()];
        return this.tapes.toArray(tapes);
    }

    public Configuration[] getConfigurations() {
        Configuration[] configurations = new Configuration[stackActualConfiguration.size()];
        return stackActualConfiguration.toArray(configurations);
    }

    public class Configuration {
        int depth;
        int index;
        State state;
        String read;
        String write;
        TMMove move;

        public Configuration(int depth, int index, State state, String read, String write, TMMove move) {
            this.depth = depth;
            this.index = index;
            this.state = state;
            this.read = read;
            this.write = write;
            this.move = move;
        }

        public boolean isFinalState() {
            return turingMachine.isFinalState(state);
        }

        public boolean isInitialState() {
            return turingMachine.isInitialState(state);
        }

        public String getState() {
            return state.getName();
        }

        public int getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return "Configuration{" +
                    "depth=" + depth +
                    ", index=" + index +
                    ", state=" + state +
                    ", read='" + read + '\'' +
                    ", write='" + write + '\'' +
                    ", move=" + move +
                    '}' + '\n';
        }

        public Configuration(int index, State state) {
            this(0, index, state, TapeEmptyCharUtils.EMPTY_CHAR_STR,
                    TapeEmptyCharUtils.EMPTY_CHAR_STR, TMMove.STATIC);
        }


    }

    public Configuration getALLInitialConf(int index, State state) {
        return new Configuration(0, index, state, "<", "<", TMMove.STATIC);
    }

    private boolean validityFromAlphabet() {
        return true;
    }

    private List<String> symbolsExternFromAlphabet() {
        return null;
    }

    public void verifyWord() throws Exception {
        if (!validityFromAlphabet()) {
            List<String> symbolsExternFromAlphabet = symbolsExternFromAlphabet();
            StringBuilder messageException = new StringBuilder();
            messageException.append(ResourcesContext.getString(R.string.exception_symbol_out_alphabet))
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
        stackConfiguration.push(new Configuration(0, turingMachine.getInitialState()));
        int depth = 0;
        int index = 0;
        while (!stackConfiguration.isEmpty()) {
            Configuration actualConfiguration = stackConfiguration.pop();
            // Backtracking
            while (actualConfiguration.depth < depth) {
                Configuration lastConfiguration = stackActualConfiguration.removeLast();
                if (lastConfiguration.move.equals(TMMove.LEFT)) {
                    index++;
                } else if (lastConfiguration.move.equals(TMMove.RIGHT)) {
                    index--;
                }
                tape.setCharAt(index, lastConfiguration.read.charAt(0));
                tapes.removeLast();
                depth--;
            }
            // Run configuration
            stackActualConfiguration.addLast(actualConfiguration);
            tape.setCharAt(index, actualConfiguration.write.charAt(0));
            if (actualConfiguration.move.equals(TMMove.LEFT)) {
                actualConfiguration.index--;
                index--;
            } else if (actualConfiguration.move.equals(TMMove.RIGHT)) {
                actualConfiguration.index++;
                index++;
            }
            if (index == tape.length()) {
                tape.append(TapeEmptyCharUtils.increaseEmptyString());
            }
            if (index < 0) {
                return false;
            }
            tapes.addLast(tape.toString());
            StringBuilder sb = new StringBuilder();
            sb.append(tape.substring(0, index));
            sb.append(actualConfiguration.state);
            sb.append(tape.substring(index));
            depth++;
            if (turingMachine.isFinalState(actualConfiguration.state)) {
                return true;
            }
            if (depth < MAX_DEPTH) {
                Set<TMTransitionFunction> transitions = turingMachine
                        .getTransitions(actualConfiguration.state,
                                Character.toString(tape.charAt(index)));

                for (TMTransitionFunction t : transitions) {
                    stackConfiguration.push(new Configuration(depth, index,
                            t.getFutureState(), t.getSymbol(), t.getWriteSymbol(),
                            t.getMove()));
                }
            }
        }
        return false;
    }

    public boolean processALL() throws Exception {
        verifyWord();
        stackConfiguration.push(getALLInitialConf(0, turingMachine.getInitialState()));
        int depth = 0;
        int index = 0;
        while (!stackConfiguration.isEmpty()) {
            Configuration actualConfiguration = stackConfiguration.pop();
            // Backtracking
            while (actualConfiguration.depth < depth) {
                Configuration lastConfiguration = stackActualConfiguration.removeLast();
                if (lastConfiguration.move.equals(TMMove.LEFT)) {
                    index++;
                } else if (lastConfiguration.move.equals(TMMove.RIGHT)) {
                    index--;
                }
                tapeALL.setCharAt(index, lastConfiguration.read.charAt(0));
                tapes.removeLast();
                depth--;
            }
            // Run configuration
            stackActualConfiguration.addLast(actualConfiguration);
            tapeALL.setCharAt(index, actualConfiguration.write.charAt(0));
            if (actualConfiguration.move.equals(TMMove.LEFT)) {
                actualConfiguration.index--;
                index--;
            } else if (actualConfiguration.move.equals(TMMove.RIGHT)) {
                actualConfiguration.index++;
                index++;
            }
            if (index < 0 || index >= tapeALL.length()) {
                return false;
            }
            tapes.addLast(tapeALL.toString());
            depth++;
            if (turingMachine.isFinalState(actualConfiguration.state)) {
                return true;
            }
            if (depth < MAX_DEPTH) {
                Set<TMTransitionFunction> transitions = turingMachine
                        .getTransitions(actualConfiguration.state,
                                Character.toString(tapeALL.charAt(index)));

                for (TMTransitionFunction t : transitions) {
                    stackConfiguration.push(new Configuration(depth, index,
                            t.getFutureState(), t.getSymbol(), t.getWriteSymbol(),
                            t.getMove()));
                }
            }
        }
        return false;
    }
}
