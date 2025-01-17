package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SanitizerInSingletonTest {
	
	public SanitizerInSingletonTest() {
		super();
	}
	
	@Test
	public void testSanitizerInSingleton() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run(
				"src/test/java/resources/others/bug72/ExampleSanitizerInSingleton.java", PMDRunner.RULESET_XSS_ANNOTATIONS));
	}
	
	@Test
	public void testSanitizerInSingleton2() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run(
				"src/test/java/resources/others/bug72/ExampleSanitizerInSingleton2.java", PMDRunner.RULESET_XSS_ANNOTATIONS));
	}

}
