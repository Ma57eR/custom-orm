package orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyConnector {
    private static Connection connection;
    private static final String connectionString = "jdbc:mysql://5.tcp.eu.ngrok.io:18528/";

    private MyConnector() {}

    //Създава конекцията към базата данни
    public static Connection createConnection(String username, String password, String dbName) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);

        connection = DriverManager.getConnection(connectionString + dbName, properties);
        return connection;
    }
    //Взима вече създадената конекция, за да я използва
    public static Connection getConnection() {
        return connection;
    }

}
