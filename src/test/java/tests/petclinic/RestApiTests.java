package tests.petclinic;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.RestApiJMix;
import utils.TestData;

import java.util.UUID;

public class RestApiTests {
    @Test(description = "Получение токена доступа api")
    public static void getTokenJ() throws Exception {
        String token = RestApiJMix.getTokenJMix();
        Assert.assertTrue(token.length()>5, "Токен получен");
    }
    @Test(description = "Получение пользователя")
    public static void getUsers() throws Exception {
        SoftAssert sa = new SoftAssert();
        JSONArray jsa = RestApiJMix.getUsers();
        sa.assertTrue(jsa.length()>1, "Получено пользователей больше 1го: " + jsa.length() + ".");
        JSONObject jso1 =  RestApiJMix.getObjectFromArray(0, jsa);
        System.out.println("First user: " + jso1.get("username"));
        sa.assertAll();
    }
    @Test(description = "Создание пользователя")
    public static void createUser() throws Exception {
        SoftAssert sa = new SoftAssert();
        TestData.UserClass.User user = new TestData.UserClass.User();
        String suffix = UUID.randomUUID().toString().substring(0,5);
        user.setUserLogin("testUser_" + suffix);
        user.setUserName("TestCreateUserAt"+suffix);
        user.setEmail("test" + suffix + "@company.ru");

        JSONObject jso = RestApiJMix.newUser(user);
        sa.assertTrue(jso.get("id").toString().length()>5, "Получен 1 результат");
        sa.assertAll();
    }
    @Test(description = "Поиск пользователя")
    public static void searchUser() throws Exception {
        TestData.UserClass.User user = TestData.UserClass.getUserByRole("Petclinic", "SystemAdmin");
        JSONArray result = RestApiJMix.searchUser(user,"username", user.getUserLogin());
        int len = result.length();
        if(len==1){
            JSONObject jso = new JSONObject(result.get(0).toString());
            Assert.assertTrue(jso.get("id").toString().isEmpty()==false, "Пользователь найден: " + jso.get("id").toString());
        }else {
            Assert.fail( "Результатов или больше или меньше 1: " + len + " .");
        }
    }


    @Test(groups = "negative", description = "Негативный. Поиск пользователя")
    public static void searchUserNegative() throws Exception {
        TestData.UserClass.User user = TestData.UserClass.getUserByRole("Petclinic", "SystemAdmin");
        JSONArray result = RestApiJMix.searchUser(user,"username", "no_login");
        int len = result.length();
        if(len==1){
            JSONObject jso = new JSONObject(result.get(0).toString());
            Assert.assertTrue(jso.get("id").toString().isEmpty()==false, "Пользователь найден: " + jso.get("id").toString());
        }else {
            Assert.fail( "Результатов или больше или меньше 1: " + len + " .");
        }
    }

}
