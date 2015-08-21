package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class AnnotationTest {

	@Test
	public void testSinkIsOk () throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/others/AnnotationExample.java", PMDRunner.RULESET_XSS));	
	}
	@Test
	public void testCallingSinkIsNotOk () throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/others/AnnotationExample2.java", PMDRunner.RULESET_XSS));	
	}
}
