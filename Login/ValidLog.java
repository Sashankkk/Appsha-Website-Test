package Login;

import static org.testng.Assert.fail;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

public class ValidLog {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test(dataProvider = "dp")
    void testLogin(String email, String pwd, boolean isLastUser) throws InterruptedException {
        driver.get("https://staging.appsha.com/login");
        System.out.println("\n=== Testing Login for: " + email + " ===");
        Thread.sleep(2000);

        try {
            // Fill email
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
            emailField.clear();
            emailField.sendKeys(email);
            Thread.sleep(2000);

            // Fill password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
            passwordField.clear();
            passwordField.sendKeys(pwd);
            Thread.sleep(2000);

            // Click Login
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Log in']")));
            loginButton.click();
            Thread.sleep(2000); 

            // Check for logo 
            boolean loginSuccess;
            try {
                WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//img[@alt='logo']")));
                loginSuccess = logo.isDisplayed();
            } catch (TimeoutException e) {
                loginSuccess = false;
            }

            if (loginSuccess && isLastUser) {
                System.out.println(" Login successful for valid user: " + email);
                System.out.println(" Logo is displayed for valid user");
                Thread.sleep(2000);

                // Click dropdown
                WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@aria-haspopup='menu']//button[contains(@class,'rounded-full')]")));
                dropdownButton.click();
                Thread.sleep(2000);

                wait.until(ExpectedConditions.attributeToBe(
                        By.xpath("//div[@aria-haspopup='menu']"), "aria-expanded", "true"));

                // Click logout
                WebElement logoutOption = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@role='menuitem' and text()='Log out']")));
                logoutOption.click();
                System.out.println("Logged out successfully for valid user");
                Thread.sleep(2000);
            } else if (!loginSuccess) {
                System.out.println(" Login failed for: " + email);
                Thread.sleep(2000);
            } else {
                System.out.println(" Unexpected login success for: " + email);
            }

        } catch (Exception e) {
            System.out.println(" Exception during login for: " + email + " - " + e.getMessage());
            fail("Test failed for: " + email);
        }
    }

    @AfterClass
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "dp")
    Object[][] loginData() {
        Object[][] rawData = {
            {"testuser1@example.com", "Test@1234"},
            {"testuser2@example.com", "Test@1234"},
            {"user.alpha@example.com", "Test@1234"},
            {"beta.user@example.com", "Test@1234"},
            {"john.doe@example.com", "Test@1234"},
            {"jane.doe@example.com", "Test@1234"},
            {"mockuser123@example.com", "Test@1234"},
            {"random.email@example.com", "Test@1234"},
            {"automation.tester@example.com", "Test@1234"},
            {"selenium.user@example.com", "Test@1234"},
            {"final.tester@example.com", "Test@1234"},
            {"sashankpandey76@gmail.com", "Sashank@1008"} 
        };

        Object[][] dataWithFlag = new Object[rawData.length][3];
        for (int i = 0; i < rawData.length; i++) {
            dataWithFlag[i][0] = rawData[i][0];
            dataWithFlag[i][1] = rawData[i][1];
            dataWithFlag[i][2] = (i == rawData.length - 1); 
        }
        return dataWithFlag;
    }
}
