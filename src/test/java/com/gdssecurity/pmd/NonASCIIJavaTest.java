package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NonASCIIJavaTest {
	
	public NonASCIIJavaTest () {
		super();
	}
	@Test
	public void testStringManipulation () throws Exception {
		Assertions.assertEquals(2, PMDRunner.run("src/test/java/resources/noascii/NonASCIIJavaExample.java", PMDRunner.RULESET_SQL_INJECTION));
	}

}
