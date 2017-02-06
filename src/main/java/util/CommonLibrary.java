package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
import com.relevantcodes.extentreports.ExtentReports;

public class CommonLibrary {

	private Properties obj;
	private Properties data;
	public static WebDriver driver;
	public static ExtentReports report;

	public void beforeTest()
	{
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")
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

	public CommonLibrary()
	{
		try
		{
			LoadPropertyFiles();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void LoadPropertyFiles() throws IOException
	{
		obj = new Properties();
		// Create Object of FileInputStream Class. Pass file path.
		FileInputStream objfile = new FileInputStream(System.getProperty("user.dir")
				+ "//src//main//resources//properties//objects.properties");
		// Pass object reference objfile to load method of Properties object.
		obj.load(objfile);
		data = new Properties();
		// Create Object of FileInputStream Class. Pass file path.
		FileInputStream testdatafile = new FileInputStream(System.getProperty("user.dir")
				+ "//src//main//resources//properties//testdata.properties");
		// Pass object reference objfile to load method of Properties object.
		data.load(testdatafile);

	}

	public Properties getObj()
	{
		return obj;
	}

	public Properties getData()
	{
		return data;
	}

	public WebDriver getDriver()
	{
		return driver;
	}

	public void setDriver(WebDriver driver)
	{
		CommonLibrary.driver = driver;
	}

	public WebElement Locator(final WebElement selector)
	{
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(150, TimeUnit.SECONDS)
				.pollingEvery(20, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);

		WebElement selectorObj = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver)
			{
				return selector;
			}
		});
		return selectorObj;
	}

	public void officeLogin() throws InterruptedException
	{
		driver.get("https://portal.office.com");

		driver.findElement(By.id("cred_userid_inputtext"))
				.sendKeys("mazhar@celigo2.onmicrosoft.com");
		driver.findElement(By.cssSelector("input#cred_password_inputtext")).sendKeys("Jaca3909");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("button[id='cred_sign_in_button']")).click();

		WebElement oneDrive = driver.findElement(By.id("ShellDocuments_link_text"));
		oneDrive.click();
		// driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL
		// +"\t");
		Thread.sleep(3000);
		for (String s : driver.getWindowHandles())
		{
			driver.switchTo().window(s);
			System.out.println(driver.getTitle());
			if (driver.getTitle().contains("OneDrive"))
			{
				System.out.println(driver.getTitle());
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
		WebElement we = driver.findElement(By.cssSelector("div[aria-label='Open']"));
		we.click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("Open in Excel Online")).click();
		Thread.sleep(1000);
		ArrayList<String> allTabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(allTabs.get(2));

	}

