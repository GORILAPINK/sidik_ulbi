package database;

import model.BarangHilang;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BarangHilangDAO {

    public static void tambahBarangHilang(BarangHilang barang) {
        String sqlUser = "INSERT IGNORE INTO user (no_hp, nama) VALUES (?, ?)";
        String sqlBarang = "INSERT INTO barang_hilang (nama_barang, lokasi_kehilangan, deskripsi, tanggal_hilang, no_hp_pelapor) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psUser = conn.prepareStatement(sqlUser);
                 PreparedStatement psBarang = conn.prepareStatement(sqlBarang)) {

                // Simpan user jika belum ada
                psUser.setString(1, barang.getPelapor().getNoHp());
                psUser.setString(2, barang.getPelapor().getNama());
                psUser.executeUpdate();

                // Simpan barang
                psBarang.setString(1, barang.getNamaBarang());
                psBarang.setString(2, barang.getLokasiKehilangan());
                psBarang.setString(3, barang.getDeskripsi());
                psBarang.setDate(4, new java.sql.Date(barang.getTanggalHilang().getTime()));
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

    public static List<BarangHilang> getAllBarangHilang() {
        List<BarangHilang> list = new ArrayList<>();
        String sql = "SELECT bh.*, u.nama FROM barang_hilang bh JOIN user u ON bh.no_hp_pelapor = u.no_hp";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String namaBarang = rs.getString("nama_barang");
                String lokasi = rs.getString("lokasi_kehilangan");
                String deskripsi = rs.getString("deskripsi");
                Date tanggal = rs.getDate("tanggal_hilang");

                String namaPelapor = rs.getString("nama");
                String noHp = rs.getString("no_hp_pelapor");

                User user = new User(namaPelapor, noHp);
                BarangHilang barang = new BarangHilang(id, namaBarang, lokasi, deskripsi, tanggal, user);
                list.add(barang);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ Tambahan: Hapus laporan barang hilang berdasarkan ID
    public static boolean deleteBarangHilang(int id) {
        String sql = "DELETE FROM barang_hilang WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Gagal menghapus data barang hilang: " + e.getMessage());
            return false;
        }
    }
}
