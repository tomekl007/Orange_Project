package openstreetmaps.Route;

import android.util.Log;
import android.util.Pair;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

public class RouteFinder 
{
	Vector<Node> end_nodes;
	int max_level;
	
	public RouteFinder(Vector<Node> end, int l)
	{
		end_nodes = end;
		max_level = l;
	}

	public void findRoutes()
	{	//start nodes have no predecessors
		for(Node end : end_nodes)
			findRoute(end, 1);
	}
	
	protected void findRoute(Node start, int level)
	{
		Log.i("route", "searching (backward) routes from " + start.toString());
		
		LinkedBlockingQueue<Pair<Node, String>> input = new LinkedBlockingQueue<Pair<Node, String>>();
		input.add(new Pair<Node, String>(start, "(start node)"));
		visit(start, level, input);
	}
	
	protected void visit(Node n, int level, LinkedBlockingQueue<Pair<Node, String>> route)
	{
		if(level > max_level)
			return;
		
		Log.i("route", "visiting " + n);
		
		if(n.bs.start_bs || n.predecessors.isEmpty())	//start node has no predecessors, because edges to start nodes are not added
		{
			Log.i("route", "found route: " );
			
			Iterator<Pair<Node, String>> it = route.iterator();
			while(it.hasNext())
			{
				Pair<Node, String> pair = it.next();
				Log.i("route", pair.first.toString() + " by line " + pair.second);
			}
		}
		
		for(Edge e : n.predecessors)
		{
			Log.i("route", "pre of '" + n.toString() + "(" + n.distance + ")' is '" + e.to.toString() + "(" + e.to.distance + ")'");			
			
			if(e.value != e.to.distance - n.distance)	//starting from end nodes, so distance should grow
				continue;
						
			LinkedBlockingQueue<Pair<Node, String>> new_route = new LinkedBlockingQueue<Pair<Node, String>>(route);
			new_route.add(new Pair<Node, String>(e.to, e.line));
			
			visit(e.to, level + 1, new_route);
			
//			if(output = visit(e.to, level + 1) != null)
//				output = true;
		}
	}
}
