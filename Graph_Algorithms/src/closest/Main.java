package closest;

import graph.Category;
import graph.Page;

import java.util.HashSet;
import java.util.Set;

public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		if (args.length != 2)
		{
			System.out.println("main usage: ");
			System.out.println("\tjava -jar closest.jar [ID of start page] [max categories]");
			return;
		}
		
		//Parse command line arguments.
		int rootID, maxCats;
		try
		{
			rootID  = Integer.parseInt(args[0]);
			maxCats = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException e)
		{
			System.err.println("Error parsing integer arguments!");
			return;
		}
		
		//Set up the initial state
		Set<Category> foundCats = new HashSet<Category>();
		
		Page root = new Page(rootID);
		
		//We are expanding breadth first, so this:
		Set<Page> visited = new HashSet<Page>();
		Set<Page> frontier = new HashSet<Page>();
		frontier.add(root);
		visited.add(root);
		while (foundCats.size()<maxCats)
		{
			Set<Page> nextFrontier = new HashSet<Page>();
			for (Page p : frontier)
			{
				//Populate the next frontier from this one (And mark them as visited).
				if (!visited.contains(p))
				{
					nextFrontier.addAll( p.getOutLinks() );
					visited.addAll( p.getOutLinks() );
				}
				
				//Add any new categories to The List
				foundCats.addAll( p.getCategories() );
			}
			frontier = nextFrontier;
		}
		
		//Now just print our resulting categories
		for (Category c : foundCats) {
			System.out.println(c.getName());
		}
		
	}

}
