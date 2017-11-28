package com.ufla.lfapp.core.machine;



import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses(
        {DFACompleteTest.class, DFAMinimizationTest.class, DotLanguageParserTest.class,
                FSASimulatorTest.class, LBASimulatorTest.class, NDFAToDFATest.class,
                PDASimulatorTest.class, RegexToNDFATest.class, TMEnumeratorSimulatorTest.class,
                TMMultiTapeSimulatorTest.class, TMMultiTrackSimulatorTest.class, TMSimulatorTest.class,
                FiniteAutomataSudkampTest.class, PDAAndCFLSudkampTest.class,
                TuringMachineSudkampTest.class, GraphAdapterTest.class, ConfigurationTest.class,
                TransitionFunctionTest.class, FSABuilderTest.class
        })

/**
 * Created by carlos on 10/23/17.
 */
public class FSATests {
}
