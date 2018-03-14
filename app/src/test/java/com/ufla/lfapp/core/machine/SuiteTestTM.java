package com.ufla.lfapp.core.machine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses(
        {TMEnumeratorSimulatorTest.class, TMMultiTapeSimulatorTest.class,
                TMMultiTrackSimulatorTest.class, TMSimulatorTest.class,
                TuringMachineSudkampTest.class
        })
/**
 * Created by carlos on 19/12/17.
 */

public class SuiteTestTM {
}
