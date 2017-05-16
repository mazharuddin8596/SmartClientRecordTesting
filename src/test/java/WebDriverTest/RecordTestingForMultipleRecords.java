package WebDriverTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import ExtentManager.ExtentManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class RecordTestingForMultipleRecords {

    CommonLibrary lib = new CommonLibrary();
    HttpLibrary http = new HttpLibrary();
    // Properties temp = lib.getTemplate();
    Properties data = lib.getData();
    Properties obj = lib.getObj();
    static WebDriver driver;
    static ExtentReports report;
    static ExtentTest logger;
    int id;
    private String recordType = "";
    private String templateName = "";
    private String templateFields = "";
    private ArrayList<String> insertValues = new ArrayList<String>();
    ArrayList<String> recordsToVerify = new ArrayList<String>();

    private String updateValues = "";

    public RecordTestingForMultipleRecords(String recordType,
	    String templateName, String templateFields,
	    ArrayList<String> insertValues, String updateValues) {
	this.recordType = recordType;
	this.templateName = templateName;
	this.templateFields = templateFields;
	this.insertValues = insertValues;
	this.updateValues = updateValues;

    }

    public void setId(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }

    @BeforeSuite
    public void InitialSetup() throws Exception {
	System.out.println("initial setup");
	lib.beforeTest();
	CommonLibrary.setAccessToken(HttpLibrary.getAccessTokenRestApi());
	report = ExtentManager.GetExtent();
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
	driver.findElement(
		By.cssSelector("table.moe-infobar-infotable tbody td.moe-infobar-button-cell button"))
		.click();
	System.out.println("Clicked on Start button in App");
	Thread.sleep(2000);
	lib.switchToApp();
	Thread.sleep(1000);
	JavascriptExecutor jse = (JavascriptExecutor) driver;
	jse.executeScript("scroll(0, 250);");
	driver.findElement(By.cssSelector("div.actionbox a")).click();
	Thread.sleep(1000);
	driver.findElement(By.cssSelector("div[align='center'] a")).click();
	Thread.sleep(500);
	driver.findElement(By.cssSelector("input[placeholder='Username']"))
		.sendKeys(data.getProperty("Emailid"));
	driver.findElement(By.cssSelector("input[placeholder='Password']"))
		.sendKeys(data.getProperty("pwd"));
	driver.findElement(By.linkText("Login")).click();
	Thread.sleep(3000);
	lib.switchIntoSheet();
    }

    public void loadTemplateAndPerformDataOperation(String Template,
	    String templateFields, ArrayList<String> values,
	    CommonLibrary.App clickOn,
	    com.aventstack.extentreports.ExtentTest logger)
	    throws InterruptedException, IOException {
	Keyboard press = ((HasInputDevices) driver).getKeyboard();

	lib.switchIntoSheet();
	Thread.sleep(3500);
	lib.switchToApp();
	/*
	 * Files.write(Paths.get("D:/fileName.html"), driver.getPageSource()
	 * .getBytes());
	 */
	Thread.sleep(1000);
	driver.findElement(By.cssSelector("a[title='Menu']")).click();
	Thread.sleep(500);
	driver.findElement(By.linkText("Templates")).click();
	Thread.sleep(1000);
	logger.log(Status.INFO, "Loaded '" + Template + "' template");
	lib.loadTemplate(Template);
	lib.waitUntilLoadingEnds();
	lib.switchIntoSheet();
	Thread.sleep(1500);
	logger.log(Status.INFO, templateFields);
	System.out.println("Header: " + templateFields);
	System.out.println("data: " + values);
	logger.log(Status.INFO, values.toString());
	press.pressKey(Keys.chord(Keys.CONTROL, Keys.HOME));
	Thread.sleep(200);
	press.pressKey(Keys.LEFT);
	press.pressKey(Keys.DOWN);
	press.pressKey(Keys.DOWN);
	for (String s : values)

	{
	    lib.insertDataIntoTemplate(s);
	    Thread.sleep(1000);
	    press.pressKey(Keys.chord(Keys.HOME));
	    Thread.sleep(100);
	    press.pressKey(Keys.chord(Keys.DOWN));

	}
	press.pressKey(Keys.chord(Keys.CONTROL, Keys.HOME));
	press.pressKey(Keys.chord(Keys.DOWN));
	press.pressKey(Keys.chord(Keys.DOWN));
	for (int m = 1; m < values.size(); m++) {
	    press.pressKey(Keys.chord(Keys.SHIFT, Keys.DOWN));
	}
	lib.switchToApp();
	try {
	    WebElement modalWindow = driver.findElement(By
		    .cssSelector("div.modal-content div.alertAction button"));
	    modalWindow.click();
	    System.out.println("Modal window is dislayed");
	} catch (Exception e) {
	    System.out.println("Modal window is not dislayed");
	}
	logger.log(Status.INFO, "Inserting Data into Template");
	lib.clickOn(clickOn);

	String notification = lib.getNotification();
	System.out.println(notification);
	logger.log(Status.INFO, notification);
    }

    @SuppressWarnings("unused")
    public boolean getFromNsAndCompare(org.json.JSONObject fromExcel,
	    String recordType, ArrayList<String> recordsToVerify,
	    ExtentTest logger) throws IOException, ParseException,
	    JSONException, InterruptedException {
	org.json.JSONObject fromNS = null;
	ArrayList<String> head = CommonLibrary.templateHeader(CommonLibrary
		.getHeader());
	HttpLibrary.printCurrentDataValues(fromExcel, logger);
	int arr[] = new int[recordsToVerify.size()];
	for (int i = 0; i < recordsToVerify.size(); i++) {
	    logger.log(Status.PASS, Integer.toString(id));

	    logger.log(Status.PASS, "Getting data from NS");
	    String s = CommonLibrary.remSpecialCharacters(recordsToVerify
		    .get(i).trim());
	    id = Integer.parseInt(s);
	    arr[i] = id;
	}
	fromNS = lib.getFromNs(recordType, arr, logger);
	System.out.println("\ndata from NS\n\n");
	HttpLibrary.printCurrentDataValues(fromNS, logger);

	if (!lib.compareData(fromExcel, fromNS, logger)) {
	    logger.log(Status.FAIL, "Opps Data Mismatch");
	    System.out.println("Data Mismatch");
	    return false;
	}
	logger.log(Status.PASS, "Cheers!! Opertion is successfull");
	return true;

    }

    @Test(priority = 0)
    public void insertOperation() throws Exception {
	System.out.println("******************");
	logger = report.createTest("Insert Operation : " + recordType);
	String fields = templateFields;
	ArrayList<String> values = insertValues;
	loadTemplateAndPerformDataOperation((String) templateName, fields,
		values, CommonLibrary.App.InsertAllRows, logger);
	HttpLibrary.setFieldsFormat(fields);
	org.json.JSONObject fromExcel = lib.getRowsData(true, logger);
	System.out.println("printing values from excel: \n ");
	HttpLibrary.printCurrentDataValues(fromExcel, logger);
	recordsToVerify = new ArrayList<String>();
	@SuppressWarnings("unchecked")
	Iterator<String> keys = fromExcel.keys();
	while (keys.hasNext()) {
	    String key = keys.next();
	    recordsToVerify.add(key);
	}
	System.out.println(recordsToVerify);

	if (recordsToVerify.isEmpty()) {
	    logger.log(Status.FAIL,
		    "unable to get internal id's from Excel sheet");
	    Assert.fail("unable to get internal id's from Excel sheet");
	} else {
	    getFromNsAndCompare(fromExcel, recordType, recordsToVerify, logger);
	}

    }

    // @Test(priority = 1, dependsOnMethods = {"insertOperation"})
    public void updateOperation() throws Exception {
	System.out.println("******************");
	System.out.println("Update operation : " + templateName);
	logger = report.createTest("Update Operation : " + recordType);
	String fields = templateFields;
	String substr = lib.appendIdToUpdateTemplateValues(updateValues,
		getId());
	System.out.println("[" + substr + "]");
	boolean success = false;
	// String fields = temp.getProperty("contactTemplate");
	// loadTemplateAndPerformDataOperation((String) templateName, fields,
	// substr, CommonLibrary.App.InsertAllRows, logger);
	HttpLibrary.setFieldsFormat(fields);
	org.json.JSONObject fromExcel = lib.getRowData(0, logger);
	getFromNsAndCompare(fromExcel, recordType, success, logger);
	System.out.println("******************");
    }

    // @Test(priority = 2, dependsOnMethods = {"insertOperation"})
    public void refreshOperation() throws Exception {
	System.out.println("******************");
	System.out.println(templateName);
	logger = report.createTest("Refresh Opearation : " + recordType);
	String substr = "," + getId() + ",";
	System.out.println("[" + substr + "]");
	boolean success = false;
	String fields = templateFields;
	// loadTemplateAndPerformDataOperation((String) templateName,
	// fields,substr, CommonLibrary.App.RefreshSelectedRows, logger);
	HttpLibrary.setFieldsFormat(fields);

	org.json.JSONObject fromExcel = lib.getRowData(0, logger);
	getFromNsAndCompare(fromExcel, recordType, success, logger);
	System.out.println("******************");
    }

    // @Test(priority = 5, dependsOnMethods = {"insertOperation"})
    public void deleteOperation() throws Exception {
	System.out.println("************");
	Thread.sleep(2000);
	lib.switchIntoSheet();
	logger = report.createTest("delete Opearation : " + recordType);
	int id = getId();
	logger.log(Status.INFO, "Deleting record " + id);
	Keyboard press = ((HasInputDevices) driver).getKeyboard();
	press.pressKey(Keys.chord(Keys.CONTROL, Keys.HOME));
	press.pressKey(Keys.DOWN);
	press.pressKey(Keys.DOWN);
	lib.switchToApp();
	Thread.sleep(1000);
	logger.log(Status.INFO, "Delete button pressed");
	lib.clickOn(CommonLibrary.App.DeleteSelectedRows);
	String notification = lib.getNotification();
	System.out.println(notification);
	logger.log(Status.INFO, notification);
	lib.waitUntilLoadingEnds();
	Thread.sleep(5000);
	StringBuilder rl = HttpLibrary.doGET(recordType, getId());
	System.out.println(rl.toString());
	if (rl.toString().equals("[]")) {
	    logger.log(Status.PASS, "Successfully Deleted record");
	} else {
	    logger.log(Status.FAIL, "Record is not deleted");
	    Assert.fail("Record is not deleted :( ");

	}

    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
	if (result.getStatus() == ITestResult.FAILURE) {
	    logger.log(Status.FAIL, result.getName() + " function is fail");

	    String screenshot_path = CommonLibrary.capture(driver,
		    result.getName());
	    ExtentTest image = logger.addScreenCaptureFromPath(screenshot_path);
	    logger.log(Status.FAIL, "asdf" + image);
	    Files.write(
		    Paths.get(System.getProperty("user.dir")
			    + "\\FailedPageSource\\" + result.getName()
			    + ".txt"), driver.getPageSource().getBytes());

	}

	report.flush();
    }

}
