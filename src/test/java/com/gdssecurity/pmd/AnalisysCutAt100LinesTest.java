package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnalisysCutAt100LinesTest {
	
	public AnalisysCutAt100LinesTest() {
		super();
	}
	
	@Test
	public void analisysCutAt100Lines() throws Exception{
		Assertions.assertEquals(2, PMDRunner.run("src/test/java/resources/others/pmdbug1469/AnalisysCutAt100Lines.java", PMDRunner.RULESET_XSS));
	}

}
