package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		String result = null;
		try{
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM t1");
			PreparedStatement stmt1;
			ResultSet rs = stmt.executeQuery();
			while (result == null && (rs.next())){
				if (text.toLowerCase().contains(rs.getString(2).toLowerCase())) {result = rs.getString(3);
	int temp = rs.getInt(4);			
	String str = "UPDATE t1 SET Hits = ";
	str += (temp + 1);
	str += "WHERE id = '";
	str += rs.getInt(1);
	str += "';";
	stmt1 = connection.prepareStatement(str);
	stmt1.executeQuery();
	result += (temp+1);
	}
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (URISyntaxException e) {
			log.info("IOException: {}", e.toString());
		}	
		finally{
			if (result != null) return result;		
		}
		throw new Exception("NOT FOUND");
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