	public void liveLogin() throws InterruptedException
	{
		driver.get("https://login.live.com");
		driver.findElement(By.cssSelector("input[type='email']"))
				.sendKeys("mazharuddin8596@outlook.com");
		driver.findElement(By.cssSelector("input[type='submit']")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("Celigo!@#4");
		driver.findElement(By.cssSelector("input[type='submit']")).click();
		Thread.sleep(2000);
	}

	public void switchToApp()
	{
		System.out.println("switching to App " + driver);
		WebElement appFrame = driver.findElement(By
				.cssSelector("iframe[title='SmartClient Staging App']"));
		driver.switchTo().frame(appFrame);
		System.out.println("Switched to app iframe");
	}

	public void waitForOfficeAddin() throws InterruptedException
	{
		for (int i = 0; i < 4; i++)
		{
			try
			{
				driver.navigate().refresh();
				WebElement f = driver.findElement(By
						.cssSelector("iframe[name='WebApplicationFrame']"));
				driver.switchTo().frame(f);

				Thread.sleep(4000);
				System.out.println("Finding Task pane " + i);
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				WebElement pane = (WebElement) jse
						.executeScript("return document.getElementById('m_excelWebRenderer_ewaCtl_novTaskPaneToolbarContainer')");
				try
				{
					System.out.println("pane visible? " + pane.isDisplayed());
					if (pane.isDisplayed())
					{
						System.out.println("able to detect taskpane");
						break;
					}
				} catch (NullPointerException n)
				{
					System.out.println("retrying");
				}
			} catch (NoSuchElementException e)
			{
				System.out.println("Task pane fails in catch: " + i);
				Thread.sleep(4000);
			}
		}
	}

	public void insertDataIntoTemplate(String data) throws InterruptedException
	{
		Keyboard press = ((HasInputDevices) driver).getKeyboard();
		press.pressKey(Keys.LEFT);
		press.pressKey(Keys.DOWN);
		System.out.println("Inserting data into Template");

		String[] col_data = data.split("\\,");
		// inserting data in contact template
		press.pressKey(Keys.TAB);
		for (int i = 0; i < col_data.length; i++)
		{
			press.pressKey(Keys.TAB);
			press.pressKey(col_data[i]);
			System.out.println(col_data[i]);
			Thread.sleep(1000);
		}
	}

	public void deleteSheetData() throws InterruptedException
	{
		Keyboard press = ((HasInputDevices) driver).getKeyboard();
		Thread.sleep(3000);
		press.pressKey(Keys.chord(Keys.CONTROL, Keys.SPACE));
		press.pressKey(Keys.chord(Keys.SHIFT, Keys.SPACE));
		press.pressKey(Keys.DELETE);
		Thread.sleep(5000);
		press.pressKey(Keys.chord(Keys.CONTROL, Keys.HOME));
		Thread.sleep(2000);
	}

	public void selectSheet() throws InterruptedException
	{
		Keyboard press = ((HasInputDevices) driver).getKeyboard();
		List<WebElement> sheetList = driver.findElements(By
				.cssSelector("table.ewr-grdcontarea-ltr  tbody tr td"));
		sheetList.get(2).click();
		Thread.sleep(5000);
		press.pressKey(Keys.DOWN);
		press.pressKey(Keys.chord(Keys.CONTROL, Keys.HOME));

	}

	public void loadTemplate(String name) throws InterruptedException
	{
		List<WebElement> Template_List = driver.findElements(By.cssSelector("div.section ul li"));
		System.out.println("Template List");
		for (WebElement we : Template_List)
		{
			if (we.getText().contains(name))
			{
				System.out.println("Clicking on Contact");
				we.findElement(By.cssSelector("a[title='Load template']")).click();
				Thread.sleep(2000);// .name ng-binding")).click();//
				break;
			}
			System.out.println(we.getText());
		}
	}

	public void switchIntoSheet() throws InterruptedException
	{
		WebElement mainsheet;
		driver.switchTo().defaultContent();
		mainsheet = driver.findElement(By.cssSelector("iframe[name='WebApplicationFrame']"));
		driver.switchTo().frame(mainsheet);
		selectSheet();
	}

	public void click(App on) throws InterruptedException
	{
		List<WebElement> appMenu = driver.findElements(By.cssSelector("div.menuContent ul li"));
		// System.out.println(appMenu.get(1).getAttribute("class"));
		List<WebElement> middleButtons = driver.findElements(By.cssSelector("div.msgBox"));
		List<WebElement> checkboxes = driver.findElements(By.cssSelector("div.checkbox"));

		switch (on)
		{
			case InsertAllRows :
				appMenu.get(1).click();
				System.out.println("insert checkbox selected ? " + checkboxes.get(2).isSelected());
				if (!checkboxes.get(2).isSelected())
				{
					checkboxes.get(2).click();
				} else
				{
					System.out.println("already checkbox is checked");
				}
				middleButtons.get(1).findElement(By.cssSelector("div.button-box a")).click();
				break;
			case DeleteAllRows :
				appMenu.get(2).click();
				if (!checkboxes.get(3).isSelected())
				{
					checkboxes.get(3).click();
				} else
				{
					System.out.println("already checkbox is checked");
				}
				middleButtons.get(2).findElement(By.cssSelector("div.button-box a")).click();
				break;
			case RefreshSelectedRows :
				appMenu.get(3).click();
				middleButtons.get(2).findElement(By.cssSelector("div.button-box a")).click();
				break;
			case DownloadWithFilter :
				appMenu.get(0).click();
				if (!checkboxes.get(1).isSelected())
				{
					checkboxes.get(1).click();
				} else
				{
					System.out.println("already checkbox is checked");
				}
				middleButtons.get(0).findElement(By.cssSelector("div.button-box a")).click();
				break;
			case InsertSelectedRows :
				appMenu.get(1).click();
				Thread.sleep(1000);
				if (checkboxes.get(2).isSelected())
				{
					checkboxes.get(2).click();
				} else
				{
					System.out.println("already checkbox is unchecked");
				}
				middleButtons.get(1).findElement(By.cssSelector("div.button-box a")).click();
				break;
			case DeleteSelectedRows :
				appMenu.get(2).click();
				Thread.sleep(1000);
				if (checkboxes.get(3).isSelected())
				{
					checkboxes.get(3).click();
				} else
				{
					System.out.println("already checkbox is unchecked");
				}
				middleButtons.get(2).findElement(By.cssSelector("div.button-box a")).click();
				break;
			case RefreshAllRows :
				appMenu.get(3).click();
				Thread.sleep(1000);
				middleButtons.get(3).findElement(By.cssSelector("div.button-box a")).click();
				break;
			case DownloadWithoutFilter :
				appMenu.get(0).click();
				Thread.sleep(1000);
				if (checkboxes.get(1).isSelected())
				{
					checkboxes.get(1).click();
				} else
				{
					System.out.println("already checkbox is unchecked");
				}
				middleButtons.get(0).findElement(By.cssSelector("div.button-box a")).click();
				break;
			default :
				System.out.println("Invalid input");
		}

	}
	public String getNotification() throws InterruptedException
	{
		int Time = 0;
		String notification = "";
		do
		{
			Time += 1;
			try
			{
				WebElement notify = driver.findElement(By
						.cssSelector("div.notifyjs-container span"));
				if (notify.isDisplayed())
				{
					notification = notify.getText();
					if (notification.equals(""))
					{
						notify = driver.findElement(By.cssSelector("div.notifyjs-container span"));
						System.out.println("trying to get notification again");
					}
					Thread.sleep(2000);
					break;
				}
			} catch (Exception e)
			{
				System.out.println("not visible");
				Thread.sleep(20);
			}

		} while (Time < 50);

		return notification;
	}

	public enum App {
		InsertSelectedRows, InsertAllRows, DeleteSelectedRows, DeleteAllRows, RefreshSelectedRows, RefreshAllRows, DownloadWithFilter, DownloadWithoutFilter
	}
}