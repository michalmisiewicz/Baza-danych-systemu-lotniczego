package airscanner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Michał on 2016-12-25.
 */
public class DBHandler
{
    private final static DBHandler instance = new DBHandler();

    private String username;
    private String password;
    private String Url;
    private Connection connection;

    private DBHandler()
    {
        Properties p = new Properties();
        try
        {
            FileInputStream is = new FileInputStream("airscanner.properties");
            p.load(is);
            Url = p.getProperty("url");
            username = p.getProperty("username");
            password = p.getProperty("password");
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static DBHandler getInstance() { return instance; }

    public void saveProperties()
    {
        try
        {
            Properties p = new Properties();
            p.setProperty("url", Url);
            p.setProperty("username", username);
            p.setProperty("password", password);
            FileOutputStream os = new FileOutputStream("airscanner.properties");
            p.store(os, "---No Comment---");
            os.close();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void openConnection()
    {
        try
        {
            connection = DriverManager.getConnection(Url, username, password);
            connection.setAutoCommit(false);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void closeConnection()
    {
        if(connection != null)
        {
            try
            {
                connection.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            finally
            {
                connection = null;
            }
        }
    }

    public List<UserInfo> getUsers()
    {
        String sql = "SELECT * FROM PERSON";

        List<UserInfo> list = new ArrayList<>();

        if(connection != null)
        {
            try
            {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet result = statement.executeQuery(sql);

                result.beforeFirst();

                while (result.next())
                {
                    UserInfo user = new UserInfo(
                            result.getInt(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getDate(5),
                            result.getString(6)
                            );
                    list.add(user);
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<FlightInfo> getFlights()
    {
        String sql = "SELECT f.id, a2.NAME, a1.NAME, a.NAME, to_char(extract(hour from f.ARRIVAL_TIME - f.DEPARTURE_TIME) || ':' || extract(minute from f.ARRIVAL_TIME - f.DEPARTURE_TIME)), f.ARRIVAL_TIME, f.DEPARTURE_TIME\n" +
                "from FLIGHT f, AIRPORT a1, AIRPORT a2, AIRLINES a, PILOT p\n" +
                "where f.DESTINATION=a1.ID and f.SOURCE=a2.ID and f.PILOT = p.LICENCE_NUMBER and p.AIRLINES_ID = a.ID";

        List<FlightInfo> list = new ArrayList<>();

        if(connection != null)
        {
            try
            {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet result = statement.executeQuery(sql);

                result.beforeFirst();

                while (result.next())
                {
                    FlightInfo flight = new FlightInfo(
                            result.getInt(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getString(5),
                            result.getTimestamp(6),
                            result.getTimestamp(7)
                    );
                    list.add(flight);
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return list;
    }

    public boolean updatePassportNumber(int oldNumber, int newNumber)
    {
        if (connection != null)
        {
            try
            {
                PreparedStatement stm = connection.prepareStatement("SELECT COUNT(*) FROM PERSON WHERE PASSPORT_NUMBER = ?",
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                connection.setAutoCommit(false);

                stm.setInt(1, oldNumber);
                stm.execute();
                ResultSet result = stm.getResultSet();
                result.absolute(1);

                if (result.getInt(1) == 1)
                {
                    stm.setInt(1, newNumber);
                    stm.execute();
                    result = stm.getResultSet();
                    result.absolute(1);

                    if (result.getInt(1) == 0)
                    {
                        stm = connection.prepareStatement("UPDATE PERSON SET PASSPORT_NUMBER = ? WHERE PASSPORT_NUMBER = ?");
                        stm.setInt(1, newNumber);
                        stm.setInt(2, oldNumber);
                        stm.executeUpdate();
                        connection.commit();
                        return true;
                    } else return false;
                } else return false;
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return false;
    }

    public boolean updateName(int passportNumber, String newName)
    {
        if (connection != null)
        {
            try
            {
                PreparedStatement stm = connection.prepareStatement("UPDATE PERSON SET FIRST_NAME = ? WHERE PASSPORT_NUMBER = ?");
                stm.setString(1, newName);
                stm.setInt(2, passportNumber);
                stm.executeUpdate();
                connection.commit();
                return true;
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return false;
    }

    public boolean updateSurname(int passportNumber, String newName)
    {
        if (connection != null)
        {
            try
            {
                PreparedStatement stm = connection.prepareStatement("UPDATE PERSON SET LAST_NAME = ? WHERE PASSPORT_NUMBER = ?");
                stm.setString(1, newName);
                stm.setInt(2, passportNumber);
                stm.executeUpdate();
                connection.commit();
                return true;
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return false;
    }

    public boolean removeFlight(int flightNumber)
    {
        if(connection != null)
        {
            try
            {
                Statement stm = connection.createStatement();
                stm.executeUpdate("DELETE FROM FLIGHT WHERE ID = " + flightNumber);
                connection.commit();
                return true;
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

    public boolean checkIfPassportNumberExistInDB(int passportNumber)
    {
        if (connection != null)
        {
            try
            {
                PreparedStatement stm = connection.prepareStatement("SELECT COUNT(*) FROM PERSON WHERE PASSPORT_NUMBER = ?",
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                connection.setAutoCommit(false);

                stm.setInt(1, passportNumber);
                stm.execute();
                ResultSet result = stm.getResultSet();
                result.absolute(1);

                if (result.getInt(1) == 1)  return true;
                else return false;
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return true;
    }

    public boolean addNewUser(int passportNumber, String name, String surname, String sex, Date birthday,  String country)
    {
        if(connection != null)
        {
            try
            {
                PreparedStatement stm = connection.prepareStatement(
                        "insert into PERSON (PASSPORT_NUMBER, FIRST_NAME, LAST_NAME, SEX, COUNTRY, BIRTH_DATE) " +
                                "values (?, ?, ?, ?, ?, ?)");
                stm.setInt(1, passportNumber);
                stm.setString(2, name);
                stm.setString(3, surname);
                stm.setString(4, sex);
                stm.setString(5, country);
                stm.setDate(6, birthday);

                stm.executeUpdate();
                connection.commit();

                return true;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int getUserAmount()
    {
        int amount = 0;
        if (connection != null)
        {
            try
            {
                String query = "SELECT COUNT(*) FROM PERSON";
                Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                stm.execute(query);
                ResultSet result = stm.getResultSet();
                result.absolute(1);
                amount = result.getInt(1);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return amount;
    }

    public double getAverageAge()
    {
        double average = 0;
        if (connection != null)
        {
            try
            {
                String query = "select avg(age) " +
                        "from (select EXTRACT(year FROM CURRENT_DATE) - EXTRACT(year from BIRTH_DATE) as age " +
                        "from PERSON)";
                Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                stm.execute(query);
                ResultSet result = stm.getResultSet();
                result.absolute(1);
                average = result.getDouble(1);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return average;
    }
}
