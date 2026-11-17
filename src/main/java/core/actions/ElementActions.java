package core.actions;

import core.driver.DriverManager;
import core.utils.LoggerUtil;
import core.waitsManagement.WaitUtil;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;


public class ElementActions {

    private static final Logger log =
            LoggerUtil.getLogger(ElementActions.class);

    private static WebDriver driver() {
        return DriverManager.getDriver();
    }

    private static Actions actions() {
        return new Actions(driver());
    }

    /* ================= CLICK ================= */

    @Step("Click element: {elementName}")
    public static void click(WebElement element, String elementName) {

        WaitUtil.waitForVisible(element);
        WaitUtil.waitForClickable(element);

        scrollTo(element, elementName);

        WaitUtil.waitForVisible(element);
        WaitUtil.waitForClickable(element);

        element.click();

        log.info("Clicked element: {}", elementName);
        Allure.step("Clicked " + elementName);
    }

    @Step("Click element: {elementName}")
    public static void clickWithoutScroll(WebElement element, String elementName) {

        WaitUtil.waitForVisible(element);
        WaitUtil.waitForClickable(element);

        element.click();

        log.info("Clicked element: {}", elementName);
        Allure.step("Clicked " + elementName);
    }

    @Step("JS click on element: {elementName}")
    public static void jsClick(WebElement element, String elementName) {

        scrollTo(element, elementName);

        WaitUtil.waitForVisible(element);
        WaitUtil.waitForClickable(element);

        ((JavascriptExecutor) driver())
                .executeScript("arguments[0].click();", element);

        log.info("JS clicked element: {}", elementName);

        Allure.step("Clicked " + elementName);
    }

    /* ================= TYPE ================= */

    @Step("Type '{text}' in {elementName}")
    public static void type(WebElement element,
                            String text,
                            String elementName) {

        scrollTo(element, elementName);

        WaitUtil.waitForVisible(element);
        WaitUtil.waitForClickable(element);

        element.clear();
        element.sendKeys(text);

        log.info("Typed '{}' in element {}", text, elementName);

        Allure.step("Typed " + text + " into " + elementName);
    }

    /* ================= GET TEXT ================= */

    @Step("Get text from {elementName}")
    public static String getText(WebElement element,
                                 String elementName) {

        WaitUtil.waitForVisible(element);

        String text = element.getText();

        log.info("Retrieved text from {} : {}", elementName, text);

        Allure.step("Get " + text + " from " + elementName);

        return text;
    }

    @Step("Get text from {elementName}")
    public static String getText(By by, String elementName) {

        WaitUtil.waitForVisible(by);

        String text = driver().findElement(by).getText();

        log.info("Retrieved text from {} : {}", elementName, text);

        Allure.step("Get text from " + elementName);

        return text;
    }

    /* ================= DROPDOWN ================= */

    @Step("Select '{text}' from dropdown {elementName}")
    public static void selectByText(WebElement element,
                                    String text,
                                    String elementName) {

        scrollTo(element, elementName);

        WaitUtil.waitForVisible(element);

        new Select(element)
                .selectByVisibleText(text);

        log.info("Selected '{}' from dropdown {}", text, elementName);

        Allure.step("Selected " + text + " from " + elementName);
    }

    /* ================= DROPDOWN ================= */

    @Step("Select '{value}' from dropdown {elementName}")
    public static void selectByValue(WebElement element,
                                     String value,
                                     String elementName) {

        scrollTo(element, elementName);

        WaitUtil.waitForVisible(element);

        Select select = new Select(element);

        boolean exists = select.getOptions().stream()
                .anyMatch(option -> value.equals(option.getAttribute("value")));

        if (!exists) {
            throw new NoSuchElementException(
                    "Option with value '" + value + "' was not found in dropdown '" + elementName + "'");
        }

        select.selectByValue(value);

        log.info("Selected value '{}' from dropdown {}", value, elementName);

        Allure.step("Selected value '" + value + "' from " + elementName);
    }

    /* ================= MOUSE ================= */

    @Step("Double click on {elementName}")
    public static void doubleClick(WebElement element,
                                   String elementName) {

        scrollTo(element, elementName);

        WaitUtil.waitForVisible(element);
        WaitUtil.waitForClickable(element);

        actions()
                .doubleClick(element)
                .perform();

        log.info("Double clicked element {}", elementName);

        Allure.step("Double clicked " + elementName);
    }

