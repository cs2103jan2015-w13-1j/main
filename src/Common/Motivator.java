/**
 * @author Esmond
 *
 */

package Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Motivator {
	
	private ArrayList<String> quotes;
	public static void main(String[] args) {
		Motivator motivator = new Motivator();
		motivator.initialise();
		System.out.println(motivator.getRandomQuotes());
	}

	/**
	 * Default constructor with initialise method call
	 */
	public Motivator() {
		initialise();
	}
	
	/**
	 * @return a random quote
	 */
	public String getRandomQuotes() {
		//note a single Random object is reused here
	    Random randomGenerator = new Random();
	    int randomInt = randomGenerator.nextInt(this.quotes.size() - 1);
		return quotes.get(randomInt);
	}
	
	/**
	 * initialise quotes list
	 */
	private void initialise() {
		this.quotes = new ArrayList<String>();
		copyQuotesFromFile();
	}
	
	/**
	 * Copy Quotes from file
	 */
	private void copyQuotesFromFile() {
		try {
			InputStream is = this.getClass().getResourceAsStream("/quotes.txt");
			try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			       // process the line.
			    	quotes.add(line);
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the quotes
	 */
	public ArrayList<String> getQuotes() {
		return quotes;
	}

	/**
	 * @param quotes the quotes to set
	 */
	public void setQuotes(ArrayList<String> quotes) {
		this.quotes = quotes;
	}

}
