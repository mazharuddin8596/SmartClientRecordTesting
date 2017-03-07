package WebDriverTest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

import com.jayway.jsonpath.Configuration;

import util.CommonLibrary;
import util.HttpLibrary;

public class Demo {
	
	public static void main(String[] args) throws Exception
	{
		CommonLibrary lib = new CommonLibrary();
		Properties template = lib.getTemplate();
		String fields = template.getProperty("contactTemplate");
		System.out.println("Header: "+fields);
		String idata = template.getProperty("contactInsert");
		System.out.println("data: "+idata);
		CommonLibrary.setAccessToken(HttpLibrary.getAccessTokenRestApi());
	   // String str = new String(Files.readAllBytes(Paths.get("C:/Users/Mazhar/AppData/Roaming/Skype/My Skype Received Files/enums.json")));
		//String fields = "Contact.NetSuite.1487671332805,.internalId,.firstName,.lastName,.subsidiary,.salutation,.company,.title,.mobilePhone,.officePhone,.phone,.email,.addressbookList.addressbook.addressbookAddress.addr1,.addressbookList.addressbook.addressbookAddress.addr2,.custentity_free_from_text,.custentity_pick_list";// 1.23456789E9;
		//HttpLibrary.setFieldsFormat(fields);	
	   // Object data = Configuration.defaultConfiguration().jsonProvider().parse(str);
		
		HttpLibrary.setFieldsFormat(fields);

		HashMap<String, String> fromExcel = (HashMap<String, String>) lib.rowData(2);
		HttpLibrary.printCurrentDataValues(fromExcel);
		
	}
}
