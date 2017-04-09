package airscanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Created by Michał on 2017-01-20.
 */
public class AddWindowController implements Initializable
{
    @FXML
    private TextField surnameField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField passportField;

    @FXML
    private TextField countryField;

    @FXML
    private ComboBox sexComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    private DBHandler db;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        db = DBHandler.getInstance();
        sexComboBox.getItems().addAll("Men", "Women");
        sexComboBox.getSelectionModel().select(0);

        datePicker.setValue(LocalDate.now());
        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>()
        {
            @Override
            public DateCell call(DatePicker param)
            {
                return new DateCell()
                {
                    @Override
                    public void updateItem(LocalDate item, boolean empty)
                    {
                        super.updateItem(item, empty);

                        if(item.isAfter(LocalDate.now()))
                        {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        });
    }

    @FXML
    private void cancelAction(ActionEvent e)
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addAction(ActionEvent event)
    {
        boolean success = false;

        String passport = passportField.getText();
        try
        {
            int passportNumber = Integer.parseInt(passport);

            if(validateFields())
            {
                if (!db.checkIfPassportNumberExistInDB(passportNumber))
                {
                    String name = nameField.getText();
                    String surname = surnameField.getText();
                    String country = countryField.getText();
                    String sex = ((String)sexComboBox.getValue()).toLowerCase().equals("men") ? "M": "F";
                    Date birthdate = Date.valueOf(datePicker.getValue());
                    db.addNewUser(
                            passportNumber,
                            name,
                            surname,
                            sex,
                            birthdate,
                            country
                            );
                    success = true;
                }
                else
                {
                    showAlert("Passport number error", "Passport number already taken");
                }
            }
        }
        catch (NumberFormatException e)
        {
            showAlert("Passport number error", "Entered value is not valid passport number");
        }
        finally
        {
            if(success)
            {
                Stage stage = (Stage) addButton.getScene().getWindow();
                stage.close();
            }
        }
    }

    private void showAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateFields()
    {
        if(surnameField.getText().equals(""))
        {
            showAlert("Surname error", "Surname cannot be empty");
            return false;
        }
        if(nameField.getText().equals(""))
        {
            showAlert("Name error", "Name cannot be empty");
            return false;
        }
        if(countryField.getText().equals(""))
        {
            showAlert("Country error", "Country cannot be empty");
            return false;
        }

        return true;
    }
}
