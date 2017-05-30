package WebDriverTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
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
    // int id;
    private String recordType = "";
    private String templateName = "";
    private String templateFields = "";
    private ArrayList<String> insertValues = new ArrayList<String>();
    private ArrayList<String> updateValues = new ArrayList<String>();
    Map<String, String> cleanRecordsCreated = new HashMap<String, String>();
    ArrayList<String> recordsToVerify = new ArrayList<String>();
    ArrayList<String> recordsToUpdate = new ArrayList<String>();

    public RecordTestingForMultipleRecords(String recordType,
	    String templateName, String templateFields,
	    ArrayList<String> insertValues, ArrayList<String> updateValues) {
	this.recordType = recordType;
	this.templateName = templateName;
	this.templateFields = templateFields;
	this.insertValues = insertValues;
	this.updateValues = updateValues;

    }

    /*
     * public void setId(int id) { this.id = id; }
     */
    /*
     * public int getId() { return id; }
     */
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
	Thread.sleep(3000);
	lib.switchToApp();
	Thread.sleep(3500);
	Files.write(Paths.get("D:/fileName.html"), driver.getPageSource()
		.getBytes());
	try {
	    driver.findElement(By.cssSelector("a[title='Menu']")).click();
	} catch (Exception e) {
	    Thread.sleep(2000);
	    System.out.println("menu issue");
	    driver.findElement(By.cssSelector("a[title='Menu']")).click();
	}
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
	logger.log(Status.INFO, "Inserting Data into Template");
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
	/*
	 * for (int m = 1; m < values.size(); m++) {
	 * press.pressKey(Keys.chord(Keys.SHIFT, Keys.DOWN)); }
	 */
	lib.switchToApp();
	try {
	    WebElement modalWindow = driver.findElement(By
		    .cssSelector("div.modal-content div.alertAction button"));
	    modalWindow.click();
	    System.out.println("Modal window is dislayed");
	} catch (Exception e) {
	    System.out.println("Modal window is not dislayed");
	}

	lib.clickOn(clickOn);
	System.out.println("getting notification");
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
	// HttpLibrary.printCurrentDataValues(fromExcel);
	int arr[] = new int[recordsToVerify.size()];
	for (int i = 0; i < recordsToVerify.size(); i++) {

	    logger.log(Status.INFO, "Getting data from NS");
	    String s = CommonLibrary.remSpecialCharacters(recordsToVerify
		    .get(i).trim());
	    arr[i] = Integer.parseInt(s);
	}
	logger.log(Status.INFO, Arrays.toString(arr));
	fromNS = lib.getFromNs(recordType, arr, logger);
	System.out.println("\ndata from NS\n\n");
	HttpLibrary.printCurrentDataValues(fromNS);
	System.out.println("Error rows" + lib.getErrorRows());

	if (lib.compareData(fromExcel, fromNS, logger)
		&& lib.getErrorRows().isEmpty()) {
	    logger.log(Status.PASS, "Cheers!! Opertion is successfull");
	    return true;
	}
	if (!lib.getErrorRows().isEmpty()) {
	    Assert.fail("few records insertion/updation failed");
	    logger.log(Status.ERROR,
		    "few records got failed\n\n" + lib.getErrorRows());
	}
	logger.log(Status.FAIL, "Opps Data Mismatch");
	System.out.println("Data Mismatch");
	return false;
    }

    @Test(priority = 0)
    public void insertOperation() throws Exception {
	System.out.println("********* insert operation *********");
	logger = report.createTest("Insert Operation : " + recordType);
	String fields = templateFields;
	ArrayList<String> values = insertValues;
	loadTemplateAndPerformDataOperation((String) templateName, fields,
		values, CommonLibrary.App.InsertAllRows, logger);
	HttpLibrary.setFieldsFormat(fields);
	org.json.JSONObject fromExcel = lib.getRowsData(true, logger);
	System.out.println("printing values from excel: \n ");
	HttpLibrary.printCurrentDataValues(fromExcel);
	recordsToVerify = new ArrayList<String>();
	@SuppressWarnings("unchecked")
	Iterator<String> keys = fromExcel.keys();
	while (keys.hasNext()) {
	    String key = keys.next();
	    recordsToVerify.add(key);
	    recordsToUpdate.add(key);
	    cleanRecordsCreated.put(key, recordType);
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

    @Test(priority = 1, dependsOnMethods = { "insertOperation" })
    public void updateOperation() throws Exception {
	System.out.println("********* update operation *********");
	logger = report.createTest("Update Operation : " + recordType);
	String fields = templateFields;
	ArrayList<String> values = updateValues;
	ArrayList<String> appendedValues = new ArrayList<String>();
	System.out.println("Records to update: " + recordsToUpdate);
	// System.out.println("**** \n" + values);
	/*
	 * for (int i = 0; i < recordsToUpdate.size(); i++) {
	 * appendedValues.add( i,
	 * lib.appendIdToUpdateTemplateValues(values.get(i),
	 * Integer.parseInt(recordsToUpdate.get(i))));
	 * System.out.println("Appending ids to update values\n " +
	 * appendedValues.get(i)); }
	 */

	for (int i = 0; i < CommonLibrary.successfullRows.size(); i++) {
	    appendedValues.add(i, lib.appendIdToUpdateTemplateValues(
		    values.get(i),
		    Integer.parseInt(CommonLibrary.successfullRows.get(i))));
	}
	System.out.println("Internal Id added to values : " + appendedValues);

	loadTemplateAndPerformDataOperation((String) templateName, fields,
		appendedValues, CommonLibrary.App.InsertAllRows, logger);
	HttpLibrary.setFieldsFormat(fields);

	CommonLibrary.successfullRows.clear();
	org.json.JSONObject fromExcel = lib.getRowsData(true, logger);
	System.out.println("printing values from excel: \n ");
	HttpLibrary.printCurrentDataValues(fromExcel);
	recordsToVerify = new ArrayList<String>();
	@SuppressWarnings("unchecked")
	Iterator<String> keys = fromExcel.keys();
	while (keys.hasNext()) {
	    String key = keys.next();
	    recordsToVerify.add(key);
	    recordsToUpdate.add(key);
	    cleanRecordsCreated.put(key, recordType);
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

    @Test(priority = 2, dependsOnMethods = { "insertOperation" })
    public void refreshOperation() throws Exception {
	System.out.println("********* Refresh Operation *********");
	System.out.println(templateName);
	logger = report.createTest("Refresh Opearation : " + recordType);
	/*
	 * String substr = "," + getId() + ","; System.out.println("[" + substr
	 * + "]");
	 */
	ArrayList<String> substr = new ArrayList<String>();
	for (int i = 0; i < CommonLibrary.successfullRows.size(); i++) {
	    substr.add("," + CommonLibrary.successfullRows.get(i) + ",");
	}
	// boolean success = false;
	String fields = templateFields;
	loadTemplateAndPerformDataOperation((String) templateName, fields,
		substr, CommonLibrary.App.RefreshAllRows, logger);
	HttpLibrary.setFieldsFormat(fields);

	CommonLibrary.successfullRows.clear();
	org.json.JSONObject fromExcel = lib.getRowsData(true, logger);
	System.out.println("printing values from excel: \n ");
	HttpLibrary.printCurrentDataValues(fromExcel);
	recordsToVerify = new ArrayList<String>();
	@SuppressWarnings("unchecked")
	Iterator<String> keys = fromExcel.keys();
	while (keys.hasNext()) {
	    String key = keys.next();
	    recordsToVerify.add(key);
	    recordsToUpdate.add(key);
	    cleanRecordsCreated.put(key, recordType);
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

    @Test(priority = 5, dependsOnMethods = { "insertOperation" })
    @SuppressWarnings("null")
    public void deleteOperation() throws Exception {
	System.out.println("************");
	Thread.sleep(2000);
	lib.switchIntoSheet();
	logger = report.createTest("delete Opearation : " + recordType);
	Keyboard press = ((HasInputDevices) driver).getKeyboard();
	press.pressKey(Keys.chord(Keys.CONTROL, Keys.HOME));
	press.pressKey(Keys.DOWN);
	press.pressKey(Keys.DOWN);
	lib.switchToApp();
	Thread.sleep(1000);
	logger.log(Status.INFO, "Delete button pressed");
	lib.clickOn(CommonLibrary.App.DeleteAllRows);
	String notification = lib.getNotification();
	System.out.println(notification);
	logger.log(Status.INFO, notification);
	lib.waitUntilLoadingEnds();
	Thread.sleep(2000);
	StringBuilder[] rl = null;
	for (int i = 0; i < CommonLibrary.successfullRows.size(); i++) {

	    System.out.println(Integer.parseInt(CommonLibrary.successfullRows
		    .get(i)));
	    rl[i] = HttpLibrary.doGET(recordType,
		    Integer.parseInt(CommonLibrary.successfullRows.get(i)));
	    System.out.println(rl[i].toString());
	}
	for (int i = 0; i < CommonLibrary.successfullRows.size(); i++) {
	    if (rl[i].toString().equals("[]")) {
		logger.log(Status.PASS, "Successfully Deleted record "
			+ CommonLibrary.successfullRows.get(i));
	    } else {
		logger.log(Status.FAIL, "Failed to delete record "
			+ CommonLibrary.successfullRows.get(i));
		Assert.fail("Record is not deleted :( ");

	    }
	}

    }

    @BeforeMethod
    public void before() {
	CommonLibrary.errorRows.clear();

    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
	if (result.getStatus() == ITestResult.FAILURE) {
	    logger.log(Status.FAIL, result.getName() + " function is fail");
	    for (Entry<String, Integer> entry : CommonLibrary.errorRows
		    .entrySet()) {
		System.out.println(entry.getKey() + "/" + entry.getValue());
		logger.log(Status.ERROR, "Failed row data : " + entry.getKey()
			+ " at row index : " + entry.getValue());
	    }

	    String screenshot_path = CommonLibrary.capture(driver,
		    result.getName());
	    ExtentTest image = logger.addScreenCaptureFromPath(screenshot_path);
	    // logger.log(Status.FAIL, "asdf" + image);
	    Files.write(
		    Paths.get(System.getProperty("user.dir")
			    + "\\FailedPageSource\\" + result.getName()
			    + ".txt"), driver.getPageSource().getBytes());

	}

	report.flush();
    }

    @SuppressWarnings("rawtypes")
    @AfterClass
    public void oneTimeTearDown() throws IOException {
	System.out.println("@AfterClass: class");
	// System.out.println("deleting records " + recordType + " : " +
	// getId());

	for (Map.Entry m : cleanRecordsCreated.entrySet()) {
	    System.out.println(m.getKey() + " " + m.getValue());
	    HttpLibrary.doDelete(m.getValue().toString(),
		    Long.parseLong(m.getKey().toString()));
	}
	CommonLibrary.successfullRows.clear();
	// HttpLibrary.doDelete(recordType, getId());
	/*
	 * StringBuilder rl = HttpLibrary.doGET(recordType, getId());
	 * System.out.println(rl.toString()); if (rl.toString().equals("[]")) {
	 * logger.log(Status.PASS, "Successfully Deleted record"); } else { }
	 */

    }

}
