package openstreetmaps.Threads;

import android.util.Log;
import android.util.Pair;
import openstreetmaps.Data.BusStop;
import openstreetmaps.Data.Line;
import openstreetmaps.ZTM.ZtmHtmlParser;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LineDataThread extends Thread
{
	static ConcurrentLinkedQueue<String> lines_to_process = new ConcurrentLinkedQueue<String>();	//no need for synchro outside, it's done inside 
	static Vector<LineDataThread> v = new Vector<LineDataThread>();
	
	String line_name = null;
	
	public LineDataThread()
	{
		start();
	}
	
	synchronized public static void startThreads(int threads)
	{
		for(int i = 0; i < threads; ++i)
			v.add(new LineDataThread());
	}
	
	public void run()
	{
		while(true)
			getNewTask();
	}

	public static void addTask(String task)
	{
		//of course it don't work when multiple tasks considering the same line are added at the time (and line is not yet ready)
		if(!Line.lineReady(task))		//synchro inside
			lines_to_process.add(task);	//synchro inside
	}
	
	void getNewTask()
	{
		while(true)
		{
			if(!lines_to_process.isEmpty())
			{
				line_name = lines_to_process.remove();
				if(!Line.lineReady(line_name))	//but still two threads will do one duplicated task in case of one is not finished when second starts
				{
					Log.i("mythread", "thread " + this.getId() + " processing '" + line_name + "'");
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
	
		readyLineData();
	}

	void readyLineData()
	{
		try	//not needed, just for locating exceptions
		{
			Log.i("line", "creating line " + line_name);
			
			ZtmHtmlParser parser = new ZtmHtmlParser();
			
			parser.get(line_name);
			
			Vector<Pair<String, String>> fnames = parser.getForward();
			Vector<Pair<String, String>> bnames = parser.getBackward();
			
			Vector<String> f = new Vector<String>(); 
			Vector<String> b = new Vector<String>(); 
			
			for(Pair<String, String> pair : fnames)	//it could be done better
				f.add(pair.first);
			for(Pair<String, String> pair : bnames)
				b.add(pair.first);
			
//				Log.i("line", "line data download complete");
					
			Line newline = new Line(line_name, BusStop.getByNames(f), BusStop.getByNames(b));
			
//				Log.i("line", "new line created");

			Line.addLine(newline, line_name);	//it's synchro inside
						
//				Log.i("line", "line put in place");	
//				Log.i("line", newline.toString());
		}
		catch(Exception e)
		{
			Log.i("line", "create line exception: " + e.getMessage());
		}		
	}

	public static void test()
	{
		LineDataThread.addTask("101");
		LineDataThread.addTask("102");
		LineDataThread.addTask("103");
		LineDataThread.addTask("104");
		LineDataThread.addTask("105");
		LineDataThread.addTask("107");
		LineDataThread.addTask("108");
		LineDataThread.addTask("109");
		LineDataThread.addTask("110");
		LineDataThread.addTask("111");
		LineDataThread.addTask("112");
	
		startThreads(6);
	}
}
