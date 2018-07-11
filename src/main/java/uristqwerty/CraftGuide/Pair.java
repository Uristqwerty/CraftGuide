package uristqwerty.CraftGuide;

import java.util.ArrayList;
import java.util.List;

public class Pair<T1, T2>
{
	public T1 first;
	public T2 second;

	public Pair(T1 a, T2 b)
	{
		first = a;
		second = b;
	}

	@Override
	public int hashCode()
	{
		// Null hashes arbitrarily chosen by keyboard mashing.
		int firsthash = first == null? 5960343 : first.hashCode();
		int secondhash = second == null? 1186323 : second.hashCode();
		return firsthash ^ Integer.rotateLeft(secondhash, 13);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Pair))
			return false;

		Pair<T1, T2> other = (Pair<T1, T2>)obj;

		return (first == null? other.first == null : first.equals(other.first)) &&
				(second == null? other.second == null : second.equals(other.second));
	}

	/**
	 * Expects a1.length == a2.length
	 */
	public static <T1, T2> Pair<T1, T2>[] zip(T1[] a1, T2[] a2)
	{
		if(a1.length != a2.length)
			throw new IllegalArgumentException("Pair.zip expects both arrays to be equal length");
		Pair<T1, T2>[] ret = new Pair[a1.length];
		for(int i = 0; i < a1.length; i++)
			ret[i] = new Pair<T1, T2>(a1[i], a2[i]);
		return ret;
	}

	/**
	 * Expects a1.size() != a2.size()
	 */
	public static <T1, T2> ArrayList<Pair<T1, T2>> zip(List<T1> a1, List<T2> a2)
	{
		if(a1.size() != a2.size())
			throw new IllegalArgumentException("Pair.zip expects both arrays to be equal length");
		int size = a1.size();
		ArrayList<Pair<T1, T2>> ret = new ArrayList<Pair<T1, T2>>(size);
		for(int i = 0; i < size; i++)
			ret.add(new Pair<T1, T2>(a1.get(i), a2.get(i)));
		return ret;
	}
}
