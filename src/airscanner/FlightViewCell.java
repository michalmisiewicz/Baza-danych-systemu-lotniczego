package airscanner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by Michał on 2016-12-26.
 */
public class FlightViewCell extends ListCell<FlightInfo>
{
    @FXML
    private GridPane gridPane;

    @FXML
    private Label dep_pl;

    @FXML
    private Label dep_date;

    @FXML
    private Label dep_time;

    @FXML
    private Label arr_pl;

    @FXML
    private Label arr_date;

    @FXML
    private Label arr_time;

    @FXML
    private Label duration;

    @FXML
    private Label airlines;

    @FXML
    private ImageView dep_img;

    @FXML
    private ImageView arr_img;

    @FXML
    private ImageView arrow;

    @Override
    protected void updateItem(FlightInfo item, boolean empty)
    {
        super.updateItem(item, empty);

        if(empty || item == null)
        {
            setText(null);
            setGraphic(null);
        }
        else
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("flightrow.fxml"));
            loader.setController(this);

            try
            {
                loader.load();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            Image image = new Image(getClass().getResourceAsStream("res/departures.png"));
            dep_img.setImage(image);
            image = new Image(getClass().getResourceAsStream("res/arrival.png"));
            arr_img.setImage(image);
            image = new Image(getClass().getResourceAsStream("res/arrow.png"));
            arrow.setImage(image);

            dep_pl.setText(item.source);
            dep_time.setText(new SimpleDateFormat("HH:mm").format(item.departureTime));
            dep_date.setText(new SimpleDateFormat("dd-MM-yyyy").format(item.departureTime));
            arr_pl.setText(item.destination);
            arr_time.setText(new SimpleDateFormat("HH:mm").format(item.arrivalTime));
            arr_date.setText(new SimpleDateFormat("dd-MM-yyyy").format(item.arrivalTime));
            airlines.setText(item.airlines);
            duration.setText("duration: " + item.duration);

            setGraphic(gridPane);
            setText(null);
        }
    }
}
