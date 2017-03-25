package WebDriverTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import util.BackgroundThread;
import util.CommonLibrary;
import util.HttpLibrary;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class RecordTesting {

	CommonLibrary lib = new CommonLibrary();
	HttpLibrary http = new HttpLibrary();
	WebDriver driver;
	Properties obj = lib.getObj();
	Properties temp = lib.getTemplate();
	ExtentTest logger;
	Properties data = lib.getData();
	int id;
	private String recordType = "";
	private String templateName = "";
	private String templateFields = "";
	private String insertValues = "";
	private String updateValues = "";

	public RecordTesting(
			String recordType,
			String templateName,
			String templateFields,
			String insertValues,
			String updateValues)
	{
		this.recordType=recordType;
		this.templateName = templateName;
		this.templateFields = templateFields;
		this.insertValues = insertValues;
		this.updateValues = updateValues;
		lib.setDriver(driver);
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	@BeforeSuite
	public void InitialSetup() throws Exception
	{
		System.out.println("initial setup");
		lib.beforeTest();
		CommonLibrary.setAccessToken(HttpLibrary.getAccessTokenRestApi());
		CommonLibrary.report = new ExtentReports(System.getProperty("user.dir")
				+ "\\Reports\\RecordTesting.html");
		System.out.println("Report path" + System.getProperty("user.dir")
				+ "\\Reports\\RecordTesting.html");
		driver = lib.getDriver();

		Runnable r = new BackgroundThread(driver);
		Thread thread = new Thread(r);
		thread.setDaemon(true);
		thread.start();

		lib.officeLogin(driver);
		Thread.sleep(4000);
		lib.handleMSDialogBox();
		lib.switchIntoSheet();
		lib.waitForOfficeAddin();
		driver.findElement(By.cssSelector("table.moe-infobar-infotable tbody td.moe-infobar-button-cell button"))
				.click();
		System.out.println("Clicked on Start button in App");
		Thread.sleep(2000);
		lib.switchToApp();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.welcomepage p.microsoft-btn a")).click();
		ArrayList<String> allTabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(allTabs.get(3));
		List<WebElement> sign = driver.findElements(By
				.cssSelector("div#main-panel-content table.tile td.tile-name"));
		sign.get(0).click();
		driver.switchTo().window(allTabs.get(2));
		//lib.setDriver(driver);
		lib.switchIntoSheet();
	}

	public void loadTemplateAndPerformDataOperation(
			String Template,
			String templateFields,
			String values,
			CommonLibrary.App clickOn) throws InterruptedException
	{
		lib.switchIntoSheet();
		Thread.sleep(2000);
		lib.switchToApp();
		Thread.sleep(700);
		System.out.println(driver);
		driver.findElement(By.cssSelector("a[title='Menu']")).click();
		driver.findElement(By.linkText("Templates")).click();
		Thread.sleep(1000);

		logger.log(LogStatus.INFO, "Loaded '" + Template + "' template");
		lib.loadTemplate(Template);
		lib.waitUntilLoadingEnds();
		lib.switchIntoSheet();
		Thread.sleep(1500);
		logger.log(LogStatus.INFO, templateFields);
		System.out.println("Header: " + templateFields);
		System.out.println("data: " + values);
		logger.log(LogStatus.INFO, values);
		lib.insertDataIntoTemplate(values);
		lib.switchToApp();
		try
		{
			WebElement modalWindow = driver.findElement(By
					.cssSelector("div.modal-content div.alertAction button"));
			modalWindow.click();
			System.out.println("Modal window is dislayed");
		} catch (Exception e)
		{
			System.out.println("Modal window is not dislayed");
		}
		logger.log(LogStatus.INFO, "Inserting Data into Template");
		lib.clickOn(clickOn);

		String notification = lib.getNotification();
		System.out.println(notification);
		logger.log(LogStatus.INFO, notification);
	}

	public boolean getFromNsAndCompare(HashMap<String, String> fromExcel,String recordType, boolean success) throws IOException, ParseException
	{
		ArrayList<String> head = CommonLibrary.templateHeader(CommonLibrary.getHeader());

		success = fromExcel.get(head.get(0)).equals("");
		if (success)
		{
			id = lib.getRecordId(fromExcel);
			System.out.println("id: " + id);
			setId(id);
			logger.log(LogStatus.PASS, Integer.toString(id));
		} else
		{
			setId(0);
			logger.log(LogStatus.FAIL, "insertion failed \n Error message :"
					+ fromExcel.get(head.get(0)));
		}

		HttpLibrary.printCurrentDataValues(fromExcel, logger);

		if (getId() != 0)
		{
			logger.log(LogStatus.PASS, "Getting data from NS");
		//	Map<String, String> fromNS = lib.getFromNs("contact", id);
			Map<String, String> fromNS = lib.getFromNs(recordType, id);
			System.out.println("\ndata from NS\n\n");
			HttpLibrary.printCurrentDataValues(fromNS, logger);
			if (!lib.compareData(fromExcel, fromNS, head.get(0)))
			{
				logger.log(LogStatus.FAIL, "Data Mismatch");
				System.out.println("Data Mismatch");
				return false;
			} else
			{
				logger.log(LogStatus.PASS, "Cheers!! insertion is successfull");
				return true;
			}
		} else
		{
			logger.log(LogStatus.FAIL, "Failed because " + fromExcel.get(head.get(0)));
			System.out.println("insertion failed \n Error message :" + fromExcel.get(head.get(0)));
			Assert.fail();
			return false;
		}
	}



	@Test(priority = 0)
	public void insertOperation() throws Exception
	{
		System.out.println(templateName);
		logger = CommonLibrary.report.startTest("Inserting Contact");
		boolean success = false;
		// String fields = temp.getProperty("contactTemplate");
		String fields = templateFields;
		// String values = temp.getProperty("contactInsert");
		String values = insertValues;
		// loadTemplateAndPerformDataOperation("Add Contacts 17", fields,
		// values, CommonLibrary.App.InsertAllRows);
		//System.out.println("Template name " + (String) templateName);
		System.out.println("fields " + fields);
		System.out.println("values " + values);
		System.out.println("update values " + updateValues);
		loadTemplateAndPerformDataOperation((String) templateName, fields, values, CommonLibrary.App.InsertAllRows);
		HttpLibrary.setFieldsFormat(fields);
		HashMap<String, String> fromExcel = (HashMap<String, String>) lib.rowData(2);
		getFromNsAndCompare(fromExcel,recordType, success);
		System.out.println("******************");
	}

	@Test(priority = 1)
	public void updateOperation() throws Exception
	{
		System.out.println(templateName);
		logger = CommonLibrary.report.startTest("Update Contact");
		String fields = templateFields;
		String substr = lib
				.appendIdToUpdateTemplateValues(updateValues, getId());
		System.out.println("[" + substr + "]");
		boolean success = false;
		//String fields = temp.getProperty("contactTemplate");
		loadTemplateAndPerformDataOperation((String) templateName, fields, substr, CommonLibrary.App.InsertAllRows);
		HttpLibrary.setFieldsFormat(fields);
		HashMap<String, String> fromExcel = (HashMap<String, String>) lib.rowData(2);
		getFromNsAndCompare(fromExcel,recordType, success);
		System.out.println("******************");
	}

	@Test(priority = 2)
	public void refreshOperation() throws Exception
	{
		System.out.println(templateName);
		logger = CommonLibrary.report.startTest("Refresh Contact");
		String substr = ","+getId()+",";
		System.out.println("[" + substr + "]");
		boolean success = false;
		String fields = templateFields;
		loadTemplateAndPerformDataOperation((String) templateName, fields, substr, CommonLibrary.App.RefreshSelectedRows);
		HttpLibrary.setFieldsFormat(fields);
		
		HashMap<String, String> fromExcel = (HashMap<String, String>) lib.rowData(2);
		getFromNsAndCompare(fromExcel,recordType, success);
		System.out.println("******************");
	}

	@Test(priority = 3)
	public void deleteOperation() throws Exception
	{
		System.out.println(templateName);
		Thread.sleep(2000);
		lib.switchIntoSheet();
		logger = CommonLibrary.report.startTest("delete Contact");
		int id = getId();
		logger.log(LogStatus.INFO, "Deleting record " + id);
		Keyboard press = ((HasInputDevices) driver).getKeyboard();
		press.pressKey(Keys.chord(Keys.CONTROL, Keys.HOME));
		press.pressKey(Keys.DOWN);
		press.pressKey(Keys.DOWN);
		lib.switchToApp();
		Thread.sleep(1000);
		logger.log(LogStatus.INFO, "Delete button pressed");
		lib.clickOn(CommonLibrary.App.DeleteSelectedRows);
		lib.waitUntilLoadingEnds();
		Thread.sleep(5000);
		StringBuilder rl = HttpLibrary.doGET("contact", getId());
		System.out.println(rl.toString());
		if (rl.toString().equals("[]"))
		{
			logger.log(LogStatus.PASS, "Successfully Deleted record");
		} else
		{
			logger.log(LogStatus.FAIL, "Record is not deleted");
			Assert.fail("Record is not deleted :( ");
			HashMap<String, String> fromExcel = (HashMap<String, String>) lib.rowData(2);
			System.out.println(fromExcel.get(0));
		}

	}
	@AfterMethod
	public void tearDown(ITestResult result)
	{
		if (result.getStatus() == ITestResult.FAILURE)
		{
			logger.log(LogStatus.FAIL, result.getName() + " function is fail");
		}
		CommonLibrary.report.endTest(logger);
		CommonLibrary.report.flush();
	}

}
