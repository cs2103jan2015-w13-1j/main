package Common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class Motivator {
	
	private ArrayList<String> quotes;
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
			for (String line : Files.readAllLines(Paths.get("tables/quotes.txt"))) {
				quotes.add(line);
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
