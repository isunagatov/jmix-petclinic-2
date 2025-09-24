package tests.api;

import POJO.User;
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

import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

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
    public static void getUsers()  {
        SoftAssert sa = new SoftAssert();
        ResponseBody rb = getUsersArr();
        String respJsonAsString = rb.asString();
        User[] userFromJson;
        try {
            ObjectMapper om = new ObjectMapper();

            userFromJson = om.readValue(respJsonAsString, User[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<User> userAdmin = Arrays.stream(userFromJson).filter(x -> x.getUserName().equals("admin")).toList();
        Assert.assertTrue(userAdmin.size()==1, "пользователя admin в базе нет");

        sa.assertAll();
    }

    public static ResponseBody getUsersArr(){
        try {
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
        }catch (Exception e){
            Assert.fail(e.getMessage());
            return null;
        }
    }
}
