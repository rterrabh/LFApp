package com.ufla.lfapp.core.machine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses(
        { SuiteTestCommon.class, SuiteTestFSA.class, SuiteTestPDA.class,
                SuiteTestLBA.class, SuiteTestTM.class
        })

public class SuiteTestMachine {
}
