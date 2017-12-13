package com.rohit.javabrains.messenger.util;

import java.sql.*;

public class DatabaseConnection {
	public static void main(String args[]){  
		try{
			System.out.println("Connecting server...");
			Connection con = DatabaseConnection.getConnection("sonoo");
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			boolean bol=stmt.execute("insert into emp values(33,'Irfan rohit',21);");
			stmt.close();
			Statement stmt2 = con.createStatement();
			ResultSet rs = stmt2.executeQuery("select * from emp;");
			System.out.println("Connecting server...Done : "+bol);
			while(rs.next())  
				System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
			con.close();  
		}catch(Exception e){ 
			System.out.println(e);
		}  
	}
	
	public static Connection getConnection(String database) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection(
					"jdbc:mysql://"+LabelUtils.DATABASE_URL+database,"root","root");
			return con;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ResultSet getAllRecords(String table_name, String where_clause) {
		try {
			Connection con = DatabaseConnection.getConnection(LabelUtils.DATABASE_NAME);
			
			String query = "SELECT * FROM " + table_name ;
			if(where_clause != "") {
				query += " WHERE "+where_clause;
			}
			query += ";";
			
			Statement statement = con.createStatement();			
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}		
	}
	
	public static boolean executeRecord(String query){
		try {
			Connection con = DatabaseConnection.getConnection(LabelUtils.DATABASE_NAME);
			
			Statement statement = con.createStatement();
			return statement.execute(query);
		}catch (Exception e)
	    {
	      System.err.println("Got an exception!");
	      System.err.println(e.getMessage());
	    }
		return false;
		
	}
	
	public static int execute(String query) throws SQLException {

		Connection con = DatabaseConnection.getConnection(LabelUtils.DATABASE_NAME);
		
		Statement statement = con.createStatement();
		statement.execute(query);
		ResultSet generatedKeys = statement.getGeneratedKeys();
		if(generatedKeys.next()) {
			return (int)generatedKeys.getLong(1);
		}			
			
		return 0;
	}

}
