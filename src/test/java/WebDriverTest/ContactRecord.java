package WebDriverTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import util.CommonLibrary;
import util.HttpLibrary;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class ContactRecord {

	CommonLibrary lib = new CommonLibrary();
	HttpLibrary http = new HttpLibrary();
	WebDriver driver;
	Properties obj = lib.getObj();
	ExtentTest logger;
	Properties data = lib.getData();

	@Parameters("TestingType")
	@BeforeSuite
	public void InitialSetup() throws Exception
	{
		System.out.println("initial setup");
		lib.beforeTest();
		CommonLibrary.setAccessToken(HttpLibrary.getAccessTokenRestApi());
		System.out.println("done before test method");
		CommonLibrary.report = new ExtentReports(System.getProperty("user.dir")
				+ "\\Reports\\TestingType");
		driver = lib.getDriver();
		System.out.println("done");
		lib.officeLogin();
		// liveLogin();
		Thread.sleep(4000);
		try
		{
			driver.findElement(By.id("errorewaDialogInner"));
			driver.navigate().refresh();
		}catch(Exception e){
		}
		lib.switchIntoSheet();
		lib.deleteSheetData();

		// Files.write(Paths.get("e://webapplicationsource.txt"),
		// driver.getPageSource().getBytes());
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
		// Files.write(Paths.get("e://webapplicationsource.txt"),
		// driver.getPageSource().getBytes());
		lib.switchIntoSheet();
	}

	@Test
	public void insertContact() throws Exception
	{

		logger = CommonLibrary.report.startTest("Opening Workbook");
		
		boolean success = false;
		Thread.sleep(2000);
		lib.switchToApp();
		Thread.sleep(700);
		lib.loadTemplate("Add Contacts");
		lib.switchIntoSheet();
		Thread.sleep(1500);
		String data = "jackson,james,Parent Company,MR,112 Anonymous : QA : testing test,title,1234567890,(206)888-1212,(206)888-1212,lizard@gmail.com,,10711,12345,TRUE,9876543,,,jackson james,,FALSE,CA,TRUE,12345,United States,hyd";
		lib.insertDataIntoTemplate(data);
		lib.switchToApp();
		lib.clickOn(CommonLibrary.App.InsertAllRows);
		WebElement loading = driver.findElement(By.cssSelector("div#loadingDiv"));
		System.out.println(loading.getAttribute("aria-hidden"));
		while (loading.getAttribute("aria-hidden").equals("false"))
		{
			loading = driver.findElement(By.cssSelector("div#loadingDiv"));
		}
		String notification = lib.getNotification();
		System.out.println(notification);
		
		
		lib.setHeaderData();
		HashMap<String, String> fromExcel = (HashMap<String, String>) lib.rowData(0);
		//fromExcel = handleMissingValues(fromExcel);
		HttpLibrary.printCurrentDataValues(fromExcel);
		ArrayList<String> head = lib.templateHeader(CommonLibrary.getHeader());
		
		success = fromExcel.get(head.get(0)).equals("");
		//needed as some fields will be populated when user perform refresh(Address/created date...etc)
		if(success){
		lib.clickOn(CommonLibrary.App.RefreshAllRows);
		System.out.println("Refresh table data.... \n getting values from sheet");
		fromExcel = (HashMap<String, String>) lib.rowData(0);
		}
		
		int id = lib.getRecordId(fromExcel);
		System.out.println("id: "+id);
		if(id!=0){
		Map<String, String> fromNS = lib.getFromNs(head, "contact", id);
		System.out.println("\ndata from NS\n\n");
		HttpLibrary.printCurrentDataValues(fromNS);
		lib.compareData(fromExcel, fromNS, head.get(0));
		}else{
			System.out.println("insertion failed \n Error message :"+fromExcel.get(head.get(0)));
		}
		
	}

	
}
