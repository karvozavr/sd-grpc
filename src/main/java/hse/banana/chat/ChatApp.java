package hse.banana.chat;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.util.Optional;

public class ChatApp extends Application {
    private static final int appWidth = 400;
    private static final int appHeight = 300;
    private static final String pathToUI = "/hse/banana/chat/UI/";

    private static Stage stage;
    private static Scene menuScene;

    @FXML
    public VBox chatBox;

    @FXML
    TextField textField;

    private String login;
    private AbstractMessenger messenger;

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

    @FXML
    public void initialize() {
        boolean success = false;
        while (!success) {
            Optional<String> result = createLoginDialog();
            success = result.isPresent() && !result.get().isEmpty();
            if (success) {
                login = result.get();
            } else {
                createAlertDialog("empty name");
            }
        }

        success = false;
        String channel = "";
        while (!success) {
            Optional<String> result = createChannelDialog();
            success = result.isPresent() && !result.get().isEmpty();
            if (success) {
                channel = result.get();
            } else {
                createAlertDialog("empty name");
            }
        }

        messenger = new RabbitMessenger(channel, "192.168.43.231");
        messenger.subcribe(this::addMessage);
    }

    private Optional<String> createLoginDialog() {
        TextInputDialog dialog = new TextInputDialog("petuh");
        dialog.setTitle("Login");
        dialog.setHeaderText("");
        dialog.setContentText("Please enter your login:");
        return dialog.showAndWait();
    }

    private Optional<String> createChannelDialog() {
        TextInputDialog dialog = new TextInputDialog("blood");
        dialog.setTitle("Choose channel");
        dialog.setHeaderText("");
        dialog.setContentText("Enter channel name:");
        return dialog.showAndWait();
    }

    private void createAlertDialog(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("");
        alert.setContentText(error);
        alert.showAndWait();
    }

    @FXML
    public void onSendButtonClick() {
        String text = textField.getText();
        if (!text.isEmpty()) {
            messenger.sendMessage(login, text);
        }
        textField.deleteText(0, text.length());
    }

    private void addMessage(String message) {
        chatBox.getChildren().add(new Label(message));
    }

    private Scene openScene(String sceneName) throws Exception {
        Parent root = FXMLLoader
                .load(getClass().getResource(pathToUI + sceneName + ".fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        return scene;
    }

}
