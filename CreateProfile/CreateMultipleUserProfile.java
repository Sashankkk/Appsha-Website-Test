package CreateProfile;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;

public class CreateMultipleUserProfile {
    WebDriver driver;
    WebDriverWait wait;

    String baseImagePath = "C:\\Users\\sasha\\OneDrive\\AppData\\Desktop\\Random For Testing\\";

    String[] profileNames = {
        "Alpha", "Bravo", "Charlie", "Delta", "Echo",
        "Foxtrot", "Golf", "Hotel", "India", "Juliet"
    };

    String[] bios = {
        "Bio for Alpha", "Bio for Bravo", "Bio for Charlie", "Bio for Delta", "Bio for Echo",
        "Bio for Foxtrot", "Bio for Golf", "Bio for Hotel", "Bio for India", "Bio for Juliet"
    };

    String[] imageNames = {
        "Sasha.webp", "sample.jpg"
    };

    @BeforeClass
    void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    void completeMultipleProfiles() throws InterruptedException {
        driver.get("https://staging.appsha.com/login");

        // Login once
        driver.findElement(By.name("email")).sendKeys("sashankpandey10@gmail.com");
        driver.findElement(By.name("password")).sendKeys("Pandey@1008");
        driver.findElement(By.xpath("//button[normalize-space()='Log in']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@alt='logo']")));

        // Create 10 profiles
        for (int i = 0; i < 10; i++) {
            createProfile(profileNames[i], bios[i], imageNames[i % 2]);
        }
    }

    void createProfile(String name, String bio, String imageFile) throws InterruptedException {
        // Step 1: Click on 'Create new profile'
        WebElement createProfileBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[normalize-space()='+ Create new profile']")));
        createProfileBtn.click();
        Thread.sleep(2000);

        // Step 2: Skip profile description
        WebElement skipDesc = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[normalize-space()='Skip']")));
        skipDesc.click();
        Thread.sleep(2000);

        // Step 3: Upload profile picture via file input
        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='file']")));
        fileInput.sendKeys(baseImagePath + imageFile);
        Thread.sleep(2000);

        // Step 4: Fill Name and Bio
        WebElement nameField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@name='name']")));
        nameField.clear();
        nameField.sendKeys(name);

        WebElement bioField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//textarea[@name='bio']")));
        bioField.clear();
        bioField.sendKeys(bio);

        // Step 5: Click Continue
        driver.findElement(By.xpath("//button[normalize-space()='Continue']")).click();
        Thread.sleep(2000);

        // Step 6: Skip social links
        WebElement skipSocial = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[normalize-space()='skip' or normalize-space()='Skip']")));
        skipSocial.click();
        Thread.sleep(2000);

        // Step 7: Click 'Create your first link'
        WebElement createFirstLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Create your first link']")));
        createFirstLink.click();
        Thread.sleep(2000);

        // Step 8: Validate presence of "Add Link" text
        WebElement addLinkText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[normalize-space()='Add Link']")));

        if (addLinkText.isDisplayed()) {
            System.out.println("Profile created: " + name);
        } else {
            System.out.println("Failed to create profile: " + name);
        }

        // Step 9: Click logo to return to dashboard
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt='logo']"))).click();
        Thread.sleep(2000);
    }

    @AfterClass
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
