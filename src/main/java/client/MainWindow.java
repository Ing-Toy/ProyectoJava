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
        setScene("/client/NamePage.fxml");
        stage.setTitle("Team 4 blackjack");
        stage.show();
    }

    public void setScene(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load(), 600, 400);
            stageWindow.setScene(scene);
            stageWindow.setResizable(false);

            if (path.contains("OnlineGameView.fxml") || path.contains("LocalMultiplayer") || path.contains("OneGamePage")) {
                stageWindow.setMaximized(true);
            } else {
                stageWindow.setMaximized(false); // asegurarte que otras no lo estén
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T setSceneWithController(String path){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load(), 600, 400);
            stageWindow.setScene(scene);
            stageWindow.setResizable(false);

            if (path.contains("OnlineGameView.fxml") || path.contains("LocalMultiplayer") || path.contains("OneGamePage")) {
                stageWindow.setMaximized(true);
            } else {
                stageWindow.setMaximized(false); // asegurarte que otras no lo estén
            }

            return loader.getController();
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}