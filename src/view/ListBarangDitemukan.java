package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import model.BarangDitemukan;
import database.BarangDitemukanDAO;
import database.DatabaseConnection;
import java.sql.*;
import java.nio.file.*;

@SuppressWarnings("unused")
public class ListBarangDitemukan extends JFrame {
    private static final long serialVersionUID = 1L;

    public ListBarangDitemukan() {
        setTitle("\uD83D\uDCCB Daftar Barang Ditemukan");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Daftar Barang Ditemukan", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(33, 150, 243));

        String[] kolom = {
            "ID", "Nama Barang", "Lokasi", "Deskripsi",
            "Nama Pelapor", "No. HP Pelapor", "Tanggal Ditemukan",
            "Gambar", "Aksi"
        };

        DefaultTableModel model = new DefaultTableModel(null, kolom) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 7 ? ImageIcon.class : Object.class;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(60);

        List<BarangDitemukan> daftar = BarangDitemukanDAO.getAllBarangDitemukan();
        for (BarangDitemukan b : daftar) {
            Object gambarStr = b.getGambar();
            ImageIcon icon = null;
            if (gambarStr != null && !((String) gambarStr).isEmpty()) {
                File imgFile = new File("uploads/" + gambarStr);
                if (imgFile.exists()) {
                    icon = new ImageIcon(new ImageIcon(imgFile.getAbsolutePath())
                            .getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
                }
            }

            model.addRow(new Object[]{
                b.getId(),
                b.getNamaBarang(),
                b.getLokasi(),
                b.getDeskripsi(),
                b.getPelapor() != null ? b.getPelapor().getNama() : "",
                b.getPelapor() != null ? b.getPelapor().getNoHp() : "",
                b.getTanggalDitemukan() != null ? b.getTanggalDitemukan().toString() : "",
                icon,
                "Ambil"
            });
        }

        table.getColumn("Aksi").setCellRenderer(new ButtonRenderer());
        table.getColumn("Aksi").setCellEditor(new ButtonEditor(new JCheckBox(), model));

        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnKembali = new JButton("Kembali");
        btnKembali.setBackground(new Color(33, 150, 243));
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.setPreferredSize(new Dimension(100, 35));
        btnKembali.addActionListener(e -> {
            dispose();
            new MenuUtama().setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(btnKembali);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value == null ? "Ambil" : value.toString());
            setBackground(new Color(76, 175, 80));
            setForeground(Color.WHITE);
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private boolean clicked;
        private final DefaultTableModel tableModel;
        private int selectedRow;
        private String label;

        public ButtonEditor(JCheckBox checkBox, DefaultTableModel model) {
            super(checkBox);
            this.tableModel = model;
            this.button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(76, 175, 80));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int row, int column) {
            label = (value == null) ? "Ambil" : value.toString();
            button.setText(label);
            clicked = true;
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                int idBarang = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                String nama = tableModel.getValueAt(selectedRow, 1).toString();
                String lokasi = tableModel.getValueAt(selectedRow, 2).toString();
                String deskripsi = tableModel.getValueAt(selectedRow, 3).toString();
                String pelapor = tableModel.getValueAt(selectedRow, 4).toString();
                String noHp = tableModel.getValueAt(selectedRow, 5).toString();
                String tanggal = tableModel.getValueAt(selectedRow, 6).toString();
                ImageIcon gambar = (ImageIcon) tableModel.getValueAt(selectedRow, 7);

                SwingUtilities.invokeLater(() -> {
                    new FormPengambilanBarang(
                        String.valueOf(idBarang), nama, lokasi, deskripsi,
                        pelapor, noHp, tanggal, gambar
                    );
                });
            }
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
