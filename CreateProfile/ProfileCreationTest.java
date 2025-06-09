package CreateProfile;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;

public class ProfileCreationTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    void completeProfileCreation() throws InterruptedException {
        driver.get("https://staging.appsha.com/login");

        // Step 1: Login with valid credentials
        driver.findElement(By.name("email")).sendKeys("sashankpandey10@gmail.com");
        driver.findElement(By.name("password")).sendKeys("Pandey@1008");
        driver.findElement(By.xpath("//button[normalize-space()='Log in']")).click();
        Thread.sleep(2000);

        // Step 2: Click on 'Create new profile'
        WebElement createProfileBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[normalize-space()='+ Create new profile']")));
        createProfileBtn.click();
        Thread.sleep(2000);

        // Step 3: Skip profile description
        WebElement skipDesc = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[normalize-space()='Skip']")));
        skipDesc.click();
        Thread.sleep(2000);

        // Step 4: Upload profile picture via file input only
        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='file']")));
        fileInput.sendKeys("C:\\Users\\sasha\\OneDrive\\AppData\\Desktop\\Random For Testing\\sample.jpg");
        Thread.sleep(2000);

        // Step 5: Fill the Name field (using name attribute instead of dynamic ID)
        WebElement nameField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@name='name']")));
        nameField.sendKeys("Automated User");

        // Step 6: Fill Bio
        WebElement bioField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//textarea[@name='bio']")));
        bioField.sendKeys("This is a test bio entered by automation.");

        // Step 7: Click Continue
        driver.findElement(By.xpath("//button[normalize-space()='Continue']")).click();
        Thread.sleep(2000);

        // Step 8: Skip social links
        WebElement skipSocial = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[normalize-space()='skip' or normalize-space()='Skip']")));
        skipSocial.click();
        Thread.sleep(2000);

        // Step 9: Click 'Create your first link'
        WebElement createFirstLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Create your first link']")));
        createFirstLink.click();
        Thread.sleep(2000);

        // Step 10: Validate presence of "Add Link" text
        WebElement addLinkText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[normalize-space()='Add Link']")));

        if (addLinkText.isDisplayed()) {
            System.out.println("Profile setup completed successfully. 'Add Link' text found.");
        } else {
            System.out.println("Profile setup failed. 'Add Link' text not found.");
        }

        Thread.sleep(3000);
    }

    @AfterClass
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
