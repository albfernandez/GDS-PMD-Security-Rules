package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class StringSplitTest {

	public StringSplitTest() {
		super();
	}
	
	@Test
	public void testStringSplit () throws Exception {
		Assert.assertEquals(2, PMDRunner.run("src/test/java/resources/others/StringSplitExample.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
