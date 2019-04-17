package hse.banana.chat;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatApp extends Application {
    private static final int appWidth = 400;
    private static final int appHeight = 300;
    private static final String pathToUI = "/hse/banana/chat/UI/";

    private static Stage stage;
    private static Scene menuScene;

    /**
     * Sets menu scene
     */
    public static void backToMenu() {
        stage.setScene(menuScene);
    }

    /**
     * Main function
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Initialize an application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        menuScene = openScene("chat");

        stage.setTitle("Chat");
        stage.setMinHeight(300);
        stage.setMinWidth(310);
        stage.setHeight(appHeight);
        stage.setWidth(appWidth);
        stage.show();
    }

    private Scene openScene(String sceneName) throws Exception {
        Parent root = FXMLLoader
                .load(getClass().getResource(pathToUI + sceneName + ".fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        return scene;
    }

}
