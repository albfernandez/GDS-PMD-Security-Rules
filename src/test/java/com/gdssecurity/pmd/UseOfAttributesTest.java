package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UseOfAttributesTest {
	
	public UseOfAttributesTest() {
		super();
	}

	
	@Test
	public void testUseOfAttributes() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/others/UseOfAttributes.java", PMDRunner.RULESET_SQL_INJECTION));
	}

}
