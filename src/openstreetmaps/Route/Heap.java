package openstreetmaps.Route;

import java.util.Comparator;
import java.util.Vector;

public class Heap<T extends Indexed>	//binary heap
{
	int n;
	int h;
	
	Vector<T> data = null;	//counting indexes from 1 !!!
	Comparator<T> comparator = null;
	
//	int index;
//	Map<T, Integer> indexes = new HashMap<T, Integer>();
//	Vector<Integer> positions = new Vector<Integer>();	// positions[index from map] = position in data table
	
	public Heap(Comparator<T> c, int size)
	{
		n = 0;
		comparator = c;
		data = new Vector<T>();
		data.ensureCapacity(size);
		data.add(null);	//position 0 will be never used
	}
	
	public boolean isEmpty()
	{
		return n == 0;
	}

	protected boolean compare(T t1, T t2)	//is t1 better than t2 ?
	{
		return comparator.compare(t1, t2) < 0;	//min heap
	}
	
	public void upheap(int index)
	{
		int j = index;
		int i = index / 2; 	//floor
		T tmp = data.get(index);
		
		while(j > 1 && compare(tmp, data.get(i)))	//while node is better than it's parent
		{
			data.set(j, data.get(i));
			data.get(j).setIndex(j);
			j = i;
			i = i/2;		
		}
	
		data.set(j, tmp);
		data.get(j).setIndex(j);
	}
	
	public void downheap(int index)
	{
		T tmp = data.get(index);
		int j;
		
		while(index <= n/2)	//while has at least one child
		{
			j = 2 * index;	//left child
			if(j < n && compare(data.get(j + 1), data.get(j)))	//if has two children and right is better than left
				j++;
			//now j points on best child
			if(compare(tmp, data.get(j)))	//node is better than it's best child, so it's fixed already
				break;
			data.set(index, data.get(j));	//replace node with it's better child
			data.get(index).setIndex(index);
			index = j;	//go further
		}
	
		data.set(index, tmp);	//finally place element on it's final position
		data.get(index).setIndex(index);
	}

	public T getBest()
	{
		return data.get(1);
	}
	
	public T removeBest()
	{
		T best = getBest();
		
		data.set(1, data.get(n));
		--n;
		downheap(1);
		
		return best;
	}

	public void insert(T t)
	{		
		n++;

		if(n >= data.size())
			data.add(t);
		else
			data.set(n, t);
	}
}