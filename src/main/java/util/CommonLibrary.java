package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.common.base.Function;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class CommonLibrary {

    public Properties obj;
    public Properties data;
    // public Properties template;
    public static WebDriver driver;
    // public static ExtentReports report;
    public static AccessToken accessToken;
    public static String workbookId = "01JNEAOJ6ZBLZDDYTT2FBZN66OLHFVYJNU";
    public static String sheet;

    public static String getSheet() {
	return sheet;
    }

    public static void setSheet(String sheet) {
	CommonLibrary.sheet = sheet;
    }

    public static HashMap<String, String> header;

    public static AccessToken getAccessToken() {
	return accessToken;
    }

    public static void setAccessToken(AccessToken accessToken) {
	CommonLibrary.accessToken = accessToken;
    }

    public static HashMap<String, String> getHeader() {
	return header;
    }

    public static void setHeader(HashMap<String, String> header) {
	CommonLibrary.header = header;
    }

    public CommonLibrary() {
	try {
	    LoadPropertyFiles();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void beforeTest() {

	System.setProperty(
		"webdriver.chrome.driver",
		System.getProperty("user.dir")
			+ "//src//main//resources//webdrivers//chromedriver.exe");
	driver = new ChromeDriver();

	/*
	 * System.setProperty("webdriver.gecko.driver",
	 * System.getProperty("user.dir") +
	 * "//src//main//resources//webdrivers//geckodriver.exe"); driver = new
	 * FirefoxDriver();
	 */
	setDriver(driver);
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    public void LoadPropertyFiles() throws IOException {
	obj = new Properties();
	FileInputStream objfile = new FileInputStream(
		System.getProperty("user.dir")
			+ "//src//main//resources//properties//objects.properties");
	obj.load(objfile);

	data = new Properties();
	FileInputStream testdatafile = new FileInputStream(
		System.getProperty("user.dir")
			+ "//src//main//resources//properties//testdata.properties");
	data.load(testdatafile);

	/*
	 * template = new Properties(); FileInputStream templatefile = new
	 * FileInputStream(System.getProperty("user.dir") +
	 * "//src//main//resources//properties//Template.properties");
	 * template.load(templatefile);
	 */

    }

    /**/
    public Properties getObj() {
	return obj;
    }

    public Properties getData() {
	return data;
    }

    public WebDriver getDriver() {
	return driver;
    }

    public void setDriver(WebDriver driver) {
	CommonLibrary.driver = driver;
    }

    public WebElement Locator(final WebElement selector) {
	Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
		.withTimeout(150, TimeUnit.SECONDS)
		.pollingEvery(20, TimeUnit.MILLISECONDS)
		.ignoring(NoSuchElementException.class);

	WebElement selectorObj = wait
		.until(new Function<WebDriver, WebElement>() {
		    public WebElement apply(WebDriver driver) {
			return selector;
		    }
		});
	return selectorObj;
    }

    public void officeLogin(WebDriver driver) throws InterruptedException {
	driver.get("https://portal.office.com");

	driver.findElement(By.id("cred_userid_inputtext")).sendKeys(
		"mazhar@celigo2.onmicrosoft.com");
	driver.findElement(By.cssSelector("input#cred_password_inputtext"))
		.sendKeys("Celigo!@#$%6");
	Thread.sleep(2000);
	driver.findElement(By.cssSelector("button[id='cred_sign_in_button']"))
		.click();

	WebElement oneDrive = driver.findElement(By
		.id("ShellDocuments_link_text"));
	oneDrive.click();
	// driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL
	// +"\t");
	Thread.sleep(3000);
	for (String s : driver.getWindowHandles()) {
	    driver.switchTo().window(s);
	    System.out.println(driver.getTitle());
	    if (driver.getTitle().contains("OneDrive")) {
		// System.out.println(driver.getTitle());
	    } else
		driver.switchTo().defaultContent();
	}
	Thread.sleep(5000);
	WebElement folder = driver.findElement(By.cssSelector("a[title=Test]"));
	folder.click();
	Thread.sleep(4000);
	WebElement book = driver.findElement(By.linkText("Book.xlsx"));
	Actions act = new Actions(driver);
	act.contextClick(book).build().perform();
	WebElement we = driver.findElement(By
		.cssSelector("div[aria-label='Open']"));
	we.click();
	Thread.sleep(1000);
	driver.findElement(By.linkText("Open in Excel Online")).click();
	Thread.sleep(1000);
	ArrayList<String> allTabs = new ArrayList<String>(
		driver.getWindowHandles());
	driver.switchTo().window(allTabs.get(2));

    }

    public void liveLogin() throws InterruptedException {
	driver.get("https://login.live.com");
	driver.findElement(By.cssSelector("input[type='email']")).sendKeys(
		"mazharuddin8596@outlook.com");
	driver.findElement(By.cssSelector("input[type='submit']")).click();
	Thread.sleep(2000);
	driver.findElement(By.cssSelector("input[type='password']")).sendKeys(
		"Celigo!@#4");
	driver.findElement(By.cssSelector("input[type='submit']")).click();
	Thread.sleep(2000);
    }

    public void switchToApp() throws InterruptedException, IOException {

	// System.out.println("switching to App " + driver);
	WebElement appFrame = driver.findElement(By
		.cssSelector("iframe[title='SmartClient Staging App']"));
	driver.switchTo().frame(appFrame);
	try {
	    driver.findElement(By.cssSelector("body[ng-app='SmartClient']"));
	} catch (Exception e) {
	    driver.navigate().refresh();
	    switchIntoSheet();
	    appFrame = driver.findElement(By
		    .cssSelector("iframe[title='SmartClient Staging App']"));
	    driver.switchTo().frame(appFrame);
	}
	System.out.println("Switched to app iframe");
    }

    public void waitForOfficeAddin() throws InterruptedException {
	for (int i = 0; i < 10; i++) {
	    try {
		driver.navigate().refresh();
		WebElement f = driver.findElement(By
			.cssSelector("iframe[name='WebApplicationFrame']"));
		driver.switchTo().frame(f);

		Thread.sleep(2500);
		// System.out.println("Finding Task pane " + i);
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		WebElement pane = (WebElement) jse
			.executeScript("return document.getElementById('m_excelWebRenderer_ewaCtl_novTaskPaneToolbarContainer')");
		try {
		    // System.out.println("pane visible? " +
		    // pane.isDisplayed());
		    if (pane.isDisplayed()) {
			System.out.println("able to detect taskpane");
			break;
		    }
		} catch (NullPointerException n) {
		    System.out.println("retrying");
		}
	    } catch (NoSuchElementException e) {
		System.out.println("Task pane fails in catch: " + i);
		Thread.sleep(1000);
	    }
	    if (i == 7) {
		driver.navigate().refresh();
		Thread.sleep(4000);
	    }
	}
    }

    public void handleMSDialogBox() {
	for (int i = 0; i < 3; i++) {
	    try {
		WebElement mainsheet;
		driver.switchTo().defaultContent();
		mainsheet = driver.findElement(By
			.cssSelector("iframe[name='WebApplicationFrame']"));
		driver.switchTo().frame(mainsheet);
		System.out.println("checking error dialog box");
		driver.findElement(By.id("errorewaDialogInner"));
		System.out.println("Found... refreshing page");
		driver.navigate().refresh();
	    } catch (Exception e) {
		System.out.println("No dialog box found");
		break;
	    }
	}
    }

    public void switchIntoSheet() throws InterruptedException {
	WebElement mainsheet;
	Keyboard press = ((HasInputDevices) driver).getKeyboard();
	driver.switchTo().defaultContent();
	mainsheet = driver.findElement(By
		.cssSelector("iframe[name='WebApplicationFrame']"));
	driver.switchTo().frame(mainsheet);
	Thread.sleep(1000);
	// *[@id="m_excelWebRenderer_ewaCtl_msospAFrameContainer"]/div[4]
	checkSheetReady();
	for (int l = 0; l < 4; l++) {
	    if (!driver.findElement(By.cssSelector("div.ewa-background-ready"))
		    .isDisplayed()) {
		Thread.sleep(2000);
	    }
	}
	try {
	    List<WebElement> sheetList = driver.findElements(By
		    .cssSelector("table.ewr-grdcontarea-ltr  tbody tr td"));
	    sheetList.get(2).click();
	} catch (org.openqa.selenium.WebDriverException e) {
	    Thread.sleep(2000);
	    List<WebElement> sheetList = driver.findElements(By
		    .cssSelector("table.ewr-grdcontarea-ltr  tbody tr td"));
	    sheetList.get(2).click();
	}
	Thread.sleep(300);
	press.pressKey(Keys.DOWN);
	Thread.sleep(800);
	press.pressKey(Keys.chord(Keys.CONTROL, Keys.HOME));
    }

    public void checkSheetReady() {
	for (int i = 0; i < 3; i++) {
	    try {
		driver.findElement(By.className("ewa-background-ready"));
	    } catch (Exception e) {

	    }
	}
    }

    public void deleteSheetData() throws InterruptedException {
	System.out.println("clearing screen Data..");
	Keyboard press = ((HasInputDevices) driver).getKeyboard();
	Thread.sleep(3000);
	press.pressKey(Keys.chord(Keys.CONTROL, Keys.END));
	Thread.sleep(2000);
	press.pressKey(Keys.ENTER);
	press.pressKey(Keys.chord(Keys.CONTROL, Keys.DOWN));
	Thread.sleep(2000);
	press.pressKey(Keys.chord(Keys.CONTROL, Keys.SPACE));
	Thread.sleep(800);
	press.pressKey(Keys.chord(Keys.SHIFT, Keys.SPACE));
	Thread.sleep(800);
	press.pressKey(Keys.DELETE);
	Thread.sleep(3000);
	press.pressKey(Keys.chord(Keys.CONTROL, Keys.HOME));
	Thread.sleep(1000);
	System.out.println("cleared screen Data..");
    }

    public void deleteSheet() throws Exception {
	String oldsheet = HttpLibrary.sheetName();
	System.out.println("Deleting sheet named : " + oldsheet);
	/*
	 * if(oldsheet.split("\\,").length > 1){ String [] s =
	 * oldsheet.split("\\,"); for(int j=0;j<s.length-1;j++){
	 * HttpLibrary.restDelete(getAccessToken(), s[j]);}
	 * 
	 * 
	 * }
	 */
	sheet = HttpLibrary.addSheet();
	Thread.sleep(2000);
	System.out.println("added new sheet named : " + sheet);
	HttpLibrary.restDelete(getAccessToken(), oldsheet);
	HttpLibrary.sheetName();
	setSheet(sheet);
    }

    public void loadTemplate(String name) throws InterruptedException {
	Thread.sleep(1000);
	driver.findElement(By.cssSelector("input[ng-model='searchString']"))
		.sendKeys(name);
	Thread.sleep(1000);
	List<WebElement> Template_List = driver.findElements(By
		.cssSelector("div.section ul li"));

	for (WebElement we : Template_List) {
	    // System.out.println(we.getText());
	    if (we.getText().contains(name)) {
		System.out.println("Clicking on " + we.getText());
		Thread.sleep(1500);
		we.findElement(By.cssSelector("a[title='Load template']"))
			.click();
		break;
	    }
	}
	Thread.sleep(2000);
	driver.findElement(
		By.cssSelector("div#confirmationPopup button#accept")).click();
	Thread.sleep(1000);
    }

    public static String remSpecialCharacters(String temp) {
	try {
	    temp = temp.replace("[", "");
	    temp = temp.replace("]", "");
	    temp = temp.replace("\"", "");
	} catch (java.lang.NullPointerException e) {

	}
	return temp;
    }

    public String appendIdToUpdateTemplateValues(String idata, int id) {
	int dot1 = idata.indexOf(",");
	int dot2 = idata.indexOf(",", dot1 + 1);
	System.out.println(dot1 + " : " + dot2);
	String substr = "," + id + ","
		+ idata.substring(dot2 + 1, idata.length());
	return substr;
    }

    public void insertDataIntoTemplate(String data) throws InterruptedException {
	Keyboard press = ((HasInputDevices) driver).getKeyboard();

	Thread.sleep(200);
	System.out.println("Inserting data into Template");

	String[] col_data = data.split("\\,");
	// inserting data in contact template
	// press.pressKey(Keys.TAB);
	for (int i = 0; i < col_data.length; i++) {
	    if (i != 0) {
		press.pressKey(Keys.TAB);
	    }
	    press.pressKey(col_data[i]);
	    // System.out.println(col_data[i]);
	    Thread.sleep(1300);
	}
    }

    public static String generateJaywayQueryString(int i, String[] data,
	    String type) {
	String query = "";
	// System.out.println("type: " + type);
	for (int k = 2; k < data.length; k++) {
	    if (k == data.length - 1) {
		if (type.equals("enum"))
		    query += ".." + data[k];
		else
		    query += "." + data[k];
	    } else {

		if (k == 2) {
		    query += ".." + data[k] + "[" + i + "]";
		} else {
		    query += ".." + data[k];
		}
	    }
	}
	// System.out.println(query);
	return query;
    }

    public void clickOn(App on) throws InterruptedException, IOException {
	try {
	    Thread.sleep(1500);
	    List<WebElement> appMenu = driver.findElements(By
		    .cssSelector("div.menuContent ul li"));
	    List<WebElement> middleButtons = driver.findElements(By
		    .cssSelector("div.msgBox"));
	    List<WebElement> checkboxes = driver.findElements(By
		    .cssSelector("div.checkbox"));

	    switch (on) {
	    case InsertAllRows:
		appMenu.get(1).click();
		Thread.sleep(500);
		System.out.println("insert checkbox selected ? "
			+ checkboxes.get(2).isSelected());
		if (!checkboxes.get(2).isSelected()) {
		    checkboxes.get(2).click();
		} else {
		    System.out.println("already checkbox is checked");
		}
		middleButtons.get(1)
			.findElement(By.cssSelector("div.button-box a"))
			.click();
		break;
	    case DeleteAllRows:
		appMenu.get(2).click();
		Thread.sleep(500);
		if (!checkboxes.get(3).isSelected()) {
		    checkboxes.get(3).click();
		} else {
		    System.out.println("already checkbox is checked");
		}
		middleButtons.get(2)
			.findElement(By.cssSelector("div.button-box a"))
			.click();
		break;
	    case RefreshSelectedRows:
		appMenu.get(3).click();
		Thread.sleep(1000);
		if (checkboxes.get(4).isSelected()) {
		    checkboxes.get(4).click();
		} else {
		    System.out.println("already checkbox is unchecked");
		}
		middleButtons.get(3)
			.findElement(By.cssSelector("div.button-box a"))
			.click();
		Thread.sleep(1500);
		break;
	    case DownloadWithFilter:
		appMenu.get(0).click();
		Thread.sleep(500);
		if (!checkboxes.get(1).isSelected()) {
		    checkboxes.get(1).click();
		} else {
		    System.out.println("already checkbox is checked");
		}
		middleButtons.get(0)
			.findElement(By.cssSelector("div.button-box a"))
			.click();
		break;
	    case InsertSelectedRows:
		appMenu.get(1).click();
		Thread.sleep(1000);
		if (checkboxes.get(2).isSelected()) {
		    checkboxes.get(2).click();
		} else {
		    System.out.println("already checkbox is unchecked");
		}
		middleButtons.get(1)
			.findElement(By.cssSelector("div.button-box a"))
			.click();
		break;
	    case DeleteSelectedRows:
		appMenu.get(2).click();
		Thread.sleep(1000);
		if (checkboxes.get(3).isSelected()) {
		    checkboxes.get(3).click();
		} else {
		    System.out.println("already checkbox is unchecked");
		}
		middleButtons.get(2)
			.findElement(By.cssSelector("div.button-box a"))
			.click();
		break;
	    case RefreshAllRows:
		appMenu.get(3).click();
		Thread.sleep(1000);
		if (!checkboxes.get(4).isSelected()) {
		    checkboxes.get(4).click();
		} else {
		    System.out.println("already checkbox is checked");
		}
		middleButtons.get(3)
			.findElement(By.cssSelector("div.button-box a"))
			.click();
		break;
	    case DownloadWithoutFilter:
		appMenu.get(0).click();
		Thread.sleep(1000);
		if (checkboxes.get(1).isSelected()) {
		    checkboxes.get(1).click();
		} else {
		    System.out.println("already checkbox is unchecked");
		}
		middleButtons.get(0)
			.findElement(By.cssSelector("div.button-box a"))
			.click();
		break;
	    default:
		System.out.println("Invalid input");
	    }
	} catch (org.openqa.selenium.WebDriverException e) {
	    Files.write(Paths.get("D://error.txt"), driver.getPageSource()
		    .getBytes());
	    clickOn(on);
	}

    }

    public String getNotification() throws InterruptedException {
	int Time = 0;
	String notification = "";
	do {
	    Time += 1;
	    try {
		WebElement notify = driver.findElement(By
			.cssSelector("div.notifyjs-container span"));
		if (notify.isDisplayed()) {
		    notification = notify.getText();
		    if (notification.equals("")) {
			notify = driver.findElement(By
				.cssSelector("div.notifyjs-container span"));
			System.out.println("trying to get notification again");
		    }
		    Thread.sleep(2000);
		    break;
		}
	    } catch (Exception e) {
		System.out.println("not visible");
		Thread.sleep(20);
	    }

	} while (Time < 50);

	return notification;
    }

    public String tableId() throws Exception {

	setAccessToken(HttpLibrary.getAccessTokenRestApi());

	String tableId = HttpLibrary.getTableId(workbookId, getAccessToken());
	System.out.println(tableId);
	return tableId;
    }

    @SuppressWarnings("rawtypes")
    public static ArrayList<String> templateHeader(
	    HashMap<String, String> header) {
	// Getting header row text
	ArrayList<String> head = new ArrayList<String>();
	Set set = header.entrySet();
	Iterator i = set.iterator();
	while (i.hasNext()) {
	    Map.Entry me = (Map.Entry) i.next();
	    head.add((String) me.getKey());

	}
	return head;
    }

    public void waitUntilLoadingEnds() throws InterruptedException {
	try {
	    WebElement loading = driver.findElement(By
		    .cssSelector("div#loadingDiv"));
	    System.out.println(loading.getAttribute("aria-hidden"));
	    while (loading.getAttribute("aria-hidden").equals("false")) {
		loading = driver.findElement(By.cssSelector("div#loadingDiv"));
	    }
	} catch (NoSuchElementException e) {
	    System.out.println("no loading");
	}
    }

    public static String capture(WebDriver driver, String screenShotName)
	    throws IOException {
	TakesScreenshot ts = (TakesScreenshot) driver;
	File source = ts.getScreenshotAs(OutputType.FILE);
	String dest = System.getProperty("user.dir") + "\\ErrorScreenshots\\"
		+ screenShotName + ".png";
	File destination = new File(dest);
	FileUtils.copyFile(source, destination);

	return dest;
    }

    public org.json.JSONObject getRowsData(boolean allRows, ExtentTest logger)
	    throws Exception {
	System.out.println("Waiting for Excel sheet to get latest data");
	// keep this when updating something from SC App
	Thread.sleep(22000);
	HashMap<String, String> header = getHeader();
	ArrayList<String> head = templateHeader(getHeader());
	List<String> rows = new ArrayList<String>();
	String rowData = null;
	if (allRows) {
	    for (int i = 0;; i++) {
		System.out.println("*************");
		rowData = getRows(header, head, i);
		if (rowData.equals("")) {
		    System.out.println("found only this rows breaking loop"
			    + rowData);
		    break;
		}
		System.out
			.println("Printing row before adding to list at index ["
				+ i + "]\n" + rowData);
		String[] values = rowData.split("\\,");
		if (!values[1].equals("")) {
		    rows.add(rowData);
		} else {
		    break;
		}
	    }
	} else {
	    String id = "";
	    for (int i = 0;; i++) {
		rowData = getRows(header, head, i);

		String[] values = rowData.split("\\,");
		if (!values[1].equals("")) {
		    if (i == 0) {
			System.out
				.println("setting id value to check multiple line if present");
			id = values[1];
			System.out.println(id);
			// rows.add(rowData);

		    }
		    if (values[1].equals(id)) {
			rows.add(rowData);
		    } else {
			System.out
				.println("breaking for loop as no more ids matched with same id");
			break;
		    }
		}
	    }
	}
	System.out.println("List :\n" + rows);
	org.json.JSONObject fromExcel = null;
	// if (head.size() == rowData.length()) {
	// need to send multiple rows (record with multiple sublist)
	// rows.add(rowData);
	fromExcel = HttpLibrary.mapHeaderWithExcelRowData(head, rows);
	// } else {
	// Assert.fail("Header and rowdata length mismatch");
	// }

	HttpLibrary.printCurrentDataValues(fromExcel, logger);

	return fromExcel;
    }

    private String getRows(HashMap<String, String> header,
	    ArrayList<String> head, int row) throws Exception,
	    InterruptedException {
	String rowData = HttpLibrary.getRowAtIndex(row + 2);
	for (int k = 0; k < 6; k++) {
	    // Thread.sleep(3000);
	    System.out.println("for loop k value " + k);
	    if (!rowData.equals("")) {
		String[] rowValues = rowData.split("\\,");
		try {
		    System.out.println(" Length " + head.size() + " : "
			    + rowValues.length);
		    if (rowValues[0].equals("") && rowValues[1].equals("")) {
			Thread.sleep(5000);
			rowData = HttpLibrary.getRowAtIndex(row + 2);
			System.out.println("Row" + " : "
				+ Arrays.toString(rowValues));
		    } else {
			System.out.println(Arrays.toString(rowValues));
			System.out.println("returning values");
			rowData = formatData(header, head, rowValues);
			break;
		    }
		} catch (ArrayIndexOutOfBoundsException e) {
		    rowData = HttpLibrary.getRowAtIndex(row + 2);
		}
	    } else {
		System.out.println("else part");
		rowData = HttpLibrary.getRowAtIndex(row + 2);
	    }
	}
	return rowData;

	// check return value + check whether multiple lines are handled or not

	// return rowData = formatData(header, head, rowValues);
    }

    // changing internal id value to labels for enum column and convert boolean
    // to lower case
    private String formatData(HashMap<String, String> header,
	    ArrayList<String> head, String[] rowData) throws IOException,
	    ParseException {
	// handle enum data if any
	String str = new String(
		Files.readAllBytes(Paths.get(System.getProperty("user.dir")
			+ "//src//test//resources//enums.json")));
	org.json.simple.JSONObject enumsJson = (org.json.simple.JSONObject) new JSONParser()
		.parse(str);
	// Country..[?(@.internalId == "_angola")].name
	Object enums = Configuration.defaultConfiguration().jsonProvider()
		.parse(enumsJson.toString());
	for (int j = 0; j < header.size(); j++) {
	    if (header.get(head.get(j)).equals("enum")) {
		System.out.println("enum feild found in header");
		System.out.println(head.get(j));
		if (rowData[j].startsWith("_")) {
		    System.out.println("enum field: " + head.get(j));
		    String[] data = head.get(j).toString().split("\\.");
		    String enumField = data[data.length - 1];
		    // Country..[?(@.internalId == "_angola")].name
		    String q = enumField + "..[?(@.internalId == \""
			    + rowData[j] + "\")].name";
		    q = q.substring(0, 1).toUpperCase() + q.substring(1);
		    System.out.println("query : " + q);
		    String temp = remSpecialCharacters(JsonPath.read(enums, q)
			    .toString());
		    System.out.println("updated enum value from " + rowData[j]
			    + " to " + temp);
		    rowData[j] = temp;
		}
	    }
	    if (header.get(head.get(j)).equals("boolean")) {
		rowData[j] = rowData[j].toLowerCase();
	    }
	}
	return Arrays.stream(rowData).collect(Collectors.joining(","));
    }

    public int getRecordId(org.json.JSONObject fromExcel) throws JSONException {
	try {
	    String s = ((String) fromExcel.get(".internalId")).trim();
	    if (s.equals("")) {
		Assert.fail("No data found in internal id column");
	    }
	    s = remSpecialCharacters(s);
	    int i = Integer.parseInt(s);
	    return i;
	} catch (NullPointerException e) {
	    return 0;
	}
    }

    public org.json.JSONObject getFromNs(String recType, int[] arr)
	    throws IOException, ParseException, JSONException,
	    InterruptedException {
	org.json.JSONObject fromNS = new org.json.JSONObject();
	ArrayList<String> actualData = new ArrayList<String>();
	ArrayList<String> head = templateHeader(CommonLibrary.getHeader());
	for (int m = 0; m < arr.length; m++) {
	    StringBuilder rl = HttpLibrary.doGET(recType, arr[m]);

	    if (!rl.toString().equals("[]")) {
		int maxArray = 0;
		JSONArray nsData = new JSONArray(rl.toString());
		org.json.JSONObject json = nsData.getJSONObject(0);
		// System.out.println(json.toString());

		// parsing JSON Response
		Configuration conf = Configuration.defaultConfiguration();

		Object document = conf.jsonProvider().parse(json.toString());
		// !! important System.out.println(JsonPath.read(document,
		// "$..addressbook[0].addressbookaddress.addressee").toString());

		ArrayList<String> res = new ArrayList<String>();

		res.add(0, "");
		// getting max length of sublist
		for (int j = 1; j < head.size(); j++) {
		    String[] data = head.get(j).toString().split("\\.");

		    if (data.length > 2) {
			// $..addressbook.length()
			// System.out.println("$.." + data[2] + ".length()");
			String s = CommonLibrary.remSpecialCharacters(JsonPath
				.read(document, "$.." + data[2] + ".length()")
				.toString());
			if (maxArray < Integer.parseInt(s)) {
			    maxArray = Integer.parseInt(s);
			    System.out.println(maxArray);
			}
		    }
		}
		// Print values from Ns JSON response
		for (int i = 0; i < maxArray; i++) {
		    res = new ArrayList<String>();
		    res.add(0, "");
		    for (int j = 1; j < head.size(); j++) {
			String[] data = head.get(j).toString().split("\\.");
			String value = "";
			String Query = "";
			if (data.length > 2) {

			    Query = generateJaywayQueryString(i, data,
				    header.get(head.get(j))).toLowerCase();
			} else {
			    Query = head.get(j).toLowerCase();
			}
			try {
			    if (Query.contains("internalid")) {
				Query = Query.replaceAll("internalid", "id");
				if (Query.contains("addressbookaddress")) {
				    res.add(j, "");
				} else {
				    value = remSpecialCharacters(JsonPath.read(
					    document, "$" + Query).toString());
				    res.add(j, value);
				}
			    } else {
				if (header.get(head.get(j)).equals("select")) {
				    value = remSpecialCharacters(JsonPath.read(
					    document, "$" + Query + ".name")
					    .toString());
				    res.add(j, value);
				} else if (header.get(head.get(j)).equals(
					"enum")) {
				    value = remSpecialCharacters(JsonPath.read(
					    document, "$" + Query + ".name")
					    .toString());
				    res.add(j, value);
				} else if (header.get(head.get(j)).equals(
					"boolean")) {
				    value = remSpecialCharacters(JsonPath.read(
					    document, "$" + Query).toString());
				    value = value.toLowerCase();
				    res.add(j, value);
				} else {
				    value = remSpecialCharacters(JsonPath.read(
					    document, "$" + Query).toString());
				    res.add(j, value);
				}

			    }
			} catch (PathNotFoundException e) {

			    res.add(j, "");
			}
		    }
		    System.out.println("From NS: " + res);
		    String[] values = new String[res.size()];

		    System.out.println(res.size());
		    for (int j = 0; j < res.size(); j++) {
			System.out.println(res.get(j));
			if (res.get(j).equals("") || res.get(j) == null) {
			    values[j] = "";
			} else {
			    values[j] = remSpecialCharacters(res.get(j));
			}

		    }

		    // Mapping header and row values as <Key,Value>
		    // fromNS = HttpLibrary.mapHeaderWithExcelRowData(head,
		    // values);
		    actualData.add(Arrays.stream(values).collect(
			    Collectors.joining(",")));

		    System.out.println(Arrays.toString(values));
		}
	    }
	}
	fromNS = HttpLibrary.mapHeaderWithExcelRowData(head, actualData);
	HttpLibrary.printCurrentDataValues(fromNS, null);

	return fromNS;

    }

    public boolean compareData(org.json.JSONObject leftMap,
	    org.json.JSONObject rightMap,
	    com.aventstack.extentreports.ExtentTest logger)
	    throws JSONException {
	logger.log(Status.INFO, leftMap + " with " + rightMap);
	if (leftMap == rightMap)
	    return true;
	if (leftMap == null || rightMap == null
		|| leftMap.length() != rightMap.length())
	    return false;
	System.out.println("Comparing sheet data with NS data");

	JSONAssert.assertEquals(leftMap, rightMap, false);

	return true;
    }

    public enum App {
	InsertSelectedRows, InsertAllRows, DeleteSelectedRows, DeleteAllRows, RefreshSelectedRows, RefreshAllRows, DownloadWithFilter, DownloadWithoutFilter
    }
}