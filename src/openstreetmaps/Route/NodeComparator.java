package openstreetmaps.Route;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node>
{
	public int compare(Node n1, Node n2)
	{
		if(n1.distance == n2.distance)
			return 0;
		else if(n1.distance < n2.distance) 
			return -1;
		else
			return 1;
	}
}
