package database;

import model.BarangDitemukan;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BarangDitemukanDAO {

    public static void tambahBarangDitemukan(BarangDitemukan barang) {
        String sqlUser = "INSERT IGNORE INTO user (no_hp, nama) VALUES (?, ?)";
        String sqlBarang = "INSERT INTO barang_ditemukan (nama_barang, lokasi, deskripsi, tanggal_ditemukan, no_hp_user) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psUser = conn.prepareStatement(sqlUser);
                 PreparedStatement psBarang = conn.prepareStatement(sqlBarang)) {

                // Tambahkan user jika belum ada
                psUser.setString(1, barang.getPelapor().getNoHp());
                psUser.setString(2, barang.getPelapor().getNama());
                psUser.executeUpdate();

                // Tambahkan laporan barang ditemukan
                psBarang.setString(1, barang.getNamaBarang());
                psBarang.setString(2, barang.getLokasi());
                psBarang.setString(3, barang.getDeskripsi());
                psBarang.setDate(4, new java.sql.Date(barang.getTanggalDitemukan().getTime()));
                psBarang.setString(5, barang.getPelapor().getNoHp());

                psBarang.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<BarangDitemukan> getAllBarangDitemukan() {
        List<BarangDitemukan> list = new ArrayList<>();

        String sql = "SELECT bd.*, u.nama FROM barang_ditemukan bd JOIN user u ON bd.no_hp_user = u.no_hp";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String namaBarang = rs.getString("nama_barang");
                String lokasi = rs.getString("lokasi");
                String deskripsi = rs.getString("deskripsi");
                Date tanggal = rs.getDate("tanggal_ditemukan");

                String namaUser = rs.getString("nama");
                String noHp = rs.getString("no_hp_user");

                User pelapor = new User(namaUser, noHp);
                BarangDitemukan barang = new BarangDitemukan(id, namaBarang, lokasi, deskripsi, tanggal, pelapor);
                list.add(barang);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}