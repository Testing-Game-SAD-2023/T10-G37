package com.example;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.Alert;

public class EditorTest {
    private static ChromeDriver driver;
    private static int timeout = 60;

    @BeforeClass
    public static void setDriver() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\didom\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
    }

    @Before
    public void openBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", "C:\\Users\\didom\\Downloads");
        options.setExperimentalOption("prefs", chromePrefs);

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);

        driver.get("http://localhost/login");
        driver.findElement(By.id("email")).sendKeys("test@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Testpass0");

        // Ora esegui l'istruzione desiderata
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        String urlPaginaDiRedirezione = "http://localhost/main";
        try {
            wait.until(ExpectedConditions.urlToBe(urlPaginaDiRedirezione));
        } catch (TimeoutException e) {
            Assert.fail();
        }
    }

    @After
    public void closeBrowser() {
        driver.close();
    }

    @Test
    public void download() throws InterruptedException {
        driver.findElement(By.id("0")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("downloadButton")).click();

        File f = new File("C:\\Users\\didom\\Downloads\\class.java");

        Thread.sleep(5000);

        Assert.assertTrue(f.exists());
    }

    public void moveToReport(String urlPaginaDiRedirezione) {
        driver.findElement(By.id("0")).click();
        driver.findElement(By.xpath("//*[@id='0-randoop lvl 1']")).click();
        driver.findElementsByCssSelector(".div_buttons_main > * button").get(1).click();

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            wait.until(ExpectedConditions.urlToBe(urlPaginaDiRedirezione));
        } catch (TimeoutException e) {
            Assert.fail();
        }
    }

    @Test
    public void selection() {
        String urlPaginaDiRedirezione = "http://localhost/report";

        moveToReport(urlPaginaDiRedirezione);

        Assert.assertEquals("Test fallito! La selezione non è avvenuta correttamente.",
            driver.getCurrentUrl(),
                urlPaginaDiRedirezione);
    }

    public void moveToEditor(String urlPaginaDiRedirezione) {
        moveToReport("http://localhost/report");

        driver.findElementsByCssSelector(".div_buttons > * button").get(1).click();

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            wait.until(ExpectedConditions.urlToBe(urlPaginaDiRedirezione));
        } catch (TimeoutException e) {
            Assert.fail();
        }
    }

    @Test
    public void startGame() {
        String urlPaginaDiRedirezione = "http://localhost/editor";
        moveToEditor(urlPaginaDiRedirezione);

        Assert.assertEquals("Test fallito! L'avvio della partita non è avvenuto correttamente.", 
            driver.getCurrentUrl(),
                urlPaginaDiRedirezione);
    }

    @Test
    public void logout() {
    
    WebDriverWait wait = new WebDriverWait(driver, timeout);

    driver.findElement(By.id("logoutButton")).click();

    try {
        wait.until(ExpectedConditions.urlToBe("http://localhost/login"));
    } catch (TimeoutException e) {
        Assert.fail();
    }
}



    @Test
    public void compile() {
        String urlPaginaDiRedirezione = "http://localhost/editor";
        moveToEditor(urlPaginaDiRedirezione);

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    By.cssSelector("#sidebar-textarea + div > * div.CodeMirror-code > *"), 1));
        } catch (TimeoutException e) {
            Assert.fail();
        }

        driver.findElement(By.id("compileButton")).click();

        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    By.cssSelector("#console-textarea + div > * div.CodeMirror-code > *"), 1));
        } catch (TimeoutException e) {
            Assert.fail();
        }
    }


    @Test
    public void logoutEditor() {
        String urlPaginaDiRedirezione = "http://localhost/editor";
        moveToEditor(urlPaginaDiRedirezione);

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    By.cssSelector("#sidebar-textarea + div > * div.CodeMirror-code > *"), 1));
        } catch (TimeoutException e) {
            Assert.fail();
        }

        driver.findElement(By.id("logoutButton")).click();

        try {
            wait.until(ExpectedConditions.urlToBe("http://localhost/login"));
        } catch (TimeoutException e) {
            Assert.fail();
        }
    }


    @Test
    public void runUserTest() {
        String urlPaginaDiRedirezione = "http://localhost/editor";
        moveToEditor(urlPaginaDiRedirezione);

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    By.cssSelector("#sidebar-textarea + div > * div.CodeMirror-code > *"), 1));
        } catch (TimeoutException e) {
            Assert.fail("Element not found within the specified timeout.");
        }

        driver.findElement(By.id("coverageButton")).click();

        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();

        String alertText = alert.getText();
        System.out.println(alertText);    
        alert.accept();

        if (alertText.equals("Risultato copertura Test utente : 0 %")) {
            System.out.println("Esito positivo: Copertura test utente è al 0%.");
        } else {
            Assert.fail("Esito negativo: Copertura test utente non è al 0%.");
        }
    }


    @Test
    public void run() {
        String urlPaginaDiRedirezione = "http://localhost/editor";
        moveToEditor(urlPaginaDiRedirezione);

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    By.cssSelector("#sidebar-textarea + div > * div.CodeMirror-code > *"), 1));
        } catch (TimeoutException e) {
            Assert.fail();
        }

        driver.findElement(By.id("runButton")).click();

        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    By.cssSelector("#console-textarea2 + div > * div.CodeMirror-code > *"), 1));
        } catch (TimeoutException e) {
            Assert.fail();
        }
    }


    @Test
    public void highlightJacoco() {
        String urlPaginaDiRedirezione = "http://localhost/editor";
        moveToEditor(urlPaginaDiRedirezione);

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#sidebar-textarea + div > * div.CodeMirror-code > *"), 1));
        } catch(TimeoutException e) {
            Assert.fail();
        }

        driver.findElement(By.id("jacocoButton")).click();

        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#sidebar-textarea + div > * div.CodeMirror-code > * .uncovered-line"), 0));
        } catch(TimeoutException e) {
            Assert.fail();
        }
    }

}
