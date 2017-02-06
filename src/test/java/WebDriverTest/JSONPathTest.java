package WebDriverTest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import util.AccessToken;
import util.HttpLibrary;


import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class JSONPathTest {

	public static void main(String[] args) throws Exception
	{
		AccessToken accessToken;
		String value = "";
		String Query = "";
		ArrayList<String> list = new ArrayList<String>();
		//accessToken = HttpLibrary.getAccessTokenRestApi();
		
		ArrayList<String> head=new ArrayList<String>();//Creating arraylist  
		head.add(".firstname");
		head.add(".addressbooklist.addressbook.addressbookaddress.internalid");
		head.add(".addressbooklist.addressbook.addressbookaddress.custrecord1481");
		head.add(".addressbooklist.addressbook.addressbookaddress.country ");
		  
		
		//String tableID = HttpLibrary.getTableId("01JNEAOJ2R2GPS4HFBPVEIN63YKYD3RGDO", accessToken);
		//System.out.println(tableID);
		// NS Api call to get NS record data
				StringBuilder rl = HttpLibrary.doGET("contact",55493); //54278);//
				JSONArray nsData = new JSONArray(rl.toString());
				JSONObject json = nsData.getJSONObject(0);
				
				for(int j=0;j<head.size();j++){
				String[] data = head.get(j).toString().split("\\.");
				
				if (data.length > 2)
				{
					Query = generateJaywayQueryString(data);
				} else
				{
					Query = head.get(j);
				}
	
				System.out.println("Query : " + Query);
				
				// parsing JSON Response
				//Configuration conf = Configuration.defaultConfiguration();
		    	// conf1 = conf.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
				Configuration conf = Configuration.builder()
						   .options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
				Object document = conf.jsonProvider()
						.parse(json.toString());
				Configuration conf2 = conf.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
				//JsonPath.using(conf).parse(json).read("$..custrecord1481");
				
				System.out.println("null "+JsonPath.using(conf2).parse(json).read("$..addressbook..addressbookaddress.custrecord1481"));
				try{
				value = JsonPath.read(document, "$" + Query);
				System.out.println("value: "+value);
				}catch(java.lang.ClassCastException e){
					System.out.println("direct print : "+JsonPath.read(document, "$" + Query));
					Object k = JsonPath.read(document, "$" + Query);
					System.out.println(k.toString());
					//System.out.println(list);
				}
				
				}
				
	}
	public static String generateJaywayQueryString(String[] data)
	{
		String query = "";
		for (int k = 2; k < data.length; k++)
		{
			if (k == data.length - 1)
			{
				query += "." + data[k];
			} else
			{
				query += ".." + data[k];
			}
		}
		return query;
	}
}
