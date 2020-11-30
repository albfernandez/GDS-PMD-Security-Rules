package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class WritingToResponseWithWriter {

	public WritingToResponseWithWriter() {
		super();
	}
	
	@Test
	@Disabled("to be fixed")
	public void test1() throws Exception {
		Assertions.assertEquals(1, PMDRunner.run("src/test/java/resources/others/bug31/UsingWriterOnReponseGetOutputStream.java", PMDRunner.RULESET_XSS));
	}
}
