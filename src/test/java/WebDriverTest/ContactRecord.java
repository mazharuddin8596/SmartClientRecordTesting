package WebDriverTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
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
	Properties template = lib.getTemplate();
	ExtentTest logger;
	Properties data = lib.getData();

	@Parameters("TestingType")
	@BeforeSuite
	public void InitialSetup() throws Exception
	{
		System.out.println("initial setup");
		lib.beforeTest();
		CommonLibrary.setAccessToken(HttpLibrary.getAccessTokenRestApi());
		CommonLibrary.report = new ExtentReports(System.getProperty("user.dir")
				+ "\\Reports\\TestingType");
		driver = lib.getDriver();
		lib.deleteSheet();
		lib.officeLogin();
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
		lib.switchIntoSheet();
	}

	@Test
	public void insertContact() throws Exception
	{

		logger = CommonLibrary.report.startTest("Inserting Contact");
		boolean success = false;
		Thread.sleep(2000);
		lib.switchToApp();
		Thread.sleep(700);
		lib.loadTemplate("contact");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("div#confirmationPopup button#accept")).click();
		Thread.sleep(10000);
		lib.switchIntoSheet();
		Thread.sleep(1500);
		//String fields = "Contact.NetSuite.1487671332805,.internalId,.firstName,.lastName,.subsidiary,.salutation,.company,.title,.mobilePhone,.officePhone,.phone,.email,.addressbookList.addressbook.addressbookAddress.addr1,.addressbookList.addressbook.addressbookAddress.addr2,.custentity13,.custentity_pick_list";// 1.23456789E9;
		//String idata = ",,Jackson,James,Parent Company,mr.,1 Anonymous,title flow,958658965,6549876544,7894564655,jacksom.james@celigo.com,address one,address 2,testing fft,accouint 2,United State";
		String fields = template.getProperty("contactTemplate");
		System.out.println("Header: "+fields);
		String idata = template.getProperty("contactInsert");
		System.out.println("data: "+idata);
		lib.insertDataIntoTemplate(idata);
		Keyboard press = ((HasInputDevices) driver).getKeyboard();
		lib.switchToApp();
		try
		{
			System.out.println("Modal window is dislayed");
			WebElement modalWindow = driver.findElement(By
					.cssSelector("div.modal-content div.alertAction button"));
			modalWindow.click();
			lib.switchIntoSheet();
			Thread.sleep(2000);
			press.pressKey(Keys.DOWN);
			lib.switchToApp();
		} catch (Exception e)
		{

		}
		lib.clickOn(CommonLibrary.App.InsertAllRows);
		WebElement loading = driver.findElement(By.cssSelector("div#loadingDiv"));
		System.out.println(loading.getAttribute("aria-hidden"));
		while (loading.getAttribute("aria-hidden").equals("false"))
		{
			loading = driver.findElement(By.cssSelector("div#loadingDiv"));
		}
		String notification = lib.getNotification();
		System.out.println(notification);

		HttpLibrary.setFieldsFormat(fields);

		HashMap<String, String> fromExcel = (HashMap<String, String>) lib.rowData(0);
		// fromExcel = handleMissingValues(fromExcel);
		HttpLibrary.printCurrentDataValues(fromExcel);
		ArrayList<String> head = lib.templateHeader(CommonLibrary.getHeader());

		success = fromExcel.get(head.get(0)).equals("");
		// needed as some fields will be populated when user perform
		// refresh(Address/created date...etc)
		if (success)
		{
			lib.clickOn(CommonLibrary.App.RefreshAllRows);
			System.out.println("Refresh table data.... \n getting values from sheet");
			fromExcel = (HashMap<String, String>) lib.rowData(0);
		}

		int id = lib.getRecordId(fromExcel);
		System.out.println("id: " + id);
		if (id != 0)
		{
			Map<String, String> fromNS = lib.getFromNs(head, "contact", id);
			System.out.println("\ndata from NS\n\n");
			HttpLibrary.printCurrentDataValues(fromNS);
			lib.compareData(fromExcel, fromNS, head.get(0));
		} else
		{
			System.out.println("insertion failed \n Error message :" + fromExcel.get(head.get(0)));
		}
		HttpLibrary.doDelete("contact", id);
	}

}
