package org.example;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String apiUrl = "http://localhost:8080/api/v1/reflect";
        int numThreads = 500;

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future<Response>> futures = new ArrayList<>();

        try {
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
            connManager.setDefaultMaxPerRoute(numThreads);
            connManager.setMaxTotal(numThreads);
            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();
            for (int i = 0; i < numThreads; i++) {
                Callable<Response> task = new HttpPostTask(httpClient, apiUrl, "{\"game\": \"Mobile Legends\"}");
                Future<Response> future = executorService.submit(task);
                futures.add(future);
            }

            // Wait for all threads to complete
            executorService.shutdown();
            executorService.awaitTermination(60, TimeUnit.SECONDS);

            // Process the results
            Map<String, Integer> counter = new HashMap<>();
            for (Future<Response> future : futures) {
                try {
                    Response response = future.get();
                    String applicationName = response.getApplicationName();
                    counter.put(applicationName, counter.getOrDefault(applicationName, 0) + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(counter);
            httpClient.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}