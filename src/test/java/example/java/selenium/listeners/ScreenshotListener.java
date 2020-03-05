package example.java.selenium.listeners;

import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import com.relevantcodes.extentreports.ExtentReports;

import static example.java.selenium.DriverBase.getDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenshotListener extends TestListenerAdapter {

    private ExtentReports extentReports = new ExtentReports();


    @Override
    public void onStart(ITestContext iTestContext) {
        super.onStart(iTestContext);
        extentReports.init("extent-report.html",true);
    }


    public void onTestStart(ITestResult result) {
        String testMethodName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();
        System.out.println();
        System.out.println("START TEST " + testMethodName);

        //extentReports = ExtentReports.get(result.getTestClass().getRealClass());
        extentReports.startTest(testMethodName);
        if (testDescription != null) {
            if (!testDescription.isEmpty()) {
                System.out.println("Description: " + testDescription);
            }
        }
    }

    private boolean createFile(File screenshot) {
        boolean fileCreated = false;

        if (screenshot.exists()) {
            fileCreated = true;
        } else {
            File parentDirectory = new File(screenshot.getParent());
            if (parentDirectory.exists() || parentDirectory.mkdirs()) {
                try {
                    fileCreated = screenshot.createNewFile();
                } catch (IOException errorCreatingScreenshot) {
                    errorCreatingScreenshot.printStackTrace();
                }
            }
        }

        return fileCreated;
    }

    private void writeScreenshotToFile(WebDriver driver, File screenshot) {
        try {
            FileOutputStream screenshotStream = new FileOutputStream(screenshot);
            screenshotStream.write(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
            screenshotStream.close();
        } catch (IOException unableToWriteScreenshot) {
            System.err.println("Unable to write " + screenshot.getAbsolutePath());
            unableToWriteScreenshot.printStackTrace();
        }
    }

    @Override
    public void onTestFailure(ITestResult failingTest) {
        try {
            WebDriver driver = getDriver();
            String screenshotDirectory = System.getProperty("screenshotDirectory", "target/screenshots");
            String screenshotAbsolutePath = screenshotDirectory + File.separator + System.currentTimeMillis() + "_" + failingTest.getName() + ".png";
            File screenshot = new File(screenshotAbsolutePath);
            if (createFile(screenshot)) {
                try {
                    writeScreenshotToFile(driver, screenshot);
                } catch (ClassCastException weNeedToAugmentOurDriverObject) {
                    writeScreenshotToFile(new Augmenter().augment(driver), screenshot);
                }
                System.out.println("Written screenshot to " + screenshotAbsolutePath);
            } else {
                System.err.println("Unable to create " + screenshotAbsolutePath);
            }

            System.out.println("#####Start Test Custom Message for Failure#####");
            String testMethodName = failingTest.getMethod().getMethodName();
            extentReports.log(LogStatus.FAIL,testMethodName);
            extentReports.endTest();

        } catch (Exception ex) {
            System.err.println("Unable to capture screenshot...");
            ex.printStackTrace();
        }
    }

    @Override
    public void onTestSuccess(ITestResult successingTest) {
        try {
            WebDriver driver = getDriver();
            String screenshotDirectory = System.getProperty("screenshotDirectory", "target/screenshots");
            String screenshotAbsolutePath = screenshotDirectory + File.separator + System.currentTimeMillis() + "_" + successingTest.getName() + ".png";
            File screenshot = new File(screenshotAbsolutePath);
            if (createFile(screenshot)) {
                try {
                    writeScreenshotToFile(driver, screenshot);
                } catch (ClassCastException weNeedToAugmentOurDriverObject) {
                    writeScreenshotToFile(new Augmenter().augment(driver), screenshot);
                }
                System.out.println("Written screenshot to " + screenshotAbsolutePath);
            } else {
                System.err.println("Unable to create " + screenshotAbsolutePath);
            }


            // count
            System.out.println("#####Start Test Custom Message for Success#####");
            String testMethodName = successingTest.getMethod().getMethodName();
            extentReports.log(LogStatus.PASS,testMethodName);
            extentReports.endTest();

        } catch (Exception ex) {
            System.err.println("Unable to capture screenshot...");
            ex.printStackTrace();
        }
    }


    @Override
    public void onTestSkipped(ITestResult result){
        System.out.println("#####Start Test Custom Message for SKIPPY#####");
        String testMethodName = result.getMethod().getMethodName();
        extentReports.log(LogStatus.SKIP, testMethodName);
        extentReports.endTest();
    }


    @Override
    public void onFinish(ITestContext iTestContext) {
        super.onFinish(iTestContext);

    }

}