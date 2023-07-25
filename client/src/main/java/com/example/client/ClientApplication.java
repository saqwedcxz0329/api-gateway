package com.example.client;

import lombok.SneakyThrows;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

public class ClientApplication {

    public static void main(String[] args) throws IOException {
        try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
            String apiUrl = "http://localhost:8080/api/v1/reflection";
            httpclient.start();

            // Set the number of concurrent calls
            int totalCalls = 6000;
            CountDownLatch latch = new CountDownLatch(totalCalls);
            ConcurrentMap<String, Integer> counter = new ConcurrentHashMap<>();

            for (int i = 0; i < totalCalls; i++) {
                // Create an HttpPost request with the URL
                HttpPost httpPost = new HttpPost(apiUrl);
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setEntity(new StringEntity("{\"game\": \"Mobile Legends\"}"));

                // Set request configuration (optional)
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
                httpPost.setConfig(requestConfig);

                httpclient.execute(httpPost, new FutureCallback<HttpResponse>() {
                    @SneakyThrows
                    @Override
                    public void completed(HttpResponse response) {
                        // Handle the response here
                        if (response.getStatusLine().getStatusCode() == 200) {
                            Header firstHeader = response.getFirstHeader("X-Application-Name");
                            String applicationName = firstHeader.getValue();
                            counter.compute(applicationName, (key, value) -> (value == null) ? 1 : value + 1);
                        } else {
                            System.out.println("Failed to get response. HTTP error code: " + response.getStatusLine().getStatusCode());
                            throw new RuntimeException("Failed to get response. HTTP error code: " + response.getStatusLine().getStatusCode());
                        }
                        latch.countDown(); // Decrease the latch count when the request completes
                    }

                    @Override
                    public void failed(Exception ex) {
                        // Handle request failure here
                        System.out.println("Failed to send request. Msg: " + ex.getMessage());
                        latch.countDown(); // Decrease the latch count when the request completes
                    }

                    @Override
                    public void cancelled() {
                        // Handle request cancellation here
                        System.out.println("Request is cancelled");
                        latch.countDown(); // Decrease the latch count when the request completes
                    }
                });
            }

            // Wait for all requests to complete
            latch.await();
            System.out.println(counter);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
