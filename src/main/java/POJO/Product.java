package main.java.POJO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "product_nummer")
    private int product_nummer;

    @Column(name = "naam", nullable = false)
    private String naam;

    @Column(name = "beschrijving")
    private String beschrijving;

    @Column(name = "prijs", nullable = false)
    private double prijs;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ov_chipkaart_product",
            joinColumns = @JoinColumn(name = "product_nummer"),
            inverseJoinColumns = @JoinColumn(name = "kaart_nummer")
    )
    private List<OVChipkaart> ovChipkaarten =
            new ArrayList<>();

    public Product() {
    }

    public Product(
            int product_nummer,
            String naam,
            String beschrijving,
            double prijs) {

        this.product_nummer = product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public int getProduct_nummer() {
        return product_nummer;
    }

    public void setProduct_nummer(int product_nummer) {
        this.product_nummer = product_nummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    public void setOvChipkaarten(List<OVChipkaart> ovChipkaarten) {

        this.ovChipkaarten =
                new ArrayList<>();

        if (ovChipkaarten != null) {

            for (OVChipkaart ovChipkaart :
                    ovChipkaarten) {

                addOVChipkaart(
                        ovChipkaart
                );
            }
        }
    }

    public boolean addOVChipkaart(OVChipkaart ovChipkaart) {

        if (ovChipkaart == null) {
            return false;
        }

        if (ovChipkaarten.contains(ovChipkaart)) {
            return false;
        }

        boolean toegevoegd =
                ovChipkaarten.add(
                        ovChipkaart
                );

        if (toegevoegd &&
                !ovChipkaart.getProducten().contains(this)) {

            ovChipkaart.addProduct(
                    this
            );
        }

        return toegevoegd;
    }

    public boolean removeOVChipkaart(OVChipkaart ovChipkaart) {

        if (ovChipkaart == null) {
            return false;
        }

        boolean verwijderd =
                ovChipkaarten.remove(
                        ovChipkaart
                );

        if (verwijderd &&
                ovChipkaart.getProducten().contains(this)) {

            ovChipkaart.removeProduct(
                    this
            );
        }

        return verwijderd;
    }

    @Override
    public String toString() {

        StringBuilder kaartenInfo =
                new StringBuilder();

        if (ovChipkaarten != null &&
                !ovChipkaarten.isEmpty()) {

            kaartenInfo.append(
                    ", OVChipkaarten ["
            );

            for (int i = 0;
                 i < ovChipkaarten.size();
                 i++) {

                OVChipkaart ovChipkaart =
                        ovChipkaarten.get(i);

                kaartenInfo.append("#")
                        .append(
                                ovChipkaart.getKaart_nummer()
                        );

                if (i < ovChipkaarten.size() - 1) {
                    kaartenInfo.append(", ");
                }
            }

            kaartenInfo.append("]");
        }

        return "Product {#" +
                product_nummer +
                ", naam " +
                naam +
                ", beschrijving " +
                beschrijving +
                ", prijs " +
                prijs +
                kaartenInfo +
                "}";
    }
}