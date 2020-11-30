package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CWE113HttpResponseSplittingTest {

	public CWE113HttpResponseSplittingTest() {
		super();
	}

	@Test
	public void testUnvalidatedRedirects() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/cwe113responsesplitting/TestRedirectServlet.java",
				PMDRunner.RULESET_UNVALIDATED_REDIRECTS));
	}
	
	@Test
	public void testHTTPResponseSplitting() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/cwe113responsesplitting/TestRedirectServlet.java",
				PMDRunner.RULESET_HTTP_RESPONSE_SPLITTING));
	}
}
