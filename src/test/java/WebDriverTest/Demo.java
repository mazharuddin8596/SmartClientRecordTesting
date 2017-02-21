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
import org.json.simple.parser.JSONParser;

import util.AccessToken;
import util.CommonLibrary;
import util.HttpLibrary;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class Demo {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception
	{
		CommonLibrary lib = new CommonLibrary();
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
		System.out.println("Header details");
		HttpLibrary.printCurrentDataValues(header);

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
		//Map<String, String> fromExcel = lib.readFromSheet();
		HttpLibrary.printCurrentDataValues(fromExcel);
		
//******************************************************************//
		// NS Api call to get NS record data
		StringBuilder rl = HttpLibrary.doGET("contact", 55493);// 55493);
		JSONArray nsData = new JSONArray(rl.toString());
		org.json.JSONObject json = nsData.getJSONObject(0);
		// System.out.println(json.toString());

		// parsing JSON Response
		Configuration conf = Configuration.defaultConfiguration();

		Object document = conf.jsonProvider().parse(json.toString());
		// !! important System.out.println(JsonPath.read(document,
		// "$..addressbook[0].addressbookaddress.addressee").toString());

		String str = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")
				+ "//src//test//resources//enums.json")));

		org.json.simple.JSONObject enumsJson = (org.json.simple.JSONObject) new JSONParser()
				.parse(str);
		Object enums = Configuration.defaultConfiguration().jsonProvider()
				.parse(enumsJson.toString());

		ArrayList<String> res = new ArrayList<String>();
		res.add(0, "");
		System.out.println(head.size());
		// Print values from Ns JSON response
		for (int j = 1; j < head.size(); j++)
		{
			String[] data = head.get(j).toString().split("\\.");
			String value = "";
			//System.out.println("data: " + Arrays.toString(data));

			String Query = "";
			if (data.length > 2)
			{
				Query = lib.generateJaywayQueryString(data, header.get(head.get(j)));
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
					if (Query.contains("addressbookaddress"))
					{
						res.add(j, "");
					} else
					{
						value = JsonPath.read(document, "$" + Query).toString();
						res.add(j, value);
						System.out.println(value);
					}
				} else
				{
					//System.out.println("header.get(head.get(j)) " + header.get(head.get(j)));
					if (header.get(head.get(j)).equals("select"))
					{
						value = JsonPath.read(document, "$" + Query + ".name");
						res.add(j, value);
						System.out.println(value);
					} else if (header.get(head.get(j)).equals("enum"))
					{
						
						String s = JsonPath.read(document, "$" + Query + ".name").toString();
						value = s.substring(2, s.length() - 2);
						res.add(j, value);
						System.out.println(value);
					} else
					{
						value = JsonPath.read(document, "$" + Query).toString();
						System.out.println(value);
						res.add(j, value);

					}
				}
			} catch (PathNotFoundException e)
			{

				res.add(j, "");

			}
		}

		// Mapping header and row values as <Key,Value>
		Map<String, String> fromNS = HttpLibrary.mapHeaderWithRowData(head, res);
		HttpLibrary.printCurrentDataValues(fromNS);

	}
	

}
