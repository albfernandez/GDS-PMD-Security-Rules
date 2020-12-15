package resources.cwe22pathtraversal;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;

public class PathTraversalExample {

	public void bad(HttpServletRequest request) {
		String fileName = request.getParameter("name");
		new File("/base/path/" + fileName);
	}
	
	public void bad2(String param) {
		File f = new File(param);
		f.getAbsolutePath();
	}
	
	
	public void bad3(HttpServletRequest request) {
		String fileName = request.getParameter("name");
		new File("/base/path", fileName);
	}
	
	public void bad4(String param) {
		File f = new File("/base/path/", param);
		f.getAbsolutePath();
	}
	
	public void bad5(String param) {
		Path p =  Paths.get(param);
		p.toFile().getAbsolutePath();
	}
	
	public void ok(String param) {
		File f = new File("/base/path", FilenameUtils.getName(param));
		f.getAbsolutePath();
	}
}
