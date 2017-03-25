package WebDriverTest;

import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import util.CommonLibrary;
import util.HttpLibrary;
public class NewTest {
	CommonLibrary lib = new CommonLibrary();
	Properties template = lib.getTemplate();
	private WebDriver driver;
	ExtentTest logger;
	
	
	@Test
	public void testEasy() throws Exception
	{
		
	}
	@BeforeTest
	public void beforeTest()
	{
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")
				+ "//src//lib//chromedriver.exe");
		
			driver = new ChromeDriver();
		
			lib.beforeTest();
	}
	@AfterTest
	public void afterTest()
	{
		driver.quit();
	}
}