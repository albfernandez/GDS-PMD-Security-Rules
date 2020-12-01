package resources.others;

import java.sql.Connection;
import java.sql.SQLException;

public class Bug46SinkInConditional {

	// Should fire a violation
	public void badMethod2(String badParam, Connection con) throws SQLException {
		if (con.createStatement().executeQuery("select * from table where field='" + badParam + "'").next()) {
			return;
		}
	}
}
