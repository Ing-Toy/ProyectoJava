package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow extends Application {

    public static MainWindow app;
    private static Stage stageWindow;

    @Override
    public void start(Stage stage) throws IOException {
        app = this;
        stageWindow = stage;
        setScene("/client/InitialPage.fxml");
        stage.setTitle("Team 4 blackjack");
        stage.show();
    }

    public void setScene(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load());
            stageWindow.setScene(scene);
            stageWindow.setResizable(false);
            stageWindow.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}