package openstreetmaps.Data;

import android.util.Log;

import java.util.Iterator;
import java.util.Vector;

public class SimpleLine 
{
	Vector<BusStop> stops = null;
	
	public SimpleLine(Vector<BusStop> _bs)
	{
		stops = _bs;
	}
	
	public Vector<BusStop> getNextStops(BusStop bs)
	{
		Vector<BusStop> output = new Vector<BusStop>();
		
		Iterator it = stops.iterator();
		BusStop _bs = null;
		
		while(it.hasNext())
		{
			_bs = (BusStop)it.next();
			if(_bs == bs)	//every BusStop comes from static map, so there will be no more than one instance of particular bus stop, so they can be == by reference
				break;
		}
		
		while(it.hasNext())
		{
			_bs = (BusStop)it.next();	//ommit the one that caused break in loop before, we're searching for stops NEXT to it
			output.add(_bs);
		}
				
		return output;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(BusStop bs : stops)
			{
			if(bs == null)
				Log.i("line", "null bus stop");
			sb.append(bs.name + " ");
			}
		
		return sb.toString();
	}
}