    @Step("Right click on {elementName}")
    public static void rightClick(WebElement element,
                                  String elementName) {

        scrollTo(element, elementName);

        WaitUtil.waitForClickable(element);

        String href = getAttributeValue(element, "href");

        ((JavascriptExecutor) driver()).executeScript("window.open(arguments[0], '_blank');", href);

        log.info("Right clicked element {}", elementName);

        Allure.step("Right clicked " + elementName);
    }

    @Step("Hover over element: {elementName}")
    public static void hover(WebElement element,
                             String elementName) {

        WaitUtil.waitForVisible(element);

        actions()
                .moveToElement(element)
                .perform();

        log.info("Hovered over element {}", elementName);

        Allure.step("Hovered over " + elementName);
    }

    /* ================= DRAG & DROP ================= */

    @Step("Drag {sourceName} to {targetName}")
    public static void dragAndDrop(WebElement source,
                                   WebElement target,
                                   String sourceName,
                                   String targetName) {

        actions()
                .dragAndDrop(source, target)
                .perform();

        log.info("Dragged {} to {}", sourceName, targetName);
    }

    /* ================= SCROLL ================= */

    /*@Step("Scroll to element: {elementName}")
    public static void scrollTo(WebElement element,
                                String elementName) {

        WaitUtil.threadSleep(5000);

        JavascriptExecutor js = (JavascriptExecutor) driver();

// 1. scroll element na view
        js.executeScript("arguments[0].scrollIntoView(true);", element);

// 2. pomeri malo dole (offset)
        js.executeScript("window.scrollBy(0, 300);");

        log.info("Scrolled to element {}", elementName);

        Allure.step("Scrolled to " + elementName);
    }*/

    @Step("Scroll to element: {elementName}")
    public static void scrollTo(WebElement element, String elementName) {

        WaitUtil.waitForVisible(element);
        WaitUtil.waitForClickable(element);

        JavascriptExecutor js = (JavascriptExecutor) driver();

        js.executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest', behavior: 'instant'});",
                element
        );

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        log.info("Scrolled to element {}", elementName);
        Allure.step("Scrolled to " + elementName);
    }

    /* ================= HELPER ================= */

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /* ================= READ ATTRIBUTE ================= */

    public static String readAttribute(WebElement element,
                                       String attribute,
                                       String elementName) {

        System.out.println("Reading attribute '" + attribute +
                "' from element: " + elementName);

        WaitUtil.waitForVisible(element);

        String value = element.getAttribute(attribute);

        log.info("Retrieved attribute '{}' value '{}' from element '{}'",
                attribute,
                value,
                elementName);

        Allure.step("Read attribute '" + attribute +
                "' from element '" + elementName +
                "' | Value: " + value);

        return value;
    }

    @Step("Get attribute '{attribute}' from element")
    public static String readAttribute(By elementBy, String attribute) {

        WaitUtil.waitForPresence(elementBy);

        String value = driver().findElement(elementBy).getAttribute(attribute);

        log.info("Retrieved attribute '{}' with value '{}'", attribute, value);

        Allure.step("Get attribute " + attribute + " = " + value);

        return value;
        }
    @Step("Get css value from {elementName}")
    public static String getCssValue(WebElement element,
                                 String propertyName) {

        WaitUtil.waitForVisible(element);

        String text = element.getCssValue(propertyName);

        log.info("Retrieved css value from {} : {}", propertyName, text);

        Allure.step("Get " + text + " from " + propertyName);

        return text;
    }

    @Step("Get attribute value from {elementName}")
    public static String getAttributeValue(WebElement element,
                                           String attributeName) {

        WaitUtil.waitForVisible(element);

        String value = element.getAttribute(attributeName);

        log.info("Retrieved attribute {} : {}", attributeName, value);

        Allure.step("Get " + value + " from " + attributeName);

        return value;
    }

    public static boolean isElementDisplayedWithoutWait(WebElement element) {

        WebDriver driver = DriverManager.getDriver();

        try {
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            return element.isDisplayed();

        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;

        } finally {
            driver.manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(10));
        }
    }

    public static void closeKeyboard() {
        try {
            ((AndroidDriver) DriverManager.getDriver()).hideKeyboard();
            log.info("Mobile keyboard closed successfully");
        } catch (Exception e) {
            log.warn("Could not close mobile keyboard: {}", e.getMessage());
        }
    }
}