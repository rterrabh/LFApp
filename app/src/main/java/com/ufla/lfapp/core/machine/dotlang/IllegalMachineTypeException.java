package com.ufla.lfapp.core.machine.dotlang;

import com.ufla.lfapp.core.machine.MachineType;

/**
 * Created by terra on 14/07/17.
 */

public class IllegalMachineTypeException extends RuntimeException {

    public IllegalMachineTypeException(MachineType typeDefined, MachineType typeRequired) {
        super("Machine type defined: '" + typeDefined + "', type required: " + typeRequired);
    }
}
