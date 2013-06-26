package openstreetmaps.API;

import android.util.Log;
import openstreetmaps.Data.BusStop;
import openstreetmaps.MyApplication;
import openstreetmaps.rest.RESTClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CityData extends RESTClient
{		
	public static final String IDs_filename = "IDs.xml";	
	
	protected String constructURLforAllIDs()
	{
		//"https://openmiddleware.pl/orange/oracle/ztm/busstop?busstop=%%%"
		StringBuilder sb = new StringBuilder("https://openmiddleware.pl:443/orange/oracle/ztm/busstop?busstop=%25%25%25");
		//StringBuilder sb = new StringBuilder("https://openmiddleware.pl/orange/oracle/ztm/busstop?busstop=Dworkowa");
		
		return sb.toString();
	}
	
	protected static String constructURLforLines(BusStop bs)
	{
		//https://host:port/orange/oracle/ztm/lines?busstopId=...&busstopNr=...
		StringBuilder sb = new StringBuilder("https://openmiddleware.pl:443/orange/oracle/ztm/lines?busstopId=");
		sb.append(bs.id);
		sb.append("&busstopNr=");
		sb.append("01");	//what to do with it ?
		return sb.toString();
	}
	
	protected static String linesFilename(BusStop bs)
	{
		return bs.name + ".xml";
	}

	public static File linesFile(BusStop bs)
	{
		File lines_dir = MyApplication.getAppContext().getDir("bs_lines", MyApplication.getAppContext().MODE_PRIVATE);
		File file = new File(lines_dir, CityData.linesFilename(bs));
		return file;
	}
	
	public static File IDsFile()
	{
		File file = new File(MyApplication.getAppContext().getFilesDir(), IDs_filename);
		return file;
	}
	
	public void getLinesToFile(BusStop bs)
	{
		getDataToFile(constructURLforLines(bs), linesFile(bs));
	}
	
	public void getIDsToFile(String IDs_filename) throws IOException
	{
		MyApplication.getAppContext().getFilesDir();
		getDataToFile(constructURLforAllIDs(), CityData.IDsFile());
	}
/*	
	public void getIDs()
	{
		String url = constructURLforAllIDs();
		
	  try
	  {
		  InputStream is = sendRequest(url);		  
//		  listInputStream(is);
		  Document doc = Jsoup.parse(is, null, url);
		  consumeContent();		  
		  
        Elements stops = doc.select("busstop");
	    
	    for (Element stop : stops) 
	    {
    		String name = null;
    		String ID = null;

    		for(Element e : stop.children())
	        {
	    		if(e.nodeName().compareTo("id") == 0)
	        		ID = e.text();
	    		else if(e.nodeName().compareTo("name") == 0)
	    			name = e.text();	        
	        }

    		BusStop.setBusStop(ID, name);
    		//output.add(new Pair<String, String>(ID, name));
    		//print(ID + " " + name);
	    }
	  
	    Log.i("Html", "finished");

	  }
	  catch(Exception e)
	  {
	  	Log.i("Html", "exception: " + e.getMessage());
	  }
	  finally
	  {
		  consumeContent();
	  }
	  
	  //return output;
	}
*/	
/*	
	public InputStream getLines(BusStop bs)
	{
		try
		{
			File file = new File(MyApplication.getAppContext().getFilesDir(), linesFilename(bs));
			
			if(!file.exists())
			{
				Log.i("busstop", "no IDs file, downloading IDs data");
				String url = constructURLforLines(bs);
				InputStream is = sendRequest(url);
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
		
		//listInputStream(is);
		return is;
	}
*/	
	private static void print(String msg, Object... args) {
        //System.out.println(String.format(msg, args));
		Log.i("Html", String.format(msg, args));
	}

	public void testFile(String IDs_filename)
	{
		File file = new File(MyApplication.getAppContext().getFilesDir(), IDs_filename);
	
		try
		{
			InputStream inputStream = new FileInputStream(file);			
			listInputStream(inputStream);
		}
		catch(Exception e)
		{
			Log.i("citydata", "exception: " + e.getMessage());
		}
		 
	}
}

