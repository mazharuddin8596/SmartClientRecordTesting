package WebDriverTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import util.BackgroundThread;
import util.CommonLibrary;
import util.HttpLibrary;

public class Demo {

	public static void main(String[] args) throws Exception
	{
		CommonLibrary lib = new CommonLibrary();
		Properties template =  lib.getTemplate();
		CommonLibrary.report = new ExtentReports(System.getProperty("user.dir")
				+ "\\Reports\\RecordTesting.html");
		ExtentTest logger = CommonLibrary.report.startTest("Inserting Contact");
		//WebDriver driver = lib.getDriver();
		CommonLibrary.setAccessToken(HttpLibrary.getAccessTokenRestApi());
		CommonLibrary.setSheet(HttpLibrary.sheetName());
		logger = CommonLibrary.report.startTest("Inserting Contact");
		
	    logger.log(LogStatus.INFO, "report thing");
		String fields = template.getProperty("contactcheckT");
		HttpLibrary.setFieldsFormat(fields);
		HashMap<String, String> fromExcel = (HashMap<String, String>) lib.rowData(2);
		HttpLibrary.printCurrentDataValues(fromExcel,logger);
		CommonLibrary.report.endTest(logger);
		CommonLibrary.report.flush();
			
		
	}
}
