package openstreetmaps;

import java.util.Comparator;

public class TestComparator implements Comparator<TestClass>
{
	 public int compare(TestClass o1, TestClass o2)
	 {
		 if(o1.value == o2.value)
			 return 0;
		 else if(o1.value < o2.value)
			 return -1;
		 else
			 return 1;
	 }	
}
