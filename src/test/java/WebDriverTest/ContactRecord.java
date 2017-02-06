package WebDriverTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

	CommonLibrary clib = new CommonLibrary();
	HttpLibrary http = new HttpLibrary();
	WebDriver driver;
	Properties obj = clib.getObj();
	ExtentTest logger;
	Properties data = clib.getData();

	@Parameters("TestingType")
	@BeforeSuite
	public void InitialSetup() throws Exception
	{
		System.out.println("initial setup");
		clib.beforeTest();
		System.out.println("done before test method");
		CommonLibrary.report = new ExtentReports(System.getProperty("user.dir")
				+ "\\Reports\\TestingType");
		driver = clib.getDriver();
		System.out.println("done");
		clib.officeLogin();
		// liveLogin();
		Thread.sleep(5000);
		clib.switchIntoSheet();
		clib.deleteSheetData();

		// Files.write(Paths.get("e://webapplicationsource.txt"),
		// driver.getPageSource().getBytes());
		clib.waitForOfficeAddin();
		driver.findElement(By.cssSelector("table.moe-infobar-infotable tbody td.moe-infobar-button-cell button"))
				.click();
		System.out.println("Clicked on Start button in App");
		Thread.sleep(3000);
		clib.switchToApp();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.welcomepage p.microsoft-btn a")).click();
		ArrayList<String> allTabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(allTabs.get(3));
		List<WebElement> sign = driver.findElements(By
				.cssSelector("div#main-panel-content table.tile td.tile-name"));
		sign.get(0).click();
		driver.switchTo().window(allTabs.get(2));
		//Files.write(Paths.get("e://webapplicationsource.txt"), driver.getPageSource().getBytes());

	}

	@Test
	public void insertContact() throws InterruptedException, IOException
	{
		logger = CommonLibrary.report.startTest("Opening Workbook");
		// Keyboard press = ((HasInputDevices) driver).getKeyboard();
		clib.switchIntoSheet();
		Thread.sleep(6000);
		clib.switchToApp();
		Thread.sleep(5000);
		clib.loadTemplate("Add Contacts");
		clib.switchIntoSheet();
		Thread.sleep(5000);
		String data = "jackson,james,Parent Company,MR,112 Anonymous : QA : testing test,title,1234567890,(206)888-1212,(206)888-1212,lizard@gmail.com,12:30 PM,FALSE,12345";
		clib.insertDataIntoTemplate(data);
		clib.switchToApp();
		clib.click(CommonLibrary.App.InsertAllRows);
		
		String notification = clib.getNotification();
		System.out.println(notification);

	}

}
