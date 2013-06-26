package openstreetmaps;


import android.util.Log;
import android.util.Xml;
import openstreetmaps.Data.BusStop;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class XMLParser //for parsing OSM data
{
	private static final String ns = null;
	
    public Vector<BusStop> parse(InputStream in) throws XmlPullParserException, IOException
    {
    	Vector<BusStop> output;
    	
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            output = readOSM(parser);
        } 
    	finally {
            in.close();
        }

    	return output;
    }
    
    private Vector<BusStop> readOSM(XmlPullParser parser) throws XmlPullParserException, IOException {
    	Vector<BusStop> output = new Vector<BusStop>();
    	
    	parser.require(XmlPullParser.START_TAG, ns, "osm");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("node")) {
                output.add(readNode(parser));
            } else {
                skip(parser);
            }
        }  
    
        return output;
    }

    private BusStop readNode(XmlPullParser parser) throws XmlPullParserException, IOException {
    	//Log.i("XML", "read node");
    	
        String ID = parser.getAttributeValue(null, "id");
        String lat = parser.getAttributeValue(null, "lat");
        String lon = parser.getAttributeValue(null, "lon");

        //Log.i("XML", ID + " " + lat + " " + lon);
        BusStop output = new BusStop("", "");
        
        output.id = ID;
        output.latitude = Double.valueOf(lat);	//troche zaokragla, mimo ze to double
        output.longitude = Double.valueOf(lon);
        
    	parser.require(XmlPullParser.START_TAG, ns, "node");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("tag")) {
            	//Log.i("XML", "found tag");
            	
    		    String key = parser.getAttributeValue(null, "k");
                String value = parser.getAttributeValue(null, "v");
             
                if(key.equals("name"))
                {
               	 //Log.i("XML", "found name: " + value);
               	 output.name = new String(value);
                }           	
            	
            	parser.nextTag();
            } else {
                skip(parser);
            }
        }
        
        Log.i("XML", output.toString());
        
        return output;
    }    
 
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
}
