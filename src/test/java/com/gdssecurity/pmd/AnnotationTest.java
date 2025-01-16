package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnotationTest {

	
	
	public AnnotationTest() {
		super();
	}
	
	@Test
	public void testSinkIsOkStringConcat() throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/AnnotationExampleSinkConcat.java", PMDRunner.RULESET_XSS_ANNOTATIONS));	
	}
	
	@Test
	public void testSinkIsOk () throws Exception {
		Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/AnnotationExample.java", PMDRunner.RULESET_XSS_ANNOTATIONS));	
	}
	@Test
	public void testCallingSinkIsNotOk () throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationExample2.java", PMDRunner.RULESET_XSS_ANNOTATIONS));	
	}
	
	@Test
	public void testCallinkSinkValidateParams() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationExampleValidateParams.java", PMDRunner.RULESET_XSS_ANNOTATIONS));
	}
	
	@Test
	public void testCallinkSinkValidateParamsQualified() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationExampleValidateParamsQualified.java", PMDRunner.RULESET_XSS_ANNOTATIONS));
	}
	
	@Test
	public void testCallinkSinkValidateParamsStatic() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationExampleValidateParamsStatic.java", PMDRunner.RULESET_XSS_ANNOTATIONS));
	}
	@Test
	public void testCallinkSinkValidateParamsStaticQualified() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationExampleValidateParamsStaticQualified.java", PMDRunner.RULESET_XSS_ANNOTATIONS));
	}	
	@Test
	public void testCallinkSinkValidateParamsStaticOtherClass() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationExampleValidateParamsStaticOtherClass.java", PMDRunner.RULESET_XSS_ANNOTATIONS));
	}
	
	@Test
	public void testComplex() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationsExampleComplexCall1.java", PMDRunner.RULESET_XSS_ANNOTATIONS));
	}
	
	@Test
	public void testComplex3() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationsExampleComplexCall3.java", PMDRunner.RULESET_XSS_ANNOTATIONS));
	}
	
	
}
