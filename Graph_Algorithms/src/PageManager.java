import java.util.HashSet;

/**
 * Page Manager, links the SQL database to our java framework
 * 
 * TODO--- need to figure out how this is going to work. ideally want some sort of lazy loading of pages
 */
public class PageManager 
{
	//A "cache for"
	static HashSet<Page> loadedPages;
	
	static 
	{
		;
	}
	
	
}
