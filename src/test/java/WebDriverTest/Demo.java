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
				+ "/workbook/tables/" + lib.tableId() + "/HeaderRowRange";

		// getting header of table
		org.json.JSONObject jo = HttpLibrary.restGet(URL, CommonLibrary.getAccessToken());
		CommonLibrary.setHeader((HashMap<String, String>) HttpLibrary.parseHeaderRowData(jo));
		
		ArrayList<String> head = lib.templateHeader(CommonLibrary.getHeader());

		// getting specified row data from table
				String URLrows = "https://graph.microsoft.com/v1.0/me/drive/items/" + workbookId
						+ "/workbook/tables/" + lib.tableId() + "/rows";
				// System.out.println();
				org.json.JSONObject rows = HttpLibrary.restGet(URLrows, CommonLibrary.getAccessToken());
				ArrayList<String> rowData = HttpLibrary.getRowAtIndex(rows, 0);
				System.out.println("Row 0" + ": " + rowData);
				for (int k = 0; k < 2; k++)
				{
					System.out.println(rowData.get(0) + "&&" + rowData.get(1));
					if (rowData.get(0).equals("") && rowData.get(1).equals(""))
					{
						rows = HttpLibrary.restGet(URLrows, CommonLibrary.getAccessToken());
						rowData = HttpLibrary.getRowAtIndex(rows, 0);
						System.out.println("Row 0" + ": " + rowData);
					} else
					{
						break;
					}
				}
				/*
				 * Map<String, String> fromExcel =
				 * HttpLibrary.mapHeaderWithRowData(head, rowData); for (int k = 0; k <
				 * 4; k++) { if (fromExcel.get(head.get(0)).equals("") &&
				 * fromExcel.get(head.get(1)).equals("")) {
				 * 
				 * Thread.sleep(1500); fromExcel = rowData(i); // fromExcel =
				 * HttpLibrary.mapHeaderWithRowData(head, rowData); } else { break; } }
				 */
				Map<String, String> fromExcel = HttpLibrary.mapHeaderWithRowData(head, rowData);
				

		

	}
	

}
