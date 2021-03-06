// @author A0111866E

/**
 * This class reads from quotes.txt and stores the content into an array list.
 * It provides a function which returns a random quote from the array list.
 */

package Motivator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Motivator implements InterfaceForMotivator {
	
	private ArrayList<String> _quotes;
	
	public static void main(String[] args) {
		Motivator motivator = new Motivator();
		motivator.initialise();
//		System.out.println(motivator.getRandomQuotes());
	}

	/**
	 * Default constructor with initialise method call
	 */
	public Motivator() {
		initialise();
	}

	@Override
	public String getRandomQuotes() {
		int randomInt = generateRandomInteger();
		return getQuoteFromIndex(randomInt);
	}

	/**
	 * @param randomInt
	 * @return quote from index
	 */
	private String getQuoteFromIndex(int randomInt) {
		return _quotes.get(randomInt);
	}

	/**
	 * @return a random integer
	 */
	private int generateRandomInteger() {
		//note a single Random object is reused here
	    Random randomGenerator = new Random();
	    int randomInt = randomGenerator.nextInt(this._quotes.size() - 1);
		return randomInt;
	}
	
	/**
	 * initialise quotes list
	 */
	private void initialise() {
		this._quotes = new ArrayList<String>();
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
			    	_quotes.add(line);
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
		return _quotes;
	}

	/**
	 * @param quotes the quotes to set
	 */
	public void setQuotes(ArrayList<String> quotes) {
		this._quotes = quotes;
	}

}
