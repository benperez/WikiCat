import java.util.HashSet;

/**
 * Page Manager, links the SQL database to our java framework
 * 
 * Works by lazy loading pages from the database only when requested. Additionally, pages are cached and never loaded a second time.
 */
public class PageManager 
{
	//A "cache" for Pages that have already been loaded
	static HashSet<Page> loadedPages;
	
	//Static initialization block, going to want to open a connection to the database
	static 
	{
		//Initialize the JDBC connector
		//TODO
		
		//Instantiate the empty set of pages
		loadedPages = new HashSet<Page>();
	}
	
	/**
	 * Query a page from the database
	 * 
	 * @return a page object associated with the provided key
	 */
	public Page getPage()
	{
		return null;
	}
	
}
