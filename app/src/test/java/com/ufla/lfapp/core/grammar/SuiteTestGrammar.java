package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SuiteTestCommon.class, SuiteTestCFG.class, SuiteTestCSG.class,
            SuiteTestIG.class, SuiteTestMistGrammar.class,
            SuiteTestRG.class })
public class SuiteTestGrammar {

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    SuiteTestGrammar() {
        ResourcesContext.isTest = true;
    }

}
