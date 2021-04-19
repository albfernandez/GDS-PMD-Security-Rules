package resources.annotations;

import com.gdssecurity.pmd.annotations.HTMLGenerator;
import com.gdssecurity.pmd.annotations.HTMLSink;

public class AnnotationsExampleComplexCall2 {

	public AnnotationsExampleComplexCall2() {
		super();
	}
	
	@HTMLSink
	@HTMLGenerator
	public String complex(String param) {
		return "";
	}
}
