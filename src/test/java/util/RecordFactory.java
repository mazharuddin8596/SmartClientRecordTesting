package util;

import java.util.Properties;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import WebDriverTest.RecordTesting;

public class RecordFactory {
	static CommonLibrary lib = new CommonLibrary();
	static Properties temp = lib.getTemplate();
	@Factory(dataProvider = "dp")
	public Object[] createInstances(
			String recordType,
			String name,
			String fields,
			String insertValues,
			String updateValues)
	{
		return new Object[]{new RecordTesting(recordType, name, fields, insertValues, updateValues)};
	}

	@DataProvider(name = "dp")
	public static Object[][] dataProvider()
	{
		Object[][] dataArray = {
				{"contact", "Add Contacts 17", temp.getProperty("contactTemplate"),
						temp.getProperty("contactInsert"), temp.getProperty("contactUpdate")}
			,{"customer", "Customer 17", temp.getProperty("customerTemplate"),temp.getProperty("customerInsert"), temp.getProperty("customerUpdate")}
						};
		return dataArray;
	}
}

/*
 * 
 * <parameter name="browser" value="chrome" /> <parameter name="TestingType"
 * value="SanityPhase1.html" /> <test name="HE Phase 1"> <classes> <!-- <class
 * name="com.test.Login_Validation"> <methods> <include name="inValidCreds" />
 * <include name="Logged_Out_LicenseNetsuiteAccount" /> </methods> </class>
 * <class name="com.test.Attach_mail_and_Verify_In_NS"> <methods> <include
 * name="attach_To_Single_RecordFrom_SenderList_With_Attachments" /> </methods>
 * </class> <class name="com.test.test1"></class> </classes> </test>
 */
