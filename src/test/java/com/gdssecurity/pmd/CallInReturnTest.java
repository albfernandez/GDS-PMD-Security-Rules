package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CallInReturnTest {

	public CallInReturnTest() {
		super();
	}
	@Test
	public void testCallInReturn() throws Exception {
		Assertions.assertEquals(1,
				PMDRunner.run("src/test/java/resources/others/CallInReturnExample.java", PMDRunner.RULESET_SQL_INJECTION));
	}
	@Test
	public void testCallInReturnWithParenthesis() throws Exception {
		Assertions.assertEquals(1,
				PMDRunner.run("src/test/java/resources/others/CallInReturnWithParenthesis.java", PMDRunner.RULESET_SQL_INJECTION));
	}


}
