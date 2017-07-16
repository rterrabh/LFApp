package com.ufla.lfapp.core.machine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses(
        { DFACompleteTest.class, DFAMinimizationTest.class, DotLanguageParserTest.class,
        FSASimulatorTest.class, LBASimulatorTest.class, NDFAToDFATest.class,
        PDASimulatorTest.class, RegexToNDFATest.class, TMEnumeratorSimulatorTest.class,
        TMMultiTapeSimulatorTest.class, TMMultiTrackSimulatorTest.class, TMSimulatorTest.class })

public class MachineTests {
}
