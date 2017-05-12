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

public class RecordTestingForSingleRecords {

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
    private String insertValues = "";
    private String updateValues = "";

    public RecordTestingForSingleRecords(String recordType,
	    String templateName, String templateFields, String insertValues,
	    String updateValues) {
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
	driver.findElement(By.cssSelector("input[placeholder='Username']"))
		.sendKeys(data.getProperty("Emailid"));
	driver.findElement(By.cssSelector("input[placeholder='Password']"))
		.sendKeys(data.getProperty("pwd"));
	driver.findElement(By.linkText("Login")).click();
	Thread.sleep(3000);

	/*
	 * driver.findElement(By.cssSelector("div.welcomepage p.microsoft-btn a")
	 * ) .click(); ArrayList<String> allTabs = new ArrayList<String>(
	 * driver.getWindowHandles()); driver.switchTo().window(allTabs.get(3));
	 * List<WebElement> sign = driver.findElements(By
	 * .cssSelector("div#main-panel-content table.tile td.tile-name"));
	 * sign.get(0).click(); driver.switchTo().window(allTabs.get(2));
	 */
	lib.switchIntoSheet();

    }

    public void loadTemplateAndPerformDataOperation(String Template,
	    String templateFields, String values, CommonLibrary.App clickOn,
	    com.aventstack.extentreports.ExtentTest logger)
	    throws InterruptedException, IOException {
	lib.switchIntoSheet();
	Thread.sleep(2000);
	lib.switchToApp();
	Thread.sleep(700);
	Files.write(Paths.get("D:/fileName.txt"), driver.getPageSource()
		.getBytes());
	Thread.sleep(2000);
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
	Keyboard press = ((HasInputDevices) driver).getKeyboard();
	logger.log(Status.INFO, values);
	press.pressKey(Keys.chord(Keys.CONTROL, Keys.HOME));
	Thread.sleep(200);
	press.pressKey(Keys.LEFT);
	press.pressKey(Keys.DOWN);
	press.pressKey(Keys.DOWN);
	lib.insertDataIntoTemplate(values);
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
	    int id = Integer.parseInt(s);
	    arr[i] = id;
	}
	fromNS = lib.getFromNs(recordType, arr);
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
	String values = insertValues;
	loadTemplateAndPerformDataOperation((String) templateName, fields,
		values, CommonLibrary.App.InsertAllRows, logger);
	HttpLibrary.setFieldsFormat(fields);
	org.json.JSONObject fromExcel = lib.getRowsData(false, logger);
	HttpLibrary.printCurrentDataValues(fromExcel, logger);
	System.out.println("printing values from excel: \n ");
	HttpLibrary.printCurrentDataValues(fromExcel, logger);
	ArrayList<String> recordsToVerify = new ArrayList<String>();
	@SuppressWarnings("unchecked")
	Iterator<String> keys = fromExcel.keys();
	while (keys.hasNext()) {
	    String key = keys.next();
	    // System.out.println(key);
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

    @Test(priority = 1, dependsOnMethods = { "insertOperation" })
    public void updateOperation() throws Exception {
	System.out.println("******************");
	System.out.println("Update operation : " + templateName);
	logger = report.createTest("Update Operation : " + recordType);
	String fields = templateFields;
	String substr = lib.appendIdToUpdateTemplateValues(updateValues,
		getId());
	System.out.println("[" + substr + "]");
	// String fields = temp.getProperty("contactTemplate");
	loadTemplateAndPerformDataOperation((String) templateName, fields,
		substr, CommonLibrary.App.InsertAllRows, logger);
	HttpLibrary.setFieldsFormat(fields);
	org.json.JSONObject fromExcel = lib.getRowsData(false, logger);
	ArrayList<String> recordsToVerify = new ArrayList<String>();
	@SuppressWarnings("unchecked")
	Iterator<String> keys = fromExcel.keys();
	while (keys.hasNext()) {
	    String key = keys.next();
	    // System.out.println(key);
	    recordsToVerify.add(key);
	}
	System.out.println(recordsToVerify);

	if (recordsToVerify.isEmpty()) {
	    Assert.fail("unable to get internal id's from Excel sheet");
	} else {
	    getFromNsAndCompare(fromExcel, recordType, recordsToVerify, logger);
	}
	System.out.println("******************");
    }

    @Test(priority = 2, dependsOnMethods = { "insertOperation" })
    public void refreshOperation() throws Exception {
	System.out.println("******************");
	System.out.println(templateName);
	logger = report.createTest("Refresh Opearation : " + recordType);
	String substr = "," + getId() + ",";
	System.out.println("[" + substr + "]");
	String fields = templateFields;
	loadTemplateAndPerformDataOperation((String) templateName, fields,
		substr, CommonLibrary.App.RefreshSelectedRows, logger);
	HttpLibrary.setFieldsFormat(fields);

	org.json.JSONObject fromExcel = lib.getRowsData(false, logger);
	ArrayList<String> recordsToVerify = new ArrayList<String>();
	@SuppressWarnings("unchecked")
	Iterator<String> keys = fromExcel.keys();
	while (keys.hasNext()) {
	    String key = keys.next();
	    // System.out.println(key);
	    recordsToVerify.add(key);
	}
	System.out.println(recordsToVerify);

	if (recordsToVerify.isEmpty()) {
	    Assert.fail("unable to get internal id's from Excel sheet");
	} else {
	    getFromNsAndCompare(fromExcel, recordType, recordsToVerify, logger);
	}
	System.out.println("******************");
    }

    @Test(priority = 5, dependsOnMethods = { "insertOperation" })
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
	    /*
	     * org.json.JSONObject fromExcel = lib.rowData(0, logger);
	     * System.out.println(fromExcel.get(0));
	     */
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
