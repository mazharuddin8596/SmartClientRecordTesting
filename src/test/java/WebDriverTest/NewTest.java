package WebDriverTest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import util.BackgroundThread;
import util.CommonLibrary;
import util.HttpLibrary;
public class NewTest {
	CommonLibrary lib = new CommonLibrary();
	Properties template = lib.getTemplate();
	private WebDriver driver;

	
	public void InitialSetup() throws Exception
	{
		System.out.println("initial setup");
		lib.beforeTest();
		CommonLibrary.setAccessToken(HttpLibrary.getAccessTokenRestApi());

		driver = lib.getDriver();

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
		// lib.setDriver(driver);
		lib.switchIntoSheet();
	}

	@Test
	public void testEasy() throws Exception
	{
		InitialSetup();
		WebElement appFrame = driver.findElement(By
				.cssSelector("iframe[title='SmartClient Staging App']"));
		driver.switchTo().frame(appFrame);
		try
		{
			driver.findElement(By.cssSelector("body[ng-app='SmartClient']"));
			System.out.println("smart client");
			driver.navigate().refresh();
			Thread.sleep(20000);
			Files.write(Paths.get("e:/fileName.txt"), driver.getPageSource().getBytes());
			
		} catch (Exception e)
		{
			System.out.println("check wht is refresh");
			driver.navigate().refresh();
		}
	}
	//@BeforeTest
	public void beforeTest()
	{
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")
				+ "//src//lib//chromedriver.exe");

		driver = new ChromeDriver();

		lib.beforeTest();
	}
	
}