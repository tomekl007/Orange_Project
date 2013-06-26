package openstreetmaps.Threads;

import android.util.Log;
import openstreetmaps.API.CityData;
import openstreetmaps.Data.BusStop;
import openstreetmaps.Data.Line;
import openstreetmaps.Data.TextHelper;
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
import java.util.concurrent.ConcurrentLinkedQueue;

public class BusStopDataThread extends Thread 
{
	static ConcurrentLinkedQueue<BusStop> BS_to_process = new ConcurrentLinkedQueue<BusStop>();	//no need for synchro outside, it's done inside
	static Vector<BusStopDataThread> v = new Vector<BusStopDataThread>();
	static Map <BusStop, Boolean> processed = new HashMap<BusStop, Boolean>();
	
	BusStop actual_task = null;
	
	public BusStopDataThread()
	{
		start();
	}
	
	synchronized public static void startThreads(int threads)
	{
		for(int i = 0; i < threads; ++i)
			v.add(new BusStopDataThread());
	}
	
	public void run()
	{
		while(true)
			getNewTask();
	}

	public static void addTask(BusStop task)
	{
		//of course it don't work when multiple tasks considering the same line are added at the time (and line is not yet ready)
//		if(!Line.lineReady(task))		//synchro inside
			BS_to_process.add(task);	//synchro inside
	}
	
	void getNewTask()
	{
		while(true)
		{
			if(!BS_to_process.isEmpty())
			{
				actual_task = BS_to_process.remove();
				if(!isProcessed(actual_task))	//maybe synchronize(this) for sure ?
				{
					markProcessed(actual_task);
					Log.i("mythread", "thread " + this.getId() + " processing '" + actual_task.name + "'");
					break;					
				}
			}
		
			try
			{
				sleep(100);				
			}
			catch(Exception e)
			{
				Log.i("mythread", e.getMessage());
			}
		}
	
		readyBSData();
	}

	void readyBSData()
	{
//		Log.i("mythread", "ready data");
		
		try
		{
			File file = CityData.linesFile(actual_task);
			
			if(!file.exists())
			{
//				Log.i("busstop", "no line data file, downloading data for '" + actual_task.name + "'");
				CityData cd = new CityData();
				cd.getLinesToFile(actual_task);
				cd.consumeContent();
//				++downloaded;	//when cd encouter a problem with downloading, it should not be increased !
			}
//			else
//				Log.i("busstop", "line data file exists, only parsing for '" + actual_task.name + "'");

			InputStream inputStream = new FileInputStream(file);			
			boolean del = !parseLines(inputStream);
			inputStream.close();
		
			if(del)	//saved file contains only error report and it'll block place for good data later
			{
//				--downloaded;
				Log.i("busstop", "deleting file with only error report '" + file.getName() + "'");
				
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
	
		actual_task.ready();
		Log.i("busstop", "finished readying data for " + actual_task.name);
	}
	
	protected boolean parseLines(InputStream is)
	{
		synchronized(actual_task)
		{
			try
			  {
				Document doc = Jsoup.parse(is, null, "");
				
		        if(RESTClient.checkForError(doc))
		        	return false;

		        Elements elines = doc.select("line");
		        
		        //Log.i("busstop", elines.size() + " lines found");
		        
		        for (Element line: elines) 	//create tasks for LineDataThreads 
			    {
			        String name = line.text();
			        LineDataThread.addTask(name);
			    }
		        
		        actual_task.lines = new Vector<Line>();
		        
		        for (Element line: elines)	//read the results (when all are read, BS is rady)
		        {
		        	String name = line.text();
		        	
		        	while(!Line.lineReady(name))
		        		sleep(100);
		        
		        	actual_task.lines.add(Line.getByName(name));
		        }
		        
		        actual_task.ready();
		        
			    //Log.i("busstop", "finished parsing line data for bus stop: " + actual_task.name);
			  }
			  catch(Exception e)
			  {
			  	Log.i("busstop", "exception parse lines: " + e.getMessage());
			  }
		
			return true;
		}
	}

	synchronized public static void markProcessed(BusStop bs)
	{
		processed.put(bs, true);
	}

	synchronized public static boolean isProcessed(BusStop bs)
	{
		return(processed.containsKey(bs));
	}
	
	public static void test()
	{
		startThreads(5);		
		
//		BusStop.getByName(TextHelper.parseString("Kostrzewskiego"));
		
		addTask(BusStop.getByName(TextHelper.parseString("Kostrzewskiego")));
		addTask(BusStop.getByName(TextHelper.parseString("Czarnomorska")));
		addTask(BusStop.getByName(TextHelper.parseString("Na��czowska")));
		addTask(BusStop.getByName(TextHelper.parseString("Dominika�ska")));
		addTask(BusStop.getByName(TextHelper.parseString("Kr�lowej Marysie�ki")));
		addTask(BusStop.getByName(TextHelper.parseString("Pa�acowa")));
		addTask(BusStop.getByName(TextHelper.parseString("Zap�ocie")));
		addTask(BusStop.getByName(TextHelper.parseString("Che�mska")));
		addTask(BusStop.getByName(TextHelper.parseString("Zawodzie")));
		addTask(BusStop.getByName(TextHelper.parseString("Bobrowiecka")));
		addTask(BusStop.getByName(TextHelper.parseString("Sypniewska")));
		addTask(BusStop.getByName(TextHelper.parseString("Noskowskiego")));
		addTask(BusStop.getByName(TextHelper.parseString("Wi�niowa")));
	}
}