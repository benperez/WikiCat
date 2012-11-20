import java.util.HashSet;

/**
 * Page Manager, links the SQL database to our java framework
 * 
 * Works by lazy loading pages from the database only when requested. Additionally, pages are cached and never loaded a second time.
 */
public class PageManager 
{
	//A "cache for"
	static HashSet<Page> loadedPages;
	
	static 
	{
		//Initialize the JDBC connector
		//TODO
		
		//Instantiate the empty set of pages
		loadedPages = new HashSet<Page>();
	}
	
	
}
