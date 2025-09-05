package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadConfig {

    public static String filename = "conf.xml";

    public static class DataBase {

        public static DB getDB(Node node) {
            DB param = new DB();
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                param.setSubSystem(getTagValue("subSystem", element));
                param.setdatabaseHost(getTagValue("databaseHost", element));
                param.setDatabaseName(getTagValue("databaseName", element));
                param.setDatabaseUser(getTagValue("databaseUser", element));
                param.setDatabasePass(getTagValue("databasePass", element));
            }
            return param;
        }

        public static DB getDbBySubSystem(String subSystemName) {
            List<DB> dbList = getListDB();
            for (DB db : dbList) {
                if (db.getSubSystem().contains(subSystemName)) {
                    return db;
                }
            }
            System.err.print("Система: " + subSystemName + " не найдена в testdata.xml User.DB");
            return null;
        }

        public static List<DB> getListDB() {
            String filepath = ReadConfig.class.getClassLoader().getResource(filename).getPath();
            File xmlFile = new File(filepath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlFile);
                document.getDocumentElement().normalize();
                NodeList nodeList = document.getElementsByTagName("DBconfig");

                List<DB> paramList = new ArrayList<DB>();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    paramList.add(getDB(nodeList.item(i)));
                }
                for (DB param : paramList) {
                    //System.out.println(param.toString());
                }
                return paramList;
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            return null;
        }

        public static class DB {
            private String subSystem;
            private String databaseHost;
            private String databaseName;
            private String databaseUser;
            private String databasePass;

            public String getSubSystem() {
                return subSystem;
            }

            public void setSubSystem(String subSystem) {
                this.subSystem = subSystem;
            }

            public String getDatabaseHost() {
                return databaseHost;
            }

            public void setdatabaseHost(String databaseHost) {
                this.databaseHost = databaseHost;
            }

            public String getDatabaseName() {
                return databaseName;
            }

            public void setDatabaseName(String databaseName) {
                this.databaseName = databaseName;
            }

            public String getDatabaseUser() {
                return databaseUser;
            }

            public void setDatabaseUser(String databaseUser) {
                this.databaseUser = databaseUser;
            }

            public String getDatabasePass() {
                return databasePass;
            }

            public void setDatabasePass(String databasePass) {
                this.databasePass = databasePass;
            }

            @Override
            public String toString() {
                return "DB: databaseHost = " + this.databaseHost + " databaseName = " + this.databaseName;
            }
        }
    }

    public static class SubSystemClass {

        public static List<SubSystem> getListSubSystem() {
            String filepath = ReadConfig.class.getClassLoader().getResource(filename).getPath();
            File xmlFile = new File(filepath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //DocumentBuilder builder;
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlFile);
                document.getDocumentElement().normalize();
                //System.out.println("Root item: " + document.getDocumentElement().getNodeName());
                NodeList nodeList = document.getElementsByTagName("SubSystem");
                List<SubSystem> paramList = new ArrayList<SubSystem>();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    paramList.add(getSubSystem(nodeList.item(i)));
                }
                for (SubSystem param : paramList) {
                    //System.out.println(param.toString());
                }
                return paramList;
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            return null;
        }

        public static SubSystem getSubSystem(Node node) {
            SubSystem param = new SubSystem();
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                param.setSubSystemName(getTagValue("subSystemName", element));
                param.setUrl(getTagValue("url", element));
                param.setClient(getTagValue("client", element));
                param.setSecret(getTagValue("secret", element));
            }
            return param;
        }

        public static class SubSystem {
            private String SubSystemName;
            private String Url;
            private String Client;
            private String Secret;

            public String getSubSystemName() {
                return SubSystemName;
            }

            public void setSubSystemName(String subSystemName) {
                SubSystemName = subSystemName;
            }

            public String getUrl() {
                return Url;
            }

            public void setUrl(String Url) {
                this.Url = Url;
            }

            public String getClient() {
                return Client;
            }

            public void setClient(String client) {
                Client = client;
            }

            public String getSecret() {
                return Secret;
            }

            public void setSecret(String secret) {
                Secret = secret;
            }

            @Override
            public String toString() {
                return "SubSystem: Url = " + this.Url;
            }
        }

        public static SubSystem getSubSystem() {
            return SubSystemClass.getListSubSystem().get(0);
        }
    }

    //TestConfiguration
    public static class TestConfigurationClass {
        public static TestConfiguration getTestConfiguration(Node node) {
            TestConfiguration param = new TestConfiguration();
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                param.setTestDataFile(getTagValue("TestDataFile", element));
                param.setCheckAndRestorePasswords(getTagValue("checkAndRestorePasswords", element));
                param.setSelenoid(getTagValue("selenoid", element));
                param.setIgnoreUiSSL(getTagValue("ignoreUiSSL", element));
                param.setIgnoreRestSSL(getTagValue("ignoreRestSSL", element));
                param.setSelenoidUrl(getTagValue("selenoidUrl", element));
            }
            return param;
        }

        public static List<TestConfiguration> getListTestConfiguration() {
            String filepath = ReadConfig.class.getClassLoader().getResource(filename).getPath();
            File xmlFile = new File(filepath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //DocumentBuilder builder;
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlFile);
                document.getDocumentElement().normalize();
                NodeList nodeList = document.getElementsByTagName("TestConfiguration");

                List<TestConfiguration> paramList = new ArrayList<TestConfiguration>();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    paramList.add(getTestConfiguration(nodeList.item(i)));
                }

                for (TestConfiguration param : paramList) {
                    //   System.out.println(param.toString());
                }
                return paramList;
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            return null;
        }

        public static class TestConfiguration {

            private String TestDataFile;
            private String selenoid;
            private String ignoreUiSSL;
            private String ignoreRestSSL;
            private String selenoidUrl;
            private String checkAndRestorePasswords;


            public String getTestDataFile() {
                return TestDataFile;
            }
            public void setTestDataFile(String testDataFile) {
                TestDataFile = testDataFile;
            }
            public String getSelenoid() {
                return selenoid;
            }
            public String getIgnoreUiSSL() {
                return ignoreUiSSL;
            }
            public void setIgnoreUiSSL(String ignoreUiSSL) {
                this.ignoreUiSSL = ignoreUiSSL;
            }
            public String getIgnoreRestSSL() {
                return ignoreRestSSL;
            }
            public void setIgnoreRestSSL(String ignoreRestSSL) {
                this.ignoreRestSSL = ignoreRestSSL;
            }
            public void setSelenoid(String selenoid) {
                this.selenoid = selenoid;
            }

            public String getSelenoidUrl() {
                return selenoidUrl;
            }

            public void setSelenoidUrl(String selenoidUrl) {
                this.selenoidUrl = selenoidUrl;
            }

            public String getCheckAndRestorePasswords() {
                return checkAndRestorePasswords;
            }

            public void setCheckAndRestorePasswords(String checkAndRestorePasswords) {
                this.checkAndRestorePasswords = checkAndRestorePasswords;
            }

            @Override
            public String toString() {
                return "TC: selenoid = " + this.selenoid;
            }
        }
        public static TestConfiguration getTestConfiguration(){
            return getListTestConfiguration().get(0);
        }
    }

    //Browser
    public static class BrowserClass {
        public static List<Browser> getListBrowser() {
            String filepath = ReadConfig.class.getClassLoader().getResource(filename).getPath();
            File xmlFile = new File(filepath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //DocumentBuilder builder;
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlFile);
                document.getDocumentElement().normalize();
                NodeList nodeList = document.getElementsByTagName("Browser");

                List<Browser> paramList = new ArrayList<Browser>();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    paramList.add(getBrowser(nodeList.item(i)));
                }

                for (Browser param : paramList) {
                    //   System.out.println(param.toString());
                }
                return paramList;
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            return null;
        }

        public static Browser getBrowser(Node node) {
            Browser param = new Browser();
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                param.setName(getTagValue("name", element));
                param.setWebDriver(getTagValue("webDriver", element));
                param.setExtension(getTagValue("extension", element));
                param.setHomeDir(getTagValue("homeDir", element));
                param.setOsName(getTagValue("OsName", element));
            }
            return param;
        }

        public static class Browser {
            private String name;
            private String OsName;
            private String webDriver;
            private String extension;
            private String homeDir;
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getWebDriver() {
                return webDriver;
            }

            public void setWebDriver(String webDriver) {
                this.webDriver = webDriver;
            }

            public String getExtension() {
                return extension;
            }

            public void setExtension(String extension) {
                this.extension = extension;
            }

            public String getHomeDir() {
                return homeDir;
            }

            public void setHomeDir(String homeDir) {
                this.homeDir = homeDir;
            }
            public String getOsName() {
                return OsName;
            }
            public void setOsName(String osName) {
                this.OsName = osName;
            }


            @Override
            public String toString() {
                return "Browser: browser = " + this.name;
            }
        }

        public static Browser getBrowserByNameByOs(String browserName, String osName) {
            List<Browser> browsers = getListBrowser();
            for (Browser browser : browsers) {
                if (browser.getName().contains(browserName) && browser.getOsName().contains(osName)) {
                    return browser;
                }
            }
            return null;
        }
        public static Browser getBrowserByName(String browserName) {
            List<Browser> browsers = getListBrowser();
            for (Browser browser : browsers) {
                if (browser.getName().contains(browserName)) {
                    return browser;
                }
            }
            return null;
        }
    }

    //KeyCloak
    public static class KeycloakClass {

        public static List<KeyCloak> getListKeyCloak() {
            String filepath = (new ReadConfig()).getClass().getClassLoader().getResource(filename).getPath();
            File xmlFile = new File(filepath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //DocumentBuilder builder;
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlFile);
                document.getDocumentElement().normalize();
                //System.out.println("Root item: " + document.getDocumentElement().getNodeName());
                NodeList nodeList = document.getElementsByTagName("KeyCloak");
                List<KeyCloak> paramList = new ArrayList<KeyCloak>();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    paramList.add(getKeyCloak(nodeList.item(i)));
                }
                for (KeyCloak param : paramList) {
                    //    System.out.println(param.toString());
                }
                return paramList;
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            return null;
        }

        public static KeyCloak getKeyCloak(Node node) {
            KeyCloak param = new KeyCloak();
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                param.setDefaultKK(getTagValue("DefaultKK", element));
                param.setNameSpace(getTagValue("NameSpace", element));
                param.setURL(getTagValue("URL", element));
                param.setUserAPI(getTagValue("UserAPI", element));
                param.setUserAPIPassword(getTagValue("UserAPIpassword", element));

                param.setClientUserApi(getTagValue("ClientUserApi", element));
                param.setClientPassApi(getTagValue("ClientPassApi", element));

                param.setRealm(getTagValue("Realm", element));
                param.setTokenPath(getTagValue("tokenPath", element));
                param.setCertTokenPath(getTagValue("CertTokenPath", element));
            }
            return param;
        }

        public static class KeyCloak {
            public String DefaultKK;
            public String NameSpace;
            public String URL;
            public String UserAPI;
            public String UserAPIPassword;
            public String ClientUserApi;
            public String ClientPassApi;

            public String Realm;
            public String TokenPath;
            public String CertTokenPath;

            public String getDefaultKK() {
                return DefaultKK;
            }

            public void setDefaultKK(String defaultKK) {
                DefaultKK = defaultKK;
            }

            public String getNameSpace() {
                return NameSpace;
            }

            public void setNameSpace(String nameSpace) {
                NameSpace = nameSpace;
            }

            public String getURL() {
                return URL;
            }

            public void setURL(String URL) {
                this.URL = URL;
            }

            public String getUserAPI() {
                return UserAPI;
            }

            public void setUserAPI(String UserAPI) {
                this.UserAPI = UserAPI;
            }

            public String getUserAPIPassword() {
                return UserAPIPassword;
            }

            public void setUserAPIPassword(String UserAPIPassword) {
                this.UserAPIPassword = UserAPIPassword;
            }

            public String getClientUserApi() {
                return ClientUserApi;
            }

            public void setClientUserApi(String userApiPersonal) {
                ClientUserApi = userApiPersonal;
            }

            public String getClientPassApi() {
                return ClientPassApi;
            }

            public void setClientPassApi(String passApiPersonal) {
                ClientPassApi = passApiPersonal;
            }

            public String getRealm() {
                return Realm;
            }

            public void setRealm(String Realm) {
                this.Realm = Realm;
            }

            public String getTokenPath() {
                return TokenPath;
            }

            public void setTokenPath(String tokenPath) {
                TokenPath = tokenPath;
            }

            public String getCertTokenPath() {
                return CertTokenPath;
            }

            public void setCertTokenPath(String certTokenPath) {
                CertTokenPath = certTokenPath;
            }

            @Override
            public String toString() {
                return "KeyCloak: URL = " + this.URL;
            }
        }

        public static KeyCloak getKeycloakByNameSpace(String Namespace) {
            List<KeyCloak> listKeycloak = KeycloakClass.getListKeyCloak();
            for (KeyCloak kl : listKeycloak) {
                if (kl.getNameSpace().contains(Namespace)) {
                    return kl;
                }
            }
            return null;
        }

        public static KeyCloak getKeycloakDefault() {
            List<KeyCloak> listKeycloak = KeycloakClass.getListKeyCloak();
            for (KeyCloak kl : listKeycloak) {
                if (kl.getDefaultKK().contains("true")) {
                    return kl;
                }
            }
            return null;
        }

    }

    public static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

}