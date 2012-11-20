import java.util.HashMap;
import java.util.HashSet;


/**
 * Class for a Page, or Article, in Wikipedia
 * 
 * Page objects are retrieved through the PageManager
 * 
 **/
public class Page 
{
	HashMap<Page,Double> TransitionProbabilities;
	HashSet<Category> Categories;
	
	/**
	 * Constructor for a page, maybe called by Page Manager
	 * 
	 * @param outgoingLinks All hyperlinks to other Wikipedia Pages
	 * @param Categories All categories which apply to this specific page
	 */
	public Page(HashSet<Page> outgoingLinks, HashSet<Category> Categories)
	{
		//For now, just create a uniform distribution over outgoing links
		this.TransitionProbabilities = makeUniformProbabilities(outgoingLinks);
		this.Categories = Categories;
	}
	
	
	/**
	 * Create a uniform probability distribution over the given pages
	 * 
	 * @param outgoingLinks The set of all pages linked to
	 * @return a mapping of N hyperlinked pages each with 1/N probability
	 */
	private HashMap<Page,Double> makeUniformProbabilities(HashSet<Page> outgoingLinks)
	{
		HashMap<Page, Double> probabilities = new HashMap<Page, Double>();
		Double uniform = new Double(1.0/outgoingLinks.size());
		for (Page p : outgoingLinks)
		{
			probabilities.put(p, uniform);
		}
		return probabilities;
	}
	
	/**
	 * Getter method for transition probabilities of this page
	 * 
	 * @return map of Page to it's associated transition probability
	 */
	public HashMap<Page,Double> getTransitionProbabilites()
	{
		return TransitionProbabilities;
	}
	
	/**
	 * Getter method for categories of this page
	 * 
	 * @return set of Category labels associated with this page
	 */
	public HashSet<Category> getCategories()
	{
		return Categories;
	}
}
