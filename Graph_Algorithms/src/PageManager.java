import java.util.HashSet;

/**
 * Page Manager, links the SQL database to our java framework
 * 
 * TODO--- need to figure out how this is going to work. ideally want some sort of lazy loading of pages
 */
public class PageManager 
{
	//A "cache" for Pages that have already been loaded
	static HashSet<Page> loadedPages;
	
	//Static initialization block, going to want to open a connection to the database
	static 
	{
		;
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
