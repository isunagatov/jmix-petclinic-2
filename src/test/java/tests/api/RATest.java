package tests.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.ReadConfig;

import java.util.Base64;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class RATest {
    public static final String BASE_URL = ReadConfig.SubSystemClass.getSubSystem().getUrl();
    public static final String AUTH_ENDPOINT = "/oauth2/token";
    public static final String USERS_ENDPOINT = "/rest/entities/User";
    public static String accessToken;

    @BeforeClass
    public void setup() {
        // Настройка базовой конфигурации
        RestAssured.baseURI = BASE_URL;

        // Получение токена авторизации
        accessToken = getAccessToken();
        Assert.assertFalse(accessToken.isEmpty());
    }

    @Test
    public static void testGetAccessToken() {
        // Проверка успешного получения токена
        assertNotNull(accessToken);
        assertTrue(accessToken.length() > 0);
    }

    private String getAccessToken() {
        ReadConfig.SubSystemClass.SubSystem subs = ReadConfig.SubSystemClass.getSubSystem();

        String clientId = subs.getClient();
        String clientSecret = subs.getSecret();
        String encodedCredentials = new String(Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes()));

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Basic " + encodedCredentials))
                .header(new Header("Content-Type", "application/x-www-form-urlencoded"))
                .formParam("grant_type", "client_credentials")
                .when()
                .post(AUTH_ENDPOINT)
                .then()
                .extract()
                .response();

        // Извлечение токена из ответа
        return response.jsonPath().getString("access_token");
    }

    @Test
    public static void getUsers() throws Exception {
        SoftAssert sa = new SoftAssert();
        ResponseBody rb = getUsersArr();
        rb.jsonPath().getString("user");
        sa.assertTrue(rb.jsonPath().getString("username").contains("admin"), "пользователи в базе есть");
        sa.assertAll();
    }

    public static ResponseBody getUsersArr() throws Exception{
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + accessToken))
                .header(new Header("Content-Type", "application/json"))
                .when()
                .get(USERS_ENDPOINT)
                .then()
                .extract()
                .response();
        return response.getBody();
    }
}
