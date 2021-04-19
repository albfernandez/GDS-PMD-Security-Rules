package resources.annotations;

import com.gdssecurity.pmd.annotations.HTMLGenerator;
import com.gdssecurity.pmd.annotations.HTMLSink;

public class AnnotationsExampleComplexCall1 {

	@HTMLSink
	@HTMLGenerator
	public static String example1(String param) {
		AnnotationsExampleComplexCall2 example = new AnnotationsExampleComplexCall2();
		return example.complex(param);
	}
}
