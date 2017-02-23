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
		CommonLibrary.setTableId(lib.tableId());
		String URL = "https://graph.microsoft.com/v1.0/me/drive/items/" + workbookId
				+ "/workbook/tables/" + CommonLibrary.getTableId() + "/HeaderRowRange";

		// getting header of table
		org.json.JSONObject jo = HttpLibrary.restGet(URL, CommonLibrary.getAccessToken());
		CommonLibrary.setHeader((HashMap<String, String>) HttpLibrary.parseHeaderRowData(jo));
		
		ArrayList<String> head = lib.templateHeader(CommonLibrary.getHeader());

		int id = 63304;
		System.out.println(id);
		Map<String, String> fromNS = lib.getFromNs(head, "contact", id);
		System.out.println("\ndata from NS\n\n");
		HttpLibrary.printCurrentDataValues(fromNS);
		String s="";
		//s=fromNS.get(".internalid").trim();
		s="\"\"";
		System.out.println(s);
		s = lib.remExtraCharacters(s);
		int i = Integer.parseInt(s);
		System.out.println("i: " + i);

		

	}
	

}
