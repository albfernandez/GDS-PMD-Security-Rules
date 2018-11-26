package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class AnalisysCutAt100LinesTest {
	
	public AnalisysCutAt100LinesTest() {
		super();
	}
	
	@Test
	@Ignore("we need pmd 6.10.0 to configure max_loops and fix this bug")
	public void analisysCutAt100Lines() throws Exception{
		Assert.assertEquals(2, PMDRunner.run("src/test/java/resources/others/pmdbug1469/AnalisysCutAt100Lines.java", PMDRunner.RULESET_XSS));
	}

}