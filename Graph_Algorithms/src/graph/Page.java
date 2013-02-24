package graph;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


/**
 * Class for a Page, or Article, in Wikipedia
 **/
public class Page 
{
	public int pageId;
	String pageName;
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
	 * Queries the database for this page's title.
	 */
	private void retrievePageName()
	{
		String query = "SELECT p.page_title as name FROM filtered_page as p WHERE p.page_id="+pageId+";";
		Connection c = DBManager.getConnection();
		ResultSet rs = DBManager.execute(c, query);
		if (rs==null)
		{
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
		String query =	"SELECT c.cat_title as cat, c.cat_pages as count " +
				"FROM filtered_categorylinks as cl, filtered_category as c " +
				"WHERE cl.cl_from = "+Integer.toString(pageId) + " " +
				"AND c.cat_hidden=0 AND c.cat_title=cl.cl_to;";
		Connection c = DBManager.getConnection();
		ResultSet rs = DBManager.execute(c, query);
		
		Categories = new HashSet<Category>();
		
		if (rs != null)
		{
			try 
			{
				while(rs.next())
				{
					String catName = rs.getString("cat");
					int catCount = rs.getInt("count");
					Category temp = new Category(catName, catCount);
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