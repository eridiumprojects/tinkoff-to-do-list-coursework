package com.example.telegram.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class RequestBuilder {
    @Value("${tg.bot.url}")
    private String URL;
    public CloseableHttpResponse postCreatingHttpResponse(CloseableHttpClient client
            , Object object, String path, String token) throws IOException {
        Charset charset = StandardCharsets.UTF_8;
        ContentType contentType = ContentType.create("application/json", charset);
        HttpPost httpPost = new HttpPost(URL + path);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(object);
        StringEntity entity = new StringEntity(json, contentType);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        if (token != null) {
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
        return client.execute(httpPost);
    }

    public CloseableHttpResponse getCreatingHttpResponse(CloseableHttpClient client
            , String path, String token) throws IOException {
        HttpGet httpGet = new HttpGet(URL + path);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return client.execute(httpGet);
    }
}
