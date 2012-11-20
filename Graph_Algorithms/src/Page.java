import java.util.HashMap;
import java.util.HashSet;


/**
 * Class for a Page, or Article, in Wikipedia
 * 
 * 
 * 
 * 
 **/
public class Page {
	HashMap<Page,Double> TransitionProbabilities;
	HashSet<Category> Categories;
	
	public Page(HashMap<Page,Double> TransitionProbabilities, HashSet<Category> Categories)
	{
		this.TransitionProbabilities = TransitionProbabilities;
		this.Categories = Categories;
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
