package com.example.telegram.service;

import com.example.telegram.model.dto.request.TaskRequest;
import com.example.telegram.model.dto.response.JwtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TaskService {
    public void sendCreateTaskRequest(String token, TaskRequest taskRequest) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        Charset charset = Charset.forName("utf-8");
        ContentType contentType = ContentType.create("application/json", charset);
        try {
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/task/create");
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(taskRequest);
            StringEntity entity = new StringEntity(json, contentType);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpPost.setHeader("Authorization", "Bearer " + token);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                EntityUtils.consume(response.getEntity());
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpclient.close();
        }
    }

    public String sendShowTasksRequest(JwtResponse jwtResponse) throws IOException {
        String token = jwtResponse.getAccessToken();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8080/api/task/list");
        httpGet.setHeader("Authorization", "Bearer " + token);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
                .lines()
                .collect(Collectors.joining());
    }

    public String tasksFromJsonString(String jsonString, long chatId) {
        JSONArray jsonArray = new JSONArray(jsonString);
        List<String> tasks = IntStream.range(0, jsonArray.length())
                .mapToObj(i -> jsonArray.getJSONObject(i).getString("data"))
                .collect(Collectors.toList());
        return IntStream.range(0, tasks.size())
                .mapToObj(i -> (i + 1) + ". " + tasks.get(i) + "\n")
                .collect(Collectors.joining());
    }
}
