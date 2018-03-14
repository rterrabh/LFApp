package com.ufla.lfapp.core.machine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses(
        {ConfigurationTest.class,
                SuiteTestDotLanguage.class, GraphAdapterTest.class,
                TransitionFunctionTest.class
        })


/**
 * Created by carlos on 19/12/17.
 */

public class SuiteTestCommon {
}
