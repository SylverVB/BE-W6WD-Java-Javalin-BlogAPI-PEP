package com.app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.app.Controller.SocialMediaController;
import com.app.Model.Message;
import com.app.Util.ConnectionUtil;
import io.javalin.Javalin;

public class DeleteMessageByMessageIdTest {
    SocialMediaController socialMediaController;
    HttpClient webClient;
    ObjectMapper objectMapper;
    Javalin app;

    /**
     * Before every test, resetting the database, restarting the Javalin app, and creating a new webClient and ObjectMapper
     * for interacting locally on the web.
     * @throws InterruptedException
     */
    @BeforeEach
    public void setUp() throws InterruptedException {
        ConnectionUtil.resetTestDatabase();
        socialMediaController = new SocialMediaController();
        app = socialMediaController.startAPI();
        webClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        app.start(8080);
        Thread.sleep(1000);
    }

    @AfterEach
    public void tearDown() {
        app.stop();
    }

    /**
     * Sending an http request to DELETE localhost:8080/messages/1 (message exists)
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON representation of the message that was deleted
     */
    @Test
    public void deleteMessageGivenMessageIdMessageFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages/1"))
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assertions.assertEquals(200, status);

        Message expectedResult = new Message(1, 1, "test message 1", 1669947792);
        Message actualResult = objectMapper.readValue(response.body().toString(), Message.class);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    /**
     * Sending an http request to DELETE localhost:8080/messages/100 (message does NOT exists)
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: 
     */
    @Test
    public void deleteMessageGivenMessageIdMessageNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages/100"))
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assertions.assertEquals(200, status);
        Assertions.assertTrue(response.body().toString().isEmpty());
    }
}