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
		//First, load up a set of pages that have Category Abstract_strategy_games
		int dogID = 0;
		Page dog = new Page(dogID);
		
		Map<Category, Integer> counts = new HashMap<Category, Integer>();
		Set<Category> walk1;
		Set<Category> walk2;
		Set<Category> walk3;
		//Repeat 10 times at each depth
		for (int i=0; i<10; i++)
		{
			//Do a walk of depth 1,2,3,...
			
		}
	}
	
}
