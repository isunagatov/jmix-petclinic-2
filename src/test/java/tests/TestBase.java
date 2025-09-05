package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;
import utils.ReadConfig;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.AssertionMode.SOFT;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;


public class TestBase {
    public static WebDriverWait wait;

    @BeforeTest
    public void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @BeforeMethod
    public void startDriver() throws IOException {
        //checkSelenoidAvailability();
        //step("Инициализация WebDriver, открытие окна браузера с адресом стенда");
        Configuration.assertionMode = SOFT;
        if(ReadConfig.TestConfigurationClass.getListTestConfiguration().get(0).getSelenoid().contains("true")) {
            Configuration.remote = System.setProperty("selenide.remoteUrl", ReadConfig.TestConfigurationClass.getTestConfiguration().getSelenoidUrl());
            Configuration.browser = System.getProperty("selenide.browser", "chrome");
            Configuration.timeout = Integer.parseInt(System.getProperty("selenide.timeout", "10000"));
            SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
        }
    }
    private void checkSelenoidAvailability() {
        HttpClient client = HttpClient.newHttpClient();

        try {
            URI uri = new URI("http://192.168.1.59:4444/wd/hub");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Selenoid не доступен! Код ответа: " + response.statusCode());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException("Ошибка при проверке Selenoid: " + e.getMessage());
        }
    }
    public static WebDriver openUrl(String testName) throws MalformedURLException {
        SoftAssert sa = new SoftAssert();
        step("Инициализация WebDriver, открытие окна браузера с адресом стенда");
        Configuration.assertionMode = SOFT;
        List<ReadConfig.SubSystemClass.SubSystem> listSubSystem = ReadConfig.SubSystemClass.getListSubSystem();
        String url = listSubSystem.get(0).getUrl();

        ChromeOptions options = new ChromeOptions();

        if (ReadConfig.TestConfigurationClass.getTestConfiguration().getIgnoreUiSSL().contains("true")) {
            options.addArguments("--ignore-certificate-errors");
        }
        if(ReadConfig.TestConfigurationClass.getListTestConfiguration().getFirst().getSelenoid().contains("true")) {
            Configuration.remote = System.setProperty("selenide.remoteUrl", ReadConfig.TestConfigurationClass.getTestConfiguration().getSelenoidUrl());
            Configuration.remote = System.getProperty("selenide.remoteUrl");
            Configuration.timeout = Integer.parseInt(System.getProperty("selenide.timeout", "10000"));

            SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));

            // Создание карты для настроек Selenoid
            Map<String, Object> selenoidOptions = new HashMap<>();
            selenoidOptions.put("name", testName);
            selenoidOptions.put("videoName", testName.replaceAll("[^\\p{L}\\w._-]", "") + "_" + utils.DateTimeUtils.getCurrentDate(0, "ddMMyyyy") + "_"
                    + utils.DateTimeUtils.getCurrentTime(0,0) + ".mp4");
            selenoidOptions.put("enableVNC", true);
            selenoidOptions.put("enableLog", true);

            // Настройки видеозаписи
            //String videoName = "testVideo";
            Map<String, Object> videoOptions = new HashMap<>();
            videoOptions.put("enableVideo", true);
            //videoOptions.put("videoName", videoName + ".mp4");

            // Объединяем все настройки в одну карту
            Map<String, Object> allOptions = new HashMap<>();
            allOptions.putAll(selenoidOptions);
            allOptions.putAll(videoOptions);

            // Устанавливаем capabilities через setCapability
            options.setCapability("selenoid:options", allOptions);

            String remUrlStr = ReadConfig.TestConfigurationClass.getListTestConfiguration().get(0).getSelenoidUrl();
            // URL вашего Selenoid/Grid
            URL urlRem = new URL(remUrlStr);
            WebDriver driver = new RemoteWebDriver(urlRem, options);

            WebDriverRunner.setWebDriver(driver);


        } else {
            WebDriver driver = new ChromeDriver(options);
            System.out.println("StartDriver. Open URL. handle " + driver.getWindowHandle());
            WebDriverRunner.setWebDriver(driver);
        }
        try {
            Selenide.open(url);
        }
        catch(WebDriverException e){
             System.out.println("Ошибка при загрузке страницы: " + e.getMessage());
        }

        WebDriver driver = getWebDriver();
        driver.manage().window().setSize(new Dimension(1900,1000));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        Selenide.sleep(500);
        System.out.println("Current url:: " + driver.getCurrentUrl());
        sa.assertFalse($$(Selectors.byText("Whitelabel Error Page")).size()>0, "Ошибка Whitelabel Error Page");
        sa.assertAll();
        return driver;
    }

    @Attachment(value = "Page screenshot", type = "image/png", fileExtension = "png")
    @Step
    public byte[] stepWithPngFile() throws IOException {
        return Files.readAllBytes(Paths.get("allure/reports/tests", ".0.png"));
    }

    @AfterMethod
    public void closeWeb(){
        Selenide.sleep(500);
        System.out.println("closeWeb ");
        try{
            getWebDriver().quit();
        }
        catch (IllegalStateException e){}
        step("Closing webdriver");
    }
    @AfterTest
    public void tearDown() {
        SelenideLogger.removeListener("allure");
    }

}
