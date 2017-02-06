package WebDriverTest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import util.AccessToken;
import util.HttpLibrary;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class Demo {

	//static HttpLibrary http =new HttpLibrary();
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception
	{

		AccessToken accessToken;
		accessToken = HttpLibrary.getAccessTokenRestApi();
		String workbookId = "01JNEAOJ47H5SYYKQIWRF3NLGJZJM2GQRE";
		String tableID = HttpLibrary.getTableId(workbookId, accessToken);
		System.out.println(tableID);

		String URL = "https://graph.microsoft.com/v1.0/me/drive/items/" + workbookId
				+ "/workbook/tables/" + tableID + "/HeaderRowRange";
		String URLrows = "https://graph.microsoft.com/v1.0/me/drive/items/" + workbookId
				+ "/workbook/tables/" + tableID + "/rows";

		// getting header of table
		org.json.JSONObject jo = HttpLibrary.restGet(URL, accessToken);

		HashMap<String, String> header = (HashMap<String, String>) HttpLibrary.getHeaderRowData(jo);
        HttpLibrary.printCurrentDataValues(header);
		/*
		 * String[] head = test.split("\\,"); for (String s : head)
		 * System.out.println("header list: " + s);
		 */
		// Getting type from JSON
		// $..[?(@.recordType=="Address" && @.name== "country" )].type
		// $..[?(@.id=="Contact.addressbookList.addressbook.addressbookAddress.addr1")].type
/*
		// getting specified row data from table
		org.json.JSONObject rows = HttpLibrary.restGet(URLrows, accessToken);
		ArrayList<String> rowData = HttpLibrary.getRowAtIndex(rows, 0);
		System.out.println("Row 0 :" + rowData);

		// Getting header row text
		ArrayList<String> head = new ArrayList<String>();
		Set set = header.entrySet();
		Iterator i = set.iterator();
		while (i.hasNext())
		{
			Map.Entry me = (Map.Entry) i.next();
			head.add((String) me.getKey());

		}

		// Mapping header and row values as <Key,Value>
		Map<String, String> fromExcel = HttpLibrary.mapHeaderWithRowData(head, rowData);
		HttpLibrary.printCurrentDataValues(fromExcel);

		// NS Api call to get NS record data
		StringBuilder rl = HttpLibrary.doGET("contact", 54278);// 55493);
		JSONArray nsData = new JSONArray(rl.toString());
		org.json.JSONObject json = nsData.getJSONObject(0);

		// parsing JSON Response
		Object document = Configuration.defaultConfiguration().jsonProvider()
				.parse(json.toString());

		ArrayList<String> res = new ArrayList<String>();
		res.add(0, "");
		// Print values from Ns JSON response
		for (int j = 2; j < head.size(); j++)
		{
			String[] data = head.get(j).toString().split("\\.");
			String value = ""; //
			System.out.println(Arrays.toString(data));

			String Query = "";
			if (data.length > 2)
			{
				Query = generateJaywayQueryString(data);
			} else
			{
				Query = head.get(j);
			}
			System.out.println("Query : " + Query);
			try
			{
				if (Query.contains("internalid"))
				{
					System.out.println("its internal id");
					Query = Query.replaceAll("internalid", "id");
					System.out.println("replacing Query : " + Query);
					if (Query.contains("addressbookaddress"))
					{
						res.add("");
					} else
					{
						value = JsonPath.read(document, "$" + Query);
						System.out.println(value);
						res.add(value);
					}
				} else
				{
					if (header.get(head.get(j)).equals("@"))
					{
						value = JsonPath.read(document, "$" + Query + ".name");
						res.add(value);
						System.out.println(value);
					} else
					{
						value = JsonPath.read(document, "$" + Query);
						System.out.println(value);
						res.add(value);

					}
				}
			} catch (PathNotFoundException e)
			{

				res.add("");

			}
		}

		// Mapping header and row values as <Key,Value>
		Map<String, String> fromNS = HttpLibrary.mapHeaderWithRowData(head, res);
		HttpLibrary.printCurrentDataValues(fromNS);
	*/
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
