package com.ufla.lfapp.core.machine;



import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses(
        {DFACompleteTest.class, DFAMinimizationTest.class,
                FiniteAutomataSudkampTest.class,
                FSABuilderTest.class, FSASimulatorTest.class,
                FSATest.class, NDFAToDFATest.class,
                RegexToNDFATest.class
        })

/**
 * Created by carlos on 10/23/17.
 */
public class SuiteTestFSA {
}
