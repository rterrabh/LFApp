package com.ufla.lfapp.vo.machine;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Responsável por construir um autômato.
 *
 * Created by carlos on 12/11/16.
 */

public class AutomatonBuilder extends MachineBuilder {

    protected SortedSet<TransitionFunction> transitionFunctions;

    /**
     * Constrói um autômato vazio.
     */
    public AutomatonBuilder() {
        super();
        transitionFunctions = new TreeSet<>();
    }

    /**
     * Constrói um autômato com os atributos do autômato passada por parâmetro.
     *
     * @param automaton autômato usada para construir o autômato desse construtor de autômato
     */
    public AutomatonBuilder(Automaton automaton) {
        super(automaton);
        transitionFunctions = new TreeSet<>(automaton.getTransitionFunctions());
    }


    /**
     * Adiciona uma função de transição no autômato em construção.
     *
     * @param transitionFunction função de transição a ser adicionada no autômato
     * @return próprio construtor de autômato
     */
    public AutomatonBuilder addTransition(TransitionFunction transitionFunction) {
        states.add(transitionFunction.getCurrentState());
        states.add(transitionFunction.getFutureState());
        transitionFunctions.add(transitionFunction);
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
    public AutomatonBuilder addTransition(String currentState, String symbol, String futureState) {
        return addTransition(new TransitionFunction(currentState, symbol, futureState));
    }

    /**
     * Remove uma função de transição do autômato em construção.
     *
     * @param transitionFunction função de transição a ser removida do autômato
     * @return próprio construtor de autômato
     */
    public AutomatonBuilder removeTransition(TransitionFunction transitionFunction) {
        transitionFunctions.remove(transitionFunction);
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
    public AutomatonBuilder removeTransitionsWhere(TransitionAtt transitionAtt[],
                                                  String transitionAttValue[]) {
        if (transitionAtt.length != transitionAttValue.length) {
            throw new RuntimeException("Transitions args array lenght diferrent of types!");
        }
        Iterator<TransitionFunction> iterator = transitionFunctions.iterator();
        while (iterator.hasNext()) {
            TransitionFunction transitionFunction = iterator.next();
            int contAttTrue = 0;
            for (int i = 0; i < transitionAttValue.length; i++) {
                if (transitionAtt[i].equals(TransitionAtt.CURRENT_STATE) &&
                        transitionFunction.getCurrentState().equals(transitionAttValue[i])) {
                    contAttTrue++;
                } else if (transitionAtt[i].equals(TransitionAtt.SYMBOL) &&
                        transitionFunction.getSymbol().equals(transitionAttValue[i])) {
                    contAttTrue++;
                } else if (transitionAtt[i].equals(TransitionAtt.FUTURE_STATE) &&
                        transitionFunction.getFutureState().equals(transitionAttValue[i])) {
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
    public AutomatonBuilder removeState(String state) {
        removeTransitionsWhere(new TransitionAtt[] { TransitionAtt.CURRENT_STATE },
                new String[] { state });
        removeTransitionsWhere(new TransitionAtt[] { TransitionAtt.FUTURE_STATE },
                new String[] { state });
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
    public AutomatonBuilder removeStates(Collection<String> states) {
        for (String state : states) {
            removeState(state);
        }
        return this;
    }

    /**
     * Cria o autômato em construção.
     *
     * @return autômato em construção
     */
    public Automaton createAutomaton() {
        return new Automaton(states, initialState, finalStates, transitionFunctions);
    }

}
