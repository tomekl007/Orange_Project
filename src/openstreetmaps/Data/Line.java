package openstreetmaps.Data;

import android.util.Log;
import android.util.Pair;
import openstreetmaps.ZTM.ZtmHtmlParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Line 
{
	static final int max_line_number = 1000;
	
	public static Line[] lines_num = new Line[max_line_number];
	public static Map<String, Line> lines_name = new HashMap<String, Line>();
	
	static ZtmHtmlParser parser = new ZtmHtmlParser();
	
	String name;
	SimpleLine forward = null;
	SimpleLine backward = null;
	
	public Line(String n, Vector<BusStop> f, Vector<BusStop> b)
	{
		name = n;
		forward = new SimpleLine(f);
		backward = new SimpleLine(b);
	}
	
	public String getName()
	{
		return name;
	}
	
	public static boolean lineReady(String ID)
	{
		if(ID.matches("\\d+"))	//only number
		{
			int number = Integer.valueOf(ID);
			
			Line output;
			synchronized (lines_num) {		output = lines_num[number];		}
			return (output != null);
		}
		else
		{
			Line output;
			synchronized (lines_name) {		output = lines_name.get(ID);	}
			return (output != null);
		}
	}

	public synchronized static void clearStaticData()
	{
		for(int i = 0; i < max_line_number; ++i)
			lines_num[i] = null;
		
		lines_name.clear();
		
		//map needs no nullyfying, it returns null for unexisting key by default
	}
	
	public static Line getByName(String ID)
	{
//		Log.i("line", "get by name: " + ID);
		
		try
		{
			if(ID.matches("\\d+"))	//only number
			{
				int number = Integer.valueOf(ID);
				
				if(lines_num[number] == null)
					Line.createLine(ID);
				
				Line output;
				synchronized (lines_num) {		output = lines_num[number];		}
				return output;
			}
			else
			{
				if(lines_name.get(ID) == null)
					Line.createLine(ID);
			
				Line output;
				synchronized (lines_num) {		output = lines_name.get(ID);	}
				return output;
			}
		}
		catch(Exception e)
		{
			Log.i("line", "get by name exception: " + e.getMessage());			
		}		
	
		return null;
	}
	
	public static void addLine(Line newline, String name)
	{
		if(name.matches("\\d+"))
		{
			synchronized(lines_num)
			{	lines_num[Integer.valueOf(name)] = newline;		}
		}
		else
		{
			synchronized(lines_name)
			{	lines_name.put(name, newline);		}
		}
	}
	
	static void createLine(String ID)
	{
		try	//not needed, just for locating exceptions
		{
			Log.i("line", "creating line " + ID);
			
			parser.get(ID);
			
			Vector<Pair<String, String>> fnames = parser.getForward();
			Vector<Pair<String, String>> bnames = parser.getBackward();
			
			Vector<String> f = new Vector<String>(); 
			Vector<String> b = new Vector<String>(); 
			
			for(Pair<String, String> pair : fnames)	//it could be done better
				f.add(pair.first);
			for(Pair<String, String> pair : bnames)
				b.add(pair.first);
			
//			Log.i("line", "line data download complete");
					
			Line newline = new Line(ID, BusStop.getByNames(f), BusStop.getByNames(b));
			
//			Log.i("line", "new line created");

			if(ID.matches("\\d+"))
				lines_num[Integer.valueOf(ID)] = newline;
			else
				lines_name.put(ID, newline);
			
//			Log.i("line", "line put in place");	
//			Log.i("line", newline.toString());
		}
		catch(Exception e)
		{
			Log.i("line", "create line exception: " + e.getMessage());
		}		
	}
	
	public Vector<BusStop> nextStops(BusStop bs)
	{
		Vector<BusStop> output = new Vector<BusStop>();
		
		output.addAll(forward.getNextStops(bs));
		output.addAll(backward.getNextStops(bs));	//in many cases it'll be just every stop on this line but chosen one (bs), but sometimes line differs forward and backward so it should stay
		
		return output;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("line " + name + "\n");
		sb.append(forward.toString() + "\n");
		sb.append(backward.toString() + "\n");		
		
		return sb.toString();
	}
}