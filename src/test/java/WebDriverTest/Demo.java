package WebDriverTest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import util.CommonLibrary;
import util.HttpLibrary;

public class Demo {

	public static void main(String[] args) throws Exception
	{
		CommonLibrary lib = new CommonLibrary();
		Properties template = lib.getTemplate();
		String fields = template.getProperty("contactTemplate");
		System.out.println("Header: " + fields);
		String idata = template.getProperty("contactInsert");
		System.out.println("data: " + idata);
		CommonLibrary.setAccessToken(HttpLibrary.getAccessTokenRestApi());
		HttpLibrary.setFieldsFormat(fields);
		HttpLibrary.printCurrentDataValues(CommonLibrary.getHeader());
		// String str = new
		// String(Files.readAllBytes(Paths.get("C:/Users/Mazhar/AppData/Roaming/Skype/My Skype Received Files/enums.json")));
		// String fields =
		// "Contact.NetSuite.1487671332805,.internalId,.firstName,.lastName,.subsidiary,.salutation,.company,.title,.mobilePhone,.officePhone,.phone,.email,.addressbookList.addressbook.addressbookAddress.addr1,.addressbookList.addressbook.addressbookAddress.addr2,.custentity_free_from_text,.custentity_pick_list";//
		// 1.23456789E9;
		
		 //Object data = Configuration.defaultConfiguration().jsonProvider().parse(str);
		Map<String, String> fromNS = lib.getFromNs("contact", 70712);
/*
		StringBuilder rl = HttpLibrary.doGET("contact", 70712);
		JSONArray nsData = new JSONArray(rl.toString());
		org.json.JSONObject json = nsData.getJSONObject(0);
		// System.out.println(json.toString());

		// parsing JSON Response
		Configuration conf = Configuration.defaultConfiguration();

		Object document = conf.jsonProvider().parse(json.toString());
		// !! important System.out.println(JsonPath.read(document,
		// "$..addressbook[0].addressbookaddress.addressee").toString());
		ArrayList<String> head = CommonLibrary.templateHeader(CommonLibrary.getHeader());
		ArrayList<String> res = new ArrayList<String>();
		res.add(0, "");
		System.out.println("NS Object: " + document.toString());
		// Print values from Ns JSON response
		for (int j = 1; j < head.size(); j++)
		{
			System.out.println("head value: " + head.get(j).toString());
			System.out.println("header " + CommonLibrary.header.get(head.get(j)));
			String[] data = head.get(j).toString().split("\\.");
			String value = "";
			String Query = "";
			if (data.length > 2)
			{
				Query = lib.generateJaywayQueryString(data, CommonLibrary.header.get(head.get(j))).toLowerCase();
				System.out.println(Query);
			} else
			{
				Query = head.get(j).toLowerCase();
				System.out.println(Query);
			}
			try
			{
				if (Query.contains("internalid"))
				{
					Query = Query.replaceAll("internalid", "id");
					if (Query.contains("addressbookaddress"))
					{
						res.add(j, "");
					} else
					{
						value = CommonLibrary.remSpecialCharacters(JsonPath.read(document, "$." + Query)
								.toString());
						res.add(j, value);
					}
					System.out.println(value);
				} else
				{
					if (CommonLibrary.header.get(head.get(j)).equals("select"))
					{
						value = CommonLibrary.remSpecialCharacters(JsonPath.read(document, "$" + Query + ".name")
								.toString());
						res.add(j, value);
					} else if (CommonLibrary.header.get(head.get(j)).equals("enum"))
					{
						String s = CommonLibrary.remSpecialCharacters(JsonPath.read(document, "$" + Query
								+ ".name").toString());
						res.add(j, value);
					} else
					{
						value = CommonLibrary.remSpecialCharacters(JsonPath.read(document, "$" + Query)
								.toString());
						res.add(j, value);
					}

					System.out.println(value);
				}
			} catch (PathNotFoundException e)
			{

				res.add(j, "");
				System.out.println(value);
			}
		}*/
		
		 System.out.println("\ndata from NS\n\n");
		 HttpLibrary.printCurrentDataValues(fromNS);

	}
}
