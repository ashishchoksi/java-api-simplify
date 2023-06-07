package org.example.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

public class SimpleClient {
    private final CloseableHttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public SimpleClient() {
        /* Multiple ways to create HttpClient
            this.httpClient = new DefaultHttpClient();
            this.httpClient = HttpClients.createDefault();
         */
        this.httpClient = HttpClientBuilder.create().build();

        // close the client
        Runtime.getRuntime().addShutdownHook(new Thread(() -> closeClient(this.httpClient)));
    }

    private void closeClient(CloseableHttpClient httpClient) {
        try {
            httpClient.close();
        } catch (IOException e) {}
    }

    public <T> T executeGet(String url, Class<T> target) throws IOException {
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(request);
        T res = mapper.readValue(EntityUtils.toByteArray(response.getEntity()), target);

        // This will consume the entity and clear buffer
        EntityUtils.consume(response.getEntity());
        return res;
    }

    public String executePost(String url, String requestBody) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setEntity(new StringEntity(requestBody));
        CloseableHttpResponse response = httpClient.execute(request);
        String responseStr = EntityUtils.toString(response.getEntity());

        // clear the buffer
        EntityUtils.consume(response.getEntity());
        return responseStr;
    }
}
