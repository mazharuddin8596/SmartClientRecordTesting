package util;

import org.openqa.selenium.WebDriver;

public class BackgroundThread implements Runnable {
    WebDriver driver;
    CommonLibrary lib = new CommonLibrary();
    com.aventstack.extentreports.ExtentTest logger;

    public BackgroundThread(WebDriver driver) {
	this.driver = driver;

    }

    public void run() {
	driver = lib.getDriver();
	System.out.println("Thread started");
	// logger.log(LogStatus.INFO, "report thing");
	try {
	    lib.deleteSheet();
	} catch (Exception e1) {
	    e1.printStackTrace();
	}

	/*
	 * while (true) { System.out
	 * .println("Running thread in background to check error pop-ups");
	 * 
	 * try { // Thread.sleep(2000); WebElement errorpopup =
	 * driver.findElement(By
	 * .cssSelector("div#errorewaDialogInner button")); if
	 * (errorpopup.isDisplayed()) { errorpopup.click(); } } catch (Exception
	 * e) {
	 * 
	 * } }
	 */

	/*
	 * int i=0; while (true) { /*i++; try { if
	 * (driver.findElement(By.cssSelector
	 * ("div.ewa-background-working")).isDisplayed()) {
	 * System.out.println("Found : background-working screen"); String
	 * screenShotPath = CommonLibrary.capture(driver, "screenShotName");
	 * 
	 * // logger.log(LogStatus.FAIL, "Snapshot below: " +
	 * logger.addScreenCapture(screenShotPath)); Thread.sleep(1500); } }
	 * catch (org.openqa.selenium.NoSuchElementException e) {
	 * //e.printStackTrace(); } catch (InterruptedException e) {
	 * 
	 * //e.printStackTrace(); } catch (IOException e) {
	 * 
	 * } if(i==10){ driver.navigate().refresh(); } }
	 */
    }
}
