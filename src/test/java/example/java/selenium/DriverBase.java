package example.java.selenium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import example.java.selenium.config.DriverFactory;
import example.java.selenium.listeners.ScreenshotListener;

@Listeners(ScreenshotListener.class)
public class DriverBase {

	private static final Logger LOGGER = Logger.getLogger(DriverBase.class);

	private static List<DriverFactory> webDriverThreadPool = Collections
			.synchronizedList(new ArrayList<DriverFactory>());
	private static ThreadLocal<DriverFactory> driverFactoryThread;

	@BeforeSuite(alwaysRun = true)
	public static void instantiateDriverObject() {
		driverFactoryThread = ThreadLocal.withInitial(() -> {
			DriverFactory driverFactory = new DriverFactory();
			webDriverThreadPool.add(driverFactory);
			return driverFactory;
		});
	}

	public static RemoteWebDriver getDriver() throws Exception {
		return driverFactoryThread.get().getDriver();
	}

	@AfterMethod(alwaysRun = true)
	public static void clearCookies() {
		try {
			driverFactoryThread.get().getStoredDriver().manage().deleteAllCookies();
		} catch (Exception ignored) {
			System.out.println("Unable to clear cookies, driver object is not viable...");
		}
	}

	@AfterSuite(alwaysRun = true)
	public static void closeDriverObjects() {
		for (DriverFactory driverFactory : webDriverThreadPool) {
			driverFactory.quitDriver();
		}
	}

	protected void sleepOfThread(int milisecond) {
		try {
			Thread.sleep(milisecond);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
		}
	}

	protected void implicitlyWaitOfWait(WebDriver driver, long timeout, TimeUnit timeUnit) {
		driver.manage().timeouts().implicitlyWait(timeout, timeUnit);
	}

	protected void visibilityOfElementLocatedOfWait(WebDriver driver, long timeout, By element) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.until(ExpectedConditions.visibilityOfElementLocated(element));
	}

	protected void visibilityOfAllElementsOfWait(WebDriver driver, long timeout, List<WebElement> listResult) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.until(ExpectedConditions.visibilityOfAllElements(listResult));
	}
}