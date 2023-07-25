package com.example.client;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ClientApplication {

    public static void main(String[] args) {
        String apiUrl = "http://localhost:8080/api/v1/reflect";
        int numThreads = 500;

        ExecutorService executorService = Executors.newFixedThreadPool(2000);
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

            // Process the results
            Map<String, Integer> counter = new HashMap<>();
            for (Future<Response> future : futures) {
                try {
                    Response response = future.get(5, TimeUnit.SECONDS);
                    String applicationName = response.getApplicationName();
                    counter.put(applicationName, counter.getOrDefault(applicationName, 0) + 1);
                } catch (Exception e) {
                    System.out.println("Faild to execute: " + e.getMessage());
//                    e.printStackTrace();
                }
            }
            System.out.println(counter);
            // Wait for all threads to complete
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
            httpClient.close();
        } catch (InterruptedException | IOException e) {
            System.out.println("Outer Exception: " + e.getMessage());
//            e.printStackTrace();
        }
    }

}
