package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

// cwe-116
// cwe-931
public class CWE931XssTest {

	public CWE931XssTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/cwe931xss/TestXSSServlet.java", PMDRunner.RULESET_XSS));
	}

	@Test
	public void test2() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/cwe931xss/XSS1jsp.java", PMDRunner.RULESET_XSS));
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/cwe931xss/XSS2jsp.java", PMDRunner.RULESET_XSS));
	}

}
