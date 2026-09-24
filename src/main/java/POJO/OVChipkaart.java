package main.java.POJO;

import java.time.LocalDate;

public class OVChipkaart {

    private int kaart_nummer;
    private LocalDate geldig_tot;
    private int klasse;
    private double saldo;

    private Reiziger reiziger;

    public OVChipkaart() {
    }

    public OVChipkaart(
            int kaart_nummer,
            LocalDate geldig_tot,
            int klasse,
            double saldo) {

        this.kaart_nummer = kaart_nummer;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
    }

    public OVChipkaart(
            int kaart_nummer,
            LocalDate geldig_tot,
            int klasse,
            double saldo,
            Reiziger reiziger) {

        this.kaart_nummer = kaart_nummer;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }

    public int getKaart_nummer() {
        return kaart_nummer;
    }

    public void setKaart_nummer(
            int kaart_nummer) {

        this.kaart_nummer = kaart_nummer;
    }

    public LocalDate getGeldig_tot() {
        return geldig_tot;
    }

    public void setGeldig_tot(
            LocalDate geldig_tot) {

        this.geldig_tot = geldig_tot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(
            int klasse) {

        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(
            double saldo) {

        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(
            Reiziger reiziger) {

        this.reiziger = reiziger;
    }

    @Override
    public String toString() {

        String reizigerInfo =
                "";

        if (reiziger != null) {

            String naam =
                    reiziger.getVoorletters();

            if (reiziger.getTussenvoegsel() != null &&
                    !reiziger.getTussenvoegsel().isEmpty()) {

                naam +=
                        " " +
                                reiziger.getTussenvoegsel();
            }

            naam +=
                    " " +
                            reiziger.getAchternaam();

            reizigerInfo =
                    ", Reiziger {#" +
                            reiziger.getId() +
                            " " +
                            naam +
                            ", geb. " +
                            reiziger.getGeboortedatum() +
                            "}";
        }

        return "OVChipkaart {#" +
                kaart_nummer +
                ", geldig tot " +
                geldig_tot +
                ", klasse " +
                klasse +
                ", saldo " +
                saldo +
                reizigerInfo +
                "}";
    }
}