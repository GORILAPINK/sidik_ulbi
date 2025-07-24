package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.BarangHilang;
import database.BarangHilangDAO;

public class ListBarangHilang extends JFrame {
    private static final long serialVersionUID = 1L;

    public ListBarangHilang() {
        setTitle("📋 Daftar Barang Hilang");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Judul
        JLabel label = new JLabel("Daftar Laporan Barang Hilang", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(33, 150, 243));

        // Kolom tabel
        String[] kolom = { "Nama Barang", "Lokasi Kehilangan", "Deskripsi", "Nama Pelapor", "No. HP", "Tanggal" };
        DefaultTableModel tableModel = new DefaultTableModel(kolom, 0);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        // Ambil data dari database
        List<BarangHilang> daftar = BarangHilangDAO.getAllBarangHilang();
        for (BarangHilang b : daftar) {
            tableModel.addRow(new Object[] {
                b.getNamaBarang(),
                b.getLokasiKehilangan(),
                b.getDeskripsi(),
                b.getPelapor() != null ? b.getPelapor().getNama() : "",
                b.getPelapor() != null ? b.getPelapor().getNoHp() : "",
                b.getTanggalHilang() != null ? b.getTanggalHilang().toString() : ""
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);

        // Tombol Kembali
        JButton btnKembali = new JButton("Kembali");
        btnKembali.setBackground(new Color(33, 150, 243));
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.setPreferredSize(new Dimension(100, 35));
        btnKembali.addActionListener(e -> {
            dispose();
            new MenuUtama().setVisible(true);
        });

        // Panel tombol bawah
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(btnKembali);

        // Panel utama
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }
}