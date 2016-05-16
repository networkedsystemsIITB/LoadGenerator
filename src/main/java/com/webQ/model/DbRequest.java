package com.webQ.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import co.paralleluniverse.fibers.SuspendExecution;

import com.webQ.interfaces.Feature;

public class DbRequest implements  Serializable { 


private String Query;
private BasicDataSource connectionPool;

public String getQuery() {
	return Query;
}

public void setQuery(String query) {
	Query = query;
}

public BasicDataSource getConnectionPool() {
	return connectionPool;
}

public void setConnectionPool(BasicDataSource connectionPool) {
	this.connectionPool = connectionPool;
}



public void execute() throws InterruptedException,
		SuspendExecution, SQLException {
	// TODO Auto-generated method stub
	//System.out.println(this.Query);
	String values[] = this.Query.split(" ", 2);
	int type;
	if (values[0].equalsIgnoreCase("create")
			|| values[0].equalsIgnoreCase("update")
			|| values[0].equalsIgnoreCase("delete"))
		type = 1;
	else
		type = 0;
	//System.out.println(this.Query);
	
	Connection connection = connectionPool.getConnection();
	Connection dbCon = null;
	Statement stmt = null;
	ResultSet rs = null;
	try{
	stmt = connection.createStatement();
	if (type == 1) {
		stmt.executeUpdate(this.Query);
	} else {
		rs = stmt.executeQuery(this.Query);
		//while (rs.next()) {
			//System.out.println(rs.getString("ip"));
		//}
	}
	}catch (SQLException ex) {
		System.out.println("SQL Connection Error");
	} finally {

		try {
			if (rs != null)
				rs.close();
			if (dbCon != null)
				dbCon.close();
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
}
