package graph;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBManager 
{

	//Database Authentication
	private static final String DB_ADDRESS = "jdbc:mysql://localhost/wiki_category";
	private static final String DB_USER = "feoc";
	private static final String DB_PASS = "wikicat";


	//A connection to the database
	private static Connection connection;


	//Static initializer for JDBC connection
	static 
	{
		//Initialize the driver
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("MySQL JDBC Driver registered!");
		} catch (ClassNotFoundException e)
		{
			System.err.println("Cannot find the MySQL JDBC Driver!");
			e.printStackTrace();
		}

		//Get a connection to the database
		connection = null;
		try
		{
			connection = DriverManager.getConnection(DB_ADDRESS, DB_USER, DB_PASS);
			//System.out.println("Successfully connected to database!");
		} catch (SQLException e)
		{
			System.err.println("Error connection to database!");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Executes the given query string on the DB returning either a ResultSet or null if there was an error.
	 * NOTE: Remember to close your result sets!
	 * @param query The mySQL query string to execute
	 * @return A ResultSet object if the query was successful, or null.
	 */
	public static ResultSet query(String query) {
		try
		{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (SQLException e)
		{
			System.err.println("Error executing query!");
			e.printStackTrace();
		}
		return null;
	}
	
}
