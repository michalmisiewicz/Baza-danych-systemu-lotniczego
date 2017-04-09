package airscanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

/**
 * Created by Michał on 2016-12-25.
 */

public class UserListController implements Initializable
{
    @FXML
    private TableColumn pass_col;
    @FXML
    private TableColumn name_col;
    @FXML
    private TableColumn surname_col;
    @FXML
    private TableColumn sex_col;
    @FXML
    private TableColumn birth_col;
    @FXML
    private TableColumn country_col;
    @FXML
    private TableView userTable;
    @FXML
    private Button addUserButton;
    @FXML
    private Label userNumberLabel;
    @FXML
    private Label averageAgeLabel;

    private ObservableList<UserInfo> userList;

    private DBHandler db;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        db = DBHandler.getInstance();

        Image image = new Image(getClass().getResourceAsStream("res/add-user.png"));
        addUserButton.setGraphic(new ImageView(image));

        userTable.setEditable(true);

        pass_col.prefWidthProperty().bind(userTable.widthProperty().multiply(0.2));
        name_col.prefWidthProperty().bind(userTable.widthProperty().multiply(0.15));
        surname_col.prefWidthProperty().bind(userTable.widthProperty().multiply(0.2));
        sex_col.prefWidthProperty().bind(userTable.widthProperty().multiply(0.07));
        birth_col.prefWidthProperty().bind(userTable.widthProperty().multiply(0.15));
        country_col.prefWidthProperty().bind(userTable.widthProperty().multiply(0.2));

        pass_col.setCellValueFactory(new PropertyValueFactory<UserInfo, Integer>("passportNo"));
        name_col.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("name"));
        surname_col.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("surname"));
        sex_col.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("sex"));
        birth_col.setCellValueFactory(new PropertyValueFactory<UserInfo, Date>("birthDate"));
        country_col.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("country"));

        pass_col.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>()
        {
            @Override
            public String toString(Integer object)
            {
                return object.toString();
            }

            @Override
            public Integer fromString(String string)
            {
                return Integer.parseInt(string);
            }
        }));
        pass_col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserInfo, Integer>>()
        {
            @Override
            public void handle(TableColumn.CellEditEvent<UserInfo, Integer> e)
            {
                if(e.getNewValue() != e.getOldValue())
                {
                    if(db.updatePassportNumber(e.getOldValue(), e.getNewValue()))
                        e.getTableView().getItems().get(e.getTablePosition().getRow()).setPassportNo(e.getNewValue());
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Incorrect passport number");
                        alert.setContentText("Incorrect passport number");
                        alert.showAndWait();
                        pass_col.setVisible(false);
                        pass_col.setVisible(true);
                    }
                }

            }
        });

        name_col.setCellFactory(TextFieldTableCell.forTableColumn());
        name_col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserInfo, String>>()
        {
            @Override
            public void handle(TableColumn.CellEditEvent<UserInfo, String> e)
            {
                if(!e.getNewValue().equals(e.getOldValue()))
                {
                    db.updateName(e.getRowValue().getPassportNo(), e.getNewValue());
                    e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
                }
            }
        });

        surname_col.setCellFactory(TextFieldTableCell.forTableColumn());
        surname_col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserInfo, String>>()
        {
            @Override
            public void handle(TableColumn.CellEditEvent<UserInfo, String> e)
            {
                if(!e.getNewValue().equals(e.getOldValue()))
                {
                    db.updateSurname(e.getRowValue().getPassportNo(), e.getNewValue());
                    e.getTableView().getItems().get(e.getTablePosition().getRow()).setSurname(e.getNewValue());
                }
            }
        });

        userList = FXCollections.observableArrayList(db.getUsers());
        userTable.setItems(userList);
        updateStats();
    }

    @FXML
    private void addUser(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("add_window.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 600, 400));
            stage.showAndWait();
            userList.clear();
            userList.addAll(db.getUsers());
            updateStats();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

    private void updateStats()
    {
        userNumberLabel.setText(String.valueOf(db.getUserAmount()));
        averageAgeLabel.setText(String.format("%.2f",db.getAverageAge()));
    }

}
