package com.gdssecurity.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CWE327BrokenCryptoTest {

	public CWE327BrokenCryptoTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		Assertions.assertEquals(1,
				PMDRunner.run("src/test/java/resources/cwe327brokencrypto/CWE327UseBrokenCryptoBasic.java", PMDRunner.RULESET_WEAK_CRYPTO));
	}
}
