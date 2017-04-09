package airscanner;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

/**
 * Created by Michał on 2016-12-25.
 */
public class UserInfo
{
    private SimpleIntegerProperty passportNo;
    private SimpleStringProperty name = null;
    private SimpleStringProperty surname = null;
    private SimpleStringProperty sex;
    private SimpleObjectProperty<Date> birthDate = null;

    public int getPassportNo()
    {
        return passportNo.get();
    }

    public SimpleIntegerProperty passportNoProperty()
    {
        return passportNo;
    }

    public void setPassportNo(int passportNo)
    {
        this.passportNo.set(passportNo);
    }

    public String getName()
    {
        return name.get();
    }

    public SimpleStringProperty nameProperty()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name.set(name);
    }

    public String getSurname()
    {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname.set(surname);
    }

    public String getSex()
    {
        return sex.get();
    }

    public SimpleStringProperty sexProperty()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex.set(sex);
    }

    public Date getBirthDate()
    {
        return birthDate.get();
    }

    public SimpleObjectProperty<Date> birthDateProperty()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate.set(birthDate);
    }

    public String getCountry()
    {
        return country.get();
    }

    public SimpleStringProperty countryProperty()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country.set(country);
    }

    private SimpleStringProperty country = null;

    public UserInfo(int pass, String name, String surname, String sex, Date date, String country)
    {
        this.passportNo = new SimpleIntegerProperty(pass);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.sex = new SimpleStringProperty(sex);
        this.birthDate = new SimpleObjectProperty<Date>(date);
        this.country = new SimpleStringProperty(country);
    }
}
