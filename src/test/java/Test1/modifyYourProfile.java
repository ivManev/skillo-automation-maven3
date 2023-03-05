package Test1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class modifyYourProfile {

// Test - successful user data modification
//    0. Run Login test => aiming to reach Profile page
//    1. Click on the Modify your page icon
//    2. Validate that the Modify your page text is visible
//    3. Clear username field
//    4. Clear email field
//    5. Enter new username
//    6. Enter new email - да бъде използван методът с isValid
//    7. Enter password - да бъде използван методът с isValid
//    8. Confirm password - да бъде използван методът с isValid
//    9. Enter public info - да бъде използван методът с isValid - тук няма такава проверка,
//    макар чр иска 6 символа, за да се отключи бутонът Save
//    10. Click Save button - да се ползва готовият клик метод след проверка за това дали бутонът е enabled
//    11. Validate that a pop-up appears indicating a successful profile update
//    12. Validate that the username has been changed successfully


    ChromeDriver driver;
    final String baseUrl = "http://training.skillo-bg.com:4200";
    final String loginUrl = baseUrl + "/users/login";
    final String homeUrl = baseUrl + "/posts/all";

    @BeforeTest
    public void setup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(homeUrl);
    }

    @Parameters({"username", "password"})
    @Test
    public void LoginTest2(String username, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));


        System.out.println("1. Navigate to Login page by clicking Login button");
        clickElement(By.id("nav-link-login"), 5, 200);

        System.out.println("2. Validate that the url is correct");
        wait.until(ExpectedConditions.urlToBe(loginUrl));

        System.out.println("3. Validate that the Sign in text is visible");
        WebElement signInText = driver.findElement(By.xpath("//p[text()='Sign in']"));
        wait.until(ExpectedConditions.visibilityOf(signInText));

        System.out.println("4. Enter correct username");
        WebElement usernameField = driver.findElement(By.cssSelector("input[formcontrolname=usernameOrEmail]"));
        enterTextField(usernameField, username);

        System.out.println("5. Enter correct password");
        WebElement passField = driver.findElement(By.name("password"));
        enterTextField(passField, password);

        System.out.println("6. Click Sign in button");
        clickElement(By.cssSelector("button.my-4"), 5, 200);

        System.out.println("7. Validate that the url is correct (Home page expected)");
        wait.until(ExpectedConditions.urlToBe(homeUrl));

        System.out.println("8. Validate that there is a Profile tab button visible");
        WebElement profileTabButton= driver.findElement(By.cssSelector("#nav-link-profile"));
        wait.until(ExpectedConditions.visibilityOf(profileTabButton));

        System.out.println("9. Validate that there is a New post tab button visible");
        WebElement newPostTabButton = driver.findElement(By.id("nav-link-new-post"));
        wait.until(ExpectedConditions.visibilityOf(newPostTabButton));

        System.out.println("10. Click Profile button");
        clickElement(By.cssSelector("#nav-link-profile"), 5, 200);

        System.out.println("11. Validate that the correct username is displayed");
        WebElement usernameHeader = driver.findElement(By.cssSelector("app-profile-section h2"));
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(usernameHeader.getText(), username, "Incorrect username displayed");

        softAssert.assertAll();
    }

    @Parameters({"newUsername", "password", "publicInfo"})
    @Test
    public void modifyProfile(String newUsername, String password, String publicInfo) {
        String newEmail = newUsername + "@abv.bg";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));


        System.out.println("1. Click on the Modify your page icon");
        clickElement(By.cssSelector("i.fa-user-edit"), 5, 400);

        System.out.println("2. Validate that the Modify your page text is visible");
        WebElement modifyYourProfileHeader = driver.findElement(By.cssSelector("app-edit-profile-modal h4"));
        wait.until(ExpectedConditions.visibilityOf(modifyYourProfileHeader));

        System.out.println("3. Clear username field");
        WebElement usernameField = driver.findElement(By.cssSelector("input[formcontrolname='username']"));
        clearTextField(usernameField);

        System.out.println("4. Clear email field");
        WebElement emailField = driver.findElement(By.cssSelector("input[formcontrolname='email']"));
        clearTextField(emailField);

        System.out.println("5. Enter new username");
        enterTextField(usernameField, newUsername);

        System.out.println("6. Enter new email");
        enterTextField(emailField, newEmail);

        System.out.println("7. Enter password");
        WebElement passwordField = driver.findElement(By.cssSelector("input[formcontrolname='password']"));
        enterTextField(passwordField, password);

        System.out.println("8. Confirm password");
        WebElement confirmPasswordField = driver.findElement(By.cssSelector("input[formcontrolname='confirmPassword']"));
        enterTextField(confirmPasswordField, password);

        System.out.println("9. Enter public info");
        WebElement publicInfoField = driver.findElement(By.cssSelector("textarea[formcontrolname='publicInfo']"));
        enterTextField(publicInfoField, publicInfo);

        System.out.println("10. Click Save button");
        clickElement(By.cssSelector("button.btn-primary"), 2, 200);

        System.out.println("11. Validate that a pop-up appears indicating a successful profile update");
        WebElement toastMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast-message")));
        String toastMsgText = toastMsg.getText().trim();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(toastMsgText, "Profile updated", "Unsuccessful profile update!");

        System.out.println("12. Validate that the username has been changed successfully");
        WebElement usernameNew = driver.findElement(By.cssSelector(".row h2"));
        softAssert.assertEquals(usernameNew.getText().trim(), newUsername, "Incorrect new username displayed!");

        softAssert.assertAll();
    }

    /**
     * Waits for an WebElement to become clickable and then clicks it
     * @param locator
     * @param totalTimeoutSec
     * @param retryTimeoutMs
     * @return
     */
    private WebElement clickElement(By locator, int totalTimeoutSec, int retryTimeoutMs) {
        FluentWait<ChromeDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(totalTimeoutSec))
                .pollingEvery(Duration.ofMillis(retryTimeoutMs))
                .ignoring(TimeoutException.class);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
        return element;
    }

    /**
     * Enters text and validates whether the input is correct
     * @param element
     * @param text
     */
    private void enterTextField(WebElement element, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(text);
    }

    /**
     * Deletes auto-leaded text
     * @param element
     */
    private void clearTextField(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
    }

    @AfterTest
    public void cleanup(){
        driver.close();
    }

}
