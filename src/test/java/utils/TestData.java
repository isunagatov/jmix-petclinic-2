package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tests.TestBase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class TestData extends TestBase {
    public static String filename = ReadConfig.TestConfigurationClass.getTestConfiguration().getTestDataFile();

    //Users
    public static class UserClass {

        public static User getUserByRole(String SubSystem, String Role) {
            List<User> userList = getListUser();
            for (User user : userList) {
                if (user.getSubSystem().contains(SubSystem) && user.getUserRole().contains(Role)) {
                    return user;
                }
            }
            System.err.print("Пользователь с ролью: " + Role + " не найден в testdata.xml User.UserRole");
            return null;
        }

        public static User getUser(Node node) {
            User param = new User();
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                param.setSubSystem(getTagValue("subSystem", element));
                param.setUserRole(getTagValue("UserRole", element));
                param.setUserName(getTagValue("UserName", element));
                param.setUserLogin(getTagValue("UserLogin", element));
                param.setUserPassword(getTagValue("UserPassword", element));
                param.setGroupKK(getTagValue("GroupKK", element));
                param.setEmail(getTagValue("UserEmail", element));
            }
            return param;
        }

        public static List<User> getListUser() {
            String filepath = TestData.class.getClassLoader().getResource(TestData.filename).getPath();
            File xmlFile = new File(filepath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //DocumentBuilder builder;
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlFile);
                document.getDocumentElement().normalize();
                //System.out.println("Root item: " + document.getDocumentElement().getNodeName());
                NodeList nodeList = document.getElementsByTagName("User");
                List<User> paramList = new ArrayList<User>();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    paramList.add(getUser(nodeList.item(i)));
                }
                for (User param : paramList) {
                    //System.out.println(param.toString());
                }
                return paramList;
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            return null;
        }

        public static class User {
            private String SubSystem;
            private String UserRole;
            private String UserName;
            private String UserLogin;
            private String UserPassword;
            private String GroupKK;
            private String UserEmail;

            public String getSubSystem() {
                return SubSystem;
            }

            public void setSubSystem(String subSystem) {
                SubSystem = subSystem;
            }

            public String getUserRole() {
                return UserRole;
            }

            public void setUserRole(String UserRole) {
                this.UserRole = UserRole;
            }

            public String getUserName() {
                return UserName;
            }

            public void setUserName(String UserName) {
                this.UserName = UserName;
            }

            public String getUserLogin() {
                return UserLogin;
            }

            public void setUserLogin(String UserLogin) {
                this.UserLogin = UserLogin;
            }

            public String getUserPassword() {
                return UserPassword;
            }

            public void setUserPassword(String UserPassword) {
                this.UserPassword = UserPassword;
            }


            public void setGroupKK(String groupKK) {
                this.GroupKK = groupKK;
            }

            public String getGroupKK() {
                return GroupKK;
            }

            public String getEmail() {
                return UserEmail;
            }

            public void setEmail(String UserEmail) {
                this.UserEmail = UserEmail;
            }

            @Override
            public String toString() {
                return "User: UserName = " + this.UserName + " UserLogin = " + this.UserLogin;
            }
        }
    }

    //DocumentBuilder builder;
    // get value elements by tag
    public static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

}
