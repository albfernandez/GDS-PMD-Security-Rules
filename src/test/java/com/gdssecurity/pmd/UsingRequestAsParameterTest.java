package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UsingRequestAsParameterTest {
	
	public UsingRequestAsParameterTest() {
		super();
	}
	
	@Test
	public void testUsingRequestAsParameter () throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/others/bug71/UsingRequestAsParameterExample.java", PMDRunner.RULESET_XSS));
	}
	
	@Test
	public void testUsingRequestAsParameterFixed () throws Exception {
		Assertions.assertEquals(0, PMDRunner.run(
				"src/test/java/resources/others/bug71/UsingRequestAsParameterFixedExample.java", PMDRunner.RULESET_ANNOTATIONS));
	}

}
