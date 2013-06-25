package com.example.orangeapihackaton.analysis.alchemyUtils;

import android.util.Log;
import api.AlchemyAPI;
import api.AlchemyAPI_NamedEntityParams;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/18/13
 * Time: 3:06 AM
 * To change this template use File | Settings | File Templates.
 */
//class used to interaction with Alchemy API
public class AlchemyCallFactory {


    public String AlchemyAPI_Key = "d7db4e835b005f9bc465b75d6dd779f0228cbd03";
    private String TAG = AlchemyCallFactory.class.getCanonicalName();

    public String SendAlchemyCall(OperationsAllowed call,String textToAnalyze)
    {
        Document doc = null;
        AlchemyAPI api = null;
        try
        {
            api = AlchemyAPI.GetInstanceFromString(AlchemyAPI_Key);
        }
        catch( IllegalArgumentException ex )
        {
            Log.d(TAG,"error: api key is wrong");

        }


        try{
            if( OperationsAllowed.CONCEPT.equals(call) )
            {
                Log.d("click", "concept");
                doc = api.TextGetRankedConcepts(textToAnalyze);
                Log.d("doc",doc.toString());
                return GetStringFromDocument(doc, false);
            }
            else if( OperationsAllowed.ENTITY.equals(call))
            {
                doc = api.TextGetRankedNamedEntities(textToAnalyze);
                return GetStringFromDocument(doc, false);
            }
            else if( OperationsAllowed.KEYWORD.equals(call))
            {
                doc = api.TextGetRankedKeywords(textToAnalyze);
                return GetStringFromDocument(doc, false);
            }
            else if( OperationsAllowed.TEXT.equals(call))
            {
                doc = api.TextGetTextSentiment(textToAnalyze);
                return GetStringFromDocument(doc, false);
            }
            else if( OperationsAllowed.SENTIMENT.equals(call))
            {
                AlchemyAPI_NamedEntityParams nep = new AlchemyAPI_NamedEntityParams();
                nep.setSentiment(true);
                doc = api.TextGetRankedNamedEntities(textToAnalyze,nep);
                //doc = api.URLGetRankedNamedEntities(textToAnalyze, nep);
                return GetStringFromDocument(doc, true);
            }else if(OperationsAllowed.HOUR.equals(call)){

                return customFindHour(textToAnalyze);
            }





        }
        catch(Throwable t)
        {
            Log.d(TAG, "Eroor : " + t.getMessage());
           // textview.setText("Error: " + t.getMessage());
        }
        return "";
    }

    private String customFindHour(String textToAnalyze) {

        List<String> keywords = new LinkedList<String>();
            keywords.add("at");
            keywords.add("on");

        String space = " ";
        String[] tokens = textToAnalyze.split(space);

        boolean nextHour = false;

        for(String t : tokens){
            if(keywords.contains(t)){
                nextHour = true;
            }
            else if(nextHour){
               return t;


            }

         }
         return "";
        //To change body of created methods use File | Settings | File Templates.
    }


    private String GetStringFromDocument(Document doc, boolean showSentiment)
    {
        //textview.setText("");
        if( doc == null )
            return "";
        Element root = doc.getDocumentElement();
        NodeList items = root.getElementsByTagName("text");
        String astring = "";
        if( showSentiment )
        {
            NodeList sentiments = root.getElementsByTagName("sentiment");
            for (int i=0;i<items.getLength();i++){
                Node concept = items.item(i);
                astring = concept.getNodeValue();
                astring = concept.getChildNodes().item(0).getNodeValue();
                //textview.append("\n" + astring);
                if( i < sentiments.getLength() )
                {
                    Node sentiment = sentiments.item(i);
                    Node aNode = sentiment.getChildNodes().item(1);
                    Node bNode = aNode.getChildNodes().item(0);
                    //textview.append(" (" + bNode.getNodeValue()+")");
                }
            }
        }
        else
        {
            for (int i=0;i<items.getLength();i++){
                Node concept = items.item(i);
                astring = concept.getNodeValue();
                astring = concept.getChildNodes().item(0).getNodeValue();
               // textview.append("\n" + astring);
            }
        }
        return astring;
    }
}
