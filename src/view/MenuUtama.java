package view;

import javax.swing.*;
import java.awt.*;

// Import kelas lain dari package view
import view.ListBarangDitemukan;
import view.ListBarangHilang;
import view.FormLaporKehilangan;
import view.FormInputPenemuan;
import view.HistoryPengambilanTable ; // Pastikan file ini ada

@SuppressWarnings({ "serial", "unused" })
public class MenuUtama extends JFrame {

    public MenuUtama() {
        setTitle("📦 Aplikasi Pencari Barang Hilang");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Color primaryColor = new Color(33, 150, 243);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 12);

        JLabel label = new JLabel("Selamat Datang", SwingConstants.CENTER);
        label.setFont(titleFont);
        label.setForeground(primaryColor);

        // Tombol utama
        JButton btnLaporHilang = new JButton("📢 Lapor Kehilangan");
        JButton btnInputDitemukan = new JButton("🔍 Input Penemuan");
        JButton btnListHilang = new JButton("📋 List Barang Hilang");
        JButton btnListDitemukan = new JButton("📋 List Barang Ditemukan");
        JButton btnHistoryPengambilan = new JButton("📜 History Pengambilan"); // ✅ Tambahan

        JButton[] buttons = {
            btnLaporHilang, btnInputDitemukan, btnListHilang,
            btnListDitemukan, btnHistoryPengambilan
        };

        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBackground(primaryColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(160, 30));
            btn.setMaximumSize(new Dimension(160, 30));
            btn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        }

        // Action listeners
        btnLaporHilang.addActionListener(e -> {
            dispose();
            new FormLaporKehilangan().setVisible(true);
        });

        btnInputDitemukan.addActionListener(e -> {
            dispose();
            new FormInputPenemuan().setVisible(true);
        });

        btnListHilang.addActionListener(e -> {
            dispose();
            new ListBarangHilang().setVisible(true);
        });

        btnListDitemukan.addActionListener(e -> {
            dispose();
            new ListBarangDitemukan().setVisible(true);
        });

        btnHistoryPengambilan.addActionListener(e -> {
            dispose();
            new HistoryPengambilan().setVisible(true); // ✅ Terhubung ke form riwayat
        });

        // Panel tombol
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.add(btnLaporHilang);
        buttonPanel.add(btnInputDitemukan);
        buttonPanel.add(btnListHilang);
        buttonPanel.add(btnListDitemukan);
        buttonPanel.add(btnHistoryPengambilan);

        // Layout utama
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.add(label, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuUtama::new);
    }
}
