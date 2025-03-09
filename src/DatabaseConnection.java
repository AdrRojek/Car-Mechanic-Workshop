import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/zakladmechaniki";

    public static Connection Connector(String username, String password) {
        try {

            return DriverManager.getConnection(DATABASE_URL, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}