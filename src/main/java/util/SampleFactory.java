package util;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

public class SampleFactory {
	@Factory(dataProvider = "dp")
	public Object[] createInstances(String s, String str)
	{
		return new Object[]{new RandomTest(s, str)};
	}

	@DataProvider(name = "dp", parallel = false)
	public static Object[][] dataProvider()
	{
		Object[][] dataArray = {{"1", "user1"}, {"2", "user2"}, {"3", "mazhar"}, {"5", "five"},
				{"6", "6"}, {"4", "4 me"}};
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
