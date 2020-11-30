package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringManipulationTest {
	
	public StringManipulationTest() {
		super();
	}
	
	@Test
	public void testStringManipulation () throws Exception {
		Assertions.assertEquals(3, PMDRunner.run("src/test/java/resources/others/StringManipulationExample.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
