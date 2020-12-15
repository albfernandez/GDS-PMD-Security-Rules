package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CWE22PathTraversal {

	public CWE22PathTraversal() {
		super();
	}

	@Test
	public void test1() throws Exception {
		Assertions.assertEquals(5, PMDRunner.run("src/test/java/resources/cwe22pathtraversal/PathTraversalExample.java",
				PMDRunner.RULESET_PATH_TRAVERSAL));
	}
}
