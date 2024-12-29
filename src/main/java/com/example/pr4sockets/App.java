package com.example.pr4sockets;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Scene scene;
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("principal"), 1300, 800);
        stage.setTitle("Gestión de usuarios y proyectos - Principal");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml, String title) throws IOException {
        scene.setRoot(loadFXML(fxml));

        Stage stage = (Stage) scene.getWindow();
        stage.sizeToScene();
        stage.setTitle(title);
    }

    private static Parent loadFXML(String file) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(file + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}