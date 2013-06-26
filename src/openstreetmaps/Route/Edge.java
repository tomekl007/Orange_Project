package openstreetmaps.Route;

public class Edge
{
	public Node to;
	public int value;
	public String line;
	
	public Edge(Node t, int v, String line)
	{
		to = t;
		value = v;
	}
}
