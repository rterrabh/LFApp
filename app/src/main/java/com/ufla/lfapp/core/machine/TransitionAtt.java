package com.ufla.lfapp.core.machine;

import java.io.Serializable;

/**
 * Enumerador que representa os atributos de uma função de transição.
 */
public enum TransitionAtt implements Serializable {
    CURRENT_STATE,
    SYMBOL,
    FUTURE_STATE
}
