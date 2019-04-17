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
    private ButtonType buttonTypeServer = new ButtonType("Server");
    private ButtonType buttonTypeClient = new ButtonType("Client");

    enum ChoseType {
        NONE, CLIENT, SERVER
    }

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

        ChoseType type = ChoseType.NONE;
        while (type == ChoseType.NONE) {
            Optional<ButtonType> result = createServerClientDialog();
            if (result.isPresent()) {
                if (result.get() == buttonTypeClient) {
                    type = ChoseType.CLIENT;
                } else {
                    type = ChoseType.SERVER;
                }
            } else {
                System.exit(0);
            }
        }

        if (type == ChoseType.CLIENT) {
            startClient();
        } else {
            startServer();
        }
    }

    private void startServer() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Waiting for client");
        alert.setHeaderText(null);
        alert.setContentText("");
        boolean success = waitingConnect();
        alert.showAndWait();
    }

    private void startClient() {
        boolean success = false;
        while (!success) {
            Optional<String> result = createClientDialog();
            if (!result.isPresent()) {
                System.exit(0);
            }
            success = !result.get().isEmpty();
            if (success) {
                success = connectToHost(result.get());
                if (!success) {
                    createAlertDialog("can't connect");
                }
            } else {
                createAlertDialog("empty name");
            }
        }
    }

    private boolean connectToHost(String host) {
        return false;
    }

    private boolean waitingConnect() {
        return false;
    }

    private Optional<String> createClientDialog() {
        TextInputDialog dialog = new TextInputDialog("localhost");
        dialog.setTitle("Choose server");
        dialog.setHeaderText("");
        dialog.setContentText("Please enter your server:");
        return dialog.showAndWait();
    }

    private Optional<String> createLoginDialog() {
        TextInputDialog dialog = new TextInputDialog("petuh");
        dialog.setTitle("Login");
        dialog.setHeaderText("");
        dialog.setContentText("Please enter your login:");
        return dialog.showAndWait();
    }

    private Optional<ButtonType> createServerClientDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog with Custom Actions");
        alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
        alert.setContentText("Choose your option.");

        alert.getButtonTypes().setAll(buttonTypeServer, buttonTypeClient);
        return alert.showAndWait();
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
            System.out.println(text);
        }
    }

    public void addMessage(String message) {
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
