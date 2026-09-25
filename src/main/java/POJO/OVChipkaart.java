package main.java.POJO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {

    private int kaart_nummer;
    private LocalDate geldig_tot;
    private int klasse;
    private double saldo;

    private Reiziger reiziger;

    private List<Product> producten =
            new ArrayList<>();

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

    public List<Product> getProducten() {
        return producten;
    }

    public void setProducten(
            List<Product> producten) {

        this.producten =
                new ArrayList<>();

        if (producten != null) {

            for (Product product :
                    producten) {

                addProduct(
                        product
                );
            }
        }
    }

    public boolean addProduct(
            Product product) {

        if (product == null) {
            return false;
        }

        if (producten.contains(
                product)) {

            return false;
        }

        boolean toegevoegd =
                producten.add(
                        product
                );

        if (toegevoegd &&
                !product
                        .getOvChipkaarten()
                        .contains(this)) {

            product.addOVChipkaart(
                    this
            );
        }

        return toegevoegd;
    }

    public boolean removeProduct(
            Product product) {

        if (product == null) {
            return false;
        }

        boolean verwijderd =
                producten.remove(
                        product
                );

        if (verwijderd &&
                product
                        .getOvChipkaarten()
                        .contains(this)) {

            product.removeOVChipkaart(
                    this
            );
        }

        return verwijderd;
    }

    @Override
    public String toString() {

        String reizigerInfo =
                "";

        if (reiziger != null) {

            reizigerInfo =
                    ", reiziger_id " +
                            reiziger.getId();
        }

        StringBuilder productenInfo =
                new StringBuilder();

        if (producten != null &&
                !producten.isEmpty()) {

            productenInfo.append(
                    ", Producten ["
            );

            for (int i = 0;
                 i < producten.size();
                 i++) {

                Product product =
                        producten.get(i);

                productenInfo.append(
                        "#"
                );

                productenInfo.append(
                        product.getProduct_nummer()
                );

                productenInfo.append(
                        " "
                );

                productenInfo.append(
                        product.getNaam()
                );

                if (i <
                        producten.size() - 1) {

                    productenInfo.append(
                            ", "
                    );
                }
            }

            productenInfo.append(
                    "]"
            );
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
                productenInfo +
                "}";
    }
}