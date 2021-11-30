package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class AnnotationSinkInConstructorTest {
	
	private static final String RULESET_ANNOTATIONS = "rulesets/xss-annotations.xml";
	
	public AnnotationSinkInConstructorTest() {
		super();
	}
	
	@Test
	@Disabled
	public void testAnnotationSinkInConstructor() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/constructor/AnnotationExampleSinkConstructor1.java", RULESET_ANNOTATIONS));	
	}

}
