package utils;

import io.qameta.allure.Step;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Base64;

public class RestApiJMix {
    @Step("getToken. Получение токена")
    public static String getTokenJMix() throws Exception {
        ReadConfig.SubSystemClass.SubSystem system = ReadConfig.SubSystemClass.getSubSystem();
        String url = system.getUrl() +  "/oauth2" + "/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String encoding = Base64.getEncoder().encodeToString((system.getClient() + ":" + system.getSecret()).getBytes());
        headers.set("Authorization", "Basic " + encoding);
        headers.set("Content-type", "application/x-www-form-urlencoded");
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        HttpEntity requestEntity = new HttpEntity<>(body, headers);

        String serverUrl = url;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
        System.out.println("Response code: " + response.getStatusCode());

        String jsonStr = response.getBody();
        String tokenStr = "";
        try {
            JSONObject json = new JSONObject(jsonStr);
            tokenStr = (String) json.get("access_token");
            Assert.assertTrue(tokenStr.isEmpty()==false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tokenStr;
    }
    @Step("getUsers. Получение списка пользователей")
    public static JSONArray getUsers() throws Exception{
        ReadConfig.SubSystemClass.SubSystem system = ReadConfig.SubSystemClass.getSubSystem();
        String source1 = system.getUrl() + "/rest/entities/User";
        HttpClient client;
        client = HttpClients.createDefault();

        HttpGet get = new HttpGet(source1);
        get.setHeader("Authorization", "Bearer " + getTokenJMix());
        get.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(get);
        System.out.println("\nSending 'Get' request to URL /users: " + source1);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result);
        String ResultRunID = result.toString();
        JSONArray jsa = new JSONArray(ResultRunID);

        return jsa;
    }
    public static JSONObject getObjectFromArray(int index, JSONArray array){
        JSONObject jso = new JSONObject();
        if(array.length()>0) {
            JSONObject jso1;
            jso1 = array.getJSONObject(index);
            return jso1;
        }else return jso;
    }
    @Step("Create User. Создание пользователя")
    public static JSONObject newUser(TestData.UserClass.User user) throws Exception{
        ReadConfig.SubSystemClass.SubSystem system = ReadConfig.SubSystemClass.getSubSystem();
        String source1 = system.getUrl() + "/rest/entities/User";
        HttpClient client;

            client = HttpClients.createDefault();

        HttpPost post = new HttpPost(source1);
        post.setHeader("Authorization", "Bearer " + RestApiJMix.getTokenJMix());
        post.setHeader("Content-type", "application/json");
        JSONObject jsonBody = new JSONObject();

        jsonBody.put("_entityName", "User");
        jsonBody.put("_instanceName", user.getUserName());
        jsonBody.put("username", user.getUserName());
        jsonBody.put("active", "true");
        jsonBody.put("firstName", "_auto");
        jsonBody.put("lastName", "test " + user.getUserName());
        jsonBody.put("password", "test " + "{noop}admin");

        StringEntity strEnt = new StringEntity(jsonBody.toString());
        post.setEntity(strEnt);
        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'Post' request to URL /users: " + source1);
        Assert.assertEquals(response.getStatusLine().getStatusCode(),201, "Статуc верный 201");
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result);
        String ResultRunID = result.toString();
        JSONObject jso = new JSONObject(ResultRunID);
        return jso;
    }
    @Step("Search User Поиск пользователя")
    public static JSONArray searchUser(TestData.UserClass.User user, String propertyName, String searchValue) throws Exception{
        ReadConfig.SubSystemClass.SubSystem system = ReadConfig.SubSystemClass.getSubSystem();
        String source1 = system.getUrl() + "/rest/entities/User/search";
        HttpClient client;

            client = HttpClients.createDefault();

        HttpPost post = new HttpPost(source1);
        post.setHeader("Authorization", "Bearer " + RestApiJMix.getTokenJMix());
        post.setHeader("Content-type", "application/json");

        JSONObject jsonBody = new JSONObject();
        JSONObject jsoFilter = new JSONObject();
        JSONArray jsaConditions = new JSONArray();

        JSONObject jsoConditions = new JSONObject();
        jsoConditions.put("property", propertyName);
        jsoConditions.put("operator", "=");
        jsoConditions.put("value", searchValue);

        jsaConditions.put(jsoConditions);
        jsoFilter.put("conditions", jsaConditions);
        jsonBody.put("filter", jsoFilter);

        StringEntity strEnt = new StringEntity(jsonBody.toString());
        post.setEntity(strEnt);
        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'Post' request to URL /users: " + source1);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result);
        JSONObject jso = new JSONObject(result);
        String ResultRunID = result.toString();
        return new JSONArray(ResultRunID);
    }
}
