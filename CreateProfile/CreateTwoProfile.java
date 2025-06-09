package CreateProfile;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class CreateTwoProfile {

    WebDriver driver;
    WebDriverWait wait;

    String baseImagePath = "C:\\Users\\sasha\\OneDrive\\AppData\\Desktop\\Random For Testing\\";

    @BeforeClass
    void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    void createTwoProfiles() throws InterruptedException {
        driver.get("https://staging.appsha.com/login");

        // Login
        driver.findElement(By.name("email")).sendKeys("sashankpandey10@gmail.com");
        driver.findElement(By.name("password")).sendKeys("Pandey@1008");
        driver.findElement(By.xpath("//button[normalize-space()='Log in']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@alt='logo']")));

        // First profile with social links
        createProfile("Krishna Test", "bio test", "sample.jpg", true);

        // Second profile without social links
        createProfile("Test Second", "This is second profile", "Sasha.webp", false);
    }

    public void createProfile(String name, String bio, String imageName, boolean addSocialLinks) throws InterruptedException {
        // Click Create New Profile
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='+ Create new profile']"))).click();

        // Skip Description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[normalize-space()='Skip']"))).click();

        // Upload Image
        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']")));
        fileInput.sendKeys(new File(baseImagePath + imageName).getAbsolutePath());
        Thread.sleep(1000);

        // Name
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='name']"))).sendKeys(name);

        // Bio
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//textarea[@name='bio']"))).sendKeys(bio);

        // Continue
        driver.findElement(By.xpath("//button[normalize-space()='Continue']")).click();

        if (addSocialLinks) {
            Thread.sleep(1000);
            // social link
            List<WebElement> socialInputs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//input[@placeholder='Platform link']")));

            
            if (socialInputs.size() >= 4) {
                socialInputs.get(0).sendKeys("httppss:\\linkedinn");
                socialInputs.get(1).sendKeys("httppss:\\instagram");
                socialInputs.get(2).sendKeys("httpsss:\\facebook");
                socialInputs.get(3).sendKeys("httpsss:\\x");
            } else {
                System.out.println(" Social input fields not found correctly.");
            }
        } else {
            // Skip social links
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[normalize-space()='Skip' or normalize-space()='skip']"))).click();
        }

        
        driver.findElement(By.xpath("//button[normalize-space()='Continue']")).click();

        
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Create your first link']"))).click();

        // Validate success
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[normalize-space()='Add Link']")));

        System.out.println("✅ Profile created: " + name);

        //db
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
