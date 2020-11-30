package com.gdssecurity.pmd;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PMDBug1393HangsTest {

	public PMDBug1393HangsTest() {
		super();
	}
	
	@Test
	public void testTimeoutPMDBug1393() throws Exception{
		Assertions.assertTimeout(Duration.ofSeconds(10), () -> {
			Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/others/pmdbug1393/PMDBug1393.java", PMDRunner.RULESET_UNVALIDATED_REDIRECTS));
		});
	}
}
