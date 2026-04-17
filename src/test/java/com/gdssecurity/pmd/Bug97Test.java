package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Bug97Test {

	public Bug97Test() {
		super();
	}
	
	@Test
	public void test() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/others/bug97/Bug97.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
