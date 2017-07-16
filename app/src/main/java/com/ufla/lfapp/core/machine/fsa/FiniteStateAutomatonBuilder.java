package com.ufla.lfapp.core.machine.fsa;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.machine.MachineBuilder;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.TransitionAtt;
import com.ufla.lfapp.utils.ResourcesContext;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Responsável por construir um autômato.
 *
 * Created by carlos on 12/11/16.
 */

public class FiniteStateAutomatonBuilder extends MachineBuilder {

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
     * Remove uma função de transição do autômato em construção.
     *
     * @param FSATransitionFunction função de transição a ser removida do autômato
     * @return próprio construtor de autômato
     */
    public FiniteStateAutomatonBuilder removeTransition(FSATransitionFunction FSATransitionFunction) {
        FSATransitionFunctions.remove(FSATransitionFunction);
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
     * @return próprio construtor de autômato
     *
     * @throws RuntimeException vetor com os tipos de atributos deverá ter o mesmo tamanho do vetor
     * com o valor dos atributos
     */
    public FiniteStateAutomatonBuilder removeTransitionsWhere(TransitionAtt transitionAtt[],
                                                              State transitionAttValue[]) {
        if (transitionAtt.length != transitionAttValue.length) {
            throw new RuntimeException(ResourcesContext.getString(R.string.exceptions_transition_builder_args));
        }
        Iterator<FSATransitionFunction> iterator = FSATransitionFunctions.iterator();
        while (iterator.hasNext()) {
            FSATransitionFunction FSATransitionFunction = iterator.next();
            int contAttTrue = 0;
            for (int i = 0; i < transitionAttValue.length; i++) {
                if (transitionAtt[i].equals(TransitionAtt.CURRENT_STATE) &&
                        FSATransitionFunction.getCurrentState().equals(transitionAttValue[i])) {
                    contAttTrue++;
                } else if (transitionAtt[i].equals(TransitionAtt.SYMBOL) &&
                        FSATransitionFunction.getSymbol().equals(transitionAttValue[i])) {
                    contAttTrue++;
                } else if (transitionAtt[i].equals(TransitionAtt.FUTURE_STATE) &&
                        FSATransitionFunction.getFutureState().equals(transitionAttValue[i])) {
                    contAttTrue++;
                }
            }
            if (contAttTrue == transitionAtt.length) {
                iterator.remove();
            }
        }
        return this;
    }

    /**
     * Remove um estado do autômato.
     *
     * @param state estado removido do autômato
     * @return próprio construtor de autômato
     */
    @Override
    public FiniteStateAutomatonBuilder removeState(State state) {
        removeTransitionsWhere(new TransitionAtt[] { TransitionAtt.CURRENT_STATE },
                new State[] { state });
        removeTransitionsWhere(new TransitionAtt[] { TransitionAtt.FUTURE_STATE },
                new State[] { state });
        super.removeState(state);
        return this;
    }

    /**
     * Remove uma coleção de estados do autômato.
     *
     * @param states coleção de estados removidas do autômato
     * @return próprio construtor de autômato
     */
    @Override
    public FiniteStateAutomatonBuilder removeStates(Collection<State> states) {
        for (State state : states) {
            removeState(state);
        }
        return this;
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
