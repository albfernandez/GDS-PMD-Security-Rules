package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TernaryAsParameterTest {

	public TernaryAsParameterTest() {
		super();
	}
	
	@Test
	public void testTernary () throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/others/TernaryAsParameter.java", PMDRunner.RULESET_XSS));
	}
	
	@Test
	public void testTernaryWithParenthesis () throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/others/TernaryAsParameterWithParenthesis.java", PMDRunner.RULESET_XSS));
	}
	
	@Test

	public void testTernaryInStringConcatenation () throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/others/TernaryInStringConcatenation.java", PMDRunner.RULESET_SQL_INJECTION));
	}
	
	@Test
	public void testTernaryInStringConcatenationMultipleConditions () throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/others/TernaryInStringConcatenationMultipleConditions.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
