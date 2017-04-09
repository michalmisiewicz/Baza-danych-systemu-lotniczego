package airscanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable
{
    @FXML
    private GridPane contentGrid;

    @FXML
    private AnchorPane temp;

    @FXML
    private Button user_bttn;

    @FXML
    private Button flight_bttn;

    private AnchorPane anchorPane = new AnchorPane();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Image image = new Image(getClass().getResourceAsStream("res/user.png"));
        user_bttn.setGraphic(new ImageView(image));
        image = new Image(getClass().getResourceAsStream("res/aeroplane.png"));
        flight_bttn.setGraphic(new ImageView(image));

        DBHandler db = DBHandler.getInstance();
        db.openConnection();

        try
        {
            anchorPane = FXMLLoader.load(getClass().getResource("userslist.fxml"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        contentGrid.getChildren().remove(temp);
        contentGrid.add(anchorPane, 1, 0);
    }

    public void usersList(ActionEvent event)
    {
        contentGrid.getChildren().remove(anchorPane);
        try
        {
            anchorPane = FXMLLoader.load(getClass().getResource("userslist.fxml"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        contentGrid.add(anchorPane, 1, 0);
    }

    public void flightList(ActionEvent event)
    {
        contentGrid.getChildren().remove(anchorPane);
        try
        {
            anchorPane = FXMLLoader.load(getClass().getResource("flightlist.fxml"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        contentGrid.add(anchorPane, 1, 0);
    }
}
