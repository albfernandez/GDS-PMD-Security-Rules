package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParameterConfussionTest {
	
	public ParameterConfussionTest() {
		super();
	}
	
	@Test
	public void confussion() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/AnnotationsParameterConfussion.java", PMDRunner.RULESET_ANNOTATIONS));
	}

}
