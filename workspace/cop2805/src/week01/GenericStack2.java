package week01;

import java.util.ArrayList;
/**
 * This class handles an array list.
 * The array is in another class.
 * @author George Harrington
 *
 * @param <T>
 */
public class GenericStack2<T> extends ArrayList<T> {
	/**
	 * This method pushes data to the array.
	 * @param T This is the array list that stores the data.
	 */
	public void push(T T) {
		this.add(T);
	}
	/**
	 * This method gets the size of the list.
	 * @return the size of the list as an int.
	 */
	public int getSize() {
		return this.size();
		
	}
	/**
	 * This method determines if the array is empty.
	 * @return Tells you whether or not the list is empty.
	 */
	public boolean isEmpty() {
		return this.isEmpty();
	}
	/**
	 * This method shows you the last item in the array list.
	 * @return shows the last item on the array list without removing it.
	 */
	public T peek() {
		return this.get(getSize()-1);
	}
	/**
	 * This method shows you the last item in the array list, and removes the item.
	 * @return The last item from the array list. Removes it from the list.
	 */
	public T pop() {
		T temp = this.get(getSize()-1);
		this.remove(getSize()-1);
		return temp;
	}
}
