package tests.petclinic;

import com.codeborne.selenide.Selenide;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import tests.TestBase;
import utils.TestData;

import java.net.MalformedURLException;
import java.util.List;

import static utils.TestData.UserClass.getListUser;

public class LoginTest extends TestBase {
    @Test(enabled = true, description = "Авторизация по Логину/паролю.")
    public void login() throws MalformedURLException {
        String testName = "Авторизация по Логину/паролю.";
        TestBase.openUrl(testName);
        LoginPage.login("user");
        Selenide.sleep(1000);
        LoginPage.logoutButton.click();
        Selenide.sleep(1000);
    }
    @Test(dataProvider = "users", description = "Авторизация по Логину/паролю. DataProvider")
    public void loginDP(TestData.UserClass.User user) throws MalformedURLException {
        String testName = "Базовый тест. Авторизация по Логину/паролю.";
        TestBase.openUrl(testName + user.getUserLogin());
        LoginPage.login(user);
        Selenide.sleep(1000);
        LoginPage.logOut();
        Selenide.sleep(1000);
    }
    @DataProvider(name = "users")
    public static TestData.UserClass.User[] usersDataProvider(){
        List<TestData.UserClass.User> arUser = getListUser();
        TestData.UserClass.User[] objUser = new TestData.UserClass.User[arUser.size()];
        for(int i = 0; i<arUser.size(); i++){
            objUser[i] = arUser.get(i);
        }
        return objUser;
    }

    @Test(enabled = true, groups = "negative", description = "Негативный тест. Авторизация по Логину/паролю.")
    public void loginNegative() throws MalformedURLException {
        String testName = "Негативный тест. Авторизация по Логину/паролю.";
        TestBase.openUrl(testName);
        TestData.UserClass.User user = new TestData.UserClass.User();
        user.setUserLogin("user1");
        user.setUserPassword("password");
        user.setUserName("user1");
        LoginPage.login(user);
        Selenide.sleep(1000);
        LoginPage.logoutButton.click();
        Selenide.sleep(1000);
    }
}
