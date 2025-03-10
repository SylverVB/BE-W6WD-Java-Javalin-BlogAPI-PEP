package com.app;

import com.app.Controller.SocialMediaController;
import io.javalin.Javalin;

/**
 * This class is provided with a main method to allow us to manually run and test our application.
 */
public class Main {
    public static void main(String[] args) {
        SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        app.start(8080);
    }
}