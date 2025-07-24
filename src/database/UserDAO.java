package database;

import model.User;

import java.sql.*;

public class UserDAO {

    public User getUserByNoHp(String noHp) {
        String sql = "SELECT nama, no_hp FROM user WHERE no_hp = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, noHp);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("nama"), rs.getString("no_hp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertUser(User user) {
        String sql = "INSERT INTO user (nama, no_hp) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getNama());
            stmt.setString(2, user.getNoHp());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}