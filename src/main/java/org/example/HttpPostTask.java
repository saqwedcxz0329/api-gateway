package org.example;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

class HttpPostTask implements Callable<Response> {
    private String url;
    private String postData;

    public HttpPostTask(String url, String postData) {
        this.url = url;
        this.postData = postData;
    }

    @Override
    public Response call() throws Exception {
        return sendPostRequest();
    }

    private Response sendPostRequest() throws IOException {
        StringBuilder responseBody = new StringBuilder();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(this.url);

            // Set request headers
            httpPost.setHeader("Content-Type", "application/json");

            // Set POST data
            StringEntity stringEntity = new StringEntity(this.postData, StandardCharsets.UTF_8);
            httpPost.setEntity(stringEntity);

            // Execute the POST request
            try (CloseableHttpResponse response = httpClient.execute(httpPost);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {

                if (response.getStatusLine().getStatusCode() == 200) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBody.append(line);
                    }
                    Header firstHeader = response.getFirstHeader("X-Application-Name");
                    return new Response(firstHeader.getValue(), responseBody.toString());
                } else {
                    System.err.println(
                            "Failed to get response. HTTP error code: " + response.getStatusLine().getStatusCode());
                    throw new RuntimeException(
                            "Failed to get response. HTTP error code: " + response.getStatusLine().getStatusCode());
                }
            }
        }
    }
}