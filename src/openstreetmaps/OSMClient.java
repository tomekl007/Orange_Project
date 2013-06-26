package openstreetmaps;


import android.util.Log;
import openstreetmaps.Data.BusStop;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
//import java.net.CookieStore;

public class OSMClient //downloads data from open street maps
{
	Vector<BusStop> bus_stops = new Vector<BusStop>();
	HttpEntity to_consume = null;
	
	InputStream sendRequest(String url)
	{
		try 
        {
        	Log.i("response", "URL: " + url);
			
			CookieStore cookieStore = new BasicCookieStore();
        	HttpContext localContext = new BasicHttpContext();
        	localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        	
        	DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse response1 = httpclient.execute(httpGet, localContext);
            
        	//System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            to_consume = entity1;
            InputStream is = entity1.getContent();
            
            return is;
        } 
        catch (Exception e)
        {	
        	Log.i("response", "Exception: " + e.getMessage());
        }		
	
		return null;
	}
	
	protected void consumeContent()
	{
		try
		{
	        to_consume.consumeContent();
		}
		catch(Exception e)
		{
			
		}
	}
	
	protected String constructURL(double lat, double lon, int radius)	//radius in meters
	{	//constucting url for OSM overpass API
        //%28 (		//%2E .		//%3D =
        //%29 )		//%2C ,
        //%2B 		//%5B [
        //%3A :		//%22 "

        //przykladowy url, 100 metrow wokol palacu kultury i nauki
		//http://overpass-api.de/api/interpreter?data=node%28around%3A100%2E0%2C52%2E229863%2C21%2E004915%29%5B%22highway%22%3D%22bus%5Fstop%22%5D%3Bout%20body%3B%0A
        
		//przykladowy url, poprzednia wersja z prostokatnym obszarem
        //http://overpass-api.de/api/interpreter?data=node%2821%2E0041%2C52%2E2194%2C21%2E0302%2C52%2E2347%29%5B%22highway%22%3D%22bus%5Fstop%22%5D%3Bout%20body%3B%0A
		
		String slat = Double.toString(lat);
        String slon = Double.toString(lon);
        slat = slat.replace(".", "%2E");	//zamiana kropek na kodowanie url
        slon = slon.replace(".", "%2E");

		StringBuilder sb = new StringBuilder("http://overpass-api.de/api/interpreter?data=node%28around%3A");
        sb.append(radius);	//nie trzeba sie przejmowac zamiana zadnych znakow
        sb.append("%2C");
        
        sb.append(slat);
        sb.append("%2C");	//,
        sb.append(slon);
        sb.append("%29");	//)
        
        sb.append("%5B%22highway%22%3D%22bus%5Fstop%22%5D%3Bout%20body%3B%0A");
        return sb.toString();
		
	}
	
	public void get(double lat, double lon, int radius)
	{
        String url = constructURL(lat, lon, radius);
        InputStream is = sendRequest(url);
        XMLParser parser = new XMLParser();
        
        try{	Vector<BusStop> bus_stops = parser.parse(is);	}
        catch(Exception e){	Log.i("OSMClient", e.getMessage());	}
        
        consumeContent();                
    }

	private void listInputStream(InputStream is)	//for test purposes
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String inputLine;
		
		try
		{
			while ((inputLine = in.readLine()) != null)
			    Log.i("XML", inputLine);
			in.close();						
		}
		catch(Exception e)
		{
		}
	}
}