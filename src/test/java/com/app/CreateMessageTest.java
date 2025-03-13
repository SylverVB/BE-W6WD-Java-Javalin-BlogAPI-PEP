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

public class CreateMessageTest {
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
     * Sending an http request to POST localhost:8080/messages with valid message credentials
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON representation of message object
     */
    @Test
    public void createMessageSuccessful() throws IOException, InterruptedException {
        HttpRequest postMessageRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages"))
                .POST(HttpRequest.BodyPublishers.ofString("{"+
                        "\"posted_by\":1, " +
                        "\"message_text\": \"hello message\", " +
                        "\"time_posted_epoch\": 1669947792}"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postMessageRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);        

        ObjectMapper om = new ObjectMapper();
        Message expectedResult = new Message(2, 1, "hello message", 1669947792);
        System.out.println(response.body().toString());
        Message actualResult = om.readValue(response.body().toString(), Message.class);
        Assertions.assertEquals(expectedResult, actualResult);
    }
}