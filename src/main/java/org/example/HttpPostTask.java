package org.example;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.Callable;

class HttpPostTask implements Callable<Response> {
    private final CloseableHttpClient httpClient;
    private final String url;
    private final String postData;

    public HttpPostTask(CloseableHttpClient httpClient, String url, String postData) {
        this.httpClient = httpClient;
        this.url = url;
        this.postData = postData;
    }

    @Override
    public Response call() throws Exception {
        return sendPostRequest();
    }

    private Response sendPostRequest() throws IOException {
        HttpPost post = new HttpPost(this.url);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(this.postData));

        try (CloseableHttpResponse response = httpClient.execute(post);) {

            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                Header firstHeader = response.getFirstHeader("X-Application-Name");
                return new Response(firstHeader.getValue(), result);
            } else {
                System.err.println(
                        "Failed to get response. HTTP error code: " + response.getStatusLine().getStatusCode());
                throw new RuntimeException(
                        "Failed to get response. HTTP error code: " + response.getStatusLine().getStatusCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}