package week01;

import java.util.ArrayList;
/**
 * This class handles an array list.
 * @author George Harrington
 *
 * @param <T> This is the array list that stores the data.
 */
public class GenericStack<T> {
	private ArrayList<T> m_list = new ArrayList<T>();	
	/**
	 * This method pushes data to the array.
	 * @param T This is the array list that stores the data.
	 */
	public void push(T T) {
		m_list.add(T);
	}
	/**
	 * This method gets the size of the list.
	 * @return the size of the list as an int.
	 */
	public int getSize() {
		return m_list.size();
		
	}
	/**
	 * This method determines if the array is empty.
	 * @return Tells you whether or not the list is empty.
	 */
	public boolean isEmpty() {
		return m_list.isEmpty();
	}
	/**
	 * This method shows you the last item in the array list.
	 * @return shows the last item on the array list without removing it.
	 */
	public T peek() {
		return m_list.get(getSize()-1);
	}
	/**
	 * This method shows you the last item in the array list, and removes the item.
	 * @return The last item from the array list. Removes it from the list.
	 */
	public T pop() {
		T temp = m_list.get(getSize()-1);
		m_list.remove(getSize()-1);
		return temp;
	}
}
