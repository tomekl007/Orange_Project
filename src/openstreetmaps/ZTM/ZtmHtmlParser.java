package openstreetmaps.ZTM;

import android.util.Log;
import android.util.Pair;
import openstreetmaps.Data.TextHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Vector;


public class ZtmHtmlParser 
{
//	private static final String ns = null;
	
	Vector<Pair<String, String>> forward = null;
	Vector<Pair<String, String>> backward = null;
	
	protected String constructURL(String line)
	{
		StringBuilder sb = new StringBuilder("http://m.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&q=");
		sb.append(line);
		sb.append("#content");
	
		return sb.toString();
	}
	
	protected Vector<Pair<String, String>> getElements(String line, boolean forward)	//Pair<stop name, stop number>
	{	
		String url = constructURL(line);
//        print("Fetching %s...", url);

		Vector<Pair<String, String>> output = new Vector<Pair<String, String>>();
		
        try
        {
	        Document doc = Jsoup.connect(url).get();	        	
	        Elements links = doc.select("a[href]");
	        Elements strong = doc.select("strong");
	        boolean started = false;
	        
	        String first_stop = strong.get(1).text();
	        String last_stop = strong.get(0).text();
	        String condition;
	        
	        if(forward)
	        {
	        	condition = new String("A");
	        	first_stop = strong.get(1).text();
	        	last_stop = strong.get(0).text();
	        }
	        else
	        {
	        	condition = new String("B");
	        	first_stop = strong.get(0).text();
	        	last_stop = strong.get(1).text();
	        }

	        //print("from %s to %s", first_stop, last_stop);
	        
	        for (Element link : links) 
	        {
	            if(link.attr("abs:href").contains("k=" + condition))	//maybe it would be better to change 'constains' to 'ends with' ?
	            {		            
	            	if(link.text().compareTo(strong.get(1).text()) == 0)	//finish of reverse path is start of normal path
	            	{
	            		if(started)
	            			break;
	            		else
	            			started = true;
	            	}

	            	Element el = link.parent().nextElementSibling();
	            	//output.add(link.text() + " " + el.text());
	            	output.add(new Pair<String, String>(TextHelper.parseString(link.text()), el.text()));
	            	//	            	print("next: " + el.text());
	            	
	            	//print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
	            	//print("%s", link.text() + " " + el.text());		            	
	            }
	        }
        
        	output.add(new Pair<String, String>(TextHelper.parseString(last_stop), "01"));	//number of last stop need to be found !!!
	        //print("final stop: " + strong.get(0).text());
        }
        catch(Exception e)
        {
        	
        }	        
		
        return output;
	}
	
	public void get(String line)
	{
		forward = getElements(line, true);
		backward = getElements(line, false);
		
//		print("-----------forward");
//		for(String s : forward)
//			print("%s", s);
		
//		print("-----------backwards");
//		for(String s : backward)
//			print("%s", s);
	}
	
	public static void test()
	{
		ZtmHtmlParser p = new ZtmHtmlParser();
		p.get("112");
	
		Vector<Pair<String, String>> f = p.getForward();
		Vector<Pair<String, String>> b = p.getBackward();
		
		for(Pair<String, String> pair : f)
			Log.i("test", pair.first + " - " + pair.second );
		for(Pair<String, String> pair : b)
			Log.i("test", pair.first + " - " + pair.second );
	}
	
	public Vector<Pair<String, String>> getForward()
	{
		return forward;
	}
	
	public Vector<Pair<String, String>> getBackward()
	{
		return backward;
	}
	
	private static void print(String msg, Object... args) {
        //System.out.println(String.format(msg, args));
		Log.i("Html", String.format(msg, args));
	}

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}
