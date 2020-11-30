package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UsingThisTest {
	
	public UsingThisTest() {
		super();
	}

	@Test
	public void testUsingThis () throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/others/UsingThis.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
