package com.gdssecurity.pmd;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.sourceforge.pmd.RuleViolation;

public class ReportColumnTest {
	
	public ReportColumnTest() {
		super();
	}
	
	@Test
	public void testNoZero() throws Exception {
		List<RuleViolation> violations = PMDRunner.getViolations("src/test/java/resources/cwe89sqlinjection/SqliServletTest.java", PMDRunner.RULESET_SQL_INJECTION);
		Assertions.assertNotNull(violations);
		Assertions.assertEquals(1, violations.size());
		RuleViolation violation = violations.get(0);
		Assertions.assertNotEquals(0, violation.getBeginColumn());
		Assertions.assertNotEquals(0, violation.getEndColumn());
	}

}
