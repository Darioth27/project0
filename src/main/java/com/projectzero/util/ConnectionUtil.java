package com.projectzero.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionUtil {
	
	public static Connection getConnection() throws SQLException {
		String url = "jdbc:oracle:thin:@database-1.cxcskd01f2om.us-east-1.rds.amazonaws.com:1521:ORCL";
		String username = "admin";
		String password = "password";
		
		return DriverManager.getConnection(url, username, password);
	}

	
}