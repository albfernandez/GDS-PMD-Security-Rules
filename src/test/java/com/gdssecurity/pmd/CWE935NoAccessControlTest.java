package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CWE935NoAccessControlTest {
	
	public CWE935NoAccessControlTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		Assertions.assertEquals(2,
				PMDRunner.run("src/test/java/resources/cwe935noaccesscontrol/TestRoleAuthZServlet.java", PMDRunner.RULESET_ACCESS));
	}
}
