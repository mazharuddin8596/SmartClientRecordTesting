package WebDriverTest;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import util.HttpLibrary;

public class Demo {

    public static void main(String[] args) throws Exception {
	JSONParser parser = new JSONParser();
	Object obj = parser.parse(new FileReader("D:\\compare JSON.json"));
	// JSONArray arr = (JSONArray) obj;
	// org.json.JSONObject jsonObject = (org.json.JSONObject) obj;
	// String jsonText = JsonWriter.formatJson(jsonObject.toString());
	// System.out.println(jsonText);

	String content = new String(Files.readAllBytes(Paths
		.get("D:\\compare JSON.json")));
	org.json.JSONObject jsonObject = new JSONObject(content);

	String content1 = new String(Files.readAllBytes(Paths
		.get("D:\\compare JSON.json")));
	org.json.JSONObject jsonObject1 = new JSONObject(content1);

	// parsing JSON Response
	// Configuration conf = Configuration.defaultConfiguration();

	// Object document = conf.jsonProvider().parse(jsonText.toString());
	String fields = "Contact.NetSuite.1487671332805,.internalId,.firstName,.lastName,.subsidiary,.salutation,.company,.title,.mobilePhone,.officePhone,.phone,.email,.addressbookList.addressbook.addressbookAddress.addr1,.addressbookList.addressbook.addressbookAddress.addr2,.custentity_free_from_text,.custentity_pick_list,.addressbookList.addressbook.addressbookAddress.internalId,.addressbookList.addressbook.addressbookAddress.country";
	// List<String> temp = (ArrayList<String>) ;
	// ArrayList<Foo> list = new ArrayList<>(Arrays.asList(sos1.getValue());
	ArrayList<String> head = new ArrayList<>(Arrays.asList(fields
		.split("\\,")));
	// System.out.println(head.toString());
	HttpLibrary.setFieldsFormat(fields);
	@SuppressWarnings("unchecked")
	Iterator<String> keys = jsonObject.keys();
	while (keys.hasNext()) {
	    String key = keys.next();
	    System.out.println(key);
	}

	/*
	 * for (Iterator iterator = jsonObject.keySet().iterator(); iterator
	 * .hasNext();) { String key = (String) iterator.next();
	 * System.out.println(key); System.out.println(jsonObject.get(key)); //
	 * Thread.sleep(23000); }
	 */

    }
}