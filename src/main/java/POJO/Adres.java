package main.java.POJO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "adres")
public class Adres {

    @Id
    @Column(name = "adres_id")
    private int adres_id;

    @Column(
            name = "postcode",
            nullable = false
    )
    private String postcode;

    @Column(
            name = "huisnummer",
            nullable = false
    )
    private String huisnummer;

    @Column(
            name = "straat",
            nullable = false
    )
    private String straat;

    @Column(
            name = "woonplaats",
            nullable = false
    )
    private String woonplaats;

    @OneToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "reiziger_id",
            unique = true,
            nullable = false
    )
    private Reiziger reiziger;

    public Adres() {
    }

    public Adres(
            int adres_id,
            String postcode,
            String huisnummer,
            String straat,
            String woonplaats) {

        this.adres_id =
                adres_id;

        this.postcode =
                postcode;

        this.huisnummer =
                huisnummer;

        this.straat =
                straat;

        this.woonplaats =
                woonplaats;
    }

    public Adres(
            int adres_id,
            String postcode,
            String huisnummer,
            String straat,
            String woonplaats,
            Reiziger reiziger) {

        this.adres_id =
                adres_id;

        this.postcode =
                postcode;

        this.huisnummer =
                huisnummer;

        this.straat =
                straat;

        this.woonplaats =
                woonplaats;

        this.reiziger =
                reiziger;
    }

    public int getId() {

        return adres_id;
    }

    public int getAdres_id() {

        return adres_id;
    }

    public void setId(
            int id) {

        this.adres_id =
                id;
    }

    public void setAdres_id(
            int adres_id) {

        this.adres_id =
                adres_id;
    }

    public String getPostcode() {

        return postcode;
    }

    public void setPostcode(
            String postcode) {

        this.postcode =
                postcode;
    }

    public String getHuisnummer() {

        return huisnummer;
    }

    public void setHuisnummer(
            String huisnummer) {

        this.huisnummer =
                huisnummer;
    }

    public String getStraat() {

        return straat;
    }

    public void setStraat(
            String straat) {

        this.straat =
                straat;
    }

    public String getWoonplaats() {

        return woonplaats;
    }

    public void setWoonplaats(
            String woonplaats) {

        this.woonplaats =
                woonplaats;
    }

    public Reiziger getReiziger() {

        return reiziger;
    }

    public void setReiziger(
            Reiziger reiziger) {

        this.reiziger =
                reiziger;
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

        return "Adres {#" +
                adres_id +
                ", postcode " +
                postcode +
                ", huisnummer " +
                huisnummer +
                ", straat " +
                straat +
                ", woonplaats " +
                woonplaats +
                reizigerInfo +
                "}";
    }
}