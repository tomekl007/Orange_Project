package openstreetmaps.Data;

import android.util.Log;
import openstreetmaps.API.CityData;
import openstreetmaps.Threads.BusStopDataThread;
import openstreetmaps.rest.RESTClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class BusStop 
{
	final static int max_stop_number = 10000;
	static BusStop[] stops_number = new BusStop[max_stop_number];	//10k references can be easily stored, even when many of indexes are wasted (constant acdces time is totally worth it)
	static Map<String, BusStop> stops_ID = new HashMap<String, BusStop>();	//for IDs that are not full number (eg. A-xx, R-xx)
	static Map<String, BusStop> stops_name = new HashMap<String, BusStop>(); 	//names
	
	public Vector<Line> lines = null;

	public double latitude;
	public double longitude;
	public String name;
	public String id;

	public boolean fully_ready = false;
	public boolean start_bs = false;

	static int error_count = 0;
	static int downloaded = 0;

	public static final String IDs_filename = "IDs.xml";

	public BusStop(String n, String ID)
	{
		name = n;
		id = ID;
	}

	public static int getErrorCount()
	{
		return error_count;
	}

	public static void clearStaticData()
	{
		for(int i = 0; i < max_stop_number; ++i)
			stops_number[i] = null;

		stops_name.clear();
		stops_ID.clear();

		error_count = 0;
		downloaded = 0;
	}

	public static void setBusStop(String ID, String name)
	{
		BusStop newstop = new BusStop(name, ID);

		synchronized (stops_number)
		{
			if(ID.matches("\\d+") && stops_number[Integer.valueOf(ID)] != null)
			{
				//Log.i("error", ID + " " + name + " overwriting existing data [array] on " + stops_number[Integer.valueOf(ID)]);
				++error_count;
			}
		}
		synchronized (stops_ID)
		{
			if(stops_ID.get(ID) != null)
			{
				//Log.i("error", ID + " " + name + " overwriting existing data [ID map] on " + stops_ID.get(ID));
				++error_count;
			}
		}
		synchronized (stops_name)
		{
			if(stops_name.get(name) != null)
			{
				//Log.i("error", ID + " " + name + " overwriting existing data [Names map] on " + stops_name.get(name));
				++error_count;
			}
		}

		if(ID.matches("\\d+"))
			synchronized (stops_number)
			{
				stops_number[Integer.valueOf(ID)] = newstop;
			}
		else
		{
			//Log.i("busstop", "not number ID: " + ID);
			synchronized (stops_ID)
			{
				stops_ID.put(ID, newstop);
			}
		}

		synchronized (stops_name)
		{
			stops_name.put(name, newstop);
		}
	}

	public static BusStop getByID(String ID)
	{
		//stops should be downloaded / read from file at the very beginning
		BusStop output = null;

		if(ID.matches("\\d+"))
			synchronized (stops_number)	{	output = stops_number[Integer.valueOf(ID)];		}
		else
			synchronized (stops_ID)		{	output = stops_ID.get(ID);		}

		return output;
	}

	public void ensureFullData()
	{
		if(!isFullyReady())
		{
			BusStopDataThread.addTask(this);

			while(!isFullyReady())	//it's still faster than one thread, because lines data is downloaded by threads
			{
				try
				{
					Thread.sleep(100);
				}
				catch(Exception e)
				{

				}
			}
		}
	}

	public static BusStop getByName(String name)
	{
		//line name should not have bus stop name with stop number, e.g. 'Olesin 01'

		BusStop output = null;

		synchronized (stops_name) {		output = stops_name.get(name);		}

		if(output == null)
			Log.i("busstop", "can't find bus stop: '" + name + "'");

		return output;	//stops should be downloaded / read from file at the very beginning
	}

	public static Vector<BusStop> getByNames(Vector<String> names)
	{
		Vector<BusStop> output = new Vector<BusStop>();

		for(String s : names)
			output.add(getByName(s));

		return output;
	}

	public String toString()
	{
//		return "" + id + " " + name + " " + latitude + " " + longitude;
		return "" + id + " " + name;
	}

	public static void test()
	{
		int array_count = 0;
		int id_count = 0;
		int name_count = 0;

		//Log.i("busstop", "---array");
		for(int i = 0; i < stops_number.length; ++i)
			if(stops_number[i] != null)
			{
				//Log.i("busstop", stops_number[i].toString());
				++array_count;
			}

		//Log.i("busstop", "---ID map");
		id_count = stops_ID.keySet().size();
		for(String s : stops_ID.keySet())
		{
			//Log.i("busstop", s + ": " + stops_ID.get(s));
		}

		//Log.i("busstop", "---names map");
		name_count = stops_name.keySet().size();
		for(String s : stops_name.keySet())
		{
			//Log.i("busstop", s + ": " + stops_name.get(s));
		}

		Log.i("busstop", array_count + " in array + " + id_count + " in ID map = " + (array_count + id_count) + " , " + name_count + " names");
	}

	public static void readIDsData()
	{
		try
		{
			//File file = new File(MyApplication.getAppContext().getFilesDir(), IDs_filename);
			File file = CityData.IDsFile();

			if(!file.exists())
			{
				Log.i("busstop", "no IDs file, downloading IDs data");
				CityData cd = new CityData();
				cd.getIDsToFile(IDs_filename);
				cd.consumeContent();
			}
			else
				Log.i("busstop", "IDs file exists, only parsing");

			InputStream inputStream = new FileInputStream(file);
			parseIDs(inputStream);
			inputStream.close();
		}
		catch(IOException e)
		{
			Log.i("busstop", "cannot download IDs data !");
		}
		catch(Exception e)
		{
			Log.i("busstop", "exception read IDs: " + e.getMessage());
		}
	}

	public static void parseIDs(InputStream is)
	{
		try
		  {
			Document doc = Jsoup.parse(is, null, "");

	        Elements stops = doc.select("busstop");

	        if(RESTClient.checkForError(doc))
	        	return;

	        Log.i("busstop", stops.size() + " elements found");

		    for (Element stop : stops)
		    {
		    	String name = null;
	    		String ID = null;

	    		for(Element e : stop.children())
		        {
		    		if(e.nodeName().compareTo("id") == 0)
		    		{
		    			ID = e.text();
		    		}
		    		else if(e.nodeName().compareTo("name") == 0)
		    		{
		    			name = e.text();
		    		}
	    			if(ID == null)	//because data from ZTM API has errors and missing names
	    				ID = new String("");
	    			if(name == null)//because data from ZTM API has errors and missing names
	    				name = new String("");
		        }

	    		if(name.length() > 0)
	    			name = TextHelper.parseString(name);
//	    		if(name.length() > 0 && name.substring(0, 1).matches("[a-z]"))
//	    			name = name.substring(0, 1).toUpperCase() + name.substring(1);	//capitalize first letter because getLines needs names to start with capital letter and getIDs not always gives capitalized names

	    		BusStop.setBusStop(ID, name);
		    }

		    Log.i("busstop", getErrorCount() + " errors in input file");
		    Log.i("busstop", "parsing IDs finished");

		  }
		  catch(Exception e)
		  {
		  	Log.i("busstop", "parse Ids exception: " + e.getMessage());
		  }

		  //test();
	}

	public Vector<Line> getLines()	//use it even inside class
	{
		if(lines == null)
		{
			lines = new Vector<Line>();
			readLineData();
		}
	
		return lines;
	}
		
	public void readLineData()
	{		
		try
		{
//			File lines_dir = MyApplication.getAppContext().getDir("bs_lines", MyApplication.getAppContext().MODE_PRIVATE);
//			File file = new File(lines_dir, CityData.linesFilename(this));
			File file = CityData.linesFile(this);
			
			if(!file.exists())
			{
				Log.i("busstop", "no line data file, downloading data for '" + name + "'");
				CityData cd = new CityData();
				cd.getLinesToFile(this);
				cd.consumeContent();
				++downloaded;	//when cd encouter a problem with downloading, it should not be increased !
			}
			else
				Log.i("busstop", "line data file exists, only parsing for '" + name + "'");

			InputStream inputStream = new FileInputStream(file);			
			boolean del = !parseLines(inputStream);
			inputStream.close();
		
			if(del)	//saved file contains only error report and it'll block place for good data later
			{
				--downloaded;
				Log.i("busstop", "deleting file with only error report" + file.getName() + " (" + downloaded + " files downloaded this session)");
				
				file.delete();
			}
		}
		catch(IOException e)
		{
			Log.i("busstop", "IOexception read line data: " + e.getMessage());
		}
		catch(Exception e)
		{
			Log.i("busstop", "exception read line data: " + e.getMessage());
		}
	}
	
	synchronized public boolean parseLines(InputStream is)
	{
		int aaa = 0;  
		
		try
		  {
			aaa = 0;
			
			Document doc = Jsoup.parse(is, null, "");
			
			aaa = 1;
			
	        if(RESTClient.checkForError(doc))
	        	return false;

	        aaa = 2;
	        
	        Elements elines = doc.select("line");
	        
	        aaa = 3;
	        
	        Log.i("busstop", elines.size() + " lines found");
		    
	        for (Element line: elines) 
		    {
		        aaa = 4;

		        String name = line.text();
	    		
		        aaa = 5;

	    		//no parsing of name - let it be original (i get line names from only one source and send them to ZTM webpage)
	    		//name = TextHelper.parseString(name);
	    		//Log.i("busstop", "found line: " + name);
	    				        
	    		lines.add(Line.getByName(name));	//no getLines() here, it would cause recurrency !!!
		        aaa = 6;
		    }
		  
	        aaa = 7;
	        
		    Log.i("busstop", "finished parsing line data for bus stop: " + name);
		    return true;	//don't delete this file
		  }
		  catch(Exception e)
		  {
		  	Log.i("busstop", "exception parse lines (" + aaa + "): " + e.getMessage());
		  }	

		  return true;	//exception doesn't have to mean that file needs to be deleted
	}
	
	synchronized public void ready()
	{
		fully_ready = true;
	}
	
	synchronized public boolean isFullyReady()
	{
		return fully_ready;
	}
}