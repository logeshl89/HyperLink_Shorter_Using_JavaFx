package com.example.demo2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LinkShorter extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Link Shortener");


        TextField longUrlTextField = new TextField();
        Button shortenButton = new Button("Shorten");
        Label shortenedLabel = new Label();
        shortenedLabel.setFont(Font.font("Arial", 14));

        shortenButton.setOnAction(e -> {
            String longUrl = longUrlTextField.getText();
            String shortenedUrl = shortenUrl(longUrl);
            shortenedLabel.setText("Shortened URL: " + shortenedUrl);
        });


        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(longUrlTextField, shortenButton, shortenedLabel);

        Scene scene = new Scene(layout, 300, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String shortenUrl(String longUrl) {
        String apiKey = "2310ed6d7550cc5f1f5241363ed2589d72af2526";
        String apiUrl = "https://api-ssl.bitly.com/v4/shorten";

        try {
            // Create connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            // Prepare payload
            String jsonPayload = "{\"long_url\":\"" + longUrl + "\"}";

            // Send request
            connection.getOutputStream().write(jsonPayload.getBytes("UTF-8"));

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();


            String shortenedUrl = response.toString().split("\"id\":\"")[1].split("\"")[0];
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            StringSelection stringSelection = new StringSelection(shortenedUrl);


            clipboard.setContents(stringSelection, null);

            return shortenedUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}

