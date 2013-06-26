package openstreetmaps.Route;

import android.util.Log;
import android.util.Pair;
import openstreetmaps.Data.BusStop;
import openstreetmaps.Data.TextHelper;

import java.util.Vector;

public class Algorithm 
{
	static int max_transfers = 1;
	
	public void constructNetwork(Vector<BusStop> start, Vector<BusStop> end)
	{	//creates part of network, in which search will be performed
		Log.i("route", "construct networks");
		
		Vector<Node> from = Node.toNodes(start);
		Vector<Node> start_nodes = Node.toNodes(start);
		Vector<Node> end_nodes = new Vector<Node>();
		Vector<Node> to = new Vector<Node>();

		Vector<Node> old_nodes = new Vector<Node>();	//to keeps nodes from deleting

		for(int i = 0; i < max_transfers; ++i)	//public transport network is constructed to be effective, so I'm not excepting more than 4-5 tranfers in any query
		{
			Log.i("route", i + " transfers");

			to.clear();

			for(Node n : from)
			{
				for(Pair<BusStop, String> pair : n.neighbours())
				{
					if(start.contains(pair.first))	//don't create edges to start nodes
					{
						pair.first.start_bs = true;
						continue;
					}

					Node new_node = new Node(pair.first);
					new_node.addPre(n, 1, pair.second);
					n.addSuc(new_node, 1, pair.second);

					if(end.contains(pair.first))	//end should be not big, O(n) won't hurt
					{
						end_nodes.add(new_node);
					}
					else
					{
						to.add(new_node);
					}
				}
			}

			old_nodes.addAll(from);
			from = to;
		}

		old_nodes.addAll(end_nodes);	//they're not added here as they're not added to 'to' which becames 'from'

		Log.i("route", "start nodes");
		for(Node n : start_nodes)
			Log.i("route", n.toString());
		Log.i("route", "end nodes");
		for(Node n : end_nodes)
			Log.i("route", n.toString());
		if(!end_nodes.isEmpty())
			getPaths(start_nodes, end_nodes, old_nodes);
		else
			Log.i("route", "no end nodes");
	}

	protected void getPaths(Vector<Node> start_nodes, Vector<Node> end_nodes, Vector<Node> all_nodes)
	{//network is already constructed, let's work
		for(Node n : start_nodes)
		{
			Log.i("route", "===== search for routes, start from " + n.toString());

			dijkstra(n, end_nodes, all_nodes);
		}
	}

	protected void dijkstra(Node start_node, Vector<Node> end_nodes, Vector<Node> all_nodes)
	{
		Heap<Node> heap = new Heap<Node>(new NodeComparator(), all_nodes.size());

		start_node.distance = 0;

		for(Node n : all_nodes)
		{
			n.distance = Integer.MAX_VALUE;	//'infinity'
			n.inHeap = true;
			heap.insert(n);
		}

		while(!heap.isEmpty())
		{
			Node best = heap.removeBest();
			best.inHeap = false;

			for(Edge suc : best.successors)
			{
				if(!suc.to.inHeap)
					continue;

				if(best.relax(suc))
				{
					heap.upheap(suc.to.getIndex());
					//repairing changed heap data
					//relaxing can only decrease (make better) distance value, so only upheaps are taken into consideration
				}
			}
		}

		reconstructPaths(end_nodes);
		/*
		for(Node n : end_nodes)
		{
			Log.i("route", n.toString() + " - " + n.distance + " transfers");
		}
*/
	}

	protected void reconstructPaths(Vector<Node> end_nodes)	//by going by backward edges
	{
		//DFS
		Log.i("route", "reconstructPaths");
		RouteFinder rf = new RouteFinder(end_nodes, max_transfers);
		rf.findRoutes();
	}

	public static void test()
	{
		Algorithm alg = new Algorithm();
		Vector<BusStop> start = new Vector<BusStop>();
		Vector<BusStop> end = new Vector<BusStop>();
		
		start.add(BusStop.getByName(TextHelper.parseString("Conrada")));
		end.add(BusStop.getByName(TextHelper.parseString("PKP Kasprzaka")));
		alg.constructNetwork(start, end);
	}
}