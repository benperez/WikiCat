package graph;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;


public class DBManager 
{

	//Database Authentication
	private static final String DB_ADDRESS = "jdbc:mysql://localhost/wiki_category";
	private static final String DB_USER = "feoc";
	private static final String DB_PASS = "wikicat";


	//The pool datasource
	private static DataSource ds;


	//Static initializer for JDBC connection
	static 
	{
		//Setup the Tomcat Connection Pool with reasonable defaults
		PoolProperties p = new PoolProperties();
		 p.setUrl(DB_ADDRESS);
         p.setDriverClassName("com.mysql.jdbc.Driver");
         p.setUsername(DB_USER);
         p.setPassword(DB_PASS);
         p.setJmxEnabled(true);
         p.setTestWhileIdle(false);
         p.setTestOnBorrow(true);
         p.setValidationQuery("SELECT 1");
         p.setTestOnReturn(false);
         p.setValidationInterval(30000);
         p.setTimeBetweenEvictionRunsMillis(30000);
         p.setMaxActive(100);
         p.setInitialSize(10);
         p.setMaxWait(10000);
         p.setRemoveAbandonedTimeout(60);
         p.setMinEvictableIdleTimeMillis(30000);
         p.setMinIdle(10);
         p.setLogAbandoned(true);
         p.setRemoveAbandoned(true);
         p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
           "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
         
         //Create the pooled datasource with our specified properties
         ds = new DataSource();
         ds.setPoolProperties(p); 
	}
	
	
	/**
	 * Retrieves a pooled connection object to run queries on.
	 * @return An open Connection object connected to the database.
	 */
	public static Connection getConnection()
	{
		Connection c = null;
		try
		{
			c = ds.getConnection();
		} catch (SQLException e)
		{
			System.err.println("Error executing query!");
			e.printStackTrace();
		}
		return c;
	}
	
	
	/**
	 * Executes the given query string on the DB returning either a ResultSet or null if there was an error.
	 * NOTE: Remember to close your result sets and connections!
	 * @param conn An open connection object to run the query on.
	 * @param query The mySQL query string to execute
	 * @return A ResultSet object if the query was successful, or null.
	 */
	public static ResultSet execute(Connection conn, String query, boolean isUpdate)
	{
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			if (isUpdate)
				stmt.executeUpdate(query);
			else
				rs = stmt.executeQuery(query);
			return rs;
		} catch (SQLException e)
		{
			System.err.println("Error executing query!");
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Simple overload for execute to preserve backwards-compatibility.
	 */
	public static ResultSet execute(Connection conn, String query)
	{
		return execute(conn, query, false);
	}
	
	
	/**
	 * Handles closing connections and their result sets object.
	 * @param conn The Connection to close
	 * @param rs The ResultSet corresponding to the connection to close also.
	 */
	public static void closeConnection(Connection conn, ResultSet rs)
	{
		try
		{
			if (conn != null)
			{
				conn.close();
			}
			if (rs != null)
			{
				rs.close();
			}
		} catch (SQLException e)
		{
			System.err.println("Error closing connection!");
			e.printStackTrace();
		}
	}
	
}
