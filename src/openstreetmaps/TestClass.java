package openstreetmaps;

import java.util.Random;

public class TestClass 
{
	static Random r = new Random();
	
	public int value;
	
	public TestClass(int v)
	{
		value = v;
	}
	
	public void randomize()
	{
		value = r.nextInt(20);
	}
	
	public static void testHeap()
	{
/*
		Heap<TestClass> heap = new Heap<TestClass>(new TestComparator(), 100);
		
		Vector<TestClass> v = new Vector<TestClass>();
		
		for(int i = 90; i  >= 0; --i)
		{
			TestClass a = new TestClass(i);
			v.add(a);
		}
		
		for(TestClass t : v)
			t.randomize();

		for(TestClass t : v)
			heap.insert(t);
	
		while(!heap.isEmpty())
		{
			TestClass t = (TestClass)heap.removeBest();
			Log.i("test", "" + t.value);
		}
*/		
	}
}
