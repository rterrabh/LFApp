package com.ufla.lfapp.core.machine.tm.var;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.TapeEmptyCharUtils;
import com.ufla.lfapp.core.machine.tm.TMMove;
import com.ufla.lfapp.utils.ResourcesContext;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by carlos on 4/8/17.
 */

public class TMMultiTapeSimulator {

    private static final char ENUM_SEP_WORDS = '#';
    private TuringMachineMultiTape turingMachine;
    private String word;
    private StringBuilder[] tapes;
    private Configuration lastConfiguration;
    private Deque<Configuration> stackConfiguration;
    private Deque<Configuration> stackActualConfiguration;
    private static final int MAX_DEPTH = 10000;


    public Configuration[] getConfigurations() {
        Configuration[] configurations = new Configuration[stackActualConfiguration.size()];
        return stackActualConfiguration.toArray(configurations);
    }


    private String getInitialTapes(int lenght) {
        return TapeEmptyCharUtils.getEmptyString(lenght);
    }

    private StringBuilder[] getEnumTapes() {
        StringBuilder[] enumTapes = new StringBuilder[2];
        String enumTape = getInitialTapes(15);
        enumTapes[0] = new StringBuilder(enumTape);
        enumTapes[1] = new StringBuilder(enumTape);
        return enumTapes;
    }

    public TMMultiTapeSimulator(TuringMachineMultiTape turingMachine) {
        this.turingMachine = turingMachine;
        this.word = "";
        stackConfiguration = new LinkedList<>();
        stackActualConfiguration = new LinkedList<>();
        tapes = getEnumTapes();
        lastConfiguration = new Configuration(0, new int[2], tapes);
    }

    public TMMultiTapeSimulator(TuringMachineMultiTape turingMachine, String word) {
        this.turingMachine = turingMachine;
        this.word = word;
        stackConfiguration = new LinkedList<>();
        stackActualConfiguration = new LinkedList<>();
        int numTapes = turingMachine.getNumTapes();
        tapes = new StringBuilder[numTapes];
        tapes[0] = new StringBuilder()
                .append(TapeEmptyCharUtils.EMPTY_CHAR)
                .append(word)
                .append(TapeEmptyCharUtils.increaseEmptyString());
        String initialTapes = getInitialTapes(tapes[0].length());
        for (int i = 1; i < numTapes; i++) {
            tapes[i] = new StringBuilder(initialTapes);
        }
        lastConfiguration = new Configuration(0, new int[2], getEnumTapes());
    }

    // REFACTOR
    public static String[] stringBuildersToString(StringBuilder[] stringBuilders) {
        int n = stringBuilders.length;
        String[] strings = new String[n];
        for (int i = 0; i < n; i++) {
            strings[i] = stringBuilders[i].toString();
        }
        return strings;
    }

    private static TMMultiTapeTransitionFunction getInitialTransition(State initialState,
                                                                      int tapesLength) {
        String[] symbols = TapeEmptyCharUtils.getArrayEmptyString(tapesLength);
        TMMove[] moves = new TMMove[tapesLength];
        Arrays.fill(moves, TMMove.STATIC);
        return new TMMultiTapeTransitionFunction(initialState,
                initialState, symbols, symbols, moves);
    }




    public class Configuration {

        int depth;
        int[] index;
        String[] tapes;
        TMMultiTapeTransitionFunction transition;

        // CONSTRUCTORS


        public Configuration(int depth, int[] index, StringBuilder[] tapes) {
            this(depth, index, stringBuildersToString(tapes));
        }

        public Configuration(int depth, int[] index, String[] tapes) {
            this(depth, index, tapes,
                    getInitialTransition(turingMachine.getInitialState(),
                            tapes.length));
        }

        public Configuration(int depth, int[] index, StringBuilder[] tapes,
                             TMMultiTapeTransitionFunction transition) {
            this(depth, index, stringBuildersToString(tapes), transition);
        }


        public Configuration(int depth, int[] index, String[] tapes,
                             TMMultiTapeTransitionFunction transition) {
            this.depth = depth;
            this.index = Arrays.copyOf(index, index.length);
            this.tapes = tapes;
            this.transition = transition;
        }

        public boolean isFinalState() {
            return turingMachine.isFinalState(transition.getFutureState());
        }

        public boolean isInitialState() {
            return turingMachine.isInitialState(transition.getFutureState());
        }

        public String getState() {
            return transition.getFutureState().getName();
        }

        public int[] getIndex() {
            return index;
        }

        public String[] getTapes() {
            return tapes;
        }

        protected void setTapes(StringBuilder[] tapes) {
            this.tapes = TMMultiTapeSimulator.stringBuildersToString(tapes);
        }

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

    private void increaseTapesLenght() {
        String increaseEmptyString = TapeEmptyCharUtils.increaseEmptyString();
        for (int i = 0; i < tapes.length; i++) {
            tapes[i].append(increaseEmptyString);
        }
    }

