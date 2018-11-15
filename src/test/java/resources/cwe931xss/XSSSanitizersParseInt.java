package resources.cwe931xss;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

public class XSSSanitizersParseInt {

	public XSSSanitizersParseInt() {
		super();
	}

	public void executeGood(JspWriter writer, String param) throws IOException {
		String[] array = { "a", "b", "c", "d", "e" };
		writer.print(array[Integer.parseInt(param)]);
		writer.print(array[Integer.parseInt(param) - 1]);
		writer.print(Integer.parseInt(param) - 1);
		writer.print(Integer.parseInt(param));
		int pos = Integer.parseInt(param);
		writer.print(array[pos]);
	}
}
