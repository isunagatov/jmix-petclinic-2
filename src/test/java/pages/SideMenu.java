package pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import tests.TestBase;

import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;

public class SideMenu extends TestBase {

    public static SelenideElement PetsMenu = $(Selectors.byText("Pets"));
    public static SelenideElement logoutButton = $("#logoutButton");

    @Step
    public static void openPets(){
        step("Раскрыть меню Документы");
        SideMenu.PetsMenu.click();
    }

}
