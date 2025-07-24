package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import database.DatabaseConnection;

@SuppressWarnings({ "unused", "serial" })
public class FormPengambilanBarang extends JFrame {
    private JTextField tfNamaPengambil, tfNpm, tfJam, tfTanggal, tfNoTelp, tfJurusan, tfBarang, tfKodeBarang;
    private JLabel lblPreview;
    private JButton btnSimpan, btnUpload;
    private File selectedFile;

    public FormPengambilanBarang(
            String idBarang,
            String namaBarang,
            String lokasi,
            String deskripsi,
            String namaPelapor,
            String noHpPelapor,
            String tanggalDitemukan,
            ImageIcon gambarBarang) {

        setTitle("📦 Form Pengambilan Barang");
        setSize(500, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label info barang ditemukan, rata tengah
        JLabel labelInfo = new JLabel("🟦 Informasi Barang Ditemukan:");
        labelInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelInfo);
        panel.add(Box.createVerticalStrut(8));

        JLabel labelId = new JLabel("ID Barang: " + idBarang);
        labelId.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelId);

        JLabel labelNama = new JLabel("Nama Barang: " + namaBarang);
        labelNama.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelNama);

        JLabel labelLokasi = new JLabel("Lokasi Ditemukan: " + lokasi);
        labelLokasi.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelLokasi);

        JLabel labelDeskripsi = new JLabel("Deskripsi: " + deskripsi);
        labelDeskripsi.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelDeskripsi);

        JLabel labelPelapor = new JLabel("Nama Pelapor: " + namaPelapor);
        labelPelapor.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelPelapor);

        JLabel labelNoHp = new JLabel("No. HP Pelapor: " + noHpPelapor);
        labelNoHp.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelNoHp);

        JLabel labelTanggal = new JLabel("Tanggal Ditemukan: " + tanggalDitemukan);
        labelTanggal.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelTanggal);

        if (gambarBarang != null) {
            JLabel lblGambarBarang = new JLabel(gambarBarang);
            lblGambarBarang.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalStrut(10));
            panel.add(lblGambarBarang);
        }

        panel.add(Box.createVerticalStrut(20));

        JLabel labelFormPengambil = new JLabel("🟩 Form Data Pengambil:");
        labelFormPengambil.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelFormPengambil);

        // Form input dengan labeledField (rata tengah dan rapi)
        tfNamaPengambil = new JTextField();
        tfNpm = new JTextField();
        tfNoTelp = new JTextField();
        tfJurusan = new JTextField();
        tfJam = new JTextField();
        tfTanggal = new JTextField();
        tfBarang = new JTextField(namaBarang);
        tfKodeBarang = new JTextField(idBarang);
        tfBarang.setEditable(false);
        tfKodeBarang.setEditable(false);

        panel.add(labeledField("Nama Pengambil:", tfNamaPengambil));
        panel.add(labeledField("NPM:", tfNpm));
        panel.add(labeledField("No. Telepon:", tfNoTelp));
        panel.add(labeledField("Jurusan:", tfJurusan));
        panel.add(labeledField("Jam Pengambilan:", tfJam));
        panel.add(labeledField("Tanggal Pengambilan:", tfTanggal));
        panel.add(labeledField("Barang:", tfBarang));
        panel.add(labeledField("Kode Barang:", tfKodeBarang));

        lblPreview = new JLabel("Belum ada gambar", SwingConstants.CENTER);
        lblPreview.setPreferredSize(new Dimension(150, 150));
        lblPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        btnUpload = new JButton("Upload Gambar Pengambilan");
        btnUpload.addActionListener(e -> chooseFile());

        JPanel uploadPanel = new JPanel(new FlowLayout());
        uploadPanel.add(lblPreview);
        uploadPanel.add(btnUpload);
        panel.add(uploadPanel);

        btnSimpan = new JButton("✅ Simpan Pengambilan");
        btnSimpan.setBackground(new Color(76, 175, 80));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSimpan.addActionListener(e -> simpanData());
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnSimpan);

        add(new JScrollPane(panel));
        setVisible(true);
    }

    private JPanel labeledField(String labelText, JTextField field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        p.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(130, 30)); // Lebar fix agar rata kanan
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        field.setMaximumSize(new Dimension(250, 30));  // Lebar fix

        p.add(label);
        p.add(Box.createRigidArea(new Dimension(10, 0)));
        p.add(field);
        return p;
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(new ImageIcon(selectedFile.getAbsolutePath())
                    .getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));
            lblPreview.setIcon(icon);
            lblPreview.setText("");
        }
    }

    private void simpanData() {
        String nama = tfNamaPengambil.getText().trim();
        String npm = tfNpm.getText().trim();
        String telp = tfNoTelp.getText().trim();
        String jurusan = tfJurusan.getText().trim();
        String jam = tfJam.getText().trim();
        String tanggal = tfTanggal.getText().trim();
        String barang = tfBarang.getText().trim();
        int idBarang = Integer.parseInt(tfKodeBarang.getText().trim());

        if (nama.isEmpty() || npm.isEmpty() || telp.isEmpty() || jurusan.isEmpty()
                || jam.isEmpty() || tanggal.isEmpty() || selectedFile == null) {
            JOptionPane.showMessageDialog(this, "⚠ Harap lengkapi semua isian dan upload gambar.");
            return;
        }

        try {
            String uploadsDir = "uploads";
            File destFolder = new File(uploadsDir);
            if (!destFolder.exists()) destFolder.mkdirs();

            Path targetPath = Paths.get(uploadsDir, selectedFile.getName());
            Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            Connection conn = DatabaseConnection.getConnection();

            // Simpan data pengambilan ke history_pengambilan
            String sqlInsert = "INSERT INTO history_pengambilan " +
                    "(nama_pengambil, npm, no_telp, jurusan, jam, tanggal, nama_barang, id_barang, gambar_pengambilan) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setString(1, nama);
            psInsert.setString(2, npm);
            psInsert.setString(3, telp);
            psInsert.setString(4, jurusan);
            psInsert.setString(5, jam);
            psInsert.setString(6, tanggal);
            psInsert.setString(7, barang);
            psInsert.setInt(8, idBarang);
            psInsert.setString(9, selectedFile.getName());
            psInsert.executeUpdate();

            // Update status barang menjadi 'diambil' di tabel barang_ditemukan
            String sqlUpdate = "UPDATE barang_ditemukan SET status = 'diambil' WHERE id = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, idBarang);
            psUpdate.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Data pengambilan berhasil disimpan.");
            dispose();
            new HistoryPengambilanTable().setVisible(true);
        } catch (IOException ioEx) {
            JOptionPane.showMessageDialog(this, "❌ Gagal menyimpan gambar: " + ioEx.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Gagal menyimpan data: " + ex.getMessage());
        }
    }
}
