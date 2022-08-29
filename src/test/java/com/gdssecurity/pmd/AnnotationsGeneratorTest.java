package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnotationsGeneratorTest {

	
	public AnnotationsGeneratorTest() {
		super();
	}
	
	@Test
	public void testNoGenerator () throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorExample.java", PMDRunner.RULESET_ANNOTATIONS));	
	}
	@Test
	public void testGeneratorOk () throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorOkExample.java", PMDRunner.RULESET_ANNOTATIONS));	
	}
	@Test
	public void testGeneratorBad () throws Exception {
		Assertions.assertEquals(2, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorBadExample.java", PMDRunner.RULESET_ANNOTATIONS));	
	}
	
	@Test
	public void testGeneratorStatic() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorStatic.java", PMDRunner.RULESET_ANNOTATIONS));
	}
	@Test
	public void testGeneratorStaticCaller() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorStaticCaller.java", PMDRunner.RULESET_ANNOTATIONS));
	}
	
	@Test
	public void testGeneratorPrivateCallOk() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorPrivateCallOk.java", PMDRunner.RULESET_ANNOTATIONS));
	}
	@Test
	public void testGeneratorPrivateCallBad() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorPrivateCallBad.java", PMDRunner.RULESET_ANNOTATIONS));
	}
	@Test
	public void testGeneratorTwoCallsInOneFile() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/twocallsonefile/TwoCallsInOneFile.java", PMDRunner.RULESET_ANNOTATIONS));
	}
	
	@Test
	public void testGeneratorTwoCallsInTwoFiles() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/twocallstwofiles", PMDRunner.RULESET_ANNOTATIONS));
	}
	
	@Test
	public void testGeneratorStringBuilder() throws Exception {
		Assertions.assertEquals(2, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorStringBuilder.java", PMDRunner.RULESET_ANNOTATIONS));
	}
	
	
	@Test
	public void testGeneratorChainedCall() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorChainedCall.java", PMDRunner.RULESET_ANNOTATIONS));
	}
}
