package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SinkFullPackageNameTest {

	
	public SinkFullPackageNameTest () {
		super();
	}

	@Test
	public void testSanitizers() throws Exception {
		int violations = 
				PMDRunner.run(
						"src/test/java/resources/others/SinkFullPackageExample.java", 
						PMDRunner.RULESET_XSS
		);
		Assertions.assertEquals(1, violations);
	}
}
