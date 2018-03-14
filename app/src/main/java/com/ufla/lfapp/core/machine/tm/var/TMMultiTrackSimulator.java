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

public class TMMultiTrackSimulator {


    private TuringMachineMultiTrack turingMachine;
    private String word;
    private StringBuilder[] tapes;
    private Deque<Configuration> stackConfiguration;
    private Deque<Configuration> stackActualConfiguration;
    private static final int MAX_DEPTH = 10000;


    public Configuration[] getConfigurations() {
        Configuration[] configurations = new Configuration[stackActualConfiguration.size()];
        return stackActualConfiguration.toArray(configurations);
    }

    private String getInitialTapes(int lenght) {
        char[] initialTapes = new char[lenght];
        Arrays.fill(initialTapes, TapeEmptyCharUtils.EMPTY_CHAR);
        return new String(initialTapes);
    }

    public TMMultiTrackSimulator(TuringMachineMultiTrack turingMachine, String word) {
        this.turingMachine = turingMachine;
        this.word = word;
        stackConfiguration = new LinkedList<>();
        stackActualConfiguration = new LinkedList<>();
        int numTracks = turingMachine.getNumTracks();
        tapes = new StringBuilder[numTracks];
        tapes[0] = new StringBuilder()
                .append(TapeEmptyCharUtils.EMPTY_CHAR)
                .append(word)
                .append(TapeEmptyCharUtils.increaseEmptyString());
        String initialTapes = getInitialTapes(tapes[0].length());
        for (int i = 1; i < numTracks; i++) {
            tapes[i] = new StringBuilder(initialTapes);
        }
    }


    private static TMMultiTrackTransitionFunction getInitialTransition(State initialState,
                                                                       int tapesLength) {
        String[] symbols = new String[tapesLength];
        Arrays.fill(symbols, TapeEmptyCharUtils.EMPTY_CHAR_STR);
        return new TMMultiTrackTransitionFunction(initialState,
                initialState, symbols, symbols, TMMove.STATIC);
    }


    public class Configuration {

        int depth;
        int index;
        String[] tapes;
        TMMultiTrackTransitionFunction transition;

        // CONSTRUCTORS

        public Configuration(int depth, int index, StringBuilder[] tapes) {
            this(depth, index, TMMultiTapeSimulator.stringBuildersToString(tapes));
        }

        public Configuration(int depth, int index, String[] tapes) {
            this(depth, index, tapes,
                    getInitialTransition(turingMachine.getInitialState(),
                            tapes.length));
        }

        public Configuration(int depth, int index, StringBuilder[] tapes,
                             TMMultiTrackTransitionFunction transition) {
            this(depth, index,
                    TMMultiTapeSimulator.stringBuildersToString(tapes),
                    transition);
        }


        public Configuration(int depth, int index, String[] tapes,
                             TMMultiTrackTransitionFunction transition) {
            this.depth = depth;
            this.index = index;
            this.tapes = tapes;
            this.transition = transition;
        }

        protected void setTapes(StringBuilder[] tapes) {
            this.tapes = TMMultiTapeSimulator.stringBuildersToString(tapes);
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

        public int getIndex() {
            return index;
        }

        public String[] getTapes() {
            return tapes;
        }

//        public String toString() {
//            return index + " - " + Arrays.deepToString(tapes);
//        }

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

    private void verifyTapesLength(int index) {
        if (index == tapes[0].length()) {
            increaseTapesLenght();
        }
    }

    private int processConfiguration(int index) {
        Configuration configuration = stackActualConfiguration.peekLast();
        String[] writeSymbols = configuration.transition.getWriteSymbols();
        for (int i = 0; i < tapes.length; i++) {
            tapes[i].setCharAt(index, writeSymbols[i].charAt(0));
        }
        TMMove move = configuration.transition.getMove();
        if (move.equals(TMMove.LEFT)) {
            index--;
            configuration.index--;
        } else if (move.equals(TMMove.RIGHT)) {
            index++;
            configuration.index++;
        }
        return index;
    }

    private int backtrackConfiguration(int index) {
        Configuration lastConfiguration = stackActualConfiguration.removeLast();
        String[] readSymbols = lastConfiguration.transition.getReadSymbols();
        TMMove move = lastConfiguration.transition.getMove();
        if (move.equals(TMMove.LEFT)) {
            index++;
        } else if (move.equals(TMMove.RIGHT)) {
            index--;
        }
        for (int i = 0; i < tapes.length; i++) {
            tapes[i].setCharAt(index, readSymbols[i].charAt(0));
        }
        return index;
    }

    private String[] actualSymbols(int index) {
        String[] symbols = new String[tapes.length];
        for (int i = 0; i < tapes.length; i++) {
            symbols[i] = Character.toString(tapes[i].charAt(index));
        }
        return symbols;
    }

    public boolean process() throws Exception {
        verifyWord();
        int depth = 0;
        int index = 0;
        stackConfiguration.push(new Configuration(depth, index, tapes));
        while (!stackConfiguration.isEmpty()) {
            Configuration actualConfiguration = stackConfiguration.pop();
            // Backtracking
            while (actualConfiguration.depth < depth) {
                index = backtrackConfiguration(index);
                depth--;
            }
            // Run configuration
            stackActualConfiguration.addLast(actualConfiguration);
            index = processConfiguration(index);
            depth++;
            verifyTapesLength(index);
            actualConfiguration.setTapes(tapes);
            if (actualConfiguration.isFinalState()) {
                return true;
            }
            if (depth < MAX_DEPTH) {
                Set<TMMultiTrackTransitionFunction> transitions = turingMachine
                        .getTransitions(actualConfiguration.transition.getFutureState(),
                                actualSymbols(index));
                for (TMMultiTrackTransitionFunction t : transitions) {
                    stackConfiguration.push(new Configuration(depth, index, tapes, t));
                }
            }
        }
        return false;
    }

}
