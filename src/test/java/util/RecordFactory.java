package util;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.parser.JSONParser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import WebDriverTest.RecordTestingForSingleRecords;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class RecordFactory {

    @Factory(dataProvider = "dp")
    public Object[] createInstances(String recordType, String name,
	    String fields, String insertValues, String updateValues) {

	return new Object[] { new RecordTestingForSingleRecords(recordType,
		name, fields, insertValues, updateValues) };
    }

    @DataProvider(name = "dp")
    public static Object[][] dataProvider() throws Exception {
	String str = new String(Files.readAllBytes(Paths.get(System
		.getProperty("user.dir")
		+ "//src//main//resources//properties//dataProvider.json")));

	org.json.simple.JSONObject metaData = (org.json.simple.JSONObject) new JSONParser()
		.parse(str);
	Object document = Configuration.defaultConfiguration().jsonProvider()
		.parse(metaData.toString());

	Object[][] dataArray = {
	/*
	 * { "contact", "Add Contacts 17", JsonPath.read(document,
	 * "$.contactTemplate").toString(), JsonPath.read(document,
	 * "$.contactInsert").toString(), JsonPath.read(document,
	 * "$.contactUpdate").toString() },
	 */
	{ "customer", "Customer 17",
		JsonPath.read(document, "$.customerTemplate").toString(),
		JsonPath.read(document, "$.customerInsert").toString(),
		JsonPath.read(document, "$.customerUpdate").toString() } };
	return dataArray;
    }
}
