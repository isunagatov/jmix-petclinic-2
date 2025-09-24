package utils;

import POJO.User;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tests.TestBase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TestData extends TestBase {
    public static String filename = ReadConfig.TestConfigurationClass.getTestConfiguration().getTestDataFile();

    //Users
    //###########

        @Deprecated
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

        public static User getUserByRoleStream(String SubSystem, String Role) {
            List<User> userList = getListUser();
            assert userList != null;
            List<User> usList = userList.stream().filter(u -> u.getUserRole().contains(Role)).collect(Collectors.toList());
            if(usList.size()>0){
                return  usList.getFirst();
            }else {
                System.err.print("Пользователь с ролью: " + Role + " не найден в testdata.xml User.UserRole");
                return  null;
            }
        }
        public interface UserService {
            // Создание нового пользователя
            User createUser(User user);

            // Получение пользователя по роли в существующем конфиге
            User getUserByRoleConfig(String subSystem, String role);

            User getUserByUserLoginDB(User user);

            User getUserByUserLoginRest(User user);

            // Обновление информации о пользователе
            void updateUser(User user);

            // Удаление пользователя
            void deleteUser(long id);
        }
        // Конкретная реализация интерфейса
        public class DefaultUserService implements UserService {
            //private static long nextId = 1;
            //private Map<Long, User> users = new HashMap<>();

            @Override
            public User createUser(User user) {
                //Rest
                try {
                    JSONObject jso = RestApiJMix.newUser(user);
                }catch (Exception e){
                    System.err.println(e.toString());
                }

                return user; //TODO предварительный вариант
            }
            @Override
            public User getUserByUserLoginDB(User user){
                try {
                    DbHelper.UsersCheck.getUserByUserLogin(user);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return  user;
            }

            @Override
            public User getUserByRoleConfig(String subSystem, String role) {
                return getUserByRoleStream(subSystem,role);
            }
            @Override
            public User getUserByUserLoginRest(User user) {
                try {
                    RestApiJMix.searchUser(user, "username", user.getUserLogin());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                return user;  //TODO предварительный вариант
            }

            @Override
            public void updateUser(User user) {
                //users.put(user.getId(), user);
            }

            @Override
            public void deleteUser(long id) {
                //users.remove(id);
            }
        }

        //######
        public User getUser(Node node) {
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
                    paramList.add(new TestData().getUser(nodeList.item(i)));
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


    //} //Users class

    //DocumentBuilder builder;
    // get value elements by tag
    public static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

}
