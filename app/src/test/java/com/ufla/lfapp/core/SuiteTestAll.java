package com.ufla.lfapp.core;

import com.ufla.lfapp.core.grammar.SuiteTestGrammar;
import com.ufla.lfapp.core.machine.SuiteTestMachine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ SuiteTestGrammar.class, SuiteTestMachine.class})
		public class SuiteTestAll {

}
