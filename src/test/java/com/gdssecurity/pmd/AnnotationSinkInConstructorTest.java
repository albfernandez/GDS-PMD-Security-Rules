package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnotationSinkInConstructorTest {
	
	
	public AnnotationSinkInConstructorTest() {
		super();
	}
	
	@Test
	public void testAnnotationSinkInConstructor() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/constructor/AnnotationExampleSinkConstructor1.java", PMDRunner.RULESET_XSS_ANNOTATIONS));	
	}

}
