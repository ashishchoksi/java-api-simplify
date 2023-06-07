package org.example.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.example.api.model.User;
import org.example.api.request.SimpleClient;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        SimpleClient client = new SimpleClient();

        // GET request
        User[] users = client.executeGet("https://jsonplaceholder.typicode.com/posts", User[].class);
        List<User> userList = Arrays.asList(users);
        System.out.println(userList.size());

        // POST request
        User user = new User(2L, "test title", "test body");
        String res = client.executePost("https://jsonplaceholder.typicode.com/posts", mapper.writeValueAsString(user));
        System.out.println("response: " + res);
    }

    public void executeSample() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String uri = "https://jsonplaceholder.typicode.com/posts";
        HttpGet httpGet = new HttpGet(URI.create(uri));
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int code = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String responseStr = EntityUtils.toString(entity);
        ObjectMapper mapper = new ObjectMapper();
        List<User> users = mapper.readValue(responseStr, new TypeReference<List<User>>() {
        });
        // convert response to class
        System.out.println("Code : " + code + "\nRes: " + responseStr);
    }
}