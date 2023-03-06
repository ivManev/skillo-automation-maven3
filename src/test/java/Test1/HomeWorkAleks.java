package Test1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.hc.core5.http.io.SessionOutputBuffer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

//1. Navigate to Login button
//2. Click on the Login button
//3. Validate the URL has been changed to the Login page's URL
//4. Enter userName
//5. Enter password
//6. Click Sign in Button
//7. Navigate and click on the Profile button
//8. Validate that the username field is displayed
//9. Validate that the post field is visible
//10. Navigate and click on the All posts button

public class HomeWorkAleks {
    final String homeUrl = "http://training.skillo-bg.com:4200/posts/all";
    final String loginUrl = "http://training.skillo-bg.com:4200/users/login";
    ChromeDriver driver;


    @BeforeTest
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(homeUrl);
    }

    @Test
    public void loginUser(){
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        System.out.println("Navigate to Login button");
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-link-login")));

        System.out.println("Click on the Login button");
        clickElement((By.id("nav-link-login")),5);

        System.out.println("Validate the URL has been changed to the Login page's URL");
        wait.until(ExpectedConditions.urlToBe(loginUrl));

        System.out.println("Enter userName");
        WebElement userName = driver.findElement(By.name("usernameOrEmail"));
        enterTextField(userName, "sani");

        System.out.println("Enter password");
        WebElement passwordField = driver.findElement(By.name("password"));
        enterTextField(passwordField, "123456");

        System.out.println("Click Sign in Button");
        clickElement(By.id("sign-in-button"), 5);

        System.out.println("Navigate and click on the Profile button");
        clickElement(By.id("nav-link-profile"), 5);


    }
    @Test
    public void functionality(){
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        System.out.println(" Navigate and click on the Profile button");
        WebElement profileButton = driver.findElement(By.id("nav-link-profile"));
        Assert.assertTrue(profileButton.isDisplayed(), "No Profile button displayed!");
        clickElement(By.id("nav-link-profile"),5);

        System.out.println("Validate that the username field is displayed");
        WebElement userNameField = driver.findElement(By.linkText("sani"));
        Assert.assertEquals(userNameField.getText(),"sani","No such user found");

        System.out.println("Validate that the post field is visible");
        WebElement postField = driver.findElement(By.linkText("posts"));
        Assert.assertEquals(postField.getText(),"posts","No posts found");

        System.out.println("Navigate and click on the All posts button");
        WebElement allButton = driver.findElement(By.className("btn-all btn btn-primary"));
        Assert.assertTrue(allButton.isDisplayed(), "All button not visible");

    }


    private WebElement clickElement(By locator, int timeOutSec){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutSec));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
        return element;
    }
    private void enterTextField(WebElement element, String text){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(text);
        Boolean isValid = element.getAttribute("class").contains("is-valid");
        Assert.assertTrue(isValid, "The field is invalid");
    }

    @AfterTest
    public void tearDown(){
        driver.close();
    }

}

