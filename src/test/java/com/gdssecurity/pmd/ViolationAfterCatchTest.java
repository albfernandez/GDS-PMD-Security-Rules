package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ViolationAfterCatchTest {

	public ViolationAfterCatchTest() {
		super();
	}
	
	@Test
	public void testViolationAfterCatch() throws Exception{
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/others/bug28/ViolationAfterCatch.java", PMDRunner.RULESET_XSS));
	}	
}
