package api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AlchemyAPI_ConstraintQueryParams extends AlchemyAPI_Params{
		
	private String cQuery;
		
	public String getCQuery() {
		return cQuery;
	}
	public void setCQuery(String cQuery) {
		this.cQuery = cQuery;
	}
	
		
	public String getParameterString(){
		String retString = super.getParameterString();
		try{
			if(cQuery!=null) retString+="&cquery="+URLEncoder.encode(cQuery,"UTF-8");
		}
		catch(UnsupportedEncodingException e ){
			retString = "";
		}
		return retString;
	}
		
	
		
}
	
