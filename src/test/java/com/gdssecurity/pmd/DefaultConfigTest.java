package com.gdssecurity.pmd;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DefaultConfigTest {
	
	public DefaultConfigTest () {
		super();
	}
	
	// Execute default rules over all tests files to ensure no misconfiguration occurs
	@Test
	public void defaultConfig() throws Exception {
		Assertions.assertTimeout(Duration.ofSeconds(20), () -> {
			Assertions.assertTrue(PMDRunner.run("src/test/java/resources", PMDRunner.RULESET_DEFAULT) > 0);
		});
	}

}
