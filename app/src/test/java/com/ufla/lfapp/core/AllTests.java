package com.ufla.lfapp.core;

import com.ufla.lfapp.core.grammar.GrammarTests;
import com.ufla.lfapp.core.grammar.RuleTests;
import com.ufla.lfapp.core.machine.MachineTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ GrammarTests.class, MachineTests.class})
		public class AllTests {

}
