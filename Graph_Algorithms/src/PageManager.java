import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Page Manager, links the SQL database to our java framework
 * 
 * Works by lazy loading pages from the database only when requested. Additionally, pages are cached and never loaded a second time.
 */
public class PageManager 
{
	
	//Database Authentication
	private static final String DB_ADDRESS = "jdbc:mysql://localhost/wiki_category";
	private static final String DB_USER = "cfeo";
	private static final String DB_PASS = "wikicat";
	
	
	//The cache of already-loaded pages
	private static Map<Integer,Page> loadedPages;
	
	//A connection to the database
	private static Connection connection;
	
	
	//Static initializer for JDBC connection
	static 
	{
		//Initialize the driver
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e)
		{
			System.err.println("Cannot find the MySQL JDBC Driver!");
			e.printStackTrace();
		}
		System.out.println("MySQL JDBC Driver registered!");
		
		//Get a connection to the database
		connection = null;
		try
		{
			connection = DriverManager.getConnection(DB_ADDRESS, DB_USER, DB_PASS);
		} catch (SQLException e)
		{
			System.err.println("Error connection to database!");
			e.printStackTrace();
		}
		System.out.println("Successfully connected to database!");
		
		//Instantiate the empty set of pages
		loadedPages = new HashMap<Integer,Page>();
	}
	
	
	/**
	 * Retrieves a set of all pages cross-linked from the provided page.
	 * @param page The Page to get outgoing neighbors for.
	 */
	public static Set<Page> getOutgoingLinks(Page page)
	{
		//TODO
		return null;
	}
	
	
	/**
	 * Retrieves a Page object by id either from the cache or the database.
	 * @param id The integer id of the Page object to retrieve.
	 * @return The Page object represented by the given id
	 */
	private static Page getPageFromId(int id) {
		//See if we have the page cached
		Page page = loadedPages.get(id);
		if (page!=null)
		{
			return page;
		}
		//Otherwise, go ahead and build it then cache it
		page = new Page(id);
		loadedPages.put(id, page);
		return page;
	}
	
	
	/**
	 * Executes the given query string on the DB returning either a ResultSet or null if there was an error.
	 * NOTE: Remember to close your result sets!
	 * @param query The mySQL query string to execute
	 * @return A ResultSet object if the query was successful, or null.
	 */
	private static ResultSet executeQuery(String query) {
		try
		{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT count(page_id) FROM page;");
			rs.first();
			return rs;
		} catch (SQLException e)
		{
			System.err.println("Error executing query!");
			e.printStackTrace();
		}
		return null;
	}
	
	
}
