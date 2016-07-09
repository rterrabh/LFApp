package com.ufla.lfapp.vo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
/*@SuiteClasses({ CloneTest.class, Grammar01Test.class, Grammar02Test.class,
		Grammar03Test.class, Grammar04Test.class, Grammar05Test.class,
		Grammar06Test.class, Grammar07Test.class, Grammar08Test.class,
		Grammar09Test.class, Grammar10Test.class, Grammar11Test.class,
		Rule01Test.class, Rule02Test.class, Rule03Test.class,
		Rule04Test.class, Rule05Test.class, Rule06Test.class,
		Rule08Test.class, Rule09Test.class })*/
@SuiteClasses({ GrammarTests.class, RuleTests.class })
		public class AllTests {

}
