package graph;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a wikipedia category.
 **/
public class Category
{
	String name;
	int article_count;
	Set<Page> pages;
	
	/**
	 * Basic Constructor for a Category
	 * @param name the name of this Category
	 * @param count the number of articles in this Category
	 */
	public Category(String name, int count)
	{
		this.name = name.replace("'", "''");
		this.article_count = count;
	}
	
	
	/////////////////////////
	// Data Retrieval
	/////////////////////////
	
	/**
	 * Queries the database to retrieve all pages in this category.
	 */
	private void retrievePages() {
		String query =	"SELECT p.page_id as id " +
						"FROM filtered_page AS p, filtered_categorylinks AS cl " +
						"WHERE cl.cl_from=p.page_id AND cl.cl_to='" + name + "' limit 1000;" ;
		Connection c = DBManager.getConnection();
		ResultSet rs = DBManager.execute(c, query);
		
		if (rs != null)
		{
			pages = new HashSet<Page>();
			
			try 
			{
				while(rs.next())
				{
					pages.add( new Page(rs.getInt("id")) );
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
	 * Getter for name
	 * 
	 * @return the name of this Category
	 */
	public String getName()
	{
		return name;
	}
	
	
	/**
	 * Getter for the article count.
	 * @return The number of articles in this category.
	 */
	public int getArticleCount()
	{
		return article_count;
	}
	
	
	/**
	 * Getter for the set of pages in this category.
	 * @return A set object with all pages in this category.
	 */
	public Set<Page> getPages()
	{
		if (pages == null)
		{
			retrievePages();
		}
		return pages;
	}
	
	
	/////////////////////////
	// Overrides
	/////////////////////////
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Category)
		{
			Category c = (Category) obj;
			return this.name.equals(c.name);
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
}
