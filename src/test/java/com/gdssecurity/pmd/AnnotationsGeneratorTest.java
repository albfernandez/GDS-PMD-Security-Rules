package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnotationsGeneratorTest {

	private static final String RULESET_ANNOTATIONS = "rulesets/xss-annotations.xml";
	
	public AnnotationsGeneratorTest() {
		super();
	}
	
	@Test
	public void testNoGenerator () throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorExample.java", RULESET_ANNOTATIONS));	
	}
	@Test
	public void testGeneratorOk () throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorOkExample.java", RULESET_ANNOTATIONS));	
	}
	@Test
	public void testGeneratorBad () throws Exception {
		Assertions.assertEquals(2, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorBadExample.java", RULESET_ANNOTATIONS));	
	}
	
	@Test
	public void testGeneratorStatic() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorStatic.java", RULESET_ANNOTATIONS));
	}
	@Test
	public void testGeneratorStaticCaller() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorStaticCaller.java", RULESET_ANNOTATIONS));
	}
	
	@Test
	public void testGeneratorPrivateCallOk() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorPrivateCallOk.java", RULESET_ANNOTATIONS));
	}
	@Test
	public void testGeneratorPrivateCallBad() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorPrivateCallBad.java", RULESET_ANNOTATIONS));
	}
	@Test
	public void testGeneratorTwoCallsInOneFile() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/twocallsonefile/TwoCallsInOneFile.java", RULESET_ANNOTATIONS));
	}
	
	@Test
	public void testGeneratorTwoCallsInTwoFiles() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/twocallstwofiles", RULESET_ANNOTATIONS));
	}
	
	@Test
	public void testGeneratorStringBuilder() throws Exception {
		Assertions.assertEquals(2, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorStringBuilder.java", RULESET_ANNOTATIONS));
	}
	
	
	@Test
	public void testGeneratorChainedCall() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorChainedCall.java", RULESET_ANNOTATIONS));
	}
}
