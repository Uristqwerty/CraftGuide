package uristqwerty.CraftGuide;

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
}
