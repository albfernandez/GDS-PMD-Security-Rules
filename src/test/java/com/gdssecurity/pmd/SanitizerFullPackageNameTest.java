package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SanitizerFullPackageNameTest {

	
	public SanitizerFullPackageNameTest () {
		super();
	}

	@Test
	public void testSanitizers() throws Exception {
		int violations = 
				PMDRunner.run(
						"src/test/java/resources/others/SanitizerFullPackageNameExample.java", 
						PMDRunner.RULESET_XSS
		);
		Assertions.assertEquals(0, violations);
	}
}
