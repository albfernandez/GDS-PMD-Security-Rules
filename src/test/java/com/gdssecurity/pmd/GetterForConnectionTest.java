package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GetterForConnectionTest {
	
	public GetterForConnectionTest() {
		super();
	}
	
	@Test
	@Disabled("not yet fixed")
	public void testGetterForConnection () throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/others/bug30/DemoUsingGetterBug30.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
