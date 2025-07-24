package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import database.DatabaseConnection;

@SuppressWarnings("serial")
public class FormLaporKehilangan extends JFrame {
    private JTextField tfNamaBarang, tfLokasi, tfDeskripsi, tfNamaUser, tfNoHp;
    private JButton btnSimpan, btnBatal;

    public FormLaporKehilangan() {
        setTitle("📢 Lapor Barang Hilang");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // UI
        Color primaryColor = new Color(33, 150, 243);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        JPanel panel = new JPanel(new GridLayout(6, 2, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblNamaBarang = new JLabel("Nama Barang:");
        lblNamaBarang.setFont(labelFont);
        tfNamaBarang = new JTextField();
        tfNamaBarang.setFont(fieldFont);

        JLabel lblLokasi = new JLabel("Lokasi Kehilangan:");
        lblLokasi.setFont(labelFont);
        tfLokasi = new JTextField();
        tfLokasi.setFont(fieldFont);

        JLabel lblDeskripsi = new JLabel("Deskripsi:");
        lblDeskripsi.setFont(labelFont);
        tfDeskripsi = new JTextField();
        tfDeskripsi.setFont(fieldFont);

        JLabel lblNoHp = new JLabel("No. HP Pelapor:");
        lblNoHp.setFont(labelFont);
        tfNoHp = new JTextField();
        tfNoHp.setFont(fieldFont);

        JLabel lblNamaUser = new JLabel("Nama Pelapor:");
        lblNamaUser.setFont(labelFont);
        tfNamaUser = new JTextField();
        tfNamaUser.setFont(fieldFont);

        // Auto isi nama jika no HP ditemukan
        tfNoHp.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                String noHp = tfNoHp.getText().trim();
                if (!noHp.isEmpty()) {
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        PreparedStatement stmt = conn.prepareStatement("SELECT nama FROM user WHERE no_hp = ?");
                        stmt.setString(1, noHp);
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            tfNamaUser.setText(rs.getString("nama"));
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "❌ Gagal cek user: " + ex.getMessage());
                    }
                }
            }
        });

        // Tombol
        btnSimpan = new JButton("💾 Simpan");
        btnSimpan.setBackground(primaryColor);
        btnSimpan.setForeground(Color.WHITE);

        btnBatal = new JButton("❌ Batal");
        btnBatal.setBackground(Color.LIGHT_GRAY);

        // Tambahkan ke panel
        panel.add(lblNamaBarang); panel.add(tfNamaBarang);
        panel.add(lblLokasi); panel.add(tfLokasi);
        panel.add(lblDeskripsi); panel.add(tfDeskripsi);
        panel.add(lblNoHp); panel.add(tfNoHp);
        panel.add(lblNamaUser); panel.add(tfNamaUser);
        panel.add(btnSimpan); panel.add(btnBatal);

        add(panel);

        // Aksi Tombol
        btnSimpan.addActionListener(e -> simpanLaporan());
        btnBatal.addActionListener(e -> {
            new MenuUtama();
            dispose();
        });

        setVisible(true);
    }

    private void simpanLaporan() {
        String namaBarang = tfNamaBarang.getText().trim();
        String lokasi = tfLokasi.getText().trim();
        String deskripsi = tfDeskripsi.getText().trim();
        String namaUser = tfNamaUser.getText().trim();
        String noHp = tfNoHp.getText().trim();

        if (namaBarang.isEmpty() || lokasi.isEmpty() || namaUser.isEmpty() || noHp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Harap isi semua data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Tambah user jika belum ada
            PreparedStatement cekUser = conn.prepareStatement("SELECT * FROM user WHERE no_hp = ?");
            cekUser.setString(1, noHp);
            ResultSet rs = cekUser.executeQuery();

            if (!rs.next()) {
                PreparedStatement insertUser = conn.prepareStatement("INSERT INTO user (no_hp, nama) VALUES (?, ?)");
                insertUser.setString(1, noHp);
                insertUser.setString(2, namaUser);
                insertUser.executeUpdate();
            }

            // Simpan laporan barang hilang
            String sql = "INSERT INTO barang_hilang (nama_barang, lokasi_kehilangan, deskripsi, tanggal_hilang, no_hp_pelapor) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertBarang = conn.prepareStatement(sql);
            insertBarang.setString(1, namaBarang);
            insertBarang.setString(2, lokasi);
            insertBarang.setString(3, deskripsi);
            insertBarang.setDate(4, new java.sql.Date(new Date().getTime()));
            insertBarang.setString(5, noHp);

            insertBarang.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Laporan berhasil disimpan!");
            
            // Arahkan ke ListBarangHilang
            new ListBarangHilang();
            dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Gagal menyimpan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}