package com.ufla.lfapp.core.machine.fsa;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.MachineBuilder;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.TransitionAtt;
import com.ufla.lfapp.utils.ResourcesContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Responsável por construir um autômato.
 *
 * Created by carlos on 12/11/16.
 */

public class FiniteStateAutomatonBuilder extends MachineBuilder<FiniteStateAutomatonBuilder> {

    protected SortedSet<FSATransitionFunction> FSATransitionFunctions;

    /**
     * Constrói um autômato vazio.
     */
    public FiniteStateAutomatonBuilder() {
        super();
        FSATransitionFunctions = new TreeSet<>();
    }

    /**
     * Constrói um autômato com os atributos do autômato passada por parâmetro.
     *
     * @param finiteStateAutomaton autômato usada para construir o autômato desse construtor de autômato
     */
    public FiniteStateAutomatonBuilder(FiniteStateAutomaton finiteStateAutomaton) {
        super(finiteStateAutomaton);
        FSATransitionFunctions = new TreeSet<>(finiteStateAutomaton.getTransitionFunctions());
    }


    /**
     * Adiciona uma função de transição no autômato em construção.
     *
     * @param FSATransitionFunction função de transição a ser adicionada no autômato
     * @return próprio construtor de autômato
     */
    public FiniteStateAutomatonBuilder addTransition(FSATransitionFunction FSATransitionFunction) {
        states.add(FSATransitionFunction.getCurrentState());
        states.add(FSATransitionFunction.getFutureState());
        FSATransitionFunctions.add(FSATransitionFunction);
        return this;
    }

    /**
     * Adiciona uma função de transição no autômato em construção.
     *
     * @param currentState estado atual da função de transição
     * @param symbol símbolo a ser lido pela função de transição
     * @param futureState estado futuro da função de transição
     * @return próprio construtor de autômato
     */
    public FiniteStateAutomatonBuilder addTransition(State currentState, String symbol, State futureState) {
        return addTransition(new FSATransitionFunction(currentState, symbol, futureState));
    }


    /**
     * Remove uma coleção de funções de transições que tenham seus atributos iguais aos passados
     * por parâmetro. Exemplo de uso: remover todas funções de transições que saiam do estado "q2"
     * e cheguem no estado "q1", implementação:
     * <code> removeTransitionsWhere( new TransitionAtt[] { TransitionAtt.CURRENT_STATE,
     *      TransitionAtt.FUTURE_STATE }, new String[] { "q2", "q1" } ); </code>
     *
     * @param transitionAtt atributos que serão comparados
     * @param transitionAttValue valor dos atributos que serão comparados
     * @return próprio construtor de autômato
     *
     * @throws RuntimeException vetor com os tipos de atributos deverá ter o mesmo tamanho do vetor
     * com o valor dos atributos
     */
    public FiniteStateAutomatonBuilder removeTransitionsWhere(TransitionAtt[] transitionAtt,
                                                              String[] transitionAttValue) {
        removeTransitionsWhereNotSafe(transitionAtt, transitionAttValue);
        updateStates();
        return this;
    }

    /**
     * Remove uma coleção de funções de transições que tenham seus atributos iguais aos passados
     * por parâmetro. Exemplo de uso: remover todas funções de transições que saiam do estado "q2"
     * e cheguem no estado "q1", implementação:
     * <code> removeTransitionsWhere( new TransitionAtt[] { TransitionAtt.CURRENT_STATE,
     *      TransitionAtt.FUTURE_STATE }, new String[] { "q2", "q1" } ); </code>
     *
     * @param transitionAtt atributos que serão comparados
     * @param transitionAttValue valor dos atributos que serão comparados
     *
     * @throws RuntimeException vetor com os tipos de atributos deverá ter o mesmo tamanho do vetor
     * com o valor dos atributos
     */
    private void removeTransitionsWhereNotSafe(TransitionAtt[] transitionAtt,
                                                              String[] transitionAttValue) {
        if (transitionAtt.length != transitionAttValue.length) {
            throw new RuntimeException(ResourcesContext.getString(R.string.exceptions_transition_builder_args));
        }
        Iterator<FSATransitionFunction> iterator = FSATransitionFunctions.iterator();
        while (iterator.hasNext()) {
            FSATransitionFunction fsaTransitionFunction = iterator.next();
            int contAttTrue = 0;
            for (int i = 0; i < transitionAttValue.length; i++) {
                if (transitionAtt[i].equals(TransitionAtt.CURRENT_STATE) &&
                        fsaTransitionFunction.getCurrentState().getName().equals(transitionAttValue[i])) {
                    contAttTrue++;
                } else if (transitionAtt[i].equals(TransitionAtt.SYMBOL) &&
                        fsaTransitionFunction.getSymbol().equals(transitionAttValue[i])) {
                    contAttTrue++;
                } else if (transitionAtt[i].equals(TransitionAtt.FUTURE_STATE) &&
                        fsaTransitionFunction.getFutureState().getName().equals(transitionAttValue[i])) {
                    contAttTrue++;
                }
            }
            if (contAttTrue == transitionAtt.length) {
                iterator.remove();
            }
        }
    }


    /**
     * Remove um estado do autômato.
     *
     * @param state estado removido do autômato
     * @return próprio construtor de autômato
     */
    @Override
    public FiniteStateAutomatonBuilder removeState(State state) {
        removeTransitionsWhereNotSafe(new TransitionAtt[] { TransitionAtt.CURRENT_STATE },
                new String[] { state.getName() });
        removeTransitionsWhereNotSafe(new TransitionAtt[] { TransitionAtt.FUTURE_STATE },
                new String[] { state.getName() });
        super.removeState(state);
        return this;
    }

    private void updateStates() {
        Set<State> states = new HashSet<>();
        for (FSATransitionFunction fsaTransitionFunction : FSATransitionFunctions) {
            states.add(fsaTransitionFunction.getCurrentState());
            states.add(fsaTransitionFunction.getFutureState());
        }
        this.states.retainAll(states);
        this.finalStates.retainAll(states);
        if (initialState != null && !states.contains(initialState)) {
            initialState = null;
        }
    }


    /**
     * Cria o autômato em construção.
     *
     * @return autômato em construção
     */
    public FiniteStateAutomaton createAutomaton() {
        return new FiniteStateAutomaton(states, initialState, finalStates, FSATransitionFunctions);
    }

}
