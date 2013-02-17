package graph;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Class for a Page, or Article, in Wikipedia
 **/
public class Page 
{
	public int pageId;
	String pageName;
	Set<Page> OutgoingLinks;
	Map<Page,Double> TransitionProbabilities;
	Set<Category> Categories;
	
	/**
	 * Constructor for a page, maybe called by Page Manager
	 * 
	 * @param pageId the row-key ID associated with this page
	 */
	public Page(int pageId)
	{
		this.pageId = pageId;
	}
	
	
	/////////////////////////
	// Data Retrieval
	/////////////////////////
	
	/**
	 * Create a uniform probability distribution over the given pages
	 * 
	 * @param outgoingLinks The set of all pages linked to
	 * @return a mapping of N hyperlinked pages each with 1/N probability
	 */
	private void makeUniformProbabilities(Set<Page> outgoingLinks)
	{
		TransitionProbabilities = new HashMap<Page, Double>();
		Double uniform = new Double(1.0/outgoingLinks.size());
		for (Page p : outgoingLinks)
		{
			TransitionProbabilities.put(p, uniform);
		}
	}
	
	
	/**
	 * Handles querying the database for pages which share a category with this one.
	 */
	private void retrievePageLinks()
	{
		//Run a query to get the page ids linked to by the given one
		//TODO Do some more sanity checking to make sure we're getting good pages
		String query =	"SELECT a.page_id AS id " +
						"FROM articles AS a, categorylinks AS c1, categorylinks AS c2 " +
						"WHERE c1.cl_to=c2.cl_to AND " +
						"c2.cl_from=a.page_id AND " +
						"c1.cl_from=" + pageId + ";";
		Connection c = DBManager.getConnection();
		ResultSet rs = DBManager.execute(c, query);
		if (rs==null){
			OutgoingLinks = null;
		}
		
		//Populate the return set of pages
		Set<Page> pages = new HashSet<Page>();
		try
		{
			while(rs.next())
			{
				pages.add( new Page(rs.getInt("id")) );
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			OutgoingLinks = null;
		}
		
		//Release the connection back to the pool.
		DBManager.closeConnection(c, rs);
		
		OutgoingLinks = pages;
	}
	
	
	/**
	 * Queries the database for this page's title.
	 */
	private void retrievePageName()
	{
		String query = "SELECT P.page_title as name FROM page as P WHERE P.page_id="+pageId+";";
		Connection c = DBManager.getConnection();
		ResultSet rs = DBManager.execute(c, query);
		if (rs==null){
			pageName = null;
		}
		
		try
		{
			rs.next();
			pageName = rs.getString("name");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			pageName = null;
		}
		
		//Release the connection back to the pool.
		DBManager.closeConnection(c, rs);
	}
	
	
	/**
	 * Retrieves the set of categories for this page.
	 */
	private void retrieveCategories()
	{
		String query =	"SELECT C.cat_title as Cat " +
				"FROM categorylinks as CL, category as C " +
				"WHERE CL.cl_from = "+Integer.toString(pageId) + " " +
				"AND C.cat_hidden=0 AND C.cat_title=CL.cl_to;";
		Connection c = DBManager.getConnection();
		ResultSet rs = DBManager.execute(c, query);
		
		Categories = new HashSet<Category>();
		
		if (rs != null)
		{
			try 
			{
				while(rs.next())
				{
					String catName = rs.getString("Cat");
					Category temp = new Category(catName);
					Categories.add(temp);
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
		//Release the connection back to the pool.
		DBManager.closeConnection(c, rs);
	}
	
	/////////////////////////
	// Getter Methods
	/////////////////////////
	
	/**
	 * Getter method for transition probabilities of this page
	 * 
	 * @return map of Page to its associated transition probability
	 */
	public Map<Page,Double> getTransitionProbabilites()
	{
		if (TransitionProbabilities == null)
		{
			//For now, just using uniform probabilities across all outgoing links
			makeUniformProbabilities( getOutLinks() );
		}
		
		return TransitionProbabilities;
	}
	
	
	/**
	 * Getter method for outgoing links from this page.
	 * 
	 * @return A set of Pages linked to by this page.
	 */
	public Set<Page> getOutLinks()
	{
		if (OutgoingLinks == null)
		{
			retrievePageLinks();
		}
		
		return OutgoingLinks;
	}
	
	
	/**
	 * Getter for the page title.
	 * 
	 * @return The String name of this page.
	 */
	public String getName()
	{
		if (pageName == null)
		{
			retrievePageName();
		}
		
		return pageName;
	}
	
	/**
	 * Getter method for categories of this page
	 * 
	 * @return set of Category labels associated with this page
	 */
	public Set<Category> getCategories()
	{
		if (Categories == null)
		{
			 retrieveCategories();
		}
		
		return Categories;
	}
}
