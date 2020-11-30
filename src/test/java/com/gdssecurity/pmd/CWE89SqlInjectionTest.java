package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class CWE89SqlInjectionTest {

	private static String DIR = "src/test/java/resources/cwe89sqlinjection/";
	
	public CWE89SqlInjectionTest() {
		super();
	}

	private static int run (String file) throws Exception {
		return PMDRunner.run(DIR + file, PMDRunner.RULESET_SQL_INJECTION);
	}

	
	@Test
	public void simpleServletTest() throws Exception {
		Assertions.assertEquals(1, run("SqliServletTest.java"));
	}
	
	@Test
	public void testDontMissAFunction () throws Exception {
		Assertions.assertEquals(20, run("SqliDontMissAFunction.java"));		
	}
	
	@Test
	public void testFieldsAreTainted () throws Exception {
		Assertions.assertEquals(2, run("SqliFieldsAreTainted.java"));
	}
	
	@Test
	public void testMethodsParametersTainted () throws Exception {
		Assertions.assertEquals(1, run("SqliMethodParametersAreTainted.java"));
	}
	
	@Test
	public void testConstFieldsAreSafe() throws Exception {
		Assertions.assertEquals(0, run("SqliConstFieldsAreSafe.java"));
	}
	
	@Test
	public void testNumericTypesAreSafe() throws Exception {
		Assertions.assertEquals(0, run("SqliNumericTypesAreSafe.java"));
	}
	
	@Test
	public void testStringBuilder() throws Exception {
		Assertions.assertEquals(2, run("StringBuilderTest.java"));
	}
	
	@Test
	public void testSafeFunctions() throws Exception {
		Assertions.assertEquals(0, run("MethodsThatReturnIntAreSafe.java"));
	}
	
	@Test
	public void testJPAInjection() throws Exception {
		Assertions.assertEquals(3, run("JPAInjection.java"));
	}
	
	@Test
	public void testConditional() throws Exception {
		Assertions.assertEquals(2, run("SqliConditional.java"));
	}
	
	@Test
	public void testStringAppend() throws Exception {
		Assertions.assertEquals(1, run("SqliAppendString.java"));
	}
	

}
