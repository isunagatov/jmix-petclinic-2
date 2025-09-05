package tests.petclinic;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.LoginPage;
import pages.Pets;
import pages.SideMenu;
import tests.TestBase;
import utils.DbHelper;

import java.net.MalformedURLException;
import java.sql.SQLException;

import static io.qameta.allure.Allure.step;
import static pages.Pets.createNewPetWeb;

public class PetsTest extends TestBase {
    @DataProvider(name = "pets")
    public static Pets.PetObject[] pets(){
        int total = 2;
        Pets.PetObject[] petObjects = new Pets.PetObject[total];
        for(int i=0; i<total; i++) {
            petObjects[i] = Pets.newPetObject();
        }
        return petObjects;
    }
    @Test(description = "Создание Pets с проверкой в базе и через web.")
    public void createPetsAndCheckWeb() throws SQLException, MalformedURLException {
        SoftAssert saNg = new SoftAssert();
        String testName = "Создание Pets с проверкой в базе и через web.";
        TestBase.openUrl(testName);
        LoginPage.login("user");
        step("Открыть меню Pets");

        SideMenu.PetsMenu.click();
        step("Нажать Create");
        Pets.createBtn.click();

        Pets.PetObject newPet = Pets.newPetObject();
        createNewPetWeb(newPet);

        Pets.setFilter(newPet.getIdentificNumber());
        Pets.TablePets.selectRowByText(newPet.getIdentificNumber());
        Pets.TablePets.editBtn.click();
        Pets.PetObject petFromWeb = Pets.getPetDataFromWeb();

        saNg.assertTrue(petFromWeb.getIdentificNumber().isEmpty()==false, "В Web присутствует строка с таким identNumber");

        saNg.assertTrue(petFromWeb.getName().contains(newPet.getName()), "В Web присутствует строка с таким Name");
        saNg.assertTrue(petFromWeb.getBirthDateWeb().contains(newPet.getBirthDateWeb()), "В Web присутствует строка с таким BirthDate");

        step("Выйти из системы");
        SideMenu.logoutButton.click();
        saNg.assertAll();
    }
    @Test(description = "Создание Pets с проверкой в базе.")
    public void createPetsAndCheckDB() throws SQLException, MalformedURLException {
        SoftAssert saNg = new SoftAssert();
        String testName = "Базовый тест. Создание Pets с проверкой в базе.";
        TestBase.openUrl(testName);
        LoginPage.login("user");
        step("Открыть меню Pets");
        SideMenu.PetsMenu.click();
        step("Нажать Create");
        Pets.createBtn.click();

        Pets.PetObject newPet = Pets.newPetObject();
        createNewPetWeb(newPet);

        saNg.assertTrue(DbHelper.PetsCheck.getPetIdByIdentificationNumber(newPet).isEmpty()==false, "В DB присутствует строка с таким identNumber");

        Pets.PetObject PetDB = DbHelper.PetsCheck.getPetByIdentificationNumber(newPet);
        saNg.assertTrue(PetDB.getName().contains(newPet.getName()), "В DB присутствует строка с таким Name");
        saNg.assertTrue(PetDB.getBirthDateWeb().contains(newPet.getBirthDateDB()), "В DB присутствует строка с таким BirthDate");

        step("Выйти из системы");
        SideMenu.logoutButton.click();
        saNg.assertAll();
    }
    @Test(dataProvider = "pets", description = "Создание Pets с проверкой в базе. DataProvider")
    public void createPetsAndCheckDbWithDataProvider(Pets.PetObject newPet) throws SQLException, MalformedURLException {
        SoftAssert saNg = new SoftAssert();
        String testName = "Создание Pets с проверкой в базе.";
        TestBase.openUrl(testName + newPet.getName());
        LoginPage.login("user");
        step("Открыть меню Pets");
        SideMenu.PetsMenu.click();
        step("Нажать Create");
        Pets.createBtn.click();

        createNewPetWeb(newPet);

        saNg.assertTrue(DbHelper.PetsCheck.getPetIdByIdentificationNumber(newPet).isEmpty()==false, "В DB присутствует строка с таким identNumber");

        Pets.PetObject PetDB = DbHelper.PetsCheck.getPetByIdentificationNumber(newPet);
        saNg.assertTrue(PetDB.getName().contains(newPet.getName()), "В DB присутствует строка с таким Name");
        saNg.assertTrue(PetDB.getBirthDateWeb().contains(newPet.getBirthDateDB()), "В DB присутствует строка с таким BirthDate");

        step("Выйти из системы");
        SideMenu.logoutButton.click();
        saNg.assertAll();
    }

}
