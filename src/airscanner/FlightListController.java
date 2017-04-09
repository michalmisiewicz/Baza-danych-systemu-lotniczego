package airscanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Michał on 2016-12-26.
 */
public class FlightListController implements Initializable
{
    @FXML
    private ListView listView;

    @FXML
    private Button removeButton;

    private ObservableList<FlightInfo> flightList;

    private DBHandler db;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Image image = new Image(getClass().getResourceAsStream("res/remove.png"));
        removeButton.setGraphic(new ImageView(image));

        db = DBHandler.getInstance();

        flightList = FXCollections.observableArrayList(db.getFlights());
        listView.setItems(flightList);
        listView.setCellFactory(e -> new FlightViewCell());
    }

    @FXML
    private void removeAction(ActionEvent e)
    {
        int row = listView.getSelectionModel().getSelectedIndex();

        if(row != -1)
        {
            FlightInfo flight = flightList.get(row);
            int newSelectedId = (row == listView.getItems().size() - 1) ? row - 1 : row;

            if(db.removeFlight(flight.id))
            {
                flightList.remove(row);
                listView.getSelectionModel().select(newSelectedId);
            }
        }
    }
}
