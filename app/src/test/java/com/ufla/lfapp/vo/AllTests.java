package com.ufla.lfapp.vo;

import com.ufla.lfapp.vo.grammar.GrammarTests;
import com.ufla.lfapp.vo.grammar.RuleTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ GrammarTests.class, RuleTests.class })
		public class AllTests {

}
