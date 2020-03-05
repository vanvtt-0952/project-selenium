package example.java.selenium.tests;

import example.java.selenium.DriverBase;
import example.java.selenium.util.ObjectMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class BingHomePageIT  extends DriverBase {
    private WebDriver driver;


    @BeforeMethod
    public void driver() throws  Exception {

        driver = getDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://www.bing.com");
    }

    @Test
    public void searchBingPage() throws Exception{

        // Create a new instance of the object map
        ObjectMap objMap = new ObjectMap("elements/loginTest.properties");
        System.out.println("fgdfg " + objMap.getLocator("bing.homepage.textbox"));
        // Retrieve search text box from object map and type search query
        WebElement element = driver.findElement(objMap.getLocator("bing.homepage.textbox"));

        element.sendKeys("Alfa Romeo");

        // Retrieve search button from object map and click it
        element = driver.findElement(objMap.getLocator("bing.homepage.searchbutton"));
        element.click();


        sleepOfThread(2000);

        // Wait
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(objMap.getLocator("bing.resultspage.results")));


        // Retrieve number of search results using results object from object map
        element = driver.findElement(objMap.getLocator("bing.resultspage.results"));
        System.out.println("Search result string: " + element.getText());

        // Verify page title
        Assert.assertEquals(driver.getTitle(), "Alfa Romeo - Bing");


    }
}
