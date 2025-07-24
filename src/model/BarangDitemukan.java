package model;

import java.util.Date;

public class BarangDitemukan {
    private int id;
    private String namaBarang;
    private String lokasi;
    private String deskripsi;
    private Date tanggalDitemukan;
    private User pelapor;

    public BarangDitemukan(int id, String namaBarang, String lokasi, String deskripsi, Date tanggalDitemukan, User pelapor) {
        this.id = id;
        this.namaBarang = namaBarang;
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
        this.tanggalDitemukan = tanggalDitemukan;
        this.pelapor = pelapor;
    }

    public BarangDitemukan(String namaBarang, String lokasi, String deskripsi, User pelapor) {
        this.namaBarang = namaBarang;
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
        this.tanggalDitemukan = new Date(); // default: hari ini
        this.pelapor = pelapor;
    }

    // Getter
    public int getId() { return id; }
    public String getNamaBarang() { return namaBarang; }
    public String getLokasi() { return lokasi; }
    public String getDeskripsi() { return deskripsi; }
    public Date getTanggalDitemukan() { return tanggalDitemukan; }
    public User getPelapor() { return pelapor; }

	public Object getGambar() {
		// TODO Auto-generated method stub
		return null;
	}
}