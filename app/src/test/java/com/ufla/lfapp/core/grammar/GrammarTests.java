package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AcademicSupportTest.class, AcademicSupportFNGTest.class, CloneTest.class, Grammar01Test.class, Grammar02Test.class,
        Grammar03Test.class, Grammar04Test.class, Grammar05Test.class,
        Grammar06Test.class, Grammar07Test.class, Grammar08Test.class,
        Grammar09Test.class, Grammar10Test.class, Grammar11Test.class,
        GrammarTest.class, GrammarParserTest.class, RuleTests.class,
        GrammarTransformationsSudkampTest.class, LefmostDerAndAmbSudkampTest.class,
        PropRegLangSudkampTest.class, GrammarParserTest2.class, NodeDerivationTest.class,
        AmbiguityVerificationTest.class, TreeDerivationTest.class })
public class GrammarTests {

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    GrammarTests() {
        ResourcesContext.isTest = true;
    }

}
