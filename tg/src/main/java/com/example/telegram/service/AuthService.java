package com.example.telegram.service;

import com.example.telegram.model.dto.request.LoginRequest;
import com.example.telegram.model.dto.response.JwtResponse;
import com.example.telegram.util.RequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class AuthService {
    private int statusCode;
    private final RequestBuilder requestBuilder;

    public JwtResponse jwtFromJsonString(String jsonString) throws JsonProcessingException {
        return new ObjectMapper().
                readValue(jsonString, JwtResponse.class);
    }

    public String sendSignInRequest(LoginRequest user) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try (httpclient; CloseableHttpResponse response =
                requestBuilder.postCreatingHttpResponse
                        (httpclient, user, "/auth/signin", null)) {
            setStatusCode(response.getStatusLine().getStatusCode());
            return new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
                    .lines()
                    .collect(Collectors.joining());
        }
    }
}
