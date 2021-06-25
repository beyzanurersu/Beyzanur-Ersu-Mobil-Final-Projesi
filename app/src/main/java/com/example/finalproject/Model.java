package com.example.finalproject;


import java.io.Serializable;
import java.util.List;

public class Model implements Serializable {

    String alisveris_yeri;
    String not;
    String tamamlandimi;
    String tarih;
    List<Urun> urunler;


    public Model() {
    }

    public Model(String alisveris_yeri, String not, String tamamlandimi, String tarih, List<Urun> urunler) {

        this.alisveris_yeri = alisveris_yeri;

        this.not = not;

        this.tamamlandimi = tamamlandimi;

        this.tarih = tarih;

        this.urunler = urunler;

    }

    public void setAlisveris_yeri(String alisveris_yeri) {

        this.alisveris_yeri = alisveris_yeri;

    }

    public String getAlisveris_yeri() {

        return this.alisveris_yeri;

    }

    public void setNot(String not) {

        this.not = not;

    }

    public String getNot() {

        return this.not;

    }

    public void setTamamlandimi(String tamamlandimi) {

        this.tamamlandimi = tamamlandimi;

    }

    public String getTamamlandimi() {

        return this.tamamlandimi;

    }

    public void setTarih(String tarih) {

        this.tarih = tarih;

    }

    public String getTarih() {

        return this.tarih;

    }

    public void setUrunler(List<Urun> urunler) {

        this.urunler = urunler;

    }

    public List<Urun> getUrunler() {

        return this.urunler;

    }

    public static class Urun implements Serializable{

        String adet;
        String alindimi;
        String isim;


        public Urun() {
        }

        public Urun(String adet, String alindimi, String isim) {

            this.adet = adet;

            this.alindimi = alindimi;

            this.isim = isim;

        }

        public void setAdet(String adet) {

            this.adet = adet;

        }

        public String getAdet() {

            return this.adet;

        }

        public void setAlindimi(String alindimi) {

            this.alindimi = alindimi;

        }

        public String getAlindimi() {

            return this.alindimi;

        }

        public void setIsim(String isim) {

            this.isim = isim;

        }

        public String getIsim() {

            return this.isim;

        }

    }

}


