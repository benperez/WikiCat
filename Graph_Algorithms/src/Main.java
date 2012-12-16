import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class for running tests using our random walk framework
 * 
 * For now, just write the tests directly into the main method
 */
public class Main
{
	
	/**
	 * What we want to run and print to test different algorithms
	 * 
	 * @param args command line input
	 */
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.out.println("main usage: ");
			System.out.println("\tjava -jar main.java [ID of start page] [NUM walks]");
		}
		else if (args.length != 2)
		{
			System.out.println("More ARGS Please!");
			return;
		}
		
			
		//Initialize this Jackson
		int sourceID = Integer.parseInt(args[0]);
		Page source = new Page(sourceID);
		
		Map<Category, Double> counts = new HashMap<Category, Double>();
		Walker jackson = new Walker(source);
		Set<Category> walk1;
		Set<Category> walk2;
		Set<Category> walk3;
		//Repeat 10 times at each depth
		for (int i=0; i<Integer.parseInt(args[1]) ; i++)
		{
			//Do a walk of depth 1,2,3,...
			walk1 = jackson.walk(1);
			walk2 = jackson.walk(2);
			walk3 = jackson.walk(3);
			
			//Increment the counts
			for (Category c : walk1)
			{
				if (counts.containsKey(c))
					counts.put(c, counts.get(c)+1.0);
				else
					counts.put(c, 1.0);
			}
			for (Category c : walk2)
			{
				if (counts.containsKey(c))
					counts.put(c, counts.get(c)+0.5);
				else
					counts.put(c, 0.5);
			}
			for (Category c : walk3)
			{
				if (counts.containsKey(c))
					counts.put(c, counts.get(c)+1.0/3.0);
				else
					counts.put(c, 1.0/3.0);
			}
		}
		double n = 0.0;
		for (Category c : counts.keySet())
		{
			n += counts.get(c);
		}
		
		for (Category c : counts.keySet())
		{
			System.out.println(c.getName()+": "+( (double) counts.get(c))/n);
		}
	}
	
}
