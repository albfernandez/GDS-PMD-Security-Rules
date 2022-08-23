package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AttributesTaintedDataTest {

	private static final String RULESET_ANNOTATIONS = "rulesets/xss-annotations.xml";
	
	
	
	@Test
	public void attributesAreTaintedDataByDefault () throws Exception {
		Assertions.assertEquals(3, PMDRunner.run("src/test/java/resources/others/AttributesAreTaintedData.java", RULESET_ANNOTATIONS));
	}
	
	@Test
	public void attributesAreTaintedByDefaultSql() throws Exception {
		Assertions.assertEquals(2, PMDRunner.run("src/test/java/resources/others/AttributesAreTaintedData.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
