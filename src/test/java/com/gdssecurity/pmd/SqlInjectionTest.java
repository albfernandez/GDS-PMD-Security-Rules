package com.gdssecurity.pmd;


import org.junit.Assert;
import org.junit.Test;

public class SqlInjectionTest {

	
	@Test
	public void test1() throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/cwe89sqlinjection/sqlinjection1", PMDRunner.RULESET_SQL_INJECTION));
	}

}
