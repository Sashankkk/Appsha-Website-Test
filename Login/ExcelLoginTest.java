package Login;

import static org.testng.Assert.fail;

import java.io.FileInputStream;
import java.time.Duration;

import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

public class ExcelLoginTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test(dataProvider = "excelData")
    void testLogin(String email, String password, boolean isValid) throws InterruptedException {
        driver.get("https://staging.appsha.com/login");
        System.out.println("\n=== Testing Login for: " + email + " ===");

        try {
            driver.findElement(By.name("email")).clear();
            driver.findElement(By.name("email")).sendKeys(email);
            if (isValid) Thread.sleep(2000); // only for valid user

            driver.findElement(By.name("password")).clear();
            driver.findElement(By.name("password")).sendKeys(password);
            if (isValid) Thread.sleep(2000);

            driver.findElement(By.xpath("//button[normalize-space()='Log in']")).click();
            if (isValid) Thread.sleep(2000);

            boolean loginSuccess;
            try {
                WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//img[@alt='logo']")));
                loginSuccess = logo.isDisplayed();
            } catch (TimeoutException e) {
                loginSuccess = false;
            }

            if (loginSuccess && isValid) {
                System.out.println("Login successful for valid user: " + email);
                System.out.println("Logo is displayed for valid user");

                WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@aria-haspopup='menu']//button[contains(@class,'rounded-full')]")));
                dropdownButton.click();
                Thread.sleep(2000);

                wait.until(ExpectedConditions.attributeToBe(
                        By.xpath("//div[@aria-haspopup='menu']"), "aria-expanded", "true"));

                WebElement logoutOption = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@role='menuitem' and text()='Log out']")));
                logoutOption.click();

                System.out.println("Logged out successfully for valid user");
                Thread.sleep(2000);

            } else if (!loginSuccess && !isValid) {
                System.out.println("Login failed (as expected) for: " + email);
            } else {
                System.out.println(" Unexpected result for: " + email);
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

    @DataProvider(name = "excelData")
    Object[][] getDataFromExcel() throws Exception {
        FileInputStream fis = new FileInputStream("src/test/resources/login_data.xlsx");
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int rowCount = sheet.getPhysicalNumberOfRows();
        Object[][] data = new Object[rowCount - 1][3]; 

        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            String email = row.getCell(0).getStringCellValue();
            String password = row.getCell(1).getStringCellValue();
            boolean valid = row.getCell(2).getBooleanCellValue();

            data[i - 1][0] = email;
            data[i - 1][1] = password;
            data[i - 1][2] = valid;
        }

        workbook.close();
        fis.close();
        return data;
    }
}
