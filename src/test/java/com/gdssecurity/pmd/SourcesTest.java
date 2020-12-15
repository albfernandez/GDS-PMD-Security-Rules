package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SourcesTest {

	public SourcesTest () {
		super();
	}
	@Test
	public void testAllOptions() throws Exception {
		Assertions.assertEquals(11, PMDRunner.run("src/test/java/resources/others/SourcesExample.java",
				PMDRunner.RULESET_XSS));
	}
}
