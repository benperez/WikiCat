package graph;

/**
 * 
 * 
 * 
 **/
public class Category
{
	String name;
	
	/**
	 * Basic Constructor for a Category
	 * 
	 * @param name the name of this Category
	 */
	public Category(String name)
	{
		this.name = name;
	}
	
	/**
	 * Getter for name
	 * 
	 * @return the name of this Category
	 */
	public String getName()
	{
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Category) {
			Category c = (Category) obj;
			return this.name.equals(c.name);
		}
		return false;
	}
}
