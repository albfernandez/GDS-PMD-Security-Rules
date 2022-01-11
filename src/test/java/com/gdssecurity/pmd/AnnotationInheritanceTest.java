package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnotationInheritanceTest {
	
	public AnnotationInheritanceTest() {
		super();
	}
	
	
	@Test
	public void testAnnotationInheritance () throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/AnnotationInheritanceClass.java", "rulesets/xss-annotations.xml"));
	}

}