    private void verifyTapesLength(int[] index) {
        boolean increase = false;
        for (int i = 0; i < tapes.length; i++) {
            if (index[i] == tapes[i].length()) {
                increase = true;
                break;
            }
        }
        if (increase) {
            increaseTapesLenght();
        }
    }

    private int[] processConfiguration(int index[]) {
        Configuration configuration = stackActualConfiguration.peekLast();
        TMMove[] tmMoves = configuration.transition.getMoves();
        String[] writeSymbols = configuration.transition.getWriteSymbols();
        for (int i = 0; i < tapes.length; i++) {
            tapes[i].setCharAt(index[i], writeSymbols[i].charAt(0));
            if (tmMoves[i].equals(TMMove.LEFT)) {
                index[i]--;
                configuration.index[i]--;
            } else if (tmMoves[i].equals(TMMove.RIGHT)) {
                index[i]++;
                configuration.index[i]++;
            }
        }
        return index;
    }

    private boolean processConfigurationEnum() {
        Configuration configuration = lastConfiguration;
        int[] index = lastConfiguration.getIndex();
        TMMove[] tmMoves = configuration.transition.getMoves();
        String[] writeSymbols = configuration.transition.getWriteSymbols();
        boolean stop = writeSymbols[0].charAt(0) == ENUM_SEP_WORDS;
        for (int i = 0; i < tapes.length; i++) {
            tapes[i].setCharAt(index[i], writeSymbols[i].charAt(0));
            if (tmMoves[i].equals(TMMove.LEFT)) {
                index[i]--;
            } else if (tmMoves[i].equals(TMMove.RIGHT)) {
                index[i]++;
            }
        }
        lastConfiguration.setTapes(tapes);
        return stop;
    }

    private int[] backtrackConfiguration(int index[]) {
        Configuration lastConfiguration = stackActualConfiguration.removeLast();
        TMMove[] tmMoves = lastConfiguration.transition.getMoves();
        String[] readSymbols = lastConfiguration.transition.getReadSymbols();
        for (int i = 0; i < tapes.length; i++) {
            if (tmMoves[i].equals(TMMove.LEFT)) {
                index[i]++;
            } else if (tmMoves[i].equals(TMMove.RIGHT)) {
                index[i]--;
            }
            tapes[i].setCharAt(index[i], readSymbols[i].charAt(0));
        }
        return index;
    }

    private String[] actualSymbols(int[] index) {
        String[] symbols = new String[tapes.length];
        for (int i = 0; i < tapes.length; i++) {
            symbols[i] = Character.toString(tapes[i].charAt(index[i]));
        }
        return symbols;
    }

    public int contConfiguration;

    public Configuration nextWord() {
        Configuration configuration;
        boolean stop;
        contConfiguration = 0;
        do {
            stop = processConfigurationEnum();
            configuration = lastConfiguration;
            verifyTapesLength(configuration.getIndex());
//            System.out.println("----------------");
//            System.out.println(configuration.getState());
//            System.out.println(Arrays.toString(configuration.getIndex()));
//            for (String tape : configuration.getTapes()) {
//                System.out.println(tape);
//            }

            Set<TMMultiTapeTransitionFunction> transitions = turingMachine
                    .getTransitions(lastConfiguration.transition.getFutureState(),
                            actualSymbols(configuration.getIndex()));
            for (TMMultiTapeTransitionFunction t : transitions) {
                lastConfiguration = new Configuration(configuration.depth + 1,
                        configuration.getIndex(), tapes, t);
            }
            contConfiguration++;
        } while (!stop && contConfiguration < 1000);
        return configuration;
    }

    public boolean process() throws Exception {
        verifyWord();
        int depth = 0;
        int[] index = new int[tapes.length];
        stackConfiguration.push(new Configuration(depth, index, tapes));
        while (!stackConfiguration.isEmpty()) {
            Configuration actualConfiguration = stackConfiguration.pop();
            // Backtracking
            while (actualConfiguration.depth < depth) {
                index = backtrackConfiguration(index);
                depth--;
            }
            // Run configuration
//            actualConfiguration.setTapes(tapes);
            stackActualConfiguration.addLast(actualConfiguration);
            index = processConfiguration(index);
            depth++;
            verifyTapesLength(index);
            actualConfiguration.setTapes(tapes);
            if (actualConfiguration.isFinalState()) {
                return true;
            }
            if (depth < MAX_DEPTH) {
                Set<TMMultiTapeTransitionFunction> transitions = turingMachine
                        .getTransitions(actualConfiguration.transition.getFutureState(),
                                actualSymbols(index));
                for (TMMultiTapeTransitionFunction t : transitions) {
                    stackConfiguration.push(new Configuration(depth, index, tapes, t));
                }
            }
        }
        return false;
    }

}
