package com.example.telegram.service;

import com.example.telegram.model.dto.request.LoginRequest;
import com.example.telegram.model.dto.response.JwtResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class AuthService {
    private int statusCode;

    public JwtResponse jwtFromJsonString(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JwtResponse response = mapper.readValue(jsonString, JwtResponse.class);
        return response;
    }

    public String sendSignInRequest(LoginRequest user) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/auth/signin");
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(user);

            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);
                EntityUtils.consume(responseEntity);
                setStatusCode(response.getStatusLine().getStatusCode());
                return responseString;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
