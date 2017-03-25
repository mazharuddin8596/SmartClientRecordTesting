package util;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class BackgroundThread implements Runnable {
	WebDriver driver;
	CommonLibrary lib = new CommonLibrary();
	ExtentTest logger;
	public BackgroundThread(WebDriver driver)
	{
		this.driver = driver;

	}
	public void run()
	{
		driver = lib.getDriver();
		System.out.println("Thread started");
		//logger.log(LogStatus.INFO, "report thing");
		try
		{
			lib.deleteSheet();
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}
/*		int i=0;
		while (true)
		{
			/*i++;
			try
			{
				if (driver.findElement(By.cssSelector("div.ewa-background-working")).isDisplayed())
				{
					System.out.println("Found : background-working screen");
					String screenShotPath = CommonLibrary.capture(driver, "screenShotName");
		           
		          //  logger.log(LogStatus.FAIL, "Snapshot below: " + logger.addScreenCapture(screenShotPath));
					Thread.sleep(1500);
				}
			} catch (org.openqa.selenium.NoSuchElementException e)
			{
				//e.printStackTrace();
			} catch (InterruptedException e)
			{
				
				//e.printStackTrace();
			} catch (IOException e)
			{
				
			}
			if(i==10){
				driver.navigate().refresh();
			}
		}
*/
	}
}
