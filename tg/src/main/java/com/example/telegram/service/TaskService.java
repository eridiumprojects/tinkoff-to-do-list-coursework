package com.example.telegram.service;

import com.example.telegram.model.dto.request.TaskRequest;
import com.example.telegram.model.dto.response.JwtResponse;
import com.example.telegram.model.enums.EMessage;
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
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

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
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity, charset);
                EntityUtils.consume(responseEntity);
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
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String inputLine;
        StringBuilder responseBody = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            responseBody.append(inputLine);
        }
        in.close();
        return responseBody.toString();
    }

    public String tasksFromJsonString(String jsonString, long chatId) {
        ArrayList<TaskRequest> tasks = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String data = jsonObject.getString("data");
            TaskRequest taskRequest = new TaskRequest();
            taskRequest.setData(data);
            tasks.add(taskRequest);
        }
        int i = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (TaskRequest taskRequest : tasks) {
            stringBuilder.append(i + ". " + taskRequest.getData() + "\n");
            i++;
        }
        return stringBuilder.toString();
    }
}
