package TestConfig;

import java.sql.*;


import java.sql.DriverManager;
import java.sql.SQLException;

public class Database_Connection {
	public  String host ;
	public  String user ;
	public  String password ;
	public ResultSet resultSet;
	public Connection connection;
	public Statement statement;

	public void OTP_Database_Configuration (String environment){
		switch (environment) {
			case "BETA":
				host = "";
				user = "qc_testing";
				password = "";
				break;
			case "DEMO":
				host = "";
				user = "";
				password = "";
				break;
			case "LIVE":
				host = "";
				user = "";
				password = "";
				break;
			default:
				throw new IllegalArgumentException("Invalid environment: " + environment);
		}
	}

	public void NagwaClasses_Database_Configuration (String environment){
		switch (environment) {
			case "BETA":
				host = "";
				user = "";
				password = "";
				break;
			case "DEMO":
				host = "";
				user = "testing";
				password = "";
				break;
			case "LIVE":
				host = "";
				user = "testing";
				password = "";
				break;
			default:
				throw new IllegalArgumentException("Invalid environment: " + environment);
		}
	}
	public ResultSet connect_to_database (String query) {
		NagwaClasses_Database_Configuration(EnvironmentSetup.env);
		try {
			connection = DriverManager.getConnection(host, user, password);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public ResultSet Connect_to_OTP_Database (String query){
		OTP_Database_Configuration(EnvironmentSetup.env);
		try {
			connection = DriverManager.getConnection(host, user, password);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

}
