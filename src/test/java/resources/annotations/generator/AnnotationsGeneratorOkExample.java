package resources.annotations.generator;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang3.StringEscapeUtils;

import com.gdssecurity.pmd.annotations.HTMLGenerator;

public class AnnotationsGeneratorOkExample {

	
	@HTMLGenerator
	public String generateCode(String bad) {
		return "ok" + StringEscapeUtils.escapeHtml4(bad);
	}
	
	public void generate (JspWriter out, String bad) throws IOException{
		out.write(generateCode(bad));
	}
}