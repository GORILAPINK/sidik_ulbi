package database;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/sidiku"; // ganti 'namadb'
    private static final String USER = "root"; // ganti sesuai user mysql kamu
    private static final String PASS = ""; // ganti sesuai password mysql kamu

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}