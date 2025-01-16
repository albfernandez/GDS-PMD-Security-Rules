package resources.others.bug71;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;

public class UsingRequestAsParameterExample {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
	    try (PrintWriter pw = response.getWriter()) {
		    String html = generateHtml(request);
		    pw.print(html);
	    }

	}

	
	private String generateHtml(HttpServletRequest request) {
	     String bad = request.getParameter("bad");
	     String html = "<html>" + StringEscapeUtils.escapeHtml4(bad) + "</html>";
	     return html;
	}
}
