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

	//Database_Configuration 
