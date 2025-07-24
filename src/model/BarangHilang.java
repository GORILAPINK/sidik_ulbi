package model;

import java.util.Date;

public class BarangHilang {
    private int id;
    private String namaBarang;
    private String lokasi;
    private String deskripsi;
    private Date tanggalHilang;
    private User pelapor;

    public BarangHilang(int id, String namaBarang, String lokasi, String deskripsi, Date tanggalHilang, User pelapor) {
        this.id = id;
        this.namaBarang = namaBarang;
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
        this.tanggalHilang = tanggalHilang;
        this.pelapor = pelapor;
    }

    public BarangHilang(String namaBarang, String lokasi, String deskripsi, User pelapor) {
        this.namaBarang = namaBarang;
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
        this.tanggalHilang = new Date(); // default
        this.pelapor = pelapor;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public String getNamaBarang() { return namaBarang; }
    public String getLokasiKehilangan() { return lokasi; }
    public String getDeskripsi() { return deskripsi; }
    public Date getTanggalHilang() { return tanggalHilang; }
    public User getPelapor() { return pelapor; }

    public void setTanggalHilang(Date tanggalHilang) {
        this.tanggalHilang = tanggalHilang;
    }
}