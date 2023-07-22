package org.example;

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
        int numThreads = 1000; // Number of threads to use

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future<Response>> futures = new ArrayList<>();

        try {
            for (int i = 0; i < numThreads; i++) {
                Callable<Response> task = new HttpPostTask(apiUrl, "{\"game\": \"Mobile Legends\"}");
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
                    System.out.println("Response: " + response);
                    String applicationName = response.getApplicationName();
                    counter.put(applicationName, counter.getOrDefault(applicationName, 0) + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(counter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}