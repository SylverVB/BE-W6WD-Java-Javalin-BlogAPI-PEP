package com.app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.app.Controller.SocialMediaController;
import com.app.Model.Message;
import com.app.Util.ConnectionUtil;
import io.javalin.Javalin;

public class RetrieveAllMessagesTest {
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
     * Sending an http request to GET localhost:8080/messages 
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON representation of a list of message objects
     */
    @Test
    public void getAllMessagesMessagesAvailable() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages"))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assertions.assertEquals(200, status);

        List<Message> expectedResult = new ArrayList<>();
        expectedResult.add(new Message(1, 1, "test message 1", 1669947792));
        List<Message> actualResult = objectMapper.readValue(response.body().toString(), new TypeReference<List<Message>>(){});
        Assertions.assertEquals(expectedResult, actualResult);
    }

    /**
     * Sending an http request to GET localhost:8080/messages with no messages in db
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON representation of an empty list
     */
    @Test
    public void getAllMessagesNoMessages() throws IOException, InterruptedException {
        removeInitialMessage();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages"))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assertions.assertEquals(200, status);

        List<Message> messages = objectMapper.readValue(response.body().toString(), new TypeReference<List<Message>>(){});
        Assertions.assertTrue(messages.isEmpty());
    }

    private void removeInitialMessage() {
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("delete from message where message_id = ?");
            ps.setInt(1, 1);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}