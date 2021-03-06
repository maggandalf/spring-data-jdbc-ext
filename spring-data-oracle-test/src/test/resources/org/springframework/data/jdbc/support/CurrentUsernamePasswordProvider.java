package org.springframework.data.sql;

import org.springframework.data.sql.jdbc.support.ConnectionUsernamePasswordProvider;

/**
 * @author Thomas Risberg
 */
public class CurrentUsernamePasswordProvider implements ConnectionUsernamePasswordProvider {

	String userName = "SPRING";
	String password = "SPRING";

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	@Override
	public String getPassword() {
		return password;
	}

}
