package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Bug46SinkInConditionalTest {

	public Bug46SinkInConditionalTest() {
		super();
	}
	
	@Test
	public void testSinkInConditional () throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/others/Bug46SinkInConditional.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
