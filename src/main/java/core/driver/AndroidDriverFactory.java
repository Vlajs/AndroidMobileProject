package core.driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/*
public class AndroidDriverFactory {

    public static AndroidDriver createAndroidDriver() {

        UiAutomator2Options options = new UiAutomator2Options();

        options.setCapability("platformName", "Android");
        options.setCapability("appium:automationName", "UiAutomator2");
        options.setCapability("appium:udid", "inpb95xslj7p9lpb");
        options.setCapability("browserName", "Chrome");
        options.setCapability("appium:noReset", true);
        options.setCapability("appium:chromedriverAutodownload", true);

        try {
            AndroidDriver driver = new AndroidDriver(
                    new URL("http://127.0.0.1:4723"),
                    options
            );

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            return driver;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL", e);
        }
    }
}*/

public class AndroidDriverFactory {

    private static final String REAL_UDID = "inpb95xslj7p9lpb";   // Xiaomi
    private static final String EMULATOR_UDID = "emulator-5554";

    public static AndroidDriver createAndroidDriver() {

        String device = System.getProperty("device", "real");          // real | emulator
        String platformVersion = System.getProperty("platformVersion"); // npr. 11, 15 (opciono)

        String udid = System.getProperty("udid",
                "emulator".equalsIgnoreCase(device) ? EMULATOR_UDID : REAL_UDID);

        UiAutomator2Options options = new UiAutomator2Options();
        options.setCapability("platformName", "Android");
        options.setCapability("appium:automationName", "UiAutomator2");
        options.setCapability("appium:udid", udid);
        options.setCapability("browserName", "Chrome");
        options.setCapability("appium:noReset", true);
        options.setCapability("appium:chromedriverAutodownload", true);
        options.setCapability("appium:newCommandTimeout", 300);
        options.setCapability("appium:adbExecTimeout", 60000);

        if (platformVersion != null && !platformVersion.isBlank()) {
            options.setCapability("appium:platformVersion", platformVersion);
        }

        try {
            AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL", e);
        }
    }
}
