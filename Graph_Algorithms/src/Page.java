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
	int pageId;
	HashMap<Page,Double> TransitionProbabilities;
	HashSet<Category> Categories;
	
	/**
	 * Constructor for a page, maybe called by Page Manager
	 * 
	 * @param pageId the row-key ID associated with this page
	 */
	public Page(int pageId)
	{
		this.pageId = pageId;
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
		//If the TransitionProbabilies for this page have not yet been loaded, then query them
		if (TransitionProbabilities == null)
		{
			//TODO - load shit from PageManager
			String query = "Select ";
			
			return TransitionProbabilities;
		} else
		{
			return TransitionProbabilities;
		}
	}
	
	/**
	 * Getter method for categories of this page
	 * 
	 * @return set of Category labels associated with this page
	 */
	public HashSet<Category> getCategories()
	{
		//Load Categories if needed
		if (Categories == null)
		{
			//Todo = load shit from PageManager
			String query = "Select cl_from from categorylinks where";
			return Categories;
		} else
		{
			return Categories;
		}	
	}
}
