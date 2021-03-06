package airscanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Air scanner");
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
        DBHandler.getInstance().closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
