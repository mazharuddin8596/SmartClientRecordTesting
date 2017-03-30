package util;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import ExtentManager.ExtentManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class RandomTest {
	private String str = "";
	private String str2 = "";
	static ExtentReports report;
	static ExtentTest logger;
	
	public RandomTest(String str, String str2)
	{
		this.str = str;
		this.str2 = str2;
	}
	
	@BeforeSuite
	public void before(){
		report = ExtentManager.GetExtent();
	}

	@Test
	public void firstTest()
	{
		logger = report.createTest("first test");
		System.out.println("Test-1 with data: " + str + " & " + str2);
		logger.log(Status.INFO, "testing");
	}

	@Test
	public void secondTest()
	{
		logger = report.createTest("2nd test");
		System.out.println("Test-2 with data: " + str + " & " + str2);
		logger.log(Status.INFO, "testing");
	}
	@Test
	public void thTest()
	{
		logger = report.createTest("3rd test");
		System.out.println("Test-3 with data: " + str + " & " + str2);
		logger.log(Status.INFO, "testing");
	}
	
	@AfterMethod
	public void after(){
		report.flush();
	}
}