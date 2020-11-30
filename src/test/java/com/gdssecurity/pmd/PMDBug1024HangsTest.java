package com.gdssecurity.pmd;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PMDBug1024HangsTest {

	
	public PMDBug1024HangsTest () {
		super();
	}
	
	@Test
	public void testTimeoutPMDBug1024 () throws Exception{
		Assertions.assertTimeout(Duration.ofSeconds(10), () -> {
			Assertions.assertEquals(0, PMDRunner.run("src/test/java/resources/others/pmdbug1024/PMDBug1024.java", PMDRunner.RULESET_UNVALIDATED_REDIRECTS));
		});
	}
}
