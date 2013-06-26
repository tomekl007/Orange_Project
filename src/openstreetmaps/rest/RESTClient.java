package openstreetmaps.rest;

import android.util.Log;
import openstreetmaps.Data.FileHelper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class RESTClient 
{
	HttpEntity to_consume = null;
	
	public void test(String url)
	{
		try {
		    String data = "";
		    //
		    CredentialsProvider credProvider = new BasicCredentialsProvider();
		    credProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
		        new UsernamePasswordCredentials("hackathon45", "2VuChZrk"));
		    //
		    DefaultHttpClient http = new DefaultHttpClient();
		    http.setCredentialsProvider(credProvider);
		    //
		    HttpPut put = new HttpPut(url);
		    try {
		        put.setEntity(new StringEntity(data, "UTF8"));
		    } catch (UnsupportedEncodingException e) {
		        Log.e("Html", "UnsupportedEncoding: ", e);
		    }
		    put.addHeader("Content-type","");
		    HttpResponse response = http.execute(put);
		    Log.i("Html", "This is what we get back:"+response.getStatusLine().toString()+", "+response.getEntity().toString());
		} catch (ClientProtocolException e) {
		    //
		    Log.i("Html", "Client protocol exception", e);
		} catch (IOException e) {
		    //
		    Log.i("Html", "IOException", e);
		}		
	}
	
	
	public void test2(String url)
	{
		listInputStream(sendRequest(url));
		//sendRequest(url);
	}
	
	//hackathon45
	//2VuChZrk
	
	public void test3(String url)
	{
		DefaultHttpClient client = HttpsClientBuilder.getBelieverHttpsClient();
		//DefaultHttpClient client = new DefaultHttpClient();
		Credentials creds = new UsernamePasswordCredentials("hackathon45", "2VuChZrk");
		client.getCredentialsProvider().setCredentials(new AuthScope(url, 443), creds);

		String username = "hackathon45";
		String password = "2VuChZrk";
		String login = username + ":" + password;
		String base64login = new String(Base64.encodeBase64(login.getBytes()));
		
		Log.i("Html", "login: " + base64login);
		
		try {
		    HttpPost post = new HttpPost(url);
		    post.addHeader("Authorization", "Basic " + base64login);
		    
//		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);   
//		    nameValuePairs.add(new BasicNameValuePair("score", points));
//		    nameValuePairs.add(new BasicNameValuePair("name", name));
//		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    HttpResponse response = client.execute(post);
		    
		    if(response == null)
		    	Log.i("html", "-=-=-=-=");
		    
		    final int statusCode = response.getStatusLine().getStatusCode();
//		    if (statusCode != HttpStatus.SC_OK) 
//		        throw new Exception();
		    
		    HttpEntity entity1 = response.getEntity();
            //to_consume = entity1;
            InputStream is = entity1.getContent();
            
            listInputStream(is);
            entity1.consumeContent();
            
		} catch (UnsupportedEncodingException e) {
		    Log.i("Html", "Uns exception" + e.getMessage());
		} catch (IOException e) {
		    Log.i("Html", "IO exception" + e.getMessage());
		} catch (Exception e) {
		    Log.i("Html", "generic exception" + e.getMessage());
		}		
	}
	
	protected InputStream sendRequest(String url)
	{
		String username = "hackathon45";
		String password = "2VuChZrk";
		String login = username + ":" + password;
		String base64login = new String(Base64.encodeBase64(login.getBytes()));
		
		try 
        {
        	//Log.i("response", "URL: " + url);
			        		    
        	CookieStore cookieStore = new BasicCookieStore();
        	HttpContext localContext = new BasicHttpContext();
        	localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);        	
        	
        	//DefaultHttpClient httpclient = new DefaultHttpClient();
            
        	DefaultHttpClient httpclient = HttpsClientBuilder.getBelieverHttpsClient();
        	
        	HttpGet httpGet = new HttpGet(url);
        	httpGet.addHeader("Authorization", "Basic " + base64login);
//		    post.addHeader("Authorization", "Basic " + base64login);

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
	
	public void consumeContent()
	{
		try
		{
	        to_consume.consumeContent();
		}
		catch(Exception e)
		{
			
		}
	}
	
	protected void listInputStream(InputStream is)
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
	
	protected void getDataToFile(String url, File file)
	{
		InputStream is = sendRequest(url);

		try
		{
			FileHelper.saveToFile(is, file);
		}
		catch(Exception e)
		{
			Log.i("busstop", "exception: " + e.getMessage());
		}		
	}

	public static boolean checkForError(Document doc)	//with JSOUP Document
	{
		Elements error = doc.select("dataError");
		
		if(!error.isEmpty())	//there is error
	    {
	    	Log.i("line", "error while downloading data from API");
	    	
	    	for(Element err : error)	//there will be max one
	    		for(Element e : err.children())
	    		{
	    			if(e.nodeName().equals("status"))
	    			{
	    				Log.i("line", "status: " + e.text());
	    			}
	    			else if(e.nodeName().equals("description"))
	    			{
	    				Log.i("line", "description: " + e.text());		    			
	    			}
	    		}
	    
	    	return true;
	    }
	
		return false;
	}
	
/*
	protected String constructURL()
	{
		StringBuilder sb = new StringBuilder();
		
		return sb.toString();
	}
*/
}
