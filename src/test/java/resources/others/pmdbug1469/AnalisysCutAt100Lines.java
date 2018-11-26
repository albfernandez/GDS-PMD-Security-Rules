package resources.others.pmdbug1469;

import java.io.IOException;


/*
 * https://github.com/pmd/pmd/pull/1469
 * 
 * MAX_LOOPS is set to 100 in pmd-core/src/main/java/net/sourceforge/pmd/lang/dfa/pathfinder/DAAPathFinder.java
 * The pull request make maxLoops configurable, so we can configure it
 * 
 * Will be released in 6.10.0
 *
 */
public class AnalisysCutAt100Lines {

	
	public void nop() {
		
	}	
	
	public void veryLargeFunction(javax.servlet.jsp.JspWriter writer, String badValue) throws IOException {
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		nop();
		writer.write(badValue);
		writer.write(badValue);
	}
}
