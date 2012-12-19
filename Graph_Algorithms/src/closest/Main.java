package closest;

import graph.Category;
import graph.Page;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
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
		System.out.println("Root Page ("+root.pageId+"): "+root.getName());
		
		//We are expanding breadth first, so this:
		Set<Page> visited = new HashSet<Page>();
		Queue<Page> frontier = new LinkedList<Page>();
		frontier.add(root);
		while (foundCats.size()<maxCats && frontier.size()!=0)
		{
			Page p = frontier.poll();
			
			//Ignore visited pages
			if (!visited.contains(p))
			{
				//Add this pages links to the frontier
				frontier.addAll( p.getOutLinks() );
				
				//Add any _new_ categories to: The List
				Set<Category> cats = p.getCategories();
				cats.removeAll(root.getCategories());
				foundCats.addAll(cats);
				
				//We are done visiting this page
				visited.add(p);
			}
		}
		
		//Now just print our resulting categories
		for (Category c : foundCats) {
			System.out.println(c.getName());
		}
		
	}

}
