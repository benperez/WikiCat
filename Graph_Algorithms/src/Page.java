import java.util.HashMap;
import java.util.HashSet;


/**
 * Class for a Page, or Article, in Wikipedia
 * 
 * 
 * 
 * 
 **/
public class Page 
{
	HashMap<Page,Double> TransitionProbabilities;
	HashSet<Category> Categories;
	
	public Page(HashSet<Page> outgoingLinks, HashSet<Category> Categories)
	{
		this.TransitionProbabilities = makeTransitionProbabilities(outgoingLinks);
		this.Categories = Categories;
	}
	
	private HashMap<Page,Double> makeTransitionProbabilities(HashSet<Page> outgoingLinks)
	{
		HashMap<Page, Double> probabilities = new HashMap<Page, Double>();
		//For now, just create a uniform distribution over outgoing links
		Double uniform = new Double(1.0/outgoingLinks.size());
		for (Page p : outgoingLinks)
		{
			probabilities.put(p, uniform);
		}
		return probabilities;
	}
	
	public HashMap<Page,Double> getTransitionProbabilites()
	{
		return TransitionProbabilities;
	}
	
	public HashSet<Category> getCategories()
	{
		return Categories;
	}
}
