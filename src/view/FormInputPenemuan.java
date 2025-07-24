package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.Date;

import database.DatabaseConnection;

@SuppressWarnings("serial")
public class FormInputPenemuan extends JFrame {
    private JTextField tfNamaBarang, tfLokasi, tfDeskripsi, tfNamaUser, tfNoHp;
    private JButton btnSimpan, btnBatal, btnPilihGambar;
    private JLabel labelPreviewGambar;
    private String pathGambar = null;

    public FormInputPenemuan() {
        setTitle("🔍 sidikulbi");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color primaryColor = new Color(33, 150, 243);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        JPanel panel = new JPanel(new GridLayout(8, 2, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblNamaBarang = new JLabel("Nama Barang:");
        lblNamaBarang.setFont(labelFont);
        tfNamaBarang = new JTextField(); tfNamaBarang.setFont(fieldFont);

        JLabel lblLokasi = new JLabel("Lokasi Penemuan:");
        lblLokasi.setFont(labelFont);
        tfLokasi = new JTextField(); tfLokasi.setFont(fieldFont);

        JLabel lblDeskripsi = new JLabel("Deskripsi:");
        lblDeskripsi.setFont(labelFont);
        tfDeskripsi = new JTextField(); tfDeskripsi.setFont(fieldFont);

        JLabel lblNoHp = new JLabel("No. HP Pelapor:");
        lblNoHp.setFont(labelFont);
        tfNoHp = new JTextField(); tfNoHp.setFont(fieldFont);

        JLabel lblNamaUser = new JLabel("Nama Pelapor:");
        lblNamaUser.setFont(labelFont);
        tfNamaUser = new JTextField(); tfNamaUser.setFont(fieldFont);

        // ====== Upload Gambar =======
        JLabel lblGambar = new JLabel("Gambar Barang:");
        lblGambar.setFont(labelFont);

        btnPilihGambar = new JButton("📷 Pilih Gambar");
        labelPreviewGambar = new JLabel("Belum ada gambar");
        labelPreviewGambar.setHorizontalAlignment(SwingConstants.CENTER);
        labelPreviewGambar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        labelPreviewGambar.setPreferredSize(new Dimension(100, 100));

        btnPilihGambar.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                pathGambar = file.getAbsolutePath();

                ImageIcon icon = new ImageIcon(new ImageIcon(pathGambar).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                labelPreviewGambar.setIcon(icon);
                labelPreviewGambar.setText(""); // clear default text
            }
        });
        // ==============================

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
                        JOptionPane.showMessageDialog(null, "❌ Gagal mengambil data user: " + ex.getMessage());
                    }
                }
            }
        });

        btnSimpan = new JButton("💾 Simpan");
        btnSimpan.setBackground(primaryColor); btnSimpan.setForeground(Color.WHITE);

        btnBatal = new JButton("❌ Batal");
        btnBatal.setBackground(Color.LIGHT_GRAY);

        panel.add(lblNamaBarang); panel.add(tfNamaBarang);
        panel.add(lblLokasi); panel.add(tfLokasi);
        panel.add(lblDeskripsi); panel.add(tfDeskripsi);
        panel.add(lblNoHp); panel.add(tfNoHp);
        panel.add(lblNamaUser); panel.add(tfNamaUser);
        panel.add(lblGambar); panel.add(btnPilihGambar);
        panel.add(new JLabel("Preview:")); panel.add(labelPreviewGambar);
        panel.add(btnSimpan); panel.add(btnBatal);

        add(panel);

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
            JOptionPane.showMessageDialog(this, "⚠️ Harap lengkapi semua kolom!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement cekUser = conn.prepareStatement("SELECT * FROM user WHERE no_hp = ?");
            cekUser.setString(1, noHp);
            ResultSet rs = cekUser.executeQuery();

            if (!rs.next()) {
                PreparedStatement insertUser = conn.prepareStatement("INSERT INTO user (no_hp, nama) VALUES (?, ?)");
                insertUser.setString(1, noHp);
                insertUser.setString(2, namaUser);
                insertUser.executeUpdate();
            }

            PreparedStatement insertBarang = conn.prepareStatement(
                "INSERT INTO barang_ditemukan (nama_barang, lokasi, deskripsi, tanggal_ditemukan, no_hp_user, gambar) VALUES (?, ?, ?, ?, ?, ?)"
            );
            insertBarang.setString(1, namaBarang);
            insertBarang.setString(2, lokasi);
            insertBarang.setString(3, deskripsi);
            insertBarang.setDate(4, new java.sql.Date(new Date().getTime()));
            insertBarang.setString(5, noHp);
            insertBarang.setString(6, pathGambar != null ? pathGambar : "");
            insertBarang.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Data barang ditemukan berhasil disimpan.");
            new ListBarangDitemukan();
            dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Gagal menyimpan ke database:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}