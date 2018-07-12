package week04;

/**
 * This program breaks a string into individual words. It then counts the number of occurrences of each word.
 * @author George Harrington
 *
 */
public class WordCountResult {
	private int m_count;
	private String m_word;
	
	/**
	 * Returns the number of occurrences of the word.
	 * @return the number of occurrences of the word as an int.
	 */
	public int getCount() {
		return m_count;
	}
	
	/**
	 * Returns the word being analyzed.
	 * @return a string containing the word being analyzed.
	 */
	public String getWord() {
		return m_word;
	}
	
	/**
	 * Increments the occurrence count.
	 */
	public void incrementCount() {
		m_count += 1;
	}
	
	/**
	 * Used to set the occurrence count.
	 * @param a incoming value that the occurrence count will be set to.
	 */
	public void setCount(int a) {
		m_count = a;
	}
	
	/**
	 * Used to set the word. Replaces the existing word.
	 * @param a incoming string that replaces the value stored in m_word.
	 */
	public void setWord(String a) {
		m_word = a;
	}
	
	/**
	 * Custom string that generates the following text: "count <tab> word"
	 */
	public String toString() {
		return String.format("%d\\t%s", m_count, m_word);
	}
	
	/**
	 * Constructor that initializes an instance.
	 * @param a Incoming integer that represents the count.
	 * @param b Incoming integer that represents the string.
	 */
	public WordCountResult(int a, String b) {
		m_count = a;
		m_word = b;
	}

}
