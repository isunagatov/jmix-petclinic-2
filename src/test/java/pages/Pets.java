package pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import tests.TestBase;

import java.util.Objects;
import java.util.UUID;

import static com.codeborne.selenide.Selenide.$;

public class Pets extends TestBase {
    public static SelenideElement createBtn = $("#createBtn");
    public static SelenideElement nameField = $("#nameField input");

    public static SelenideElement birthDate = $("#birthdateField input");

    public static SelenideElement owner = $("#ownerField input");
    public static SelenideElement type = $("#typeField input");

    public static SelenideElement identificNumber = $("#identificationNumberField");
    public static SelenideElement okButton = $("#saveAndCloseBtn");
    public class filterBox{
        public static SelenideElement identificNumberFilter = $("#identificationNumberFilter_valueComponent").$("input");
        public static SelenideElement refresh = $("//*[@id=\"content-vaadin-details-115\"]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-menu-bar[1]/vaadin-menu-bar-button[1]");
    }
    public class TablePets{
        public static SelenideElement editBtn = $("#editBtn");
        public static SelenideElement table = $("#petsDataGrid");
        public static SelenideElement firstRow = $("#items > tr:nth-child(1)");
        public static void selectRowByText(String pet){
            table.$(Selectors.byText(pet)).click();
        }
    }

    public static class PetObject {
        private UUID id;
        private String name;
        private String identificNumber;
        private String birthDateWeb;
        private String birthDateDB;
        private String owner;
        private String type;

        public UUID getId() {
            return id;
        }
        public void setId(UUID id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getIdentificNumber() {
            return identificNumber;
        }
        public void setIdentificNumber(String identificNumber) {
            this.identificNumber = identificNumber;
        }
        public String getBirthDateWeb() {
            return birthDateWeb;
        }
        public void setBirthDateWeb(String birthDate) {
            this.birthDateWeb = birthDate;
        }
        public String getBirthDateDB() {
            return birthDateDB;
        }
        public void setBirthDateDB(String birthDateDB) {
            this.birthDateDB = birthDateDB;
        }
        public String getOwner() {
            return owner;
        }
        public void setOwner(String owner) {
            this.owner = owner;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public void setUniqNewName(){
            String newId = "New name of Pets" + UUID.randomUUID().toString().substring(0,5);
            setName(newId);
        }
        public void setUniqIdentific(){
            String newId = "id" + UUID.randomUUID().toString().substring(0,8);
            setIdentificNumber(newId);
        }

        @Override
        public String toString() {
            return "PetObject{" +
                    "name='" + name + '\'' +
                    ", identificNumber='" + identificNumber + '\'' +
                    ", birthDate=" + birthDateWeb +
                    ", owner='" + owner + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            PetObject petObject = (PetObject) o;
            return Objects.equals(name, petObject.name) && Objects.equals(identificNumber, petObject.identificNumber) && Objects.equals(birthDateWeb, petObject.birthDateWeb) && Objects.equals(owner, petObject.owner) && Objects.equals(type, petObject.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, identificNumber, birthDateWeb, owner, type);
        }
    }
    public static PetObject newPetObject (){
        PetObject newPet = new PetObject();
        newPet.setUniqNewName();
        String birthdateWEB = utils.DateTimeUtils.getCurrentDate(-50, "dd/MM/yyyy");
        String birthdateDB = utils.DateTimeUtils.getCurrentDate(-50, "yyyy-MM-dd");
        newPet.setBirthDateWeb(birthdateWEB);
        newPet.setBirthDateDB(birthdateDB);
        newPet.setUniqIdentific();
        return newPet;
    }
    @Step("Заполнение атрибутов Pet при создании Pet")
    public static void createNewPetWeb(PetObject newPet){
        Pets.nameField.click();
        Pets.nameField.sendKeys(newPet.getName());
        Pets.birthDate.click();
        Pets.birthDate.sendKeys(newPet.getBirthDateWeb());
        Pets.birthDate.sendKeys(Keys.ENTER);
        Pets.identificNumber.sendKeys(newPet.getIdentificNumber());
        Pets.okButton.click();
    }
    @Step("Задать фильтры")
    public static void setFilter(String identNumber){
        filterBox.identificNumberFilter.click();
        filterBox.identificNumberFilter.sendKeys(identNumber);
        filterBox.identificNumberFilter.sendKeys(Keys.ENTER);
    }

    @Step("Получение значений Pet в виде объекта с web формы ")
    public static PetObject getPetDataFromWeb(){
        PetObject PetOb = new PetObject();
        Pets.nameField.click();
        PetOb.setName(Pets.nameField.getValue());
        PetOb.setBirthDateWeb(Pets.birthDate.getValue());
        PetOb.setIdentificNumber(Pets.identificNumber.getValue());
        PetOb.setOwner(Pets.owner.getValue());
        PetOb.setType(Pets.type.getValue());
        return PetOb;
    }
}
