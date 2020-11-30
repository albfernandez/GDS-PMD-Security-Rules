package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InheritanceTest {
	
	public InheritanceTest () {
		super();
	}

	@Test
	public void test1 () throws Exception {
		Assertions.assertEquals(
				2,
				PMDRunner.run("src/test/java/resources/inheritance/", PMDRunner.RULESET_SQL_INJECTION)
		);
	}
}
