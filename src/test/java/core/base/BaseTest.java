package core.base;

import core.driver.DriverManager;
import core.localization.MessageProvider;
import core.localization.UrlProvider;
import core.utils.LoggerUtil;
import core.utils.ScreenshotUtil;
import core.utils.AllureEnvironmentWriter;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class BaseTest {

    private static final Logger log =
            LoggerUtil.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    @Parameters({"env", "browser"})
    public void setUp(String env, String browser) {

        System.setProperty("env", env);
        System.setProperty("browser", browser);

        // Always start mobile driver
        DriverManager.initMobileDriver();

        AllureEnvironmentWriter.writeEnvironment();

        System.out.println("Mobile driver started");
    }

    protected void openUrl(String url) {
        UrlProvider.initialize(url);
        MessageProvider.initialize(url);

        DriverManager.getDriver().get(url);
    }

    // Switch from mobile to desktop browser when needed
    protected void startDesktopBrowser() {

        // Close current mobile session
        DriverManager.quitDriver();

        // Start desktop browser
        DriverManager.initDriver();

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            DriverManager.getDriver().manage().window().maximize();
        } else {
            DriverManager.getDriver().manage().window()
                    .setSize(new Dimension(1920, 1080));
        }

        System.out.println("Desktop browser started");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        String status;

        switch (result.getStatus()) {
            case ITestResult.FAILURE:
                status = "FAILED";
                break;
            case ITestResult.SUCCESS:
                status = "PASSED";
                break;
            case ITestResult.SKIP:
                status = "SKIPPED";
                break;
            default:
                status = "UNKNOWN";
        }

        WebDriver driver = DriverManager.getDriver();

        if (driver == null) {
            return;
        }

        try {
            if (!driver.getWindowHandles().isEmpty()) {

                String path = ScreenshotUtil.captureScreenshot(
                        result.getName() + "_" + status, status);

                if (path != null && !path.isBlank()) {
                    result.setAttribute("screenshotPath", path);
                }
            }

        } catch (WebDriverException e) {
            log.warn("Could not capture screenshot: " + e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error during screenshot capture", e);

        } finally {
            DriverManager.quitDriver();
        }
    }
}