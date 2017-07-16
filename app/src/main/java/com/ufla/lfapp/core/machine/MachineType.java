package com.ufla.lfapp.core.machine;

import java.io.Serializable;

/**
 * Created by terra on 13/07/17.
 */

public enum MachineType implements Serializable {

    FSA,
    PDA,
    PDA_EXT,
    TM,
    TM_MULTI_TRACK,
    TM_MULTI_TAPE,
    TM_ENUM,
    UNDEFINED;

}
