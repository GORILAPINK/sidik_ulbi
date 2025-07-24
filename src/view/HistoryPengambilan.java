package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import database.DatabaseConnection;

public class HistoryPengambilan extends JFrame {
    private static final long serialVersionUID = 1L;

    public HistoryPengambilan() {
        setTitle("📦 Riwayat Pengambilan Barang");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] kolom = {
            "Nama Pengambil", "NPM", "Jam", "Tanggal", "No Telp", "Jurusan", "Nama Barang", "ID Barang", "Gambar"
        };

        DefaultTableModel model = new DefaultTableModel(null, kolom) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 9) return ImageIcon.class; // kolom gambar
                return String.class;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(80);
        table.setAutoCreateRowSorter(true); // ✅ Diletakkan di luar while-loop

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM history_pengambilan")) {

            while (rs.next()) {
                String nama = rs.getString("nama_pengambil");
                String npm = rs.getString("npm");
                String jam = rs.getString("jam");

                java.sql.Date tanggalRaw = rs.getDate("tanggal");
                String tanggal = new java.text.SimpleDateFormat("dd-MM-yyyy").format(tanggalRaw);

                String telp = rs.getString("no_telp");
                String jurusan = rs.getString("jurusan");
                String namaBarang = rs.getString("nama_barang");
                int idBarang = rs.getInt("id_barang");
                String namaGambar = rs.getString("gambar_pengambilan");

                ImageIcon icon = null;
                if (namaGambar != null && !namaGambar.isEmpty()) {
                    File imgFile = new File("uploads/" + namaGambar);
                    if (imgFile.exists()) {
                        icon = new ImageIcon(new ImageIcon(imgFile.getAbsolutePath())
                                .getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
                    }
                }

                model.addRow(new Object[]{
                    nama, npm, jam, tanggal, telp, jurusan, namaBarang, idBarang, icon
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil data: " + ex.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnKembali = new JButton("Kembali");
        btnKembali.setBackground(new Color(33, 150, 243));
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.setPreferredSize(new Dimension(100, 35));
        btnKembali.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        bottomPanel.add(btnKembali);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.add(new JLabel("Riwayat Pengambilan", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }
}
