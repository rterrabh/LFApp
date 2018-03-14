package com.ufla.lfapp.core.grammar;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AcademicSupportFNGTest.class, AcademicSupportTest.class,
        AmbiguityVerificationTest.class, CloneTest.class, GrammarTest.class,
        LefmostDerAndAmbSudkampTest.class, NodeDerivationTest.class,
        SuiteTestRule.class, TreeDerivationTest.class
        })
/**
 * Created by carlos on 19/12/17.
 */

public class SuiteTestCommon {
}
