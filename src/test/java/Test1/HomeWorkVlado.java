package Test1;

// 0. Open Homepage
// 1. Navigate to Login button
// 2. Click on the Login button
// 3. Validate the URL has be changed to the Login page's URL
// 4. Enter userName
// 5. Enter password
// 6. Click Sign in Button
// 7. Check if user is successfully logged by locating the New post button
// 8. Navigate and click on the Profile button
// 9. Validate the proper username is displayed
// 10. Navigate and click on the followers link
// 11. Validate if the followers pop-up window is shown
// 12. Check if Public post is marked by default

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class HomeworkVlado {
    final String baseUrl = "http://training.skillo-bg.com:4200";
    final String homePage = baseUrl + "/posts/all";
    final String loginPage = baseUrl + "/users/login";
    ChromeDriver driver;

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(homePage);
    }

    @Parameters({"username", "password"})
    @Test
    public void loginUser(String username, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        System.out.println("1. Navigate to Login button");
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-link-login")));

        System.out.println("2. Click on the Login button");
        clickElementByElement(loginButton, 5);

        System.out.println("3. Validate the URL has be changed to the Login page's URL");
        wait.until(ExpectedConditions.urlToBe(loginPage));

        System.out.println("4. Enter userName");
        WebElement usernameField = driver.findElement(By.name("usernameOrEmail"));
        enterTextField(usernameField, username);

        System.out.println("5. Enter password");
        WebElement passwordField = driver.findElement(By.name("password"));
        enterTextField(passwordField, password);

        System.out.println("6. Click Sign in Button");
        clickElementByLocator(By.id("sign-in-button"), 5);

        System.out.println("7. Check if user is successfully logged by locating the New post button");
        WebElement newPostButton = driver.findElement(By.xpath("//a[text()='New post']"));
        Assert.assertTrue(newPostButton.isDisplayed(), "New post button not displayed");

        System.out.println("8. Navigate and click on the Profile button");
        clickElementByLocator(By.id("nav-link-profile"), 5);
        
    }

    @Test(dependsOnMethods = "loginUser")
    public void checkFuntionality() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        System.out.println("8. Navigate and click on the Profile button");
        WebElement profileButton = driver.findElement(By.id("nav-link-profile"));
        Assert.assertTrue(profileButton.isDisplayed(), "No Profile button displayed!");
        clickElementByElement(profileButton, 5);

        System.out.println("9. Validate the proper username is displayed");
        WebElement usernameText = driver.findElement(By.xpath("//h2[text()='VladTest']"));
        Assert.assertEquals(usernameText.getText(), "VladTest", "Incorrect username displayed!");

        System.out.println("10. Navigate and click on the followers link");
        clickElementByLocator(By.id("followers"), 5);

        System.out.println("11. Validate if the followers pop-up window is shown");
        WebElement followersPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()='Followers']")));
        Assert.assertEquals(followersPopup.getText(), "Followers", "Incorrect text displayed or pop-op now shown");

        System.out.println("12. Check if Public posts is marked by default");
        WebElement publicPosts = driver.findElement(By.xpath("//label[@class='btn-public btn btn-primary active']"));
        Assert.assertTrue((publicPosts != null), "Public posts button not marked");
    }

    @AfterTest
    public void teardown() {
        driver.close();
    }

    private WebElement clickElementByLocator(By locator, int timeoutSec) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
        return element;
    }

    private WebElement clickElementByElement(WebElement element, int timeoutSec) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
        element.click();
        return element;
    }

    private void enterTextField(WebElement element, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(text);
    }
}
