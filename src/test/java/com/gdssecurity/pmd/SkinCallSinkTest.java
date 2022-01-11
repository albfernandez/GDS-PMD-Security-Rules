package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SkinCallSinkTest {

	
	public SkinCallSinkTest() {
		super();
	}
	
	@Test
	public void testCallSink () throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/AnnotationsSinkCallSink.java", "rulesets/xss-annotations.xml"));
	}
}
