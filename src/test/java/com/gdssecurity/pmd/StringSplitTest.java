package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringSplitTest {

	public StringSplitTest() {
		super();
	}
	
	@Test
	public void testStringSplit () throws Exception {
		Assertions.assertEquals(2, PMDRunner.run("src/test/java/resources/others/StringSplitExample.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
