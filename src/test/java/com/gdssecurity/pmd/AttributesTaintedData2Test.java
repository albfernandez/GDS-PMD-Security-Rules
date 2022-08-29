package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AttributesTaintedData2Test {


	
	
	@Test
	public void attributesAreTaintedDataByDefault () throws Exception {
		Assertions.assertEquals(3, PMDRunner.run("src/test/java/resources/others/AttributesAreTaintedData2.java", PMDRunner.RULESET_SQL_INJECTION));
	}

}
