package com.example.telegram.service;

import com.example.telegram.model.dto.request.TaskRequest;
import com.example.telegram.model.dto.response.JwtResponse;
import com.example.telegram.util.RequestBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final RequestBuilder requestBuilder;

    public void sendCreateTaskRequest(String token, TaskRequest taskRequest) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try (httpclient; CloseableHttpResponse response =
                requestBuilder.postCreatingHttpResponse
                        (httpclient, taskRequest, "/task/create", token)) {
            EntityUtils.consume(response.getEntity());
        }
    }

    public String sendShowTasksRequest(JwtResponse jwtResponse) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try (httpclient; CloseableHttpResponse response =
                requestBuilder.getCreatingHttpResponse(httpclient, "/task/list",
                        jwtResponse.getAccessToken())) {
            return new BufferedReader(new InputStreamReader
                    (response.getEntity().getContent()))
                    .lines()
                    .collect(Collectors.joining());
        }
    }

    public String tasksFromJsonString(String jsonString) {
        JSONArray jsonArray = new JSONArray(jsonString);
        List<String> tasks = IntStream.range(0, jsonArray.length())
                .mapToObj(i -> jsonArray.getJSONObject(i).getString("data")).toList();
        return IntStream.range(0, tasks.size())
                .mapToObj(i -> (i + 1) + ". " + tasks.get(i) + "\n")
                .collect(Collectors.joining());
    }
}
