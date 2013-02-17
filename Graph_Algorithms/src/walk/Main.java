package walk;

import graph.Page;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
			System.out.println("\tjava -jar main.jar [NO. PRODUCERS] [NO. CONSUMERS] [NO. SAMPLES PER CATEGORY]");
			return;
		}
		else if (args.length != 3)
		{
			System.out.println("Wrong number of arguments!");
			return;
		}
		
		//Parse arguments
		int NUM_PRODUCERS = Integer.parseInt(args[0]);
		int NUM_CONSUMERS = Integer.parseInt(args[1]);
		int N_SAMPLES = Integer.parseInt(args[2]);
		
		//Create a blocking queue for the producers and consumers
		BlockingQueue<Page> queue = new LinkedBlockingQueue<Page>();
		
		///Create a number of producers and consumers and start them
		Thread[] producers = new Thread[NUM_PRODUCERS];
		Thread[] consumers = new Thread[NUM_CONSUMERS];
		
		for (int i=0; i<NUM_PRODUCERS; i++)
		{
			producers[i] = new Thread(new QueueFiller(queue));
			producers[i].start();
		}
		
		for (int i=0; i<NUM_CONSUMERS; i++)
		{
			consumers[i] = new Thread(new Walker(queue, N_SAMPLES));
			consumers[i].start();
		}
		
	}
	
}
